package cn.zdn.easycar.pojo;

import java.util.Date;

public class Vio {
    private String vioId;
    private String licence;
    private String action;
    private String location;
    private String punish;
    private String isLook;
    private String state;
    private String createTime;

    public String getVioId() {
        return vioId;
    }

    public void setVioId(String vioId) {
        this.vioId = vioId;
    }

    public String getLicence() {
        return licence;
    }

    public void setLicence(String licence) {
        this.licence = licence;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPunish() {
        return punish;
    }

    public void setPunish(String punish) {
        this.punish = punish;
    }

    public String getIsLook() {
        if(isLook.equals("0"))
        return "未读";
        else return "已读";
    }

    public void setIsLook(String isLook) {
        this.isLook = isLook;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "违章信息" +'\n' +
                "车牌号：" + licence + '\n' +
                "违章行为：" + action + '\n' +
                "违章地点：" + location + '\n' +
                "处罚：" + punish + '\n' +
             //   "是否已读'" + isLook + '\'' +
              //  ", state='" + state + '\'' +
                "创建时间" + createTime + '\n';
    }
}
