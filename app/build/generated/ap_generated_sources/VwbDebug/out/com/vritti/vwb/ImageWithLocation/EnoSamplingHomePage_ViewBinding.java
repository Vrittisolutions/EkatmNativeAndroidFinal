// Generated code from Butter Knife. Do not modify!
package com.vritti.vwb.ImageWithLocation;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.vritti.ekatm.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class EnoSamplingHomePage_ViewBinding implements Unbinder {
  private EnoSamplingHomePage target;

  private View view2131296361;

  private View view2131298760;

  @UiThread
  public EnoSamplingHomePage_ViewBinding(EnoSamplingHomePage target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public EnoSamplingHomePage_ViewBinding(final EnoSamplingHomePage target, View source) {
    this.target = target;

    View view;
    target.recycleView = Utils.findRequiredViewAsType(source, R.id.recycleView, "field 'recycleView'", RecyclerView.class);
    target.sampleSpinner = Utils.findRequiredViewAsType(source, R.id.sampleSpinner, "field 'sampleSpinner'", Spinner.class);
    target.locationNameET = Utils.findRequiredViewAsType(source, R.id.locationNameET, "field 'locationNameET'", EditText.class);
    target.progressBar = Utils.findRequiredViewAsType(source, R.id.toolbar_progress_logging, "field 'progressBar'", ProgressBar.class);
    view = Utils.findRequiredView(source, R.id.addImageBtn, "method 'addImgButton'");
    view2131296361 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.addImgButton();
      }
    });
    view = Utils.findRequiredView(source, R.id.nextBtn, "method 'nextBtn'");
    view2131298760 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.nextBtn();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    EnoSamplingHomePage target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.recycleView = null;
    target.sampleSpinner = null;
    target.locationNameET = null;
    target.progressBar = null;

    view2131296361.setOnClickListener(null);
    view2131296361 = null;
    view2131298760.setOnClickListener(null);
    view2131298760 = null;
  }
}
