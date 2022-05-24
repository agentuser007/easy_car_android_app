package cn.zdn.easycar.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import cn.zdn.easycar.CarInspectionActivity;
import cn.zdn.easycar.CarViolationActivity;
import cn.zdn.easycar.R;
import cn.zdn.easycar.SettingsActivity;
import cn.zdn.easycar.config.Constants;


public class SendNotification {

    private  static NotificationManager mNotificationManager;
    private static NotificationCompat.Builder mBuilder;



    /**
     * 初始化通知
     */
    public static void showNotification(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = (SPUtil.build().getBoolean(Constants.SP_NOTIFICATION)) ? NotificationManager.IMPORTANCE_HIGH : NotificationManager.IMPORTANCE_NONE;
            String channelId = "vio";
            Intent intent = new Intent(context, CarViolationActivity.class);
            PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);
            //获取系统通知服务
            mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(channelId,
                    "vio message", importance);
            mNotificationManager.createNotificationChannel(channel);

            //创建通知
            mBuilder = new NotificationCompat.Builder(context, channelId)
                    .setContentTitle("违章通知")
                    .setContentText("新增违章信息，请及时处理")
                    .setWhen(System.currentTimeMillis())
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                    .setContentIntent(pi)
                    .setAutoCancel(true);

            //发送通知( id唯一,可用于更新通知时对应旧通知; 通过mBuilder.build()拿到notification对象 )
            mNotificationManager.notify(1, mBuilder.build());
        }
    }

    public static void showCarInsNotification(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = (SPUtil.build().getBoolean(Constants.SP_NOTIFICATION))? NotificationManager.IMPORTANCE_HIGH : NotificationManager.IMPORTANCE_NONE;
            String channelId = "car";
            Intent intent = new Intent(context, CarInspectionActivity.class);
            PendingIntent pi = PendingIntent.getActivity(context, 0 ,intent, 0);
            //获取系统通知服务
            mNotificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(channelId,
                    "car message",importance);
            mNotificationManager.createNotificationChannel(channel);

            //创建通知
            mBuilder = new NotificationCompat.Builder(context,channelId)
                    .setContentTitle("年检通知")
                    .setContentText("车辆年检期限即将到期，请及时处理")
                    .setWhen(System.currentTimeMillis())
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                    .setContentIntent(pi)
                    .setAutoCancel(true);

            //发送通知( id唯一,可用于更新通知时对应旧通知; 通过mBuilder.build()拿到notification对象 )
            mNotificationManager.notify(2,mBuilder.build());
        }

    }


}
