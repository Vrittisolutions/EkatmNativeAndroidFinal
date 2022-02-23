// Generated code from Butter Knife. Do not modify!
package com.vritti.chat.adapter;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.github.rtoshiro.view.video.FullscreenVideoLayout;
import com.vritti.ekatm.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class GroupChatRecycleViewAdapter$ChatHolder_ViewBinding implements Unbinder {
  private GroupChatRecycleViewAdapter.ChatHolder target;

  private View view2131298814;

  private View view2131298815;

  private View view2131299909;

  private View view2131296766;

  private View view2131296361;

  private View view2131297719;

  private View view2131297845;

  private View view2131298853;

  private View view2131298826;

  @UiThread
  public GroupChatRecycleViewAdapter$ChatHolder_ViewBinding(final GroupChatRecycleViewAdapter.ChatHolder target,
      View source) {
    this.target = target;

    View view;
    target.messageTextView = Utils.findOptionalViewAsType(source, R.id.textview_message, "field 'messageTextView'", TextView.class);
    target.timeTextView = Utils.findOptionalViewAsType(source, R.id.textview_time, "field 'timeTextView'", TextView.class);
    target.textview_username = Utils.findOptionalViewAsType(source, R.id.textview_username, "field 'textview_username'", TextView.class);
    target.textview_time1 = Utils.findOptionalViewAsType(source, R.id.textview_time1, "field 'textview_time1'", TextView.class);
    view = source.findViewById(R.id.outgoing_img_chat);
    target.outgoing_img_chat = Utils.castView(view, R.id.outgoing_img_chat, "field 'outgoing_img_chat'", SimpleDraweeView.class);
    if (view != null) {
      view2131298814 = view;
      view.setOnClickListener(new DebouncingOnClickListener() {
        @Override
        public void doClick(View p0) {
          target.setOutgoing_imgChat();
        }
      });
      view.setOnLongClickListener(new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View p0) {
          return target.imageLongPress();
        }
      });
    }
    target.outgoing_rel_image = Utils.findOptionalViewAsType(source, R.id.outgoing_rel_image, "field 'outgoing_rel_image'", RelativeLayout.class);
    view = source.findViewById(R.id.outgoing_layout_bubble);
    target.outgoing_layout_bubble = Utils.castView(view, R.id.outgoing_layout_bubble, "field 'outgoing_layout_bubble'", LinearLayout.class);
    if (view != null) {
      view2131298815 = view;
      view.setOnClickListener(new DebouncingOnClickListener() {
        @Override
        public void doClick(View p0) {
          target.clickForreply();
        }
      });
      view.setOnLongClickListener(new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View p0) {
          return target.setClickView(p0);
        }
      });
    }
    view = source.findViewById(R.id.txt_download);
    target.txt_download = Utils.castView(view, R.id.txt_download, "field 'txt_download'", ImageView.class);
    if (view != null) {
      view2131299909 = view;
      view.setOnClickListener(new DebouncingOnClickListener() {
        @Override
        public void doClick(View p0) {
          target.setTxt_download();
        }
      });
    }
    target.img_progress1 = Utils.findOptionalViewAsType(source, R.id.img_progress1, "field 'img_progress1'", ProgressBar.class);
    target.textview_username1 = Utils.findOptionalViewAsType(source, R.id.textview_username1, "field 'textview_username1'", TextView.class);
    target.play1 = Utils.findOptionalViewAsType(source, R.id.play1, "field 'play1'", ImageView.class);
    target.pause1 = Utils.findOptionalViewAsType(source, R.id.pause1, "field 'pause1'", ImageView.class);
    target.media_seekbar = Utils.findOptionalViewAsType(source, R.id.media_seekbar, "field 'media_seekbar'", SeekBar.class);
    target.run_time = Utils.findOptionalViewAsType(source, R.id.run_time, "field 'run_time'", TextView.class);
    target.total_time = Utils.findOptionalViewAsType(source, R.id.total_time, "field 'total_time'", TextView.class);
    target.len_audio_play = Utils.findOptionalViewAsType(source, R.id.len_audio_play, "field 'len_audio_play'", LinearLayout.class);
    target.textview_mp3_username = Utils.findOptionalViewAsType(source, R.id.textview_mp3_username, "field 'textview_mp3_username'", TextView.class);
    target.textview_mp3_time = Utils.findOptionalViewAsType(source, R.id.textview_mp3_time, "field 'textview_mp3_time'", TextView.class);
    target.txt_mp3_download = Utils.findOptionalViewAsType(source, R.id.txt_mp3_download, "field 'txt_mp3_download'", TextView.class);
    target.txt_video_download = Utils.findOptionalViewAsType(source, R.id.txt_video_download, "field 'txt_video_download'", TextView.class);
    target.play_pause_layout = Utils.findOptionalViewAsType(source, R.id.play_pause_layout, "field 'play_pause_layout'", LinearLayout.class);
    target.textview_video_username = Utils.findOptionalViewAsType(source, R.id.textview_video_username, "field 'textview_video_username'", TextView.class);
    target.rel_video_view = Utils.findOptionalViewAsType(source, R.id.rel_video_view, "field 'rel_video_view'", RelativeLayout.class);
    target.textview_video_time = Utils.findOptionalViewAsType(source, R.id.textview_video_time, "field 'textview_video_time'", TextView.class);
    target.txt_image_delivered = Utils.findOptionalViewAsType(source, R.id.txt_image_delivered, "field 'txt_image_delivered'", ImageView.class);
    target.txt_message_delivered = Utils.findOptionalViewAsType(source, R.id.txt_message_delivered, "field 'txt_message_delivered'", ImageView.class);
    target.txt_message_delivered2 = Utils.findOptionalViewAsType(source, R.id.txt_message_delivered2, "field 'txt_message_delivered2'", ImageView.class);
    target.contactLayout = Utils.findOptionalViewAsType(source, R.id.contactLayout, "field 'contactLayout'", RelativeLayout.class);
    view = source.findViewById(R.id.callBtn);
    target.callBtn = Utils.castView(view, R.id.callBtn, "field 'callBtn'", Button.class);
    if (view != null) {
      view2131296766 = view;
      view.setOnClickListener(new DebouncingOnClickListener() {
        @Override
        public void doClick(View p0) {
          target.setCallBtn();
        }
      });
    }
    view = source.findViewById(R.id.addContactBtn);
    target.addContactBtn = Utils.castView(view, R.id.addContactBtn, "field 'addContactBtn'", Button.class);
    if (view != null) {
      view2131296361 = view;
      view.setOnClickListener(new DebouncingOnClickListener() {
        @Override
        public void doClick(View p0) {
          target.setAddContactBtn();
        }
      });
    }
    target.contectUserName = Utils.findOptionalViewAsType(source, R.id.contectUserName, "field 'contectUserName'", TextView.class);
    target.date = Utils.findOptionalViewAsType(source, R.id.date, "field 'date'", TextView.class);
    target.replyLayout = Utils.findOptionalViewAsType(source, R.id.replyLayout, "field 'replyLayout'", RelativeLayout.class);
    target.replyImage = Utils.findOptionalViewAsType(source, R.id.replyImage, "field 'replyImage'", SimpleDraweeView.class);
    target.replyImage1 = Utils.findOptionalViewAsType(source, R.id.replyImage1, "field 'replyImage1'", SimpleDraweeView.class);
    view = source.findViewById(R.id.img_chat);
    target.img_chat = Utils.castView(view, R.id.img_chat, "field 'img_chat'", ImageView.class);
    if (view != null) {
      view2131297719 = view;
      view.setOnClickListener(new DebouncingOnClickListener() {
        @Override
        public void doClick(View p0) {
          target.setImg_chat();
        }
      });
      view.setOnLongClickListener(new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View p0) {
          return target.imageLongPress();
        }
      });
    }
    target.rel_image = Utils.findOptionalViewAsType(source, R.id.rel_image, "field 'rel_image'", RelativeLayout.class);
    view = source.findViewById(R.id.incoming_layout_bubble);
    target.incoming_layout_bubble = Utils.castView(view, R.id.incoming_layout_bubble, "field 'incoming_layout_bubble'", LinearLayout.class);
    if (view != null) {
      view2131297845 = view;
      view.setOnClickListener(new DebouncingOnClickListener() {
        @Override
        public void doClick(View p0) {
          target.clickForreply();
        }
      });
      view.setOnLongClickListener(new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View p0) {
          return target.setClickView(p0);
        }
      });
    }
    view = source.findViewById(R.id.play);
    target.play = Utils.castView(view, R.id.play, "field 'play'", ImageView.class);
    if (view != null) {
      view2131298853 = view;
      view.setOnClickListener(new DebouncingOnClickListener() {
        @Override
        public void doClick(View p0) {
          target.setPlay1();
        }
      });
    }
    view = source.findViewById(R.id.pause);
    target.pause = Utils.castView(view, R.id.pause, "field 'pause'", ImageView.class);
    if (view != null) {
      view2131298826 = view;
      view.setOnClickListener(new DebouncingOnClickListener() {
        @Override
        public void doClick(View p0) {
          target.setPauseMusic();
        }
      });
    }
    target.playIcon = Utils.findOptionalViewAsType(source, R.id.playIcon, "field 'playIcon'", ImageView.class);
    target.video_player_view = Utils.findOptionalViewAsType(source, R.id.video_player_view, "field 'video_player_view'", FullscreenVideoLayout.class);
    target.replyText = Utils.findOptionalViewAsType(source, R.id.replyText, "field 'replyText'", TextView.class);
    target.userNameText = Utils.findOptionalViewAsType(source, R.id.userNameText, "field 'userNameText'", TextView.class);
    target.closeImg = Utils.findOptionalViewAsType(source, R.id.closeImg, "field 'closeImg'", ImageView.class);
    target.textview_time_contact = Utils.findOptionalViewAsType(source, R.id.textview_time_contact, "field 'textview_time_contact'", TextView.class);
    target.rootLayout = Utils.findOptionalViewAsType(source, R.id.rootLayout, "field 'rootLayout'", LinearLayout.class);
    target.imageMessage = Utils.findOptionalViewAsType(source, R.id.imageMessage, "field 'imageMessage'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    GroupChatRecycleViewAdapter.ChatHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.messageTextView = null;
    target.timeTextView = null;
    target.textview_username = null;
    target.textview_time1 = null;
    target.outgoing_img_chat = null;
    target.outgoing_rel_image = null;
    target.outgoing_layout_bubble = null;
    target.txt_download = null;
    target.img_progress1 = null;
    target.textview_username1 = null;
    target.play1 = null;
    target.pause1 = null;
    target.media_seekbar = null;
    target.run_time = null;
    target.total_time = null;
    target.len_audio_play = null;
    target.textview_mp3_username = null;
    target.textview_mp3_time = null;
    target.txt_mp3_download = null;
    target.txt_video_download = null;
    target.play_pause_layout = null;
    target.textview_video_username = null;
    target.rel_video_view = null;
    target.textview_video_time = null;
    target.txt_image_delivered = null;
    target.txt_message_delivered = null;
    target.txt_message_delivered2 = null;
    target.contactLayout = null;
    target.callBtn = null;
    target.addContactBtn = null;
    target.contectUserName = null;
    target.date = null;
    target.replyLayout = null;
    target.replyImage = null;
    target.replyImage1 = null;
    target.img_chat = null;
    target.rel_image = null;
    target.incoming_layout_bubble = null;
    target.play = null;
    target.pause = null;
    target.playIcon = null;
    target.video_player_view = null;
    target.replyText = null;
    target.userNameText = null;
    target.closeImg = null;
    target.textview_time_contact = null;
    target.rootLayout = null;
    target.imageMessage = null;

    if (view2131298814 != null) {
      view2131298814.setOnClickListener(null);
      view2131298814.setOnLongClickListener(null);
      view2131298814 = null;
    }
    if (view2131298815 != null) {
      view2131298815.setOnClickListener(null);
      view2131298815.setOnLongClickListener(null);
      view2131298815 = null;
    }
    if (view2131299909 != null) {
      view2131299909.setOnClickListener(null);
      view2131299909 = null;
    }
    if (view2131296766 != null) {
      view2131296766.setOnClickListener(null);
      view2131296766 = null;
    }
    if (view2131296361 != null) {
      view2131296361.setOnClickListener(null);
      view2131296361 = null;
    }
    if (view2131297719 != null) {
      view2131297719.setOnClickListener(null);
      view2131297719.setOnLongClickListener(null);
      view2131297719 = null;
    }
    if (view2131297845 != null) {
      view2131297845.setOnClickListener(null);
      view2131297845.setOnLongClickListener(null);
      view2131297845 = null;
    }
    if (view2131298853 != null) {
      view2131298853.setOnClickListener(null);
      view2131298853 = null;
    }
    if (view2131298826 != null) {
      view2131298826.setOnClickListener(null);
      view2131298826 = null;
    }
  }
}
