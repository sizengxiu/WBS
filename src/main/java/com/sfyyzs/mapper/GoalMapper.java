package com.sfyyzs.mapper;

import com.sfyyzs.model.Goal;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @user szx
 * @date 2020/10/18 16:18
 */
public interface GoalMapper {
    /**
     * 获取目标列表
     * @param: 关键词
     * @return:
     * @auther: szx
     * @date: 2020/10/18 16:20
     */
    List<Goal> getGoals(@Param("des")String des);

    /**
     * 新增目标
     * @param:
     * @return:
     * @auther: szx
     * @date: 2020/10/18 16:56
     */
    int saveGoal(Goal goal);
}
