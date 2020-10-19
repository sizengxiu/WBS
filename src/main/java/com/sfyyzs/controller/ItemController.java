package com.sfyyzs.controller;

import com.sfyyzs.model.Item;
import com.sfyyzs.model.Result;
import com.sfyyzs.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @user szx
 * @date 2020/10/18 17:00
 */
@RestController
@RequestMapping("task")
public class ItemController {

    @Autowired
    private ItemService itemService;

    /**
     * 获取目标列表
     * @param:
     * @return:
     * @auther: szx
     * @date: 2020/10/18 17:02
     */
    @RequestMapping("getItems")
    public Result getItems(String des){
        List<Item> list = itemService.getItems(des);
        return Result.getSuccessResult(list);
    }
    /**
     * 保存目标
     * @param:
     * @return:
     * @auther: szx
     * @date: 2020/10/18 17:02
     */
    @RequestMapping("saveItem")
    public Result saveItem(@RequestParam("des") String des){
        int id=itemService.saveItem(des);
        return Result.getSuccessResult(id);
    }




}
