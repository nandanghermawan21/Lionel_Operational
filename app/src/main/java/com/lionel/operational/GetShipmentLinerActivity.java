package com.lionel.operational;

import static com.lionel.operational.model.Constant.GET_SHIPPING_AGENT;
import static com.lionel.operational.model.Constant.GET_SHIPPING_LINER;
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
import com.lionel.operational.adapter.ShippingAgentRecycleViewAdapter;
import com.lionel.operational.adapter.ShippingLinerRecycleViewAdapter;
import com.lionel.operational.model.AccountModel;
import com.lionel.operational.model.ApiClient;
import com.lionel.operational.model.ApiResponse;
import com.lionel.operational.model.ApiService;
import com.lionel.operational.model.ShippingAgentModel;
import com.lionel.operational.model.ShippingLinerModel;
import com.lionel.operational.model.ShippingMethodModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetShipmentLinerActivity extends AppCompatActivity {

    private TextView titleAppBar;
    private TextInputLayout hintSearch;

    private RecyclerView recyclerView;
    private EditText textFieldSearchDestinationView;

    ShippingLinerModel selectedShippingLiner = new ShippingLinerModel();
    List<ShippingLinerModel> shippingLiners =  new ArrayList<ShippingLinerModel>();

    ShippingLinerRecycleViewAdapter adapter = new ShippingLinerRecycleViewAdapter(shippingLiners);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_option);

        titleAppBar = findViewById(R.id.appBarTitle);
        hintSearch = findViewById(R.id.inputLayoutSearchDestination);

        //set title
        titleAppBar.setText(getString(R.string.select_liner));
        hintSearch.setHint(getString(R.string.liner));

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

        ApiService apiService = ApiClient.getInstant(getApplicationContext()).create(ApiService.class);

        Call<ApiResponse<List<ShippingLinerModel>>> call = apiService.getShippingLiner("get-liner");

        call.enqueue(new Callback<ApiResponse<List<ShippingLinerModel>>>() {

            @Override
            public void onResponse(Call<ApiResponse<List<ShippingLinerModel>>> call, Response<ApiResponse<List<ShippingLinerModel>>> response) {
                if(response.body().isSuccess()) {
                    shippingLiners = response.body().getData();
                    adapter = new ShippingLinerRecycleViewAdapter(shippingLiners);
                    recyclerView.setAdapter(adapter);
                    adapter.setOnItemClickListener(new ShippingLinerRecycleViewAdapter.OnItemOptionClickListener() {
                        @Override
                        public void onItemClick(ShippingLinerModel item) {
                            selectedShippingLiner = item;
                            setResult(RESULT_OK, getIntent().putExtra(GET_SHIPPING_LINER, selectedShippingLiner.toJson()));
                            finish();
                        }
                    });
                    Log.e(GET_SHIPPING_LINER, "onResponse: "+ shippingLiners.size());
                } else {
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(GET_SHIPPING_LINER, "onResponse: "+ response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<ShippingLinerModel>>> call, Throwable t) {
                Log.e(GET_SHIPPING_LINER, "onFailure: "+ t.getMessage());
            }
        });
    }

    private void filter(String text) {
        List<ShippingLinerModel> filteredList = new ArrayList<>();
        for (ShippingLinerModel item : shippingLiners) {
            if ((item.getId().toLowerCase() + " - " + item.getName().toLowerCase()).contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapter.filterList(filteredList);
    }
}