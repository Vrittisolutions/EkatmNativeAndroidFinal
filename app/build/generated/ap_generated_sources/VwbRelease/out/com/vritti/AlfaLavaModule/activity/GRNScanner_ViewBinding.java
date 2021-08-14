// Generated code from Butter Knife. Do not modify!
package com.vritti.AlfaLavaModule.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.EditText;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.vritti.ekatm.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class GRNScanner_ViewBinding implements Unbinder {
  private GRNScanner target;

  @UiThread
  public GRNScanner_ViewBinding(GRNScanner target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public GRNScanner_ViewBinding(GRNScanner target, View source) {
    this.target = target;

    target.locationId = Utils.findRequiredViewAsType(source, R.id.locationId, "field 'locationId'", EditText.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    GRNScanner target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.locationId = null;
  }
}
