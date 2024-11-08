package org.example.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRequest {

    private int age;
    private String gender;
    private String genderData;
    private int id;
    private int numberOfOrders;

    private String cusType;
    private String prodType;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date invoiceDate;
    private Date todaysDate;
    private int size;
    private int discount;
    private String carpetRange;
    private int carpetStartRange;
    private int carpetEndRange;

}
