package com.shaw.claims.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shaw.claims.model.CustomerWatchlist;

import jakarta.transaction.Transactional;

import java.util.List;

@Repository
public interface CustomerWatchlistRepository extends JpaRepository<CustomerWatchlist, Integer>{
  @Modifying
  @Transactional
  @Query("update CustomerWatchlist set statusId=3 where createdByUserId=:userId and customerNumber=:customerNumber")
  void deleteCustomerWatchList(@Param("userId") Integer userId,@Param("customerNumber") String customerNumber);
  @Query("SELECT cw FROM CustomerWatchlist cw where cw.createdByUserId=:userId and cw.customerNumber=:customerNumber")
  List<CustomerWatchlist> findByCustomerNumber(@Param("userId") Integer userId, @Param("customerNumber") String customerNumber);
}
