package com.sfyyzs.controller;

import com.sfyyzs.model.Goal;
import com.sfyyzs.model.Result;
import com.sfyyzs.service.GoalService;
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
public class GoalController {

    @Autowired
    private GoalService goalService;

    /**
     * 获取目标列表
     * @param:
     * @return:
     * @auther: szx
     * @date: 2020/10/18 17:02
     */
    @RequestMapping("getGoals")
    public Result getGoals(String des,int itemId){
        List<Goal> list = goalService.getGoals(des,itemId);
        return Result.getSuccessResult(list);
    }
    /**
     * 保存目标
     * @param:
     * @return:
     * @auther: szx
     * @date: 2020/10/18 17:02
     */
    @RequestMapping("saveGoal")
    public Result saveGoal(@RequestParam("des") String des,@RequestParam("itemId")int itemId){
        int id=goalService.saveGoal(des,itemId);
        return Result.getSuccessResult(id);
    }




}
