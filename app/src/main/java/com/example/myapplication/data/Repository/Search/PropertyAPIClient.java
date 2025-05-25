package com.example.myapplication.data.Repository.Search;

import android.util.Log;

import com.example.myapplication.data.Model.Property.SearchProperty;
import com.example.myapplication.data.Model.Search.SearchField;
import com.example.myapplication.data.Model.Search.SearchResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PropertyAPIClient {
    private static final String BASE_URL = "http://10.0.2.2:5000/"; // Thay đổi URL server của bạn
    private PropertyAPIService apiService;

    public interface OnPropertyCallback {
        void onSuccess(SearchResponse response);
        void onError(String errorMessage);
    }

    public PropertyAPIClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(PropertyAPIService.class);
    }

    public void addPropertyToAlgolia(SearchProperty property, final OnPropertyCallback callback) {
        apiService.addPropertyToAlgolia(property).enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Failed to add property: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    public void getPropertyById(String propertyId, final OnPropertyCallback callback) {
        apiService.getPropertyById(propertyId).enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Failed to get property: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    /**
     * Xóa một property từ Algolia theo ID
     * @param propertyId ID của property cần xóa
     * @param callback Interface callback để xử lý kết quả
     */
    public void deletePropertyById(String propertyId, final OnPropertyCallback callback) {
        apiService.deletePropertyById(propertyId).enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Failed to delete property: " + response.message());
                }
            }
            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    /**
     * Tìm kiếm các property theo tiêu chí
     * @param searchField Đối tượng chứa các tiêu chí tìm kiếm
     * @param callback Interface callback để xử lý kết quả
     */
    public void searchProperties(SearchField searchField, final OnPropertyCallback callback) {
        apiService.searchProperties(searchField).enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Failed to search properties: " + response.message());
                }
            }
            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }
}