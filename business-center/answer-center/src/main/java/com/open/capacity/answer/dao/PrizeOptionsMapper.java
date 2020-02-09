package com.open.capacity.answer.dao;

import com.open.capacity.answer.entity.PrizeOptionsEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface PrizeOptionsMapper {
    int insert(PrizeOptionsEntity record);

    int insertSelective(PrizeOptionsEntity record);


    int updateCount(Integer id);

    @Delete("delete from prize_options where id = #{id}")
    int deleteByPrimaryKey(Integer id);

    List<PrizeOptionsEntity> queryPriceList(Map<String, Object> param);


    List<PrizeOptionsEntity> selectByActivityId(Integer activityId);

    PrizeOptionsEntity queryOne(Integer id);
}