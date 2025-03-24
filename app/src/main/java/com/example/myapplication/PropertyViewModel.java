package com.example.myapplication;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.myapplication.data.Property;
import java.util.List;

public class PropertyViewModel extends ViewModel {
    private MutableLiveData<List<Property>> properties;

    public LiveData<List<Property>> getProperties() {
        if (properties == null) {
            properties = new MutableLiveData<>();
            loadProperties();
        }
        return properties;
    }

    private void loadProperties() {
        // Load properties from repository
    }
}