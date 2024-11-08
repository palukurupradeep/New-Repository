package com.shaw.claims.services;

import com.shaw.claims.dto.ADUserDTO;

import java.util.List;

public interface AdService {
    List<ADUserDTO> fetchUserFromAD(String firstName, String lastName, String activeDirId) throws Exception;
    void syncUsersFromAD(Integer userId) throws Exception;
}
