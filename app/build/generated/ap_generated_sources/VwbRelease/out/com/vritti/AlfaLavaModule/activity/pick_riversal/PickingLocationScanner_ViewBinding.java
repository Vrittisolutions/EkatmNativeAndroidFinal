// Generated code from Butter Knife. Do not modify!
package com.vritti.AlfaLavaModule.activity.pick_riversal;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.EditText;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.vritti.ekatm.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class PickingLocationScanner_ViewBinding implements Unbinder {
  private PickingLocationScanner target;

  @UiThread
  public PickingLocationScanner_ViewBinding(PickingLocationScanner target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public PickingLocationScanner_ViewBinding(PickingLocationScanner target, View source) {
    this.target = target;

    target.locationId = Utils.findRequiredViewAsType(source, R.id.locationId, "field 'locationId'", EditText.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    PickingLocationScanner target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.locationId = null;
  }
}
