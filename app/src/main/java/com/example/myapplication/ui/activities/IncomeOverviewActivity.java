package com.example.myapplication.ui.activities;

import static java.security.AccessController.getContext;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.graphics.Color;


import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;
import com.example.myapplication.data.Model.Booking.Booking;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class IncomeOverviewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_overview); // layout bạn đã thiết kế

        TextView total_income = findViewById(R.id.total_income);
        TextView income_forecast = findViewById(R.id.income_forecast);

        double totalIncome = getIntent().getDoubleExtra("total_income", 0.0);
        double currentMonthActual = getIntent().getDoubleExtra("current_month_actual", 0.0);
        double currentMonthForecast = getIntent().getDoubleExtra("current_month_forecast", 0.0);
        List<Booking> transactions = (List<Booking>) getIntent().getSerializableExtra("transactions");

        DecimalFormat df = new DecimalFormat("#,###");

        // Ví dụ hiển thị tổng thu nhập
        String formattedTotalIncome = df.format(totalIncome);
        total_income.setText("₫" + formattedTotalIncome);

        // Nếu muốn hiển thị riêng từng khoản, bạn cần TextView riêng, ví dụ:
        // actualIncomeTextView.setText("Thực tế: ₫" + df.format(currentMonthActual));
        income_forecast.setText("Số tiền ₫" + df.format(currentMonthForecast) + " sắp tới");

        HashMap<String, Double> monthlyTotal =
                (HashMap<String, Double>) getIntent().getSerializableExtra("monthly_income_map");

        Map<String, Double> sortedMonthlyTotal = new LinkedHashMap<>();
        monthlyTotal.entrySet().stream()
                .sorted(Map.Entry.comparingByKey()) // hoặc .comparingByKey(Comparator.reverseOrder()) nếu muốn ngược
                .forEachOrdered(entry -> sortedMonthlyTotal.put(entry.getKey(), entry.getValue()));

        int size = sortedMonthlyTotal.size();
        int fromIndex = Math.max(0, size - 5);

        List<Map.Entry<String, Double>> lastFiveEntries = new ArrayList<>(
                new ArrayList<>(sortedMonthlyTotal.entrySet()).subList(fromIndex, size)
        );


        BarChart barChart = findViewById(R.id.barChart);
        List<BarEntry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        int index = 0;
        for (Map.Entry<String, Double> entry : lastFiveEntries) {
            entries.add(new BarEntry(index, entry.getValue().floatValue()));

            String key = entry.getKey(); // ví dụ "2023-05"
            String[] parts = key.split("-");
            String label = parts[1] + "-" + parts[0]; // "05-2023"
            labels.add(label);

            index++;
        }


        BarDataSet dataSet = new BarDataSet(entries, "Doanh thu theo tháng");
        BarData barData = new BarData(dataSet);
        barChart.setData(barData);

        // Cấu hình trục X
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        // Chỉ hiện trục Y bên trái
        barChart.getAxisRight().setEnabled(false);  // Ẩn trục Y bên phải
        barChart.getAxisLeft().setEnabled(false);

        // Cải thiện giao diện
        dataSet.setColor(Color.parseColor("#E6005C"));
        dataSet.setValueTextSize(12f);
        barChart.getDescription().setEnabled(false); // Ẩn mô tả mặc định
        barChart.animateY(1000); // Thêm hiệu ứng

        barChart.invalidate();  // Refresh

        ImageButton backButton = findViewById(R.id.btnBack);
        backButton.setOnClickListener(v -> finish());

        ImageButton closeButton = findViewById(R.id.btnClose);
        closeButton.setOnClickListener(v -> finish());
    }
}
