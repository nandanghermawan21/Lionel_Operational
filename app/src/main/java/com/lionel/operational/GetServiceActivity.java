package com.lionel.operational;

import static com.lionel.operational.model.Constant.GET_SHIPPING_LINER;
import static com.lionel.operational.model.Constant.GET_SHIPPING_SERVICE;
import static com.lionel.operational.model.Constant.PREFERENCES_KEY;
import static com.lionel.operational.model.Constant.USERDATA;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.lionel.operational.adapter.ServiceRecycleViewAdapter;
import com.lionel.operational.adapter.ShippingLinerRecycleViewAdapter;
import com.lionel.operational.model.AccountModel;
import com.lionel.operational.model.ApiClient;
import com.lionel.operational.model.ApiResponse;
import com.lionel.operational.model.ApiService;
import com.lionel.operational.model.ServiceModel;
import com.lionel.operational.model.ShippingLinerModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetServiceActivity extends AppCompatActivity {

    private TextView titleAppBar;
    private TextInputLayout hintSearch;

    private RecyclerView recyclerView;
    private EditText textFieldSearchDestinationView;

    ServiceModel selectedService = new ServiceModel();
    List<ServiceModel> services =  new ArrayList<ServiceModel>();

    ServiceRecycleViewAdapter adapter = new ServiceRecycleViewAdapter(services);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_option);

        titleAppBar = findViewById(R.id.appBarTitle);
        hintSearch = findViewById(R.id.inputLayoutSearchDestination);

        //set title
        titleAppBar.setText(getString(R.string.select_service));
        hintSearch.setHint(getString(R.string.service));

        //initialize recyclerview
        textFieldSearchDestinationView = findViewById(R.id.inputTextSearchDestination);
        recyclerView = findViewById(R.id.recyclerViewDestination);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        getDataFromAPi();

        textFieldSearchDestinationView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void getDataFromAPi() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
        AccountModel account = new Gson().fromJson(sharedPreferences.getString(USERDATA, "{}"), AccountModel.class);

        ApiService apiService = ApiClient.getInstant(getApplicationContext()).create(ApiService.class);

        Call<ApiResponse<List<ServiceModel>>> call = apiService.getService("get-service-select");

        call.enqueue(new Callback<ApiResponse<List<ServiceModel>>>() {

            @Override
            public void onResponse(Call<ApiResponse<List<ServiceModel>>> call, Response<ApiResponse<List<ServiceModel>>> response) {
                if(response.body().isSuccess()) {
                    services = response.body().getData();
                    adapter = new ServiceRecycleViewAdapter(services);
                    recyclerView.setAdapter(adapter);
                    adapter.setOnItemClickListener(new ServiceRecycleViewAdapter.OnItemOptionClickListener() {
                        @Override
                        public void onItemClick(ServiceModel item) {
                            selectedService = item;
                            setResult(RESULT_OK, getIntent().putExtra(GET_SHIPPING_SERVICE, selectedService.toJson()));
                            finish();
                        }
                    });
                    Log.e(GET_SHIPPING_SERVICE, "onResponse: "+ services.size());
                } else {
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(GET_SHIPPING_SERVICE, "onResponse: "+ response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<ServiceModel>>> call, Throwable t) {
                Log.e(GET_SHIPPING_SERVICE, "onFailure: "+ t.getMessage());
            }
        });
    }

    private void filter(String text) {
        List<ServiceModel> filteredList = new ArrayList<>();
        for (ServiceModel item : services) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapter.filterList(filteredList);
    }
}