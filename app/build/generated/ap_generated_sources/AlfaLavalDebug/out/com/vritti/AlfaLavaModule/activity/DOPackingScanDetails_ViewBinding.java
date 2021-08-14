// Generated code from Butter Knife. Do not modify!
package com.vritti.AlfaLavaModule.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.vritti.ekatm.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class DOPackingScanDetails_ViewBinding implements Unbinder {
  private DOPackingScanDetails target;

  @UiThread
  public DOPackingScanDetails_ViewBinding(DOPackingScanDetails target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public DOPackingScanDetails_ViewBinding(DOPackingScanDetails target, View source) {
    this.target = target;

    target.edt_scanPacket = Utils.findRequiredViewAsType(source, R.id.edt_scanPacket, "field 'edt_scanPacket'", TextView.class);
    target.progressBar = Utils.findRequiredViewAsType(source, R.id.progressBar, "field 'progressBar'", ProgressBar.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    DOPackingScanDetails target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.edt_scanPacket = null;
    target.progressBar = null;
  }
}
