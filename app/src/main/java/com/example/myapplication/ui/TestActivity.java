package com.example.myapplication.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.data.Model.Property.SearchProperty;
import com.example.myapplication.data.Model.Search.SearchField;
import com.example.myapplication.data.Model.Search.SearchResponse;
import com.example.myapplication.data.Repository.Search.PropertyAPIClient;

import java.util.List;

public class TestActivity extends AppCompatActivity {
    private static final String TAG = "SearchTestActivity";

    private EditText etSearchQuery;
    private Button btnSearch;
    private TextView tvResults;
    private PropertyAPIClient apiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_test);

        // Khởi tạo các view
        etSearchQuery = findViewById(R.id.etSearchQuery);
        btnSearch = findViewById(R.id.btnSearch);
        tvResults = findViewById(R.id.tvResults);

        // Khởi tạo API client
        apiClient = new PropertyAPIClient();

        // Thiết lập sự kiện click cho nút tìm kiếm
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSearch();
            }
        });
    }

    private void performSearch() {
        String query = etSearchQuery.getText().toString().trim();
        if (query.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập từ khóa tìm kiếm", Toast.LENGTH_SHORT).show();
            return;
        }

        // Hiển thị trạng thái đang tìm kiếm
        tvResults.setText("Đang tìm kiếm...");

        // Tạo đối tượng tìm kiếm với Builder pattern
        SearchField searchField = new SearchField.Builder()
                .propertyName(query)  // Tìm theo tên property
                .cityCode(26)
                .pagination(0, 10)    // Lấy trang đầu tiên với 10 kết quả
                .build();

        // Gọi API tìm kiếm
        apiClient.searchProperties(searchField, new PropertyAPIClient.OnPropertyCallback() {
            @Override
            public void onSuccess(SearchResponse response) {
                if (response.isSuccess() && response.getResults() != null) {
                    // Lấy danh sách kết quả
                    List<SearchProperty> properties = response.getResults().getHits();

                    // Hiển thị kết quả
                    displayResults(properties);
                } else {
                    runOnUiThread(() -> {
                        tvResults.setText("Không tìm thấy kết quả hoặc có lỗi xảy ra");
                    });
                }
            }

            @Override
            public void onError(String errorMessage) {
                Log.e(TAG, "Search error: " + errorMessage);
                runOnUiThread(() -> {
                    tvResults.setText("Lỗi: " + errorMessage);
                });
            }
        });
    }

    private void displayResults(List<SearchProperty> properties) {
        runOnUiThread(() -> {
            if (properties == null || properties.isEmpty()) {
                tvResults.setText("Không tìm thấy kết quả nào");
                return;
            }

            StringBuilder result = new StringBuilder();
            result.append("Tìm thấy ").append(properties.size()).append(" kết quả:\n\n");

            for (SearchProperty property : properties) {
                result.append("ObjectID: ").append(property.getObjectID()).append("\n");
                result.append("Tên: ").append(property.getPropertyName()).append("\n");
                result.append("Giá: ").append(property.getPrice()).append("\n");
                result.append("Số phòng ngủ: ").append(property.getBed_rooms()).append("\n");
                result.append("Số khách tối đa: ").append(property.getMax_guest()).append("\n");
                result.append("---------------------------------------\n");
            }

            tvResults.setText(result.toString());
        });
    }
}