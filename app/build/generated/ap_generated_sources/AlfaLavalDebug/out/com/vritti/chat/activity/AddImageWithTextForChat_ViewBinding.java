// Generated code from Butter Knife. Do not modify!
package com.vritti.chat.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.EditText;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.vritti.ekatm.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class AddImageWithTextForChat_ViewBinding implements Unbinder {
  private AddImageWithTextForChat target;

  private View view2131299109;

  @UiThread
  public AddImageWithTextForChat_ViewBinding(AddImageWithTextForChat target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public AddImageWithTextForChat_ViewBinding(final AddImageWithTextForChat target, View source) {
    this.target = target;

    View view;
    target.sdv_Image = Utils.findRequiredViewAsType(source, R.id.sdv_Image, "field 'sdv_Image'", SimpleDraweeView.class);
    target.img_message = Utils.findRequiredViewAsType(source, R.id.img_message, "field 'img_message'", EditText.class);
    view = Utils.findRequiredView(source, R.id.sendMessage, "method 'sendMessage'");
    view2131299109 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.sendMessage();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    AddImageWithTextForChat target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.sdv_Image = null;
    target.img_message = null;

    view2131299109.setOnClickListener(null);
    view2131299109 = null;
  }
}
