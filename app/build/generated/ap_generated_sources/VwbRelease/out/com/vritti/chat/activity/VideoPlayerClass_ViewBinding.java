// Generated code from Butter Knife. Do not modify!
package com.vritti.chat.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.VideoView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.vritti.ekatm.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class VideoPlayerClass_ViewBinding implements Unbinder {
  private VideoPlayerClass target;

  @UiThread
  public VideoPlayerClass_ViewBinding(VideoPlayerClass target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public VideoPlayerClass_ViewBinding(VideoPlayerClass target, View source) {
    this.target = target;

    target.videoView = Utils.findRequiredViewAsType(source, R.id.videoView1, "field 'videoView'", VideoView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    VideoPlayerClass target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.videoView = null;
  }
}
