package com.example.myapplication.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.data.Enum.PropertyType;
import com.example.myapplication.ui.adapters.RoomTypeAdapter;

import java.util.Arrays;
import java.util.List;

public class SetPropertyType extends Fragment {

    private RecyclerView recyclerView;
    private RoomTypeAdapter propertyAdapter;
    private List<PropertyType> propertyList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_set_property_type, container, false);

        // Initialize the property list and add some data
        propertyList = Arrays.asList(PropertyType.values());

        // Set up RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize and set adapter
        propertyAdapter = new RoomTypeAdapter(propertyList, v -> {});
        recyclerView.setAdapter(propertyAdapter);

        return view;
    }
}