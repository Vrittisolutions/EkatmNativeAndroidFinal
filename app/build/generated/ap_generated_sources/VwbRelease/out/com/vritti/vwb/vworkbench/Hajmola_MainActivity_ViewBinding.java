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

  private View view2131297740;

  private View view2131297741;

  private View view2131297742;

  private View view2131297743;

  private View view2131297744;

  private View view2131297745;

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
    view2131297740 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.imliClick();
      }
    });
    view = Utils.findRequiredView(source, R.id.img_h2, "method 'pudinaClick'");
    view2131297741 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.pudinaClick();
      }
    });
    view = Utils.findRequiredView(source, R.id.img_h3, "method 'anarClick'");
    view2131297742 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.anarClick();
      }
    });
    view = Utils.findRequiredView(source, R.id.img_h4, "method 'regularClick'");
    view2131297743 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.regularClick();
      }
    });
    view = Utils.findRequiredView(source, R.id.img_h5, "method 'chatkolaClick'");
    view2131297744 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.chatkolaClick();
      }
    });
    view = Utils.findRequiredView(source, R.id.img_h6, "method 'hingClick'");
    view2131297745 = view;
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

    view2131297740.setOnClickListener(null);
    view2131297740 = null;
    view2131297741.setOnClickListener(null);
    view2131297741 = null;
    view2131297742.setOnClickListener(null);
    view2131297742 = null;
    view2131297743.setOnClickListener(null);
    view2131297743 = null;
    view2131297744.setOnClickListener(null);
    view2131297744 = null;
    view2131297745.setOnClickListener(null);
    view2131297745 = null;
  }
}
