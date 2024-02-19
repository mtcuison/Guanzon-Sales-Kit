/*
 * Created by Android Team MIS-SEG Year 2021
 * Copyright (c) 2021. Guanzon Central Office
 * Guanzon Bldg., Perez Blvd., Dagupan City, Pangasinan 2400
 * Project name : GhostRider_Android
 * Module : GhostRider_Android.g3appdriver
 * Electronic Personnel Access Control Security System
 * project file created : 4/24/21 3:19 PM
 * project file last modified : 4/24/21 3:17 PM
 */

package org.rmj.guanzongroup.ganado.Dialog;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.widget.LinearLayoutCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.divider.MaterialDivider;
import com.google.android.material.textview.MaterialTextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.rmj.g3appdriver.GCircle.room.DataAccessObject.DGanadoOnline;
import org.rmj.g3appdriver.GCircle.room.Entities.ECountryInfo;
import org.rmj.g3appdriver.GCircle.room.Entities.EGanadoOnline;
import org.rmj.g3appdriver.etc.FormatUIText;
import org.rmj.g3appdriver.lib.Etc.Country;
import org.rmj.g3appdriver.lib.Ganado.Obj.Ganado;
import org.rmj.g3appdriver.lib.Ganado.Obj.ProductInquiry;
import org.rmj.g3appdriver.lib.Ganado.model.GConstants;
import org.rmj.guanzongroup.ganado.R;

import java.util.Objects;

public class DialogInquiryHistory {

    private static final String TAG = DialogInquiryHistory.class.getSimpleName();
    private AlertDialog poDialogx;
    private MaterialButton btnPositive;
    private MaterialButton btnClose;
    private MaterialTextView lblDate,lblTargetDate, lblStatus, lblclientName,
            lblclientMbilNo, lblfinancierName, lblfinancierCountry,
            lblModelNme, lblInquiryType, lblCashAmount , lblTerm,
            lblDownPayment, lblMonthAmrt;
    LinearLayoutCompat lnCash, lnInstTerm;
    private MaterialTextView lblMsgxx;
    private MaterialDivider midBorder;

