package com.sfyyzs.service.impl;

import com.sfyyzs.mapper.GoalMapper;
import com.sfyyzs.mapper.ItemMapper;
import com.sfyyzs.mapper.TaskMapper;
import com.sfyyzs.model.Goal;
import com.sfyyzs.model.Item;
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
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private GoalMapper goalMapper;
    @Override
    public List<TaskTree> getTaskTreeByItemId(int id) {
        return taskMapper.getTaskTreeByItemId(id);
    }

    @Override
    public List<TaskTree> getTaskListByItemId(int itemId) {
        return taskMapper.getTaskListByItemId(itemId);
    }
    @Override
    public List<Task> getTaskTreeListByItemId(int itemId) {
        List<Task> list=new LinkedList<>();
        //1、类别转任务结构
        Item item = itemMapper.selectByPrimaryKey(itemId);
        Task itemTask=new Task();
        itemTask.setId(item.getId());
        itemTask.setDes(item.getDes());
        itemTask.setType("类别");
        list.add(itemTask);
        //2、目标转任务结构，目标挂在类别下
        List<Goal> goalList= goalMapper.getGoals(null, itemId);
        if(goalList==null || goalList.size()==0){
            return list;
        }
        List<Task> goalTaskList=new LinkedList<>();
        for(Goal goal:goalList){
            Task goalTask=new Task();
            goalTask.setId(goal.getId());
            goalTask.setType("目标");
            goalTask.setDes(goal.getDes());
            goalTaskList.add(goalTask);
            //3、任务挂在目标下
            List<Task> taskList = taskMapper.getTaskTreeListByGoalId(goal.getId());
            goalTask.setChildren(taskList);
        }
        itemTask.setChildren(goalTaskList);
        return list;
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
