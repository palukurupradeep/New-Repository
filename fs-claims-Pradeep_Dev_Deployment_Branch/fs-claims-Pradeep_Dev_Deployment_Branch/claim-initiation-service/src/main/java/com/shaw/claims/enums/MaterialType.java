package com.shaw.claims.enums;

public enum MaterialType {

    RESIDENTIAL("001"),
    COMMERCIAL("002"),
    LOWES("003"),
    HOME_DEPOT("004"),
    CCA("005"),
    ALIGNED("006"),
    RESIDENTIAL_RESILIENT("007"),
    RESIDENTIAL_RESILIENT_CCA("008"),
    RESIDENTIAL_RESILIENT_ALIGN("009"),
    COMMERCIAL_RESILIENT("010"),
    RUG("011"),
    TUFTEX_RUG("012"),
    PROMOTIONAL_GOODS("013"),
    SHAW_GRASS("014"),
    ECOMMERCE_RUG("015"),
    RESIDENTIAL_HARDWOOD("016"),
    RESIDENTIAL_LAMINATE("017"),
    RESIDENTIAL_CERAMIC("018"),
    RESIDENTIAL_CARPET("019"),
    FLOORIGAMI("020"),
    COMMERCIAL_HARDWOOD("021"),
    COMMERCIAL_LAMINATE("022"),
    COMMERCIAL_CERAMIC("023"),
    COMMERCIAL_CARPET("024");

    private String areaTypeCode;

    MaterialType(String areaTypeCode) {
        this.areaTypeCode = areaTypeCode;
    }

    public String getAreaTypeCode() {
        return areaTypeCode;
    }
}
