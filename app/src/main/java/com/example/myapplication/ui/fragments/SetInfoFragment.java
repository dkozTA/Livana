package com.example.myapplication.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.data.Model.Location.District;
import com.example.myapplication.data.Model.Location.Province;
import com.example.myapplication.data.Model.Location.Ward;
import com.example.myapplication.data.Repository.Location.LocationAPIClient;
import com.example.myapplication.data.Repository.Location.LocationAPIService;

import java.util.Arrays;
import java.util.List;

public class SetInfoFragment extends Fragment {

    private LocationAPIClient locationApi;
    private AutoCompleteTextView actProvince, actDistrict, actWard;
    private Province selectedProvince = null;
    private District selectedDistrict = null;
    private Ward selectedWard = null;

    class ProvincesCallbackHandler implements LocationAPIClient.OnProvinceListCallback {
        @Override
        public void onSuccess(List<Province> provinces) {
            //nap province adapter
            ArrayAdapter<Province> adapter = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_dropdown_item_1line, provinces);

            actProvince.setAdapter(adapter);
            actProvince.setEnabled(true);
        }

        @Override
        public void onError(String errorMessage) {
            actProvince.setAdapter(null);
            actProvince.setEnabled(false);
        }
    }

    class DistrictCallbackHandler implements LocationAPIClient.OnDistrictListCallback {
        @Override
        public void onSuccess(List<District> districts) {
            //nap province adapter
            ArrayAdapter<District> adapter = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_dropdown_item_1line, districts);

            actDistrict.setAdapter(adapter);
            actDistrict.setEnabled(true);
        }

        @Override
        public void onError(String errorMessage) {
            actDistrict.setAdapter(null);
            actDistrict.setEnabled(false);
        }
    }

    class WardsCallbackHandler implements LocationAPIClient.OnWardListCallback {
        @Override
        public void onSuccess(List<Ward> wards) {
            //nap province adapter
            ArrayAdapter<Ward> adapter = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_dropdown_item_1line, wards);

            actWard.setAdapter(adapter);
            actWard.setEnabled(true);
        }

        @Override
        public void onError(String errorMessage) {
            actWard.setAdapter(null);
            actWard.setEnabled(false);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_set_info, container, false);

        actProvince = view.findViewById(R.id.actProvince);
        actDistrict = view.findViewById(R.id.actDistrict);
        actWard = view.findViewById(R.id.actWard);

        locationApi = new LocationAPIClient();

        actProvince.setEnabled(false);
        actDistrict.setEnabled(false);
        actWard.setEnabled(false);

        locationApi.getAllProvinces(new ProvincesCallbackHandler());
        setupProvince();

        return view;
    }

    private void setupProvince() {

        actProvince.setOnItemClickListener((parent, view, pos, id) -> {
            selectedProvince = (Province) actProvince.getAdapter().getItem(pos);
            selectedDistrict = null;
            selectedWard = null;
            actDistrict.setText("");
            actWard.setText("");
            actDistrict.setEnabled(false);
            actWard.setEnabled(false);

            locationApi.getAllDistrictsInProvince(selectedProvince.code, new DistrictCallbackHandler());
        });

        actDistrict.setOnItemClickListener((parent, view, pos, id) -> {
            selectedDistrict = (District) actDistrict.getAdapter().getItem(pos);
            selectedWard = null;
            actWard.setText("");
            actWard.setEnabled(false);

            locationApi.getAllWardsInDistrict(selectedDistrict.code, new WardsCallbackHandler());
        });

        actWard.setOnItemClickListener((parent, view, pos, id) -> {
            selectedWard = (Ward) actWard.getAdapter().getItem(pos);
        });
    }

    public boolean isValidAddress() {
        return selectedProvince != null && selectedDistrict != null && selectedWard != null;
    }

    public int getProvinceCode() { return selectedProvince != null ? selectedProvince.code : null; }
    public int getDistrictCode() { return selectedDistrict != null ? selectedDistrict.code : null; }
    public int getWardCode() { return selectedWard != null ? selectedWard.code : null; }
}
