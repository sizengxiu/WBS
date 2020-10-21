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
    List<TaskTree> getTaskTreeByItemId( int id);
    /**
     * 根据目标Id获取对应的任务树
     * @param:
     * @return:
     * @auther: szx
     * @date: 2020/10/18 16:20
     */
    List<TaskTree> getTaskListByItemId(int itemId);
    /**
     * 根据目标Id获取对应的任务树
     * @param:
     * @return:
     * @auther: szx
     * @date: 2020/10/18 16:20
     */
    List<Task> getTaskTreeListByItemId( int itemId);
    /**
     * 任务保存
     * @param:
     * @return:
     * @auther: szx
     * @date: 2020/10/18 17:54
     */
    void saveTask(Task task);

    /**
     * 根据任务Id删除任务以及子任务（逻辑删除）
     * @param taskId
     * @return
     */
    int deleteTaskTreeByTaskId(int taskId);
    /**
     * 根据目标Id删除任务以及子任务（逻辑删除）
     * @param goalId
     * @return
     */
    int deleteTaskTreeByGoalId(int goalId);
    /**
     * 根据类别Id删除任务以及子任务（逻辑删除）
     * @param itemId
     * @return
     */
    int deleteTaskTreeByItemId(int itemId);
}
