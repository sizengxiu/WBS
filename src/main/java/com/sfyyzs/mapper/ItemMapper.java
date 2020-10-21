package com.sfyyzs.mapper;

import com.sfyyzs.model.Goal;
import com.sfyyzs.model.Item;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Item record);

    int insertSelective(Item record);

    Item selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Item record);

    int updateByPrimaryKey(Item record);

    /**
     * 获取类别列表
     * @param: 关键词
     * @return:
     * @auther: szx
     * @date: 2020/10/18 16:20
     */
    List<Item> getItems(@Param("des")String des);

    /**
     * 根据类别id删除类别
     * @param:
     * @return:
     * @auther: szx
     * @date: 2020/10/21 21:50
     */
    int deleteItemByItemId(@Param("itemId") int itemId);
}
