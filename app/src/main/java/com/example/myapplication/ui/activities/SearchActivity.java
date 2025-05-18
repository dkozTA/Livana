package com.example.myapplication.ui.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
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

    // Tab system variables
    private LinearLayout tabHomes, tabServices, tabExperiences;
    private LinearLayout homesContent, servicesContent, experiencesContent;
    private ImageView iconHomes, iconServices, iconExperiences;
    private TextView textHomes, textServices, textExperiences;

    // Current active tab
    private enum Tab {
        HOMES, SERVICES, EXPERIENCES
    }
    private Tab currentTab = Tab.HOMES;

    // Original variables for Homes tab
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
        setTabClickListeners();
        setClickListeners();
        setupDatePickers();

        // Set initial tab state
        switchToTab(Tab.HOMES);
    }

    /**
     * Initialize all view components including tabs
     */
    private void initializeViews() {
        // Tab views
        tabHomes = findViewById(R.id.tabHomes);
        tabServices = findViewById(R.id.tabServices);
        tabExperiences = findViewById(R.id.tabExperiences);

        // Content views
        homesContent = findViewById(R.id.homesContent);
        servicesContent = findViewById(R.id.servicesContent);
        experiencesContent = findViewById(R.id.experiencesContent);

        // Tab icons and texts
        iconHomes = findViewById(R.id.iconHomes);
        iconServices = findViewById(R.id.iconServices);
        iconExperiences = findViewById(R.id.iconExperiences);
        textHomes = findViewById(R.id.textHomes);
        textServices = findViewById(R.id.textServices);
        textExperiences = findViewById(R.id.textExperiences);

        // Name section (Homes tab)
        cardName = findViewById(R.id.cardName);
        layoutNameExpanded = findViewById(R.id.layoutNameExpanded);
        cardInnerLayout = findViewById(R.id.cardInnerLayout);

        // Where section (Homes tab)
        cardWhere = findViewById(R.id.cardWhere);
        layoutWhereExpanded = findViewById(R.id.layoutWhereExpanded);
        cardWhereInnerLayout = findViewById(R.id.cardWhereInnerLayout);

        // When section (Homes tab)
        cardWhen = findViewById(R.id.cardWhen);
        layoutWhenExpanded = findViewById(R.id.layoutWhenExpanded);
        cardWhenInnerLayout = findViewById(R.id.cardWhenInnerLayout);
        editTextDepartureDate = findViewById(R.id.editTextDepartureDate);
        editTextArrivalDate = findViewById(R.id.editTextArrivalDate);

        // Set tomorrow as default for arrival date
        arrivalCalendar.add(Calendar.DAY_OF_MONTH, 1);
    }

    /**
     * Set up click listeners for tab switching
     */
    private void setTabClickListeners() {
        tabHomes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToTab(Tab.HOMES);
            }
        });

        tabServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToTab(Tab.SERVICES);
            }
        });

        tabExperiences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToTab(Tab.EXPERIENCES);
            }
        });
    }

    /**
     * Switch to the specified tab
     */
    private void switchToTab(Tab tab) {
        currentTab = tab;

        // Hide all content
        homesContent.setVisibility(View.GONE);
        servicesContent.setVisibility(View.GONE);
        experiencesContent.setVisibility(View.GONE);

        // Reset all tab styles
        resetTabStyles();

        // Show selected content and update tab style
        switch (tab) {
            case HOMES:
                homesContent.setVisibility(View.VISIBLE);
                setActiveTabStyle(textHomes, iconHomes);
                break;
            case SERVICES:
                servicesContent.setVisibility(View.VISIBLE);
                setActiveTabStyle(textServices, iconServices);
                break;
            case EXPERIENCES:
                experiencesContent.setVisibility(View.VISIBLE);
                setActiveTabStyle(textExperiences, iconExperiences);
                break;
        }
    }

    /**
     * Reset all tab styles to inactive state
     */
    private void resetTabStyles() {
        // Reset text colors
        textHomes.setTextColor(getResources().getColor(android.R.color.darker_gray));
        textServices.setTextColor(getResources().getColor(android.R.color.darker_gray));
        textExperiences.setTextColor(getResources().getColor(android.R.color.darker_gray));

        // Reset text styles
        textHomes.setTypeface(null, android.graphics.Typeface.NORMAL);
        textServices.setTypeface(null, android.graphics.Typeface.NORMAL);
        textExperiences.setTypeface(null, android.graphics.Typeface.NORMAL);

        // Reset icon colors (you might need to create different drawable resources for different states)
        iconHomes.setColorFilter(getResources().getColor(android.R.color.darker_gray));
        iconServices.setColorFilter(getResources().getColor(android.R.color.darker_gray));
        iconExperiences.setColorFilter(getResources().getColor(android.R.color.darker_gray));
    }

    /**
     * Set active style for the selected tab
     */
    private void setActiveTabStyle(TextView textView, ImageView iconView) {
        // Set active text color and style
        textView.setTextColor(getResources().getColor(R.color.colorAccent)); // You might need to define this color
        textView.setTypeface(null, android.graphics.Typeface.BOLD);

        // Set active icon color
        iconView.setColorFilter(getResources().getColor(R.color.colorAccent));
    }

    /**
     * Set up click listeners for all expandable cards (Homes tab only)
     */
    private void setClickListeners() {
        if (cardName != null) {
            cardName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleNameExpansion();
                }
            });
        }

        if (cardWhere != null) {
            cardWhere.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleWhereExpansion();
                }
            });
        }

        if (cardWhen != null) {
            cardWhen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleWhenExpansion();
                }
            });
        }
    }

    /**
     * Set up date pickers for departure and arrival date fields
     */
    private void setupDatePickers() {
        if (editTextDepartureDate != null) {
            editTextDepartureDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDatePickerDialog(true);
                }
            });
        }

        if (editTextArrivalDate != null) {
            editTextArrivalDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDatePickerDialog(false);
                }
            });
        }
    }

    /**
     * Show date picker dialog for selecting dates
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
        if (currentTab != Tab.HOMES) return;

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
        if (currentTab != Tab.HOMES) return;

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
        if (currentTab != Tab.HOMES) return;

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