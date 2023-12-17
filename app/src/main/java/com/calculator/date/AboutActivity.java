package com.calculator.date;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.Objects;

import static com.calculator.date.MyFileUtils.theme_of_theme;

public class AboutActivity extends AppCompatActivity {
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        TextView privacyPolicy = findViewById(R.id.privacy_policy);
        TextView terms = findViewById(R.id.term_conditions);


        customTextView(terms, getResources().getString(R.string.term_condition_about),
                "https://drive.google.com/file/d/11DkvQLFPoxG0E_fL8EWkykosQHz58HUL/view?usp=sharing");

        customTextView(privacyPolicy, getResources().getString(R.string.privacy_about),
                "https://drive.google.com/file/d/1S0sQhYch9Ux9LjE4gBMQD5fZDLeIUTtx/view?usp=sharing");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, theme_of_theme));
            Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(
                    new ColorDrawable(getResources().getColor(theme_of_theme)));
        }

    }

    private void customTextView(TextView textView, String wrap, final String url) {
        SpannableStringBuilder spanTxt = new SpannableStringBuilder("");
        spanTxt.append(wrap);
        spanTxt.setSpan(
                new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {
                        Uri web_page = Uri.parse(url);
                        Intent intent = new Intent(Intent.ACTION_VIEW, web_page);
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        }
                    }
                }, spanTxt.length() - wrap.length(), spanTxt.length(), 0);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(spanTxt, TextView.BufferType.SPANNABLE);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return true;
    }
}