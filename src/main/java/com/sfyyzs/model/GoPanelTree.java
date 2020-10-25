package com.sfyyzs.model;

/**
 * @user szx
 * @date 2020/10/24 17:12
 */
public class GoPanelTree {

    private String id;
    private String des;
    private String parent;


    public GoPanelTree(){

    }

    public GoPanelTree(Task task){
        this.setId(task.getId()+"");
        this.setDes(task.getDes());
        if(task.getParent()==-1){
            this.setParent("2-"+task.getGoalId());
        }else{
            this.setParent(task.getParent()+"");
        }
    }
    public GoPanelTree(Goal goal){
        this.setId("2-"+goal.getId());
        this.setDes(goal.getDes());
        this.setParent("1-"+goal.getItemId());
    }
    public GoPanelTree(Item item){
        this.setId("1-"+item.getId());
        this.setDes(item.getDes());
        this.setParent("-1");
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }
}
