// Generated code from Butter Knife. Do not modify!
package com.vritti.vwb.ImageWithLocation;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.vritti.ekatm.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SampleAdapter$SampleHolder_ViewBinding implements Unbinder {
  private SampleAdapter.SampleHolder target;

  private View view2131299054;

  @UiThread
  public SampleAdapter$SampleHolder_ViewBinding(final SampleAdapter.SampleHolder target,
      View source) {
    this.target = target;

    View view;
    target.locationStyle = Utils.findRequiredViewAsType(source, R.id.locationStyle, "field 'locationStyle'", TextView.class);
    target.locationName = Utils.findRequiredViewAsType(source, R.id.locationName, "field 'locationName'", TextView.class);
    target.sdvImage = Utils.findRequiredViewAsType(source, R.id.sdvImage, "field 'sdvImage'", SimpleDraweeView.class);
    view = Utils.findRequiredView(source, R.id.row_layout, "method 'rowClick'");
    view2131299054 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.rowClick();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    SampleAdapter.SampleHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.locationStyle = null;
    target.locationName = null;
    target.sdvImage = null;

    view2131299054.setOnClickListener(null);
    view2131299054 = null;
  }
}
