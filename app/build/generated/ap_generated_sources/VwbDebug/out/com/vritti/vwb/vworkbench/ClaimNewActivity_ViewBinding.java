// Generated code from Butter Knife. Do not modify!
package com.vritti.vwb.vworkbench;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.vritti.ekatm.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ClaimNewActivity_ViewBinding implements Unbinder {
  private ClaimNewActivity target;

  private View view2131296460;

  private View view2131296473;

  private View view2131297657;

  @UiThread
  public ClaimNewActivity_ViewBinding(ClaimNewActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ClaimNewActivity_ViewBinding(final ClaimNewActivity target, View source) {
    this.target = target;

    View view;
    target.sp_task = Utils.findRequiredViewAsType(source, R.id.sp_task, "field 'sp_task'", Spinner.class);
    target.sp_approver = Utils.findRequiredViewAsType(source, R.id.sp_approver, "field 'sp_approver'", Spinner.class);
    target.Sp_climAgainst = Utils.findRequiredViewAsType(source, R.id.Sp_climAgainst, "field 'Sp_climAgainst'", Spinner.class);
    target.Sp_costCenter = Utils.findRequiredViewAsType(source, R.id.sp_costcenter, "field 'Sp_costCenter'", Spinner.class);
    target.ed_remark = Utils.findRequiredViewAsType(source, R.id.ed_remark, "field 'ed_remark'", EditText.class);
    target.ed_travel_purpose = Utils.findRequiredViewAsType(source, R.id.ed_travel_purpose, "field 'ed_travel_purpose'", EditText.class);
    target.lay_claim_details = Utils.findRequiredViewAsType(source, R.id.lay_claim_details, "field 'lay_claim_details'", RecyclerView.class);
    view = Utils.findRequiredView(source, R.id.btnSave, "field 'btnSave' and method 'submitClaim'");
    target.btnSave = Utils.castView(view, R.id.btnSave, "field 'btnSave'", Button.class);
    view2131296460 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.submitClaim();
      }
    });
    target.lay_claim_against = Utils.findRequiredViewAsType(source, R.id.lay_claim_against, "field 'lay_claim_against'", LinearLayout.class);
    target.lay_costcenter = Utils.findRequiredViewAsType(source, R.id.lay_costcenter, "field 'lay_costcenter'", LinearLayout.class);
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar1, "field 'toolbar'", Toolbar.class);
    target.mProgress = Utils.findRequiredViewAsType(source, R.id.toolbar_progress_App_bar, "field 'mProgress'", ProgressBar.class);
    view = Utils.findRequiredView(source, R.id.btn_add_claim, "method 'addClaim'");
    view2131296473 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.addClaim();
      }
    });
    view = Utils.findRequiredView(source, R.id.imAttachment, "method 'addAttachment'");
    view2131297657 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.addAttachment();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    ClaimNewActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.sp_task = null;
    target.sp_approver = null;
    target.Sp_climAgainst = null;
    target.Sp_costCenter = null;
    target.ed_remark = null;
    target.ed_travel_purpose = null;
    target.lay_claim_details = null;
    target.btnSave = null;
    target.lay_claim_against = null;
    target.lay_costcenter = null;
    target.toolbar = null;
    target.mProgress = null;

    view2131296460.setOnClickListener(null);
    view2131296460 = null;
    view2131296473.setOnClickListener(null);
    view2131296473 = null;
    view2131297657.setOnClickListener(null);
    view2131297657 = null;
  }
}
