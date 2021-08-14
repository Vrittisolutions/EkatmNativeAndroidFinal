package com.vritti.vwb.ImageWithLocation;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.vritti.ekatm.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SampleAdapter extends RecyclerView.Adapter<SampleAdapter.SampleHolder> {
    Context context;
    ArrayList<SamplePojoClass> samplePojoClassArrayList;

    public SampleAdapter(Context context, ArrayList<SamplePojoClass> samplePojoClassArrayList) {
        this.context = context;
        this.samplePojoClassArrayList = samplePojoClassArrayList;
    }

    @NonNull
    @Override
    public SampleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sample_row, parent, false);
        return new SampleHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SampleHolder holder, int position) {
        SamplePojoClass samplePojoClass = samplePojoClassArrayList.get(position);
        holder.locationStyle.setText(samplePojoClass.getLocationType());
        holder.locationName.setText(samplePojoClass.getLocationName());
        holder.sdvImage.setImageURI(Uri.parse(samplePojoClass.imageUri));
    }

    @Override
    public int getItemCount() {
        return samplePojoClassArrayList.size();
    }

    public class SampleHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.locationStyle)
        TextView locationStyle;
        @BindView(R.id.locationName)
        TextView locationName;
        @BindView(R.id.sdvImage)
        SimpleDraweeView sdvImage;


        public SampleHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this ,itemView);
        }
        @OnClick(R.id.row_layout)
        void rowClick(){
            ((EnoSamplingHomePage)context).updateImage(getAdapterPosition());
        }
    }
}
