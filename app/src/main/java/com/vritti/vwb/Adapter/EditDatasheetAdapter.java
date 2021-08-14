package com.vritti.vwb.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.vritti.SaharaModule.SaharaBeans.AttachmentBean;
import com.vritti.ekatm.Constants;
import com.vritti.vwb.Beans.EditDatasheet;
import com.vritti.ekatm.R;
import com.vritti.vwb.vworkbench.EditDatasheetActivityMain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class EditDatasheetAdapter extends BaseAdapter {
	ArrayList<EditDatasheet> datasheetlists;
	List<AttachmentBean> attachmentBeanList;
	Context context;
	private ViewHolder holder;
	EditDatasheet datasheet;
	String Designation = "";
	int flagfromTeam = -1;

	public EditDatasheetAdapter(Context context, ArrayList<EditDatasheet> datasheetlists, int flagFromTeam, String designation) {
		super();
		this.context = context;
		this.datasheetlists = datasheetlists;
		this.Designation = designation;
		this.flagfromTeam = flagFromTeam;
	}

	public void update(ArrayList<EditDatasheet> editDatasheetArrayList) {
		datasheetlists = editDatasheetArrayList;
		notifyDataSetChanged();
	}

	public void deleteChange(ArrayList<EditDatasheet> editDatasheetArrayList) {

		datasheetlists = datasheetlists;
		notifyDataSetChanged();
	}

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
			if(Constants.type == Constants.Type.Sahara /*|| Constants.type == Constants.Type.ZP*/) {
				view = inflater.inflate(R.layout.vwb_custom_datasheet, viewGroup, false);
			}else{
				view = inflater.inflate(R.layout.vwb_custom_datasheet_others, viewGroup, false);
			}
			holder.txtQuestion = (TextView) view.findViewById(R.id.txtquestion);
			holder.txtAnswer = (TextView) view.findViewById(R.id.txtanswer);
			holder.txtremark = (TextView) view.findViewById(R.id.txtremark);
			holder.txt_remarkstatus = (TextView) view.findViewById(R.id.txt_remarkstatus);
			holder.rel_statusimg = view.findViewById(R.id.rel_statusimg);
			holder.img_attachment = view.findViewById(R.id.img_attachment);
			holder.attachment_count = view.findViewById(R.id.attachment_count);
			holder.ln_View = view.findViewById(R.id.cView);
			holder.img_appr = view.findViewById(R.id.img_appr);
			holder.rel_appr = view.findViewById(R.id.rel_appr);
			holder.rel_attachment = view.findViewById(R.id.rel_attachment);
			view.setTag(holder);

		} else {
			holder = (ViewHolder) view.getTag();
		}


		holder.rel_appr.setVisibility(View.GONE);

		if (Constants.type == Constants.Type.Sahara /*|| Constants.type == Constants.Type.ZP*/) {
			if (flagfromTeam == 1 && Designation.equalsIgnoreCase("school")) {
				holder.rel_statusimg.setVisibility(View.GONE);
				holder.txtAnswer.setVisibility(View.GONE);
				holder.txtremark.setVisibility(View.GONE);
				holder.txt_remarkstatus.setVisibility(View.GONE);
				holder.txtQuestion.setText(datasheetlists.get(pos).getQuesText());


			} else {
				holder.rel_statusimg.setVisibility(View.VISIBLE);
				holder.txtAnswer.setVisibility(View.VISIBLE);
				holder.txtremark.setVisibility(View.VISIBLE);
				holder.txt_remarkstatus.setVisibility(View.VISIBLE);


				holder.txtQuestion.setText(datasheetlists.get(pos).getQuesText());
				String Answer = datasheetlists.get(pos).getResponsebycustomer();
				if (Answer.equals("")) {
					holder.txtAnswer.setVisibility(View.GONE);
				} else {
					holder.txtAnswer.setVisibility(View.VISIBLE);
					if (Answer.contains("@")) {
						Answer = Answer.replaceAll("@", "");
						holder.txtAnswer.setText(Answer);

					} else {
						holder.txtAnswer.setText(Answer);

					}
				}

				String date = datasheetlists.get(pos).getAddeddt();
				String date1 = "";
				if (date != null) {
					date1 = formateDateFromstring("yyyy-MM-dd hh:mm:ss", "dd/MM/yyyy hh:mm", date);
					//Extension Officer-Extension Officer 1, Approved on 20/01/2020 11:56

					if (datasheetlists.get(pos).getIsApprDisAppr().equalsIgnoreCase("Approved") ||
							datasheetlists.get(pos).getIsApprDisAppr().equalsIgnoreCase("Disapproved")) {
						holder.txtremark.setVisibility(View.VISIBLE);
						String append_String = datasheetlists.get(pos).getDescr() + "-" + datasheetlists.get(pos).getUsername() + " , " + datasheetlists.get(pos).getIsApprDisAppr() + " on " + date1;
						holder.txtremark.setText(append_String);
					} else {

						holder.txtremark.setVisibility(View.VISIBLE);
						String append_String = datasheetlists.get(pos).getDescr() + "-" + datasheetlists.get(pos).getUsername() + " , on " + date1;
						holder.txtremark.setText(append_String);
					}
				} else {
					holder.txtremark.setVisibility(View.GONE);
				}


				if (datasheetlists.get(pos).getRemark() != null) {
					if (datasheetlists.get(pos).getRemark().equals("")) {
						holder.txt_remarkstatus.setVisibility(View.GONE);
					} else {
						holder.txt_remarkstatus.setVisibility(View.VISIBLE);
						holder.txt_remarkstatus.setText(datasheetlists.get(pos).getRemark());
					}
				} else {
					holder.txt_remarkstatus.setVisibility(View.GONE);
				}

			}
		} else {
			holder.rel_statusimg.setVisibility(View.GONE);
			holder.txtAnswer.setVisibility(View.VISIBLE);
			holder.txtremark.setVisibility(View.VISIBLE);
			holder.txt_remarkstatus.setVisibility(View.GONE);


			holder.txtQuestion.setText(datasheetlists.get(pos).getQuesText());
			String Answer = datasheetlists.get(pos).getResponsebycustomer();
			if (Answer.equals("")) {
				holder.txtAnswer.setVisibility(View.GONE);
			} else {
				holder.txtAnswer.setVisibility(View.VISIBLE);
				if (Answer.contains("@")) {
					Answer = Answer.replaceAll("@", "");
					holder.txtAnswer.setText(Answer);

				} else {
					holder.txtAnswer.setText(Answer);

				}
			}
		}


		if (datasheetlists.get(pos).getFilePathName() == null) {
			if (datasheetlists.get(pos).getAttachmentCount().equals("0"))
				holder.attachment_count.setText("0");
			else
				holder.attachment_count.setText(String.valueOf(datasheetlists.get(pos).getAttachmentCount()));
		} else {

			if (datasheetlists.get(pos).getFilePathName().size() > 0) {
				holder.attachment_count.setText(String.valueOf(datasheetlists.get(pos).getFilePathName().size()));
				datasheetlists.get(pos).setAttachmentCount(String.valueOf(datasheetlists.get(pos).getFilePathName().size()));
			} else {
				holder.attachment_count.setText("0");
			}
		}


		holder.rel_attachment.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String pkCssDetailId = String.valueOf(datasheetlists.get(pos).getPkcssdtlsid());
				((EditDatasheetActivityMain) context).attachmentDetailsShow(pos);

			}
		});


		if (position % 2 == 1) {
			holder.ln_View.setBackgroundColor(Color.parseColor("#DBE8EA"));
		} else {
			holder.ln_View.setBackgroundColor(Color.parseColor("#F1F6F7"));

		}


		holder.rel_statusimg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				((EditDatasheetActivityMain) context).remarksDetails(pos, true);
			}
		});


		return view;
	}

	public void updateCount(int selectedPos, int size, int attachPos) {
		datasheetlists.get(selectedPos).setAttachmentCount(String.valueOf(size));
		if (datasheetlists.get(selectedPos).getFilePathName() != null && datasheetlists.get(selectedPos).getFilePathName().size() != 0) {
			datasheetlists.get(selectedPos).getFilePathName().remove(attachPos);
		}
		notifyDataSetChanged();
	}


	static class ViewHolder {

		RelativeLayout rel_statusimg, rel_appr, rel_attachment;
		TextView txtremark, txt_remarkstatus;
		ImageView img_attachment, img_appr;
		TextView attachment_count;
		TextView txtQuestion, txtAnswer;
		LinearLayout ln_View;
		// ImageButton imgcomplete;

	}


	public static String formateDateFromstring(String inputFormat, String outputFormat, String inputDate) {

		Date parsed = null;
		String outputDate = "";

		SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, java.util.Locale.getDefault());
		SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, java.util.Locale.getDefault());

		try {
			parsed = df_input.parse(inputDate);
			outputDate = df_output.format(parsed);

		} catch (ParseException e) {
			e.printStackTrace();

		}

		return outputDate;

	}

}
