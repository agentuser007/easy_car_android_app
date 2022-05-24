package cn.zdn.easycar.pojo;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CarInfo {

    private String carId;

    private String userId;

    private String licence;

    private Integer carType;

    private Integer ownerType;

    private Integer useType;

    private Date permitTime;


    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLicence() {
        return licence;
    }

    public void setLicence(String licence) {
        this.licence = licence;
    }

    public Integer getCarType() {
        return carType;
    }

    public void setCarType(Integer carType) {
        this.carType = carType;
    }

    public Integer getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(Integer ownerType) {
        this.ownerType = ownerType;
    }

    public Integer getUseType() {
        return useType;
    }

    public void setUseType(Integer useType) {
        this.useType = useType;
    }

    public Date getPermitTime() {
        return permitTime;
    }

    public void setPermitTime(Date permitTime) {
        this.permitTime = permitTime;
    }

    public String getCar_typeS() {
        if(carType == 0)return "微型乘用车";
        else if(carType == 1)return "普通级乘用车";
        else return "中级乘用车";

    }

    public String getUseTypeS(){
        return (useType == 0) ? "非营运" : "营运";
    }

    public String getOwnerS() {
        return (ownerType == 0) ? "私家车" : "公司车";
    }


    @SuppressLint("SimpleDateFormat")
    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM ddHH:mm:ss 'GMT+08:00' yyyy", Locale.US);
        String d = "";
        try {
            Date date = sdf.parse(this.permitTime.toString());
            sdf = new SimpleDateFormat("yyyy-MM-dd");
            d = sdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

//     System.out.println(date.toString());

        return "车辆信息 \n" +
                "车牌号：" + licence + '\n' +
                "车辆使用性质：" + this.getUseTypeS() +'\n' +
                "车辆类型：" + this.getCar_typeS() +'\n' +
                "车辆所有人类型：" + this.getOwnerS() +'\n' +
                "检验有效期：" + d;

                //this.permitTime.getYear()+"."+this.permitTime.getMonth()+"."+this.permitTime.getDate() ;
    }
}
