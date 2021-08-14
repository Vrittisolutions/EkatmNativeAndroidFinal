package com.vritti.crmlib.adapter;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.vritti.crmlib.R;
import com.vritti.crmlib.bean.ProspectContact;
import com.vritti.crmlib.vcrm7.ProspectEnterpriseActivity;


public class CustomAdapterContactList extends BaseAdapter {

    private Context activity;
    private List<ProspectContact> data;
    private static LayoutInflater inflater = null;
    List<String> lstDept;
    Dialog d_contact;
    // public Resources res;
    ProspectContact tempValues;
    int i = 0;

    public CustomAdapterContactList(Context c, List<ProspectContact> d) {
        activity = c;
        data = d;
        inflater = (LayoutInflater) activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public int getCount() {

        if (data.size() <= 0)
            return 1;
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }


    public static class ViewHolder {

        public TextView tname;
        public TextView tdesignation;
        Button buttonEdit;

    }


    public View getView(final int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;

        if (convertView == null) {

            vi = inflater.inflate(R.layout.crm_row_contactitem, null);
            holder = new ViewHolder();
            holder.tname = (TextView) vi.findViewById(R.id.tvName);
            holder.tdesignation = (TextView) vi.findViewById(R.id.tvDesignation);
            holder.buttonEdit = (Button) vi.findViewById(R.id.buttonEdit);
            vi.setTag(holder);
        } else
            holder = (ViewHolder) vi.getTag();

        if (data.size() <= 0) {
            holder.tname.setText("No Data");

        } else {

            tempValues = data.get(position);
            holder.tname.setText(data.get(position).getName());
            holder.tdesignation.setText(tempValues.getDesignation() + ", " + tempValues.getDepartment());
            holder.buttonEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 //   Toast.makeText(activity, "Name" + tempValues.getName(), Toast.LENGTH_LONG).show();
                    // AddContactPopup();

                    d_contact = new Dialog(activity);
                    d_contact.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    d_contact.setContentView(R.layout.crm_popupaddcontact);

                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(d_contact.getWindow().getAttributes());
                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                    lp.gravity = Gravity.CENTER;

                    d_contact.getWindow().setAttributes(lp);


                    final Button btnSave = ((Button) d_contact
                            .findViewById(R.id.btnpopupSaveContact));
                    final Button btnCancel = ((Button) d_contact
                            .findViewById(R.id.btnpopupCancelContact));
                    final EditText eName = ((EditText) d_contact
                            .findViewById(R.id.eContactName));

                    final EditText eDesignation = ((EditText) d_contact
                            .findViewById(R.id.eContactDesignation));
                    final EditText eBirthdate = ((EditText) d_contact
                            .findViewById(R.id.eBirthdate));
                    final Spinner sDepartmnt = ((Spinner) d_contact
                            .findViewById(R.id.sDepartment));

                    final EditText cEmailId = (EditText) d_contact
                            .findViewById(R.id.econtactpEmailid);
                    final EditText cMobilee = (EditText) d_contact
                            .findViewById(R.id.econtactpMobile);
                    final EditText cTele = (EditText) d_contact
                            .findViewById(R.id.eContactpTele);
                    final EditText cFaxe = (EditText) d_contact
                            .findViewById(R.id.eContactpFax);
                    final  EditText econtactwhatsapp= (EditText) d_contact.findViewById(R.id.econtactwhatsapp);

                    eName.setText(tempValues.getName());
                    eDesignation.setText(tempValues.getDesignation());
                    eBirthdate.setText(tempValues.getBirthdate());
                    //  sDepartmnt.setText();
                    cEmailId.setText(tempValues.getEmailId());
                    cMobilee.setText(tempValues.getMobile());
                    cTele.setText(tempValues.getTelephone());
                    cFaxe.setText(tempValues.getFax());
                    econtactwhatsapp.setText(tempValues.getWhtsapp());
                    lstDept=new ArrayList<String>();
                    lstDept.add("--Select Role--");
                    lstDept.add("Decision Maker");
                    lstDept.add("HR");
                    lstDept.add("Marketing");
                    lstDept.add("Operator");
                    lstDept.add("Purchase");

                    MySpinnerAdapter customDept = new MySpinnerAdapter(activity,
                            R.layout.crm_custom_spinner_txt, lstDept);
                    sDepartmnt.setAdapter(customDept);
                    if (tempValues.getDepartment().toString().equalsIgnoreCase("\"--Select--\"")) {
                        sDepartmnt.setSelection(0);
                    }
                    else if (tempValues.getDepartment().toString().equalsIgnoreCase("Decision Maker")) {
                        sDepartmnt.setSelection(1);
                    }  else if (tempValues.getDepartment().toString().equalsIgnoreCase("HR")) {
                        sDepartmnt.setSelection(2);
                    }else if (tempValues.getDepartment().toString().equalsIgnoreCase("Marketing")) {
                        sDepartmnt.setSelection(3);
                    }else if (tempValues.getDepartment().toString().equalsIgnoreCase("Operator")) {
                        sDepartmnt.setSelection(4);
                    } else if (tempValues.getDepartment().toString().equalsIgnoreCase("Purchase")) {
                        sDepartmnt.setSelection(5);
                    }
                    eBirthdate.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            Calendar mcurrentDate = Calendar.getInstance();
                            int mYear = mcurrentDate.get(Calendar.YEAR);
                            int mMonth = mcurrentDate.get(Calendar.MONTH);
                            int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                            DatePickerDialog date = new DatePickerDialog(activity,
                                    new DatePickerDialog.OnDateSetListener() {

                                        @Override
                                        public void onDateSet(DatePicker view, int year,
                                                              int monthOfYear, int dayOfMonth) {

                                            eBirthdate.setText(year + "/"
                                                    + String.format("%02d", (monthOfYear + 1))
                                                    + "/" + dayOfMonth);



                                        }
                                    }, mYear, mMonth, mDay);
                            date.setTitle("Select date");
                            date.show();

                        }
                    });
                    btnSave.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            String cname = eName.getText().toString();
                            String cDesignation = eDesignation.getText().toString();
                            String cBirthdate = eBirthdate.getText().toString();
                            String cDept = sDepartmnt.getSelectedItem().toString();
                            String cemailid = cEmailId.getText().toString().trim();
                            String ctele = cTele.getText().toString().trim();
                            String cMobile = cMobilee.getText().toString().trim();
                            String cFax = cFaxe.getText().toString().trim();
                            String cWhtsapp = econtactwhatsapp.getText().toString().trim();
                            ProspectEnterpriseActivity.lstContact.get(position).setName(cname);
                            ProspectEnterpriseActivity.lstContact.get(position).setBirthdate(cBirthdate);
                            ProspectEnterpriseActivity.lstContact.get(position).setDepartment(cDept);
                            ProspectEnterpriseActivity.lstContact.get(position).setDesignation(cDesignation);
                            ProspectEnterpriseActivity.lstContact.get(position).setEmailId(cemailid);
                            ProspectEnterpriseActivity.lstContact.get(position).setFax(cFax);
                            ProspectEnterpriseActivity.lstContact.get(position).setMobile(cMobile);
                            ProspectEnterpriseActivity.lstContact.get(position).setTelephone(ctele);
                            ProspectEnterpriseActivity.lstContact.get(position).setWhtsapp(cWhtsapp);

