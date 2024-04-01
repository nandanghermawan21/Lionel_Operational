package com.lionel.operational;

import static com.lionel.operational.model.Constant.GET_SHIPPING_AGENT;
import static com.lionel.operational.model.Constant.GET_SHIPPING_METHOD;
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
import com.lionel.operational.adapter.ShippingMethodRecycleViewAdapter;
import com.lionel.operational.model.AccountModel;
import com.lionel.operational.model.ApiClient;
import com.lionel.operational.model.ApiResponse;
import com.lionel.operational.model.ApiService;
import com.lionel.operational.model.ShippingAgentModel;
import com.lionel.operational.model.ShippingMethodModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetShippingAgentActivity extends AppCompatActivity {

    private TextView titleAppBar;
    private TextInputLayout hintSearch;
    private RecyclerView recyclerView;
    private EditText textFieldSearchDestinationView;

    ShippingAgentModel selectedShippingAgent = new ShippingAgentModel();
    List<ShippingAgentModel> shippingAgents =  new ArrayList<>();

    ShippingAgentRecycleViewAdapter adapter = new ShippingAgentRecycleViewAdapter(shippingAgents);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_option);

        titleAppBar = findViewById(R.id.appBarTitle);
        hintSearch = findViewById(R.id.inputLayoutSearchDestination);

        //set title
        titleAppBar.setText(getString(R.string.select_shipping_agent));
        //set hint
        hintSearch.setHint(getString(R.string.shipping_agent));

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

        ApiService apiService = ApiClient.getInstant().create(ApiService.class);

        //get data user from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(USERDATA, "");
        AccountModel account = new Gson().fromJson(json, AccountModel.class);

        Call<ApiResponse<List<ShippingAgentModel>>> call = apiService.getShippingAgent("get-shipping-agent", account.getBranchId());

        call.enqueue(new Callback<ApiResponse<List<ShippingAgentModel>>>() {

            @Override
            public void onResponse(Call<ApiResponse<List<ShippingAgentModel>>> call, Response<ApiResponse<List<ShippingAgentModel>>> response) {
                if(response.body().isSuccess()) {
                    shippingAgents = response.body().getData();
                    adapter = new ShippingAgentRecycleViewAdapter(shippingAgents);
                    recyclerView.setAdapter(adapter);
                    adapter.setOnItemClickListener(new ShippingAgentRecycleViewAdapter.OnItemOptionClickListener() {
                        @Override
                        public void onItemClick(ShippingAgentModel item) {
                            selectedShippingAgent = item;
                            setResult(RESULT_OK, getIntent().putExtra(GET_SHIPPING_AGENT, selectedShippingAgent.toJson()));
                            finish();
                        }
                    });
                    Log.e(GET_SHIPPING_AGENT, "onResponse: "+ shippingAgents.size());
                } else {
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(GET_SHIPPING_AGENT, "onResponse: "+ response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<ShippingAgentModel>>> call, Throwable t) {

            }
        });
    }

    private void filter(String text) {
        List<ShippingAgentModel> filteredList = new ArrayList<>();
        for (ShippingAgentModel item : shippingAgents) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapter.filterList(filteredList);
    }
}