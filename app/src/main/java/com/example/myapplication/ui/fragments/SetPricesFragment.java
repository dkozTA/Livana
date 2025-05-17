package com.example.myapplication.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.R;
import com.example.myapplication.data.Model.Property.Property;
import com.example.myapplication.interfaces.IStepValidator;
import com.example.myapplication.ui.misc.PropertyViewModel;

public class SetPricesFragment extends Fragment implements IStepValidator {

    private PropertyViewModel viewModel;

    private EditText normalPrice;
    private EditText weekendPrice;
    private EditText holidayPrice;
    private EditText deposit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_prices, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(PropertyViewModel.class);

        normalPrice = view.findViewById(R.id.normalPrice);
        weekendPrice = view.findViewById(R.id.weekendPrice);
        holidayPrice = view.findViewById(R.id.holidayPrice);
        deposit = view.findViewById(R.id.deposit);

        applyData();
        return view;
    }

    @Override
    public void applyData() {
        Property property = viewModel.getPropertyData().getValue();

        if (property != null) {
            normalPrice.setText(String.valueOf(property.getNormal_price()));
            weekendPrice.setText(String.valueOf(property.getWeekend_price()));
            holidayPrice.setText(String.valueOf(property.getHoliday_price()));
            deposit.setText(String.valueOf(property.getDeposit()));
        }
    }

    @Override
    public void save() {
        Property newValue = viewModel.getPropertyData().getValue();
        if (newValue == null) newValue = new Property();

        newValue.normal_price = Float.parseFloat(String.valueOf(normalPrice.getText()));
        newValue.weekend_price = Float.parseFloat(String.valueOf(weekendPrice.getText()));
        newValue.holiday_price = Float.parseFloat(String.valueOf(holidayPrice.getText()));
        newValue.deposit = Float.parseFloat(String.valueOf(deposit.getText()));
    }

    @Override
    public boolean validate(String warning) {
        return true;
    }

    @Override
    public int getStepIndex() {
        return 5;
    }
}
