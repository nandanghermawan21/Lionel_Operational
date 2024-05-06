package com.lionel.operational.ui.WayBill;

import static com.lionel.operational.model.Constant.GET_BARCODE;
import static com.lionel.operational.model.Constant.GET_CITY;
import static com.lionel.operational.model.Constant.GET_SHIPPING_AGENT;
import static com.lionel.operational.model.Constant.GET_SHIPPING_LINER;
import static com.lionel.operational.model.Constant.GET_SHIPPING_METHOD;
import static com.lionel.operational.model.Constant.GET_SHIPPING_SERVICE;
import static com.lionel.operational.model.Constant.PREFERENCES_KEY;
import static com.lionel.operational.model.Constant.USERDATA;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import com.lionel.operational.GetCityActivity;
import com.lionel.operational.GetServiceActivity;
import com.lionel.operational.GetShipmentLinerActivity;
import com.lionel.operational.GetShippingAgentActivity;
import com.lionel.operational.GetShippingMethodActivity;
import com.lionel.operational.R;
import com.lionel.operational.ScanBarcodeActivity;
import com.lionel.operational.adapter.ShipmentRecycleViewAdapter;
import com.lionel.operational.model.AccountModel;
import com.lionel.operational.model.ApiClient;
import com.lionel.operational.model.ApiResponse;
import com.lionel.operational.model.ApiService;
import com.lionel.operational.model.CityModel;
import com.lionel.operational.model.ServiceModel;
import com.lionel.operational.model.ShipmentModel;
import com.lionel.operational.model.ShippingAgentModel;
import com.lionel.operational.model.ShippingLinerModel;
import com.lionel.operational.model.ShippingMethodModel;
import com.lionel.operational.model.WaybillShipmentItem;
import com.lionel.operational.util.SoundPlayer;

import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;

public class WayBillFragment extends Fragment {

    private WayBillViewModel viewModel;
    private ScrollView layoutInputWayBill;
    private Button buttonNext;
    private Button buttonCancel;
    private Button buttonSubmit;
    private Button buttonShippingMethod;
    private TextView labelShippingMethodError;
    private Button buttonShippingAgent;
    private TextView labelShippingAgentError;
    private Button buttonOestination;
    private TextView labelShippingDestinationError;
    private Button buttonShippingLiner;
    private TextView labelLinerError;
    private Button buttonShippingService;
    private TextView labelServiceError;
    private ScrollView layoutInputDetail;
    private RecyclerView recyclerShipmentView;
    private ShipmentRecycleViewAdapter shipmentRecycleViewAdapter;
    private Button buttonAddShipment;
    private TextInputEditText inputShipmentCode;
    private TextView labelShipmentCodeError;
    private TextView detailShippingMethod;
    private TextView detailShippingAgent;
    private TextView detailDestination;
    private TextView detailLiner;
    private TextView detailService;
    private TextView detailTotalWeight;
    private LinearLayout totalColiGrossWeightLayout;
    private ImageView scanBarcodeSttBtn;

