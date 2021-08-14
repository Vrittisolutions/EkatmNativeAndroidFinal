// Generated code from Butter Knife. Do not modify!
package com.vritti.vwb.vworkbench;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.vritti.ekatm.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ClaimRecordActivity_ViewBinding implements Unbinder {
  private ClaimRecordActivity target;

  private View view2131299767;

  @UiThread
  public ClaimRecordActivity_ViewBinding(ClaimRecordActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ClaimRecordActivity_ViewBinding(final ClaimRecordActivity target, View source) {
    this.target = target;

    View view;
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar1, "field 'toolbar'", Toolbar.class);
    target.mRecycler = Utils.findRequiredViewAsType(source, R.id.recycleView, "field 'mRecycler'", RecyclerView.class);
    target.mProgress = Utils.findRequiredViewAsType(source, R.id.toolbar_progress_App_bar, "field 'mProgress'", ProgressBar.class);
    view = Utils.findRequiredView(source, R.id.txt_add_claim, "method 'claimAdd'");
    view2131299767 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.claimAdd();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    ClaimRecordActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.toolbar = null;
    target.mRecycler = null;
    target.mProgress = null;

    view2131299767.setOnClickListener(null);
    view2131299767 = null;
  }
}
