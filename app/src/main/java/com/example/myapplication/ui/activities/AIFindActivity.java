package com.example.myapplication.ui.activities;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

import org.json.*;

import java.io.*;
import java.net.*;

public class AIFindActivity extends AppCompatActivity {

    EditText editTextRequest;
    Button btnSubmit;
    ImageButton backButton;
    LinearLayout resultContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_find);

        editTextRequest = findViewById(R.id.editTextRequest);
        btnSubmit = findViewById(R.id.btnSubmit);
        //resultContainer = findViewById(R.id.resultContainer);


        btnSubmit.setOnClickListener(view -> {
            String userInput = editTextRequest.getText().toString().trim();
            if (!userInput.isEmpty()) {
                sendRequestToModel(userInput);
            } else {
                Toast.makeText(this, "Vui lòng nhập nội dung", Toast.LENGTH_SHORT).show();
            }
        });

        backButton = findViewById(R.id.back);
        backButton.setOnClickListener(v -> finish());
    }

    private void sendRequestToModel(String userInput) {
        // Dùng Thread riêng để tránh block UI
        new Thread(() -> {
            try {
                URL url = new URL("http://10.0.2.2:8000/extract_booking");  // Địa chỉ localhost từ Android Emulator
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                // JSON gửi đi
                JSONObject jsonRequest = new JSONObject();
                jsonRequest.put("text", userInput);

                OutputStream os = conn.getOutputStream();
                os.write(jsonRequest.toString().getBytes());
                os.flush();
                os.close();

                // Đọc phản hồi
                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream is = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();
                    is.close();

                    parseAndDisplayResponse(response.toString());
                } else {
                    Toast.makeText(this, "Lỗi kết nối: " , Toast.LENGTH_SHORT).show();
                }

                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
                //showError("Lỗi: " + e.getMessage());
            }
        }).start();
    }

    private void parseAndDisplayResponse(String json) {
        try {
            JSONObject root = new JSONObject(json);
            JSONObject info = root.getJSONObject("extracted_info");

            String hasRoomType = info.optString("Room_type", null);
            String numBedroom = info.optString("Num_bedrooms", null);
            boolean hasKitchen = info.optBoolean("Kitchen", false);
            boolean hasLivingRoom = info.optBoolean("Living_room", false);
            JSONArray amenitiesArray = info.optJSONArray("Amenities");
            String price = info.optString("Price", null);

            // Chạy UI thread để cập nhật giao diện
            runOnUiThread(() -> {
                resultContainer.removeAllViews(); // Xóa kết quả cũ

                if (hasRoomType != null && !hasRoomType.equals("null")) {
                    addResultItem(String.format("Loại phòng: %s", hasRoomType));
                }

                if (numBedroom != null && !numBedroom.equals("null")) {
                    addResultItem(String.format("Cần: %s phòng ngủ", numBedroom));
                }

                if (hasKitchen)
                    addResultItem("Yêu cầu có bếp");

                if (hasLivingRoom)
                    addResultItem("Yêu cầu có phòng khách");

                if (amenitiesArray != null) {
                    for (int i = 0; i < amenitiesArray.length(); i++) {
                        try {
                            String amenity = amenitiesArray.getString(i);
                            addResultItem("Tiện ích: " + amenity);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                if (price != null && !price.equals("null")) {
                    addResultItem(String.format("Giá: %s", price));
                }

                if (!hasKitchen && !hasLivingRoom && (amenitiesArray == null || amenitiesArray.length() == 0)) {
                    addResultItem("Không có thông tin rõ ràng");
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addResultItem(String text) {
        TextView tv = new TextView(this);
        tv.setText("• " + text);
        tv.setTextSize(22);
        tv.setTextColor(getResources().getColor(android.R.color.black));
        tv.setPadding(40, 30, 0, 8);
        resultContainer.addView(tv);
    }
}
