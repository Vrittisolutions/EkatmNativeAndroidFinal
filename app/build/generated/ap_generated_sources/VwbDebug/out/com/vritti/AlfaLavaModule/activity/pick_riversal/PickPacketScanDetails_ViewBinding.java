// Generated code from Butter Knife. Do not modify!
package com.vritti.AlfaLavaModule.activity.pick_riversal;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.vritti.ekatm.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class PickPacketScanDetails_ViewBinding implements Unbinder {
  private PickPacketScanDetails target;

  @UiThread
  public PickPacketScanDetails_ViewBinding(PickPacketScanDetails target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public PickPacketScanDetails_ViewBinding(PickPacketScanDetails target, View source) {
    this.target = target;

    target.txt_locatn = Utils.findRequiredViewAsType(source, R.id.txt_locatn, "field 'txt_locatn'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    PickPacketScanDetails target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.txt_locatn = null;
  }
}
