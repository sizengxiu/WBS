package com.sfyyzs.service.impl;

import com.sfyyzs.mapper.GoalMapper;
import com.sfyyzs.model.Goal;
import com.sfyyzs.service.GoalService;
import com.sfyyzs.service.TaskServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @user szx
 * @date 2020/10/18 16:57
 */
@Service
public class GoalServiceImpl implements GoalService {

    @Autowired
    private GoalMapper goalMapper;

    @Autowired
    private TaskServiceI taskServiceI;
    @Override
    public int saveGoal(String des,int itemId) {
        Goal goal=new Goal();
        goal.setDes(des);
        goal.setTime(new Date());
        goal.setItemId(itemId);
        goalMapper.saveGoal(goal);
        return goal.getId();
    }

    @Override
    public List<Goal> getGoals(String des,int itemId) {
        return goalMapper.getGoals(des,itemId);
    }

    @Override
    public int deleteGoalByGoalId(int goalId) {
        int count=1;
        count+=taskServiceI.deleteTaskTreeByGoalId(goalId);
        goalMapper.deleteGoalByGoalId(goalId);
        return count;
    }


}
