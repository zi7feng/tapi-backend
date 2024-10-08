package com.fzq.tapibackend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fzq.tapibackend.common.PageResponse;
import com.fzq.tapibackend.model.dto.InterfaceInfoQueryDTO;
import com.fzq.tapibackend.model.entity.InterfaceInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Page;

import java.util.List;


/**
* @author zfeng
* @description 针对表【InterfaceInfo(interface information)】的数据库操作Service
* @createDate 2024-09-11 18:07:57
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    List<InterfaceInfo> getAllInterface();

    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean verify);

    /**
     * Save the InterfaceInfo object.
     *
     * @param interfaceInfo The interface information object to be saved.
     * @return true if the save operation is successful, false otherwise.
     */
    boolean save(InterfaceInfo interfaceInfo);

    /**
     * Get InterfaceInfo By id
     * @param id The Interface id
     * @return Instance of id
     */
    InterfaceInfo getById(Long id);

    /**
     * Delete InterfaceInfo By id
     * @param id The Interface id
     * @return True if delete successfully
     */
    boolean deleteById(Long id);


    boolean updateById(InterfaceInfo interfaceInfo);

    IPage<InterfaceInfo> listInterfaceInfoByPage(InterfaceInfoQueryDTO interfaceInfoQueryDTO);

}
