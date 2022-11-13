package cn.htwinkle.intelligenceplatform.service;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import java.util.Calendar;
import java.util.TimeZone;

import cn.htwinkle.intelligenceplatform.broadcast.RefreshRouterAlarmReceive;
import cn.htwinkle.intelligenceplatform.constant.Constants;

public class RouterTimeService extends Service {
    private static final String TAG = "RouterTimeService";
    private final RouterTimeService.RouterTimeBinder binder = new RouterTimeService.RouterTimeBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: 初始化服务 " + TAG);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(() -> Log.i(TAG, "onStartCommand: 开始任务 " + TAG)).start();
        sendBroadForReceive();
        //返回状态异常关闭自动重启
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        new Thread(() -> Log.i(TAG, "onDestroy: 停止任务" + TAG)).start();
        super.onDestroy();
    }

    private void sendBroadForReceive() {
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        long triggerAtTime = getTriggerAtTime();
        Intent i = new Intent(this, RefreshRouterAlarmReceive.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, Constants.PENDING_REQUEST,
                i, PendingIntent.FLAG_CANCEL_CURRENT);
        am.setRepeating(AlarmManager.RTC_WAKEUP, triggerAtTime, AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private long getTriggerAtTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        //这里时区需要设置一下，不然会有8个小时的时间差
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        calendar.set(Calendar.HOUR_OF_DAY, 19);
        calendar.set(Calendar.MINUTE, 06);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long systemTime = System.currentTimeMillis();
        long selectTime = calendar.getTimeInMillis();
        //当前时间大于设置时间，说明今天提醒时间已经过去了，设置为明天的点来提醒
        if (systemTime > selectTime) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            selectTime = calendar.getTimeInMillis();
        }
        return selectTime;
    }

    public class RouterTimeBinder extends Binder {

        /**
         * mainActivity
         */
        private Activity activity;

        public RouterTimeService getService() {
            return RouterTimeService.this;
        }

        public Activity getActivity() {
            return activity;
        }

        public void setActivity(Activity activity) {
            this.activity = activity;
        }
    }
}
