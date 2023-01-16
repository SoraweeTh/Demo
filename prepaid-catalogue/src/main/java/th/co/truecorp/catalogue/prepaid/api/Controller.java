/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package th.co.truecorp.catalogue.prepaid.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.awt.event.ActionEvent;
import javax.swing.Timer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import th.co.truecorp.catalogue.prepaid.jpa.repo.LogTableRepo;
import th.co.truecorp.catalogue.prepaid.jpa.repo.OfferItemRepo;
import th.co.truecorp.catalogue.prepaid.jpa.repo.OfferParamRepo;
import th.co.truecorp.catalogue.prepaid.jpa.repo.OfferRelationRepo;
import th.co.truecorp.catalogue.prepaid.jpa.repo.OfferRepo;
import th.co.truecorp.catalogue.prepaid.model.BodyToken;
import th.co.truecorp.catalogue.prepaid.service.PrepaidCatalogService;

/**
 *
 * @author Sorawe3
 */

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1")
public class Controller {
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private WebClient webClient;
    
    @Autowired
    private OfferRepo offerRepo;

    @Autowired
    private OfferItemRepo offerItemRepo;

    @Autowired
    private OfferParamRepo offerParamRepo;

    @Autowired
    private OfferRelationRepo offerRelationRepo;
    
    @Autowired
    private LogTableRepo logTableRepo;
    
    @Autowired
    private PrepaidCatalogService prepaidCatalogService;

    @GetMapping("/get-token/fetchAll")
    public ResponseEntity<?> getAccessTokenThenFetchAll() throws JsonProcessingException {
        
        if (!logTableRepo.checkProcessingRemark().isEmpty()) {
            
            return new ResponseEntity<>("Convert-all process is still running!!!", HttpStatus.NOT_FOUND);
            
        } else {
            
            offerRepo.truncateTable();
            offerItemRepo.truncateTable();
            offerParamRepo.truncateTable();
            offerRelationRepo.truncateTable();
            
            prepaidCatalogService.startProcess();
            
            String API_GET_TOKEN = "https://iam-uat.truecorp.co.th/auth/token";
            String API_FETCHALL = "https://cat-uat2.true.th/api/v1/fetchAll";
            
            MultiValueMap<String, String> bodyGetToken = new LinkedMultiValueMap<>();
            bodyGetToken.add("grant_type", "client_credentials");
            bodyGetToken.add("client_id", "ITDSCMC-EPCBPT");
            bodyGetToken.add("client_secret", "24a4b555-66a4-44c6-a6fe-6df629941d90");
            
            String responseToken = webClient.post()
                    .uri(API_GET_TOKEN)
                    .body(BodyInserters.fromFormData(bodyGetToken))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            BodyToken bodyToken = objectMapper.readValue(responseToken, BodyToken.class);
            System.out.println("access_token =>  " + bodyToken.getAccess_token());
            
            if (bodyToken.getAccess_token().equalsIgnoreCase("")) {
                return new ResponseEntity<>("Token not generated for fetchAll action..!", HttpStatus.BAD_REQUEST);
            } else {
            
                MultiValueMap<String, String> bodyFetchAll = new LinkedMultiValueMap<>();
                bodyFetchAll.add("uuid", "SOI_FetchAll");
                bodyFetchAll.add("isActive", "true");
                webClient.post()
                        .uri(API_FETCHALL)
                        .header("Authorization", "Bearer " + bodyToken.getAccess_token())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .accept(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromFormData(bodyFetchAll))
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();
                
                return new ResponseEntity<>("fetchAll successful..!!", HttpStatus.OK);
            }
        }
    }
    
    // api =>> http://localhost:8855/api/v1/endProcess
    @GetMapping("/endProcess")
    public ResponseEntity<?> endProcessCat() throws InterruptedException {
        System.out.println("::: delete processing log ::: insert end process log ::: ");
        prepaidCatalogService.endProcess();
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    
    // api ==> http://localhost:8855/api/v1/changeStatus
    @GetMapping("/changeStatus")
    public ResponseEntity<?> changingStatus() {
        if (logTableRepo.checkProcessingRemark().isEmpty()) {
            System.out.println(":: check processing log from GUI ::");
            return new ResponseEntity<>("OK", HttpStatus.OK);
        }
        return new ResponseEntity<>("UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
    }
}
