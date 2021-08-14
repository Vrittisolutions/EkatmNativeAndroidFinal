// Generated code from Butter Knife. Do not modify!
package com.vritti.chat.adapter;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.vritti.ekatm.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ChatRoomMultipleAdapterNewRecycleView$ChatRoomMultipleAdapterNewRecycleViewHolder_ViewBinding implements Unbinder {
  private ChatRoomMultipleAdapterNewRecycleView.ChatRoomMultipleAdapterNewRecycleViewHolder target;

  private View view2131296783;

  @UiThread
  public ChatRoomMultipleAdapterNewRecycleView$ChatRoomMultipleAdapterNewRecycleViewHolder_ViewBinding(final ChatRoomMultipleAdapterNewRecycleView.ChatRoomMultipleAdapterNewRecycleViewHolder target,
      View source) {
    this.target = target;

    View view;
    target.txt_chatroom_name = Utils.findRequiredViewAsType(source, R.id.txt_chatroom_name, "field 'txt_chatroom_name'", TextView.class);
    target.txt_created = Utils.findRequiredViewAsType(source, R.id.txt_created, "field 'txt_created'", TextView.class);
    target.text_cartcount = Utils.findRequiredViewAsType(source, R.id.text_cartcount, "field 'text_cartcount'", TextView.class);
    target.text_cartcountLay = Utils.findRequiredViewAsType(source, R.id.text_cartcountLay, "field 'text_cartcountLay'", RelativeLayout.class);
    target.txt_chatroom_message = Utils.findRequiredViewAsType(source, R.id.txt_chatroom_message, "field 'txt_chatroom_message'", TextView.class);
    target.txt_chat_time = Utils.findRequiredViewAsType(source, R.id.chat_time, "field 'txt_chat_time'", TextView.class);
    target.len_closed = Utils.findRequiredViewAsType(source, R.id.len_closed, "field 'len_closed'", LinearLayout.class);
    target.img_file = Utils.findRequiredViewAsType(source, R.id.img_file, "field 'img_file'", ImageView.class);
    target.profile_image = Utils.findRequiredViewAsType(source, R.id.profile_image, "field 'profile_image'", SimpleDraweeView.class);
    view = Utils.findRequiredView(source, R.id.card_view, "field 'card_view' and method 'roeClick'");
    target.card_view = Utils.castView(view, R.id.card_view, "field 'card_view'", RelativeLayout.class);
    view2131296783 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.roeClick();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    ChatRoomMultipleAdapterNewRecycleView.ChatRoomMultipleAdapterNewRecycleViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.txt_chatroom_name = null;
    target.txt_created = null;
    target.text_cartcount = null;
    target.text_cartcountLay = null;
    target.txt_chatroom_message = null;
    target.txt_chat_time = null;
    target.len_closed = null;
    target.img_file = null;
    target.profile_image = null;
    target.card_view = null;

    view2131296783.setOnClickListener(null);
    view2131296783 = null;
  }
}
