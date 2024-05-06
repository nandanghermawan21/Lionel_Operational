package com.lionel.operational;

import static com.lionel.operational.model.Constant.GET_CITY;
import static com.lionel.operational.model.Constant.GET_SHIPPING_AGENT;
import static com.lionel.operational.model.Constant.PREFERENCES_KEY;
import static com.lionel.operational.model.Constant.USERDATA;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.lionel.operational.adapter.CityRecycleViewAdapter;
import com.lionel.operational.adapter.ShippingAgentRecycleViewAdapter;
import com.lionel.operational.model.AccountModel;
import com.lionel.operational.model.ApiClient;
import com.lionel.operational.model.ApiResponse;
import com.lionel.operational.model.ApiService;
import com.lionel.operational.model.CityModel;
import com.lionel.operational.model.ShippingAgentModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetCityActivity extends AppCompatActivity {
    private TextView titleAppBar;
    private TextInputLayout hintSearch;
    private RecyclerView recyclerView;
    private EditText textFieldSearchDestinationView;

    CityModel selectedCity = new CityModel();
    List<CityModel> cities =  new ArrayList<>();

    CityRecycleViewAdapter adapter = new CityRecycleViewAdapter(cities);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_option);

        titleAppBar = findViewById(R.id.appBarTitle);
        hintSearch = findViewById(R.id.inputLayoutSearchDestination);

        //set title
        titleAppBar.setText(getString(R.string.select_destination));
        //set hint
        hintSearch.setHint(getString(R.string.select_destination));

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

        Call<ApiResponse<List<CityModel>>> call = apiService.getCity("get-branch");

        call.enqueue(new Callback<ApiResponse<List<CityModel>>>() {

            @Override
            public void onResponse(Call<ApiResponse<List<CityModel>>> call, Response<ApiResponse<List<CityModel>>> response) {
                if(response.body().isSuccess()) {
                    cities = response.body().getData();
                    adapter = new CityRecycleViewAdapter(cities);
                    recyclerView.setAdapter(adapter);
                    adapter.setOnItemClickListener(new CityRecycleViewAdapter.OnItemOptionClickListener() {
                        @Override
                        public void onItemClick(CityModel item) {
                            selectedCity = item;
                            setResult(RESULT_OK, getIntent().putExtra(GET_CITY, selectedCity.toJson()));
                            finish();
                        }
                    });
                    Log.e(GET_CITY, "onResponse: "+ cities.size());
                } else {
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(GET_CITY, "onResponse: "+ response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<CityModel>>> call, Throwable t) {

            }
        });
    }

    private void filter(String text) {
        List<CityModel> filteredList = new ArrayList<>();
        for (CityModel item : cities) {
            if ((item.getId()).toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapter.filterList(filteredList);
    }
}
