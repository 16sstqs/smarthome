package com.fudan.smarthome.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.fudan.smarthome.R;
import com.fudan.smarthome.ui.home.entity.HomeEnvironmentEntity;

public class HomeFragment extends Fragment {

    private HomeEnvironmentViewModel homeEnvModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView tempView = root.findViewById(R.id.text_temp);
        final TextView humView = root.findViewById(R.id.text_hum);
        final TextView longView = root.findViewById(R.id.text_long);
        final TextView latView = root.findViewById(R.id.text_lat);

        homeEnvModel = HomeEnvironmentViewModel.getHomeEnvironmentViewModelInstance();
        homeEnvModel.getHomeEnv().observe(this, new Observer<HomeEnvironmentEntity>() {
            @Override
            public void onChanged(HomeEnvironmentEntity homeEnvironmentEntity) {
                tempView.setText("temperature:"+homeEnvironmentEntity.getTemp()+"humidity:"+homeEnvironmentEntity.getHum());
//                humView.setText(homeEnvironmentEntity.getHum());
//                longView.setText(homeEnvironmentEntity.getLongitude());
//                latView.setText(homeEnvironmentEntity.getLatitude());

            }
        });


        return root;
    }


}