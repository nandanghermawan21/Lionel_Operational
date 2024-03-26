package com.lionel.operational.ui.console;

import static com.lionel.operational.model.Constant.REQUEST_CODE;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.lionel.operational.GetDestinationActivity;
import com.lionel.operational.LoginActivity;
import com.lionel.operational.MainActivity;
import com.lionel.operational.R;
import com.lionel.operational.ui.PaySlip.PaySlipFragment;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ConsoleCreateFragment extends Fragment {

    private ConsoleCreateViewModel viewModel;

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

        // Temukan tombol di dalam layout fragment_console.xml
        Button button = view.findViewById(R.id.buttonSelectDestination);

        // Atur OnClickListener untuk tombol
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handler untuk klik tombol
                // Panggil metode atau lakukan tindakan yang Anda inginkan di sini
                handleButtonClick();
            }
        });
    }

    private void handleButtonClick() {
        //buka dan dapatkan balikan dari GetDestinationActivity
        Intent intent = new Intent(getContext(), GetDestinationActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }
}