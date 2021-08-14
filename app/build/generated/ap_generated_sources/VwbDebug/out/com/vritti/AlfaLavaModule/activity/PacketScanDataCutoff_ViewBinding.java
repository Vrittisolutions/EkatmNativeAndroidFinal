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

public class PacketScanDataCutoff_ViewBinding implements Unbinder {
  private PacketScanDataCutoff target;

  @UiThread
  public PacketScanDataCutoff_ViewBinding(PacketScanDataCutoff target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public PacketScanDataCutoff_ViewBinding(PacketScanDataCutoff target, View source) {
    this.target = target;

    target.txt_locatn = Utils.findRequiredViewAsType(source, R.id.txt_locatn, "field 'txt_locatn'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    PacketScanDataCutoff target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.txt_locatn = null;
  }
}
