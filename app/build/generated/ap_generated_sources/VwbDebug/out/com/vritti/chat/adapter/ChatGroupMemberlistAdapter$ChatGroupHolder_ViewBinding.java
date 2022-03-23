// Generated code from Butter Knife. Do not modify!
package com.vritti.chat.adapter;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.vritti.ekatm.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ChatGroupMemberlistAdapter$ChatGroupHolder_ViewBinding implements Unbinder {
  private ChatGroupMemberlistAdapter.ChatGroupHolder target;

  private View view2131297029;

  @UiThread
  public ChatGroupMemberlistAdapter$ChatGroupHolder_ViewBinding(final ChatGroupMemberlistAdapter.ChatGroupHolder target,
      View source) {
    this.target = target;

    View view;
    target.profileImag = Utils.findRequiredViewAsType(source, R.id.profile_image, "field 'profileImag'", SimpleDraweeView.class);
    view = Utils.findRequiredView(source, R.id.delete, "field 'delete' and method 'deleteUserName'");
    target.delete = Utils.castView(view, R.id.delete, "field 'delete'", ImageView.class);
    view2131297029 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.deleteUserName();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    ChatGroupMemberlistAdapter.ChatGroupHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.profileImag = null;
    target.delete = null;

    view2131297029.setOnClickListener(null);
    view2131297029 = null;
  }
}
