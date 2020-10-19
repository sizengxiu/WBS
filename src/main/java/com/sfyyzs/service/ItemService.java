package com.sfyyzs.service;

import com.sfyyzs.model.Goal;
import com.sfyyzs.model.Item;

import java.util.List;

/**
 * @user szx
 * @date 2020/10/18 16:56
 */
public interface ItemService {
    /**
     * 新增类别
     * @param:
     * @return:
     * @auther: szx
     * @date: 2020/10/18 16:56
     */
    int saveItem(String des);
    /**
     * 获取类别列表
     * @param:
     * @return:
     * @auther: szx
     * @date: 2020/10/18 16:59
     */
    List<Item> getItems(String des);
}
