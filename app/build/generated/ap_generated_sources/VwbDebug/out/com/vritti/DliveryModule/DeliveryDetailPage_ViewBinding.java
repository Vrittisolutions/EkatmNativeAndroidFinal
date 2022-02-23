// Generated code from Butter Knife. Do not modify!
package com.vritti.DliveryModule;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.vritti.ekatm.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class DeliveryDetailPage_ViewBinding implements Unbinder {
  private DeliveryDetailPage target;

  private View view2131298839;

  private View view2131299031;

  private View view2131298835;

  private View view2131299051;

  private View view2131299033;

  private View view2131298813;

  private View view2131298960;

  private View view2131298961;

  private View view2131298962;

  private View view2131296399;

  private View view2131296400;

  private View view2131296809;

  private View view2131299030;

  private View view2131298836;

  private View view2131298810;

  private View view2131298811;

  @UiThread
  public DeliveryDetailPage_ViewBinding(DeliveryDetailPage target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public DeliveryDetailPage_ViewBinding(final DeliveryDetailPage target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.pickupBtn, "field 'pickupBtn' and method 'setPickupBtn'");
    target.pickupBtn = Utils.castView(view, R.id.pickupBtn, "field 'pickupBtn'", Button.class);
    view2131298839 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.setPickupBtn();
      }
    });
    view = Utils.findRequiredView(source, R.id.resheduleBtn, "field 'resheduleBtn' and method 'setResheduleBtn'");
    target.resheduleBtn = Utils.castView(view, R.id.resheduleBtn, "field 'resheduleBtn'", Button.class);
    view2131299031 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.setResheduleBtn();
      }
    });
    target.otpLayout = Utils.findRequiredViewAsType(source, R.id.otpLayout, "field 'otpLayout'", LinearLayout.class);
    view = Utils.findRequiredView(source, R.id.phoneNumber, "field 'phoneNumber' and method 'setPhoneNumber'");
    target.phoneNumber = Utils.castView(view, R.id.phoneNumber, "field 'phoneNumber'", TextView.class);
    view2131298835 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.setPhoneNumber();
      }
    });
    target.toolBar = Utils.findRequiredViewAsType(source, R.id.toolbar1, "field 'toolBar'", Toolbar.class);
    target.etOtp = Utils.findRequiredViewAsType(source, R.id.etOtp, "field 'etOtp'", EditText.class);
    target.customerName = Utils.findRequiredViewAsType(source, R.id.customerName, "field 'customerName'", TextView.class);
    target.InvoiveNumber = Utils.findRequiredViewAsType(source, R.id.InvoiveNumber, "field 'InvoiveNumber'", TextView.class);
    target.addressName = Utils.findRequiredViewAsType(source, R.id.addressName, "field 'addressName'", TextView.class);
    target.price = Utils.findRequiredViewAsType(source, R.id.price, "field 'price'", TextView.class);
    target.date = Utils.findRequiredViewAsType(source, R.id.date, "field 'date'", TextView.class);
    target.spinnerLayout = Utils.findRequiredViewAsType(source, R.id.spinnerLayout, "field 'spinnerLayout'", RelativeLayout.class);
    target.changeStatusBtn = Utils.findRequiredViewAsType(source, R.id.changeStatusBtn, "field 'changeStatusBtn'", Button.class);
    view = Utils.findRequiredView(source, R.id.route, "field 'route' and method 'setRootBtn'");
    target.route = Utils.castView(view, R.id.route, "field 'route'", ImageView.class);
    view2131299051 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.setRootBtn();
      }
    });
    target.editTextOne = Utils.findRequiredViewAsType(source, R.id.editTextone, "field 'editTextOne'", EditText.class);
    target.editTextTwo = Utils.findRequiredViewAsType(source, R.id.editTexttwo, "field 'editTextTwo'", EditText.class);
    target.editTextThree = Utils.findRequiredViewAsType(source, R.id.editTextthree, "field 'editTextThree'", EditText.class);
    target.editTextFour = Utils.findRequiredViewAsType(source, R.id.editTextfour, "field 'editTextFour'", EditText.class);
    target.editTextFive = Utils.findRequiredViewAsType(source, R.id.editTextFive, "field 'editTextFive'", EditText.class);
    target.editTextSix = Utils.findRequiredViewAsType(source, R.id.editTextSix, "field 'editTextSix'", EditText.class);
    view = Utils.findRequiredView(source, R.id.resheduleTxt, "field 'resheduleTxt' and method 'setResheduleBtn'");
    target.resheduleTxt = Utils.castView(view, R.id.resheduleTxt, "field 'resheduleTxt'", TextView.class);
    view2131299033 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.setResheduleBtn();
      }
    });
    target.wareHouseAddressTxt = Utils.findRequiredViewAsType(source, R.id.wareHouseAddressTxt, "field 'wareHouseAddressTxt'", TextView.class);
    target.time = Utils.findRequiredViewAsType(source, R.id.time, "field 'time'", TextView.class);
    view = Utils.findRequiredView(source, R.id.otpLayout_new, "field 'otpLayout_new' and method 'setOtpLayout'");
    target.otpLayout_new = Utils.castView(view, R.id.otpLayout_new, "field 'otpLayout_new'", RelativeLayout.class);
    view2131298813 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.setOtpLayout();
      }
    });
    target.ll = Utils.findRequiredViewAsType(source, R.id.ll, "field 'll'", LinearLayout.class);
    target.waereHouseLayout = Utils.findRequiredViewAsType(source, R.id.waereHouseLayout, "field 'waereHouseLayout'", RelativeLayout.class);
    target.resheduleLayout = Utils.findRequiredViewAsType(source, R.id.resheduleLayout, "field 'resheduleLayout'", RelativeLayout.class);
    target.pickUpLayout = Utils.findRequiredViewAsType(source, R.id.pickUpLayout, "field 'pickUpLayout'", CardView.class);
    target.expectedTime = Utils.findRequiredViewAsType(source, R.id.expectedTime, "field 'expectedTime'", TextView.class);
    target.expectedTimeLayout = Utils.findRequiredViewAsType(source, R.id.expectedTimeLayout, "field 'expectedTimeLayout'", RelativeLayout.class);
    target.cashAmmount = Utils.findRequiredViewAsType(source, R.id.cashAmmount, "field 'cashAmmount'", RelativeLayout.class);
    target.paymentTypeLayout = Utils.findRequiredViewAsType(source, R.id.paymentTypeLayout, "field 'paymentTypeLayout'", LinearLayout.class);
    target.editTexCash = Utils.findRequiredViewAsType(source, R.id.editTexCash, "field 'editTexCash'", EditText.class);
    view = Utils.findRequiredView(source, R.id.rbCash, "field 'rbCash' and method 'setCashBtn'");
    target.rbCash = Utils.castView(view, R.id.rbCash, "field 'rbCash'", RadioButton.class);
    view2131298960 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.setCashBtn();
      }
    });
    view = Utils.findRequiredView(source, R.id.rbOnline, "field 'rbOnline' and method 'setOnlineBtn'");
    target.rbOnline = Utils.castView(view, R.id.rbOnline, "field 'rbOnline'", RadioButton.class);
    view2131298961 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.setOnlineBtn();
      }
    });
    view = Utils.findRequiredView(source, R.id.rbPaid, "field 'rbPaid' and method 'setPaidBtn'");
    target.rbPaid = Utils.castView(view, R.id.rbPaid, "field 'rbPaid'", RadioButton.class);
    view2131298962 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.setPaidBtn();
      }
    });
    view = Utils.findRequiredView(source, R.id.arraiveBtn, "method 'setArrivedBtn'");
    view2131296399 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.setArrivedBtn();
      }
    });
    view = Utils.findRequiredView(source, R.id.arraiveBtn_next, "method 'setArrivedBtn'");
    view2131296400 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.setArrivedBtn();
      }
    });
    view = Utils.findRequiredView(source, R.id.cashBtn_new, "method 'setCash'");
    view2131296809 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.setCash();
      }
    });
    view = Utils.findRequiredView(source, R.id.resendOtp, "method 'setResend'");
    view2131299030 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.setResend();
      }
    });
    view = Utils.findRequiredView(source, R.id.phoneNumber1, "method 'setPhoneNumber'");
    view2131298836 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.setPhoneNumber();
      }
    });
    view = Utils.findRequiredView(source, R.id.otpBtn, "method 'getOtp'");
    view2131298810 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.getOtp();
      }
    });
    view = Utils.findRequiredView(source, R.id.otpBtn_new, "method 'getOtp'");
    view2131298811 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.getOtp();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    DeliveryDetailPage target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.pickupBtn = null;
    target.resheduleBtn = null;
    target.otpLayout = null;
    target.phoneNumber = null;
    target.toolBar = null;
    target.etOtp = null;
    target.customerName = null;
    target.InvoiveNumber = null;
    target.addressName = null;
    target.price = null;
    target.date = null;
    target.spinnerLayout = null;
    target.changeStatusBtn = null;
    target.route = null;
    target.editTextOne = null;
    target.editTextTwo = null;
    target.editTextThree = null;
    target.editTextFour = null;
    target.editTextFive = null;
    target.editTextSix = null;
    target.resheduleTxt = null;
    target.wareHouseAddressTxt = null;
    target.time = null;
    target.otpLayout_new = null;
    target.ll = null;
    target.waereHouseLayout = null;
    target.resheduleLayout = null;
    target.pickUpLayout = null;
    target.expectedTime = null;
    target.expectedTimeLayout = null;
    target.cashAmmount = null;
    target.paymentTypeLayout = null;
    target.editTexCash = null;
    target.rbCash = null;
    target.rbOnline = null;
    target.rbPaid = null;

    view2131298839.setOnClickListener(null);
    view2131298839 = null;
    view2131299031.setOnClickListener(null);
    view2131299031 = null;
    view2131298835.setOnClickListener(null);
    view2131298835 = null;
    view2131299051.setOnClickListener(null);
    view2131299051 = null;
    view2131299033.setOnClickListener(null);
    view2131299033 = null;
    view2131298813.setOnClickListener(null);
    view2131298813 = null;
    view2131298960.setOnClickListener(null);
    view2131298960 = null;
    view2131298961.setOnClickListener(null);
    view2131298961 = null;
    view2131298962.setOnClickListener(null);
    view2131298962 = null;
    view2131296399.setOnClickListener(null);
    view2131296399 = null;
    view2131296400.setOnClickListener(null);
    view2131296400 = null;
    view2131296809.setOnClickListener(null);
    view2131296809 = null;
    view2131299030.setOnClickListener(null);
    view2131299030 = null;
    view2131298836.setOnClickListener(null);
    view2131298836 = null;
    view2131298810.setOnClickListener(null);
    view2131298810 = null;
    view2131298811.setOnClickListener(null);
    view2131298811 = null;
  }
}
