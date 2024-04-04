package com.lionel.operational.ui.welcome;

import static com.lionel.operational.model.Constant.PREFERENCES_KEY;
import static com.lionel.operational.model.Constant.USERDATA;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lionel.operational.R;
import com.lionel.operational.model.AccountModel;

public class WelcomeFragment extends Fragment {

    TextView tvUsername;
    TextView tvGroupName;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_welcome, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvUsername = view.findViewById(R.id.name);
        tvGroupName = view.findViewById(R.id.group);

        //ambil data user dari shared preferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
        String userData = sharedPreferences.getString(USERDATA, null);
        //convert to object
        if(userData != null){
            AccountModel accountModel = new Gson().fromJson(userData, AccountModel.class);
            tvUsername.setText(accountModel.getName());
            tvGroupName.setText(accountModel.getGroup() + " - " + accountModel.getBranchId());
        }
    }
}