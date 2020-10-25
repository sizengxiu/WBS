package com.sfyyzs.config;

import java.util.HashMap;
import java.util.Map;

/**
 * @user szx
 * @date 2020/10/24 18:20
 */
public class Dic {

    /**
     * 任务状态字典
     * @date: 2020/10/24 18:21
     */
    private static final Map<Integer,String> taskStateDic=new HashMap<>();
    public static final String taskStateDicName="TASK_STATE";

    private static final Map<String,Map<Integer,String>> dics=new HashMap<>();


    static{

        dics.put(taskStateDicName,taskStateDic);

//        taskStateDic.put(1,"未开始");
        taskStateDic.put(2,"进行中");
        taskStateDic.put(3,"已完成");
        taskStateDic.put(4,"已终止");
        taskStateDic.put(5,"延期");
    }

    /**
     * 获取字典值
     * @param:
     * @return:
     * @auther: szx
     * @date: 2020/10/24 18:30
     */
    public static String getItemValue(String dicName,Integer key){
        Map<Integer, String> map = dics.get(dicName);
        if(map==null){
            return null;
        }
        return map.get(key);
    }
}
