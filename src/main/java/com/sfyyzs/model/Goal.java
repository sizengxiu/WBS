package com.sfyyzs.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * @user szx
 * @date 2020/10/18 16:18
 */
public class Goal {
    private Integer id;
    private String des;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date time;

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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
