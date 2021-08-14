package com.vritti.expensemanagement;

/**
 * Created by sharvari on 18-Sep-19.
 */

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


import com.vritti.ekatm.R;

import java.util.ArrayList;


/**
 * Created by Jerry on 12/19/2017.
 */

public class AssistantAdapter extends RecyclerView.Adapter<AssistantAdapter.AssistantViewHolder> {

    private ArrayList<Expenses> msgDtoList = null;
    Context context;

    public AssistantAdapter(Context context,ArrayList<Expenses> msgDtoList) {
        this.context=context;
        this.msgDtoList = msgDtoList;
    }

    @Override
    public void onBindViewHolder(final AssistantViewHolder holder, int position) {
        Expenses msgDto = msgDtoList.get(position);
        // If the message is a received message.
        if(msgDto.getUser_type().equals(UserPreference.RECEIVE_TYPE))
        {
            if (msgDto.getText_for_assistant().equals("Mode of payment")){
                holder.rightMsgLayout.setVisibility(LinearLayout.VISIBLE);
                holder.radio_cash.setVisibility(View.VISIBLE);
                holder.rightMsgTextView.setVisibility(View.GONE);
                holder.leftMsgTextView.setText(msgDto.getText_for_assistant());
                holder.leftMsgLayout.setVisibility(LinearLayout.VISIBLE);
            }else if (msgDto.getText_for_assistant().contains("img_type")){
                holder.rightMsgLayout.setVisibility(LinearLayout.VISIBLE);
                holder.len_image.setVisibility(View.VISIBLE);
                String image=msgDto.getText_for_assistant();
                String image_list[] = image.split("&");
                String pic = image_list[0];
                holder.imageView.setImageURI(Uri.parse(pic));
                holder.rightMsgTextView.setVisibility(View.GONE);
                /*RoundingParams roundingParams = RoundingParams.fromCornersRadius(10f);
                holder.imageView.setHierarchy(new GenericDraweeHierarchyBuilder(context.getResources())
                        .setRoundingParams(roundingParams)
                        .build());*/
            }else if (msgDto.getText_for_assistant().contains("What type of expenses")){
                holder.rightMsgLayout.setVisibility(LinearLayout.GONE);
                holder.leftMsgTextView.setText("What type of expenses?");
                holder.len_exp_type.setVisibility(View.VISIBLE);
                holder.leftMsgLayout.setVisibility(LinearLayout.VISIBLE);

            }else if (msgDto.getText_for_assistant().contains("2 wheeler 4 wheeler Bus Taxi Train Flight")){
                holder.rightMsgLayout.setVisibility(View.GONE);
                holder.len_exp_travel.setVisibility(View.VISIBLE);
                holder.leftMsgLayout.setVisibility(View.GONE);

            }
            else {
                holder.leftMsgLayout.setVisibility(LinearLayout.VISIBLE);
                holder.leftMsgTextView.setText(msgDto.getText_for_assistant());
                holder.rightMsgLayout.setVisibility(LinearLayout.GONE);
                holder.radio_cash.setVisibility(View.GONE);



            }
        }
        else {

               if (msgDto.getText_for_assistant().equals(UserPreference.book)){
                   holder.len_exp.setVisibility(View.VISIBLE);
                   holder.rightMsgLayout.setVisibility(LinearLayout.VISIBLE);
                   holder.rightMsgTextView.setText("Is it Personal or Official?");

               }else if (msgDto.getText_for_assistant().equals(UserPreference.official)){
                   holder.rightMsgLayout.setVisibility(LinearLayout.GONE);

               }else {
                   holder.rightMsgLayout.setVisibility(LinearLayout.VISIBLE);
                   holder.rightMsgTextView.setText(msgDto.getText_for_assistant());
                   holder.leftMsgLayout.setVisibility(LinearLayout.GONE);
                   holder.radio_cash.setVisibility(View.GONE);
                   holder.len_image.setVisibility(View.GONE);
               }
        }

        holder.radiobtn_cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.radiobtn_cash.isChecked()) {

                    String cash = holder.radiobtn_cash.getText().toString();
                    cash = "cash";
                    ((RecycleExpenseMainActivity) context).cash(context, cash);

                }else {

                }

            }
        });
        holder.radiobtn_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String card=holder.radiobtn_card.getText().toString();
                if (holder.radiobtn_card.isChecked()) {
                    card = "card";
                    ((RecycleExpenseMainActivity) context).card(context, card);

                }else {

                }

            }
        });

        holder.radiobtn_evallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String wallet=holder.radiobtn_evallet.getText().toString();
                if (holder.radiobtn_evallet.isChecked()) {
                    wallet = "E Wallet";
                    ((RecycleExpenseMainActivity) context).ewallet(context, wallet);

                }
                else {

                }

            }
        });
        holder.btn_official.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String official=holder.btn_official.getText().toString();

                    ((RecycleExpenseMainActivity) context).Official(context, official);



            }
        });
        holder.btn_per.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String personal=holder.btn_per.getText().toString();

                ((RecycleExpenseMainActivity) context).Personal(context, personal);



            }
        });

        holder.btn_travel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String travel=holder.btn_travel.getText().toString();

                ((RecycleExpenseMainActivity) context).Travel(context, travel);



            }
        });
        holder.btn_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String food=holder.btn_food.getText().toString();

              //  ((RecycleExpenseMainActivity) context).Personal(context, food);



            }
        });
        holder.btn_local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String local=holder.btn_local.getText().toString();

                //((RecycleExpenseMainActivity) context).Personal(context, local);



            }
        });
        holder.btn_lodge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lodge=holder.btn_per.getText().toString();

                //((RecycleExpenseMainActivity) context).Personal(context, lodge);



            }
        });

        holder.btn_2wheeler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String two=holder.btn_2wheeler.getText().toString();

                ((RecycleExpenseMainActivity) context).TwoWheeler(context, UserPreference.two);



            }
        });

        holder.btn_4wheeler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String four=holder.btn_4wheeler.getText().toString();

                ((RecycleExpenseMainActivity) context).FourWheeler(context, UserPreference.four);



            }
        });

        holder.btn_bus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bus=holder.btn_bus.getText().toString();

                ((RecycleExpenseMainActivity) context).Bus(context, UserPreference.bus);



            }
        });

        holder.btn_taxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taxi=holder.btn_taxi.getText().toString();

                ((RecycleExpenseMainActivity) context).Taxi(context, UserPreference.taxi);



            }
        });

        holder.btn_train.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String train=holder.btn_train.getText().toString();

                ((RecycleExpenseMainActivity) context).Train(context, UserPreference.train);



            }
        });

        holder.btn_flight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String flight=holder.btn_flight.getText().toString();

                ((RecycleExpenseMainActivity) context).Flight(context, UserPreference.flight);



            }
        });


    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public AssistantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.assistant_item, parent, false);
        return new AssistantViewHolder(view);
    }

    @Override
    public int getItemCount() {
        if(msgDtoList==null)
        {
            msgDtoList = new ArrayList<Expenses>();
        }
        return msgDtoList.size();
    }
    public class AssistantViewHolder extends RecyclerView.ViewHolder {

        LinearLayout leftMsgLayout;

        LinearLayout rightMsgLayout,len_image,len_exp,len_exp_type,len_exp_travel;

        TextView leftMsgTextView;

        TextView rightMsgTextView;
        RadioGroup radio_cash;
        RadioButton radiobtn_cash,radiobtn_evallet,radiobtn_card;
        TextView btn_official,btn_per,btn_travel,btn_local,btn_lodge,btn_food;
        TextView btn_2wheeler,btn_4wheeler,btn_bus,btn_taxi,btn_train,btn_flight ;
        ImageView imageView;

        public AssistantViewHolder(View itemView) {
            super(itemView);

            if(itemView!=null) {
                leftMsgLayout = (LinearLayout) itemView.findViewById(R.id.chat_left_msg_layout);
                rightMsgLayout = (LinearLayout) itemView.findViewById(R.id.chat_right_msgf_layout);
                leftMsgTextView = (TextView) itemView.findViewById(R.id.chat_left_msg_text_view);
                rightMsgTextView = (TextView) itemView.findViewById(R.id.chat_right_msg_text_view);
                radio_cash = (RadioGroup) itemView.findViewById(R.id.radio_cash);
                radiobtn_cash = (AppCompatRadioButton) itemView.findViewById(R.id.radiobtn_cash);
                radiobtn_evallet = (AppCompatRadioButton) itemView.findViewById(R.id.radiobtn_evallet);
                radiobtn_card = (AppCompatRadioButton) itemView.findViewById(R.id.radiobtn_card);
                len_image = (LinearLayout) itemView.findViewById(R.id.len_image);
                len_exp = (LinearLayout) itemView.findViewById(R.id.len_exp);
                len_exp_type = (LinearLayout) itemView.findViewById(R.id.len_exp_type);
                len_exp_travel = (LinearLayout) itemView.findViewById(R.id.len_exp_travel);
                btn_official = (TextView) itemView.findViewById(R.id.btn_official);
                btn_per = (TextView) itemView.findViewById(R.id.btn_per);
                btn_travel = (TextView) itemView.findViewById(R.id.btn_travel);
                btn_local = (TextView) itemView.findViewById(R.id.btn_local);
                btn_lodge = (TextView) itemView.findViewById(R.id.btn_lodge);
                btn_food = (TextView) itemView.findViewById(R.id.btn_food);
                btn_2wheeler = (TextView) itemView.findViewById(R.id.btn_2wheeler);
                btn_4wheeler = (TextView) itemView.findViewById(R.id.btn_4wheeler);
                btn_bus = (TextView) itemView.findViewById(R.id.btn_bus);
                btn_taxi = (TextView) itemView.findViewById(R.id.btn_taxi);
                btn_train = (TextView) itemView.findViewById(R.id.btn_train);
                btn_flight = (TextView) itemView.findViewById(R.id.btn_flight);
                imageView = (ImageView) itemView.findViewById(R.id.imageView);
            }
        }
    }
}