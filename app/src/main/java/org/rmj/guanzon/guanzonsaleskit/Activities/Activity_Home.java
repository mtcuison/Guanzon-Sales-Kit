package org.rmj.guanzon.guanzonsaleskit.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;
import com.google.android.material.navigation.NavigationView;

import org.rmj.g3appdriver.GCircle.room.Entities.EClientInfo;
import org.rmj.g3appdriver.GCircle.room.Entities.EClientInfoSalesKit;
import org.rmj.g3appdriver.GConnect.Account.ClientMaster;
import org.rmj.g3appdriver.SalesKit.Entities.EKPOPAgentRole;
import org.rmj.g3appdriver.etc.AppConfigPreference;
import org.rmj.g3appdriver.etc.MessageBox;
import org.rmj.guanzon.guanzonsaleskit.R;
import org.rmj.guanzon.guanzonsaleskit.Service.DataSyncService;
import org.rmj.guanzon.guanzonsaleskit.ViewModel.VMHome;
import org.rmj.guanzon.guanzonsaleskit.databinding.ActivityHomeBinding;
import org.rmj.guanzongroup.agent.Activities.Activity_AgentEnroll;
import org.rmj.guanzongroup.agent.Activities.Activity_AgentList;
import org.rmj.guanzongroup.agent.Activities.Activity_SelectUpLine;
import org.rmj.guanzongroup.agent.Activities.Activity_UserPerformance;
import org.rmj.guanzongroup.authlibrary.Activity.Activity_Settings;
import org.rmj.guanzongroup.ganado.Activities.Activity_Inquiries;
import org.rmj.guanzongroup.ghostrider.notifications.Activity.Activity_NotificationList;

import kotlin.Suppress;

public class Activity_Home extends AppCompatActivity {

    private static final String TAG = "Sales Kit Home Activity";
    private VMHome mviewModel;
    private Boolean isCompleteAccount;
    private Boolean hasUpline;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomeBinding binding;
    private NavigationView navigationView;
    private MessageBox loMessage;
    private DataSyncService poNetRecvr;
    private DrawerLayout drawer;
    private BadgeDrawable loBadge;
    private Toolbar toolbar;
    private TextView lblUserIDxx;

//    @Override (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)

    @SuppressLint("UnsafeOptInUsageError")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
        loMessage = new MessageBox(Activity_Home.this);

        /*VERIFY FIRST USER IF COMPLETED ITS ACCOUNT*/
        mviewModel = new ViewModelProvider(this).get(VMHome.class);

        poNetRecvr = mviewModel.getInternetReceiver();
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        setSupportActionBar(binding.appBarActivityHome.toolbar);

