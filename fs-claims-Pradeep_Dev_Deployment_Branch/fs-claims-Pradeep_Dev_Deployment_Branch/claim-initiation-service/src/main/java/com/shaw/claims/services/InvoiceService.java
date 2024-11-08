package com.shaw.claims.services;

import com.shaw.claims.dto.InvoiceLineTypeResponseDTO;
import com.shaw.claims.dto.InvoiceStatusResponseDTO;
import com.shaw.claims.dto.InvoiceTypeResponseDTO;

import java.util.List;

public interface InvoiceService {

    List<InvoiceLineTypeResponseDTO> getAllActiveInvoiceLineType();

    List<InvoiceTypeResponseDTO> getAllActiveInvoiceType();

    List<InvoiceStatusResponseDTO> getAllActiveInvoiceStatus();
}