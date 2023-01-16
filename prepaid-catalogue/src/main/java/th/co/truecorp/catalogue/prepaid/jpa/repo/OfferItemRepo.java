/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package th.co.truecorp.catalogue.prepaid.jpa.repo;

import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import th.co.truecorp.catalogue.prepaid.jpa.entity.CatOfferItem;

/**
 *
 * @author Sorawe3
 */

@Transactional
public interface OfferItemRepo extends CrudRepository<CatOfferItem, String> {
    
    @Modifying
    @Query(
            value = "truncate table cat_offer_item",
            nativeQuery = true
    )
    void truncateTable();
}
