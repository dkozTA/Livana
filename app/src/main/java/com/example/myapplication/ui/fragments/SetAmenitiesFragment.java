package com.example.myapplication.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data.Model.Property.AmenityStatus;
import com.example.myapplication.data.Model.Property.Property;
import com.example.myapplication.data.Model.Property.Rooms;
import com.example.myapplication.interfaces.IStepValidator;
import com.example.myapplication.ui.adapters.AmenitySetupAdapter;
import com.example.myapplication.ui.customviews.NumberSelectorView;
import com.example.myapplication.ui.misc.Amenity;
import com.example.myapplication.ui.misc.PropertyViewModel;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class SetAmenitiesFragment extends Fragment implements IStepValidator {
    private PropertyViewModel viewModel;

    NumberSelectorView numBedroom;
    NumberSelectorView maxGuess;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_amentities, container, false);

        numBedroom = view.findViewById(R.id.numBedroom);
        maxGuess = view.findViewById(R.id.maxGuess);

        //Amenity setup
        RecyclerView amenityRecycler = view.findViewById(R.id.amenityRecycler);
        amenityRecycler.setLayoutManager(new LinearLayoutManager(this.getContext()));

        List<Amenity> amenityList = Arrays.asList(
                new Amenity("TV", R.drawable.ic_tv, AmenityStatus.Hidden),
                new Amenity("Wi-Fi", R.drawable.ic_wifi, AmenityStatus.Hidden),
                new Amenity("Thú cưng", R.drawable.ic_pets, AmenityStatus.Hidden),
                new Amenity("Hồ bơi", R.drawable.ic_pool, AmenityStatus.Hidden),
                new Amenity("Máy giặt", R.drawable.ic_bed, AmenityStatus.Hidden),
                new Amenity("Bữa sáng", R.drawable.ic_free_breakfast, AmenityStatus.Hidden),
                new Amenity("Máy lạnh", R.drawable.ic_airconditioner, AmenityStatus.Hidden),
                new Amenity("BBQ", R.drawable.ic_outdoor_grill, AmenityStatus.Hidden)
        );

        AmenitySetupAdapter adapter = new AmenitySetupAdapter(amenityList);
        amenityRecycler.setAdapter(adapter);

        Log.d("Adapter", "" + amenityRecycler.getItemDecorationCount());

        viewModel = new ViewModelProvider(requireActivity()).get(PropertyViewModel.class);

        applyData();
        return view;
    }

    @Override
    public void applyData() {
        Property property = viewModel.getPropertyData().getValue();
        //room apply
        if (property.getRooms() == null) property.rooms = new Rooms(0, AmenityStatus.Hidden, AmenityStatus.Hidden);

        numBedroom.setCount(property.getRooms().bedRooms);
        maxGuess.setCount(property.getMax_guess());

        //amenities


    }

    @Override
    public void save() {

    }

    @Override
    public boolean validate(String warning) {
        return false;
    }

    @Override
    public int getStepIndex() {return 2;}
}
