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
import th.co.truecorp.catalogue.prepaid.jpa.entity.CatLogTable;

/**
 *
 * @author Sorawe3
 */

@Transactional
public interface LogTableRepo extends JpaRepository<CatLogTable, String>{
    
    @Modifying
    @Query(
            value = "select * from true9_epc_tmp_2cnvr where tmp_remark = 'PROCESSING_CAT_FETCHALL'",
            nativeQuery = true
    )
    List<CatLogTable> checkProcessingRemark();
    
    @Modifying
    @Query(
            value = "delete true9_epc_tmp_2cnvr where tmp_remark = 'PROCESSING_CAT_FETCHALL'",
            nativeQuery = true
    )
    void deleteProcessingRemark();
}
