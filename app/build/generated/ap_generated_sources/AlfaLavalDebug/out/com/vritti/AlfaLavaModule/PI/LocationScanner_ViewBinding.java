// Generated code from Butter Knife. Do not modify!
package com.vritti.AlfaLavaModule.PI;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.vritti.ekatm.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class LocationScanner_ViewBinding implements Unbinder {
  private LocationScanner target;

  @UiThread
  public LocationScanner_ViewBinding(LocationScanner target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public LocationScanner_ViewBinding(LocationScanner target, View source) {
    this.target = target;

    target.locationId = Utils.findRequiredViewAsType(source, R.id.locationId, "field 'locationId'", EditText.class);
    target.txtLoc = Utils.findRequiredViewAsType(source, R.id.txtLoc, "field 'txtLoc'", TextView.class);
    target.img_barcode = Utils.findRequiredViewAsType(source, R.id.img_barcode, "field 'img_barcode'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    LocationScanner target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.locationId = null;
    target.txtLoc = null;
    target.img_barcode = null;
  }
}
