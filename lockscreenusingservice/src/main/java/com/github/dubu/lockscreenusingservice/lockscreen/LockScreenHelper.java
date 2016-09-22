package com.github.dubu.lockscreenusingservice.lockscreen;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.graphics.Typeface;
import android.os.Handler;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.github.dubu.lockscreenusingservice.R;
import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by penglong on 16/9/21.
 */

public class LockScreenHelper {

  private final Activity activity;
  Calendar calendar;

  private Handler handler = new Handler();


  Context context;
  private String timeFormat;
  private TimeZoneReceiver timeZoneReceiver;
  private SettingObserver settingObserver;
  private TextView lockScreenCurrentTime;
  private String dateFormat;
  private TextView lockScreenDate;

  public LockScreenHelper(Context context, Activity activity) {
    this.context = context;
    this.activity = activity;

    init();
    setDate();
  }

  //public LockScreenHelper() {
  //}

  private void init() {
    calendar = Calendar.getInstance();
    initDateFormat();

    lockScreenCurrentTime = (TextView) findViewById(R.id.lockScreenCurrentTime);

    lockScreenDate = (TextView) findViewById(R.id.lockScreenDate);

    dateFormat = "M月d日 EEE";

    try {
      Typeface createFromAsset = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Thin.ttf");
      Typeface createFromFile = Typeface.createFromFile("system/fonts/Roboto-Regular.ttf");
      if (createFromAsset != null) {
        lockScreenCurrentTime.setTypeface(createFromAsset);
      }
      if (createFromFile != null) {
        lockScreenDate.setTypeface(createFromFile);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private View findViewById(int id) {
    if (this.activity != null) {
      return activity.findViewById(id);
    }
    return null;
  }

  public void initTimeReceiver() {
    if (timeZoneReceiver == null) {
      timeZoneReceiver = new TimeZoneReceiver(this, context);
      IntentFilter intentFilter = new IntentFilter();
      intentFilter.addAction(Intent.ACTION_TIME_TICK);
      intentFilter.addAction(Intent.ACTION_TIME_CHANGED);
      intentFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
      context.registerReceiver(timeZoneReceiver, intentFilter);
    }
    //if ()
    if (settingObserver == null) {
      settingObserver = new SettingObserver(this, context);
      context.getContentResolver().registerContentObserver(Settings.System.CONTENT_URI, true,
          settingObserver);
    }
    setTime();

  }

  public void release() {
    if (timeZoneReceiver != null) {
      context.unregisterReceiver(timeZoneReceiver);
    }
    if (settingObserver != null) {
      context.getContentResolver().unregisterContentObserver(settingObserver);
    }
    timeZoneReceiver = null;
    settingObserver = null;
  }

  private void initDateFormat() {
    if (context != null) {
      timeFormat = DateFormat.is24HourFormat(context) ? "kk:mm" : "h:mm";
    }
  }

  private static class SettingObserver extends ContentObserver {

    Context context;
    private WeakReference<LockScreenHelper> weakReferenceHelpder;
    /**
     * Creates a content observer.
     *
     */
    public SettingObserver(LockScreenHelper helper, Context context) {
      super(new Handler());

      weakReferenceHelpder = new WeakReference<>(helper);
      this.context = context;
    }

    @Override public void onChange(boolean selfChange) {
      LockScreenHelper helper = weakReferenceHelpder.get();
      if (context == null) {
        return;
      }
      if (helper != null) {
        helper.initDateFormat();
        helper.setTime();
        return;
      }
      context.getContentResolver().unregisterContentObserver(this);

    }
  }

  public static class TimeZoneReceiver extends BroadcastReceiver {
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
        lockScreenHelper.handler.post(new CalendarTask(equals, lockScreenHelper));
      }

    }
  }

   void setTime() {
    calendar.setTimeInMillis(System.currentTimeMillis());
    this.lockScreenCurrentTime.setText(DateFormat.format(timeFormat, calendar));
  }

  private void setDate() {
    if (lockScreenDate != null) {
      try {
        lockScreenDate.setText(DateFormat.format(this.dateFormat, new Date()));
      } catch (OutOfMemoryError e) {
        Log.e("LockScreen", "refreshDate oom ");
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }




}
