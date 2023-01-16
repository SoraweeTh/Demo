/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package th.co.truecorp.catalogue.prepaid.kafka.dto;

import java.util.Date;
import java.util.Map;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 *
 * @author Sorawe3
 */

@Data
@NoArgsConstructor
@Getter
public class OfferItem {
    
    private String id;
    private String code;
    private String name;
    private String description;
    private Date versionEffDate;
    private Date versionExpDate;
    private Map<String, String> offerItemProperties;
    private Map<String, String> offerItemParam;
}
