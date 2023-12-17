package com.calculator.date;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.calculator.date.MyViews.SimpleContentTextView;
import com.calculator.date.MyViews.TitleTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import static com.calculator.date.MainActivity.SETTING_ACTIVITY_REQUEST_CODE;
import static com.calculator.date.MyFileUtils.theme_of_theme;

public class DureActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialogListener, NumberPicker.OnValueChangeListener {
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd [EEEEEEE]", Locale.getDefault());
    private Button addBaseDate;
    LinearLayout addAmountLayoutYears;
    LinearLayout addAmountLayoutMonths;
    LinearLayout addAmountLayoutDays;
    LinearLayout resContainer;
    LinearLayout dure_layout;
    private NumberPicker addAmountYears;
    private NumberPicker addAmountMonths;
    private NumberPicker addAmountDays;
    public boolean businessMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dure);
        // get all the views from the layout
        addBaseDate = (Button) findViewById(R.id.add_base_date);
        addAmountLayoutYears = (LinearLayout) findViewById(R.id.add_amount_layout_years);
        addAmountLayoutMonths = (LinearLayout) findViewById(R.id.add_amount_layout_months);
        addAmountLayoutDays = (LinearLayout) findViewById(R.id.add_amount_layout_days);
        addAmountYears = (NumberPicker) findViewById(R.id.add_amount_years);
        addAmountMonths = (NumberPicker) findViewById(R.id.add_amount_months);
        addAmountDays = (NumberPicker) findViewById(R.id.add_amount_days);
        resContainer = (LinearLayout) findViewById(R.id.ondure_resContainer);

        dure_layout = findViewById(R.id.dure_container_constraint);
        // addAmountDays todo manipuler la couleur des picker
        addBaseDate.setOnClickListener(this);

        String[] numbers = new String[1001];
        for (int i = 0; i <= 1000; i++) {
            numbers[i] = Integer.toString(i - 250);
        }

        addAmountYears.setMaxValue(1000);
        addAmountYears.setMinValue(0);
        addAmountYears.setWrapSelectorWheel(true);
        addAmountYears.setDisplayedValues(numbers);
        addAmountYears.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        addAmountYears.setOnValueChangedListener(this);

        addAmountMonths.setMaxValue(1000);
        addAmountMonths.setMinValue(0);
        addAmountMonths.setWrapSelectorWheel(true);
        addAmountMonths.setDisplayedValues(numbers);
        addAmountMonths.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        addAmountMonths.setOnValueChangedListener(this);

        addAmountDays.setMaxValue(1000);
        addAmountDays.setMinValue(0);
        addAmountDays.setWrapSelectorWheel(true);
        addAmountDays.setDisplayedValues(numbers);
        addAmountDays.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        addAmountDays.setOnValueChangedListener(this);

        if (savedInstanceState == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

            String today = sdf.format(calendar.getTime());
            addBaseDate.setText(today);

            addAmountYears.setValue(250);
            addAmountMonths.setValue(250);
            addAmountDays.setValue(250);
        } else {
            // restore the date
            addBaseDate.setText(savedInstanceState.getString("addBaseDate"));

            // restore the amounts
            addAmountYears.setValue(savedInstanceState.getInt("addAmountYears"));
            addAmountMonths.setValue(savedInstanceState.getInt("addAmountMonths"));
            addAmountDays.setValue(savedInstanceState.getInt("addAmountDays"));
        }
        // update the number pickers according to the business mode
        updateNumberPickers();
        // calculate the addition
        calculateAddition();
        dure_layout.setBackgroundColor(getResources().getColor(theme_of_theme));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            theme_of_theme = theme_of_theme == 0 ? R.color.dark : theme_of_theme;
            getWindow().setStatusBarColor(ContextCompat.getColor(this, theme_of_theme));
            Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(
                    new ColorDrawable(getResources().getColor(theme_of_theme)));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save the date and the amounts
        outState.putString("addBaseDate", (String) addBaseDate.getText());
        outState.putInt("addAmountYears", addAmountYears.getValue());
        outState.putInt("addAmountMonths", addAmountMonths.getValue());
        outState.putInt("addAmountDays", addAmountDays.getValue());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.add_base_date) {
            // get a calendar and get rid of the time of day information
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

            // get the button's current date
            String source = (String) addBaseDate.getText();

            // parse the button's text into the calendar as a date
            try {
                calendar.setTime(sdf.parse(source));
            } catch (ParseException e) {
                Log.d("onClick", "parse exception");
            }

            // get the year month and day of the calendar with the parsed date
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            // create and show a date picker fragment using the date information from the button
            DatePickerFragment2 fragment = DatePickerFragment2.newInstance(id, year, month, day);
            fragment.show(getSupportFragmentManager(), "add_date_picker");
        }
    }

    @Override
    public void onDatePickerDialogDone(int id, int year, int month, int day) {
        // get a calendar and get rid of the time of day information
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        // set the year month and day to the chosen date
        calendar.set(year, month, day);

        // get a string for the chosen date
        String newDateText = sdf.format(calendar.getTime());

        // update the button's text to be the new chosen date
        if (id == R.id.add_base_date) {
            addBaseDate.setText(newDateText);
        }

        // calculate the addition
        calculateAddition();
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        // calculate the addition
        calculateAddition();
    }

    public void updateNumberPickers() {
        // toggle the year and month pickers based on the business mode
        addAmountYears.setEnabled(!businessMode);
        addAmountMonths.setEnabled(!businessMode);

        // toggle the visibility of the year and month pickers based on the business mode
        if (businessMode) {
            addAmountLayoutYears.setVisibility(View.GONE);
            addAmountLayoutMonths.setVisibility(View.GONE);
        } else {
            addAmountLayoutYears.setVisibility(View.VISIBLE);
            addAmountLayoutMonths.setVisibility(View.VISIBLE);
        }
    }

    public void calculateAddition() {
        // get a calendar and get rid of the time of day information
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        // try to set the calendar to the date from the button's text
        try {
            Date date = sdf.parse((String) addBaseDate.getText());
            calendar.setTime(date);
        } catch (ParseException e) {
            Log.d("calculateAddition", "parse exception");
        }

        // get the amount of years months and days to add
        int amountYears = addAmountYears.getValue();
        int amountMonths = addAmountMonths.getValue();
        int amountDays = addAmountDays.getValue();

        if (businessMode) {
            // adjust the amount of days
            int amount = amountDays - 250;

            // keep adding or subtracting days, skipping weekends until the amount is satisfied
            if (amount > 0) {
                while (amount > 0) {
                    calendar.add(Calendar.DAY_OF_YEAR, 1);
                    if (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY && calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
                        amount--;
                    }
                }
            } else if (amount < 0) {
                while (amount < 0) {
                    calendar.add(Calendar.DAY_OF_YEAR, -1);
                    if (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY && calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
                        amount++;
                    }
                }
            }

            // update the result text view with the results
            //addResults.setText(sdf.format(calendar.getTime()));
            resContainer.removeAllViews();

            //addResults.setText(sdf.format(calendar.getTime()));
            SimpleContentTextView labelTextm = new SimpleContentTextView(this);
            TitleTextView valueTextm = new TitleTextView(this);
            labelTextm.setText(getResources().getString(R.string.deb_or_fin));
            valueTextm.setText(sdf.format(calendar.getTime()));

            LinearLayout linearLayoutm = new LinearLayout(this);
            linearLayoutm.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            linearLayoutm.setOrientation(LinearLayout.HORIZONTAL);
            linearLayoutm.setBackgroundColor(0xffffffff);
            linearLayoutm.addView(labelTextm);
            linearLayoutm.addView(valueTextm);
            resContainer.addView(linearLayoutm);


        } else {
            // add the adjusted amounts
            calendar.add(Calendar.YEAR, amountYears - 250);
            calendar.add(Calendar.MONTH, amountMonths - 250);
            calendar.add(Calendar.DAY_OF_YEAR, amountDays - 250);
            // update the result text view with the results

            resContainer.removeAllViews();

            //addResults.setText(sdf.format(calendar.getTime()));
            SimpleContentTextView labelTextm = new SimpleContentTextView(this);
            TitleTextView valueTextm = new TitleTextView(this);
            labelTextm.setText(getResources().getString(R.string.deb_or_fin));
            valueTextm.setText(sdf.format(calendar.getTime()));

            LinearLayout linearLayoutm = new LinearLayout(this);
            linearLayoutm.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            linearLayoutm.setOrientation(LinearLayout.VERTICAL);
            linearLayoutm.setBackgroundColor(0xffffffff);
            linearLayoutm.addView(labelTextm);
            linearLayoutm.addView(valueTextm);
            resContainer.addView(linearLayoutm);

            //=======================      ===============================      ==============================
            Calendar calendar1 = Calendar.getInstance();
            try {
                Date date1 = sdf.parse((String) addBaseDate.getText());
                Date date2 = sdf.parse((String) valueTextm.getText());
                calendar1.setTime(date1);

                if (calendar.getTimeInMillis() < calendar1.getTimeInMillis()) {
                    calendar1.setTime(date2);
                    calendar.setTime(date1);
                }
            } catch (ParseException e) {
                Log.d("calculateDifference", "parse exception");
            }

            // get the two times
            long time1 = calendar1.getTimeInMillis();
            long time2 = calendar.getTimeInMillis();
            // get their total difference in days based on an assumed amount of days in a year
            long difference = Math.abs(time1 - time2);
            double yearInDays = 365.2425;
            double daysInMonth = yearInDays / 12;
            long totalDays = 1 + difference / (1000 * 60 * 60 * 24);
            long miduredays = totalDays / 2;

            long mi_years = (long) Math.floor(miduredays / yearInDays);
            long mi_months = (long) Math.floor((miduredays - (mi_years * yearInDays)) / daysInMonth);
            long mi_days = (long) Math.floor(miduredays - (mi_years * yearInDays) - (mi_months * daysInMonth));

            //================================================
            // get a calendar and get rid of the time of day information
            Calendar calendar3 = Calendar.getInstance();
            calendar3.set(Calendar.HOUR_OF_DAY, 0);
            calendar3.set(Calendar.MINUTE, 0);
            calendar3.set(Calendar.SECOND, 0);
            // try to set the calendar to the date from the button's text
            try {
                Date date = sdf.parse((String) addBaseDate.getText());
                calendar3.setTime(date);
            } catch (ParseException e) {
                Log.d("calculateAddition", "parse exception");
            }

            // add the adjusted amounts
            calendar3.add(Calendar.YEAR, (int) mi_years);
            calendar3.add(Calendar.MONTH, (int) mi_months);
            calendar3.add(Calendar.DAY_OF_YEAR, (int) mi_days);

            //moitiResults.setText(sdf.format(calendar3.getTime()));
            SimpleContentTextView labelTextmi = new SimpleContentTextView(this);
            TitleTextView valueTextmi = new TitleTextView(this);
            labelTextmi.setText(getResources().getString(R.string.mi_dure_lab_title));
            valueTextmi.setText(sdf.format(calendar3.getTime()));
            LinearLayout linearLayoutmi = new LinearLayout(this);
            linearLayoutmi.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            linearLayoutmi.setOrientation(LinearLayout.VERTICAL);
            linearLayoutmi.setBackgroundColor(0xffffffff);
            linearLayoutmi.addView(labelTextmi);
            linearLayoutmi.addView(valueTextmi);
            resContainer.addView(linearLayoutmi);
        }

        // update the result text view with the results
        //addResults.setText(sdf.format(calendar.getTime()));
/*
        SimpleContentTextView labelTextm = new SimpleContentTextView(this);
        TitleTextView valueTextm = new TitleTextView(this);
        labelTextm.setText(getResources().getString(R.string.mi_dure_lab_title));
        valueTextm.setText(sdf.format(calendar.getTime()));
        LinearLayout linearLayoutm = new LinearLayout(this);
        linearLayoutm.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayoutm.setOrientation(LinearLayout.HORIZONTAL);
        linearLayoutm.setBackgroundColor(0xffffffff);
        linearLayoutm.addView(labelTextm);
        linearLayoutm.addView(valueTextm);
        resContainer.addView(linearLayoutm);
 */
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_settings:
                Intent Setting_intent = new Intent(this, SettingActivity.class);
                startActivityForResult(Setting_intent, SETTING_ACTIVITY_REQUEST_CODE);
                break;

            case R.id.action_about:
                Intent about_intent = new Intent(this, AboutActivity.class);
                startActivity(about_intent);
                break;
            case R.id.action_rating: {
                final String appPackageName = getPackageName();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
            break;
            case R.id.action_share_app: {
                share_app_process();
            }
            break;
            case R.id.action_feedback:
                startActivity(new Intent(this, FeedBackActivity.class));
                break;
            default:
                break;
        }
        return true;
    }

    private void share_app_process() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, getResources().getText(R.string.sharing_app_text));
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.sharing_app_text_intent_title)));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SETTING_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Intent restart_intent = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(restart_intent);
                finish();
            }
        }
    }
}