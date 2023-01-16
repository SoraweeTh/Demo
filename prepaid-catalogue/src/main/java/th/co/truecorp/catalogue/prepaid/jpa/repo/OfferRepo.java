/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package th.co.truecorp.catalogue.prepaid.jpa.repo;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import th.co.truecorp.catalogue.prepaid.jpa.entity.CatOffer;

/**
 *
 * @author Sorawe3
 */

@Transactional
public interface OfferRepo extends JpaRepository<CatOffer, String> {
    
    @Modifying
    @Query(
            value = "truncate table cat_offer",
            nativeQuery = true
    )
    void truncateTable();
    
    @Modifying
    @Query(
            value = "select * from cat_offer",
            nativeQuery = true
    )
    List<CatOffer> countOffer();
}
