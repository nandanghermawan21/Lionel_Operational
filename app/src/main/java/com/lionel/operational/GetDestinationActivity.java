package com.lionel.operational;

import static com.lionel.operational.model.Constant.BASE_URL;
import static com.lionel.operational.model.Constant.DESTINATION_KEY;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.lionel.operational.adapter.DestinationRecaicleViewAdapter;
import com.lionel.operational.model.ApiClient;
import com.lionel.operational.model.ApiResponse;
import com.lionel.operational.model.ApiService;
import com.lionel.operational.model.DestinationModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetDestinationActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EditText textFieldSearchDestinationView;

    DestinationModel selectedDestination = new DestinationModel();
    List<DestinationModel> destinations =  new ArrayList<>();
    DestinationRecaicleViewAdapter adapter = new DestinationRecaicleViewAdapter(destinations);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_destination);

        //initialize search view
        textFieldSearchDestinationView = findViewById(R.id.inputTextSearchDestination);

        //initialize recyclerview
        recyclerView = findViewById(R.id.recyclerViewDestination);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        //fetch data from api
        fetchDataFromApi();

        // Tambahkan TextWatcher untuk EditText
        textFieldSearchDestinationView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Panggil metode untuk melakukan filter saat teks berubah
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void fetchDataFromApi() {
        ApiService apiService = ApiClient.getInstant().create(ApiService.class);

        Call<ApiResponse<List<DestinationModel>>> call = apiService.getDestination();

        call.enqueue(new Callback<ApiResponse<List<DestinationModel>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<DestinationModel>>> call, Response<ApiResponse<List<DestinationModel>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if(response.body().isSuccess()) {
                        destinations = response.body().getData();
                        adapter = new DestinationRecaicleViewAdapter(destinations);
                        recyclerView.setAdapter(adapter);
                        adapter.setOnItemClickListener(new DestinationRecaicleViewAdapter.OnItemDestinationClickListener() {
                            @Override
                            public void onItemClick(DestinationModel item) {
                                selectedDestination = item;
                                setResult(RESULT_OK, getIntent().putExtra(DESTINATION_KEY, selectedDestination.toJson()));
                                finish();
                            }
                        });
                        Log.e("GET_DESTINATION", "onResponse: "+ destinations.size());
                    } else {
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("GET_DESTINATION", "onResponse: "+ response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<DestinationModel>>> call, Throwable t) {
                String errorMessage = getString(R.string.error_message);
                errorMessage = String.format(errorMessage, t.getMessage());
                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                Log.e("GET_DESTINATION", "onFailure: "+ t.getMessage());
            }
        });
    }

    private void filter(String text) {
        List<DestinationModel> filteredList = new ArrayList<>();
        for (DestinationModel item : destinations) {
            if (item.getId().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapter.filterList(filteredList);
    }

}