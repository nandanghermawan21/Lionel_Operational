package com.lionel.operational.ui.acceptance;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.lionel.operational.databinding.FragmentAcceptanceBinding;

public class AcceptanceFragment extends Fragment {

    private FragmentAcceptanceBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AcceptanceViewModel galleryViewModel =
                new ViewModelProvider(this).get(AcceptanceViewModel.class);

        binding = FragmentAcceptanceBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}