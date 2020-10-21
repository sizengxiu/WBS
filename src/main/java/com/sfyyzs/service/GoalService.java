package com.sfyyzs.service;

import com.sfyyzs.model.Goal;

import java.util.List;

/**
 * @user szx
 * @date 2020/10/18 16:56
 */
public interface GoalService {
    /**
     * 新增目标
     * @param:
     * @return:
     * @auther: szx
     * @date: 2020/10/18 16:56
     */
    int saveGoal(String des,int itemId);
    /**
     * 获取目标列表
     * @param:
     * @return:
     * @auther: szx
     * @date: 2020/10/18 16:59
     */
    List<Goal> getGoals(String des,int itemId);
    /**
     * 根据目标id删除目标
     * @param goalId
     * @return
     */
    int deleteGoalByGoalId(int goalId);
}
