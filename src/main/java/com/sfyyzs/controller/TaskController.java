package com.sfyyzs.controller;

import com.sfyyzs.model.Result;
import com.sfyyzs.model.Task;
import com.sfyyzs.model.TaskTree;
import com.sfyyzs.service.TaskServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;

/**
 * @user szx
 * @date 2020/10/18 16:24
 */
@RestController
@RequestMapping("task")
public class TaskController {

    @Autowired
    private TaskServiceI taskServiceI;

    /**
     * 根据目标Id获取对应的任务树
     * @param:
     * @return:
     * @auther: szx
     * @date: 2020/10/18 16:20
     */
    @RequestMapping("getTaskTreeByGoalId")
    public Result  getTaskTreeByGoalId(int id){
        List<TaskTree> list;//=taskServiceI.getTaskTreeByGoalId(id);
        TaskTree tree=new TaskTree();
        tree.setDes("1");
        tree.setId(1);
        tree.setParent(-1);
        TaskTree tree2=new TaskTree();
        tree2.setDes("2");
        tree2.setId(2);
        tree2.setParent(1);
        TaskTree tree3=new TaskTree();
        tree3.setDes("3");
        tree3.setId(3);
        tree3.setParent(1);
        TaskTree tree4=new TaskTree();
        tree4.setDes("4");
        tree4.setId(4);
        tree4.setParent(2);
        list=new LinkedList<>();
        list.add(tree);
        list.add(tree2);
        list.add(tree3);
        list.add(tree4);
        return Result.getSuccessResult(list);
    }
    /**
     * 任务保存
     * @param:
     * @return:
     * @auther: szx
     * @date: 2020/10/18 17:55
     */
    @RequestMapping("saveTask")
    public Result  saveTask( Task task){
        task.setId(9);
        taskServiceI.saveTask(task);
        return Result.getSuccessResult(task.getId());
    }

    /**
     * 根据目标Id获取对应的任务树
     * @param:
     * @return:
     * @auther: szx
     * @date: 2020/10/18 16:20
     */
    @RequestMapping("getTaskTreeByItemId")
    public Result  getTaskTreeByItemId(@RequestParam("itemId") int itemId){
        List<TaskTree> list = taskServiceI.getTaskTreeByItemId(itemId);
        return Result.getSuccessResult(list);
    }

}
