package com.sfyyzs.service.impl;

import com.sfyyzs.mapper.GoalMapper;
import com.sfyyzs.model.Goal;
import com.sfyyzs.service.GoalService;
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
    @Override
    public int saveGoal(String des,int itemId) {
        Goal goal=new Goal();
        goal.setDes(des);
        goal.setTime(new Date());
        goalMapper.saveGoal(goal);
        return goal.getId();
    }

    @Override
    public List<Goal> getGoals(String des) {
        return goalMapper.getGoals(des);
    }


}
