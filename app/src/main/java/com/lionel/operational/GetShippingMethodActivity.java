package com.lionel.operational;

import static com.lionel.operational.model.Constant.DESTINATION_KEY;
import static com.lionel.operational.model.Constant.GET_SHIPPING_METHOD;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.lionel.operational.adapter.ShippingMethodRecycleViewAdapter;
import com.lionel.operational.model.ApiClient;
import com.lionel.operational.model.ApiResponse;
import com.lionel.operational.model.ApiService;
import com.lionel.operational.model.DestinationModel;
import com.lionel.operational.model.ShippingMethodModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetShippingMethodActivity extends AppCompatActivity {

    private TextView titleAppBar;
    private TextInputLayout hintSearch;
    private RecyclerView recyclerView;
    private EditText textFieldSearchDestinationView;

    ShippingMethodModel selectedShippingMethod = new ShippingMethodModel();
    List<ShippingMethodModel> shippingMethods =  new ArrayList<>();

    ShippingMethodRecycleViewAdapter adapter = new ShippingMethodRecycleViewAdapter(shippingMethods);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_option);

        titleAppBar = findViewById(R.id.appBarTitle);
        hintSearch = findViewById(R.id.inputLayoutSearchDestination);

        //set title
        titleAppBar.setText(getString(R.string.select_shipping_method));
        //set hint
        hintSearch.setHint(getString(R.string.shipping_method));

        //initialize recyclerview
        textFieldSearchDestinationView = findViewById(R.id.inputTextSearchDestination);
        recyclerView = findViewById(R.id.recyclerViewDestination);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        //fetch data from api
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

        Call<ApiResponse<List<ShippingMethodModel>>> call = apiService.getShippingMethod("shipping-method");

        call.enqueue(new Callback<ApiResponse<List<ShippingMethodModel>>>() {

            @Override
            public void onResponse(Call<ApiResponse<List<ShippingMethodModel>>> call, Response<ApiResponse<List<ShippingMethodModel>>> response) {
                if(response.body().isSuccess()) {
                    shippingMethods = response.body().getData();
                    adapter = new ShippingMethodRecycleViewAdapter(shippingMethods);
                    recyclerView.setAdapter(adapter);
                    adapter.setOnItemClickListener(new ShippingMethodRecycleViewAdapter.OnItemOptionClickListener() {
                        @Override
                        public void onItemClick(ShippingMethodModel item) {
                            selectedShippingMethod = item;
                            setResult(RESULT_OK, getIntent().putExtra(GET_SHIPPING_METHOD, selectedShippingMethod.toJson()));
                            finish();
                        }
                    });
                    Log.e(GET_SHIPPING_METHOD, "onResponse: "+ shippingMethods.size());
                } else {
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(GET_SHIPPING_METHOD, "onResponse: "+ response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<ShippingMethodModel>>> call, Throwable t) {

            }
        });
    }

    private void filter(String text) {
        List<ShippingMethodModel> filteredList = new ArrayList<>();
        for (ShippingMethodModel item : shippingMethods) {
            if (item.getId().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapter.filterList(filteredList);
    }
}