/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package th.co.truecorp.catalogue.prepaid.jpa.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import th.co.truecorp.catalogue.prepaid.jpa.entity.id.IdLogTable;

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
@Table(name = "TRUE9_EPC_TMP_2CNVR")
@IdClass(IdLogTable.class)
public class CatLogTable implements Serializable {
    
    @Id
    @Column(name = "OFFER_ID")
    private Integer offerId;
    
    @Column(name = "PACKAGE_ID")
    private Integer packageId;
    
    @Column(name = "PRICING_ITEM_ID")
    private Integer pricingItemId;
    
    @Column(name = "REVENUE_TYPE")
    private String revenueType;
    
    @Column(name = "SYS_CREATION_DATE")
    private Date sysCreationDate;
    
    @Column(name = "TMP_REMARK")
    private String tmpRemark;
}
