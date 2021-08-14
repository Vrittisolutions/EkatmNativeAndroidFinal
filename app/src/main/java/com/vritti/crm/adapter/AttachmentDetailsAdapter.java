package com.vritti.crm.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vritti.SaharaModule.SaharaBeans.AttachmentBean;
import com.vritti.crm.vcrm7.CRM_Callslist_Partial;
import com.vritti.ekatm.R;
import com.vritti.vwb.vworkbench.EditDatasheetActivityMain;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AttachmentDetailsAdapter extends RecyclerView.Adapter<AttachmentDetailsAdapter.AttachmentHolder> {
    Context context;
    ArrayList<AttachmentBean> attachmentlist;
    ArrayList<String> stringList;
    boolean isEdit;


    public AttachmentDetailsAdapter(Context context, ArrayList<AttachmentBean> attachmentList, boolean isEdit) {

        this.attachmentlist = attachmentList;
        this.context = context;
        this.isEdit = isEdit;
    }
    public AttachmentDetailsAdapter(ArrayList<String> attachmentList, boolean isEdit) {
        this.stringList = attachmentList;
        this.isEdit = isEdit;
    }

    @NonNull
    @Override
    public AttachmentDetailsAdapter.AttachmentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.attachment_lay_v1, parent, false);


        return new AttachmentHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AttachmentHolder holder, int position) {


        if(!isEdit){
            String path = stringList.get(position);
            File file = new File(path);
            file.getName();

            holder.deleteFile.setVisibility(View.GONE);
            holder.downloadFile.setVisibility(View.GONE);
            holder.txt_attachment.setText(file.getName());
        }else {
            String attachFile  = attachmentlist.get(position).getAttachFilename();
            holder.deleteFile.setVisibility(View.VISIBLE);
            holder.downloadFile.setVisibility(View.VISIBLE);
            holder.txt_attachment.setText(attachFile);
        }




    }

    @Override
    public int getItemCount() {
        if(isEdit)
        return attachmentlist.size();
        else
        return stringList.size();

    }

    public class AttachmentHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_attachment)
        TextView txt_attachment;
 @BindView(R.id.downloadFile)
 ImageView downloadFile;
 @BindView(R.id.deleteFile)
        ImageView deleteFile;



        public AttachmentHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this ,itemView);
        }

        @OnClick(R.id.downloadFile)
        void downloadFile(){
            ((CRM_Callslist_Partial)context).downloadFile(getAdapterPosition() , true);
        }
        @OnClick(R.id.deleteFile)
        void deleteFile(){
            ((CRM_Callslist_Partial)context).deleteAttachment(getAdapterPosition() , false);
        }
    }
}
