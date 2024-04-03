package com.lionel.operational;

import static com.lionel.operational.model.Constant.PREFERENCES_KEY;
import static com.lionel.operational.model.Constant.USERDATA;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.lionel.operational.databinding.ActivityMainBinding;
import com.lionel.operational.model.AccountModel;
import com.lionel.operational.model.ApiClient;
import com.lionel.operational.model.ApiResponse;
import com.lionel.operational.model.ApiService;

import retrofit2.Call;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private TextView tvUsername;
    private TextView tvGroupName;
    NavigationView navigationView;
    private FragmentManager fragmentManager;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check session here
        if (sessionIsNull()) {
            redirectToLoginPage();
        }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        navigationView = binding.navView;
        fragmentManager = getSupportFragmentManager();
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_acceptance, R.id.nav_console, R.id.nav_way_bill, R.id.nav_stob)
                .setOpenableLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //ubah user name di header
        View headerView = navigationView.getHeaderView(0);
        tvUsername = headerView.findViewById(R.id.userName);
        tvGroupName = headerView.findViewById(R.id.group);
        //ambil data user dari shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
        String userData = sharedPreferences.getString(USERDATA, null);
        //convert to object
        AccountModel accountModel = new Gson().fromJson(userData, AccountModel.class);
        tvUsername.setText(accountModel.getName());
        tvGroupName.setText(accountModel.getGroup());

        hideAllMenuItem();
        getMenu();
    }

    private boolean sessionIsNull() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
        // Periksa apakah nilai 'isLoggedIn' dalam SharedPreferences bernilai false atau belum ada sama sekali
        return sharedPreferences.getString(USERDATA, null) == null;
    }

    private void redirectToLoginPage() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish(); // Optional: menyelesaikan MainActivity sehingga pengguna tidak dapat kembali ke halaman ini tanpa login.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            // Panggil fungsi logout di sini
            logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        // Hapus status login dari SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(USERDATA);
        editor.apply();


        // Redirect ke halaman login
        redirectToLoginPage();
    }

    //mapping menu name to menu id
    private int mapMenuItem(String menuName) {
        switch (menuName) {
            case "OP Acceptance":
                return (R.id.nav_acceptance);
            case "OP Console":
                return (R.id.nav_console);
            case "OP Waybill":
                return (R.id.nav_way_bill);
            case "OP STOB":
                return (R.id.nav_stob);
            default:
                return 0;
        }
    }

    //get fist visible menu item
    private int getFirstVisibleMenuItem() {
        Menu menu = navigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            if (item.isVisible()) {
                return item.getItemId();
            }
        }
        return 0;
    }

    //hide all menu item
    private void hideAllMenuItem() {
        Menu menu = navigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            menu.getItem(i).setVisible(false);
        }
    }

    private void showMenuItem(int menuItemId) {
        Menu menu = navigationView.getMenu();
        MenuItem item = menu.findItem(menuItemId);
        if (item != null) {
            item.setVisible(true);
        }
    }

    private void setInitialSelectedItem(int menuItemId) {
        Menu menu = navigationView.getMenu();
        MenuItem item = menu.findItem(menuItemId);
        if (item != null) {
            item.setChecked(true);
        }
    }

    //get menu item from api
    private void getMenu() {
        ApiService apiService = ApiClient.getInstant().create(ApiService.class);

        //get data from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
        String userData = sharedPreferences.getString(USERDATA, null);
        //convert to object
        AccountModel accountModel = new Gson().fromJson(userData, AccountModel.class);

        Call<ApiResponse<AccountModel>> call = apiService.getMenu(
                accountModel.getUsername()
        );

        call.enqueue(new retrofit2.Callback<ApiResponse<AccountModel>>() {
            @Override
            public void onResponse(Call<ApiResponse<AccountModel>> call, retrofit2.Response<ApiResponse<AccountModel>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().isSuccess()) {
                            if (response.body().getData() instanceof AccountModel) {
                                AccountModel accountModel = response.body().getData();
                                if(accountModel.getMenu() != null && accountModel.getMenu().size() > 0){
                                    for (String menu : accountModel.getMenu()) {
                                        showMenuItem(mapMenuItem(menu));
                                    }
                                    setInitialSelectedItem(getFirstVisibleMenuItem());
                                    NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                                            .findFragmentById(R.id.nav_host_fragment_content_main);
                                    navController = navHostFragment.getNavController();
                                    navController.navigate(getFirstVisibleMenuItem());
                                }else{
                                    doesNotHavePermission();
                                }
                            } else {
                                doesNotHavePermission();
                            }
                        }else{
                            doesNotHavePermission();
                        }
                    }else{
                        doesNotHavePermission();
                    }
                }else{
                    doesNotHavePermission();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<AccountModel>> call, Throwable t) {
                doesNotHavePermission();
            }
        });
    }

    void doesNotHavePermission() {
        Toast.makeText(MainActivity.this, "user does not have permission", Toast.LENGTH_SHORT).show();
        redirectToLoginPage();
    }
}