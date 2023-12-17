package com.calculator.date;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private boolean businessMode = false;
    private SimpleDateFormat sdf = new SimpleDateFormat("EEEEEEEEE dd MMMMMMMMMM yyyy", Locale.getDefault());

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
        /*
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        //sdf.format(calendar1.getTime())
        System.out.println("cal1= "+ sdf.format(calendar1.getTime()));

        // get rid of the first calendar's time of day information
        calendar1.set(Calendar.MONTH,0);
        calendar1.set(Calendar.HOUR_OF_DAY,0);
        calendar1.set(Calendar.MINUTE, 0);
        calendar1.set(Calendar.SECOND, 0);
        System.out.println("comp= "+ calendar1.compareTo(calendar2));
        System.out.println("cal2= "+ sdf.format(calendar1.getTime()));
         */
        calculateDifference();
    }

    public void calculateDifference() {
        // get two calendars
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar1.set(Calendar.MONTH, 0);
        calendar2.set(Calendar.MONTH, 2);
        System.out.println("c1 = " + calendar1.getTime());
        System.out.println("c2 = " + calendar2.getTime());

        if (businessMode) {
            long time1 = calendar1.getTimeInMillis();
            long time2 = calendar2.getTimeInMillis();
            long difference = Math.abs(time2 - time1);
            long totalDays = difference / (1000 * 60 * 60 * 24);
            long preDays = MainActivity.DateCalcHelper.daysUntilSunday(calendar1);
            long postDays = MainActivity.DateCalcHelper.daysFromSunday(calendar2);
            long weeks = (totalDays - preDays - postDays) / 7;
            long days = Math.max(0, preDays - 2) + postDays + (weeks * 5);
            System.out.println(days + " business days");
        } else {
            // get the two times
            long time1 = calendar1.getTimeInMillis();
            long time2 = calendar2.getTimeInMillis();

            // get their total difference in days based on an assumed amount of days in a year
            long difference = Math.abs(time1 - time2);
            System.out.println("diff = " + difference);

            double yearInDays = 365.2425;
            double daysInMonth = yearInDays / 12;

            long totalYears = difference / (1000 * 60 * 60 * 24 * (long) daysInMonth * 12);
            long totalMonths =  difference / (1000 * 60 * 60 * 24 * (long) daysInMonth);
            long totalDays =  difference / (1000 * 60 * 60 * 24);
            long totalHours =  difference / (1000 * 60 * 60);
            long totalMinutes = difference / (1000 * 60);
            long totalSecondes = difference / (1000);
            long miduredays = totalDays / 2;

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

            // add the adjusted amounts
            calendar.add(Calendar.YEAR, (int) mi_years);
            calendar.add(Calendar.MONTH, (int) mi_months);
            calendar.add(Calendar.DAY_OF_YEAR, (int) mi_days);
            System.out.println("midure = " + sdf.format(calendar.getTime()));
            String nexline = "\n";
            System.out.println(
                    years + " " + " an " + nexline +
                            months + " " + " mois " + nexline +
                            days + " " + " jours " + nexline +
                            totalYears + " " + " tans " + nexline +
                            totalMonths + " tmois " + nexline +
                            totalDays + " tjours " + nexline +
                            totalHours + " " + " theures " + nexline +
                            totalMinutes + " " + " tmin " + nexline +
                            totalSecondes + " tsec "
            );
        }
    }

}