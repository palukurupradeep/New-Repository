package com.shaw.claims.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactDetailsDTO {
    private String customerNumber;
    private String contactType;
    private String contactTitleCode;
    private String contactTitle;
    private String contactCompanyName;
    private String contactPrimaryInd;
    private String firstName;
    private String middleName;
    private String lastName;
    private String suffix;
    private String nickName;
    private String contactComments;
    private String category;
    private String phonePrimaryInd;
    private String phoneType;
    private String countryCode;
    private String areaCode;
    private String phoneNumber;
    private String extension;
    private String emailPrimaryInd;
    private String email;
    private String addressPrimaryInd;
    private String addressType;
    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    private String city;
    private String stateProvince;
    private String zipCode;
    private String country;
    private String county;
    private String latitude;
    private String longitude;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ContactDetailsDTO that)) return false;
        return Objects.equals(contactType, that.contactType) && Objects.equals(category, that.category) && Objects.equals(addressType, that.addressType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contactType, category, addressType);
    }
}
