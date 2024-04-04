package com.lionel.operational.ui.stob;

import static com.lionel.operational.model.Constant.GET_SHIPPING_AGENT;
import static com.lionel.operational.model.Constant.GET_SHIPPING_METHOD;
import static com.lionel.operational.model.Constant.GET_SHIPPING_SERVICE;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.lionel.operational.GetShippingAgentActivity;
import com.lionel.operational.GetShippingMethodActivity;
import com.lionel.operational.R;
import com.lionel.operational.adapter.ServiceRecycleViewAdapter;
import com.lionel.operational.adapter.ShipmentRecycleViewAdapter;
import com.lionel.operational.adapter.WayBillRecycleViewAdapter;
import com.lionel.operational.model.AccountModel;
import com.lionel.operational.model.ApiClient;
import com.lionel.operational.model.ApiResponse;
import com.lionel.operational.model.ApiService;
import com.lionel.operational.model.DestinationModel;
import com.lionel.operational.model.ServiceModel;
import com.lionel.operational.model.ShipmentModel;
import com.lionel.operational.model.ShippingAgentModel;
import com.lionel.operational.model.ShippingMethodModel;
import com.lionel.operational.model.WayBillModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    ScrollView layoutInputDetail;
    TextView detailOrigin;
    TextView detailShippingAgent;
    TextView detailShippingMethod;
    TextView detailCarLicense;
    TextView detailSealNo;
    private RecyclerView recyclerViewWaybill;
    private WayBillRecycleViewAdapter waybillAdapter;
    private CheckBox selectAll;
    private TextInputEditText searchWaybill;

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

        recyclerViewWaybill = view.findViewById(R.id.recyclerViewWayBill);
        recyclerViewWaybill.setLayoutManager(new LinearLayoutManager(getActivity()));

        waybillAdapter = new WayBillRecycleViewAdapter(viewModel.getWayBillList().getValue());
        recyclerViewWaybill.setAdapter(waybillAdapter);

        waybillAdapter.setOnItemClickListener(new WayBillRecycleViewAdapter.OnItemOptionClickListener() {
            @Override
            public void onItemCheck(WayBillModel position) {

            }
        });

        //initialize cehck all and search waybill
        selectAll = view.findViewById(R.id.checkAll);
        searchWaybill = view.findViewById(R.id.searchWaybill);

        //add listener to check all
        selectAll.setOnClickListener(v -> {
            if (selectAll.isChecked()) {
                waybillAdapter.selectAll();
                waybillAdapter.notifyDataSetChanged();
            } else {
                waybillAdapter.unSelectAll();
                waybillAdapter.notifyDataSetChanged();
            }
        });

        //add listener to search waybill
        searchWaybill.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                waybillAdapter.filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        return view;
    }

    private void getDataWayBillFromAPi() {
        ApiService apiService = ApiClient.getInstant().create(ApiService.class);

        Call<ApiResponse<List<WayBillModel>>> call = apiService.getStobWayBill("get-shipment", viewModel.getShippingMethod().getValue().getId());

        call.enqueue(new Callback<ApiResponse<List<WayBillModel>>>() {

            @Override
            public void onResponse(Call<ApiResponse<List<WayBillModel>>> call, Response<ApiResponse<List<WayBillModel>>> response) {
                if(response.body().isSuccess()) {
                    viewModel.setWayBillList(response.body().getData());
                    waybillAdapter = new WayBillRecycleViewAdapter(viewModel.getWayBillList().getValue());
                    recyclerViewWaybill.setAdapter(waybillAdapter);
                    waybillAdapter.setOnItemClickListener(new WayBillRecycleViewAdapter.OnItemOptionClickListener() {
                        @Override
                        public void onItemCheck(WayBillModel position) {

                        }
                    });
                    Log.e(GET_SHIPPING_SERVICE, "onResponse: "+ viewModel.getWayBillList().getValue().size());
                } else {
                    Toast.makeText(getContext().getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(GET_SHIPPING_SERVICE, "onResponse: "+ response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<WayBillModel>>> call, Throwable t) {
                Log.e(GET_SHIPPING_SERVICE, "onFailure: "+ t.getMessage());
            }
        });
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
        layoutInputDetail = view.findViewById(R.id.layoutInputDetail);
        detailOrigin = view.findViewById(R.id.detailOrigin);
        detailShippingAgent = view.findViewById(R.id.detailShippingAgent);
        detailShippingMethod = view.findViewById(R.id.detailShippingMethod);
        detailCarLicense = view.findViewById(R.id.detailLicenseNo);
        detailSealNo = view.findViewById(R.id.detailSealNo);

        //set values
        editTextOrigin.setText(account.getBranchId());
        detailOrigin.setText(account.getBranchId());

        //observe view model
        viewModel.getState().observe(getViewLifecycleOwner(), state -> {
            if (viewModel.isStateNew()) {
                layoutInputHeader.setVisibility(View.VISIBLE);
                buttonCancel.setVisibility(View.GONE);
                buttonSubmit.setVisibility(View.GONE);
                buttonNext.setVisibility(View.VISIBLE);
                layoutInputDetail.setVisibility(View.GONE);
            } else if (viewModel.isStateCreated()) {
                layoutInputHeader.setVisibility(View.GONE);
                buttonCancel.setVisibility(View.VISIBLE);
                buttonSubmit.setVisibility(View.VISIBLE);
                buttonNext.setVisibility(View.GONE);
                layoutInputDetail.setVisibility(View.VISIBLE);
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
                    detailCarLicense.setText(inputCarLicense.getText().toString());
                    detailSealNo.setText(inputSealNo.getText().toString());
                    viewModel.setStateAsCreated();
                    getDataWayBillFromAPi();
                }
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.setStateAsNew();
            }
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSubmit();
            }
        });

        //wath shipping method data
        viewModel.getShippingMethod().observe(getViewLifecycleOwner(), shippingMethodModel -> {
            if(shippingMethodModel != null){
                selectShippingMethod.setText(shippingMethodModel.getId());
                detailShippingMethod.setText(shippingMethodModel.getId());
                labelShippingMethodError.setText("");
            }else{
                labelShippingMethodError.setText(getString(R.string.select_shipping_method));
            }
        });

        //watch shipping agent data
        viewModel.getShippingAgent().observe(getViewLifecycleOwner(), shippingAgentModel -> {
            if(shippingAgentModel != null){
                selectShippingAgent.setText(shippingAgentModel.getName());
                detailShippingAgent.setText(shippingAgentModel.getName());
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

    private void doSubmit(){
        ApiService apiService = ApiClient.getInstant().create(ApiService.class);

        //get data user from shared preferences
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
        AccountModel account = new Gson().fromJson(sharedPreferences.getString(USERDATA, "{}"), AccountModel.class);

        Call<ApiResponse> call = apiService.submitStob(
                "submit-stob",
                account.getBranchId(),
                account.getBranchId(),
                viewModel.getShippingAgent().getValue().getId(),
                viewModel.getShippingMethod().getValue().getId(),
                inputCarLicense.getText().toString(),
                inputSealNo.getText().toString(),
                account.getName(),
                waybillAdapter.getSelectedList().stream().map(WayBillModel::getCode).collect(Collectors.toList())
                );

        call.enqueue(new retrofit2.Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, retrofit2.Response<ApiResponse> response) {
                if(response.isSuccessful() && response.body() != null) {
                    if(response.body().isSuccess()) {
                        //show success message
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        //reset form
                        viewModel.setStateAsNew();
                        viewModel.setShippingAgent(null);
                        viewModel.setShippingMethod(null);
                        inputCarLicense.setText("");
                        inputSealNo.setText("");
                        labelShippingAgentError.setText("");
                        labelShippingMethodError.setText("");
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