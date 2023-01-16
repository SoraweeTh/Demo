/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package th.co.truecorp.catalogue.prepaid.kafka.dto;

import java.util.Date;
import java.util.List;
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
public class ChildOffer {
    
    private String childOfferId;
    private String code;
    private String name;
    private String description;
    private String isActive;
    private String productType;
    private String relationType;
    private String saleContext;
    private Date saleEffDate;
    private Date saleExpDate;
    private Boolean selectedByDefault;
    private String type;
    private String typeDesc;
    private String urNo;
    private Date versionEffDate;
    private Date versionExpDate;
    private Map<String, String> properties;
    private List<OfferItem> offerItem;
}
