package com.shaw.claims.constant;

import java.util.Arrays;
import java.util.List;

public class ManageCodes {
    // Rug
    private static final List<String> rugList = Arrays.asList("01", "32", "33", "35");
    private static final List<String> tuftexRugList = Arrays.asList("34");

    // Promotional Goods
    private static final List<String> promotionalGoodsList = Arrays.asList("09", "10");

    // Shawgrass
    private static final List<String> shawgrassList = Arrays.asList("24");

    // E-commerce Rug
    private static final List<String> ecommerceRugList = Arrays.asList("03");
    private static final List<String> ecommerceRugProductCodeList = Arrays.asList("76", "7S", "7P");

    // Floorigami
    private static final List<String> floorigamiList = Arrays.asList("30");
    private static final List<String> floorigamiProductCodeList = Arrays.asList("33");

    // Resilient
    private static final List<String> resilientList1 = Arrays.asList("19", "29", "43", "44", "54", "61", "86");
    private static final List<String> resilientList2 = Arrays.asList("27", "36", "49", "59", "60", "72", "76", "85", "47", "87");
    private static final List<String> resilientProductCodeList = Arrays.asList("59", "6D", "6E", "41", "4D", "9G");

    // Hardwood
    private static final List<String> hardwoodList = Arrays.asList("27", "36", "38", "49", "57", "58", "59", "60", "71", "72", "76", "85", "47", "87");
    private static final List<String> hardwoodProductCodeList = Arrays.asList("37", "44", "48", "9B", "39", "46");

    // Laminate
    private static final List<String> laminateList = Arrays.asList("45", "49", "59", "60", "71", "72", "85", "47", "87");
    private static final List<String> laminateProductCodeList = Arrays.asList("38", "43", "45", "49", "9C");

    // Ceramic
    private static final List<String> ceramicList = Arrays.asList("42", "49", "59", "60", "71", "85", "47", "87");
    private static final List<String> ceramicProductCodeList = Arrays.asList("35", "42", "47", "9D");

    // Carpet
    private static final List<String> carpetList = Arrays.asList("02", "03", "11", "12", "13", "15", "20", "21", "25", "30", "31", "39", "40", "41", "48", "52", "64", "65", "66", "67", "68", "69", "70", "79", "80");

    // Non - financial type categories
    private static final List<String> nonFinancialTypeList = Arrays.asList("FREIGHT","TAX","PRICING","ADVERTISING");

    public static List<String> getRugList() {
        return rugList;
    }

    public static List<String> getTuftexRugList() {
        return tuftexRugList;
    }

    public static List<String> getPromotionalGoodsList() {
        return promotionalGoodsList;
    }

    public static List<String> getShawgrassList() {
        return shawgrassList;
    }

    public static List<String> getEcommerceRugList() {
        return ecommerceRugList;
    }

    public static List<String> getEcommerceRugProductCodeList() {
        return ecommerceRugProductCodeList;
    }

    public static List<String> getFloorigamiList() {
        return floorigamiList;
    }

    public static List<String> getFloorigamiProductCodeList() {
        return floorigamiProductCodeList;
    }

    public static List<String> getResilientList1() {
        return resilientList1;
    }

    public static List<String> getResilientList2() {
        return resilientList2;
    }

    public static List<String> getResilientProductCodeList() {
        return resilientProductCodeList;
    }

    public static List<String> getHardwoodList() {
        return hardwoodList;
    }

    public static List<String> getHardwoodProductCodeList() {
        return hardwoodProductCodeList;
    }

    public static List<String> getLaminateList() {
        return laminateList;
    }

    public static List<String> getLaminateProductCodeList() {
        return laminateProductCodeList;
    }

    public static List<String> getCeramicList() {
        return ceramicList;
    }

    public static List<String> getCeramicProductCodeList() {
        return ceramicProductCodeList;
    }

    public static List<String> getCarpetList() {
        return carpetList;
    }

    public static List<String> getNonFinancialTypeList() { return nonFinancialTypeList; }
}
