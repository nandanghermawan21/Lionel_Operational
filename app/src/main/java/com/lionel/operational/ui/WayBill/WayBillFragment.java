package com.lionel.operational.ui.WayBill;

import static com.lionel.operational.model.Constant.GET_DESTINATION;
import static com.lionel.operational.model.Constant.GET_SHIPPING_AGENT;
import static com.lionel.operational.model.Constant.GET_SHIPPING_LINER;
import static com.lionel.operational.model.Constant.GET_SHIPPING_METHOD;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.lionel.operational.GetDestinationActivity;
import com.lionel.operational.GetShipmentLinerActivity;
import com.lionel.operational.GetShippingAgentActivity;
import com.lionel.operational.GetShippingMethodActivity;
import com.lionel.operational.R;
import com.lionel.operational.adapter.ShipmentRecycleViewAdapter;
import com.lionel.operational.model.ApiClient;
import com.lionel.operational.model.ApiResponse;
import com.lionel.operational.model.ApiService;
import com.lionel.operational.model.DestinationModel;
import com.lionel.operational.model.ShipmentModel;
import com.lionel.operational.model.ShippingAgentModel;
import com.lionel.operational.model.ShippingLinerModel;
import com.lionel.operational.model.ShippingMethodModel;

import java.util.List;

import retrofit2.Call;

public class WayBillFragment extends Fragment {

