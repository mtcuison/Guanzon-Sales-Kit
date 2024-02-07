package org.rmj.guanzongroup.ghostrider.notifications.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.rmj.g3appdriver.GCircle.room.DataAccessObject.DNotification;
import org.rmj.g3appdriver.etc.GToast;
import org.rmj.guanzongroup.ghostrider.notifications.Adapter.NotificationListAdapter;
import org.rmj.guanzongroup.ghostrider.notifications.R;
import org.rmj.guanzongroup.ghostrider.notifications.ViewModel.VMNotificationList;

import java.util.List;

public class Activity_NotificationList extends AppCompatActivity {

    private VMNotificationList mViewModel;
    private Toolbar toolbar;
    private ConstraintLayout noNotif;
    private LinearLayoutManager manager;
    private RecyclerView recyclerView;
    private NotificationListAdapter poAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(Activity_NotificationList.this).get(VMNotificationList.class);
        setContentView(R.layout.activity_notification_list);

        initViews();
        toolbar.setTitle("Notifications");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        LinearLayoutManager layoutManager = new LinearLayoutManager(Activity_NotificationList.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(Activity_NotificationList.this, DividerItemDecoration.VERTICAL));
        mViewModel.GetOtherNotificationList().observe(Activity_NotificationList.this, new Observer<List<DNotification.NotificationListDetail>>() {
            @Override
            public void onChanged(List<DNotification.NotificationListDetail> list) {
                try{
                    if(list == null){
                        return;
                    }

                    if(list.size() == 0){
                        recyclerView.setVisibility(View.GONE);
                        noNotif.setVisibility(View.VISIBLE);
                        return;
                    }
                    recyclerView.setVisibility(View.VISIBLE);
                    noNotif.setVisibility(View.GONE);

                    poAdapter = new NotificationListAdapter(list, new NotificationListAdapter.OnItemClickListener() {
                        @Override
                        public void OnClick(String messageID) {
                            Intent loIntent = new Intent(Activity_NotificationList.this, Activity_ViewNotification.class);
                            loIntent.putExtra("sMesgIDxx", messageID);
                            startActivity(loIntent);
                            overridePendingTransition(org.rmj.g3appdriver.R.anim.anim_intent_slide_in_right, org.rmj.g3appdriver.R.anim.anim_intent_slide_out_left);
                        }

                        @Override
                        public void OnActionButtonClick(String message) {
                            GToast.CreateMessage(Activity_NotificationList.this, message, GToast.INFORMATION).show();
                        }
                    });
                    poAdapter.notifyDataSetChanged();
                    manager = new LinearLayoutManager(Activity_NotificationList.this);
                    manager.setOrientation(RecyclerView.VERTICAL);
                    recyclerView.setLayoutManager(manager);
                    recyclerView.setAdapter(poAdapter);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }


    private void initViews() {
        toolbar = findViewById(R.id.toolbar_notification);
        noNotif = findViewById(R.id.ln_empty);
        recyclerView = findViewById(R.id.recycler_view_notifications);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}