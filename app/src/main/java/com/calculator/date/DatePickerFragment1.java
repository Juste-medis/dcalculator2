package com.calculator.date;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.View;
import android.widget.DatePicker;

import java.util.Objects;

import static com.calculator.date.MyFileUtils.theme_of_theme;
/**
 * A simple {@link Fragment} subclass.
 */
// date picker fragment used as a dialog to pick a date
public class DatePickerFragment1 extends androidx.fragment.app.DialogFragment implements DatePickerDialog.OnDateSetListener {
    public static DatePickerFragment1 newInstance(int id, int year, int month, int day) {
        // create a new instance
        DatePickerFragment1 datePickerFragment1 = new DatePickerFragment1();
        // create and attach a bundle with the id of the button that initiated the dialog, and the date information
        Bundle args = new Bundle();
        args.putInt("id", id);
        args.putInt("year", year);
        args.putInt("month", month);
        args.putInt("day", day);
        datePickerFragment1.setArguments(args);

        return datePickerFragment1;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            theme_of_theme = theme_of_theme == 0 ? R.color.dark: theme_of_theme;
            view.setBackgroundColor(ContextCompat.getColor(view.getContext().getApplicationContext(), theme_of_theme));
            this.requireActivity().getWindow().setStatusBarColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), theme_of_theme));
            Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setBackgroundDrawable(
                    new ColorDrawable(getContext().getResources().getColor(theme_of_theme)));
        }
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // get the date information
        int year = getArguments().getInt("year");
        int month = getArguments().getInt("month");
        int day = getArguments().getInt("day");

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        // use the handler to handle the chosen date²
        ((MainActivity)getActivity()).onDatePickerDialogDone(getArguments().getInt("id"), year, month, day);
    }
}