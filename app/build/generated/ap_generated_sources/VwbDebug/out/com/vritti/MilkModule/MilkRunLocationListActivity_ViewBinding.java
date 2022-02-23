// Generated code from Butter Knife. Do not modify!
package com.vritti.MilkModule;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.vritti.ekatm.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MilkRunLocationListActivity_ViewBinding implements Unbinder {
  private MilkRunLocationListActivity target;

  private View view2131296836;

  @UiThread
  public MilkRunLocationListActivity_ViewBinding(MilkRunLocationListActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public MilkRunLocationListActivity_ViewBinding(final MilkRunLocationListActivity target,
      View source) {
    this.target = target;

    View view;
    target.toolBar = Utils.findRequiredViewAsType(source, R.id.toolbar1, "field 'toolBar'", Toolbar.class);
    view = Utils.findRequiredView(source, R.id.changeStatus, "field 'changeStatus' and method 'clcikComplete'");
    target.changeStatus = Utils.castView(view, R.id.changeStatus, "field 'changeStatus'", Button.class);
    view2131296836 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.clcikComplete();
      }
    });
    target.recycleview = Utils.findRequiredViewAsType(source, R.id.recycleview, "field 'recycleview'", RecyclerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MilkRunLocationListActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.toolBar = null;
    target.changeStatus = null;
    target.recycleview = null;

    view2131296836.setOnClickListener(null);
    view2131296836 = null;
  }
}
