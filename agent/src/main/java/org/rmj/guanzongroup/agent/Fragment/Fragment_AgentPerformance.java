package org.rmj.guanzongroup.agent.Fragment;

import android.os.Bundle;
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

import org.rmj.g3appdriver.GCircle.room.DataAccessObject.DGanadoOnline;
import org.rmj.guanzongroup.agent.R;
import org.rmj.guanzongroup.agent.ViewModel.VMAgentInfo;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_AgentPerformance#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_AgentPerformance extends Fragment {
    private static final String TAG = Fragment_AgentPerformance.class.getSimpleName();
    private VMAgentInfo mViewModel;
    private MaterialTextView totalsales;
    private MaterialTextView username;
    private MaterialTextView totalopen;
    private MaterialTextView totalextr;
    private MaterialTextView totalsold;
    private MaterialTextView totaleng;
    private MaterialTextView totallost;
    private MaterialCardView mcv_totalopen;
    private MaterialCardView mcv_totalextr;
    private MaterialCardView mcv_totalsold;
    private MaterialCardView mcv_totaleng;
    private MaterialCardView mcv_totallost;



    public Fragment_AgentPerformance() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(requireActivity()).get(VMAgentInfo.class);
        View view = inflater.inflate(R.layout.fragment_agent_performance, container, false);

        initViews(view);

        mViewModel.GetCountEntries().observe(requireActivity(), new Observer<DGanadoOnline.CountEntries>() {
            @Override
            public void onChanged(DGanadoOnline.CountEntries countEntries) {

                totalopen.setText(String.valueOf(countEntries.nOpen));
                totalextr.setText(String.valueOf(countEntries.nExtracted));
                totalsold.setText(String.valueOf(countEntries.nSold));
                totaleng.setText(String.valueOf(countEntries.nEngaged));
                totallost.setText(String.valueOf(countEntries.nLost));
                totalsales.setText(String.valueOf(countEntries.nEntries));

//                username.setText(poSession.getUserName());
            }
        });
        mViewModel.GetKPOPAgentInfo().observe(requireActivity(),agentInfo ->{
            if(agentInfo != null){
                username.setText(agentInfo.getsUserName());
            }
        });
        return view;
    }

    private void initViews(View v) {

        username = v.findViewById(R.id.username);
        totalsales = v.findViewById(R.id.totalsales);
        totalopen = v.findViewById(R.id.totalopen);
        totalextr = v.findViewById(R.id.totalextr);
        totalsold = v.findViewById(R.id.totalsold);
        totaleng = v.findViewById(R.id.totaleng);
        totallost = v.findViewById(R.id.totallost);

        mcv_totalopen = v.findViewById(R.id.mcv_totalopen);
        mcv_totalextr = v.findViewById(R.id.mcv_totalextr);
        mcv_totalsold = v.findViewById(R.id.mcv_totalsold);
        mcv_totaleng = v.findViewById(R.id.mcv_totaleng);
        mcv_totallost = v.findViewById(R.id.mcv_totallost);
    }

}