package com.sfyyzs.service.impl;

import com.sfyyzs.config.Dic;
import com.sfyyzs.mapper.GoalMapper;
import com.sfyyzs.mapper.ItemMapper;
import com.sfyyzs.mapper.TaskMapper;
import com.sfyyzs.model.*;
import com.sfyyzs.service.TaskServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @user szx
 * @date 2020/10/18 16:22
 */
@Service
@Transactional
public class TaskServiceImpl implements TaskServiceI {

    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private GoalMapper goalMapper;
    @Override
    public List<GoPanelTree> getTaskTreeByItemId(Integer itemId) {
        List<GoPanelTree> list=new LinkedList<>();

        if(itemId==-1){
            itemId=null;

            GoPanelTree tree=new GoPanelTree();
            tree.setId("-1");
            tree.setDes("汇总");
            tree.setParent("-2");
            list.add(tree);
        }
        //1、处理类型信息
        List<Item> itemList;
        if(itemId!=null){
            itemList=new LinkedList<>();
            itemList.add(itemMapper.selectByPrimaryKey(itemId));
        }else{
            itemList=itemMapper.getItems(null);
        }
        if(itemList==null){
            return list;
        }

        for(Item item:itemList){
            GoPanelTree tree=new GoPanelTree(item);
            list.add(tree);
        }

        //2、获取目标信息
        List<Goal> goalList= goalMapper.getGoals(null, itemId);
        if(goalList==null){
            return list;
        }
        for(Goal goal:goalList){
            GoPanelTree tree=new GoPanelTree(goal);
            list.add(tree);
        }

        //3、获取任务信息
        List<Task> taskList = taskMapper.getTaskListByItemId(itemId);
        if(taskList==null){
            return list;
        }
        for(Task task:taskList){
            GoPanelTree tree=new GoPanelTree(task);
            list.add(tree);
        }

        return list;
    }

    @Override
    public List<Task> getTaskListByItemId(int itemId) {
        return taskMapper.getTaskListByItemId(itemId);
    }
    @Override
    public List<Task> getTaskTreeListByItemId(Integer itemId) {
        if(itemId==-1){
            itemId=null;
        }
        List<Task> itemTaskList = getItemTaskList(itemId);
        List<Task> goalTaskList = getGoalTaskList(itemId);
        List<Task> taskList = getTaskList(itemId);

        taskList = organizeTaskTaskTree(taskList);
        goalTaskList= organizeGoalTaskTree(goalTaskList, taskList);
        itemTaskList = organizeItemGoalTree(itemTaskList, goalTaskList);
        return itemTaskList;
    }


