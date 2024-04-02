package com.lionel.operational.ui.console;

import static com.lionel.operational.model.Constant.GET_DESTINATION;
import static com.lionel.operational.model.Constant.PREFERENCES_KEY;
import static com.lionel.operational.model.Constant.USERDATA;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.lionel.operational.GetDestinationActivity;
import com.lionel.operational.R;
import com.lionel.operational.adapter.ShipmentRecycleViewAdapter;
import com.lionel.operational.model.AccountModel;
import com.lionel.operational.model.ApiClient;
import com.lionel.operational.model.ApiResponse;
import com.lionel.operational.model.ApiService;
import com.lionel.operational.model.DestinationModel;
import com.lionel.operational.model.ShipmentModel;

import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;

public class ConsoleCreateFragment extends Fragment {

    private ConsoleCreateViewModel viewModel;
    Button buttonGetDestination;
    Button buttonNext;
    Button buttonCancel;
    Button buttonSubmit;
    TextView labelConsoleCodeError;
    TextView labelDestinationError;
    TextInputEditText inputConsoleCode;
    LinearLayout consoleCodeLayout;
    LinearLayout destinationLayout;
    LinearLayout consoleDetailLayout;
    TextView labelDetailConsoleCode;
    TextView labelDetailDestination;
    RecyclerView recyclerShipmentView;
    ShipmentRecycleViewAdapter shipmentReceicleViewAdapter;
    Button buttonAddShipment;
    TextInputEditText inputShipmentCode;
    TextView labelShipmentCodeError;

