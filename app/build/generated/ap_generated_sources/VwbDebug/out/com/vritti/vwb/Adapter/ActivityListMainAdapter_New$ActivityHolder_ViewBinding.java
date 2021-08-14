// Generated code from Butter Knife. Do not modify!
package com.vritti.vwb.Adapter;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.vritti.ekatm.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ActivityListMainAdapter_New$ActivityHolder_ViewBinding implements Unbinder {
  private ActivityListMainAdapter_New.ActivityHolder target;

  private View view2131296789;

  @UiThread
  public ActivityListMainAdapter_New$ActivityHolder_ViewBinding(final ActivityListMainAdapter_New.ActivityHolder target,
      View source) {
    this.target = target;

    View view;
    target.tv_activity_desc = Utils.findRequiredViewAsType(source, R.id.tv_activity_desc, "field 'tv_activity_desc'", TextView.class);
    target.tv_assignedBy = Utils.findRequiredViewAsType(source, R.id.tv_assignedBy, "field 'tv_assignedBy'", TextView.class);
    target.tv_endDate1 = Utils.findRequiredViewAsType(source, R.id.tv_endDate1, "field 'tv_endDate1'", TextView.class);
    target.tv_ConsigneeName = Utils.findRequiredViewAsType(source, R.id.tv_ConsigneeName, "field 'tv_ConsigneeName'", TextView.class);
    target.tv_activityCode = Utils.findRequiredViewAsType(source, R.id.tv_activityCode, "field 'tv_activityCode'", TextView.class);
    target.tv_workspace = Utils.findRequiredViewAsType(source, R.id.tv_workspace, "field 'tv_workspace'", TextView.class);
    target.tv_hoursreq = Utils.findRequiredViewAsType(source, R.id.tv_hoursreq, "field 'tv_hoursreq'", TextView.class);
    target.tv_hoursbook = Utils.findRequiredViewAsType(source, R.id.tv_hoursbook, "field 'tv_hoursbook'", TextView.class);
    target.tv_endDate = Utils.findRequiredViewAsType(source, R.id.tv_endDate, "field 'tv_endDate'", TextView.class);
    target.lay_ticketDesc = Utils.findRequiredViewAsType(source, R.id.lay_ticketDesc, "field 'lay_ticketDesc'", LinearLayout.class);
    view = Utils.findRequiredView(source, R.id.card_viewfill, "field 'card_viewfill', method 'header', and method 'longPress'");
    target.card_viewfill = Utils.castView(view, R.id.card_viewfill, "field 'card_viewfill'", CardView.class);
    view2131296789 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.header();
      }
    });
    view.setOnLongClickListener(new View.OnLongClickListener() {
      @Override
      public boolean onLongClick(View p0) {
        return target.longPress();
      }
    });
    target.tv_SorceType = Utils.findRequiredViewAsType(source, R.id.tv_SorceType, "field 'tv_SorceType'", ImageView.class);
    target.tv_activityStatus = Utils.findRequiredViewAsType(source, R.id.tv_activityStatus, "field 'tv_activityStatus'", ImageView.class);
    target.tv_clientName = Utils.findRequiredViewAsType(source, R.id.tv_clientName, "field 'tv_clientName'", TextView.class);
    target.tv_contact = Utils.findRequiredViewAsType(source, R.id.tv_contact, "field 'tv_contact'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ActivityListMainAdapter_New.ActivityHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.tv_activity_desc = null;
    target.tv_assignedBy = null;
    target.tv_endDate1 = null;
    target.tv_ConsigneeName = null;
    target.tv_activityCode = null;
    target.tv_workspace = null;
    target.tv_hoursreq = null;
    target.tv_hoursbook = null;
    target.tv_endDate = null;
    target.lay_ticketDesc = null;
    target.card_viewfill = null;
    target.tv_SorceType = null;
    target.tv_activityStatus = null;
    target.tv_clientName = null;
    target.tv_contact = null;

    view2131296789.setOnClickListener(null);
    view2131296789.setOnLongClickListener(null);
    view2131296789 = null;
  }
}
