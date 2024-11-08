package com.shaw.claims.repo;

import com.shaw.claims.model.InvoiceLineType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceLineTypeRepository extends JpaRepository<InvoiceLineType,Integer> {

    List<InvoiceLineType> findByStatusIdOrderByDisplaySequenceAsc(int active);
}