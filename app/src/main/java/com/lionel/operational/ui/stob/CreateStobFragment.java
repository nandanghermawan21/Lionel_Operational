package com.lionel.operational.ui.stob;

import static com.lionel.operational.model.Constant.GET_SHIPPING_AGENT;
import static com.lionel.operational.model.Constant.GET_SHIPPING_METHOD;
import static com.lionel.operational.model.Constant.PREFERENCES_KEY;
import static com.lionel.operational.model.Constant.USERDATA;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.lionel.operational.GetShippingAgentActivity;
import com.lionel.operational.GetShippingMethodActivity;
import com.lionel.operational.R;
import com.lionel.operational.model.AccountModel;
import com.lionel.operational.model.ShippingAgentModel;
import com.lionel.operational.model.ShippingMethodModel;

public class CreateStobFragment extends Fragment {

    private TextInputEditText editTextOrigin;
    private CreateStobViewModel viewModel;
    Button buttonNext;
    Button buttonCancel;
    Button buttonSubmit;
    ScrollView layoutInputHeader;
    Button selectShippingAgent;
    TextView labelShippingAgentError;
    Button selectShippingMethod;
    TextView labelShippingMethodError;
    TextInputEditText inputCarLicense;
    TextView labelCarLicenseError;
    TextInputEditText inputSealNo;
    TextView labelSealNoError;

    private final ActivityResultLauncher<Intent> shippingMethodLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    String shippingMethod = data.getStringExtra(GET_SHIPPING_METHOD);
                    //ubah data ke dalam bentuk object
                    ShippingMethodModel shippingMethodModel = new Gson().fromJson(shippingMethod, ShippingMethodModel.class);
                    // set data ke dalam view model
                    viewModel.setShippingMethod(shippingMethodModel);
                }
            });

    private final ActivityResultLauncher<Intent> shippingAgentLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    String shippingAgent = data.getStringExtra(GET_SHIPPING_AGENT);
                    //ubah data ke dalam bentuk object
                    ShippingAgentModel shippingAgentModel = new Gson().fromJson(shippingAgent, ShippingAgentModel.class);
                    // set data ke dalam view model
                    viewModel.setShippingAgent(shippingAgentModel);
                }
            });

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(CreateStobViewModel.class);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stob_create, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //get data user from shared preferences
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
        AccountModel account = new Gson().fromJson(sharedPreferences.getString(USERDATA, "{}"), AccountModel.class);

        //initialize views
        editTextOrigin = view.findViewById(R.id.textInputOrigin);
        layoutInputHeader = view.findViewById(R.id.inputHeaderLayout);
        buttonNext = view.findViewById(R.id.buttonNext);
        buttonCancel = view.findViewById(R.id.buttonCancel);
        buttonSubmit = view.findViewById(R.id.buttonSubmit);
        selectShippingAgent = view.findViewById(R.id.buttonSelectShippingAgent);
        labelShippingAgentError = view.findViewById(R.id.labelShippingAgentError);
        selectShippingMethod = view.findViewById(R.id.buttonSelectShippingMethod);
        labelShippingMethodError = view.findViewById(R.id.labelShippingMethodError);
        inputCarLicense = view.findViewById(R.id.textInputCarLicense);
        labelCarLicenseError = view.findViewById(R.id.labeCarLicenseError);
        inputSealNo = view.findViewById(R.id.textInputSealNo);
        labelSealNoError = view.findViewById(R.id.labelSealNoError);


        //set values
        editTextOrigin.setText(account.getBranchId());

        //observe view model
        viewModel.getState().observe(getViewLifecycleOwner(), state -> {
            if (viewModel.isStateNew()) {
                layoutInputHeader.setVisibility(View.VISIBLE);
                buttonCancel.setVisibility(View.GONE);
                buttonSubmit.setVisibility(View.GONE);
                buttonNext.setVisibility(View.VISIBLE);
            } else if (viewModel.isStateCreated()) {
                layoutInputHeader.setVisibility(View.GONE);
                buttonCancel.setVisibility(View.VISIBLE);
                buttonSubmit.setVisibility(View.VISIBLE);
                buttonNext.setVisibility(View.GONE);
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isValid = true;
                if (viewModel.getShippingAgent().getValue() == null) {
                    isValid = false;
                    labelShippingAgentError.setText(getString(R.string.shipping_agent_required));
                }else{
                    labelShippingAgentError.setText("");
                }
                //validate shipping method
                if (viewModel.getShippingMethod().getValue() == null) {
                    isValid = false;
                    labelShippingMethodError.setText(getString(R.string.shipping_method_required));
                }else{
                    labelShippingMethodError.setText("");
                }
                //validate car license
                if (inputCarLicense.getText().toString().isEmpty()) {
                    isValid = false;
                    labelCarLicenseError.setText(getString(R.string.car_license_required));
                }else{
                    labelCarLicenseError.setText("");
                }
                //validate seal no
                if (inputSealNo.getText().toString().isEmpty()) {
                    isValid = false;
                    labelSealNoError.setText(getString(R.string.seal_no_required));
                }else{
                    labelSealNoError.setText("");
                }

                if(isValid) {
                    viewModel.setStateAsCreated();
                }
            }
        });

        //wath shipping method data
        viewModel.getShippingMethod().observe(getViewLifecycleOwner(), shippingMethodModel -> {
            if(shippingMethodModel != null){
                selectShippingMethod.setText(shippingMethodModel.getId());
                labelShippingMethodError.setText("");
            }else{
                labelShippingMethodError.setText(getString(R.string.select_shipping_method));
            }
        });

        //watch shipping agent data
        viewModel.getShippingAgent().observe(getViewLifecycleOwner(), shippingAgentModel -> {
            if(shippingAgentModel != null){
                selectShippingAgent.setText(shippingAgentModel.getName());
                labelShippingAgentError.setText("");
            }else{
                labelShippingAgentError.setText(getString(R.string.select_shipping_agent));
            }
        });

        selectShippingMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectShippingMethod();
            }
        });

        selectShippingAgent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectShippingAgent();
            }
        });
    }

    void selectShippingAgent(){
        Intent intent = new Intent(getContext(), GetShippingAgentActivity.class);
        shippingAgentLauncher.launch(intent);
    }

    void selectShippingMethod(){
        Intent intent = new Intent(getContext(), GetShippingMethodActivity.class);
        shippingMethodLauncher.launch(intent);
    }
}