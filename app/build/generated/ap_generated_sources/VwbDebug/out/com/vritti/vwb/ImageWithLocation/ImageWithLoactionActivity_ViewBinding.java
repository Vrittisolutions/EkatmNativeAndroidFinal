// Generated code from Butter Knife. Do not modify!
package com.vritti.vwb.ImageWithLocation;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.vritti.ekatm.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ImageWithLoactionActivity_ViewBinding implements Unbinder {
  private ImageWithLoactionActivity target;

  private View view2131296952;

  private View view2131299072;

  @UiThread
  public ImageWithLoactionActivity_ViewBinding(ImageWithLoactionActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ImageWithLoactionActivity_ViewBinding(final ImageWithLoactionActivity target,
      View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.clickImage, "field 'clickImage' and method 'setClickImage'");
    target.clickImage = Utils.castView(view, R.id.clickImage, "field 'clickImage'", Button.class);
    view2131296952 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.setClickImage();
      }
    });
    target.image = Utils.findRequiredViewAsType(source, R.id.image, "field 'image'", SimpleDraweeView.class);
    target.eAddress = Utils.findRequiredViewAsType(source, R.id.eAddress, "field 'eAddress'", TextView.class);
    target.fullImage = Utils.findRequiredViewAsType(source, R.id.fullImage, "field 'fullImage'", RelativeLayout.class);
    target.locationImage = Utils.findRequiredViewAsType(source, R.id.locationImage, "field 'locationImage'", SimpleDraweeView.class);
    view = Utils.findRequiredView(source, R.id.saveImage, "field 'saveImage' and method 'setScreeenShot'");
    target.saveImage = Utils.castView(view, R.id.saveImage, "field 'saveImage'", Button.class);
    view2131299072 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.setScreeenShot();
      }
    });
    target.locationLayout = Utils.findRequiredViewAsType(source, R.id.locationLayout, "field 'locationLayout'", LinearLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ImageWithLoactionActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.clickImage = null;
    target.image = null;
    target.eAddress = null;
    target.fullImage = null;
    target.locationImage = null;
    target.saveImage = null;
    target.locationLayout = null;

    view2131296952.setOnClickListener(null);
    view2131296952 = null;
    view2131299072.setOnClickListener(null);
    view2131299072 = null;
  }
}
