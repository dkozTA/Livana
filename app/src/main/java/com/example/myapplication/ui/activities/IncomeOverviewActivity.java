package com.example.myapplication.ui.activities;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.example.myapplication.data.Enum.Booking_status;


public class IncomeOverviewActivity extends AppCompatActivity {
    private boolean isExpanded = false;
    private boolean isCompletedExpanded = false;

    List<Booking> completedBookings = new ArrayList<>();
    List<Booking> upcomingBookings = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_overview); // layout bạn đã thiết kế

        TextView total_income = findViewById(R.id.total_income);
        TextView income_forecast = findViewById(R.id.income_forecast);
        Button upcomingButton = findViewById(R.id.btn_upcoming_summary);
        LinearLayout upcomingBookingList = findViewById(R.id.upcoming_summary);
        Button completedButton = findViewById(R.id.btn_completed_summary);
        LinearLayout completedBookingList = findViewById(R.id.completed_summary);

        double totalIncome = getIntent().getDoubleExtra("total_income", 0.0);
        double currentMonthForecast = getIntent().getDoubleExtra("current_month_forecast", 0.0);
        List<Booking> bookings = (List<Booking>) getIntent().getSerializableExtra("bookings");

        // ✅ Sắp xếp theo ngày check-in (định dạng dd-MM-yyyy)
        if (bookings != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Collections.sort(bookings, (b1, b2) -> {
                try {
                    Date d1 = sdf.parse(b1.check_out_day);
                    Date d2 = sdf.parse(b2.check_out_day);
                    return d1.compareTo(d2); // tăng dần. Đổi thành d2.compareTo(d1) nếu muốn giảm dần.
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0;
                }
            });

            Log.d("bookings_size", bookings.size() + "");
        } else {
            Log.e("transactions", "transactions is null");
        }


        for (Booking booking : bookings) {
            if (booking.status == Booking_status.COMPLETED || booking.status == Booking_status.REVIEWED) {
                completedBookings.add(booking);
            } else if (booking.status == Booking_status.ACCEPTED) {
                upcomingBookings.add(booking);
            }
        }

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

        // hien cac giao dich
        loadupcomingBookings(upcomingBookingList, upcomingBookings, 3);

        upcomingButton.setOnClickListener(v -> {
            upcomingBookingList.removeAllViews();
            if (isExpanded) {
                loadupcomingBookings(upcomingBookingList, upcomingBookings, 3);
                upcomingButton.setText("Xem tất cả giao dịch sắp tới");
            } else {
                loadupcomingBookings(upcomingBookingList, upcomingBookings, upcomingBookings.size());
                upcomingButton.setText("Ẩn bớt các giao dịch");
            }
            isExpanded = !isExpanded;
        });

        Collections.reverse(completedBookings);

        loadCompletedBookings(completedBookingList, completedBookings, 3);

        completedButton.setOnClickListener(v -> {
            completedBookingList.removeAllViews();
            if (isCompletedExpanded) {
                loadCompletedBookings(completedBookingList, completedBookings, 3);
                completedButton.setText("Xem tất cả các khoản đã thanh toán");
            } else {
                loadCompletedBookings(completedBookingList, completedBookings, completedBookings.size());
                completedButton.setText("Ẩn bớt các khoản đã thanh toán");
            }
            isCompletedExpanded = !isCompletedExpanded;
        });
    }

    //Ham hien thi cac giao dich trong container
    private void loadupcomingBookings(LinearLayout container, List<Booking> bookings, int count) {
        LayoutInflater inflater = LayoutInflater.from(this);
        container.removeAllViews();

        for (int i = 0; i < Math.min(count, bookings.size()); i++) {
            Booking t = bookings.get(i);

            View itemView = inflater.inflate(R.layout.item_upcoming_income, container, false);

            TextView date = itemView.findViewById(R.id.income_date);
            TextView income = itemView.findViewById(R.id.income_amount);
            ImageView icon = itemView.findViewById(R.id.button_explore);

            date.setText(t.check_out_day);
            DecimalFormat df = new DecimalFormat("#,###");
            String totalIncome = df.format(t.total_price);
            income.setText(totalIncome);

            container.addView(itemView);

            // Divider (trừ item cuối cùng)
            if (i < count - 1) {
                View divider = new View(this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, 1);
                divider.setLayoutParams(lp);
                divider.setBackgroundColor(Color.parseColor("#E0E0E0"));
                container.addView(divider);
            }
        }
    }

    private void loadCompletedBookings(LinearLayout container, List<Booking> bookings, int count) {
        LayoutInflater inflater = LayoutInflater.from(this);
        container.removeAllViews();

        for (int i = 0; i < Math.min(count, bookings.size()); i++) {
            Booking t = bookings.get(i);

            View itemView = inflater.inflate(R.layout.item_completed_income, container, false);

            TextView date = itemView.findViewById(R.id.completed_date);
            TextView income = itemView.findViewById(R.id.completed_amount);
            ImageView icon = itemView.findViewById(R.id.button_explore);

            date.setText(t.check_out_day);
            DecimalFormat df = new DecimalFormat("#,###");
            String totalIncome = df.format(t.total_price);
            income.setText(totalIncome);

            container.addView(itemView);

            // Divider (trừ item cuối cùng)
            if (i < count - 1) {
                View divider = new View(this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, 1);
                divider.setLayoutParams(lp);
                divider.setBackgroundColor(Color.parseColor("#E0E0E0"));
                container.addView(divider);
            }
        }
    }
}
