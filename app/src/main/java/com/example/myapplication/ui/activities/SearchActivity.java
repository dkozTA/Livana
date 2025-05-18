package com.example.myapplication.ui.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.myapplication.R;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity {

    private CardView cardName, cardWhere, cardWhen;
    private LinearLayout layoutNameExpanded, layoutWhereExpanded, layoutWhenExpanded;
    private LinearLayout cardInnerLayout, cardWhereInnerLayout, cardWhenInnerLayout;
    private boolean isNameExpanded = false;
    private boolean isWhereExpanded = false;
    private boolean isWhenExpanded = false;
    private TextInputEditText editTextDepartureDate, editTextArrivalDate;
    private final Calendar departureCalendar = Calendar.getInstance();
    private final Calendar arrivalCalendar = Calendar.getInstance();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Initialize views
        initializeViews();
        setClickListeners();
        setupDatePickers();
    }

    /**
     * Initialize all view components
     */
    private void initializeViews() {
        // Name section
        cardName = findViewById(R.id.cardName);
        layoutNameExpanded = findViewById(R.id.layoutNameExpanded);
        cardInnerLayout = findViewById(R.id.cardInnerLayout);

        // Where section
        cardWhere = findViewById(R.id.cardWhere);
        layoutWhereExpanded = findViewById(R.id.layoutWhereExpanded);
        cardWhereInnerLayout = findViewById(R.id.cardWhereInnerLayout);

        // When section
        cardWhen = findViewById(R.id.cardWhen);
        layoutWhenExpanded = findViewById(R.id.layoutWhenExpanded);
        cardWhenInnerLayout = findViewById(R.id.cardWhenInnerLayout);
        editTextDepartureDate = findViewById(R.id.editTextDepartureDate);
        editTextArrivalDate = findViewById(R.id.editTextArrivalDate);

        // Set tomorrow as default for departure date
        arrivalCalendar.add(Calendar.DAY_OF_MONTH, 1);
    }

    /**
     * Set up click listeners for all expandable cards
     */
    private void setClickListeners() {
        // Set click listener for the Name card
        cardName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleNameExpansion();
            }
        });

        // Set click listener for the Where card
        cardWhere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleWhereExpansion();
            }
        });

        // Set click listener for the When card
        cardWhen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleWhenExpansion();
            }
        });
    }

    /**
     * Set up date pickers for departure and arrival date fields
     */
    private void setupDatePickers() {
        // Setup date selection for departure date
        editTextDepartureDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(true);
            }
        });

        // Setup date selection for arrival date
        editTextArrivalDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(false);
            }
        });
    }

    /**
     * Show date picker dialog for selecting dates
     *
     * @param isDeparture true if picking departure date, false if picking arrival date
     */
    private void showDatePickerDialog(final boolean isDeparture) {
        Calendar calendar = isDeparture ? departureCalendar : arrivalCalendar;
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(Calendar.YEAR, year);
                        selectedDate.set(Calendar.MONTH, month);
                        selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        String formattedDate = dateFormat.format(selectedDate.getTime());

                        if (isDeparture) {
                            departureCalendar.set(year, month, dayOfMonth);
                            editTextDepartureDate.setText(formattedDate);

                            // If arrival date is before departure date, update it
                            if (arrivalCalendar.before(departureCalendar)) {
                                Calendar newArrival = (Calendar) departureCalendar.clone();
                                newArrival.add(Calendar.DAY_OF_MONTH, 1);
                                arrivalCalendar.setTime(newArrival.getTime());
                                editTextArrivalDate.setText(dateFormat.format(arrivalCalendar.getTime()));
                            }
                        } else {
                            arrivalCalendar.set(year, month, dayOfMonth);
                            editTextArrivalDate.setText(formattedDate);
                        }
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        // Set minimum date for departure to today
        if (isDeparture) {
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        } else {
            // Set minimum date for arrival to departure date + 1 day
            Calendar minDate = Calendar.getInstance();
            minDate.setTime(departureCalendar.getTime());
            datePickerDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());
        }

        datePickerDialog.show();
    }

    /**
     * Toggle the expansion of the Name search card
     */
    private void toggleNameExpansion() {
        if (isNameExpanded) {
            // Collapse the name section
            layoutNameExpanded.setVisibility(View.GONE);
            cardInnerLayout.setBackgroundResource(android.R.color.white); // Remove border
        } else {
            // Expand the name section
            layoutNameExpanded.setVisibility(View.VISIBLE);
            cardInnerLayout.setBackgroundResource(R.drawable.bg_card_with_border); // Add border
        }
        isNameExpanded = !isNameExpanded;
    }

    /**
     * Toggle the expansion of the Where search card
     */
    private void toggleWhereExpansion() {
        if (isWhereExpanded) {
            // Collapse the where section
            layoutWhereExpanded.setVisibility(View.GONE);
            cardWhereInnerLayout.setBackgroundResource(android.R.color.white); // Remove border
        } else {
            // Expand the where section
            layoutWhereExpanded.setVisibility(View.VISIBLE);
            cardWhereInnerLayout.setBackgroundResource(R.drawable.bg_card_with_border); // Add border
        }
        isWhereExpanded = !isWhereExpanded;
    }

    /**
     * Toggle the expansion of the When search card
     */
    private void toggleWhenExpansion() {
        if (isWhenExpanded) {
            // Collapse the when section
            layoutWhenExpanded.setVisibility(View.GONE);
            cardWhenInnerLayout.setBackgroundResource(android.R.color.white); // Remove border
        } else {
            // Expand the when section
            layoutWhenExpanded.setVisibility(View.VISIBLE);
            cardWhenInnerLayout.setBackgroundResource(R.drawable.bg_card_with_border); // Add border
        }
        isWhenExpanded = !isWhenExpanded;
    }
}