// Generated code from Butter Knife. Do not modify!
package com.vritti.vwb.ImageWithLocation;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.EditText;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.vritti.ekatm.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class EnoSampleSubmitClass_ViewBinding implements Unbinder {
  private EnoSampleSubmitClass target;

  private View view2131296500;

  @UiThread
  public EnoSampleSubmitClass_ViewBinding(EnoSampleSubmitClass target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public EnoSampleSubmitClass_ViewBinding(final EnoSampleSubmitClass target, View source) {
    this.target = target;

    View view;
    target.txttotalQuant = Utils.findRequiredViewAsType(source, R.id.txttotalQuant, "field 'txttotalQuant'", EditText.class);
    target.txtsampleunit = Utils.findRequiredViewAsType(source, R.id.txtsampleunit, "field 'txtsampleunit'", EditText.class);
    target.txtsaleunit = Utils.findRequiredViewAsType(source, R.id.txtsaleunit, "field 'txtsaleunit'", EditText.class);
    view = Utils.findRequiredView(source, R.id.btn_finalsave, "method 'finalSubmit'");
    view2131296500 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.finalSubmit();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    EnoSampleSubmitClass target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.txttotalQuant = null;
    target.txtsampleunit = null;
    target.txtsaleunit = null;

    view2131296500.setOnClickListener(null);
    view2131296500 = null;
  }
}
