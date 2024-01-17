package org.rmj.guanzon.guanzonsaleskit.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import org.rmj.g3appdriver.GCircle.Account.EmployeeMaster;
import org.rmj.g3appdriver.GCircle.room.Entities.EClientInfoSalesKit;
import org.rmj.g3appdriver.SalesKit.Entities.EKPOPAgentRole;
import org.rmj.g3appdriver.etc.AppConfigPreference;
import org.rmj.g3appdriver.etc.MessageBox;
import org.rmj.guanzon.guanzonsaleskit.R;
import org.rmj.guanzon.guanzonsaleskit.ViewModel.VMHome;
import org.rmj.guanzon.guanzonsaleskit.databinding.ActivityHomeBinding;
import org.rmj.guanzongroup.agent.Activities.Activity_AgentEnroll;
import org.rmj.guanzongroup.agent.Activities.Activity_AgentList;
import org.rmj.guanzongroup.agent.Activities.Activity_SelectUpLine;
import org.rmj.guanzongroup.agent.Activities.Activity_UserPerformance;
import org.rmj.guanzongroup.authlibrary.Activity.Activity_Settings;
import org.rmj.guanzongroup.ganado.Activities.Activity_Inquiries;

public class Activity_Home extends AppCompatActivity {
    private VMHome mviewModel;
    private Boolean isCompleteAccount;
    private Boolean hasUpline;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomeBinding binding;
    private NavigationView navigationView;
    private MessageBox loMessage;

//    @Override (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {

        /*VERIFY FIRST USER IF COMPLETED ITS ACCOUNT*/
        mviewModel = new ViewModelProvider(this).get(VMHome.class);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarActivityHome.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        navigationView = binding.navView;

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_inquiry, R.id.nav_agent_enroll, R.id.nav_agent_list,R.id.nav_profile,
                R.id.nav_setupline, R.id.nav_userperform, R.id.nav_log_out)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_activity_home);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
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

            loMessage = new MessageBox(Activity_Home.this);
            loMessage.initDialog();
            loMessage.setNegativeButton("No", (view, dialog) -> dialog.dismiss());
            loMessage.setPositiveButton("Yes", (view, dialog) -> {
                dialog.dismiss();
                new EmployeeMaster(getApplication()).LogoutUserSession();
                AppConfigPreference.getInstance(Activity_Home.this).setIsAppFirstLaunch(false);
                startActivity(new Intent(Activity_Home.this, Activity_SplashScreen.class));
                finish();
            });
            loMessage.setTitle("Account Session");
            loMessage.setMessage("Are you sure you want to end session/logout?");
            loMessage.show();
//
//            Dialog_DoubleButton loDialog = new Dialog_DoubleButton(Activity_Dashboard.this);
//            loDialog.setButtonText("YES", "NO");
//            loDialog.initDialog("Confirm Logout", "Do you want to log out?", new Dialog_DoubleButton.OnDialogConfirmation() {
//                @Override
//                public void onConfirm(AlertDialog dialog) {
//                    mViewModel.LogoutUserSession(() -> {
//                        Intent loIntent = new Intent(Activity_Dashboard.this, Activity_Dashboard.class);
//                        startActivity(loIntent);
//                    });
//                    dialog.dismiss();
//                }
//
//                @Override
//                public void onCancel(AlertDialog dialog) {
//                    dialog.dismiss();
//                }
//            });
//            loDialog.show();
            return false;
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
        getMenuInflater().inflate(R.menu.activity__home, menu);
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
        }
//        else if (item.getItemId() == R.id.nav_log_out) {
//
//
//            loMessage = new MessageBox(Activity_Home.this);
//            loMessage.initDialog();
//            loMessage.setNegativeButton("No", (view, dialog) -> dialog.dismiss());
//            loMessage.setPositiveButton("Yes", (view, dialog) -> {
//                dialog.dismiss();
//                new EmployeeMaster(getApplication()).LogoutUserSession();
//                AppConfigPreference.getInstance(Activity_Home.this).setIsAppFirstLaunch(false);
//                startActivity(new Intent(Activity_Home.this, Activity_SplashScreen.class));
//                finish();
//            });
//            loMessage.setTitle("Account Session");
//            loMessage.setMessage("Are you sure you want to end session/logout?");
//            loMessage.show();
//            loIntent = new Intent(Activity_Home.this, Activity_Login.class);
//            loIntent.putExtra("isComplete", isCompleteAccount);
//            startActivity(loIntent);
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_activity_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}