    private final ActivityResultLauncher<Intent> destinationLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    String destination = data.getStringExtra(GET_DESTINATION);
                    //ubah data ke dalam bentuk object
                     DestinationModel destinationModel = new Gson().fromJson(destination, DestinationModel.class);
                    // set data ke dalam view model
                    viewModel.setDestinationModel(destinationModel);
                }
            });

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ConsoleCreateViewModel.class);

        //initialize view

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_console, container, false);

        recyclerShipmentView = view.findViewById(R.id.recyclerViewShipment);
        recyclerShipmentView.setLayoutManager(new LinearLayoutManager(getActivity()));

        shipmentReceicleViewAdapter = new ShipmentRecycleViewAdapter(viewModel.getShipmentList().getValue());
        recyclerShipmentView.setAdapter(shipmentReceicleViewAdapter);

        //set listener adapter
        shipmentReceicleViewAdapter.setOnItemClickListener(new ShipmentRecycleViewAdapter.OnItemShipmentClickListener() {
            @Override
            public void onItemClickDelete(ShipmentModel item) {
                viewModel.removeShipment(item);
                shipmentReceicleViewAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //initialize view
        buttonGetDestination = view.findViewById(R.id.buttonSelectDestination);
        buttonNext = view.findViewById(R.id.buttonNext);
        buttonCancel = view.findViewById(R.id.buttonCancel);
        buttonSubmit = view.findViewById(R.id.buttonSubmit);
        labelConsoleCodeError = view.findViewById(R.id.labelConsoleCodeError);
        labelDestinationError = view.findViewById(R.id.labelDestinationError);
        inputConsoleCode = view.findViewById(R.id.textInputConsoleCode);
        consoleCodeLayout = view.findViewById(R.id.console_code_layout);
        destinationLayout = view.findViewById(R.id.destination_layout);
        consoleDetailLayout = view.findViewById(R.id.console_detail_layout);
        labelDetailConsoleCode = view.findViewById(R.id.detailConsoleCode);
        labelDetailDestination = view.findViewById(R.id.detailConsoleDestination);
        buttonAddShipment = view.findViewById(R.id.buttonAddShipment);
        inputShipmentCode = view.findViewById(R.id.textInputSTTCode);
        labelShipmentCodeError = view.findViewById(R.id.labelSTTCodeError);

        buttonGetDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButtonClick();
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isValid = true;
                if (viewModel.getDestinationModel().getValue() == null) {
                    isValid = false;
                    viewModel.setDestinationErrorMessage(getString(R.string.error_destination));
                }else{
                    viewModel.setDestinationErrorMessage("");
                }
                if(inputConsoleCode.getText().toString().isEmpty()) {
                    isValid = false;
                    viewModel.setConsoleCodeErrorMessage(getString(R.string.error_console_code));
                }else{
                    viewModel.setConsoleCodeErrorMessage("");
                }

                if(isValid) {
                    viewModel.setStateAsCreated();
                }
            }
        });

        //watch state
        viewModel.getState().observe(getViewLifecycleOwner(), state -> {
            if (viewModel.isStateNew()) {
                buttonCancel.setVisibility(View.GONE);
                buttonSubmit.setVisibility(View.GONE);
                consoleDetailLayout.setVisibility(View.GONE);
                labelDetailConsoleCode.setText("");
                labelDetailDestination.setText("");
                //enable button next
                consoleCodeLayout.setVisibility(View.VISIBLE);
                destinationLayout.setVisibility(View.VISIBLE);
                buttonNext.setVisibility(View.VISIBLE);
            } else if (viewModel.isStateCreated()) {
                buttonCancel.setVisibility(View.VISIBLE);
                buttonSubmit.setVisibility(View.VISIBLE);
                consoleDetailLayout.setVisibility(View.VISIBLE);
                labelDetailConsoleCode.setText(inputConsoleCode.getText().toString());
                labelDetailDestination.setText(viewModel.getDestinationModel().getValue().getId());
                //disable button next
                consoleCodeLayout.setVisibility(View.GONE);
                destinationLayout.setVisibility(View.GONE);
                buttonNext.setVisibility(View.GONE);
            }
        });

        viewModel.getDestinationModel().observe(getViewLifecycleOwner(), new Observer<DestinationModel>() {
            @Override
            public void onChanged(DestinationModel destinationModel) {
                if(destinationModel != null){
                    viewModel.setDestinationErrorMessage("");
                    buttonGetDestination.setText(destinationModel.getId());
                }else{
                    buttonGetDestination.setText(getString(R.string.select_destination));
                }
            }
        });

        viewModel.getConsoleCodeErrorMessage().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                labelConsoleCodeError.setText(s);
                if (s.isEmpty()) {
                    labelConsoleCodeError.setVisibility(View.GONE);
                } else {
                    labelConsoleCodeError.setVisibility(View.VISIBLE);
                }
            }
        });

        viewModel.getDestinationErrorMessage().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                labelDestinationError.setText(s);
                if(s.isEmpty()) {
                    labelDestinationError.setVisibility(View.GONE);
                } else {
                    labelDestinationError.setVisibility(View.VISIBLE);
                }
            }
        });

        //handle on click cancel
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.setStateAsNew();
                inputConsoleCode.setText("");
                viewModel.setDestinationModel(null);
                viewModel.clearShipmentList();
            }
        });

        //handle on click submit
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewModel.getShipmentList().getValue().size() == 0) {
                    Toast.makeText(getContext(), getString(R.string.error_shipment_empty), Toast.LENGTH_SHORT).show();
                }else{
                    doSubmit();
                }
            }
        });

        //handle on click add
        buttonAddShipment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create object shipemnt
                ShipmentModel shipmentModel = new ShipmentModel();
                shipmentModel.setParentCode(inputConsoleCode.getText().toString());
                //validaasi apakah shipmentCode sudah diisi
                if(inputShipmentCode.getText().toString().isEmpty()) {
                    labelShipmentCodeError.setText(getString(R.string.please_fill_STT));
                    labelShipmentCodeError.setVisibility(View.VISIBLE);
                }else{
                    boolean isExist = false;
                    for (ShipmentModel shipment : viewModel.getShipmentList().getValue()) {
                        if(shipment.getBarcode().equals(inputShipmentCode.getText().toString())) {
                            isExist = true;
                            break;
                        }
                    }
                    if(isExist) {
                        labelShipmentCodeError.setText(getString(R.string.error_shipment_exist));
                        labelShipmentCodeError.setVisibility(View.VISIBLE);
                    }else{
                        labelShipmentCodeError.setText("");
                        labelShipmentCodeError.setVisibility(View.GONE);
                        doAddShipment();
                    }
                }

            }
        });

        //wath shipment list
        viewModel.getShipmentList().observe(getViewLifecycleOwner(), new Observer<List<ShipmentModel>>() {
            @Override
            public void onChanged(List<ShipmentModel> shipmentModels) {
                shipmentReceicleViewAdapter = new ShipmentRecycleViewAdapter(shipmentModels);
                recyclerShipmentView.setAdapter(shipmentReceicleViewAdapter);
                shipmentReceicleViewAdapter.notifyDataSetChanged();
            }
        });
    }

    private void handleButtonClick() {
        Intent intent = new Intent(getActivity(), GetDestinationActivity.class);
        destinationLauncher.launch(intent);
    }

    private void doAddShipment() {
        //ambil shipment dari api
        ApiService apiService = ApiClient.getInstant().create(ApiService.class);

        Call<ApiResponse<ShipmentModel>> call = apiService.getShipmentConsole(inputShipmentCode.getText().toString(), "get-shipment",  viewModel.getDestinationModel().getValue().getId());

        call.enqueue(new retrofit2.Callback<ApiResponse<ShipmentModel>>() {
            @Override
            public void onResponse(Call<ApiResponse<ShipmentModel>> call, retrofit2.Response<ApiResponse<ShipmentModel>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    if(response.body().isSuccess()) {
                        ShipmentModel shipmentModel = response.body().getData();
                        //check jika shipment bernilai null maka tampilkan pesan error
                        if(shipmentModel == null) {
                            Toast.makeText(getContext(), getString(R.string.data_not_found), Toast.LENGTH_SHORT).show();
                        }else{
                            //for test set parent code
                            shipmentModel.setBarcode(inputShipmentCode.getText().toString());
                            viewModel.addShipment(shipmentModel);
                            //update adapter
                            shipmentReceicleViewAdapter.notifyDataSetChanged();
                            //set listener adapter
                            shipmentReceicleViewAdapter.setOnItemClickListener(new ShipmentRecycleViewAdapter.OnItemShipmentClickListener() {
                                @Override
                                public void onItemClickDelete(ShipmentModel item) {
                                    viewModel.removeShipment(item);
                                    shipmentReceicleViewAdapter.notifyDataSetChanged();
                                }
                            });
                            //scroll to last position
                            recyclerShipmentView.scrollToPosition(shipmentReceicleViewAdapter.getItemCount() - 1);
                            //clear input
                            inputShipmentCode.setText("");
                        }

                    }else{
                        //show error message
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<ShipmentModel>> call, Throwable t) {
                //show error message
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void doSubmit(){
        ApiService apiService = ApiClient.getInstant().create(ApiService.class);

        //get data user from shared preferences
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
        AccountModel account = new Gson().fromJson(sharedPreferences.getString(USERDATA, "{}"), AccountModel.class);

        Log.i("SUBMIT-CONSOLE", "doSubmit: " + account.getName() + " " + viewModel.getDestinationModel().getValue().getBranchId() + " " + viewModel.getDestinationModel().getValue().getId() + " " + viewModel.getShipmentList().getValue().stream().map(ShipmentModel::getBarcode).collect(Collectors.toList()) + " " + inputConsoleCode.getText().toString());

        Call<ApiResponse> call = apiService.submitConsole(
                "submit-console",
                inputConsoleCode.getText().toString(),
                account.getName(),
                viewModel.getDestinationModel().getValue().getBranchId(),
                viewModel.getDestinationModel().getValue().getId(),
                viewModel.getShipmentList().getValue().stream().map(ShipmentModel::getBarcode).collect(Collectors.toList()));

        call.enqueue(new retrofit2.Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, retrofit2.Response<ApiResponse> response) {
                if(response.isSuccessful() && response.body() != null) {
                    if(response.body().isSuccess()) {
                        //show success message
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        //reset form
                        viewModel.setStateAsNew();
                        inputConsoleCode.setText("");
                        viewModel.setDestinationModel(null);
                    }else{
                        //show error message
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                //show error message
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}