package com.github.dubu.lockscreenusingservice.lockscreen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import java.lang.ref.WeakReference;
import java.util.Calendar;

/**
 * Created by penglong on 16/9/21.
 */

public class LockScreenHelper {

  private Calendar calendar;

  private Handler handler;

  public void initTimeReceiver() {

  }

  private static class TimeZoneReceiver extends BroadcastReceiver {
    private final WeakReference<LockScreenHelper> helperWeakReference;
    Context context;

    public TimeZoneReceiver(LockScreenHelper helper, Context context) {
      helperWeakReference = new WeakReference<>(helper);
      this.context = context;
    }

    @Override public void onReceive(Context context, Intent intent) {
      boolean equals = intent.getAction().equals(Intent.ACTION_TIMEZONE_CHANGED);
      LockScreenHelper lockScreenHelper = helperWeakReference.get();
      if (lockScreenHelper != null) {
        lockScreenHelper.handler.post(new )
      }

    }
  }
}
