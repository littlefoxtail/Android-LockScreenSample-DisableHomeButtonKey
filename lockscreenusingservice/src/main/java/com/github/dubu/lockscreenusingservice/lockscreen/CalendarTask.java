package com.github.dubu.lockscreenusingservice.lockscreen;

import java.util.Calendar;

/**
 */
public class CalendarTask implements Runnable {

  private final boolean changed;
  private final LockScreenHelper helper;

  public CalendarTask( boolean changed, LockScreenHelper helper) {
    this.changed = changed;
    this.helper = helper;
  }

  @Override public void run() {
    if (changed) {
      helper.calendar = Calendar.getInstance();
    }
    helper.setTime();
  }
}
