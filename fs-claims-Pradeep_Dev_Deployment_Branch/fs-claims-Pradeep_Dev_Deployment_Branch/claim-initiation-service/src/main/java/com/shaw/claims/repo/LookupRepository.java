package com.shaw.claims.repo;

import com.shaw.claims.model.Lookup;
import com.shaw.claims.model.LookupTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LookupRepository extends JpaRepository<Lookup, Integer> {
    @Query("SELECT l FROM Lookup l JOIN l.lookupTypes lt ON lt.lookupTypeCode = :lookupTypeCode AND l.statusId = 1")
    public List<Lookup> getLookupTypeByCode(String lookupTypeCode);

    @Query("SELECT l.lookupCode FROM Lookup l WHERE l.lookupDescription =:lookupDescription")
    String findLookupCodeByLookupDescription(String lookupDescription);

    Lookup findByLookupCode(@Param("lookupCode") String lookupCode);
    
    @Query("SELECT l FROM Lookup l WHERE l.statusId = 1 AND l.lookupTypes.lookupTypeId = (SELECT lt.lookupTypeId FROM LookupTypes lt WHERE lt.lookupTypeCode = 'PRIM_CLM_SEARCH') AND l.lookupCode NOT IN ('PHO','ADV','GL','B/L','REP','MBO','KWI') ORDER BY l.displaySequence")
    List<Lookup> primarySearchDropDown();
    
    @Query("SELECT l FROM Lookup l WHERE l.statusId = 1 AND l.lookupTypes.lookupTypeId = (SELECT lt.lookupTypeId FROM LookupTypes lt WHERE lt.lookupTypeCode = 'SECOND_CLM_SEARCH') ORDER BY l.displaySequence")
    List<Lookup> secondarySearchDropDown();
}