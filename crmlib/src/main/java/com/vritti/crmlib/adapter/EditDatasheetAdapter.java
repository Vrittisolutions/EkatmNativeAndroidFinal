package com.vritti.crmlib.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;




import java.util.List;

import com.vritti.crmlib.R;
import com.vritti.crmlib.bean.EditDatasheet;


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
					.inflate(R.layout.crm_custom_datasheet, viewGroup, false);
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
			Log.d("crm_dialog_action", "Que :" + datasheetlists.get(pos).getQuesText());

			holder.txtAnswer.setText(datasheetlists.get(pos).getResponsebycustomer());
		

//		} else if (datasheetlists.get(pos).getIsResponseMandatory().equals("N")) {
//			holder.txtQuestion.setText(datasheetlists.get(pos).getQuesText());
//		//	holder.txtQuestion.setTextColor(Color.BLUE);
//
//			Log.d("crm_dialog_action", "Que :" + datasheetlists.get(pos).getQuesText());
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
		// Log.d("crm_dialog_action", "Que :" + datasheetlists.get(pos).getQuesText());
		// } else if (datasheetlists.get(pos).getIsResponseMandatory()
		// .equals("N")) {
		// holder.txtQuestion.setText(datasheetlists.get(pos)
		// .getQuesText());
		// holder.txtQuestion.setTextColor(Color.BLUE);
		// Log.d("crm_dialog_action", "Que :" + datasheetlists.get(pos).getQuesText());
		// }
		// }
		return view;
	}

	static class ViewHolder {

		TextView txtQuestion, txtAnswer;
		// ImageButton imgcomplete;

	}
}
