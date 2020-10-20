package com.sfyyzs.mapper;

import com.sfyyzs.model.Task;
import com.sfyyzs.model.TaskTree;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @user szx
 * @date 2020/10/18 16:16
 */
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
    List<TaskTree> getTaskTreeListByItemId(@Param("itemId") int itemId);
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
}