    private List<Task> backup(int itemId){
        List<Task> list=new LinkedList<>();
        //1、类别转任务结构
        Item item = itemMapper.selectByPrimaryKey(itemId);
        if(item==null){
            return list;
        }
        Task itemTask=new Task();
        itemTask.setId(item.getId());
        itemTask.setDes(item.getDes());
        itemTask.setTypeName("类别");
        itemTask.setType(0);
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
            goalTask.setTypeName("目标");
            goalTask.setType(1);
            goalTask.setDes(goal.getDes());
            goalTaskList.add(goalTask);
            //3、任务挂在目标下
            List<Task> taskList = taskMapper.getTaskTreeListByGoalId(goal.getId());
            goalTask.setChildren(taskList);
        }
        itemTask.setChildren(goalTaskList);
        return list;
    }

    /**
     * 获取类型列表并转换成task
     * @param:
     * @return:
     * @auther: szx
     * @date: 2020/10/23 21:31
     */
    private List<Task> getItemTaskList(Integer itemId){
        List<Task> list=new LinkedList<>();
        List<Item> itemList;
        if(itemId!=null){
            itemList=new LinkedList<>();
            itemList.add(itemMapper.selectByPrimaryKey(itemId));
        }else{
            itemList=itemMapper.getItems(null);
        }
        if(itemList==null){
            return list;
        }
        for(Item item:itemList){
            Task itemTask=new Task();
            itemTask.setId(item.getId());
            itemTask.setDes(item.getDes());
            itemTask.setTypeName("类型");
            itemTask.setType(0);
            list.add(itemTask);
        }
        return list;
    }

    /**
     * 获取目标列表并转换成task
     * @param:
     * @return:
     * @auther: szx
     * @date: 2020/10/23 21:34
     */
    private List<Task> getGoalTaskList(Integer itemId){
        List<Task> list=new LinkedList<>();
        List<Goal> goalList= goalMapper.getGoals(null, itemId);
        if(goalList==null || goalList.size()==0){
            return list;
        }
        for(Goal goal:goalList){
            Task goalTask=new Task();
            goalTask.setId(goal.getId());
            goalTask.setTypeName("目标");
            goalTask.setType(1);
            goalTask.setDes(goal.getDes());
            goalTask.setItemId(goal.getItemId());
            list.add(goalTask);
        }
        return list;
    }


    /**
     * 获取任务列表
     * @param:
     * @return:
     * @auther: szx
     * @date: 2020/10/23 21:41
     */
    private List<Task> getTaskList(Integer itemId){
        List<Task> list=taskMapper.getTaskListByItemId(itemId);
        if(list==null){
            return list;
        }
        for(Task task:list){
            task.setTaskStateName(Dic.getItemValue(Dic.taskStateDicName,task.getTaskState()));
        }
        return list;
    }

    /**
     * 组装目标任务树
     * @param:
     * @return:
     * @auther: szx
     * @date: 2020/10/23 21:46
     */
    private List<Task>  organizeItemGoalTree(List<Task> itemList,List<Task> goalList){
        if(goalList==null || itemList==null){
            return itemList;
        }
        Map<Integer,List<Task>> map=new HashMap<>(goalList.size());
        for(Task task:goalList){
            List<Task> eachList = map.get(task.getItemId());
            if(eachList==null){
                eachList=new LinkedList<>();
                map.put(task.getItemId(),eachList);
            }
            eachList.add(task);
        }

        //组装树
        for(Task task:itemList){
            task.setChildren(map.get(task.getId()));
        }
        return itemList;
    }
    /**
     * 组装目标任务树
     * @param:
     * @return:
     * @auther: szx
     * @date: 2020/10/23 21:46
     */
    private List<Task>  organizeGoalTaskTree(List<Task> goalList,List<Task> taskList){
        if(goalList==null || taskList==null){
            return null;
        }
        Map<Integer,List<Task>> map=new HashMap<>(taskList.size());
        for(Task task:taskList){
            List<Task> eachList = map.get(task.getGoalId());
            if(eachList==null){
                eachList=new LinkedList<>();
                map.put(task.getGoalId(),eachList);
            }
            eachList.add(task);
        }

        //组装树
        for(Task task:goalList){
            task.setChildren(map.get(task.getId()));
        }
        return goalList;
    }

    /**
     * 把任务组装成树状结构
     * @param:
     * @return:
     * @auther: szx
     * @date: 2020/10/23 22:03
     */
    private List<Task> organizeTaskTaskTree(List<Task> list){
        if(list==null){
            return null;
        }
        Map<Integer,List<Task>> map=new HashMap<>(list.size());
        for(Task task:list){
            List<Task> eachList = map.get(task.getParent());
            if(eachList==null){
                eachList=new LinkedList<>();
                map.put(task.getParent(),eachList);
            }
            eachList.add(task);
        }

        //组装父子关系
        for(Task task:list){
            task.setChildren(map.get(task.getId()));
        }
        //最终列表中只保留第一级的任务
        Iterator<Task> itr = list.iterator();
        while(itr.hasNext()){
            Task task = itr.next();
            if(task.getParent()!=-1){
                itr.remove();
            }
        }

        return list;

    }



    @Override
    public void saveTask(Task task) {
        task.setAddDate(new Date());
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

    @Override
    public int updateTask(Task task) {
        task.setUpdateDate(new Date());
        return taskMapper.updateByPrimaryKeySelective(task);
    }


}
