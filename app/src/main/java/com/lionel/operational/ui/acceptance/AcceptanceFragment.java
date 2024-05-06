package com.lionel.operational.ui.acceptance;

import static com.lionel.operational.model.Constant.BASE_URL;
import static com.lionel.operational.model.Constant.GET_BARCODE;
import static com.lionel.operational.model.Constant.GET_DESTINATION;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.lionel.operational.GetDestinationActivity;
import com.lionel.operational.R;
import com.lionel.operational.ScanBarcodeActivity;
import com.lionel.operational.model.ApiClient;
import com.lionel.operational.model.ApiResponse;
import com.lionel.operational.model.ApiService;
import com.lionel.operational.model.DestinationModel;
import com.lionel.operational.model.ShipmentModel;
import com.lionel.operational.util.SoundPlayer;

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
    private ImageView scanBarcodeBtn;
    private SoundPlayer soundPlayer;

    private final ActivityResultLauncher<Intent> scanBarcode = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    String barcode = data.getStringExtra(GET_BARCODE);
                    editTextSearch.setText(barcode);
                }
            });

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
        scanBarcodeBtn = view.findViewById(R.id.scanBarcodeBtn);

        soundPlayer = new SoundPlayer();

        //focuse to search
        editTextSearch.requestFocus();

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
                //focus to search
                editTextSearch.requestFocus();
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

            //check format input apakah seudah merupakan angka double
            if (isValid) {
                try {
                    Double.parseDouble(editTextGW.getText().toString());
                } catch (NumberFormatException e) {
                    editTextGW.setError(getString(R.string.wrong_format));
                    isValid = false;
                }
                try {
                    Double.parseDouble(editTextLength.getText().toString());
                } catch (NumberFormatException e) {
                    editTextLength.setError(getString(R.string.wrong_format));
                    isValid = false;
                }
                try {
                    Double.parseDouble(editTextWidth.getText().toString());
                } catch (NumberFormatException e) {
                    editTextWidth.setError(getString(R.string.wrong_format));
                    isValid = false;
                }
                try {
                    Double.parseDouble(editTextHeight.getText().toString());
                } catch (NumberFormatException e) {
                    editTextHeight.setError(getString(R.string.wrong_format));
                    isValid = false;
                }
            }

            if(isValid){
                //check apakah nilai gw lebih kecil dari response bernilai double
                if (viewModel.getShipment().getValue() != null) {
                    if (Double.parseDouble(editTextGW.getText().toString()) < viewModel.getShipment().getValue().getGrossWeight()) {
                        editTextGW.setError(getString(R.string.weight_must_greater_than_or_equal) + viewModel.getShipment().getValue().getGrossWeight());
                        isValid = false;
                    }
                }
            }

            if (isValid) {
                doSubmit();
            }
        });

        //add click event to scandbarcodeBtn
        scanBarcodeBtn.setOnClickListener(v -> {
            handleScan();
        });
    }

    private void doGetShipment() {
        ApiService apiService = ApiClient.getInstant(getContext()).create(ApiService.class);

        Call<ApiResponse<ShipmentModel>> call = apiService.getShipment(editTextSearch.getText().toString().trim(), "get-shipment");

        call.enqueue(new Callback<ApiResponse<ShipmentModel>>() {

            @Override
            public void onResponse(Call<ApiResponse<ShipmentModel>> call, Response<ApiResponse<ShipmentModel>> response) {
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        viewModel.setShipment(response.body().getData());
                        if (viewModel.getShipment().getValue() != null) {
                            viewModel.setStateAsCreated();
                            editTextGW.setText(String.valueOf(viewModel.getShipment().getValue().getGrossWeight()));
                            editTextLength.setText(String.valueOf(viewModel.getShipment().getValue().getLength()));
                            editTextWidth.setText(String.valueOf(viewModel.getShipment().getValue().getWidth()));
                            editTextHeight.setText(String.valueOf(viewModel.getShipment().getValue().getHeight()));
                            //focuse to search
                            editTextSearch.requestFocus();
                        } else {
                            //play sound
                            soundPlayer.playSound(getContext(), R.raw.error);

                            Toast.makeText(getContext(), getString(R.string.data_not_found), Toast.LENGTH_SHORT).show();
                            //focuse to search
                            editTextSearch.requestFocus();
                            //clear all input
                            editTextSearch.setText("");
                        }
                    } else {
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        //focuse to search
                        editTextSearch.requestFocus();
                        //clear all input
                        editTextSearch.setText("");
                    }
                } else {
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                    //focuse to search
                    editTextSearch.requestFocus();
                    //clear all input
                    editTextSearch.setText("");
                }

            }

            @Override
            public void onFailure(Call<ApiResponse<ShipmentModel>> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void doSubmit() {
        ApiService apiService = ApiClient.getInstant(getContext()).create(ApiService.class);

        Call<ApiResponse> call = apiService.acceptShipment(
                "submit-acceptance",
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
                    if (response.body().isSuccess()) {
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        viewModel.setStateAsNew();
                        editTextSearch.setText("");
                        editTextGW.setText("");
                        editTextLength.setText("");
                        editTextWidth.setText("");
                        editTextHeight.setText("");
                    } else {
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleScan() {
        Intent intent = new Intent(getActivity(), ScanBarcodeActivity.class);
        scanBarcode.launch(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}