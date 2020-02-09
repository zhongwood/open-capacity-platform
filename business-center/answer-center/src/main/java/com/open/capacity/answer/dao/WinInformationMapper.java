package com.open.capacity.answer.dao;

import com.open.capacity.answer.entity.WinInformationEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WinInformationMapper {
    int insert(WinInformationEntity record);

    int insertSelective(WinInformationEntity record);

    int updateLog(Integer id);

    int updateWinInformation(WinInformationEntity winInformationEntity);

    int selectCount(WinInformationEntity winInformationEntity);

    String selectCode(Integer id);

    List<WinInformationEntity> queryPriceLogList(Map<String, Object> param);
}