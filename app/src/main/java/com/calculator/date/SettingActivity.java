package com.calculator.date;

import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Locale;
import java.util.Objects;

import static com.calculator.date.MyFileUtils.theme_of_theme;

public class SettingActivity extends AppCompatActivity {
    public static final String KEY_PREF_MODE = "mode_preference";
    public static final String KEY_PREF_LANGUAGE = "language_preference";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, theme_of_theme));
            Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(
                    new ColorDrawable(getResources().getColor(theme_of_theme)));
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            ListPreference themeListPreference = findPreference(SettingActivity.KEY_PREF_MODE);
            ListPreference languageListPreference = findPreference(SettingActivity.KEY_PREF_LANGUAGE);
            String local = Locale.getDefault().getLanguage();
            if (languageListPreference != null) {
                languageListPreference.setDefaultValue((!local.equals(Locale.FRENCH.getLanguage()) && !local.equals(Locale.ENGLISH.getLanguage()))
                        ? Locale.ENGLISH.getLanguage() : local);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_done_setting) {
            setResult(RESULT_OK);
        }
        finish();

        return true;
    }
}
