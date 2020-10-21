package com.sfyyzs.mapper;

import com.sfyyzs.model.Goal;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @user szx
 * @date 2020/10/18 16:18
 */
@Repository
public interface GoalMapper {
    /**
     * 获取目标列表
     * @param: 关键词
     * @return:
     * @auther: szx
     * @date: 2020/10/18 16:20
     */
    List<Goal> getGoals(@Param("des")String des,@Param("itemId")int itemId);

    /**
     * 新增目标
     * @param:
     * @return:
     * @auther: szx
     * @date: 2020/10/18 16:56
     */
    int saveGoal(Goal goal);

    /**
     * 根据目标id删除目标
     * @param goalId
     * @return
     */
    int deleteGoalByGoalId(@Param("goalId") int goalId);
}
