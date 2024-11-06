package org.rmj.guanzongroup.ganado.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import org.rmj.g3appdriver.etc.MessageBox;
import org.rmj.guanzongroup.ganado.Adapter.RecyclerViewAdapter_BrandSelection;
import org.rmj.guanzongroup.ganado.Dialog.DialogDisclosure;
import org.rmj.guanzongroup.ganado.R;
import org.rmj.guanzongroup.ganado.ViewModel.VMBrandList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Activity_BrandSelection extends AppCompatActivity {

    private VMBrandList mViewModel;
    private RecyclerView rvc_brandlist;
    private RecyclerViewAdapter_BrandSelection rec_brandList;
    private ActivityResultLauncher<String[]> poRequest;
    private DialogDisclosure dialogDisclosure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_brand_selection);

        mViewModel = new ViewModelProvider(this).get(VMBrandList.class);
        dialogDisclosure = new DialogDisclosure(this);

        intWidgets();

        poRequest = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
            Log.d("PERMISSION RESULT", result.toString());

            if(ActivityCompat.checkSelfPermission(Activity_BrandSelection.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(Activity_BrandSelection.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

                initProductList();
            } else {
                showDisclosure();
            }
        });
    }

    private void intWidgets() {
        rvc_brandlist = findViewById(R.id.rv_brands);

        MaterialToolbar toolbar = findViewById(R.id.toolbar_brand);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(" ");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    private void showDisclosure(){

        dialogDisclosure.initDialog(new DialogDisclosure.onDisclosure() {
            @Override
            public void onAccept() {
                dialogDisclosure.dismiss();

                List<String> lsPermissions = new ArrayList<>();

                if(ActivityCompat.checkSelfPermission(Activity_BrandSelection.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                    lsPermissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
                }
                if(ActivityCompat.checkSelfPermission(Activity_BrandSelection.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                    lsPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
                }

                poRequest.launch(lsPermissions.toArray(new String[0]));
            }

            @Override
            public void onDecline() {
                dialogDisclosure.dismiss();

                MessageBox loMessage = new MessageBox(Activity_BrandSelection.this);
                loMessage.initDialog();
                loMessage.setIcon(org.rmj.g3appdriver.R.drawable.baseline_error_24);
                loMessage.setTitle("Disclosure");
                loMessage.setMessage("Disclosure denied. Unable to retrieve product brands");
                loMessage.setPositiveButton("Okay", new MessageBox.DialogButton() {
                    @Override
                    public void OnButtonClick(View view, AlertDialog dialog) {
                        dialog.dismiss();
                        finish();
                    }
                });

                loMessage.show();
            }
        });

        dialogDisclosure.setMessage("Guanzon Circle collects location data to enable saving of product inquiry when the app is in use.");
        dialogDisclosure.show();
    }

    private void initProductList(){
        mViewModel.getBrandList().observe(Activity_BrandSelection.this, brandList -> {
            if (brandList.size() > 0) {
                rec_brandList = new RecyclerViewAdapter_BrandSelection(brandList, new RecyclerViewAdapter_BrandSelection.OnBrandSelectListener() {
                    @Override
                    public void OnSelect(String BrandID, String BrandName) {
                        Intent intent = new Intent(Activity_BrandSelection.this, Activity_ProductSelection.class);
                        intent.putExtra("lsBrandID", BrandID);
                        intent.putExtra("lsBrandNm", BrandName);
                        startActivity(intent);
                        overridePendingTransition(org.rmj.g3appdriver.R.anim.anim_intent_slide_in_right, org.rmj.g3appdriver.R.anim.anim_intent_slide_out_left);
                    }
                });

                rvc_brandlist.setAdapter(rec_brandList);
                rvc_brandlist.setLayoutManager(new GridLayoutManager(Activity_BrandSelection.this, 2, RecyclerView.VERTICAL, false));

                rec_brandList.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(ActivityCompat.checkSelfPermission(Activity_BrandSelection.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(Activity_BrandSelection.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            showDisclosure();
        }else {
            initProductList();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(org.rmj.g3appdriver.R.anim.anim_intent_slide_in_left, org.rmj.g3appdriver.R.anim.anim_intent_slide_out_right);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}