package com.sfyyzs.mapper;

import com.sfyyzs.model.Task;
import com.sfyyzs.model.TaskTree;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @user szx
 * @date 2020/10/18 16:16
 */
@CacheNamespace
@Repository
public interface TaskMapper {

    /**
     * 根据目标Id获取对应的任务树
     * @param:
     * @return:
     * @auther: szx
     * @date: 2020/10/18 16:20
     */
    List<TaskTree> getTaskTreeByItemId(@Param("itemId") int itemId);
    /**
     * 根据目标Id获取对应的任务树
     * @param:
     * @return:
     * @auther: szx
     * @date: 2020/10/18 16:20
     */
    List<TaskTree> getTaskListByItemId(@Param("itemId") int itemId);
    /**
     * 根据目标Id获取对应的任务（最外层任务）
     * @param:
     * @return:
     * @auther: szx
     * @date: 2020/10/18 16:20
     */
    List<Task> getTaskTreeListByGoalId(@Param("goalId") int goalId);
    /**
     * 获取子任务
     * @param:
     * @return:
     * @auther: szx
     * @date: 2020/10/20 23:12
     */
    List<TaskTree> getSubTree(@Param("id") int id);
    /**
     * 任务保存
     * @param:
     * @return:
     * @auther: szx
     * @date: 2020/10/18 17:54
     */
    void saveTask(Task task);

    /**
     * 根据任务Id列表获取子任务id列表
     * @param list
     * @return
     */
    List<Integer> getDirectSubTaskIdList(@Param("list") List<Integer> list);
    /**
     * 根据任务Id列表删除任务（逻辑删除）
     * @param list
     * @return
     */
    int deleteTasksByList(@Param("list") List<Integer> list);
    /**
     * 根据目标Id删除任务以及子任务（逻辑删除）
     * @param goalId
     * @return
     */
    int deleteTaskTreeByGoalId(@Param("goalId") int goalId);
    /**
     * 根据类别Id删除任务以及子任务（逻辑删除）
     * @param itemId
     * @return
     */
    int deleteTaskTreeByItemId(@Param("itemId") int itemId);
}
