package com.shaw.claims.repo;

import com.shaw.claims.model.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceStatusRepository extends JpaRepository<InvoiceStatus,Integer> {

    List<InvoiceStatus> findByStatusIdOrderByDisplaySequenceAsc(int active);
}