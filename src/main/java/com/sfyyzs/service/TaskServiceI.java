package com.sfyyzs.service;

import com.sfyyzs.model.Task;
import com.sfyyzs.model.TaskTree;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @user szx
 * @date 2020/10/18 16:21
 */
public interface TaskServiceI {
    /**
     * 根据目标Id获取对应的任务树
     * @param:
     * @return:
     * @auther: szx
     * @date: 2020/10/18 16:20
     */
    List<TaskTree> getTaskTreeByGoalId( int id);
    /**
     * 根据目标Id获取对应的任务树
     * @param:
     * @return:
     * @auther: szx
     * @date: 2020/10/18 16:20
     */
    List<TaskTree> getTaskTreeByItemId(@Param("itemId") int itemId);
    /**
     * 任务保存
     * @param:
     * @return:
     * @auther: szx
     * @date: 2020/10/18 17:54
     */
    void saveTask(Task task);
}
