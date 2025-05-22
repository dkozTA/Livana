package com.example.myapplication.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data.Model.Property.Property;
import com.example.myapplication.data.Repository.Property.PropertyRepository;
import com.example.myapplication.ui.adapters.PostAdapter;
import com.example.myapplication.ui.misc.Post;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.*;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class AIFindActivity extends AppCompatActivity {

    EditText editTextRequest;
    Button btnSubmit;
    ImageButton backButton;
    RecyclerView resultContainer;

    Button detailButton;

    // Store property data from backend
    private PropertyRepository propertyRepository;
    // List to hold UI post items
    private List<Post> postList;
    // Adapter to display posts in RecyclerView
    private PostAdapter postAdapter;

    private List<Post> fullPostList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_find);

        editTextRequest = findViewById(R.id.editTextRequest);
        btnSubmit = findViewById(R.id.btnSubmit);
        resultContainer = findViewById(R.id.recyclerAI);
        detailButton = findViewById(R.id.detailButton);
        resultContainer.setLayoutManager(new LinearLayoutManager(this));


        btnSubmit.setOnClickListener(view -> {
            String userInput = editTextRequest.getText().toString().trim();
            if (!userInput.isEmpty()) {
                sendRequestToModel(userInput);
                editTextRequest.setEnabled(false);
                btnSubmit.setEnabled(false);
            } else {
                Toast.makeText(this, "Vui lòng nhập nội dung", Toast.LENGTH_SHORT).show();
            }
        });

        backButton = findViewById(R.id.back);
        backButton.setOnClickListener(v -> finish());

        // Initialize empty list and adapter
        postList = new ArrayList<>();
        fullPostList = new ArrayList<>();
        postAdapter = new PostAdapter(this, postList, false);
        resultContainer.setAdapter(postAdapter);

        // Create repository instance to interact with Firebase
        propertyRepository = new PropertyRepository(this);
        fetchBackendData();

        resultContainer.setVisibility(View.GONE);
        detailButton.setVisibility(View.GONE);
    }

    private void sendRequestToModel(String userInput) {
        new Thread(() -> {
            HttpURLConnection conn = null;
            try {
                URL url = new URL("http://10.0.2.2:8000/extract_booking");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);
                conn.setConnectTimeout(50000); // 10s timeout kết nối
                conn.setReadTimeout(50000);    // 10s timeout đọc dữ liệu

                // Gửi dữ liệu JSON
                JSONObject jsonRequest = new JSONObject();
                jsonRequest.put("text", userInput);

                OutputStream os = conn.getOutputStream();
                os.write(jsonRequest.toString().getBytes());
                os.flush();
                os.close();

                // Nhận phản hồi
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

                    // Chuyển về UI Thread để hiển thị Toast
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Kết nối thành công", Toast.LENGTH_SHORT).show();
                        parseAndDisplayResponse(response.toString());
                    });
                } else {
                    runOnUiThread(() -> {
                        editTextRequest.setEnabled(true);
                        btnSubmit.setEnabled(true);
                        resultContainer.setVisibility(View.GONE);
                        detailButton.setVisibility(View.GONE);
                        Toast.makeText(this, "Lỗi server: " + responseCode, Toast.LENGTH_SHORT).show();
                    });
                }
            } catch (SocketTimeoutException e) {
                runOnUiThread(() -> Toast.makeText(this, "Mất kết nối: Timeout", Toast.LENGTH_SHORT).show());
            } catch (IOException e) {
                runOnUiThread(() -> Toast.makeText(this, "Lỗi mạng: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            } catch (JSONException e) {
                runOnUiThread(() -> Toast.makeText(this, "Lỗi dữ liệu JSON", Toast.LENGTH_SHORT).show());
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Lỗi không xác định", Toast.LENGTH_SHORT).show());
            } finally {
                if (conn != null) {
                    conn.disconnect(); // Đảm bảo đóng kết nối
                }
            }
        }).start();
    }

    private void parseAndDisplayResponse(String json) {
        editTextRequest.setEnabled(true);
        btnSubmit.setEnabled(true);

        detailButton.setOnClickListener(v -> {
            NavResult(json);
        });

        detailButton.setVisibility(View.VISIBLE);
        resultContainer.setVisibility(View.VISIBLE);
    }

    private void NavResult(String result) {
        Intent intent = new Intent(this, AIResultActivity.class);
        intent.putExtra("property_json", result);
        startActivity(intent);
    }

    private void addResultItem(String text) {
        TextView tv = new TextView(this);
        tv.setText("• " + text);
        tv.setTextSize(22);
        tv.setTextColor(getResources().getColor(android.R.color.black));
        tv.setPadding(40, 30, 0, 8);
        resultContainer.addView(tv);
    }

    private void fetchBackendData() {
        // Call repository method to get all properties from Firestore
        propertyRepository.getAllProperties(
                // Success callback - receives List<Property> from Firebase
                new OnSuccessListener<List<Property>>() {
                    @Override
                    public void onSuccess(List<Property> properties) {
                        // Clear existing posts
                        postList.clear();
                        fullPostList.clear();

                        // Convert each Property to Post
                        for (Property property : properties) {
                            // Format price to display with $ symbol
                            String formattedPrice = "₫" + String.format("%,.0f", property.getNormal_price()) + " cho 1 đêm";

                            // Handle null address case
                            String title = property.address.getDetailAddress() != null ?
                                    property.address.getDetailAddress() : "No location";

                            String propertyType = property.property_type.toString();
                            int maxGuest = property.max_guess;
                            int bedRooms = property.rooms.bedRooms;
                           /*
                            String livingRoomStatus = property.rooms.livingRooms.toString();
                            String kitchenStatus = property.rooms.kitchen.toString();

                            // Nếu có phòng khách, chỉ ghi "· living room"
                            String livingRoomText = "";
                            if ("available".equalsIgnoreCase(livingRoomStatus)) {
                                livingRoomText = " · phòng khách";
                            }

                            // Nếu có phòng khách, chỉ ghi "· living room"
                            String kitchenText = "";
                            if ("available".equalsIgnoreCase(kitchenStatus)) {
                                livingRoomText = " · phòng bếp";
                            }
                            */

                            // Ghép chuỗi mô tả chi tiết
                            //String detail = propertyType + " · " + maxGuest + " khách" + " · " + bedRooms + " phòng ngủ" + livingRoomText + kitchenText;

                            String detail = "Để tạm ở đây cho đỡ lỗi thôi bro, nhớ sửa lại nhé, living room và kitchen sẽ là int nhé ông bạn";
                            // Create new Post object with property data
                            Post post = new Post(
                                    property.getId(),
                                    property.getHost_id(),
                                    title,                    // title
                                    property.getMainPhoto(),               // placeholder image
                                    property.name,                        // address string
                                    detail, // property type as detail
                                    "1.200 km",                          // no distance available
                                    "Available now",                 // placeholder date range
                                    formattedPrice,                 // formatted price
                                    property.total_reviews,
                                    property.avg_ratings,
                                    property.amenities,
                                    property.sub_photos
                            );
//                            Log.d("ExploreFragment", "Creating post with ID: " + property.getId() +
//                                    ", Title: " + title);

                            postList.add(post);
                            fullPostList.add(post);
                        }
                        // Notify adapter to refresh RecyclerView
                        postAdapter.notifyDataSetChanged();
                    }
                },
                // Failure callback - shows error toast
                new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                    }
                }
        );
    }
}
