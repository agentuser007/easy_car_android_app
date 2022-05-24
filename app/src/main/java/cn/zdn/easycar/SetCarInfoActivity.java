package cn.zdn.easycar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.zdn.easycar.config.Api;
import cn.zdn.easycar.config.Constants;
import cn.zdn.easycar.okhttp.OkUtil;
import cn.zdn.easycar.okhttp.ResultCallback;
import cn.zdn.easycar.pojo.CarInfo;
import cn.zdn.easycar.result.Result;
import cn.zdn.easycar.result.ResultConstant;
import cn.zdn.easycar.ui.home.HomeFragment;
import cn.zdn.easycar.util.SPUtil;
import okhttp3.Call;

public class SetCarInfoActivity extends AppCompatActivity {

    String car_licence = "";
    int co_type = 0;
    int ct_type = 0;
    int cut_type = 0;
    String c_e = "";
    //int c_e_year = 0;
    //int c_e_mouth = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_car_info);
        CalendarView calendarView = (CalendarView) findViewById(R.id.calendarView);
        //calendarView 监听事件
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange( CalendarView view, int year, int month, int dayOfMonth) {
                //显示用户选择的日期
                c_e = year + "-" + (month+1) + "-" + dayOfMonth ;
                Toast.makeText(SetCarInfoActivity.this,year + "年" + (month+1) + "月" + dayOfMonth + "日",Toast.LENGTH_SHORT).show();
            }
        });
        init();
    }
    protected void init(){
        List<String> co, ct, cut;
        ArrayAdapter<String> arr_adapter1, arr_adapter2, arr_adapter3;
        Spinner spinner_co = findViewById(R.id.spinner_car_owner);
        co = new ArrayList<String>();
        co.add("私家车");
        co.add("公司车");

        Spinner spinner_ct = findViewById(R.id.spinner_car_type);
        ct = new ArrayList<String>();
        ct.add("微型乘用车");
        ct.add("普通级乘用车");
        ct.add("中级乘用车");

        Spinner spinner_cut = findViewById(R.id.spinner_car_use_type);
        cut = new ArrayList<String>();
        cut.add("非营运");
        cut.add("营运");



        //适配器
        arr_adapter1= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, co);
        arr_adapter2= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ct);
        arr_adapter3= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cut);

        //设置样式
        arr_adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        arr_adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        arr_adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //加载适配器
        spinner_co.setAdapter(arr_adapter1);
        spinner_ct.setAdapter(arr_adapter2);
        spinner_cut.setAdapter(arr_adapter3);

        spinner_co.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                co_type = position;
                //String str=parent.getItemAtPosition(position).toString();
                //Toast.makeText(SetCarInfoActivity.this, "你点击的是:"+str, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        spinner_ct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                ct_type = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        spinner_cut.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                cut_type = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });




    }

    @Override
    public void finish() {
        super.finish();
    }

    public boolean confirm(View view){
        boolean state = false;
        String phone = SPUtil.build().getString(Constants.SP_USER_PHONE);
        AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(this);
        alertdialogbuilder.setMessage("确定绑定该手机号?\n"+phone);
        alertdialogbuilder.setPositiveButton("确定",  new DialogInterface.OnClickListener() {
            //添加"Yes"按钮
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                save();
            }
        });
        alertdialogbuilder.setNegativeButton("取消",  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        final AlertDialog alertdialog = alertdialogbuilder.create();
        alertdialog.show();
        return state;

    }

    public void save() {
        EditText cl = findViewById(R.id.car_licence);
        car_licence = cl.getText().toString();
        if(c_e.isEmpty())
            Toast.makeText(SetCarInfoActivity.this, "请选择检验有效期" , Toast.LENGTH_SHORT).show();
/*
        EditText cm = findViewById(R.id.car_e_mouth);
        c_e_mouth = Integer.parseInt(cm.getText().toString());

        EditText cy = findViewById(R.id.car_e_year);
        c_e_year = Integer.parseInt(cy.getText().toString());
*/

        //正确的车牌：川A123AB、川A2222学、川AF12345、川A12345D。
        Pattern pattern = Pattern.compile("^(([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-Z](([0-9]{5}[DF])|([DF]([A-HJ-NP-Z0-9])[0-9]{4})))|([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-Z][A-HJ-NP-Z0-9]{4}[A-HJ-NP-Z0-9挂学警港澳使领]))$");
        Matcher matcher = pattern.matcher(car_licence);
        if (!matcher.matches()) {
            Toast.makeText(SetCarInfoActivity.this, "输入车牌号错误" , Toast.LENGTH_SHORT).show();
        }else{
            //本地保存数据，发送更新api
            Toast.makeText(SetCarInfoActivity.this, "save" , Toast.LENGTH_SHORT).show();

            //发送请求
            OkUtil.post()
                    .url(Api.updateCar)
                    .addParam("userId", SPUtil.build().getString(Constants.SP_USER_ID))
                    .addParam("licence", car_licence)
                    .addParam("carType", ct_type)
                    .addParam("ownerType", co_type)
                    .addParam("useType", cut_type)
                    .addParam("permitTime", c_e)
                    .execute(new ResultCallback<Result<CarInfo>>() {
                        @Override
                        public void onSuccess(Result<CarInfo> response) {
                            String code = response.getCode();
                            if (ResultConstant.CODE_SUCCESS.equals(code)) {
                                SPUtil.build().putString(Constants.SP_USER_CAR_LICENCE, car_licence);
                                Toast.makeText(SetCarInfoActivity.this, "保存成功" , Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(SetCarInfoActivity.this, "保存失败" , Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(Call call, Exception e) {
                            Toast.makeText(SetCarInfoActivity.this, "保存失败，网络错误" , Toast.LENGTH_SHORT).show();
                        }
                    });
        }


    }



}