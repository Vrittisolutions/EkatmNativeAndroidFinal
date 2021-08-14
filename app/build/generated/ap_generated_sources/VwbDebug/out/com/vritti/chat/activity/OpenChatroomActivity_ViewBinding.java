// Generated code from Butter Knife. Do not modify!
package com.vritti.chat.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.vritti.ekatm.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class OpenChatroomActivity_ViewBinding implements Unbinder {
  private OpenChatroomActivity target;

  @UiThread
  public OpenChatroomActivity_ViewBinding(OpenChatroomActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public OpenChatroomActivity_ViewBinding(OpenChatroomActivity target, View source) {
    this.target = target;

    target.listview_multiple_group = Utils.findRequiredViewAsType(source, R.id.listview_multiple_group, "field 'listview_multiple_group'", RecyclerView.class);
    target.edt_search_chatroomname = Utils.findRequiredViewAsType(source, R.id.edt_search_chatroomname, "field 'edt_search_chatroomname'", EditText.class);
    target.txt_chatroom_add = Utils.findRequiredViewAsType(source, R.id.txt_chatroom_add, "field 'txt_chatroom_add'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    OpenChatroomActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.listview_multiple_group = null;
    target.edt_search_chatroomname = null;
    target.txt_chatroom_add = null;
  }
}
