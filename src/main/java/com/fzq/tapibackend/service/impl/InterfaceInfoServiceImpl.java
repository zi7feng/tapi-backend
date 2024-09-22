package com.fzq.tapibackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fzq.tapibackend.common.ErrorCode;
import com.fzq.tapibackend.common.PageResponse;
import com.fzq.tapibackend.exception.BusinessException;
import com.fzq.tapibackend.model.dto.InterfaceInfoQueryDTO;
import com.fzq.tapibackend.model.entity.InterfaceInfo;
import com.fzq.tapibackend.service.InterfaceInfoService;
import com.fzq.tapibackend.mapper.InterfaceInfoMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
    private InterfaceInfoMapper interfaceInfoMapper;

    @Override
    public List<InterfaceInfo> getAllInterface() {
        return interfaceInfoMapper.getAllInterface();
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
        int result = interfaceInfoMapper.insertInterface(interfaceInfo);

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

    @Override
    public InterfaceInfo getById(Long id) {
        InterfaceInfo interfaceInfo = interfaceInfoMapper.getById(id);
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "Failed to retrieve interface info by id: " + id);
        }
        return interfaceInfo;
    }

    @Override
    public boolean deleteById(Long id) {
        if (id == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "Id is null");
        }
        return interfaceInfoMapper.deleteById(id);
    }

    @Override
    public boolean updateById(InterfaceInfo interfaceInfo) {
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "InterfaceInfo is null");
        }
        return interfaceInfoMapper.updateInfoById(interfaceInfo);
    }

    @Override
    public IPage<InterfaceInfo> listInterfaceInfoByPage(InterfaceInfoQueryDTO interfaceInfoQueryDTO) {
        if (interfaceInfoQueryDTO == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "InterfaceInfoQueryDTO is null");
        }

        Page<InterfaceInfo> page = new Page<>(interfaceInfoQueryDTO.getCurrentPage(), interfaceInfoQueryDTO.getPageSize());
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();

        String name = interfaceInfoQueryDTO.getName();
        String url = interfaceInfoQueryDTO.getUrl();
        String description = interfaceInfoQueryDTO.getDescription();
        String method = interfaceInfoQueryDTO.getMethod();
        Integer status = interfaceInfoQueryDTO.getStatus();
        if (!StringUtils.isEmpty(name)) {
            queryWrapper.like("name", name);
        }
        if (!StringUtils.isEmpty(url)) {
            queryWrapper.like("url", url);
        }
        if (!StringUtils.isEmpty(description)) {
            queryWrapper.like("description", description);
        }
        if (!StringUtils.isEmpty(method)) {
            queryWrapper.like("method", method);
        }
        if (status != null) {
            queryWrapper.eq("status", status);
        }

        IPage<InterfaceInfo> pageResult = this.page(page, queryWrapper);
        return pageResult;

    }


}




