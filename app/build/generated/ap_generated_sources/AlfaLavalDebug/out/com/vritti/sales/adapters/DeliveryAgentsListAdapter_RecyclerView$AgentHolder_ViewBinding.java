// Generated code from Butter Knife. Do not modify!
package com.vritti.sales.adapters;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.vritti.ekatm.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class DeliveryAgentsListAdapter_RecyclerView$AgentHolder_ViewBinding implements Unbinder {
  private DeliveryAgentsListAdapter_RecyclerView.AgentHolder target;

  private View view2131296913;

  @UiThread
  public DeliveryAgentsListAdapter_RecyclerView$AgentHolder_ViewBinding(final DeliveryAgentsListAdapter_RecyclerView.AgentHolder target,
      View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.chkagent, "field 'chkagent' and method 'setChkagent'");
    target.chkagent = Utils.castView(view, R.id.chkagent, "field 'chkagent'", CheckBox.class);
    view2131296913 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.setChkagent();
      }
    });
    target.txtagentname = Utils.findRequiredViewAsType(source, R.id.txtagentname, "field 'txtagentname'", TextView.class);
    target.txtagentid = Utils.findRequiredViewAsType(source, R.id.txtagentid, "field 'txtagentid'", TextView.class);
    target.txtmobile = Utils.findRequiredViewAsType(source, R.id.txtmobile, "field 'txtmobile'", TextView.class);
    target.txt_estimate = Utils.findRequiredViewAsType(source, R.id.txt_estimate, "field 'txt_estimate'", TextView.class);
    target.txtlocation = Utils.findRequiredViewAsType(source, R.id.txtlocation, "field 'txtlocation'", TextView.class);
    target.txt_pendingshipments = Utils.findRequiredViewAsType(source, R.id.txt_pendingshipments, "field 'txt_pendingshipments'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    DeliveryAgentsListAdapter_RecyclerView.AgentHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.chkagent = null;
    target.txtagentname = null;
    target.txtagentid = null;
    target.txtmobile = null;
    target.txt_estimate = null;
    target.txtlocation = null;
    target.txt_pendingshipments = null;

    view2131296913.setOnClickListener(null);
    view2131296913 = null;
  }
}
