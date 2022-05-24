package cn.zdn.easycar.result;

import java.util.List;

import cn.zdn.easycar.pojo.LocList;

/*
极速数据api
 */
public class ResultJS<T> {
    private String status;
    private String msg;
    private T result;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}