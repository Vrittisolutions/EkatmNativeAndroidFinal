// Generated code from Butter Knife. Do not modify!
package com.vritti.chat.adapter;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.vritti.ekatm.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class AddUserAdapterForGroup$AddUserHolder_ViewBinding implements Unbinder {
  private AddUserAdapterForGroup.AddUserHolder target;

  private View view2131300458;

  private View view2131298182;

  private View view2131296910;

  @UiThread
  public AddUserAdapterForGroup$AddUserHolder_ViewBinding(final AddUserAdapterForGroup.AddUserHolder target,
      View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.user_name_data, "field 'user_name_data' and method 'chatnewUser'");
    target.user_name_data = Utils.castView(view, R.id.user_name_data, "field 'user_name_data'", TextView.class);
    view2131300458 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.chatnewUser();
      }
    });
    view = Utils.findRequiredView(source, R.id.len, "field 'len' and method 'chatnewUser'");
    target.len = Utils.castView(view, R.id.len, "field 'len'", RelativeLayout.class);
    view2131298182 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.chatnewUser();
      }
    });
    view = Utils.findRequiredView(source, R.id.checkbox_user, "field 'checkbox_user' and method 'chatUser'");
    target.checkbox_user = Utils.castView(view, R.id.checkbox_user, "field 'checkbox_user'", AppCompatCheckBox.class);
    view2131296910 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.chatUser();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    AddUserAdapterForGroup.AddUserHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.user_name_data = null;
    target.len = null;
    target.checkbox_user = null;

    view2131300458.setOnClickListener(null);
    view2131300458 = null;
    view2131298182.setOnClickListener(null);
    view2131298182 = null;
    view2131296910.setOnClickListener(null);
    view2131296910 = null;
  }
}