    private final ActivityResultLauncher<Intent> scanBarcode = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    String barcode = data.getStringExtra(GET_BARCODE);
                    inputShipmentCode.setText(barcode);
                    buttonAddShipment.performClick();
                }
            });
    private final ActivityResultLauncher<Intent> destinationLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    String city = data.getStringExtra(GET_CITY);
                    //ubah data ke dalam bentuk object
                    CityModel cityModel = new Gson().fromJson(city, CityModel.class);
                    // set data ke dalam view model
                    viewModel.setDestination(cityModel);
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

    private final ActivityResultLauncher<Intent> serviceLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    String servic = data.getStringExtra(GET_SHIPPING_SERVICE);
                    //ubah data ke dalam bentuk object
                    ServiceModel serviceModel = new Gson().fromJson(servic, ServiceModel.class);
                    // set data ke dalam view model
                    viewModel.setService(serviceModel);
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

        shipmentRecycleViewAdapter = new ShipmentRecycleViewAdapter(viewModel.getShipmentList().getValue());
        recyclerShipmentView.setAdapter(shipmentRecycleViewAdapter);

        shipmentRecycleViewAdapter.setOnItemClickListener(new ShipmentRecycleViewAdapter.OnItemShipmentClickListener() {
            @Override
            public void onItemClickDelete(ShipmentModel item) {
                viewModel.removeShipment(item);
                shipmentRecycleViewAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        layoutInputWayBill = view.findViewById(R.id.layoutInputWayBill);
        buttonNext = view.findViewById(R.id.buttonNext);
        buttonCancel = view.findViewById(R.id.buttonCancel);
        buttonSubmit = view.findViewById(R.id.buttonSubmit);
        buttonShippingMethod = view.findViewById(R.id.buttonSelectShippingMethod);
        labelShippingMethodError = view.findViewById(R.id.labelShippingMethodError);
        buttonShippingAgent = view.findViewById(R.id.buttonSelectShippingAgent);
        labelShippingAgentError = view.findViewById(R.id.labelShippingAgentError);
        buttonOestination = view.findViewById(R.id.buttonSelectDestination);
        labelShippingDestinationError = view.findViewById(R.id.labelDestinationError);
        buttonShippingLiner = view.findViewById(R.id.buttonSelectLiner);
        labelLinerError = view.findViewById(R.id.labelLinerError);
        buttonShippingService = view.findViewById(R.id.buttonSelectService);
        labelServiceError = view.findViewById(R.id.labelServiceError);
        layoutInputDetail = view.findViewById(R.id.layoutInputDetail);
        buttonAddShipment = view.findViewById(R.id.buttonAddShipment);
        inputShipmentCode = view.findViewById(R.id.textInputSTTCode);
        labelShipmentCodeError = view.findViewById(R.id.labelSTTCodeError);
        detailShippingMethod = view.findViewById(R.id.detailWayBillShippingMethod);
        detailShippingAgent = view.findViewById(R.id.detailWayBillShippingAgent);
        detailDestination = view.findViewById(R.id.detailWayBilDestination);
        detailLiner = view.findViewById(R.id.detailWayBilShippingLiner);
        detailService = view.findViewById(R.id.detailWayBilShippingService);
        detailTotalWeight = view.findViewById(R.id.totalColiGrossWeight);
        totalColiGrossWeightLayout = view.findViewById(R.id.totalColiGrossWeightLayout);
        scanBarcodeSttBtn = view.findViewById(R.id.scanBarcodeSttBtn);

        //obserb status
        viewModel.getState().observe(getViewLifecycleOwner(), status -> {
            if (viewModel.isStateNew()) {
                layoutInputWayBill.setVisibility(View.VISIBLE);
                buttonNext.setVisibility(View.VISIBLE);
                layoutInputDetail.setVisibility(View.GONE);
                buttonCancel.setVisibility(View.GONE);
                buttonSubmit.setVisibility(View.GONE);
                totalColiGrossWeightLayout.setVisibility(View.GONE);
                scanBarcodeSttBtn.setVisibility(View.GONE);
            } else {
                layoutInputWayBill.setVisibility(View.GONE);
                buttonNext.setVisibility(View.GONE);
                layoutInputDetail.setVisibility(View.VISIBLE);
                buttonCancel.setVisibility(View.VISIBLE);
                buttonSubmit.setVisibility(View.VISIBLE);
                totalColiGrossWeightLayout.setVisibility(View.VISIBLE);
                scanBarcodeSttBtn.setVisibility(View.VISIBLE);
                //focuse to input shipment code
                inputShipmentCode.requestFocus();
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //validate input
                boolean isValid = true;

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

                if(viewModel.getDestination().getValue() == null){
                    labelShippingDestinationError.setText(getString(R.string.destination_required));
                    isValid = false;
                }else{
                    labelShippingDestinationError.setText("");
                }

                if(viewModel.getLiner().getValue() == null){
                    labelLinerError.setText(getString(R.string.liner_required));
                    isValid = false;
                }else{
                    labelLinerError.setText("");
                }

                if(viewModel.getService().getValue() == null){
                    labelServiceError.setText(getString(R.string.service_required));
                    isValid = false;
                }else{
                    labelServiceError.setText("");
                }

                if(isValid){
                    viewModel.setStateAsCreated();
                }
            }
        });

        buttonOestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSelectDestination();
            }
        });

        // cancel
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.setStateAsNew();
            }
        });

        viewModel.getDestination().observe(getViewLifecycleOwner(), destinationModel -> {
            if(destinationModel != null){
                buttonOestination.setText(destinationModel.getId());
                detailDestination.setText(destinationModel.getId());
                labelShippingDestinationError.setText("");
            }else{
                buttonOestination.setText(getString(R.string.select_destination));
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

        buttonShippingService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSelectShippingService();
            }
        });

        //wath shipping method data
        viewModel.getShippingMethod().observe(getViewLifecycleOwner(), shippingMethodModel -> {
            if(shippingMethodModel != null){
                buttonShippingMethod.setText(shippingMethodModel.getId());
                detailShippingMethod.setText(shippingMethodModel.getId());
                labelShippingMethodError.setText("");
            }else{
                buttonShippingMethod.setText(getString(R.string.select_shipping_method));
            }
        });

        //watch shipping agent data
        viewModel.getShippingAgent().observe(getViewLifecycleOwner(), shippingAgentModel -> {
            if(shippingAgentModel != null){
                buttonShippingAgent.setText(shippingAgentModel.getId() + " - " + shippingAgentModel.getName());
                detailShippingAgent.setText(shippingAgentModel.getId() + " - " + shippingAgentModel.getName());
                labelShippingAgentError.setText("");
            }else{
                buttonShippingAgent.setText(getString(R.string.select_shipping_agent));
            }
        });

        //watch shipping liner data
        viewModel.getLiner().observe(getViewLifecycleOwner(), shippingAgentModel -> {
            if(shippingAgentModel != null){
                buttonShippingLiner.setText(shippingAgentModel.getId() + " - " + shippingAgentModel.getName());
                detailLiner.setText(shippingAgentModel.getId() + " - " + shippingAgentModel.getName());
                labelLinerError.setText("");
            }else{
                buttonShippingLiner.setText(getString(R.string.select_liner));
            }
        });

        //watch shipping service data
        viewModel.getService().observe(getViewLifecycleOwner(), serviceModel -> {
            if(serviceModel != null){
                buttonShippingService.setText(serviceModel.getName());
                detailService.setText(serviceModel.getName());
                labelServiceError.setText("");
            }else{
                buttonShippingService.setText(getString(R.string.select_service));
            }
        });

        buttonAddShipment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create object shipemnt
                ShipmentModel shipmentModel = new ShipmentModel();
                //validaasi apakah shipmentCode sudah diisi
                if(inputShipmentCode.getText().toString().isEmpty()) {
                    labelShipmentCodeError.setText(getString(R.string.please_fill_STT_or_consol_no));
                    labelShipmentCodeError.setVisibility(View.VISIBLE);
                    //focus to input shipment code
                    inputShipmentCode.requestFocus();
                }else{
                    labelShipmentCodeError.setText("");
                    labelShipmentCodeError.setVisibility(View.GONE);
                    doAddShipment();
                }
            }
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check jika shipment list kosong maka tampilkan pesan error
                if(viewModel.getShipmentList().getValue().size() == 0){
                    Toast.makeText(getContext(), getString(R.string.error_shipment_empty), Toast.LENGTH_SHORT).show();
                    return;
                }

                //total weight
                double totalWeight = viewModel.getShipmentList().getValue().stream().map(ShipmentModel::getGrossWeight).reduce(0.0, Double::sum);

                //jika total shipment > dari max coli liner maka tampilkan pesan error
                if(viewModel.getShipmentList().getValue().size() > viewModel.getLiner().getValue().getMaxColi()){
                    Object weight;
                    Toast.makeText(getContext(), getString(R.string.the_total_coli_exceeds_the_maximum_limit) + (" ("+viewModel.getLiner().getValue().getMaxColi()+" Coli)"), Toast.LENGTH_SHORT).show();
                    return;
                }

                //jika total weight melebihi dari max weight liner
                if(totalWeight > viewModel.getLiner().getValue().getMaxGw()){
                    Toast.makeText(getContext(), getString(R.string.the_total_weight_exceeds_the_maximum_limit) + (" ("+viewModel.getLiner().getValue().getMaxGw()+" Kg)"), Toast.LENGTH_SHORT).show();
                    return;
                }

                doSubmit();
            }
        });

        //handle on done input shipment code
        inputShipmentCode.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    buttonAddShipment.performClick();
                    return true;
                }
                return false;
            }
        });

        //watch shipment list
        viewModel.getShipmentList().observe(getViewLifecycleOwner(), shipmentModels -> {
            if(shipmentModels != null){
                String totalWeight = shipmentModels.stream().map(ShipmentModel::getGrossWeight).reduce(0.0, Double::sum).toString();
                detailTotalWeight.setText(String.valueOf(shipmentModels.size()) + " Coli / " + totalWeight + " Kg");
            }
        });

        scanBarcodeSttBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openScanBarcode();
            }
        });

    }

    private void doAddShipment() {
        //ambil shipment dari api
        ApiService apiService = ApiClient.getInstant(getContext()).create(ApiService.class);

        Call<ApiResponse<List<ShipmentModel>>> call = apiService.getWayBillShipment("get-shipment", inputShipmentCode.getText().toString().trim(),  viewModel.getDestination().getValue().getId());

        call.enqueue(new retrofit2.Callback<ApiResponse<List<ShipmentModel>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<ShipmentModel>>> call, retrofit2.Response<ApiResponse<List<ShipmentModel>>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    if(response.body().isSuccess()) {
                        List<ShipmentModel> shipmentModels = response.body().getData();
                        //jika shipmentmodels null kosong maka tampilkan pesan error
                        if(shipmentModels == null || shipmentModels.size() == 0) {
                            //play sound error
                            SoundPlayer soundPlayer = new SoundPlayer();
                            soundPlayer.playSound(getContext(), R.raw.error);

                            Toast.makeText(getContext(), getString(R.string.data_not_found), Toast.LENGTH_SHORT).show();
                            //clear input shipment code
                            inputShipmentCode.setText("");
                            //focus to input shipment code
                            inputShipmentCode.requestFocus();
                        }else{
                            //loop data shipment dan kembalikan pesan error jika shipment telah ada
                            for(ShipmentModel shipmentModel : shipmentModels){
                                if(viewModel.getShipmentList().getValue().stream().anyMatch(item -> item.getCode().equals(shipmentModel.getCode()))){
                                    Toast.makeText(getContext(),   getString(R.string.shipment) + " "+shipmentModel.getCode() + " "+  getString(R.string.already_exist), Toast.LENGTH_SHORT).show();
                                }else{
                                    //tambahkan shipment ke dalam list
                                    viewModel.addShipment(shipmentModel);
                                }
                            }

                            //update adapter
                            shipmentRecycleViewAdapter.notifyDataSetChanged();
                            //set listener adapter
                            shipmentRecycleViewAdapter.setOnItemClickListener(new ShipmentRecycleViewAdapter.OnItemShipmentClickListener() {
                                @Override
                                public void onItemClickDelete(ShipmentModel item) {
                                    viewModel.removeShipment(item);
                                    shipmentRecycleViewAdapter.notifyDataSetChanged();
                                }
                            });
                            //Clear input shipment code
                            inputShipmentCode.setText("");
                            //scroll to last position
                            recyclerShipmentView.scrollToPosition(shipmentRecycleViewAdapter.getItemCount() - 1);
                            //focus to input shipment code
                            inputShipmentCode.requestFocus();
                        }
                    }else{
                        //show error message
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        //clear input shipment code
                        inputShipmentCode.setText("");
                        //focus to input shipment code
                        inputShipmentCode.requestFocus();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<ShipmentModel>>> call, Throwable t) {
                //show error message
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                //clear input shipment code
                inputShipmentCode.setText("");
                //focus to input shipment code
                inputShipmentCode.requestFocus();
            }
        });
    }

    private void doSubmit() {
        ApiService apiService = ApiClient.getInstant(getContext()).create(ApiService.class);

        //get data user from shared preferences
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
        AccountModel account = new Gson().fromJson(sharedPreferences.getString(USERDATA, "{}"), AccountModel.class);

        //kumpulkan barang WaybillShipmentItem dari list shipment
        List<WaybillShipmentItem> waybillShipmentItems = viewModel.getShipmentList().getValue().stream().map(shipmentModel -> {
            return new WaybillShipmentItem(shipmentModel.getCode(), shipmentModel.getConsoleBarcode());
        }).collect(Collectors.toList());

        Call<ApiResponse> call = apiService.submitWaybill(
                "submit-waybill",
                viewModel.getShippingMethod().getValue().getId(),
                viewModel.getShippingAgent().getValue().getId(),
                viewModel.getDestination().getValue().getId(),
                viewModel.getLiner().getValue().getId(),
                viewModel.getService().getValue().getId(),
                account.getBranchId(),
                account.getName(),
                new Gson().toJson(waybillShipmentItems));

        call.enqueue(new retrofit2.Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, retrofit2.Response<ApiResponse> response) {
                if(response.isSuccessful() && response.body() != null) {
                    if(response.body().isSuccess()) {
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        //clear data
                        viewModel.clear();
                    }else{
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openScanBarcode() {
        Intent intent = new Intent(getActivity(), ScanBarcodeActivity.class);
        scanBarcode.launch(intent);
    }

    private void openSelectDestination() {
        Intent intent = new Intent(getActivity(), GetCityActivity.class);
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

    private void openSelectShippingService() {
        Intent intent = new Intent(getActivity(), GetServiceActivity.class);
        serviceLauncher.launch(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}