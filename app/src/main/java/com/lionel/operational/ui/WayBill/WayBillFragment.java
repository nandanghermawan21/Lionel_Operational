package com.lionel.operational.ui.WayBill;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.lionel.operational.model.DestinationModel;

public class WayBillFragment extends Fragment {

    private WayBillViewModel viewModel;
    private LinearLayout layoutInputWayBill;
    private TextInputEditText inputWayBillNumber;
    private Button buttonNext;
    private Button buttonCancel;
    private Button buttonSubmit;
    private TextView labelWayBillError;
    private TextView labelShippingMethodError;
    private TextView labelShippingAgentError;
    private Button buttonOrigin;
    private TextView labelShippingOriginError;
    private TextView labelLinerError;

    private final ActivityResultLauncher<Intent> destinationLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    String destination = data.getStringExtra("DESTINATION");
                    //ubah data ke dalam bentuk object
                    DestinationModel destinationModel = new Gson().fromJson(destination, DestinationModel.class);
                    // set data ke dalam view model
                    viewModel.setOrigin(destinationModel);
                }
            });

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(WayBillViewModel.class);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_waybill, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inputWayBillNumber = view.findViewById(R.id.textInputWayBillNo);
        layoutInputWayBill = view.findViewById(R.id.layoutInputWayBill);
        buttonNext = view.findViewById(R.id.buttonNext);
        buttonCancel = view.findViewById(R.id.buttonCancel);
        buttonSubmit = view.findViewById(R.id.buttonSubmit);
        labelWayBillError = view.findViewById(R.id.labelWayBillNoError);
        labelShippingMethodError = view.findViewById(R.id.labelShippingMethodError);
        labelShippingAgentError = view.findViewById(R.id.labelShippingAgentError);
        buttonOrigin = view.findViewById(R.id.buttonSelectOrigin);
        labelShippingOriginError = view.findViewById(R.id.labelOriginError);
        labelLinerError = view.findViewById(R.id.labelLinerError);

        //obserb status
        viewModel.getState().observe(getViewLifecycleOwner(), status -> {
            if (viewModel.isStateNew()) {
                layoutInputWayBill.setVisibility(View.VISIBLE);
                buttonNext.setVisibility(View.VISIBLE);
                buttonCancel.setVisibility(View.GONE);
                buttonSubmit.setVisibility(View.GONE);
            } else {
                layoutInputWayBill.setVisibility(View.GONE);
                buttonNext.setVisibility(View.GONE);
                buttonCancel.setVisibility(View.VISIBLE);
                buttonSubmit.setVisibility(View.VISIBLE);
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //validate input
                boolean isValid = true;
                if (inputWayBillNumber.getText().toString().isEmpty()) {
                    labelWayBillError.setText(getString(R.string.waybill_no_required));
                    isValid = false;
                } else {
                    labelWayBillError.setText("");
                }

                if(viewModel.getShippingMethod().getValue() == null){
                    labelShippingMethodError.setText(getString(R.string.shipping_method_required));
                    isValid = false;
                }else{
                    labelShippingMethodError.setText("");
                }

                if(viewModel.getShippingAgent().getValue() == null){
                    labelShippingAgentError.setVisibility(View.VISIBLE);
                    labelShippingAgentError.setText(getString(R.string.shipping_agent_required));
                    isValid = false;
                }else{
                    labelShippingAgentError.setText("");
                }

                if(viewModel.getOrigin().getValue() == null){
                    labelShippingOriginError.setText(getString(R.string.rigin_required));
                    isValid = false;
                }else{
                    labelShippingOriginError.setText("");
                }

                if(viewModel.getLiner().getValue() == null){
                    labelLinerError.setText(getString(R.string.liner_required));
                    isValid = false;
                }else{
                    labelLinerError.setText("");
                }
            }
        });

        buttonOrigin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSelectOrigin();
            }
        });

        viewModel.getOrigin().observe(getViewLifecycleOwner(), destinationModel -> {
            if(destinationModel != null){
                buttonOrigin.setText(destinationModel.getId());
                labelShippingOriginError.setText("");
            }else{
                buttonOrigin.setText(getString(R.string.select_origin));
            }
        });
    }

    private void openSelectOrigin() {
        Intent intent = new Intent(getActivity(), GetDestinationActivity.class);
        destinationLauncher.launch(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}