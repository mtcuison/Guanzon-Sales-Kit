package org.rmj.guanzon.guanzonsaleskit.Fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.rmj.g3appdriver.GCircle.room.Entities.EClientInfoSalesKit;
import org.rmj.g3appdriver.etc.MessageBox;
import org.rmj.g3appdriver.lib.Promotions.Adapter_ImageSlider;
import org.rmj.g3appdriver.lib.Promotions.model.HomeImageSliderModel;
import org.rmj.guanzon.guanzonsaleskit.Activities.Activity_Browser;
import org.rmj.guanzon.guanzonsaleskit.R;
import org.rmj.guanzon.guanzonsaleskit.ViewModel.VMHome;
import org.rmj.guanzongroup.authlibrary.Activity.Activity_Settings;
import org.rmj.guanzongroup.ganado.Activities.Activity_BrandSelection;

import java.util.ArrayList;
import java.util.List;

public class FragmentHome extends Fragment {
    private static final String TAG = FragmentHome.class.getSimpleName();
    private VMHome mViewModel;
    private Boolean isCompleteAccount;
    private MaterialTextView textView1;
    private SliderView poSliderx;
    private MaterialCardView selectAuto;
    private MaterialCardView selectMC;
    private MessageBox loMessage;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        loMessage = new MessageBox(getActivity());
        loMessage.initDialog();


        mViewModel = new ViewModelProvider(requireActivity()).get(VMHome.class);
        mViewModel.GetCompleteProfile().observe(requireActivity(), new Observer<EClientInfoSalesKit>() {
            @Override
            public void onChanged(EClientInfoSalesKit eClientInfoSalesKit) {
                if (eClientInfoSalesKit == null) {
                    isCompleteAccount = false;
                }else {
                    if (eClientInfoSalesKit.getClientID()==null){
                        isCompleteAccount = false;
                    }else {
                        isCompleteAccount = true;
                    }
                }
            }
        });

        textView1 = view.findViewById(R.id.textView1);
        poSliderx = view.findViewById(R.id.imgSlider);

        selectAuto = view.findViewById(org.rmj.guanzongroup.ganado.R.id.materialCardView) ;
        selectMC =  view.findViewById(org.rmj.guanzongroup.ganado.R.id.materialCardView1);

        setSliderImages();
        selectAuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCompleteAccount){ //allow to use feature

                    //REQUIRE UPLINE BEFORE INQUIRY
                    if (mViewModel.GetUpline() == null){
                        loMessage.setTitle("Sub Agent");
                        loMessage.setMessage("Please submit your upline first");
                        loMessage.setPositiveButton("Close", new MessageBox.DialogButton() {
                            @Override
                            public void OnButtonClick(View view, AlertDialog dialog) {
                                dialog.dismiss();
                            }
                        });
                        loMessage.show();
                    }

                    loMessage.setTitle("Under Development");
                    loMessage.setMessage("Sorry, this feature is currently under development. We're working hard to bring it to you.");
                    loMessage.setPositiveButton("Close", new MessageBox.DialogButton() {
                        @Override
                        public void OnButtonClick(View view, AlertDialog dialog) {
                            dialog.dismiss();
                        }
                    });
                    loMessage.show();
                }else { //must complete first account
                    loMessage.setTitle("Guanzon Sales Kit");
                    loMessage.setMessage("Must complete account to access this feature");
                    loMessage.setPositiveButton("Close", new MessageBox.DialogButton() {
                        @Override
                        public void OnButtonClick(View view, AlertDialog dialog) {
                            dialog.dismiss();

                            Intent loIntent = new Intent(requireActivity(), Activity_Settings.class);
                            startActivity(loIntent);
                        }
                    });
                    loMessage.show();
                }
            }
        });
        selectMC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCompleteAccount){ //allow to use feature

                //REQUIRE UPLINE BEFORE INQUIRY
                if (mViewModel.GetUpline() == null) {
                    loMessage.setTitle("Sub Agent");
                    loMessage.setMessage("Please submit your upline first");
                    loMessage.setPositiveButton("Close", new MessageBox.DialogButton() {
                        @Override
                        public void OnButtonClick(View view, AlertDialog dialog) {
                            dialog.dismiss();
                        }
                    });
                    loMessage.show();
                }else {
                    Intent intent = new Intent(requireActivity(), Activity_BrandSelection.class);
                    intent.putExtra("background", org.rmj.guanzongroup.ganado.R.drawable.img_category_mc);
                    startActivity(intent);
                }
                }else { //must complete first account
                    loMessage.setTitle("Guanzon Sales Kit");
                    loMessage.setMessage("Must complete account to access this feature");
                    loMessage.setPositiveButton("Close", new MessageBox.DialogButton() {
                        @Override
                        public void OnButtonClick(View view, AlertDialog dialog) {
                            dialog.dismiss();

                            Intent loIntent = new Intent(requireActivity(), Activity_Settings.class);
                            startActivity(loIntent);
                        }
                    });
                    loMessage.show();
                }
            }
        });

        return view;
    }
    private void initSlider(){
        poSliderx.setIndicatorAnimation(IndicatorAnimationType.WORM);
        poSliderx.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        poSliderx.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
        poSliderx.setIndicatorSelectedColor(Color.WHITE);
        poSliderx.setIndicatorUnselectedColor(Color.GRAY);
        poSliderx.setScrollTimeInSec(5);
        poSliderx.startAutoCycle();
    }
    private void setSliderImages() {
        List<HomeImageSliderModel> loSliders = new ArrayList<>();
        mViewModel.GetPromoLinkList().observe(getViewLifecycleOwner(), ePromos -> {
            try {
                for (int x = 0; x < ePromos.size(); x++) {
                    Log.e(TAG, ePromos.get(x).getImageUrl());
                    loSliders.add(new HomeImageSliderModel(ePromos.get(x).getImageUrl()));
                }

                Log.d(TAG, String.valueOf(ePromos.size()));
                if (ePromos.size() > 0){
                    //set visibility if there is current promos and events
                    textView1.setVisibility(View.VISIBLE);
                    poSliderx.setVisibility(View.VISIBLE);
                    initSlider();
                    Adapter_ImageSlider adapter = new Adapter_ImageSlider(loSliders, args -> {
                        try{
                            Intent intent = new Intent(requireActivity(), Activity_Browser.class);
                            intent.putExtra("url_link", ePromos.get(args).getPromoUrl());
                            intent.putExtra("args", "1");
                            startActivity(intent);
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    });
                    poSliderx.setSliderAdapter(adapter);
                }else {
                    //set visibility if there is current promos and events
                    textView1.setVisibility(View.GONE);
                    poSliderx.setVisibility(View.GONE);
                }

            } catch (Exception e){
                e.printStackTrace();
            }
        });
    }
}