package com.calculator.date.MyViews;

import android.content.Context;
import android.graphics.Typeface;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.calculator.date.R;


public class TitleTextView extends SimpleContentTextView {

    public TitleTextView(@NonNull Context context) {
        super(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(3, 3, 3, 0);
        this.setTypeface(this.getTypeface(), Typeface.BOLD);
        this.setLayoutParams(params);

        this.setTextSize(getResources().getDimension(R.dimen.defaultTextSize));
        this.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        this.setPadding(8,8,8,8);

        this.setTextColor(getResources().getColor(android.R.color.white));
    }
}
