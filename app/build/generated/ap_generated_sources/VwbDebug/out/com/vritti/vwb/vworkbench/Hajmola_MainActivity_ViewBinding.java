// Generated code from Butter Knife. Do not modify!
package com.vritti.vwb.vworkbench;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.LinearLayout;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.vritti.ekatm.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class Hajmola_MainActivity_ViewBinding implements Unbinder {
  private Hajmola_MainActivity target;

  private View view2131297751;

  private View view2131297752;

  private View view2131297753;

  private View view2131297754;

  private View view2131297755;

  private View view2131297756;

  @UiThread
  public Hajmola_MainActivity_ViewBinding(Hajmola_MainActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public Hajmola_MainActivity_ViewBinding(final Hajmola_MainActivity target, View source) {
    this.target = target;

    View view;
    target.ln_imgMain = Utils.findRequiredViewAsType(source, R.id.ln_imgMain, "field 'ln_imgMain'", LinearLayout.class);
    target.ln_img1 = Utils.findRequiredViewAsType(source, R.id.ln_img1, "field 'ln_img1'", LinearLayout.class);
    target.ln_img2 = Utils.findRequiredViewAsType(source, R.id.ln_img2, "field 'ln_img2'", LinearLayout.class);
    view = Utils.findRequiredView(source, R.id.img_h1, "method 'imliClick'");
    view2131297751 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.imliClick();
      }
    });
    view = Utils.findRequiredView(source, R.id.img_h2, "method 'pudinaClick'");
    view2131297752 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.pudinaClick();
      }
    });
    view = Utils.findRequiredView(source, R.id.img_h3, "method 'anarClick'");
    view2131297753 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.anarClick();
      }
    });
    view = Utils.findRequiredView(source, R.id.img_h4, "method 'regularClick'");
    view2131297754 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.regularClick();
      }
    });
    view = Utils.findRequiredView(source, R.id.img_h5, "method 'chatkolaClick'");
    view2131297755 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.chatkolaClick();
      }
    });
    view = Utils.findRequiredView(source, R.id.img_h6, "method 'hingClick'");
    view2131297756 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.hingClick();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    Hajmola_MainActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.ln_imgMain = null;
    target.ln_img1 = null;
    target.ln_img2 = null;

    view2131297751.setOnClickListener(null);
    view2131297751 = null;
    view2131297752.setOnClickListener(null);
    view2131297752 = null;
    view2131297753.setOnClickListener(null);
    view2131297753 = null;
    view2131297754.setOnClickListener(null);
    view2131297754 = null;
    view2131297755.setOnClickListener(null);
    view2131297755 = null;
    view2131297756.setOnClickListener(null);
    view2131297756 = null;
  }
}
