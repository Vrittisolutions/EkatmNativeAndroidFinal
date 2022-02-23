// Generated code from Butter Knife. Do not modify!
package com.vritti.crm.adapter;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.vritti.ekatm.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CallLogDetailsAdapter$ActivityHolder_ViewBinding implements Unbinder {
  private CallLogDetailsAdapter.ActivityHolder target;

  private View view2131296790;

  @UiThread
  public CallLogDetailsAdapter$ActivityHolder_ViewBinding(final CallLogDetailsAdapter.ActivityHolder target,
      View source) {
    this.target = target;

    View view;
    target.txt_number = Utils.findRequiredViewAsType(source, R.id.txt_number, "field 'txt_number'", TextView.class);
    target.txt_customerName = Utils.findRequiredViewAsType(source, R.id.txt_customerName, "field 'txt_customerName'", TextView.class);
    target.txt_durationdetails = Utils.findRequiredViewAsType(source, R.id.txt_durationdetails, "field 'txt_durationdetails'", TextView.class);
    target.img = Utils.findRequiredViewAsType(source, R.id.img, "field 'img'", ImageView.class);
    target.txt_calltypename = Utils.findRequiredViewAsType(source, R.id.txt_calltypename, "field 'txt_calltypename'", TextView.class);
    view = Utils.findRequiredView(source, R.id.card_view, "field 'card_view' and method 'rowClick'");
    target.card_view = Utils.castView(view, R.id.card_view, "field 'card_view'", CardView.class);
    view2131296790 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.rowClick();
      }
    });
    target.ln_main = Utils.findRequiredViewAsType(source, R.id.ln_main, "field 'ln_main'", LinearLayout.class);
    target.txt_call_state = Utils.findRequiredViewAsType(source, R.id.txt_call_state, "field 'txt_call_state'", TextView.class);
    target.txt_firmname = Utils.findRequiredViewAsType(source, R.id.txt_firmname, "field 'txt_firmname'", TextView.class);
    target.txt_mobile = Utils.findRequiredViewAsType(source, R.id.txt_mobile, "field 'txt_mobile'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    CallLogDetailsAdapter.ActivityHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.txt_number = null;
    target.txt_customerName = null;
    target.txt_durationdetails = null;
    target.img = null;
    target.txt_calltypename = null;
    target.card_view = null;
    target.ln_main = null;
    target.txt_call_state = null;
    target.txt_firmname = null;
    target.txt_mobile = null;

    view2131296790.setOnClickListener(null);
    view2131296790 = null;
  }
}
