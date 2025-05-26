package com.example.myapplication.ui.fragments.host;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import com.example.myapplication.R;
import com.example.myapplication.data.Model.Booking.Booking;
import com.example.myapplication.data.Repository.Booking.BookingRepository;
import com.example.myapplication.ui.activities.IncomeOverviewActivity;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.auth.FirebaseAuth;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class StatisticFragment extends Fragment {

    private List<Booking> bookingList = new ArrayList<>();

    private BookingRepository bookingRepository;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_host_statistic, container, false);
        TextView total_income = view.findViewById(R.id.total_income);
        BarChart barChart = view.findViewById(R.id.barChart);



        String hostId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d("userId", hostId);


        bookingRepository = new BookingRepository(requireContext());
        bookingRepository.getAllBookingsByHostID(hostId,
                bookings -> {
                    Map<String, Double> monthlyTotal = new HashMap<>();

                    double currentMonthActual = 0;
                    double currentMonthForecast = 0;

                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                    Calendar today = Calendar.getInstance();
                    int currentYear = today.get(Calendar.YEAR);
                    int currentMonth = today.get(Calendar.MONTH) + 1; // Tháng bắt đầu từ 0


                    for (Booking booking : bookings) {
                        try {
                            Calendar checkIn = Calendar.getInstance();
                            Calendar checkOut = Calendar.getInstance();

                            checkIn.setTime(sdf.parse(booking.check_in_day));
                            checkOut.setTime(sdf.parse(booking.check_out_day));


                            int year = checkOut.get(Calendar.YEAR);
                            int month = checkOut.get(Calendar.MONTH) + 1;

                            String key = String.format("%04d-%02d", year, month);

                            if (year == currentYear && month == currentMonth) {
                                // Tháng hiện tại
                                if (checkOut.before(today)) {
                                    currentMonthActual += booking.total_price;
                                } else {
                                    currentMonthForecast += booking.total_price;
                                }
                                monthlyTotal.put(key, currentMonthActual + currentMonthForecast);
                            } else {
                                double oldTotal = monthlyTotal.getOrDefault(key, 0.0);
                                monthlyTotal.put(key, oldTotal + booking.total_price);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    // Tổng thu nhập tháng hiện tại (gồm thực tế + dự kiến)
                    double total = currentMonthActual + currentMonthForecast;

                    // Format số tiền có dấu phẩy (vd: 1,000,000 đ)
                    DecimalFormat df = new DecimalFormat("#,###");
                    String formatted = df.format(total);
                    total_income.setText(formatted + " đ trong tháng này");

                    // Tìm min và max tháng
                    String minMonth = null, maxMonth = null;
                    for (String key : monthlyTotal.keySet()) {
                        if (minMonth == null || key.compareTo(minMonth) < 0) {
                            minMonth = key;
                        }
                        if (maxMonth == null || key.compareTo(maxMonth) > 0) {
                            maxMonth = key;
                        }
                    }

                    // Tạo đầy đủ các tháng từ min đến max
                    if (minMonth != null && maxMonth != null) {
                        try {
                            SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM", Locale.getDefault());
                            Calendar minCal = Calendar.getInstance();
                            Calendar maxCal = Calendar.getInstance();

                            minCal.setTime(monthFormat.parse(minMonth));
                            maxCal.setTime(monthFormat.parse(maxMonth));

                            while (!minCal.after(maxCal)) {
                                String key = monthFormat.format(minCal.getTime());
                                if (!monthlyTotal.containsKey(key)) {
                                    monthlyTotal.put(key, 0.0);  // Thêm tháng không có dữ liệu
                                }
                                minCal.add(Calendar.MONTH, 1);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    Map<String, Double> sortedMonthlyTotal = new LinkedHashMap<>();

                    monthlyTotal.entrySet().stream()
                            .sorted(Map.Entry.comparingByKey()) // So sánh theo key: "yyyy-MM"
                            .forEachOrdered(entry -> sortedMonthlyTotal.put(entry.getKey(), entry.getValue()));

                    int size = sortedMonthlyTotal.size();
                    int fromIndex = Math.max(0, size - 5);

                    List<Map.Entry<String, Double>> lastFiveEntries = new ArrayList<>(
                            new ArrayList<>(sortedMonthlyTotal.entrySet()).subList(fromIndex, size)
                    );

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
                    xAxis.setDrawLabels(false); // Ẩn nhãn trục X
                    xAxis.setDrawGridLines(false); // (tuỳ chọn) ẩn lưới trục X
                    xAxis.setDrawAxisLine(false); // (tuỳ chọn) ẩn đường trục X

                    // Chỉ hiện trục Y bên trái
                    barChart.getAxisRight().setEnabled(false);
                    barChart.getAxisLeft().setEnabled(false);

                    // Giao diện
                    dataSet.setColor(Color.parseColor("#E6005C"));
                    dataSet.setValueTextSize(12f);
                    dataSet.setDrawValues(false); // Ẩn số liệu trên cột

                    barChart.getLegend().setEnabled(false); // Ẩn chú thích "Doanh thu theo tháng"
                    barChart.getDescription().setEnabled(false); // Ẩn mô tả mặc định
                    barChart.animateY(1000);

                    barChart.invalidate();



                    // Gán sự kiện click sau khi đã có dữ liệu
                    CardView incomeCard = getView().findViewById(R.id.income_card);
                    double finalCurrentMonthActual = currentMonthActual;
                    double finalCurrentMonthForecast = currentMonthForecast;
                    incomeCard.setOnClickListener(v -> {
                        Intent intent = new Intent(getActivity(), IncomeOverviewActivity.class);
                        intent.putExtra("bookings", new ArrayList<>(bookings));
                        intent.putExtra("total_income", total);
                        intent.putExtra("current_month_actual", finalCurrentMonthActual);
                        intent.putExtra("current_month_forecast", finalCurrentMonthForecast);
//                        intent.putExtra("monthly_income_map", new HashMap<>(monthlyTotal));
                        intent.putExtra("monthly_income_map", new HashMap<>(sortedMonthlyTotal));
                        startActivity(intent);
                    });

                    barChart.setOnClickListener(v -> {
                        Intent intent = new Intent(getActivity(), IncomeOverviewActivity.class);
                        intent.putExtra("total_income", total);
                        intent.putExtra("current_month_actual", finalCurrentMonthActual);
                        intent.putExtra("current_month_forecast", finalCurrentMonthForecast);
//                        intent.putExtra("monthly_income_map", new HashMap<>(monthlyTotal));
                        intent.putExtra("monthly_income_map", new HashMap<>(sortedMonthlyTotal));
                        startActivity(intent);
                    });
                },
                e -> {
                    Toast.makeText(requireContext(), "Lỗi lấy dữ liệu", Toast.LENGTH_SHORT).show();
                });


        return view;
    }
}