        drawer = binding.drawerLayout;
        navigationView = binding.navView;


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_inquiry, R.id.nav_agent_enroll, R.id.nav_agent_list,R.id.nav_profile,
                R.id.nav_setupline, R.id.nav_userperform)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_activity_home);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        View nav_header_view = navigationView.getHeaderView(0);
        lblUserIDxx = nav_header_view.findViewById(R.id.lsUserID);

        initReceiver();
        mviewModel.GetPoEmpInfo().observe(Activity_Home.this, new Observer<EClientInfo>() {
            @Override
            public void onChanged(EClientInfo client) {
                if(client != null){

                    Log.e(TAG, client.getUserIDxx());
                    lblUserIDxx.setText("User ID : " + client.getUserIDxx());
                }
            }
        });

            mviewModel.GetUnreadMessagesCount().observe(Activity_Home.this, count -> {
                try{
                    toolbar = findViewById(R.id.toolbar);
                    if(count > 0) {
                        loBadge = BadgeDrawable.create(Activity_Home.this);
                        loBadge.setNumber(count);
                        BadgeUtils.attachBadgeDrawable(loBadge, toolbar, R.id.item_notifications);
                    } else {
                        BadgeUtils.detachBadgeDrawable(loBadge, toolbar, R.id.item_notifications);
                        supportInvalidateOptionsMenu();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            });
            mviewModel.GetKPOPAgentInfo().observe(this, new Observer<EKPOPAgentRole>() {
                @Override
                public void onChanged(EKPOPAgentRole ekpopAgentRole) {
                    Menu nav_Menu = navigationView.getMenu();
                    try {
                        if(ekpopAgentRole != null){
                            nav_Menu.findItem(R.id.nav_setupline).setVisible(false);
                        } else {
                            nav_Menu.findItem(R.id.nav_setupline).setVisible(true);
                        }
                    } catch(Exception e) {
                        e.printStackTrace();
                    }

                }
            });
            mviewModel.GetCompleteProfile().observe(Activity_Home.this, new Observer<EClientInfoSalesKit>() {
                @Override
                public void onChanged(EClientInfoSalesKit eClientInfoSalesKit) {
                    if (eClientInfoSalesKit == null){
                        isCompleteAccount = false;
                    }else {
                        if (eClientInfoSalesKit.getClientID() == null){
                            isCompleteAccount = false;
                        }else {
                            isCompleteAccount = true;
                        }
                    }
                }
            });


        navigationView.getMenu().findItem(R.id.nav_log_out).setOnMenuItemClickListener(menuItem -> {

            loMessage.initDialog();
            loMessage.setNegativeButton("No", (view, dialog) -> dialog.dismiss());
            loMessage.setPositiveButton("Yes", (view, dialog) -> {
                dialog.dismiss();
//                new EmployeeMaster(getApplication()).LogoutUserSession();
                new ClientMaster(getApplication()).LogoutUserSession();
                AppConfigPreference.getInstance(Activity_Home.this).setIsAppFirstLaunch(false);

                Intent loIntent = new Intent(Activity_Home.this, Activity_SplashScreen.class);
                loIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(loIntent);
                finish();
            });
            loMessage.setTitle("Account Session");
            loMessage.setMessage("Are you sure you want to end session/logout?");
            loMessage.show();
            return false;
        });
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                loMessage.initDialog();
                loMessage.setPositiveButton("Yes", (view, dialog) -> {
                    dialog.dismiss();
                    finish();
                });
                loMessage.setNegativeButton("No", (view, dialog) -> dialog.dismiss());
                loMessage.setTitle("Guanzon Sales Kit");
                loMessage.setMessage("Exit Guanzon Sales Kit app?");
                loMessage.show();
//                finish();
            }
        });
        } catch (NullPointerException e) {
            e.printStackTrace();
            // Handle or log the exception
        }
         catch (Exception e) {
        e.printStackTrace();
        // Handle or log the exception
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.activity__home, menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity__home, menu);
        mviewModel.GetCompleteProfile().observe(Activity_Home.this, eClientinfo -> {

            //This area of code has been commented to avoid users from accessing
            try {
                if(eClientinfo != null){
                    menu.findItem(R.id.item_notifications).setVisible(true);
                } else {
                    menu.findItem(R.id.item_notifications).setVisible(false);
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent loIntent;

        if(item.getItemId() == android.R.id.home) {

        } else if (item.getItemId() == R.id.nav_inquiry) {
            loIntent = new Intent(Activity_Home.this, Activity_Inquiries.class);
            startActivity(loIntent);
        }else if (item.getItemId() == R.id.nav_setupline) {
            loIntent = new Intent(Activity_Home.this, Activity_SelectUpLine.class);
            loIntent.putExtra("isComplete", isCompleteAccount);
            startActivity(loIntent);
        }else if (item.getItemId() == R.id.nav_agent_enroll) {
            loIntent = new Intent(Activity_Home.this, Activity_AgentEnroll.class);
            loIntent.putExtra("isComplete", isCompleteAccount);
            startActivity(loIntent);
        } else if (item.getItemId() == R.id.nav_agent_list) {
            loIntent = new Intent(Activity_Home.this, Activity_AgentList.class);
            startActivity(loIntent);
        }else if (item.getItemId() == R.id.nav_profile) {
            loIntent = new Intent(Activity_Home.this, Activity_Settings.class);
            loIntent.putExtra("isComplete", isCompleteAccount);
            startActivity(loIntent);
        }else if (item.getItemId() == R.id.nav_userperform) {
            loIntent = new Intent(Activity_Home.this, Activity_UserPerformance.class);
            loIntent.putExtra("isComplete", isCompleteAccount);
            startActivity(loIntent);
        }else {
            startActivity(new Intent(Activity_Home.this, Activity_NotificationList.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_activity_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @MainThread
    @Suppress(names = "DEPRECATION")
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }
    private void initReceiver(){
        IntentFilter loFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(poNetRecvr, loFilter);
        AppConfigPreference.getInstance(Activity_Home.this).setIsMainActive(true);
        Log.e(TAG, "Internet status receiver has been registered.");
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(poNetRecvr);
        Log.e(TAG, "Internet status receiver has been unregistered.");
        AppConfigPreference.getInstance(Activity_Home.this).setIsMainActive(false);
        super.onDestroy();
    }
}