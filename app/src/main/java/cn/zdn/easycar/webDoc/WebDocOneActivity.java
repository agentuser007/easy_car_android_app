package cn.zdn.easycar.webDoc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import cn.zdn.easycar.R;
import cn.zdn.easycar.WebViewActivity;
import cn.zdn.easycar.pojo.Feed;
import cn.zdn.easycar.ui.home.FeedActivity;
import cn.zdn.easycar.util.PagerPosition;

public class WebDocOneActivity extends AppCompatActivity {
    TextView titleTextView;
    TextView contentTextView;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wed_doc_one);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("详情");

        titleTextView = findViewById(R.id.title);
        contentTextView = findViewById(R.id.content);
        imageView = findViewById(R.id.imageView);
        initView();
    }

    private void initView() {
        Bundle bundle = this.getIntent().getExtras();
        if (bundle == null) return;
        int type = (int) bundle.getSerializable("sel");
        String url;
        Spanned result;
        switch (type){
            case 1:
                titleTextView.setText("验车前的准备工作");
                result = Html.fromHtml(contextOne);
                contentTextView.setText(result);
                url = "https://wl01.findlawimg.com/flimg/zhishi/50093/1644888661206_500936466.jpg";
                Glide.with(WebDocOneActivity.this)
                        .load(url)
                        .fitCenter()
                        .into(imageView);
                break;

            case 2:
                titleTextView.setText("机动车检验周期");
                result = Html.fromHtml(contextTwo);
                contentTextView.setText(result);
                url = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2F5b0988e595225.cdn.sohucs.com%2Fq_70%2Cc_zoom%2Cw_640%2Fimages%2F20181211%2F6d185ffabb734665af48e34bceacaa20.jpeg&refer=http%3A%2F%2F5b0988e595225.cdn.sohucs.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1655198312&t=f952f8878cf4cf9677bb1ada6091c8b4";
                Glide.with(WebDocOneActivity.this)
                        .load(url)
                        .fitCenter()
                        .into(imageView);
                break;
            case 4:
                titleTextView.setText("预约验车");
                result = Html.fromHtml(contextThree);
                contentTextView.setText(result);
                url = "http://jtgl.beijing.gov.cn/jgj/uiFramework/commonResource/image/2020032722404338061.png";
                Glide.with(WebDocOneActivity.this)
                        .load(url)
                        .fitCenter()
                        .into(imageView);
                break;





        }


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    String contextOne = "<p>　　（一）查看行驶证副证上的检验有效期，在检验有效期满前三个月（含本月）之内可以参加检验。&nbsp; <o:p></o:p></p><p>　　" +
            "（二）通过交管局外网、交管12123APP,查询待检车辆有无未处理的交通违法行为和事故记录，存在未处理记录的，提前到各交通支（大）队执法站进行处理，并前往工商银行缴纳罚款。 <o:p></o:p></p><p>　　" +
            "（三）到4S店或修理厂对车辆进行检查、保养，送检机动车应清洁，无明显漏油、漏水、漏气现象，轮胎完好，轮胎气压正常且胎冠花纹中无异物，发动机应运转平稳，怠速稳定，无异响；装有车载诊断系统（OBD）的车辆，不应有与防抱死制动系统（ABS）、电动助力转向系统（EPS）及其他与行车安全相关的故障信息。 <o:p></o:p></p><p>　　" +
            "（四）检查车辆轮胎，同轴轮胎花纹不一致、轮胎有异常磨损、胎面和胎壁有足以暴露出帘布层的割裂伤、花纹深度不符合要求的，及时更换同规格尺寸的轮胎。 <o:p></o:p></p><p>　　" +
            "（五）检查车窗玻璃，所有车窗玻璃应完好且未粘贴镜面反光遮阳膜。 <o:p></o:p></p><p>" +
            "　　（六）检查灯具、仪表，各种灯具、仪表应齐全有效。&nbsp; <o:p></o:p></p><p>　　" +
            "（七）配备有效灭火器（小、微型载客汽车除外）和机动车用三角警告牌。 <o:p></o:p></p><p>　　" +
            "（八）非法加装改装的，要恢复原状。 <o:p></o:p></p><p>　　" +
            "（九）使用可拆卸号牌架和可翻转号牌架的要拆除。&nbsp; <o:p></o:p></p><p>　　" +
            "（十）检查车身，漆面破损、车身锈蚀的及时修复；除公交车外，其它车辆不能喷涂车身广告或影响车身颜色的标志、图案、文字。 <o:p></o:p></p><p>　　" +
            "（十一）检查车辆号牌。号牌不清晰、不完整，影响识别时，应更换号牌。金属材料号牌的安装要求如下： <o:p></o:p></p><p>　" +
            "　1、应正面朝外、字符正向安装在号牌板（架）上，禁止反装或倒装。 <o:p></o:p></p><p>　" +
            "　2、前号牌安装在机动车前端的中间或者偏右（按机动车前进方向），后号牌安装在机动车后端的中间或者偏左，应不影响机动车安全行驶和号牌的识别。 <o:p></o:p></p><p>　" +
            "　3、安装要保证号牌无任何变形和遮盖，横向水平，纵向基本垂直于地面，纵向夹角不大于15°（摩托车号牌向上倾斜纵向夹角可不大于30°）。 <o:p></o:p></p><p>　" +
            "　4、安装孔均应安装符合GA804要求的固封装置，但受车辆条件限制无法安装的除外。 <o:p></o:p></p><p>　" +
            "　5、使用号牌架辅助安装时，号牌架内侧边缘距离机动车登记编号字符边缘大于 5mm 以上，不得遮盖生产序列标识。 <o:p></o:p></p><p>　" +
            "　6、号牌周边不得有其他影响号牌识别的光源。 <o:p></o:p></p><p>　" +
            "　（十二）货车的反光标识、侧后部防护装置符合要求。 <o:p></o:p></p><p>　　" +
            "（十三）验车时携带行驶证、在外省市缴纳机动车交通事故责任强制保险的需提供保险凭证，以及车船税纳税或者免税证明。 <o:p></o:p></p><p>　　" +
            "（十四）可通过交管局外网、电话、微信预约验车、交管12123APP。</p>";

    String contextTwo = "<p>　　《中华人民共和国道路交通安全法实施条例》第十六条规定，机动车应当从注册登记之日起，按照下列期限进行安全技术检验：</p><p>　　（—）营运载客汽车5年以内每年检验1次；达到和超过5年的，每6个月检验1次；</p><p>　　（二）载货汽车和大型、中型非营运载客汽车10年以内每年检验1次；达到和超过10年的，每6个月检验1次；</p><p>　　（三）注册登记4年以内的摩托车每2年检验1次，4年以上每年检验一次；注册登记6年以内的摩托车免于安全技术检验，但自车辆出厂之日起，超过5年未办理注册登记手续或发生过造成人员伤亡交通事故或者因非法改装被依法处罚的，仍按原周期进行检验。</p><p>　　（四）专用校车应当自注册登记之日起每6个月检验1次；</p><p>　　（五）非专用校车应当自取得校车标牌后每6个月检验1次；</p><p>　　（六）其他机动车每年检验1次；</p><p>　　（七）注册登记10年以内的非营运小型、微型载客汽车、大型轿车每2年检验1次；达到和超过10年的，每1年检验1次；达到和超过15年的，每6个月检验1次。注册登记6年以内的非营运小型、微型载客汽车、大型轿车免于安全技术检验，发生过造成人员伤亡的交通事故或者因非法改装被依法处罚的、营运车辆变更为非营运的、面包车仍按原周期进行检验。</p> ";
    String contextThree = "您可点击下面图标进入互联网交通安全综合服务平台，登录系统后选择“机动车业务”，点击“机动车检验预约”即可申请已成功备案车辆的预约验车服务。";

    public void imageClick(View view) {
        Intent intent = new Intent(view.getContext(), WebViewActivity.class);
        intent.putExtra("url", "https://bj.122.gov.cn");
        startActivity(intent);

    }
}