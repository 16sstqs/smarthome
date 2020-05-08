package com.fudan.smarthome.ui.home;


import com.fudan.smarthome.ui.home.entity.HomeEnvironmentEntity;


import androidx.lifecycle.MutableLiveData;

public class HomeEnvironmentViewModel {
    private final String TAG = "HomeEnvironViewModel";
    private MutableLiveData<HomeEnvironmentEntity> homeEnv;

    private static volatile HomeEnvironmentViewModel homeEnvironmentViewModel;

    private HomeEnvironmentViewModel() {
    }

    public MutableLiveData<HomeEnvironmentEntity> getHomeEnv() {
        return homeEnv;
    }

    public static HomeEnvironmentViewModel getHomeEnvironmentViewModelInstance() {

        if (homeEnvironmentViewModel == null) {
            synchronized (HomeEnvironmentViewModel.class) {
                if (homeEnvironmentViewModel == null) {
                    homeEnvironmentViewModel = new HomeEnvironmentViewModel();
                    homeEnvironmentViewModel.homeEnv = new MutableLiveData<>();
                }
            }
        }
        return homeEnvironmentViewModel;
    }
}

