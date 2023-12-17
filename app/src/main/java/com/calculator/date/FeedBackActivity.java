package com.calculator.date;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.Objects;

import static com.calculator.date.MyFileUtils.theme_of_theme;

public class FeedBackActivity extends AppCompatActivity {
    private  EditText email;
    private  EditText message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }
        email = findViewById(R.id.customEmail);
        message = findViewById(R.id.customFeedback);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, theme_of_theme));
            Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(
                    new ColorDrawable(getResources().getColor(theme_of_theme)));
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feedback_option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_send_feedback) {

            String emailStr = email.getText().toString();
            String messageStr = message.getText().toString().trim();
            Intent emailIntent = new Intent(Intent.ACTION_SEND);

            emailIntent.setData(Uri.parse("mailto:")).setType("message/rfc822");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"datecalcul@gmail.com"});
            emailIntent.putExtra(Intent.EXTRA_CC, emailStr);
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "July Feedback");
            emailIntent.putExtra(Intent.EXTRA_TEXT,  messageStr);
            if (messageStr.equals(""))
            {
                Toast.makeText(this, getResources().getString(R.string.feed_message_empty), Toast.LENGTH_SHORT).show();
            }
            else
            if (emailIntent.resolveActivity(getPackageManager()) != null ) {
                startActivity(Intent.createChooser(emailIntent, "Send Email ..."));
            } else {
                Toast.makeText(this, "Sorry you don't have any email app", Toast.LENGTH_SHORT).show();
            }
        }
        finish();
        return true;
    }
}