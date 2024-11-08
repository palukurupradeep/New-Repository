package com.shaw.claims.repo;

import com.shaw.claims.model.InvoiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceTypeRepository extends JpaRepository<InvoiceType,Integer> {

    List<InvoiceType> findByStatusIdOrderByDisplaySequenceAsc(int active);
}