    private final Context context;
    private String message = "";
    private String nMAort = "";
    public DialogInquiryHistory(Context context){
        // Must be, at all times, pass Activity Context.
        this.context = Objects.requireNonNull(context);
    }
    public void initDialog(Application apps, EGanadoOnline foDetail){
        AlertDialog.Builder poBuilder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_inquiry_history, null);
        poBuilder.setCancelable(false)
                .setView(view);
        poDialogx = poBuilder.create();
        poDialogx.setCancelable(false);
        initView(view);
        try {
            Ganado poSys = new Ganado(apps);
            ProductInquiry poInq = new ProductInquiry(apps);
            Country poCountry = new Country(apps);

//            poSys.
            JSONObject joClient = new JSONObject(foDetail.getClntInfo());
            JSONObject joFinancier = new JSONObject(foDetail.getFinancex());
            JSONObject joProduct = new JSONObject(foDetail.getProdInfo());
            String financier_name = joFinancier.getString("sLastName") + ", " + joFinancier.getString("sFrstName") + " " + joFinancier.getString("sMiddName");
            lblStatus.setText(GConstants.INQUIRY_STATUS[Integer.parseInt(foDetail.getTranStat())]);
//            lblStatus.setText("Pending Inc. Release");
            lblclientName.setText(foDetail.getClientNm());
            lblclientMbilNo.setText(joClient.getString("sMobileNo"));
            lblfinancierName.setText(financier_name);

            lblDate.setText(FormatUIText.formatGOCasBirthdate(foDetail.getTransact()));
            lblTargetDate.setText(FormatUIText.formatGOCasBirthdate(foDetail.getTargetxx()));

            ECountryInfo eCountry = poCountry.getCountryInfo(joFinancier.getString("sCntryCde"));
            lblfinancierCountry.setText(eCountry.getCntryNme());
            DGanadoOnline.McInfo mcInfo = poInq.GetMCInfo(joProduct.getString("sModelIDx"),joProduct.getString("sBrandIDx"), joProduct.getString("sColorIDx"));
            lblModelNme.setText(mcInfo.ModelNme);
            lblInquiryType.setText(GConstants.PAYMENT_FORM[Integer.parseInt(foDetail.getPaymForm())]);
            if(foDetail.getPaymForm().equalsIgnoreCase("0")){
                lnInstTerm.setVisibility(View.GONE);
                lnCash.setVisibility(View.VISIBLE);
                lblCashAmount.setText(FormatUIText.getCurrencyUIFormat(joProduct.getString("nSelPrice")));

            }else{
                lnInstTerm.setVisibility(View.VISIBLE);
                lnCash.setVisibility(View.GONE);
                JSONObject joPaymInfo = new JSONObject(foDetail.getPaymInfo());
                int pos = 0;
                if(joPaymInfo.getString("sTermIDxx").equalsIgnoreCase("36")){
                    pos = 0;
                }else if(joPaymInfo.getString("sTermIDxx").equalsIgnoreCase("24")){
                    pos = 1;
                }else{
                    pos = 2;
                }
                lblTerm.setText(GConstants.INSTALLMENT_TERM[pos]);
                lblDownPayment.setText(FormatUIText.getCurrencyUIFormat(joPaymInfo.getString("nDownPaym")));
                lblMonthAmrt.setText(FormatUIText.getCurrencyUIFormat(joPaymInfo.getString("nMonthAmr")));

//                lblMonthAmrt.setText(FormatUIText.getCurrencyUIFormat(String.valueOf(CalculateNewDownpayment(poInq, joProduct.getString("sModelIDx"), Integer.parseInt(joPaymInfo.getString("sTermIDxx")), Double.parseDouble(joPaymInfo.getString("nDownPaym"))))));
            }
;
        } catch (JSONException e) {
            Log.e(TAG,e.getMessage());
            lblMonthAmrt.setText(FormatUIText.getCurrencyUIFormat("0.00"));
//            throw new RuntimeException(e);
        }
    }

    private void initView(View v){

        lblTargetDate = v.findViewById(R.id.lblTargetDate);
        lblDate = v.findViewById(R.id.lblDate);
        lblStatus = v.findViewById(R.id.lblStatus);
        lblclientName = v.findViewById(R.id.lbl_clientName);
        lblclientMbilNo = v.findViewById(R.id.lbl_clientMbilNo);
        lblfinancierName = v.findViewById(R.id.lbl_financierName);
        lblfinancierCountry = v.findViewById(R.id.lbl_financierCountry);
        lblModelNme = v.findViewById(R.id.lblModelNme);
        lblInquiryType = v.findViewById(R.id.lblInquiryType);
        lblCashAmount = v.findViewById(R.id.lblCashAmount);
        lblTerm = v.findViewById(R.id.lblTerm);
        lblDownPayment = v.findViewById(R.id.lblDownPayment);
        lblMonthAmrt = v.findViewById(R.id.lblMonthAmrt);
        lnCash = v.findViewById(R.id.lnCashTerm);
        lnInstTerm = v.findViewById(R.id.lnInstTerm);
        btnClose = v.findViewById(R.id.btn_close);
    }

    public interface AsyncTaskCallback {
        void onTaskComplete(double result);
    }
    public void setMessage(String psMessage) {
        try {
            lblMsgxx.setText(Objects.requireNonNull(psMessage));
        } catch(NullPointerException e){
            e.printStackTrace();
        }
    }

    public void setTitle(String psTitlexx) {
        try {
//            lblTitle.setText(Objects.requireNonNull(psTitlexx));
        } catch(NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void setPositiveButton(String psBtnPost, final DialogButton listener) {
        btnClose.setVisibility(View.VISIBLE);
        btnClose.setText(psBtnPost);
        btnClose.setOnClickListener(view -> {
            listener.OnButtonClick(view, poDialogx);
//            isDialogShown = false;
        });
    }


    public void show() {
        if(!poDialogx.isShowing()) {
            poDialogx.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            poDialogx.getWindow().getAttributes().windowAnimations = org.rmj.g3appdriver.R.style.PopupAnimation;
            poDialogx.show();
        }
    }

    public interface DialogButton{
        void OnButtonClick(View view, AlertDialog dialog);
    }
}

