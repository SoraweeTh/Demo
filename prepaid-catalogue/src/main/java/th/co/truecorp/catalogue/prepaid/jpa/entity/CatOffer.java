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
import th.co.truecorp.catalogue.prepaid.jpa.entity.id.IdOffer;

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
@Table(name = "CAT_OFFER")
@IdClass(IdOffer.class)
public class CatOffer {
    
    @Column(name = "OFFER_VERSION_ID")
    private String id;
    
    @Column(name = "OFFERCODE")
    private String code;
    
    @Id
    @Column(name = "OFFERNAME")
    private String name;
    
    @Column(name = "DESCRIPTION")
    private String description;
    
    @Column(name = "SALEEFFECTIVEDATE")
    private Date saleEffDate;
    
    @Column(name = "SALEEXPIRATIONDATE")
    private Date saleExpDate;
    
    @Column(name = "OFFERTYPE")
    private String type;
    
    @Column(name = "PRODUCTTYPE")
    private String productType;
    
    @Column(name = "SALE_CONTEXT")
    private String saleContext;
    
    @Column(name = "OFFER_VERSION_EFFDATE")
    private Date versionEffDate;
    
    @Column(name = "OFFER_VERSION_EXPDATE")
    private Date versionExpDate;
    
    @Column(name = "OFFERPROPERTIES")
    private String offerProperties;
    
    @Column(name = "PRIMARYPARAM")
    private String primaryParam;
    
    @Column(name = "ISDEPLOYMENT")
    private String isDeployment;
    
    @Column(name = "SERVICELEVEL")
    private String serviceLevel;
    
    @Column(name = "SYS_CREATION_DATE")
    private Date sysCreationDate;
    
}
