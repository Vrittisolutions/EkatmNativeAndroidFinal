// Generated code from Butter Knife. Do not modify!
package com.vritti.vwb.Adapter;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.vritti.ekatm.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class Sahara_AttachmentDetailsAdapter$AttachmentHolder_ViewBinding implements Unbinder {
  private Sahara_AttachmentDetailsAdapter.AttachmentHolder target;

  private View view2131297051;

  private View view2131297022;

  @UiThread
  public Sahara_AttachmentDetailsAdapter$AttachmentHolder_ViewBinding(final Sahara_AttachmentDetailsAdapter.AttachmentHolder target,
      View source) {
    this.target = target;

    View view;
    target.txt_attachment = Utils.findRequiredViewAsType(source, R.id.txt_attachment, "field 'txt_attachment'", TextView.class);
    view = Utils.findRequiredView(source, R.id.downloadFile, "field 'downloadFile' and method 'downloadFile'");
    target.downloadFile = Utils.castView(view, R.id.downloadFile, "field 'downloadFile'", ImageView.class);
    view2131297051 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.downloadFile();
      }
    });
    view = Utils.findRequiredView(source, R.id.deleteFile, "field 'deleteFile' and method 'deleteFile'");
    target.deleteFile = Utils.castView(view, R.id.deleteFile, "field 'deleteFile'", ImageView.class);
    view2131297022 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.deleteFile();
      }
    });
    target.cardView = Utils.findRequiredViewAsType(source, R.id.card_view, "field 'cardView'", CardView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    Sahara_AttachmentDetailsAdapter.AttachmentHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.txt_attachment = null;
    target.downloadFile = null;
    target.deleteFile = null;
    target.cardView = null;

    view2131297051.setOnClickListener(null);
    view2131297051 = null;
    view2131297022.setOnClickListener(null);
    view2131297022 = null;
  }
}
