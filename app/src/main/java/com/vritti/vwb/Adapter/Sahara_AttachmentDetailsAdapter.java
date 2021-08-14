package com.vritti.vwb.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vritti.SaharaModule.SaharaBeans.AttachmentBean;
import com.vritti.ekatm.R;
import com.vritti.vwb.Beans.Datasheet;
import com.vritti.vwb.Beans.EditDatasheet;
import com.vritti.vwb.vworkbench.AddDatasheetActivityMain;
import com.vritti.vwb.vworkbench.EditDatasheetActivityMain;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class Sahara_AttachmentDetailsAdapter extends RecyclerView.Adapter<Sahara_AttachmentDetailsAdapter.AttachmentHolder> {
    Context context;
    ArrayList<AttachmentBean> attachmentlist;
    ArrayList<Datasheet> datasheetArrayList;
    ArrayList<EditDatasheet> editdatasheetArrayList;
    //ArrayList<String> stringList;
    ArrayList<String> stringList;
    boolean isEdit;
    int postn;
    BottomSheetDialog dialog;
    TextView deleteBtn;
    String fromEdit="";
    String designation="";


    public Sahara_AttachmentDetailsAdapter(Context context1, ArrayList<AttachmentBean> attachmentList, boolean isEdit) {

        this.attachmentlist = attachmentList;
        context = context1;
        this.isEdit = isEdit;
    }
    public Sahara_AttachmentDetailsAdapter(Context context1, ArrayList<AttachmentBean> attachmentList, boolean isEdit,String fromDocReview) {

        this.attachmentlist = attachmentList;
        context = context1;
        this.isEdit = isEdit;
        this.fromEdit = fromDocReview;
    }
    public Sahara_AttachmentDetailsAdapter(Context context1, ArrayList<AttachmentBean> attachmentList, boolean isEdit,
                                           String fromDocReview,String designation) {

        this.attachmentlist = attachmentList;
        context = context1;
        this.isEdit = isEdit;
        this.fromEdit = fromDocReview;
        this.designation = designation;
    }


    public Sahara_AttachmentDetailsAdapter(Context context, ArrayList<String> attachmentList,ArrayList<Datasheet> datasheetArrayList,
                                           boolean isEdit,int selectedPos) {
        this.stringList = attachmentList;
        this.context = context;
        this.isEdit = isEdit;
        this.datasheetArrayList = datasheetArrayList;
        this.postn = selectedPos;

    }

    public Sahara_AttachmentDetailsAdapter(Context context, ArrayList<String> attachmentList,
                                           ArrayList<EditDatasheet> editDatasheetslist, boolean isEdit, int selectedPos, String n) {


        this.stringList = attachmentList;
        this.context = context;
        this.isEdit = isEdit;
        this.editdatasheetArrayList = editDatasheetslist;
        this.postn = selectedPos;
        this.fromEdit = n;
    }




    @NonNull
    @Override
    public Sahara_AttachmentDetailsAdapter.AttachmentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sahara_attachment, parent, false);

        return new AttachmentHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AttachmentHolder holder, final int position) {


        if(!isEdit){

            String path = stringList.get(position);
            path = path.replace("[", "").replace("]", "");

            File file = new File(path);
            file.getName().replace("]", "");
            String s = file.getName().replace("]", "");
            holder.txt_attachment.setText(s);
            holder.cardView.setVisibility(View.VISIBLE);


       /*  holder.deleteFile.setVisibility(View.GONE);
            holder.downloadFile.setVisibility(View.GONE);*/

        }else {
            if(fromEdit.equalsIgnoreCase("fromEdit")){
                String path = stringList.get(position);
                path = path.replace("[", "").replace("]", "");

                File file = new File(path);
                file.getName().replace("]", "");
                String s = file.getName().replace("]", "");
                holder.txt_attachment.setText(s);
                holder.cardView.setVisibility(View.VISIBLE);
            }else {
                String attachFile = attachmentlist.get(position).getAttachFilename();
                if(fromEdit.equalsIgnoreCase("fromDocReview")) {
                    holder.deleteFile.setVisibility(View.GONE);
                }else {
                    if(designation.equalsIgnoreCase("Kendra") || designation.contains("Extension Officer") || designation.equalsIgnoreCase("school")){
                        holder.deleteFile.setVisibility(View.GONE);
                    }else{
                        holder.deleteFile.setVisibility(View.VISIBLE);

                    }

                }


                holder.downloadFile.setVisibility(View.VISIBLE);
                holder.txt_attachment.setText(attachFile);
                holder.cardView.setVisibility(View.VISIBLE);
            }
        }


    }

    @Override
    public int getItemCount() {
        if(isEdit)
            if(fromEdit.equalsIgnoreCase("fromEdit"))
                return stringList.size();
            else
                return attachmentlist.size();
        else
            return stringList.size();

    }

    public void update(int adapterPos) {
        if(attachmentlist.size()!=0) {
            //attachmentlist.remove(adapterPos);
            notifyItemRemoved(adapterPos);
        }
    }

    public class AttachmentHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_attachment)
        TextView txt_attachment;
        @BindView(R.id.downloadFile)
        ImageView downloadFile;
        @BindView(R.id.deleteFile)
        ImageView deleteFile;
        @BindView(R.id.card_view)
        CardView cardView;



        public AttachmentHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this ,itemView);
        }

        @OnClick(R.id.downloadFile)
        void downloadFile(){
            if(isEdit) {
                if(fromEdit.equalsIgnoreCase("fromEdit"))
                    ((EditDatasheetActivityMain) context).viewFile(postn, true,getAdapterPosition());
                else if(fromEdit.equalsIgnoreCase("fromDocReview")) {
                    // ((DocumentReviewList) context).downloadFile(getAdapterPosition(), true);
                }
                else
                    ((EditDatasheetActivityMain) context).downloadFile(getAdapterPosition(), true);
            } else {
                ((AddDatasheetActivityMain) context).viewFile(postn, true,getAdapterPosition());
            }

        }
        @OnClick(R.id.deleteFile)
        void deleteFile(){
            int selectedPos = -1 ;
            if(isEdit) {
                if(fromEdit.equalsIgnoreCase("fromEdit")) {
                    ((EditDatasheetActivityMain) context).deletefileName(postn,getAdapterPosition(),fromEdit);
                }else {
                    ((EditDatasheetActivityMain) context).downloadFile(getAdapterPosition(), false);
                }
            } else {
                // int pos = getAdapterPosition();
                int p = postn;


                ((AddDatasheetActivityMain) context).deletefileName(postn, getAdapterPosition());
            }


        }
    }
}
