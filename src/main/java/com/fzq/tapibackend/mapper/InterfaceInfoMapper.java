package com.fzq.tapibackend.mapper;

import com.fzq.tapibackend.model.entity.InterfaceInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author zfeng
* @description 针对表【InterfaceInfo(interface information)】的数据库操作Mapper
* @createDate 2024-09-11 18:07:57
* @Entity com.fzq.tapibackend.model.entity.Interfaceinfo
*/
public interface InterfaceInfoMapper extends BaseMapper<InterfaceInfo> {

    @Select("SELECT * FROM InterfaceInfo")
    List<InterfaceInfo> getAllInterface();

    @Insert("INSERT INTO InterfaceInfo (name, description, url, request_params, request_header, response_header, method, user_id) " +
            "VALUES (#{name}, #{description}, #{url}, #{requestParams}, #{requestHeader}, #{responseHeader}, #{method},#{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertInterface(InterfaceInfo interfaceInfo);
}




