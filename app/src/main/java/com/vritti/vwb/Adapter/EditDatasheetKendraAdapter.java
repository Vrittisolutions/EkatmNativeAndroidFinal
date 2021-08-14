package com.vritti.vwb.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vritti.ekatm.R;
import com.vritti.vwb.Beans.EditDatasheet;
import com.vritti.vwb.vworkbench.EditDatasheetActivityMain;

import java.util.ArrayList;
import java.util.List;


public class EditDatasheetKendraAdapter extends BaseAdapter {
	List<EditDatasheet> datasheetlists;
	Context context;
	private ViewHolder holder;
	EditDatasheet datasheet;
	int flagFromTeam;
	String designation = "";

	public EditDatasheetKendraAdapter(Context context, List<EditDatasheet> datasheetlists) {
		super();
		this.context = context;
		this.datasheetlists = datasheetlists;
	}


	public EditDatasheetKendraAdapter(Context context, ArrayList<EditDatasheet> datasheetlists, int flagFromTeam, String designation) {

		this.context = context;
		this.datasheetlists = datasheetlists;
		this.flagFromTeam = flagFromTeam;
		this.designation = designation;

	}
	@Override
	public int getCount() {

		if (datasheetlists != null)
			return datasheetlists.size();

		return 0;
	}

	@Override
	public Object getItem(int pos) {

		return datasheetlists.get(pos);
	}

	@Override
	public long getItemId(int pos) {

		return pos;
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		final int pos = position;

		if (view == null) {
			holder = new ViewHolder();

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			view = inflater
					.inflate(R.layout.vwb_custom_datasheet_sahara, viewGroup, false);
			holder.txtQuestion = (TextView) view.findViewById(R.id.txtquestion);
			holder.txtAnswer = (TextView) view.findViewById(R.id.txtanswer);
			//holder.txt_approve = view.findViewById(R.id.txt_approve);
			//holder.txt_disapprove = view.findViewById(R.id.txt_disapprove);
			//holder.txt_dispname = view.findViewById(R.id.txt_dispname);
			holder.ln_main = view.findViewById(R.id.ln_main);
			holder.attachment_count = view.findViewById(R.id.attachment_count);
			holder.rel_attachment = view.findViewById(R.id.rel_attachment);

			view.setTag(holder);

		} else {
			holder = (ViewHolder) view.getTag();
		}

		// if (datasheetlists.get(pos).getFlag() == 1) {

		//	if (datasheetlists.get(pos).getIsResponseMandatory().equals("Y")) {

		holder.txtQuestion.setText(datasheetlists.get(pos).getQuesText());

		if(datasheetlists.get(pos).isSelected()){
			holder.txt_dispname.setVisibility(View.VISIBLE);
			if(datasheetlists.get(pos).getIsApproved() == 1){
				holder.txt_dispname.setText("Approved");
				holder.txt_dispname.setTextColor(Color.parseColor("#27C24C"));

			}else{
				holder.txt_dispname.setText("Disapproved");
				holder.txt_dispname.setTextColor(Color.parseColor("#F05050"));
			}
		}else {
			if( datasheetlists.get(pos).getApprStatus().equalsIgnoreCase("Yes")){
				holder.txt_dispname.setVisibility(View.VISIBLE);
				holder.txt_dispname.setText("Approved");
				holder.ln_main.setClickable(false);
				holder.txt_dispname.setTextColor(Color.parseColor("#27C24C"));

			}else if( datasheetlists.get(pos).getApprStatus().equalsIgnoreCase("No")){
				holder.txt_dispname.setVisibility(View.VISIBLE);
				holder.txt_dispname.setText("Disapproved");
				holder.txt_dispname.setTextColor(Color.parseColor("#F05050"));
			}else if( datasheetlists.get(pos).getApprStatus().equalsIgnoreCase(null)){
				holder.txt_dispname.setVisibility(View.GONE);
			}else{
				holder.txt_dispname.setVisibility(View.GONE);

			}
		}

		if(datasheetlists.get(pos).getAttachmentCount().equalsIgnoreCase("0")
				|| datasheetlists.get(pos).getAttachmentCount().equalsIgnoreCase("")){

			holder.attachment_count.setText("0");

		}else{
			holder.attachment_count.setText(datasheetlists.get(pos).getAttachmentCount());
		}


		// holder.txtQuestion.setTextColor(Color.RED);
		Log.d("test", "Que :" + datasheetlists.get(pos).getQuesText());

		String Answer=datasheetlists.get(pos).getResponsebycustomer();

		if (Answer.contains("@")){
			Answer = Answer.replaceAll("@", "");
			holder.txtAnswer.setText(Answer);

		}else {
			holder.txtAnswer.setText(Answer);
		}

		holder.txt_approve.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				datasheetlists.get(pos).setSelected(true);
				datasheetlists.get(pos).setIsApproved(1);
				//pkcssdtId

				((EditDatasheetActivityMain)context).updateAppr(pos);


				notifyDataSetChanged();
			}
		});

		holder.txt_disapprove.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				datasheetlists.get(pos).setSelected(true);
				datasheetlists.get(pos).setIsApproved(0);
				((EditDatasheetActivityMain)context).updateAppr(pos);
				notifyDataSetChanged();

			}
		});

		holder.rel_attachment.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String pkCssDetailId = datasheetlists.get(pos).getPkcssdtlsid();
				((EditDatasheetActivityMain)context).attachmentDetailsShow(pos);

			}
		});







//		} else if (datasheetlists.get(pos).getIsResponseMandatory().equals("N")) {
//			holder.txtQuestion.setText(datasheetlists.get(pos).getQuesText());
//		//	holder.txtQuestion.setTextColor(Color.BLUE);
//
//			Log.d("test", "Que :" + datasheetlists.get(pos).getQuesText());
//			//holder.txtAnswer.setText(datasheetlists.get(pos).getAnswer());
//
//		}
		// } else {
		// if (datasheetlists.get(pos).getIsResponseMandatory().equals("Y")) {
		//
		// holder.txtQuestion.setText(datasheetlists.get(pos)
		// .getQuesText());
		// holder.txtQuestion.setTextColor(Color.RED);
		//
		// Log.d("test", "Que :" + datasheetlists.get(pos).getQuesText());
		// } else if (datasheetlists.get(pos).getIsResponseMandatory()
		// .equals("N")) {
		// holder.txtQuestion.setText(datasheetlists.get(pos)
		// .getQuesText());
		// holder.txtQuestion.setTextColor(Color.BLUE);
		// Log.d("test", "Que :" + datasheetlists.get(pos).getQuesText());
		// }
		// }
		return view;
	}

	static class ViewHolder {

		TextView txtQuestion, txtAnswer;
		//Button btn_approve,btn_disapprove;
		TextView txt_dispname,txt_approve,txt_disapprove;
		LinearLayout ln_main;
		TextView attachment_count;
		RelativeLayout rel_attachment;
		// ImageButton imgcomplete;

	}

	public void update(ArrayList<EditDatasheet> editdatasheetlists) {
		datasheetlists = editdatasheetlists;
		notifyDataSetChanged();
	}


}
