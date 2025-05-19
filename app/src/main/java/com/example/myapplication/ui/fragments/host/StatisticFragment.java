package com.example.myapplication.ui.fragments.host;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import com.example.myapplication.R;
import com.example.myapplication.ui.activities.IncomeOverviewActivity;

public class StatisticFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_host_statistic, container, false);

        // Bắt CardView "Thu nhập"
        CardView incomeCard = view.findViewById(R.id.income_card);  // ID này bạn cần đặt trong layout
        incomeCard.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), IncomeOverviewActivity.class);
            startActivity(intent);
        });

        return view;
    }
}
