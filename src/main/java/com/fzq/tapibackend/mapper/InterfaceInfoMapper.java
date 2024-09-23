package com.fzq.tapibackend.mapper;

import com.fzq.tapibackend.model.entity.InterfaceInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

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

    @Select("SELECT * FROM InterfaceInfo WHERE id = #{id}")
    InterfaceInfo getById(Long id);

    @Delete("DELETE FROM InterfaceInfo WHERE id = #{id}")
    boolean deleteById(Long id);

    @Update("UPDATE interfaceInfo SET name = #{name}, description = #{description}, " +
            "url = #{url}, request_params = #{requestParams}, request_header = #{requestHeader}," +
            "response_header = #{responseHeader}, method = #{method}, status = #{status} " +
            "WHERE id = #{id}")
    boolean updateInfoById(InterfaceInfo interfaceInfo);




}




