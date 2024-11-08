package com.shaw.claims.repo;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.shaw.claims.model.ClaimJob;


@Repository
public interface JobRepo extends JpaRepository<ClaimJob, Integer> {
    @Query("SELECT j FROM ClaimJob j WHERE j.jobId > 0 AND j.statusId != 3 ORDER BY j.createdDateTime DESC")
    List<ClaimJob> findAllJobs();
    @Query(value = "SELECT NEXT VALUE FOR CLM.JobCodeSequence", nativeQuery = true)
	Integer getNextJobNumber();
    ClaimJob findByJobCode(String jobCode);
  //  @Query("SELECT j FROM ClaimJob j  WHERE j.jobCode = jobCode")
   // ClaimJob findJobByJobCode(String jobCode);
}
