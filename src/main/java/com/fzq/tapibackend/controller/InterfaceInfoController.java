package com.fzq.tapibackend.controller;

import com.fzq.tapibackend.common.BaseResponse;
import com.fzq.tapibackend.common.ErrorCode;
import com.fzq.tapibackend.common.ResultUtils;
import com.fzq.tapibackend.exception.BusinessException;
import com.fzq.tapibackend.model.dto.AddInterfaceInfoDTO;
import com.fzq.tapibackend.model.entity.InterfaceInfo;
import com.fzq.tapibackend.service.InterfaceInfoService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
        interfaceInfo.setUserId(12345L);

        boolean result = interfaceInfoService.save(interfaceInfo);
        long newInterfaceInfoId = interfaceInfo.getId(); // ID is guaranteed to be set if save is successful
        return ResultUtils.success(newInterfaceInfoId);


    }

}
