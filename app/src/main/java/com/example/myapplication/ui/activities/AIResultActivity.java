package com.example.myapplication.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AIResultActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_result);

        textView = findViewById(R.id.textView);

        // Lấy JSON string từ intent extras
        Intent intent = getIntent();
        String jsonString = intent.getStringExtra("property_json");

        if (jsonString != null && !jsonString.isEmpty()) {
            Gson gson = new Gson();
            PropertyResponse response = gson.fromJson(jsonString, PropertyResponse.class);

            String formattedText = formatPropertyData(response);
            textView.setText(formattedText);
        } else {
            textView.setText("No data received.");
        }
    }

    private String formatPropertyData(PropertyResponse response) {
        StringBuilder sb = new StringBuilder();

        for (Property property : response.properties) {
            sb.append("🆔 Property ID: ").append(property.id).append("\n");
            sb.append("📊 Score: ").append(property.score).append("\n\n");

            sb.append("✔️ Matches:\n");
            for (Match match : property.matches) {
                sb.append("- Request: ").append(match.requestSent).append("\n");
                sb.append("  Matched: ").append(match.descSent).append("\n");
                sb.append("  From: ").append(match.from).append("\n");
                sb.append("  Confidence: ").append(match.confidence).append("\n\n");
            }

            sb.append("❌ Contradictions:\n");
            for (Contradiction contra : property.contradictions) {
                sb.append("- Request: ").append(contra.requestSent).append("\n");
                sb.append("  Contradicted: ").append(contra.descSent).append("\n");
                sb.append("  From: ").append(contra.from).append("\n");
                sb.append("  Confidence: ").append(contra.confidence).append("\n\n");
            }

            sb.append("────────────────────\n\n");
        }

        return sb.toString();
    }

    // Model classes
    public static class PropertyResponse {
        public List<Property> properties;
    }

    public static class Property {
        public String id;
        public double score;
        public List<Match> matches;
        public List<Contradiction> contradictions;
    }

    public static class Match {
        @SerializedName("request_sent")
        public String requestSent;

        @SerializedName("desc_sent")
        public String descSent;

        public String from;
        public double confidence;
    }

    public static class Contradiction {
        @SerializedName("request_sent")
        public String requestSent;

        @SerializedName("desc_sent")
        public String descSent;

        public String from;
        public double confidence;
    }
}
