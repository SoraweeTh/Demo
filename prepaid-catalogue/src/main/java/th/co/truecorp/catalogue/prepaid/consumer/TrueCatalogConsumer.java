/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package th.co.truecorp.catalogue.prepaid.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.event.ListenerContainerIdleEvent;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import th.co.truecorp.catalogue.prepaid.jpa.repo.LogTableRepo;
import th.co.truecorp.catalogue.prepaid.kafka.dto.Offer;
import th.co.truecorp.catalogue.prepaid.service.PrepaidCatalogService;

/**
 *
 * @author Sorawe3
 */

@Slf4j
@Component
public class TrueCatalogConsumer {
    
    @Autowired
    private WebClient webClient;
    
    @Autowired
    private LogTableRepo logTableRepo;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private PrepaidCatalogService prepaidCatalogService;
    
    public static final String ACTION_FETCHALL = "fetchAll";
    public static final String ACTION_UPSERT = "upsert";
    public static final String ACTION_VERSIONEXP = "VersionExpireHistory";
    
    public static final String UUID_TO_CONSUME = "SOI_FetchAll";
        
    @KafkaListener(
            id = "epcbpt_lookup_cat",
            topics = "${topic.name}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(
            Offer payload,
            @Header("action") String action,
            @Header("typeName") String typeName,
            @Header("uuid") String uuid,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.OFFSET) Integer offset,
            Acknowledgment ack) {
        String msgPayload = "";
        boolean loadFlag = false;
        
        try {
            msgPayload = objectMapper.writeValueAsString(payload); 
            System.out.println("KafkaListener start comsume ........ ");
            if (ACTION_FETCHALL.equalsIgnoreCase(action)) {
                if (uuid != null && uuid.equalsIgnoreCase(UUID_TO_CONSUME)) {
                    loadFlag = true;
                }
            } else {
                ///////////////
            } 
            
            if (loadFlag) {
                prepaidCatalogService.saveOfferPayload(payload, offset);
            }
            
        } catch (Exception e) {
            log.error("[CAT_EXCEPTION] payload.name={}, payload.id={}, offset={}, e={}, errorMsg={}", 
                    payload.getName(), payload.getId(), offset, e, e.getMessage());
        } finally {
            ack.acknowledge();
            log.info("[CAT_OFFER] topic: {} uuid: {} offset: {} action: {} typeName: {} payload: {}",
                    topic, uuid, offset, action, typeName, msgPayload);
        }
    }
    
    @EventListener(
            condition = "event.listenerId.startsWith('epcbpt_lookup_cat')"
    )
    public void idleEventHandler(ListenerContainerIdleEvent event) {        
        System.out.println("Event Listener Idle Detecting .............");
        log.info("Idle Event Handler Received Message @ :: " + LocalDateTime.now());
        // log.info("Get idle time :: " + event.getIdleTime());
        if (!logTableRepo.checkProcessingRemark().isEmpty()) {
            webClient.get()
                    .uri("http://localhost:8080/api/v1/endProcess")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            System.out.println("checked --> processing log found :: calling api to end process log ::");
        } else {
            System.out.println("checked --> processing log not found :: continue checking as idle listener ::");
        }
    }
    
//    @Override
//    public void onIdleContainer(Map<TopicPartition, Long> assignments, ConsumerSeekCallback callback) {
//        System.out.println("Kafka idle container start ::  ");
//
//        Timer timer = new Timer(1800000, (ActionEvent e) -> { 
//            if (!logTableRepo.checkProcessingRemark().isEmpty()) {
//                System.out.println("processing found in log == end and deleting ....... ");
//                webClient.get()
//                    .uri("http://localhost:8080/api/v1/endProcess")
//                    .retrieve()
//                    .bodyToMono(String.class)
//                    .block();
//            } else {
//                System.out.println("no processing in log ....... ");
//            }
//        });
//        timer.setRepeats(false);
//        timer.start();
        
//        if (!logTableRepo.checkProcessingRemark().isEmpty()) {
//            webClient.get()
//                    .uri("http://localhost:8080/api/v1/endProcess")
//                    .retrieve()
//                    .bodyToMono(String.class)
//                    .block();
//            System.out.println("processing in log table ..... ");
//        } else {
//            System.out.println("no processing in log table .....");
//        }
//    }
}
