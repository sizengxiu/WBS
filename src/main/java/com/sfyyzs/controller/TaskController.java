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
    @RequestMapping("getTaskTreeByItemId")
    public Result  getTaskTreeByItemId(@RequestParam("itemId") int itemId ){
        List<TaskTree> list=taskServiceI.getTaskTreeByItemId(itemId);
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
    @RequestMapping("getTaskListByItemId")
    public Result  getTaskListByItemId(@RequestParam("itemId") int itemId){
        List<TaskTree> list = taskServiceI.getTaskListByItemId(itemId);
        return Result.getSuccessResult(list);
    }
    /**
     * 根据目标Id获取对应的任务树
     * @param:
     * @return:
     * @auther: szx
     * @date: 2020/10/18 16:20
     */
    @RequestMapping("getTaskTreeListByItemId")
    public Result  getTaskTreeListByItemId(@RequestParam("itemId") int itemId){
        List<TaskTree> list = taskServiceI.getTaskTreeListByItemId(itemId);
        return Result.getSuccessResult(list);
    }

}
