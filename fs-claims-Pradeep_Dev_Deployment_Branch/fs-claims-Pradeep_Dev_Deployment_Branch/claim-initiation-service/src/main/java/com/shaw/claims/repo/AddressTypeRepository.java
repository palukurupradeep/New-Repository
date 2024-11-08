package com.shaw.claims.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shaw.claims.model.AddressType;

@Repository
public interface AddressTypeRepository extends JpaRepository<AddressType,Integer> {
    @Query("SELECT s.addressTypeId FROM AddressType s WHERE s.addressTypeCode =:addressTypeCode")
    int findAddressTypeIdByAddressTypeCode(@Param("addressTypeCode") String addressTypeCode);
    @Query("SELECT s.addressTypeCode FROM AddressType s WHERE s.addressTypeId =:addressTypeId")
    String findAddressTypeCodeByAddressTypeId(Integer addressTypeId);
}
