// Generated code from Butter Knife. Do not modify!
package com.vritti.vwb.Adapter;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.vritti.ekatm.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SaharaRemarkAdapter$RemarkHolder_ViewBinding implements Unbinder {
  private SaharaRemarkAdapter.RemarkHolder target;

  @UiThread
  public SaharaRemarkAdapter$RemarkHolder_ViewBinding(SaharaRemarkAdapter.RemarkHolder target,
      View source) {
    this.target = target;

    target.txt_Info = Utils.findRequiredViewAsType(source, R.id.txt_Info, "field 'txt_Info'", TextView.class);
    target.txt_remark = Utils.findRequiredViewAsType(source, R.id.txt_remark, "field 'txt_remark'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    SaharaRemarkAdapter.RemarkHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.txt_Info = null;
    target.txt_remark = null;
  }
}
