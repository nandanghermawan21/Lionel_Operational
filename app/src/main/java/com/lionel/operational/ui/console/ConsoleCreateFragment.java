package com.lionel.operational.ui.console;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.lionel.operational.adapter.ShipmentReceicleViewAdapter;
import com.lionel.operational.model.DestinationModel;
import com.lionel.operational.model.ShipmentModel;

import org.jetbrains.annotations.Nullable;

import java.util.List;

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
    ShipmentReceicleViewAdapter shipmentReceicleViewAdapter;
    Button buttonAddShipment;
    TextInputEditText inputShipmentCode;

    private final ActivityResultLauncher<Intent> destinationLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    String destination = data.getStringExtra("DESTINATION");
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

        shipmentReceicleViewAdapter = new ShipmentReceicleViewAdapter(viewModel.getShipmentList().getValue());
        recyclerShipmentView.setAdapter(shipmentReceicleViewAdapter);

        // Load data into adapter
        // adapter.setData(data); // You can set data here if needed

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
                }else{
                    buttonGetDestination.setText(destinationModel.getId());
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
            }
        });

        //handle on click add
        buttonAddShipment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create object shipemnt
                ShipmentModel shipmentModel = new ShipmentModel();
                shipmentModel.setParentCode(inputConsoleCode.getText().toString());
                //add new shipment
                viewModel.addShipment(shipmentModel);
            }
        });

        //wath shipment list
        viewModel.getShipmentList().observe(getViewLifecycleOwner(), new Observer<List<ShipmentModel>>() {
            @Override
            public void onChanged(List<ShipmentModel> shipmentModels) {
                shipmentReceicleViewAdapter = new ShipmentReceicleViewAdapter(shipmentModels);
                recyclerShipmentView.setAdapter(shipmentReceicleViewAdapter);
                shipmentReceicleViewAdapter.notifyDataSetChanged();
            }
        });
    }

    private void handleButtonClick() {
        Intent intent = new Intent(getActivity(), GetDestinationActivity.class);
        destinationLauncher.launch(intent);
    }

}