/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package th.co.truecorp.catalogue.prepaid.config;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import th.co.truecorp.catalogue.prepaid.kafka.dto.Offer;

/**
 *
 * @author Sorawe3
 */

@Configuration
@EnableKafka
public class KafkaConsumerConfig {
    
    @Value("${spring.kafka.consumer.bootstrap-servers}")
    private String bootstrapServers;
    
    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;
    
    @Value("${spring.kafka.properties.security.protocol}")
    private String securityProtocol;
    
    @Value("${spring.kafka.properties.ssl.truststore.location}")
    private String sslTruststoreLocation;
    
    @Value("${spring.kafka.properties.ssl.truststore.password}")
    private String sslTruststorePassword;
    
    @Value("${spring.kafka.properties.sasl.mechanism}")
    private String saslMechanism;
    
    @Value("${spring.kafka.properties.sasl.kerberos.service.name}")
    private String saslKerberosServiceName;
    
    @Bean(name = "KafkaConsumerConfig")
    public Map<String, Object> kafkaConsumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        if ("SASL_SSL".equals(securityProtocol)) {
            System.setProperty("java.security.auth.login.config", "./keytest/client_jaas.conf");
            System.setProperty("java.security.krb5.conf", "./keytest/krb5.conf");
            
            props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_SSL");
            props.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, sslTruststoreLocation);
            props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, sslTruststorePassword);
            props.put("sasl.mechanism", saslMechanism);
            props.put("sasl.kerberos.service.name", saslKerberosServiceName);
        } else if ("SSL".equals(securityProtocol)) {
            props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SSL");
            props.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, sslTruststoreLocation);
            props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, sslTruststorePassword);
        }
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, JsonDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());
        
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 100);
        props.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, 5000);
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 25000);        
        return props;
    }
    
    @Bean(name = "kafkaConsumerFactory")
    public ConsumerFactory<String, Offer> kafkaConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                kafkaConsumerConfigs(),
                new StringDeserializer(),
                new JsonDeserializer<>(Offer.class)
        );
    }
        
    @Bean(name = "kafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, Offer> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Offer> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConcurrency(1);
        factory.setConsumerFactory(kafkaConsumerFactory());
        factory.getContainerProperties().setPollTimeout(3000);
        // idle interval  ----  60000L = 5 mins
        factory.getContainerProperties().setIdleEventInterval(360000L);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        
        return factory;
    }
 }
