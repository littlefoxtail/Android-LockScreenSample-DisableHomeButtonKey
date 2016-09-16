package com.github.dubu.lockscreenusingservice;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by penglong on 16/9/15.
 */

public class CustomImageView extends ImageView {
  private boolean a;
  private boolean b;

  public CustomImageView(Context context) {
    super(context, null);
    this.a = false;
    this.b = false;
  }

  public CustomImageView(Context context, AttributeSet attributeSet) {
    super(context, attributeSet);
    this.a = false;
    this.b = false;
  }

  public void setImageResource(int i) {
    super.setImageResource(i);
    this.a = false;
  }

  public void setImageBitmap(Bitmap bitmap) {
    super.setImageBitmap(bitmap);
    this.a = false;
  }

  public void setImageDrawable(Drawable drawable) {
    super.setImageDrawable(drawable);
    this.a = false;
  }

  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
  }

  protected void onDraw(Canvas canvas) {
    try {
      super.onDraw(canvas);
    } catch (RuntimeException e) {
      e.printStackTrace();
    }
  }
}
