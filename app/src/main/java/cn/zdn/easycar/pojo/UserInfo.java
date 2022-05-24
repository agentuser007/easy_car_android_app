package cn.zdn.easycar.pojo;

import java.io.Serializable;

public class UserInfo implements Serializable {
    private String id;
    private String username;
    private String phone;
    private int state;
    private int is_set;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getIs_set() {
        return is_set;
    }

    public void setIs_set(int is_set) {
        this.is_set = is_set;
    }
}
