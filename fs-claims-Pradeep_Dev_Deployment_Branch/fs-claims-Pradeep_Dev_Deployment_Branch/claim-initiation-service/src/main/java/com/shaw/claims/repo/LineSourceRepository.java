package com.shaw.claims.repo;

import com.shaw.claims.model.LineSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LineSourceRepository extends JpaRepository<LineSource,Integer> {
    @Query("SELECT ls FROM LineSource ls WHERE ls.lineSourceCode =:lineSourceCode")
    LineSource findByLineSourceCode(@Param("lineSourceCode") String lineSourceCode);
}
