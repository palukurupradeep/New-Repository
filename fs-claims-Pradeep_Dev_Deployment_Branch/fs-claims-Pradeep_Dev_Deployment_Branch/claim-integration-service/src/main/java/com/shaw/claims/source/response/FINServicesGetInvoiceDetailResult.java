package com.shaw.claims.source.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FINServicesGetInvoiceDetailResult {
    @JsonProperty("FinancialServices.InvoiceNbr")
    public String InvoiceNbr;
    @JsonProperty("FinancialServices.InvoiceDate")
    public String InvoiceDate;
    @JsonProperty("FinancialServices.LineNbr")
    public String LineNbr;
    @JsonProperty("FinancialServices.SeqNbr")
    public String SeqNbr;
    @JsonProperty("FinancialServices.RollNbr")
    public String RollNbr;
    @JsonProperty("FinancialServices.SellingStyleNbr")
    public String SellingStyleNbr;
    @JsonProperty("FinancialServices.InventoryStyleNbr")
    public String InventoryStyleNbr;
    @JsonProperty("FinancialServices.ColorNbr")
    public String ColorNbr;
    @JsonProperty("FinancialServices.SellingColorName")
    public String SellingColorName;
    @JsonProperty("FinancialServices.DyeLot")
    public String DyeLot;
    @JsonProperty("FinancialServices.RcsCode")
    public String RcsCode;
    @JsonProperty("FinancialServices.Grade")
    public String Grade;
    @JsonProperty("FinancialServices.WidthFeet")
    public double WidthFeet;
    @JsonProperty("FinancialServices.WidthInch")
    public double WidthInch;
    @JsonProperty("FinancialServices.LengthFeet")
    public double LengthFeet;
    @JsonProperty("FinancialServices.LengthInch")
    public double LengthInch;
    @JsonProperty("FinancialServices.UnitofMeasure")
    public String UnitofMeasure;
    @JsonProperty("FinancialServices.Quantity")
    public double Quantity;
    @JsonProperty("FinancialServices.UnitPrice")
    public double UnitPrice;
    @JsonProperty("FinancialServices.NetAmount")
    public double NetAmount;
    @JsonProperty("FinancialServices.Selco")
    public String Selco;
    @JsonProperty("FinancialServices.Region")
    public String Region;
    @JsonProperty("FinancialServices.District")
    public String District;
    @JsonProperty("FinancialServices.Territory")
    public String Territory;
    @JsonProperty("FinancialServices.MasterBolNbr")
    public String MasterBolNbr;
    @JsonProperty("FinancialServices.ProductCode")
    public String ProductCode;
    @JsonProperty("FinancialServices.InventoryColorNbr")
    public String InventoryColorNbr;
    @JsonProperty("FinancialServices.VendorId")
    public String VendorId;
    @JsonProperty("FinancialServices.ConvFactor")
    public double ConvFactor;

}
