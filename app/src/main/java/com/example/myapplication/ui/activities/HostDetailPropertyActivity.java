package com.example.myapplication.ui.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.data.Model.Property.Property;
import com.example.myapplication.data.Repository.Property.PropertyRepository;
import com.example.myapplication.ui.adapters.LinkingIDAdapter;
import com.example.myapplication.utils.PostConverter;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class HostDetailPropertyActivity extends AppCompatActivity {
    private Property propertyData;

    private ImageView[] images;
    private TextView nameProperty;
    private TextView detailProperty;
    private TextView roomId;
    private ImageButton copyIDButton;
    private Button updateButton;
    private Button newLinkButton;

    private RecyclerView linkingRecycler;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_detail_property);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("property_json")) {
            String json = intent.getStringExtra("property_json");

            if (json != null && !json.isEmpty()) {
                try {
                    Property property = new Gson().fromJson(json, Property.class);

                    if (property != null) {
                        propertyData = property;
                        Log.d("IntentCheck", "Nhận property thành công: " + property.name);
                    } else {
                        Log.e("IntentCheck", "Không thể parse JSON thành Property");
                    }

                } catch (JsonSyntaxException e) {
                    Log.e("IntentCheck", "Lỗi parse JSON: " + e.getMessage());
                }
            } else {
                Log.w("IntentCheck", "JSON rỗng hoặc null");
            }
        } else {
            Log.w("IntentCheck", "Không nhận được extra 'property_json'");
        }

        if (propertyData == null) {
            finish();
            return;
        }

        images = new ImageView[]{
                findViewById(R.id.image6),
                findViewById(R.id.image4),
                findViewById(R.id.image5)
        };

        nameProperty = findViewById(R.id.nameProperty);
        detailProperty = findViewById(R.id.typeProperty);

        linkingRecycler = findViewById(R.id.linkingRecycler);
        linkingRecycler.setLayoutManager(new LinearLayoutManager(this));

        roomId = findViewById(R.id.textId);
        copyIDButton = findViewById(R.id.copyButton);
        copyIDButton.setOnClickListener(v -> {
            ClipboardManager clipboardManager = ((ClipboardManager) getSystemService(CLIPBOARD_SERVICE));
            ClipData clipData = ClipData.newPlainText("label", propertyData.id);

            clipboardManager.setPrimaryClip(clipData);
            Toast.makeText(this, "Sao chép thành công", Toast.LENGTH_SHORT).show();
        });

        FetchLinking(propertyData);
        UpdateDisplay(propertyData);

        updateButton = findViewById(R.id.updateButton);
        newLinkButton = findViewById(R.id.addLinkButton);

        updateButton.setOnClickListener(v -> {
            Gson gson = new Gson();
            String json = gson.toJson(propertyData);

            Intent createIntent = new Intent(this, CreatePropertyActivity.class);
            createIntent.putExtra("property_json", json);

            startActivity(createIntent);
        });

        newLinkButton.setOnClickListener(v -> showLinkDialog());

        ImageButton backBtn = findViewById(R.id.btnBack);
        backBtn.setOnClickListener(v -> finish());
    }

    private void UpdateDisplay(Property property) {
        Glide.with(this)
                .load(property.getMainPhoto()) // nếu là URL, nên đổi tên hàm thành getImageUrl()
                .placeholder(R.drawable.photo1)
                .error(R.drawable.photo1)
                .into(images[0]);

        if (!property.getSub_photos().isEmpty()) {
            Glide.with(this)
                    .load(property.getSub_photos().get(0)) // nếu là URL, nên đổi tên hàm thành getImageUrl()
                    .placeholder(R.drawable.photo1)
                    .error(R.drawable.photo1)
                    .into(images[1]);
        }

        if (property.getSub_photos().size() >= 2) {
            Glide.with(this)
                    .load(property.getSub_photos().get(1)) // nếu là URL, nên đổi tên hàm thành getImageUrl()
                    .placeholder(R.drawable.photo1)
                    .error(R.drawable.photo1)
                    .into(images[2]);
        }

        nameProperty.setText(property.name);
        detailProperty.setText(PostConverter.convertPropertyToPost(property).getDetail());

        roomId.setText(String.format("ID: %s", property.id));
    }

    private void FetchProperty() {
        PropertyRepository repo = new PropertyRepository(this);

        repo.getPropertyById(propertyData.id, unused -> {
            propertyData = unused;
            FetchLinking(propertyData);
        }, e -> {
            Toast.makeText(this, "Fetch Data failed", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void FetchLinking(Property property) {
        LinkingIDAdapter adapter = new LinkingIDAdapter(property.links, null);
        linkingRecycler.setAdapter(adapter);
    }

    // Gọi hàm này trong activity hoặc fragment khi cần hiện dialog
    private void showLinkDialog() {
        // Tạo dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Inflate layout từ file XML
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_field, null);

        // Gắn layout vào dialog
        builder.setView(dialogView);

        // Tạo AlertDialog từ builder
        AlertDialog alertDialog = builder.create();

        // Tìm các view trong dialog để xử lý sự kiện
        EditText editText = dialogView.findViewById(R.id.dialog_input);
        Button btnCancel = dialogView.findViewById(R.id.btn_cancel);
        Button btnSave = dialogView.findViewById(R.id.btn_save);

        // Bắt sự kiện Cancel
        btnCancel.setOnClickListener(v -> alertDialog.dismiss());

        // Bắt sự kiện Save
        btnSave.setOnClickListener(v -> {
            String input = editText.getText().toString().trim();
            btnSave.setEnabled(false);

            // Xử lý giá trị nhập vào ở đây
            PropertyRepository propertyRepo = new PropertyRepository(this);

            propertyRepo.addLinksToBothProperty(propertyData.id, input, unused -> {
               Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show();
               FetchProperty();
               alertDialog.dismiss();
            }, e -> {
                Toast.makeText(this, "Không thành công", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            });

            alertDialog.dismiss();
        });

        // Hiển thị dialog
        alertDialog.show();
    }
}
