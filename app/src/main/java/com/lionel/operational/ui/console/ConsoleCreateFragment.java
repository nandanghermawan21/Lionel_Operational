package com.lionel.operational.ui.console;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.lionel.operational.GetDestinationActivity;
import com.lionel.operational.R;
import com.lionel.operational.model.DestinationModel;

import org.jetbrains.annotations.Nullable;

public class ConsoleCreateFragment extends Fragment {

    private ConsoleCreateViewModel viewModel;
    Button buttonGetDestination;
    Button buttonNext;
    TextView labelConsoleCodeError;
    TextView labelDestinationError;
    TextInputEditText inputConsoleCode;

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
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_console, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        buttonGetDestination = view.findViewById(R.id.buttonSelectDestination);
        buttonNext = view.findViewById(R.id.buttonNext);
        labelConsoleCodeError = view.findViewById(R.id.labelConsoleCodeError);
        labelDestinationError = view.findViewById(R.id.labelDestinationError);
        inputConsoleCode = view.findViewById(R.id.textInputConsoleCode);

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
            }
        });

        viewModel.getDestinationModel().observe(getViewLifecycleOwner(), new Observer<DestinationModel>() {
            @Override
            public void onChanged(DestinationModel destinationModel) {
                buttonGetDestination.setText(destinationModel.getId());
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
    }

    private void handleButtonClick() {
        Intent intent = new Intent(getActivity(), GetDestinationActivity.class);
        destinationLauncher.launch(intent);
    }

}