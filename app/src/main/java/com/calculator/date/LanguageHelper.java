package com.calculator.date;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;

import java.util.Locale;

public class LanguageHelper {

    public static void set__lang(Resources res, String lang) {
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration config = res.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(new Locale(lang.toLowerCase()));
        } else {
            config.locale = new Locale(lang.toLowerCase());
        }
        res.updateConfiguration(config, dm);
    }
}