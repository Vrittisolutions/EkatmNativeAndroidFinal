// Generated code from Butter Knife. Do not modify!
package com.vritti.vwb.Adapter;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.vritti.ekatm.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class BirthdayMainAdapter$BirthDayHoldaer_ViewBinding implements Unbinder {
  private BirthdayMainAdapter.BirthDayHoldaer target;

  private View view2131297708;

  private View view2131297761;

  @UiThread
  public BirthdayMainAdapter$BirthDayHoldaer_ViewBinding(final BirthdayMainAdapter.BirthDayHoldaer target,
      View source) {
    this.target = target;

    View view;
    target.tv_userName = Utils.findRequiredViewAsType(source, R.id.tv_userName, "field 'tv_userName'", TextView.class);
    target.tv_bod = Utils.findRequiredViewAsType(source, R.id.tv_bod, "field 'tv_bod'", TextView.class);
    view = Utils.findRequiredView(source, R.id.img_chat, "field 'img_chat' and method 'Chatmessage'");
    target.img_chat = Utils.castView(view, R.id.img_chat, "field 'img_chat'", ImageView.class);
    view2131297708 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.Chatmessage();
      }
    });
    view = Utils.findRequiredView(source, R.id.img_message, "field 'img_message' and method 'sendmessage'");
    target.img_message = Utils.castView(view, R.id.img_message, "field 'img_message'", ImageView.class);
    view2131297761 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.sendmessage();
      }
    });
    target.profileImag = Utils.findRequiredViewAsType(source, R.id.profile_image, "field 'profileImag'", SimpleDraweeView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    BirthdayMainAdapter.BirthDayHoldaer target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.tv_userName = null;
    target.tv_bod = null;
    target.img_chat = null;
    target.img_message = null;
    target.profileImag = null;

    view2131297708.setOnClickListener(null);
    view2131297708 = null;
    view2131297761.setOnClickListener(null);
    view2131297761 = null;
  }
}
