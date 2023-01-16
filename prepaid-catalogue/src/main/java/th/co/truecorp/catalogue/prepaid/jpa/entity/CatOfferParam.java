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
import th.co.truecorp.catalogue.prepaid.jpa.entity.id.IdOfferParam;

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
@Table(name = "CAT_OFFER_PARAM")
@IdClass(IdOfferParam.class)
public class CatOfferParam {
    
    @Id
    @Column(name = "OFFERITEM")
    private String itemCode;
    
    @Id
    @Column(name = "OFFERCODE")
    private String offerCode;
    
    @Column(name = "ITEM_VERSION_ID")
    private String itemId;
    
    @Column(name = "OFFER_VERSION_ID")
    private String offerId;
    
    @Id
    @Column(name = "PARAMNAME")
    private String paramName;
    
    @Column(name = "PARAMPROP")
    private String paramValue;
    
    @Column(name = "ISEXPOSE")
    private String isExpose;
    
    @Column(name = "POPULATELEVEL")
    private String populateLevel;
    
    @Column(name = "ISMANDATORY")
    private String isMandatory;
    
    @Column(name = "PARAMCAT")
    private String paramCat;
    
    @Column(name = "ISTRANSIENT")
    private String isTransient;
    
    @Column(name = "PARAMTYPE")
    private String paramType;
    
    @Column(name = "MINIMUMVALUE")
    private int minimumValue;
    
    @Column(name = "MAXIMUMVALUE")
    private int maximumValue;
    
    @Column(name = "SYS_CREATION_DATE")
    private Date sysCreationDate;
    
}
