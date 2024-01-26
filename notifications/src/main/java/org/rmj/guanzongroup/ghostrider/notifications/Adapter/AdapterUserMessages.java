/*
 * Created by Android Team MIS-SEG Year 2021
 * Copyright (c) 2021. Guanzon Central Office
 * Guanzon Bldg., Perez Blvd., Dagupan City, Pangasinan 2400
 * Project name : GhostRider_Android
 * Module : GhostRider_Android.notifications
 * Electronic Personnel Access Control Security System
 * project file created : 5/17/21 2:47 PM
 * project file last modified : 5/17/21 2:47 PM
 */

package org.rmj.guanzongroup.ghostrider.notifications.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import org.rmj.g3appdriver.GCircle.room.DataAccessObject.DMessages;
import org.rmj.g3appdriver.etc.FormatUIText;
import org.rmj.guanzongroup.ghostrider.notifications.R;

import java.util.List;

public class AdapterUserMessages extends RecyclerView.Adapter<AdapterUserMessages.MessagesViewHolder> {

    private List<DMessages.UserMessages> plMessage;

    public AdapterUserMessages(List<DMessages.UserMessages> plMessage) {
        this.plMessage = plMessage;
    }

    @NonNull
    @Override
    public MessagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_messages, parent, false);
        return new MessagesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesViewHolder holder, int position) {
        holder.cvSenderMsg.setVisibility(View.VISIBLE);
        holder.lblSenderMsg.setText(plMessage.get(position).sMessagex);
        holder.lblDateTmeSd.setText(FormatUIText.getParseDateTime(plMessage.get(position).dReceived));
        holder.cvSenderMsg.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return plMessage.size();
    }

    public static class MessagesViewHolder extends RecyclerView.ViewHolder{

        public TextView lblSenderMsg,
                        lblRecpntMsg,
                        lblDateTmeRc,
                        lblDateTmeSd;
        public ConstraintLayout cvSenderMsg,
                cvRecpntMsg;

        public MessagesViewHolder(@NonNull View itemView) {
            super(itemView);
            lblSenderMsg = itemView.findViewById(R.id.lbl_sender_message);
            lblRecpntMsg = itemView.findViewById(R.id.lbl_recipient_message);
            lblDateTmeRc = itemView.findViewById(R.id.lbl_messageDateTimeRcpt);
            lblDateTmeSd = itemView.findViewById(R.id.lbl_messageDateTimeSndr);
            cvSenderMsg = itemView.findViewById(R.id.constraint_sender);
            cvRecpntMsg = itemView.findViewById(R.id.constraint_user);

            lblSenderMsg.setOnClickListener(v -> {
                if(lblDateTmeSd.getVisibility() != View.VISIBLE) {
                    lblDateTmeSd.setVisibility(View.VISIBLE);
                } else {
                    lblDateTmeSd.setVisibility(View.GONE);
                }
            });
        }
    }

}
