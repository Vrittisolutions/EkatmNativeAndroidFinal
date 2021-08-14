// Generated code from Butter Knife. Do not modify!
package com.vritti.AlfaLavaModule.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.vritti.ekatm.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ShipingInspectionSheetActivity_ViewBinding implements Unbinder {
  private ShipingInspectionSheetActivity target;

  @UiThread
  public ShipingInspectionSheetActivity_ViewBinding(ShipingInspectionSheetActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ShipingInspectionSheetActivity_ViewBinding(ShipingInspectionSheetActivity target,
      View source) {
    this.target = target;

    target.edt_scanPacket = Utils.findRequiredViewAsType(source, R.id.edt_scanPacket, "field 'edt_scanPacket'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ShipingInspectionSheetActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.edt_scanPacket = null;
  }
}
