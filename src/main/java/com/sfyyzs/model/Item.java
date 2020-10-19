package com.sfyyzs.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @user szx
 * @date 2020/10/19 21:53
 */
public class Item implements Serializable {
    private Integer id;

    private String des;

    private Date dataTime;

    private Integer state;

    private static final long serialVersionUID = 1L;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public Date getDataTime() {
        return dataTime;
    }

    public void setDataTime(Date dataTime) {
        this.dataTime = dataTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
