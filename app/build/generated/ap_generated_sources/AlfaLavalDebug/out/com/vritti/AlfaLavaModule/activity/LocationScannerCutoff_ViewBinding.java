// Generated code from Butter Knife. Do not modify!
package com.vritti.AlfaLavaModule.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.EditText;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.vritti.ekatm.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class LocationScannerCutoff_ViewBinding implements Unbinder {
  private LocationScannerCutoff target;

  @UiThread
  public LocationScannerCutoff_ViewBinding(LocationScannerCutoff target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public LocationScannerCutoff_ViewBinding(LocationScannerCutoff target, View source) {
    this.target = target;

    target.locationId = Utils.findRequiredViewAsType(source, R.id.locationId, "field 'locationId'", EditText.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    LocationScannerCutoff target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.locationId = null;
  }
}
