/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package th.co.truecorp.catalogue.prepaid.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 *
 * @author Sorawe3
 */

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BodyToken {
    
    private String access_token;
    private String expires_in;
    private String refresh_expires_in;
    private String refresh_token;
    private String token_type;
    private String session_state;
    private String scope;
    
}
