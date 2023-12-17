package com.calculator.date.MyViews;

import android.content.Context;
import android.graphics.Typeface;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.calculator.date.R;


public class SimpleContentTextView extends androidx.appcompat.widget.AppCompatTextView {
    public SimpleContentTextView(@NonNull Context context) {
        super(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(16,1,10,0);
        this.setTextSize(getResources().getDimension(R.dimen.defaultTextSize));
        this.setTypeface(this.getTypeface(), Typeface.BOLD);
        this.setLayoutParams(params);
    }
}
