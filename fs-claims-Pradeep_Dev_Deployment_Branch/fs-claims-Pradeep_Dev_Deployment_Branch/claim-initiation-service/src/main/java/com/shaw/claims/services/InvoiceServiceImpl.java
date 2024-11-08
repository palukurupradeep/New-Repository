package com.shaw.claims.services;

import com.shaw.claims.constant.CommonConstant;
import com.shaw.claims.dto.InvoiceLineTypeResponseDTO;
import com.shaw.claims.dto.InvoiceStatusResponseDTO;
import com.shaw.claims.dto.InvoiceTypeResponseDTO;
import com.shaw.claims.repo.InvoiceLineTypeRepository;
import com.shaw.claims.repo.InvoiceStatusRepository;
import com.shaw.claims.repo.InvoiceTypeRepository;
import com.shaw.claims.util.DateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceServiceImpl implements InvoiceService{

    Logger log = LogManager.getLogger(InspectionServicesImpl.class);

    @Autowired
    private DateUtil dateUtil;
    @Autowired
    private InvoiceTypeRepository invoiceTypeRepository;
    @Autowired
    private InvoiceStatusRepository invoiceStatusRepository;
    @Autowired
    private InvoiceLineTypeRepository invoiceLineTypeRepository;

    @Override
    public List<InvoiceLineTypeResponseDTO> getAllActiveInvoiceLineType() {
        log.info("InvoiceServiceImpl.getAllActiveInvoiceLineType");
        return invoiceLineTypeRepository.findByStatusIdOrderByDisplaySequenceAsc(CommonConstant.ACTIVE).stream()
                .map(invoiceLineType -> {
                    InvoiceLineTypeResponseDTO invoiceLineTypeResponseDTO = new InvoiceLineTypeResponseDTO();
                    BeanUtils.copyProperties(invoiceLineType,invoiceLineTypeResponseDTO);
                    return invoiceLineTypeResponseDTO;
                }).collect(Collectors.toList());
    }

    @Override
    public List<InvoiceTypeResponseDTO> getAllActiveInvoiceType() {
        log.info("InvoiceServiceImpl.getAllActiveInvoiceType");
        return invoiceTypeRepository.findByStatusIdOrderByDisplaySequenceAsc(CommonConstant.ACTIVE).stream()
                .map(invoiceType -> {
                    InvoiceTypeResponseDTO invoiceTypeResponseDTO = new InvoiceTypeResponseDTO();
                    BeanUtils.copyProperties(invoiceType,invoiceTypeResponseDTO);
                    return invoiceTypeResponseDTO;
                }).collect(Collectors.toList());
    }

    @Override
    public List<InvoiceStatusResponseDTO> getAllActiveInvoiceStatus() {
        log.info("InvoiceServiceImpl.getAllActiveInvoiceStatus");
        return invoiceStatusRepository.findByStatusIdOrderByDisplaySequenceAsc(CommonConstant.ACTIVE)
                .stream().map(invoiceStatus -> {
                    InvoiceStatusResponseDTO invoiceStatusResponseDTO = new InvoiceStatusResponseDTO();
                    BeanUtils.copyProperties(invoiceStatus,invoiceStatusResponseDTO);
                    return invoiceStatusResponseDTO;
                }).collect(Collectors.toList());
    }

}