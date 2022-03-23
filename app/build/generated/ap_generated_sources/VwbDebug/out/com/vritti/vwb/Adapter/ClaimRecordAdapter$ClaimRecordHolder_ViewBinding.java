// Generated code from Butter Knife. Do not modify!
package com.vritti.vwb.Adapter;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.vritti.ekatm.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ClaimRecordAdapter$ClaimRecordHolder_ViewBinding implements Unbinder {
  private ClaimRecordAdapter.ClaimRecordHolder target;

  private View view2131297129;

  private View view2131297029;

  @UiThread
  public ClaimRecordAdapter$ClaimRecordHolder_ViewBinding(final ClaimRecordAdapter.ClaimRecordHolder target,
      View source) {
    this.target = target;

    View view;
    target.claim_row = Utils.findRequiredViewAsType(source, R.id.claim_row, "field 'claim_row'", RelativeLayout.class);
    target.statusLayout = Utils.findRequiredViewAsType(source, R.id.statusLayout, "field 'statusLayout'", RelativeLayout.class);
    target.recordId = Utils.findRequiredViewAsType(source, R.id.recordId, "field 'recordId'", TextView.class);
    target.status = Utils.findRequiredViewAsType(source, R.id.status, "field 'status'", TextView.class);
    target.claimName = Utils.findRequiredViewAsType(source, R.id.claimName, "field 'claimName'", TextView.class);
    target.claimDate = Utils.findRequiredViewAsType(source, R.id.claimDate, "field 'claimDate'", TextView.class);
    target.deptDesc = Utils.findRequiredViewAsType(source, R.id.deptDesc, "field 'deptDesc'", TextView.class);
    target.paidAmt = Utils.findRequiredViewAsType(source, R.id.paidAmt, "field 'paidAmt'", TextView.class);
    target.claimAmt = Utils.findRequiredViewAsType(source, R.id.claimAmt, "field 'claimAmt'", TextView.class);
    target.statusLayout1 = Utils.findRequiredViewAsType(source, R.id.statusLayout1, "field 'statusLayout1'", RelativeLayout.class);
    target.updateLayout = Utils.findRequiredViewAsType(source, R.id.updateLayout, "field 'updateLayout'", LinearLayout.class);
    target.claimLayout = Utils.findRequiredViewAsType(source, R.id.claimLayout, "field 'claimLayout'", LinearLayout.class);
    view = Utils.findRequiredView(source, R.id.edit, "method 'editBtn'");
    view2131297129 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.editBtn();
      }
    });
    view = Utils.findRequiredView(source, R.id.delete, "method 'deleteBtn'");
    view2131297029 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.deleteBtn();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    ClaimRecordAdapter.ClaimRecordHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.claim_row = null;
    target.statusLayout = null;
    target.recordId = null;
    target.status = null;
    target.claimName = null;
    target.claimDate = null;
    target.deptDesc = null;
    target.paidAmt = null;
    target.claimAmt = null;
    target.statusLayout1 = null;
    target.updateLayout = null;
    target.claimLayout = null;

    view2131297129.setOnClickListener(null);
    view2131297129 = null;
    view2131297029.setOnClickListener(null);
    view2131297029 = null;
  }
}
