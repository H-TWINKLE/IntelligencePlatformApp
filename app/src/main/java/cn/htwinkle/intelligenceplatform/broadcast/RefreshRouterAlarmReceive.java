package cn.htwinkle.intelligenceplatform.broadcast;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import cn.htwinkle.intelligenceplatform.service.RouterTimeService;


/**
 * 循环启动Service
 */
public class RefreshRouterAlarmReceive extends BroadcastReceiver {
    private static final String TAG = "RefreshRouterAlarmReceive";

    @SuppressLint("LongLogTag")
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive: " + "接收到指令");
        context.startService(new Intent(context, RouterTimeService.class));
    }
}
