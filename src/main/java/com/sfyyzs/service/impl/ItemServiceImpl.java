package com.sfyyzs.service.impl;

import com.sfyyzs.mapper.GoalMapper;
import com.sfyyzs.mapper.ItemMapper;
import com.sfyyzs.model.Goal;
import com.sfyyzs.model.Item;
import com.sfyyzs.service.GoalService;
import com.sfyyzs.service.ItemService;
import com.sfyyzs.service.TaskServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @user szx
 * @date 2020/10/18 16:57
 */
@Service
@Transactional
public class ItemServiceImpl implements ItemService {


    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private TaskServiceI taskServiceI;

    @Autowired
    private GoalService goalService;


    @Override
    public int saveItem(String des) {
        Item item=new Item();
        item.setDes(des);
        item.setDataTime(new Date());
        item.setState(1);
        itemMapper.insert(item);
        return item.getId();
    }

    @Override
    public List<Item> getItems(String des) {
        return itemMapper.getItems(des);
    }

    @Override
    public int deleteItemByItemId(int itemId) {
        int count=1;
        count+=taskServiceI.deleteTaskTreeByItemId(itemId);
        count+=goalService.deleteGoalsByItemId(itemId);
        itemMapper.deleteItemByItemId(itemId);
        return count;
    }

    @Override
    public int updateItem(Item item) {
        return itemMapper.updateByPrimaryKeySelective(item);
    }


}
