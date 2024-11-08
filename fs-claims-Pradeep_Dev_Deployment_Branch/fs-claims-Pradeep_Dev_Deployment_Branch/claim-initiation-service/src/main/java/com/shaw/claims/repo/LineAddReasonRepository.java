package com.shaw.claims.repo;

import com.shaw.claims.model.LineAddReason;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LineAddReasonRepository extends JpaRepository<LineAddReason,Integer> {
    @Query("SELECT s.lineAddReasonId FROM LineAddReason s WHERE s.lineAddReasonCode =:lineAddReasonCode")
    int findLineAddReasonIdByLineAddReasonCode(@Param("lineAddReasonCode") String lineAddReasonCode);

    @Query("SELECT s FROM LineAddReason s WHERE s.statusId =:statusId And s.displaySequence != 99 ORDER BY s.displaySequence")
	List<LineAddReason> findByStatusId(int statusId);
}
