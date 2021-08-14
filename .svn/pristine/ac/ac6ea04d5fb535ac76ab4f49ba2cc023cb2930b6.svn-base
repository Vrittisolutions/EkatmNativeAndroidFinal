package com.vritti.vwblib.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.vritti.vwblib.Beans.EditDatasheet;
import com.vritti.vwblib.R;

import java.util.List;


public class EditDatasheetAdapter  extends BaseAdapter {
	List<EditDatasheet> datasheetlists;
	Context context;
	private ViewHolder holder;
	EditDatasheet datasheet;

	public EditDatasheetAdapter(Context context, List<EditDatasheet> datasheetlists) {
		super();
		this.context = context;
		this.datasheetlists = datasheetlists;
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

			view = inflater
					.inflate(R.layout.vwb_custom_datasheet, viewGroup, false);
			holder.txtQuestion = (TextView) view.findViewById(R.id.txtquestion);
			holder.txtAnswer = (TextView) view.findViewById(R.id.txtanswer);

			view.setTag(holder);

		} else {
			holder = (ViewHolder) view.getTag();
		}

		// if (datasheetlists.get(pos).getFlag() == 1) {

	//	if (datasheetlists.get(pos).getIsResponseMandatory().equals("Y")) {

			holder.txtQuestion.setText(datasheetlists.get(pos).getQuesText());

			// holder.txtQuestion.setTextColor(Color.RED);
			Log.d("test", "Que :" + datasheetlists.get(pos).getQuesText());

		    String Answer=datasheetlists.get(pos).getResponsebycustomer();

		    if (Answer.contains("@")){
				Answer = Answer.replaceAll("@", "");
				holder.txtAnswer.setText(Answer);

			}else {
				holder.txtAnswer.setText(Answer);
			}


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
		// ImageButton imgcomplete;

	}
}
