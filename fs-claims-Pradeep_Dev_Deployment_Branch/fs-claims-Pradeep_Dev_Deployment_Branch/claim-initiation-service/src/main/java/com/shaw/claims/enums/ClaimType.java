package com.shaw.claims.enums;

public enum ClaimType {
    INSTORE("shawonline"),
    INITIATION("initiation"),
    SUBMITTAL("submittal");
    private String type;
    ClaimType(String type) {this.type = type;}
    public String getType() {return type;}
    public static ClaimType fromString(String type){
        for(ClaimType claimType : ClaimType.values()){
            if(claimType.getType().equalsIgnoreCase(type))
                return claimType;
        }
        throw new IllegalArgumentException("Invalid claim type:" + type);
    }


}
