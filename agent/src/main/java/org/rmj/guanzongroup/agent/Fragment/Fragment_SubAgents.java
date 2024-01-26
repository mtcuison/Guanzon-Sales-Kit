package org.rmj.guanzongroup.agent.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.rmj.g3appdriver.etc.MessageBox;
import org.rmj.guanzongroup.agent.Adapter.AgentListAdapter;
import org.rmj.guanzongroup.agent.R;
import org.rmj.guanzongroup.agent.ViewModel.VMAgentInfo;

public class Fragment_SubAgents extends Fragment {
    private static final String TAG = Fragment_SubAgentInquiry.class.getSimpleName();
    private VMAgentInfo mViewModel;
    private RecyclerView rvAgents;
    private TextView lblNoData;
    private LinearLayout lnLoading;
    private MessageBox loMessage;

    public Fragment_SubAgents() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(requireActivity()).get(VMAgentInfo.class);
        View view = inflater.inflate(R.layout.fragment_sub_agents, container, false);

        loMessage = new MessageBox(getActivity());
        initViews(view);
        importAgents();
        return view;
    }

    private void initViews(View v) {
        rvAgents = v.findViewById(R.id.rvAgents);
        lblNoData = v.findViewById(R.id.lbl_AgentNoData);
        lnLoading = v.findViewById(R.id.linear_loadingAgent);
    }

    private void displayData() {
    }
    private void importAgents(){
        mViewModel.ImportKPOPAgent(new VMAgentInfo.OnTaskExecute() {
            @Override
            public void OnExecute() {
//                hideInquiries();
//                hideAgents();
                lnLoading.setVisibility(View.VISIBLE);
            }

            @Override
            public void OnSuccess() {
                LoadSubAgents();
                lnLoading.setVisibility(View.GONE);
            }

            @Override
            public void OnFailed(String message) {
                lnLoading.setVisibility(View.GONE);
                lblNoData.setVisibility(View.VISIBLE);

            }
        });
    }
    private void LoadSubAgents(){
        mViewModel.GetKPOPAgent(mViewModel.GetUserID()).observe(requireActivity(), ekpopAgentRoles -> {
            Log.e(TAG, String.valueOf(ekpopAgentRoles.size()));
            if (ekpopAgentRoles.size() > 0){

                lblNoData.setVisibility(View.GONE);
                AgentListAdapter adapter= new AgentListAdapter(ekpopAgentRoles, new AgentListAdapter.OnAgentClickListener() {
                    @Override
                    public void OnClick(String sUserIDxx) {
//                        Intent intent = new Intent(Activity_AgentInfo.this, Activity_AgentInfo.class);
//                        intent.putExtra("sUserIDxx", sUserIDxx);
//                        startActivity(intent);
//                        overridePendingTransition(org.rmj.g3appdriver.R.anim.anim_intent_slide_in_right, org.rmj.g3appdriver.R.anim.anim_intent_slide_out_left);
                    }

                });

                rvAgents.setAdapter(adapter);
                rvAgents.setLayoutManager(new LinearLayoutManager(requireActivity(),  RecyclerView.VERTICAL, false));


            }else{
                lblNoData.setVisibility(View.VISIBLE);
            }
        });
    }
}
