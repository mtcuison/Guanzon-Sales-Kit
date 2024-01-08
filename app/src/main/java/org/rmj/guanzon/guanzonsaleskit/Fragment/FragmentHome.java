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
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.card.MaterialCardView;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.rmj.g3appdriver.etc.MessageBox;
import org.rmj.g3appdriver.lib.Promotions.Adapter_ImageSlider;
import org.rmj.g3appdriver.lib.Promotions.model.HomeImageSliderModel;
import org.rmj.guanzon.guanzonsaleskit.R;
import org.rmj.guanzon.guanzonsaleskit.ViewModel.VMHome;
import org.rmj.guanzongroup.ganado.Activities.Activity_BrandSelection;

import java.util.ArrayList;
import java.util.List;

public class FragmentHome extends Fragment {
    private static final String TAG = FragmentHome.class.getSimpleName();
    private VMHome mViewModel;
    private SliderView poSliderx;
    private MaterialCardView selectAuto;
    private MaterialCardView selectMC;
    private MessageBox loMessage;
    public FragmentHome() {}


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(requireActivity()).get(VMHome.class);
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        loMessage = new MessageBox(getActivity());
        initViews(view);
        displayData();

        return view;
    }

    private void initViews(View v) {
        poSliderx = v.findViewById(R.id.imgSlider);
        poSliderx.setIndicatorAnimation(IndicatorAnimationType.WORM);
        poSliderx.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        poSliderx.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
        poSliderx.setIndicatorSelectedColor(Color.WHITE);
        poSliderx.setIndicatorUnselectedColor(Color.GRAY);
        poSliderx.setScrollTimeInSec(5);
        poSliderx.startAutoCycle();

        selectAuto = v.findViewById(org.rmj.guanzongroup.ganado.R.id.materialCardView) ;
        selectMC =  v.findViewById(org.rmj.guanzongroup.ganado.R.id.materialCardView1);

    }

    private void displayData() {
        setSliderImages();
        selectAuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loMessage.initDialog();
                loMessage.setTitle("Under Development");
                loMessage.setMessage("Sorry, this feature is currently under development. We're working hard to bring it to you.");
                loMessage.setPositiveButton("Okay", new MessageBox.DialogButton() {
                    @Override
                    public void OnButtonClick(View view, AlertDialog dialog) {
                        dialog.dismiss();
                    }
                });
                loMessage.show();

//                Intent intent1 = new Intent(requireActivity(), Activity_BrandSelection.class);
//                intent1.putExtra("background", org.rmj.guanzongroup.ganado.R.drawable.img_category_mc);
//                startActivity(intent1);
////                requireActivity().finish();
            }
        });
        selectMC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform the action you want for button 1
                // For example, switch to a new activity

                Intent intent1 = new Intent(requireActivity(), Activity_BrandSelection.class);
                intent1.putExtra("background", org.rmj.guanzongroup.ganado.R.drawable.img_category_mc);
                startActivity(intent1);
//                requireActivity().finish();

            }
        });
    }

    private void setSliderImages() {
        List<HomeImageSliderModel> loSliders = new ArrayList<>();
        mViewModel.GetPromoLinkList().observe(getViewLifecycleOwner(), ePromos -> {
            try {
                for (int x = 0; x < ePromos.size(); x++) {
                    Log.e(TAG, ePromos.get(x).getImageUrl());
                    loSliders.add(new HomeImageSliderModel(ePromos.get(x).getImageUrl()));
                }
                Log.e(TAG, String.valueOf(ePromos.size()));
                Adapter_ImageSlider adapter = new Adapter_ImageSlider(loSliders, args -> {
                    try{
                        Log.e(TAG,  ePromos.get(args).getPromoUrl());
                        Intent intent = new Intent("android.intent.action.SUCCESS_LOGIN");
                        intent.putExtra("url_link", ePromos.get(args).getPromoUrl());
                        intent.putExtra("browser_args", "1");
                        intent.putExtra("args", "promo");
                        requireActivity().sendBroadcast(intent);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                });
                poSliderx.setSliderAdapter(adapter);

            } catch (Exception e){
                e.printStackTrace();
            }
        });
    }
}