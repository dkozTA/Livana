package com.example.myapplication.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.fragment.app.FragmentActivity;

import com.example.myapplication.ui.fragments.SetAmenitiesFragment;


// cai nay de ben backend test data thoi khong quan trong dau
public class TestActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout container = new FrameLayout(this);
        container.setId(View.generateViewId());
        setContentView(container);

        // Gắn fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(container.getId(), new SetAmenitiesFragment())
                .commitNow();
    }

}
