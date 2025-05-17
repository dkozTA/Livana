package com.example.myapplication.ui.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.myapplication.R;
import com.example.myapplication.interfaces.IStepValidator;
import com.example.myapplication.ui.misc.PropertyViewModel;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class CreatePropertyActivity extends AppCompatActivity {
    private static final int TOTAL_STEP = 5;
    private PropertyViewModel viewModel;
    private NavHostFragment navigator;
    private NavController navController;

    MaterialButton nextButton;
    MaterialButton prevButton;

    private int stepIndex = 0;

    private final int[] stepNextTrans = {
            R.id.t01,
            R.id.t12,
            R.id.t23,
            R.id.t34
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_property);

        viewModel = new ViewModelProvider(this).get(PropertyViewModel.class);

        navigator = (NavHostFragment)
                getSupportFragmentManager().findFragmentById(R.id.stepNavigator);

        if(navigator != null) navController = navigator.getNavController();


        nextButton = findViewById(R.id.nextButton);
        prevButton = findViewById(R.id.prevButton);

        nextButton.setOnClickListener(v -> NextStep());
        prevButton.setOnClickListener(v -> PrevStep());
    }

    private void NextStep() {
        Fragment current = navigator.getChildFragmentManager().getFragments().get(0);
        if (stepIndex >= TOTAL_STEP - 1) {
            nextButton.setText("Hoàn thành");



            return;
        }
        
        if (current instanceof IStepValidator) {
            String warning = null;
            if (((IStepValidator) current).validate(warning)) {
                ((IStepValidator) current).save();
                Toast.makeText(this, "Validate Successfully", Toast.LENGTH_SHORT).show();
                navController.navigate(stepNextTrans[stepIndex]);
                stepIndex++;
            } else {

            }
        }
    }

    private void PrevStep() {
        Fragment current = navigator.getChildFragmentManager().getFragments().get(0);
        if (stepIndex <= 0) return;

        if (current instanceof IStepValidator) {
            ((IStepValidator) current).save();

            navController.popBackStack();
            stepIndex--;
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }
}
