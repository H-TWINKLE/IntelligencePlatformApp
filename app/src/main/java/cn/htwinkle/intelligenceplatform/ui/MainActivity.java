package cn.htwinkle.intelligenceplatform.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import cn.htwinkle.intelligenceplatform.R;
import cn.htwinkle.intelligenceplatform.kit.SharedPrefsKit;
import cn.htwinkle.intelligenceplatform.service.RouterTimeService;

@ContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    public static final String ENABLE_ROUTER = "enableRouter";
    @ViewInject(R.id.main_switch)
    private Switch activeSwitch;

    @Event(value = R.id.main_switch, type = CompoundButton.OnCheckedChangeListener.class)
    private void onChange(CompoundButton compoundButton, boolean b) {
        if (b) {
            enableTimer();
            return;
        }
        disableTimer();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initView();
    }

    private void initView() {
        boolean value = cn.htwinkle.intelligenceplatform.kit.SharedPrefsKit.INSTANCE.getValue(this, ENABLE_ROUTER, false);
        activeSwitch.setChecked(value);
    }

    private void enableTimer() {
        Toast.makeText(this, "正在启用定时服务", Toast.LENGTH_SHORT).show();
        SharedPrefsKit.INSTANCE.saveValue(this, ENABLE_ROUTER, true);
        startService();
    }

    private void disableTimer() {
        Toast.makeText(this, "正在停用定时服务", Toast.LENGTH_SHORT).show();
        SharedPrefsKit.INSTANCE.saveValue(this, ENABLE_ROUTER, false);
        stopService();
    }

    private void startService() {
        Intent intent = new Intent(this, RouterTimeService.class);
        startService(intent);
    }

    private void stopService() {
        Intent intent = new Intent(this, RouterTimeService.class);
        stopService(intent);
    }

}