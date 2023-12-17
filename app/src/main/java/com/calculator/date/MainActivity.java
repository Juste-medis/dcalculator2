package com.calculator.date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.calculator.date.MyViews.SimpleContentTextView;
import com.calculator.date.MyViews.TitleTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import static com.calculator.date.MyFileUtils.theme_of_theme;
import static com.calculator.date.MyFileUtils.lang_of_lang;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialogListener {
    public static final int SETTING_ACTIVITY_REQUEST_CODE = 3;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy MM dd [EEEEEEE]", Locale.getDefault());
    private Button compareDate1;
    private Button compareDate2;
    private TextView compareResults;
    public boolean businessMode;
    public ConstraintLayout mainConstraintLayout;
    public LinearLayout mLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        compareDate1 = (Button) findViewById(R.id.compare_date_1);
        compareDate2 = (Button) findViewById(R.id.compare_date_2);
        compareResults = (TextView) findViewById(R.id.compare_results);
        mainConstraintLayout = findViewById(R.id.main_container_constraint);
        mLinearLayout = findViewById(R.id.results_container);

        settingProcess();
        // set the button click listeners
        compareDate1.setOnClickListener(this);
        compareDate2.setOnClickListener(this);
        //l'ecranj ,n'est pas responsif

        // handle runtime configuration change
        if (savedInstanceState == null) {
            // get a calendar and get rid of the time of day information
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

            // set the default dates to today
            String today = sdf.format(calendar.getTime());
            compareDate1.setText(today);
            compareDate2.setText(today);
        } else {
            // restore the dates
            compareDate1.setText(savedInstanceState.getString("compareDate1"));
            compareDate2.setText(savedInstanceState.getString("compareDate2"));
        }
        // calculate the difference
        calculateDifference();
    }


    private void settingProcess() {
        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, false);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        String local = Locale.getDefault().getLanguage();
        lang_of_lang = sharedPref.getString(SettingActivity.KEY_PREF_LANGUAGE,
                (!local.equals(Locale.FRENCH.getLanguage()) && !local.equals(Locale.ENGLISH.getLanguage()))
                        ? Locale.ENGLISH.getLanguage() : local);
        theme_of_theme = getResources()
                .getIdentifier(sharedPref.getString(SettingActivity.KEY_PREF_MODE, "orange"), "color",
                        getApplicationContext().getPackageName());
        LanguageHelper.set__lang(getResources(), lang_of_lang);
        setTitle(getString(R.string.app_name));
        theme_of_theme = theme_of_theme == 0 ? R.color.dark : theme_of_theme;
        mainConstraintLayout.setBackgroundColor(getResources().getColor(theme_of_theme));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, theme_of_theme));
            Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(
                    new ColorDrawable(getResources().getColor(theme_of_theme)));
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // save the dates
        outState.putString("compareDate1", (String) compareDate1.getText());
        outState.putString("compareDate2", (String) compareDate2.getText());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.compare_date_1 || id == R.id.compare_date_2) {
            // get a calendar and get rid of the time of day information
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

            // get the source date from the button text
            String source;
            if (id == R.id.compare_date_1) {
                source = (String) compareDate1.getText();
            } else {
                source = (String) compareDate2.getText();
            }

            // parse the date into the calendar from the button text
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
            DatePickerFragment1 fragment = DatePickerFragment1.newInstance(id, year, month, day);
            fragment.show(getSupportFragmentManager(), "compare_date_picker");
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

        // update the button's text to the new chosen date
        if (id == R.id.compare_date_1) {
            compareDate1.setText(newDateText);
        } else if (id == R.id.compare_date_2) {
            compareDate2.setText(newDateText);
        }

        // calculate the difference
        calculateDifference();
    }

    @SuppressLint("SetTextI18n")
    public void calculateDifference() {
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();

        // get rid of the first calendar's time of day information
        calendar1.set(Calendar.HOUR_OF_DAY, 0);
        calendar1.set(Calendar.MINUTE, 0);
        calendar1.set(Calendar.SECOND, 0);

        // get rid of the second calendar's time of day information
        calendar2.set(Calendar.HOUR_OF_DAY, 0);
        calendar2.set(Calendar.MINUTE, 0);
        calendar2.set(Calendar.SECOND, 0);

        // parse the dates into the calendars from the buttons' text
        try {
            Date date1 = sdf.parse((String) compareDate1.getText());
            Date date2 = sdf.parse((String) compareDate2.getText());

            calendar1.setTime(date1);
            calendar2.setTime(date2);

            if (calendar2.getTimeInMillis() < calendar1.getTimeInMillis()) {
                calendar1.setTime(date2);
                calendar2.setTime(date1);
            }
        } catch (ParseException e) {
            Log.d("calculateDifference", "parse exception");
        }

        if (businessMode) {
            long time1 = calendar1.getTimeInMillis();
            long time2 = calendar2.getTimeInMillis();

            long difference = Math.abs(time2 - time1);
            long totalDays = difference / (1000 * 60 * 60 * 24);

            // get the days until the first sunday, and the days from the last sunday
            long preDays = DateCalcHelper.daysUntilSunday(calendar1);
            long postDays = DateCalcHelper.daysFromSunday(calendar2);

            // get the number of weeks between the first and last sundays
            long weeks = (totalDays - preDays - postDays) / 7;

            // calculate the days based on the number of weeks and the excess pre and post days
            long days = Math.max(0, preDays - 2) + postDays + (weeks * 5);

            // update the result text view with the results
            compareResults.setText(days + " business days");
        } else {
            // get the two times
            long time1 = calendar1.getTimeInMillis();
            long time2 = calendar2.getTimeInMillis();

            // get their total difference in days based on an assumed amount of days in a year
            long difference = Math.abs(time1 - time2);

            double yearInDays = 365.2425;
            double daysInMonth = yearInDays / 12;

            long totalYears = difference / (1000 * 60 * 60 * 24 * (long) daysInMonth * 12);
            long totalMonths = difference / (1000 * 60 * 60 * 24 * (long) daysInMonth);
            long totalDays = difference / (1000 * 60 * 60 * 24);
            long totalHours = difference / (1000 * 60 * 60);
            long totalMinutes = difference / (1000 * 60);
            long totalSecondes = difference / (1000);
            long miduredays = totalDays / 2;

            // get assumed amount of years, months, and days
            long years = (long) Math.floor(totalDays / yearInDays);
            long months = (long) Math.floor((totalDays - (years * yearInDays)) / daysInMonth);
            long days = (long) Math.floor(totalDays - (years * yearInDays) - (months * daysInMonth));

            long mi_years = (long) Math.floor(miduredays / yearInDays);
            long mi_months = (long) Math.floor((miduredays - (mi_years * yearInDays)) / daysInMonth);
            long mi_days = (long) Math.floor(miduredays - (mi_years * yearInDays) - (mi_months * daysInMonth));
            //================================================
            // get a calendar and get rid of the time of day information
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            // try to set the calendar to the date from the button's text
            try {
                Date date = sdf.parse((String) compareDate1.getText());
                calendar.setTime(date);
            } catch (ParseException e) {
                Log.d("calculateAddition", "parse exception");
            }

            // add the adjusted amounts
            calendar.add(Calendar.YEAR, (int) mi_years);
            calendar.add(Calendar.MONTH, (int) mi_months);
            calendar.add(Calendar.DAY_OF_YEAR, (int) mi_days);

            String[] resLab = new String[]{
                    getResources().getString(R.string.days),
                    getResources().getString(R.string.month),
                    getResources().getString(R.string.years),
                    getResources().getString(R.string.total_days),
                    getResources().getString(R.string.total_month),
                    getResources().getString(R.string.total_years),
                    getResources().getString(R.string.total_hour),
                    getResources().getString(R.string.total_min),
                    getResources().getString(R.string.total_sec),
            };

            long[] resValu = new long[]{
                    days,
                    months,
                    years,
                    totalDays,
                    totalMonths,
                    totalYears,
                    totalHours,
                    totalMinutes,
                    totalSecondes,
            };


            mLinearLayout.removeAllViews();
            for (int i = 0; i < resLab.length; i++) {
                SimpleContentTextView labelText = new SimpleContentTextView(this);
                TitleTextView valueText = new TitleTextView(this);

                if(theme_of_theme==R.color.dark){
                    labelText.setTextColor(getResources().getColor(R.color.light_blue));
                }

                labelText.setText(resLab[i]);

                valueText.setText(String.valueOf(resValu[i]));

                LinearLayout linearLayout = new LinearLayout(this);
                linearLayout.setBaselineAligned(false);

                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.addView(labelText);
                linearLayout.addView(valueText);
                mLinearLayout.addView(linearLayout);
            }

            SimpleContentTextView labelTextm = new SimpleContentTextView(this);
            TitleTextView valueTextm = new TitleTextView(this);
            labelTextm.setText(getResources().getString(R.string.mi_dure_lab_title));
            valueTextm.setText(sdf.format(calendar.getTime()));

            LinearLayout linearLayoutm = new LinearLayout(this);
            linearLayoutm.setBaselineAligned(false);

            linearLayoutm.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            linearLayoutm.setOrientation(LinearLayout.HORIZONTAL);
            linearLayoutm.setBackgroundColor(0xffffffff);
            linearLayoutm.addView(labelTextm);
            linearLayoutm.addView(valueTextm);

            //todo ondure design

/*
            compareResults.setText(
                    getString(R.string.years) + ": " + years + nexline +
                            getString(R.string.month) + ": " + months + nexline +
                            getString(R.string.days) + ": " + days + nexline +
                            "---------------------" + nexline +
                            getString(R.string.total_years) + ": " + totalYears + nexline +
                            getString(R.string.total_month) + ": " + totalMonths + nexline +
                            getString(R.string.total_days) + ": " + totalDays + nexline +
                            getString(R.string.total_hour) + ": " + totalHours + nexline +
                            getString(R.string.total_min) + ": " + totalMinutes + nexline +
                            getString(R.string.total_sec) + ": " + totalSecondes + nexline
            );
 */
        }
    }

    public static class DateCalcHelper {
        public static long daysUntilSunday(Calendar calendar) {
            long days = 0;
            while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                days++;
                calendar.add(Calendar.DAY_OF_YEAR, 1);
            }
            return days;
        }

        public static long daysFromSunday(Calendar calendar) {
            long days = 0;
            while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                days++;
                calendar.add(Calendar.DAY_OF_YEAR, -1);
            }
            return days;
        }
    }

    public void onBasedDureeButtonClicked(View view) {
        Intent intent = new Intent(this, DureActivity.class);
        startActivity(intent);
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
                Intent restart_intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(restart_intent);
                finish();
            }
        }
    }


}
