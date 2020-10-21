package com.sfyyzs.service.impl;

import com.sfyyzs.mapper.TaskMapper;
import com.sfyyzs.model.Task;
import com.sfyyzs.model.TaskTree;
import com.sfyyzs.service.TaskServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @user szx
 * @date 2020/10/18 16:22
 */
@Service
public class TaskServiceImpl implements TaskServiceI {

    @Autowired
    private TaskMapper taskMapper;
    @Override
    public List<TaskTree> getTaskTreeByItemId(int id) {
        return taskMapper.getTaskTreeByItemId(id);
    }

    @Override
    public List<TaskTree> getTaskListByItemId(int itemId) {
        return taskMapper.getTaskListByItemId(itemId);
    }
    @Override
    public List<TaskTree> getTaskTreeListByItemId(int itemId) {
        return taskMapper.getTaskTreeListByItemId(itemId);
    }

    @Override
    public void saveTask(Task task) {
        task.setTime(new Date());
        task.setState(1);
        taskMapper.saveTask(task);
    }

    @Override
    public int deleteTaskTreeByTaskId(int taskId) {
        List<Integer> list=new LinkedList<>();
        list.add(taskId);
        taskMapper.deleteTasksByList(list);
        int count=1;
        List<Integer> directSubTaskIdList = taskMapper.getDirectSubTaskIdList(list);
        while(directSubTaskIdList!=null && directSubTaskIdList.size()!=0 ){
            taskMapper.deleteTasksByList(directSubTaskIdList);
            count+=directSubTaskIdList.size();
            directSubTaskIdList = taskMapper.getDirectSubTaskIdList(directSubTaskIdList);
        }
        return count;
    }

    @Override
    public int deleteTaskTreeByGoalId(int goalId) {
        return taskMapper.deleteTaskTreeByGoalId(goalId);
    }

    @Override
    public int deleteTaskTreeByItemId(int itemId) {
        return taskMapper.deleteTaskTreeByItemId(itemId);
    }


}
