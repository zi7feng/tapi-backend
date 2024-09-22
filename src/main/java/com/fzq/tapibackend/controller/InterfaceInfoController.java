package com.fzq.tapibackend.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.fzq.tapibackend.common.BaseResponse;
import com.fzq.tapibackend.common.ErrorCode;
import com.fzq.tapibackend.common.PageResponse;
import com.fzq.tapibackend.common.ResultUtils;
import com.fzq.tapibackend.exception.BusinessException;
import com.fzq.tapibackend.model.dto.AddInterfaceInfoDTO;
import com.fzq.tapibackend.model.dto.InterfaceInfoQueryDTO;
import com.fzq.tapibackend.model.dto.UpdateInterfaceInfoDTO;
import com.fzq.tapibackend.model.dto.common.DeleteDTO;
import com.fzq.tapibackend.model.entity.InterfaceInfo;
import com.fzq.tapibackend.service.InterfaceInfoService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/interfaceinfo")
public class InterfaceInfoController {

    @Autowired
    private InterfaceInfoService interfaceInfoService;

    @GetMapping("/all")
    public List<InterfaceInfo> selectAll() {
        return interfaceInfoService.getAllInterface();
    }


    /**
     * Adds new interface information.
     *
     * @param addInterfaceInfoDTO DTO containing interface info.
     * @param httpServletRequest  HTTP request object.
     * @return BaseResponse<Long> Response with the newly created interface ID.
     *
     * @throws BusinessException If the DTO is null.
     */
    @PostMapping("/add")
    public BaseResponse<Long> addInterfaceInfo(@RequestBody AddInterfaceInfoDTO addInterfaceInfoDTO, HttpServletRequest httpServletRequest) {
        if (addInterfaceInfoDTO == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(addInterfaceInfoDTO, interfaceInfo);
        Boolean needVerify = addInterfaceInfoDTO.getNeedVerify();
        interfaceInfoService.validInterfaceInfo(interfaceInfo, needVerify);

        // mock userId
        // TODO user id
        interfaceInfo.setUserId(12345L);

        interfaceInfoService.save(interfaceInfo);
        long newInterfaceInfoId = interfaceInfo.getId(); // ID is guaranteed to be set if save is successful
        return ResultUtils.success(newInterfaceInfoId);

    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteInterfaceInfo(@RequestBody DeleteDTO deleteDTO, HttpServletRequest request) {
        if (ObjectUtils.anyNull(deleteDTO, deleteDTO.getId())) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        if (deleteDTO.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // TODO user role verification

        // whether interfaceInfo exists
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(deleteDTO.getId());

        boolean result = interfaceInfoService.deleteById(deleteDTO.getId());
        return ResultUtils.success(result);

    }

    @PostMapping("/update")
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse<Boolean> updateInterfaceInfo(@RequestBody UpdateInterfaceInfoDTO updateInterfaceInfoDTO, HttpServletRequest request) {
        if (ObjectUtils.anyNull(updateInterfaceInfoDTO, updateInterfaceInfoDTO.getId())) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        if (updateInterfaceInfoDTO.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        InterfaceInfo interfaceInfo = new InterfaceInfo();

        BeanUtils.copyProperties(updateInterfaceInfoDTO, interfaceInfo);
        interfaceInfoService.validInterfaceInfo(interfaceInfo, false);

        long id = updateInterfaceInfoDTO.getId();
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(result);
    }

    @GetMapping("/get")
    public BaseResponse<InterfaceInfo> getInterfaceInfoById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        return ResultUtils.success(interfaceInfo);
    }

    @GetMapping("/list")
    public BaseResponse<IPage<InterfaceInfo>> listInterfaceInfo(InterfaceInfoQueryDTO interfaceInfoQueryDTO, HttpServletRequest request) {
        if (interfaceInfoQueryDTO == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        if (interfaceInfoQueryDTO.getCurrentPage() <= 0 || interfaceInfoQueryDTO.getPageSize() <= 0) {
            interfaceInfoQueryDTO.setCurrentPage(1);
            interfaceInfoQueryDTO.setPageSize(10);
        }

        IPage<InterfaceInfo> result = interfaceInfoService.listInterfaceInfoByPage(interfaceInfoQueryDTO);
        return ResultUtils.success(result);
    }






}
