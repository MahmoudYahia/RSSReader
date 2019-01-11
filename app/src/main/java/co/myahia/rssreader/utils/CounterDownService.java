package co.myahia.rssreader.utils;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by M.YAHIA on 10/01/2019.
 */
public class CounterDownService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);

    }

    CountDownTimer countDownTimer = new CountDownTimer(2000 * 20, 1000) {
        @Override
        public void onTick(long l) {

            countDownTimer.start();
        }

        @Override
        public void onFinish() {

        }
    }.start();
}
