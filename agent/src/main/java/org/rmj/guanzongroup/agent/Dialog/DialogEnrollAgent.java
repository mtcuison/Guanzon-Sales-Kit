/*
 * Created by Android Team MIS-SEG Year 2021
 * Copyright (c) 2021. Guanzon Central Office
 * Guanzon Bldg., Perez Blvd., Dagupan City, Pangasinan 2400
 * Project name : GhostRider_Android
 * Module : GhostRider_Android.CreditEvaluator
 * Electronic Personnel Access Control Security System
 * project file created : 1/03/24 5:19 PM
 * project file last modified : 1/03/24 5:19 PM
 */

package org.rmj.guanzongroup.agent.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import org.rmj.g3appdriver.etc.GToast;
import org.rmj.guanzongroup.agent.R;

public class DialogEnrollAgent {
    Context mContex;
    private AlertDialog poDialogx;
    private String sUserIDxx = "";
    public DialogEnrollAgent(Context context){
        this.mContex = context;
    }

    public void initDialogEnrollAgent(DialogButtonClickListener listener){
        AlertDialog.Builder loBuilder = new AlertDialog.Builder(mContex);
        View view = LayoutInflater.from(mContex).inflate(R.layout.dialog_enroll_agent, null, false);
        loBuilder.setCancelable(false)
                .setView(view);
        poDialogx = loBuilder.create();
        poDialogx.setCancelable(false);

        MaterialAutoCompleteTextView txtUpline = view.findViewById(R.id.txt_upLine);
        MaterialButton btnConfirm = view.findViewById(R.id.btn_dialogConfirm);
        MaterialButton btnCancelx = view.findViewById(R.id.btn_dialogCancel);

        btnConfirm.setOnClickListener(view1 -> {
            if (sUserIDxx.isEmpty()){
                GToast.CreateMessage(mContex, "Please select approval/disapproval status dialog.",GToast.WARNING).show();
            }else if(txtUpline.getText().toString().isEmpty()){
                GToast.CreateMessage(mContex, "Please enter reason for approval/disapproval evaluation dialog.",GToast.WARNING).show();
            }else{
                listener.OnClick(poDialogx, sUserIDxx, txtUpline.getText().toString());
            }
        });

        btnCancelx.setOnClickListener(view12 -> dismiss());
    }


    public void show(){
        poDialogx.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        poDialogx.getWindow().getAttributes().windowAnimations = org.rmj.g3appdriver.R.style.PopupAnimation;
        poDialogx.show();
    }

    public void dismiss(){
        if(poDialogx != null && poDialogx.isShowing()){
            poDialogx.dismiss();
        }
    }

    public interface DialogButtonClickListener{
        void OnClick(Dialog dialog, String userid, String username);
    }

}