    private WayBillViewModel viewModel;
    private ScrollView layoutInputWayBill;
    private TextInputEditText inputWayBillNumber;
    private Button buttonNext;
    private Button buttonCancel;
    private Button buttonSubmit;
    private TextView labelWayBillError;
    private Button buttonShippingMethod;
    private TextView labelShippingMethodError;
    private Button buttonShippingAgent;
    private TextView labelShippingAgentError;
    private Button buttonOrigin;
    private TextView labelShippingOriginError;
    private Button buttonShippingLiner;
    private TextView labelLinerError;
    private LinearLayout layoutInputDetail;
    private RecyclerView recyclerShipmentView;
    private ShipmentRecycleViewAdapter shipmentReceicleViewAdapter;
    private Button buttonAddShipment;
    private TextInputEditText inputShipmentCode;
    private TextView labelShipmentCodeError;
    private final ActivityResultLauncher<Intent> destinationLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    String destination = data.getStringExtra(GET_DESTINATION);
                    //ubah data ke dalam bentuk object
                    DestinationModel destinationModel = new Gson().fromJson(destination, DestinationModel.class);
                    // set data ke dalam view model
                    viewModel.setOrigin(destinationModel);
                }
            });

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

    private final ActivityResultLauncher<Intent> shippingLinerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    String shippingLiner = data.getStringExtra(GET_SHIPPING_LINER);
                    //ubah data ke dalam bentuk object
                    ShippingLinerModel shippingLinerModel = new Gson().fromJson(shippingLiner, ShippingLinerModel.class);
                    // set data ke dalam view model
                    viewModel.setLiner(shippingLinerModel);
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
        recyclerShipmentView = view.findViewById(R.id.recyclerViewShipment);
        recyclerShipmentView.setLayoutManager(new LinearLayoutManager(getActivity()));

        shipmentReceicleViewAdapter = new ShipmentRecycleViewAdapter(viewModel.getShipmentList().getValue());
        recyclerShipmentView.setAdapter(shipmentReceicleViewAdapter);

        shipmentReceicleViewAdapter.setOnItemClickListener(new ShipmentRecycleViewAdapter.OnItemShipmentClickListener() {
            @Override
            public void onItemClickDelete(ShipmentModel item) {
                viewModel.removeShipment(item);
                shipmentReceicleViewAdapter.notifyDataSetChanged();
            }
        });

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
        buttonShippingMethod = view.findViewById(R.id.buttonSelectShippingMethod);
        labelShippingMethodError = view.findViewById(R.id.labelShippingMethodError);
        buttonShippingAgent = view.findViewById(R.id.buttonSelectShippingAgent);
        labelShippingAgentError = view.findViewById(R.id.labelShippingAgentError);
        buttonOrigin = view.findViewById(R.id.buttonSelectOrigin);
        labelShippingOriginError = view.findViewById(R.id.labelOriginError);
        buttonShippingLiner = view.findViewById(R.id.buttonSelectLiner);
        labelLinerError = view.findViewById(R.id.labelLinerError);
        layoutInputDetail = view.findViewById(R.id.layoutInputDetail);
        buttonAddShipment = view.findViewById(R.id.buttonAddShipment);
        inputShipmentCode = view.findViewById(R.id.textInputSTTCode);
        labelShipmentCodeError = view.findViewById(R.id.labelSTTCodeError);

        //obserb status
        viewModel.getState().observe(getViewLifecycleOwner(), status -> {
            if (viewModel.isStateNew()) {
                layoutInputWayBill.setVisibility(View.VISIBLE);
                buttonNext.setVisibility(View.VISIBLE);
                layoutInputDetail.setVisibility(View.GONE);
                buttonCancel.setVisibility(View.GONE);
                buttonSubmit.setVisibility(View.GONE);
            } else {
                layoutInputWayBill.setVisibility(View.GONE);
                buttonNext.setVisibility(View.GONE);
                layoutInputDetail.setVisibility(View.VISIBLE);
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

                if(isValid){
                    viewModel.setStateAsCreated();
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

        buttonShippingMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSelectShippingMethod();
            }
        });

        buttonShippingAgent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSelectShippingAgent();
            }
        });

        buttonShippingLiner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSelectShippingLiner();
            }
        });

        //wath shipping method data
        viewModel.getShippingMethod().observe(getViewLifecycleOwner(), shippingMethodModel -> {
            if(shippingMethodModel != null){
                buttonShippingMethod.setText(shippingMethodModel.getId());
                labelShippingMethodError.setText("");
            }else{
                buttonShippingMethod.setText(getString(R.string.select_shipping_method));
            }
        });

        //watch shipping agent data
        viewModel.getShippingAgent().observe(getViewLifecycleOwner(), shippingAgentModel -> {
            if(shippingAgentModel != null){
                buttonShippingAgent.setText(shippingAgentModel.getName());
                labelShippingAgentError.setText("");
            }else{
                buttonShippingAgent.setText(getString(R.string.select_shipping_agent));
            }
        });

        //watch shipping liner data
        viewModel.getLiner().observe(getViewLifecycleOwner(), shippingAgentModel -> {
            if(shippingAgentModel != null){
                buttonShippingLiner.setText(shippingAgentModel.getName());
                labelLinerError.setText("");
            }else{
                buttonShippingLiner.setText(getString(R.string.select_liner));
            }
        });

        buttonAddShipment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create object shipemnt
                ShipmentModel shipmentModel = new ShipmentModel();
                //validaasi apakah shipmentCode sudah diisi
                if(inputShipmentCode.getText().toString().isEmpty()) {
                    labelShipmentCodeError.setText(getString(R.string.please_fill_STT));
                    labelShipmentCodeError.setVisibility(View.VISIBLE);
                }else{
                    doAddShipment();
                }
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputWayBillNumber.setText("");
                viewModel.clear();
            }
        });
    }

    private void doAddShipment() {
        //ambil shipment dari api
        ApiService apiService = ApiClient.getInstant().create(ApiService.class);

        Call<ApiResponse<List<ShipmentModel>>> call = apiService.getWayBillShipment("get-shipment", inputShipmentCode.getText().toString(),  viewModel.getOrigin().getValue().getId());

        call.enqueue(new retrofit2.Callback<ApiResponse<List<ShipmentModel>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<ShipmentModel>>> call, retrofit2.Response<ApiResponse<List<ShipmentModel>>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    if(response.body().isSuccess()) {
                        List<ShipmentModel> shipmentModels = response.body().getData();
                        //for test set parent code
                        viewModel.addShipmentList(shipmentModels);
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
                    }else{
                        //show error message
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<ShipmentModel>>> call, Throwable t) {
                //show error message
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openSelectOrigin() {
        Intent intent = new Intent(getActivity(), GetDestinationActivity.class);
        destinationLauncher.launch(intent);
    }

    private void openSelectShippingMethod() {
        Intent intent = new Intent(getActivity(), GetShippingMethodActivity.class);
        shippingMethodLauncher.launch(intent);
    }

    private void openSelectShippingAgent() {
        Intent intent = new Intent(getActivity(), GetShippingAgentActivity.class);
        shippingAgentLauncher.launch(intent);
    }

    private void openSelectShippingLiner() {
        Intent intent = new Intent(getActivity(), GetShipmentLinerActivity.class);
        shippingLinerLauncher.launch(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}