package com.open.capacity.answer.dao;

import com.open.capacity.answer.entity.SelectionModelEntity;

public interface SelectionModelMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SelectionModelEntity record);

    int insertSelective(SelectionModelEntity record);

    SelectionModelEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SelectionModelEntity record);

    int updateByPrimaryKeyWithBLOBs(SelectionModelEntity record);

    int updateByPrimaryKey(SelectionModelEntity record);
}