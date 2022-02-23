// Generated code from Butter Knife. Do not modify!
package com.vritti.MilkModule;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.vritti.ekatm.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MilkRunListAdapter$MilkListHolder_ViewBinding implements Unbinder {
  private MilkRunListAdapter.MilkListHolder target;

  private View view2131299559;

  @UiThread
  public MilkRunListAdapter$MilkListHolder_ViewBinding(final MilkRunListAdapter.MilkListHolder target,
      View source) {
    this.target = target;

    View view;
    target.venderName = Utils.findRequiredViewAsType(source, R.id.venderName, "field 'venderName'", TextView.class);
    target.venderNumber = Utils.findRequiredViewAsType(source, R.id.venderNumber, "field 'venderNumber'", TextView.class);
    target.venderAddress = Utils.findRequiredViewAsType(source, R.id.venderAddress, "field 'venderAddress'", TextView.class);
    target.statusImage = Utils.findRequiredViewAsType(source, R.id.statusImage, "field 'statusImage'", ImageView.class);
    target.v1 = Utils.findRequiredView(source, R.id.v1, "field 'v1'");
    target.v2 = Utils.findRequiredView(source, R.id.v2, "field 'v2'");
    view = Utils.findRequiredView(source, R.id.tripRow, "method 'tripRow'");
    view2131299559 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.tripRow();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    MilkRunListAdapter.MilkListHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.venderName = null;
    target.venderNumber = null;
    target.venderAddress = null;
    target.statusImage = null;
    target.v1 = null;
    target.v2 = null;

    view2131299559.setOnClickListener(null);
    view2131299559 = null;
  }
}
