package com.github.dubu.lockscreenusingservice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ViewSwitcher;
import com.github.dubu.lockscreenusingservice.service.LockScreenViewService;
import com.swipebacklayout.SwipeBackLayout;
import com.swipebacklayout.Utils;
import com.swipebacklayout.SwipeBackActivityHelper;

/**
 * Created by mugku on 15. 3. 16..
 */
public class LockScreenActivity extends Activity {

    private SwipeBackActivityHelper swipeBackActivityHelper;
    private ImageSwitcher bigAlbumCover;

    public static void startLockScreenActivity(Context context) {
        Intent intent = new Intent(context, LockScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
            | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(intent);
    }

    private final String TAG = "LockScreenActivity";
    private static Context sLockscreenActivityContext = null;
    private RelativeLayout mLockscreenMainLayout = null;

    public static SendMassgeHandler mMainHandler = null;

    public PhoneStateListener phoneStateListener = new PhoneStateListener() {
        public void onCallStateChanged(int state, String incomingNumber) {

            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    break;
                default:
                    break;
            }
        }

    };


    @Override
    protected void onCreate(Bundle bundle) {
        setSystemUi();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        super.onCreate(bundle);
        sLockscreenActivityContext = this;
        mMainHandler = new SendMassgeHandler();
        getWindow().setType(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);



        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        //    getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //} else {
        //    getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //}
        TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        manager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);

        initLockScreenUi();

        //setLockGuard();

        //Rom mRom = RomFactory.createRom(this);
        //
        //if (mRom == null) {
        //
        //} else {
        //
        //}
    }

    private void setSystemUi() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            View decorView = getWindow().getDecorView();
            decorView.setOnSystemUiVisibilityChangeListener(new KitkatSystemUiVisibilityListener(this, decorView));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            );
            getWindow().addFlags(Integer.MIN_VALUE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);

            View decorView = getWindow().getDecorView();
            decorView.setOnSystemUiVisibilityChangeListener(new LollipopSystemUiVisibilityListener(this, decorView));
        }
    }


    class KitkatSystemUiVisibilityListener implements View.OnSystemUiVisibilityChangeListener {
        final View view;
        final LockScreenActivity activity;
        KitkatSystemUiVisibilityListener(LockScreenActivity activity, View view) {
            this.activity = activity;
            this.view = view;
        }

        @Override public void onSystemUiVisibilityChange(int visibility) {
            if ((visibility & View.INVISIBLE) == 0) {
                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
                    view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                    activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

                }
            }
        }
    }

    class LollipopSystemUiVisibilityListener implements View.OnSystemUiVisibilityChangeListener {
        final View view;
        final LockScreenActivity activity;

        LollipopSystemUiVisibilityListener(LockScreenActivity activity, View view) {
            this.activity = activity;
            this.view = view;
        }

        @Override public void onSystemUiVisibilityChange(int visibility) {
            if ((visibility & View.INVISIBLE) == 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
                    getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    );
                    activity.getWindow().addFlags(Integer.MIN_VALUE);
                    activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
                }

            }
        }
    }

    private class SendMassgeHandler extends android.os.Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            finishLockscreenAct();
        }
    }

    private void finishLockscreenAct() {
        finish();
    }


    private void initLockScreenUi() {
        setContentView(R.layout.activity_lock_screen);
        //mLockscreenMainLayout = (RelativeLayout) findViewById(R.id.lockscreen_main_layout);
        //mLockscreenMainLayout.getBackground().setAlpha(15);

        swipeBackActivityHelper = new SwipeBackActivityHelper(this);
        swipeBackActivityHelper.onActivityCreate();
        final SwipeBackLayout swipeBackLayout =
            swipeBackActivityHelper.getSwipeBackLayout();
        swipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
        swipeBackLayout.setEdgeSize(50);
        swipeBackLayout.addSwipeListener(new SwipeBackLayout.SwipeListener() {


            @Override public void onScrollStateChange(int state, float scrollPercent) {
                if (state == SwipeBackLayout.STATE_IDLE) {
                    Utils.convertActivityFromTranslucent(LockScreenActivity.this);
                }
            }

            @Override public void onEdgeTouch(int edgeFlag) {
                //Utils.convertActivityToTranslucent(LockScreenActivity.this);
            }

            @Override public void onScrollOverThreshold() {

            }
        });

        bigAlbumCover = (ImageSwitcher) findViewById(R.id.bigAlbumCover);
        bigAlbumCover.setFactory(new ViewSwitcher.ViewFactory() {
            @Override public View makeView() {
                CustomImageView imageview = new CustomImageView(LockScreenActivity.this);
                imageview.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageview.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
                return imageview;
            }
        });
        bigAlbumCover.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_right));
        bigAlbumCover.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_out_left));
    }

    @Override protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        swipeBackActivityHelper.onPostCreate();
        Utils.convertActivityFromTranslucent(this);

    }

    @Override public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT && hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            );
            getWindow().addFlags(Integer.MIN_VALUE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    private void setLockGuard() {
        boolean isLockEnable = false;
        if (!LockscreenUtil.getInstance(sLockscreenActivityContext).isStandardKeyguardState()) {
            isLockEnable = false;
        } else {
            isLockEnable = true;
        }

        Intent startLockscreenIntent = new Intent(this, LockScreenViewService.class);
        startService(startLockscreenIntent);

        boolean isSoftkeyEnable = LockscreenUtil.getInstance(sLockscreenActivityContext).isSoftKeyAvail(this);
        SharedPreferencesUtil.setBoolean(Lockscreen.ISSOFTKEY, isSoftkeyEnable);
        if (!isSoftkeyEnable) {
            mMainHandler.sendEmptyMessage(0);
        } else if (isSoftkeyEnable) {
            if (isLockEnable) {
                mMainHandler.sendEmptyMessage(0);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //finish();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

}