                            Log.e("lstContact ", "size : " + ProspectEnterpriseActivity.lstContact.size());

                            Log.e("lstContact ", "after size : " + ProspectEnterpriseActivity.lstContact.size());
                            notifyDataSetChanged();

                            d_contact.cancel();
                        }
                    });

                    btnCancel.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            d_contact.cancel();

                        }
                    });

                    d_contact.show();


                }
            });
        }
        return vi;
    }

    private void AddContactPopup() {
        d_contact = new Dialog(activity);
        d_contact.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d_contact.setContentView(R.layout.crm_popupaddcontact);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(d_contact.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        d_contact.getWindow().setAttributes(lp);


        final Button btnSave = ((Button) d_contact
                .findViewById(R.id.btnpopupSaveContact));
        final Button btnCancel = ((Button) d_contact
                .findViewById(R.id.btnpopupCancelContact));
        final EditText eName = ((EditText) d_contact
                .findViewById(R.id.eContactName));
        final EditText eDesignation = ((EditText) d_contact
                .findViewById(R.id.eContactDesignation));
        final EditText eBirthdate = ((EditText) d_contact
                .findViewById(R.id.eBirthdate));
        final Spinner sDepartmnt = ((Spinner) d_contact
                .findViewById(R.id.sDepartment));

        final EditText cEmailId = (EditText) d_contact
                .findViewById(R.id.econtactpEmailid);
        final EditText cMobilee = (EditText) d_contact
                .findViewById(R.id.econtactpMobile);
        final EditText cTele = (EditText) d_contact
                .findViewById(R.id.eContactpTele);
        final EditText cFaxe = (EditText) d_contact
                .findViewById(R.id.eContactpFax);

        lstDept=new ArrayList<String>();
        lstDept.add("--Select Role--");
        lstDept.add("Decision Maker");
        lstDept.add("HR");
        lstDept.add("Marketing");
        lstDept.add("Operator");
        lstDept.add("Purchase");
        MySpinnerAdapter customDept = new MySpinnerAdapter(activity,
                R.layout.crm_custom_spinner_txt, lstDept);
        sDepartmnt.setAdapter(customDept);



       /* ((TextView) d_contact.findViewById(R.id.popupHead))
                .setTypeface(typeFace);*/

        eBirthdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog date = new DatePickerDialog(activity,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                String[] monthName = {"January", "February",
                                        "March", "April", "May", "June",
                                        "July", "August", "September",
                                        "October", "November", "December"};

                                eBirthdate.setText(dayOfMonth + " "
                                        + monthName[monthOfYear] + " " + year);

                                // eBirthdate.setText(dayOfMonth+"-"+monthOfYear+"-"+year);

                            }
                        }, mYear, mMonth, mDay);
                date.setTitle("Select date");
                date.show();

            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String cname = eName.getText().toString();
                String cDesignation = eDesignation.getText().toString();
                String cBirthdate = eBirthdate.getText().toString();
                String cDept = sDepartmnt.getSelectedItem().toString();
                String cemailid = cEmailId.getText().toString().trim();
                String ctele = cTele.getText().toString().trim();
                String cMobile = cMobilee.getText().toString().trim();
                String cFax = cFaxe.getText().toString().trim();
                String whtsapp = null;


                Log.e("lstContact ", "size : " + ProspectEnterpriseActivity.lstContact.size());
                ProspectEnterpriseActivity.lstContact.add(new ProspectContact(cname, cDesignation, cDept,
                        cBirthdate, cemailid, ctele, cMobile, cFax,whtsapp));
                Log.e("lstContact ", "after size : " + ProspectEnterpriseActivity.lstContact.size());
                notifyDataSetChanged();
                //     LinearLayout layout = ((LinearLayout) findViewById(R.id.llContact));
                /*LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams) layout
                        .getLayoutParams();
                lp.height = FragmentProspectEntry.lstContact.size() * 100;*/
                d_contact.cancel();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                d_contact.cancel();

            }
        });

        d_contact.show();

		/*
         * } catch (Exception e) { e.printStackTrace(); }
		 */
    }

    private static class MySpinnerAdapter extends ArrayAdapter<String> {
        // Initialise custom font, for example:


        private MySpinnerAdapter(Context context, int resource,
                                 List<String> items) {
            super(context, resource, items);
        }

        // Affects default (closed) state of the spinner
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getView(position, convertView,
                    parent);
            //view.setTypeface(font);
            return view;
        }

        // Affects opened state of the spinner
        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            TextView view = (TextView) super.getDropDownView(position,
                    convertView, parent);
            //  view.setTypeface(font);
            return view;
        }

    }
}


