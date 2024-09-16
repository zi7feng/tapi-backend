package com.fzq.tapibackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fzq.tapibackend.common.ErrorCode;
import com.fzq.tapibackend.exception.BusinessException;
import com.fzq.tapibackend.model.entity.InterfaceInfo;
import com.fzq.tapibackend.service.InterfaceInfoService;
import com.fzq.tapibackend.mapper.InterfaceInfoMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* @author zfeng
* @description InterfaceInfo(interface information) implementation
* @createDate 2024-09-11 18:07:57
*/
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
    implements InterfaceInfoService {

    @Autowired
    private InterfaceInfoMapper interfaceinfoMapper;

    @Override
    public List<InterfaceInfo> getAllInterface() {
        return interfaceinfoMapper.getAllInterface();
    }

    @Override
    public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean verify) {
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        String name = interfaceInfo.getName();
        String url = interfaceInfo.getUrl();
        String description = interfaceInfo.getDescription();
        String method = interfaceInfo.getMethod();
        String requestParams = interfaceInfo.getRequestParams();
        String requestHeader = interfaceInfo.getRequestHeader();
        String responseHeader = interfaceInfo.getResponseHeader();

        if (verify) {
            if (StringUtils.isAnyBlank(name, url, method, requestParams, description, requestHeader, responseHeader)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "Incomplete interface info");
            }
        }
        if (StringUtils.isNotBlank(name) && name.length() > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Name exceeds max length of 50");
        }
    }

    /**
     * Save the InterfaceInfo object to the database.
     *
     * @param interfaceInfo The interface information object to be saved.
     * @return true if the save operation is successful, false otherwise.
     */
    @Transactional
    @Override
    public boolean save(InterfaceInfo interfaceInfo) {
        // Insert the interface info
        int result = interfaceinfoMapper.insertInterface(interfaceInfo);

        // Check if insertion was successful
        if (result <= 0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "Failed to save interface info");
        }

        // Check if ID was generated and assigned
        if (interfaceInfo.getId() == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "Failed to retrieve generated ID for interface info");
        }

        return true;
    }


}




