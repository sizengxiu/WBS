package com.sfyyzs.service.impl;

import com.sfyyzs.mapper.TaskMapper;
import com.sfyyzs.model.Task;
import com.sfyyzs.model.TaskTree;
import com.sfyyzs.service.TaskServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
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
}
