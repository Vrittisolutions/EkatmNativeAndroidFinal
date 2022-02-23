// Generated code from Butter Knife. Do not modify!
package com.vritti.vwb.Adapter;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.vritti.ekatm.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ClaimDetailsRecycleAdapter$ClaimHolder_ViewBinding implements Unbinder {
  private ClaimDetailsRecycleAdapter.ClaimHolder target;

  private View view2131299602;

  private View view2131299601;

  @UiThread
  public ClaimDetailsRecycleAdapter$ClaimHolder_ViewBinding(final ClaimDetailsRecycleAdapter.ClaimHolder target,
      View source) {
    this.target = target;

    View view;
    target.tv_Cdate = Utils.findRequiredViewAsType(source, R.id.tv_clim_date, "field 'tv_Cdate'", TextView.class);
    target.tv_Camount = Utils.findRequiredViewAsType(source, R.id.tv_Total, "field 'tv_Camount'", TextView.class);
    target.fromPlace = Utils.findRequiredViewAsType(source, R.id.fromPlace, "field 'fromPlace'", TextView.class);
    target.ToPlace = Utils.findRequiredViewAsType(source, R.id.ToPlace, "field 'ToPlace'", TextView.class);
    target.tv_mode = Utils.findRequiredViewAsType(source, R.id.tv_mode_new, "field 'tv_mode'", TextView.class);
    target.tv_travelling = Utils.findRequiredViewAsType(source, R.id.tv_travelling, "field 'tv_travelling'", TextView.class);
    target.tv_lodging = Utils.findRequiredViewAsType(source, R.id.tv_lodging, "field 'tv_lodging'", TextView.class);
    target.tv_food = Utils.findRequiredViewAsType(source, R.id.tv_food, "field 'tv_food'", TextView.class);
    target.tv_Local = Utils.findRequiredViewAsType(source, R.id.tv_Local, "field 'tv_Local'", TextView.class);
    target.tv_Ph = Utils.findRequiredViewAsType(source, R.id.tv_Ph, "field 'tv_Ph'", TextView.class);
    target.tv_Maintenanace = Utils.findRequiredViewAsType(source, R.id.tv_Maintenanace, "field 'tv_Maintenanace'", TextView.class);
    target.tv_distance = Utils.findRequiredViewAsType(source, R.id.tv_distance, "field 'tv_distance'", TextView.class);
    view = Utils.findRequiredView(source, R.id.tv_claim_edit, "field 'tv_claim_edit' and method 'editClaim'");
    target.tv_claim_edit = Utils.castView(view, R.id.tv_claim_edit, "field 'tv_claim_edit'", TextView.class);
    view2131299602 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.editClaim();
      }
    });
    view = Utils.findRequiredView(source, R.id.tv_claim_cancel, "field 'tv_claim_cancel' and method 'deleteClaim'");
    target.tv_claim_cancel = Utils.castView(view, R.id.tv_claim_cancel, "field 'tv_claim_cancel'", TextView.class);
    view2131299601 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.deleteClaim();
      }
    });
    target.to = Utils.findRequiredViewAsType(source, R.id.to, "field 'to'", TextView.class);
    target.by = Utils.findRequiredViewAsType(source, R.id.byText, "field 'by'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ClaimDetailsRecycleAdapter.ClaimHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.tv_Cdate = null;
    target.tv_Camount = null;
    target.fromPlace = null;
    target.ToPlace = null;
    target.tv_mode = null;
    target.tv_travelling = null;
    target.tv_lodging = null;
    target.tv_food = null;
    target.tv_Local = null;
    target.tv_Ph = null;
    target.tv_Maintenanace = null;
    target.tv_distance = null;
    target.tv_claim_edit = null;
    target.tv_claim_cancel = null;
    target.to = null;
    target.by = null;

    view2131299602.setOnClickListener(null);
    view2131299602 = null;
    view2131299601.setOnClickListener(null);
    view2131299601 = null;
  }
}
