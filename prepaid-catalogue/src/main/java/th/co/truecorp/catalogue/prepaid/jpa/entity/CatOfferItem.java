/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package th.co.truecorp.catalogue.prepaid.jpa.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import th.co.truecorp.catalogue.prepaid.jpa.entity.id.IdOfferItem;

/**
 *
 * @author Sorawe3
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CAT_OFFER_ITEM")
@IdClass(IdOfferItem.class)
public class CatOfferItem {
    
    @Column(name = "OFFER_VERSION_ID")
    private String offerId;
    
    @Column(name = "ITEM_VERSION_ID")
    private String itemId;
	
    @Id
    @Column(name = "OFFERCODE")
    private String code;
    
    @Id
    @Column(name = "OFFERITEM")
    private String itemCode;
	
    @Column(name = "ITEMNAME")
    private String name;
	
    @Column(name = "DESCRIPTION")
    private String description;
    
    @Id
    @Column(name = "ITEMPROPERTIES")
    private String itemProperties;
    
    @Column(name = "ITEMTYPE")
    private String itemType;
    
    @Column(name = "SYS_CREATION_DATE")
    private Date sysCreationDate;    
}
