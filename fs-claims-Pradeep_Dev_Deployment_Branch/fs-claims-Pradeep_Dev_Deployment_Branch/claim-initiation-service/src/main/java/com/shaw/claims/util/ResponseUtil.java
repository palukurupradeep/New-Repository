package com.shaw.claims.util;

import com.shaw.claims.constant.ErrorCodes;
import com.shaw.claims.dto.ResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class ResponseUtil {

    public ResponseDTO getResponse(Object data) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setIsSuccess(true);
        responseDTO.setMessage("Saved successfully");
        responseDTO.setErrorCode(ErrorCodes.SAVED_SUCCESSFULLY);
        responseDTO.setData(data);
        return responseDTO;
    }
}
