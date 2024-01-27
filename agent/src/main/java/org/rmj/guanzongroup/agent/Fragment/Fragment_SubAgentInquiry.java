package org.rmj.guanzongroup.agent.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.rmj.g3appdriver.etc.MessageBox;
import org.rmj.g3appdriver.lib.Ganado.Obj.InquiryListAdapter;
import org.rmj.guanzongroup.agent.R;
import org.rmj.guanzongroup.agent.ViewModel.VMAgentInfo;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_SubAgentInquiry#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_SubAgentInquiry extends Fragment {
    private static final String TAG = Fragment_SubAgentInquiry.class.getSimpleName();
    private VMAgentInfo mViewModel;
    private RecyclerView rvInquiries;
    private TextView lblNoData;
    private ConstraintLayout lnLoading;
    private MessageBox loMessage;

    public Fragment_SubAgentInquiry() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(requireActivity()).get(VMAgentInfo.class);
        View view = inflater.inflate(R.layout.fragment_sub_agent_inquiry, container, false);

        loMessage = new MessageBox(getActivity());
        initViews(view);
        importInquiries();
        return view;
    }

    private void initViews(View v) {
        rvInquiries = v.findViewById(R.id.rvInquiries);
        lblNoData = v.findViewById(R.id.lbl_InquiryNoData);
        lnLoading = v.findViewById(R.id.linear_loadingInquiries);
    }

    private void displayData() {
    }
    private void importInquiries(){
        mViewModel.ImportInquiries(new VMAgentInfo.OnTaskExecute() {
            @Override
            public void OnExecute() {
//                hideInquiries();
//                hideAgents();
                rvInquiries.setVisibility(View.GONE);
                lnLoading.setVisibility(View.VISIBLE);
            }

            @Override
            public void OnSuccess() {
                LoadInquiries();
                lnLoading.setVisibility(View.GONE);
            }

            @Override
            public void OnFailed(String message) {
                lnLoading.setVisibility(View.GONE);
                lblNoData.setVisibility(View.VISIBLE);

            }
        });
    }
    private void LoadInquiries(){
        mViewModel.GetInquiries(mViewModel.GetUserID()).observe(requireActivity(), inquiries -> {

            Log.e("inquiries", String.valueOf(inquiries.size()));
            if (inquiries.size() > 0){

                lblNoData.setVisibility(View.GONE);
                rvInquiries.setVisibility(View.VISIBLE);
                InquiryListAdapter adapter= new InquiryListAdapter(requireActivity().getApplication(), inquiries, new InquiryListAdapter.OnModelClickListener() {
                    @Override
                    public void OnClick(String TransNox) {
//                        Intent intent = new Intent(Activity_Inquiries.this, Activity_ProductSelection.class);
//                        intent.putExtra("TransNox",TransNox);
//                        startActivity(intent);
//                        overridePendingTransition(org.rmj.g3appdriver.R.anim.anim_intent_slide_in_left, org.rmj.g3appdriver.R.anim.anim_intent_slide_out_right);
//                        finish();

                    }

                });

                rvInquiries.setAdapter(adapter);
                rvInquiries.setLayoutManager(new LinearLayoutManager(requireActivity(),RecyclerView.VERTICAL,false));

            }else{
                lblNoData.setVisibility(View.VISIBLE);
                rvInquiries.setVisibility(View.GONE);
            }
        });
    }
}