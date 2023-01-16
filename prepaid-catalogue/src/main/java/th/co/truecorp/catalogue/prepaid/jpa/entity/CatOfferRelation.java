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
import th.co.truecorp.catalogue.prepaid.jpa.entity.id.IdOfferRelation;

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
@Table(name = "CAT_OFFER_RELATION")
@IdClass(IdOfferRelation.class)
public class CatOfferRelation {

    @Id
    @Column(name = "PARENTOFFER")
    private String parentCode;
    
    @Id
    @Column(name = "CHILD")
    private String childCode;
    
    @Column(name = "RELATIONTYPE")
    private String relationType;
    
    @Column(name = "DEFAULTFLAG")
    private String selectedByDefault;
    
    @Column(name = "OFFER_VERSION_ID")
    private String parentId;
    
    @Column(name = "CHILD_VERSION_ID")
    private String childId;
    
    @Column(name = "SYS_CREATION_DATE")
    private Date sysCreationDate;
}
