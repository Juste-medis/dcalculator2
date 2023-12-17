package com.calculator.date;

// interface for handling the response of a date picker dialog
public interface DatePickerDialogListener {
    public void onDatePickerDialogDone(int id, int year, int month, int day);
}
