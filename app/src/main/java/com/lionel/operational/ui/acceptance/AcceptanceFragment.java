package com.lionel.operational.ui.acceptance;

import static com.lionel.operational.model.Constant.BASE_URL;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.lionel.operational.R;
import com.lionel.operational.model.ApiResponse;
import com.lionel.operational.model.ApiService;
import com.lionel.operational.model.ShipmentModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AcceptanceFragment extends Fragment {

    private AcceptanceViewModel viewModel;
    private Button buttonNext;
    private Button buttonCancel;
    private Button buttonSubmit;
    private TextInputEditText editTextSearch;
    private TextInputEditText editTextGW;
    private TextInputEditText editTextLength;
    private TextInputEditText editTextWidth;
    private TextInputEditText editTextHeight;
    private TextView labelSearchError;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(AcceptanceViewModel.class);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_acceptance, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //initialize view
        buttonNext = view.findViewById(R.id.buttonNext);
        buttonCancel = view.findViewById(R.id.buttonCancel);
        buttonSubmit = view.findViewById(R.id.buttonSubmit);
        editTextSearch = view.findViewById(R.id.editTextSearch);
        editTextGW = view.findViewById(R.id.editTextGW);
        editTextLength = view.findViewById(R.id.editTextLength);
        editTextWidth = view.findViewById(R.id.editTextWidth);
        editTextHeight = view.findViewById(R.id.editTextHeight);
        labelSearchError = view.findViewById(R.id.labelSearchError);

        viewModel.getState().observe(getViewLifecycleOwner(), state -> {
            if (viewModel.isStateNew()) {
                buttonCancel.setVisibility(View.GONE);
                buttonSubmit.setVisibility(View.GONE);
                //disable all input text in layout input
                editTextGW.setEnabled(false);
                editTextLength.setEnabled(false);
                editTextWidth.setEnabled(false);
                editTextHeight.setEnabled(false);
                //enable textsearch
                editTextSearch.setEnabled(true);
                //enable button next
                buttonNext.setVisibility(View.VISIBLE);
            } else if (viewModel.isStateCreated()) {
                buttonCancel.setVisibility(View.VISIBLE);
                buttonSubmit.setVisibility(View.VISIBLE);
                //enable all input text in layout input
                editTextGW.setEnabled(true);
                editTextLength.setEnabled(true);
                editTextWidth.setEnabled(true);
                editTextHeight.setEnabled(true);
                //disable textsearch
                editTextSearch.setEnabled(false);
                //disable button next
                buttonNext.setVisibility(View.GONE);
            }
        });

        buttonNext.setOnClickListener(v -> {
            if (editTextSearch.getText().toString().isEmpty()) {
                labelSearchError.setText(getString(R.string.please_fill_STT));
                labelSearchError.setVisibility(View.VISIBLE);
            } else {
                doGetShipment();
            }
        });

        buttonCancel.setOnClickListener(v -> {
            viewModel.setStateAsNew();
            editTextSearch.setText("");
            editTextGW.setText("");
            editTextLength.setText("");
            editTextWidth.setText("");
            editTextHeight.setText("");
        });

        buttonSubmit.setOnClickListener(v -> {
            boolean isValid = true;
            if (editTextGW.getText().toString().isEmpty()) {
                editTextGW.setError(getString(R.string.gw_required));
                isValid = false;
            }
            if (editTextLength.getText().toString().isEmpty()) {
                editTextLength.setError(getString(R.string.length_required));
                isValid = false;
            }
            if (editTextWidth.getText().toString().isEmpty()) {
                editTextWidth.setError(getString(R.string.width_required));
                isValid = false;
            }
            if (editTextHeight.getText().toString().isEmpty()) {
                editTextHeight.setError(getString(R.string.height_required));
                isValid = false;
            }
            if (isValid) {
                doSubmit();
            }
        });
    }

    private void doGetShipment() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        Call<ApiResponse<ShipmentModel>> call = apiService.getShipment(editTextSearch.getText().toString());

        call.enqueue(new Callback<ApiResponse<ShipmentModel>>() {

            @Override
            public void onResponse(Call<ApiResponse<ShipmentModel>> call, Response<ApiResponse<ShipmentModel>> response) {
                if (response.isSuccessful()) {
                    if(response.body().isSuccess()){
                        viewModel.setShipment(response.body().getData());
                        viewModel.setStateAsCreated();
                        editTextGW.setText(String.valueOf(viewModel.getShipment().getValue().getGrossWeight()));
                        editTextLength.setText(String.valueOf(viewModel.getShipment().getValue().getLength()));
                        editTextWidth.setText(String.valueOf(viewModel.getShipment().getValue().getWidth()));
                        editTextHeight.setText(String.valueOf(viewModel.getShipment().getValue().getHeight()));
                    }else{
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ApiResponse<ShipmentModel>> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void doSubmit(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

            Call<ApiResponse> call = apiService.acceptShipment(
                    "accept-shipment",
                    editTextSearch.getText().toString(),
                    editTextGW.getText().toString(),
                    editTextLength.getText().toString(),
                    editTextWidth.getText().toString(),
                    editTextHeight.getText().toString()
            );

            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful()) {
                        if(response.body().isSuccess()){
                            Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            viewModel.setStateAsNew();
                            editTextSearch.setText("");
                            editTextGW.setText("");
                            editTextLength.setText("");
                            editTextWidth.setText("");
                            editTextHeight.setText("");
                        }else{
                            Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}