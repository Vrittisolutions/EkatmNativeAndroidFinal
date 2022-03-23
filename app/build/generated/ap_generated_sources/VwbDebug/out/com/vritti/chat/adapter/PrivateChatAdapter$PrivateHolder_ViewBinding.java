// Generated code from Butter Knife. Do not modify!
package com.vritti.chat.adapter;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.vritti.ekatm.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class PrivateChatAdapter$PrivateHolder_ViewBinding implements Unbinder {
  private PrivateChatAdapter.PrivateHolder target;

  private View view2131298182;

  @UiThread
  public PrivateChatAdapter$PrivateHolder_ViewBinding(final PrivateChatAdapter.PrivateHolder target,
      View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.len, "method 'rowclick'");
    view2131298182 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.rowclick();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    target = null;


    view2131298182.setOnClickListener(null);
    view2131298182 = null;
  }
}
