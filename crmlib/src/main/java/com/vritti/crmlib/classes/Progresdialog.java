package com.vritti.crmlib.classes;


import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.vritti.crmlib.R;

public class Progresdialog extends Dialog {
	public Progresdialog(Context context) {
		super(context);
	}

	public Progresdialog(Context context, int theme) {
		super(context, theme);
	}

	/*public void onWindowFocusChanged(boolean hasFocus){
		ImageView imageView = (ImageView) findViewById(R.id.spinnerImageView);
        AnimationDrawable spinner = (AnimationDrawable) imageView.getBackground();
        spinner.start();
    }*/
	
	/*public void setMessage(CharSequence message) {
		if(message != null && message.length() > 0) {
			findViewById(R.id.message).setVisibility(View.VISIBLE);			
			TextView txt = (TextView)findViewById(R.id.message);
			txt.setText(message);
			txt.invalidate();
		}
	}*/
	
	public static Progresdialog show(Context context, CharSequence message, boolean indeterminate, boolean cancelable,
                                     OnCancelListener cancelListener) {
		Progresdialog dialog = new Progresdialog(context);
		dialog.setTitle("");
		dialog.setContentView(R.layout.crm_progress_login);
		if(message == null || message.length() == 0) {
			dialog.findViewById(R.id.progressbar).setVisibility(View.GONE);
		} else {
			TextView txt = (TextView)dialog.findViewById(R.id.progressbar);
			txt.setText(message);
		}
		dialog.setCancelable(cancelable);
		//dialog.setCanceledOnTouchOutside(true);
		dialog.setOnCancelListener(cancelListener);
		dialog.getWindow().getAttributes().gravity=Gravity.CENTER;
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();  
		lp.dimAmount=0.2f;
		dialog.getWindow().setAttributes(lp); 
		//dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		dialog.show();
		return dialog;
	}	
}
