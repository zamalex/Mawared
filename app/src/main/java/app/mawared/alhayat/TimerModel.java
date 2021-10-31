package app.mawared.alhayat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimerModel {
   public long milliseconds;
    public long seconds;
    public long minutes;
    public long hours;
    public long days;

    public TimerModel(long milliseconds, long seconds, long minutes, long hours, long days) {
        this.milliseconds = milliseconds;
        this.seconds = seconds;
        this.minutes = minutes;
        this.hours = hours;
        this.days = days;
    }

    public TimerModel(long milliseconds) {
        this.milliseconds = milliseconds;
    }

   public static TimerModel
    findDifference(String start_date,
                   String end_date)
    {

        // SimpleDateFormat converts the
        // string format to date object
       // end_date = "2021-10-21 00:00:00";
        SimpleDateFormat sdf
                = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");

        // Try Block
        try {

            // parse method is used to parse
            // the text from a string to
            // produce the date
            Date date = new Date();


            Date d1 = sdf.parse(sdf.format(date));
            Date d2 = sdf.parse(end_date);

            // Calucalte time difference
            // in milliseconds
            long difference_In_Time
                    = d2.getTime() - d1.getTime();

            // Calucalte time difference in
            // seconds, minutes, hours, years,
            // and days
            long difference_In_Seconds
                    = (difference_In_Time
                    / 1000);

            long difference_In_Minutes
                    = (difference_In_Time
                    / (1000 * 60));

            long difference_In_Hours
                    = (difference_In_Time
                    / (1000 * 60 * 60));

            long difference_In_Years
                    = (difference_In_Time
                    / (1000l * 60 * 60 * 24 * 365));

            long difference_In_Days
                    = (difference_In_Time
                    / (1000 * 60 * 60 * 24));

            // Print the date difference in
            // years, in days, in hours, in
            // minutes, and in seconds

            System.out.print(
                    "Difference "
                            + "between two dates is: ");

            System.out.println(
                    difference_In_Years
                            + " years, "
                            + difference_In_Days
                            + " days, "
                            + difference_In_Hours
                            + " hours, "
                            + difference_In_Minutes
                            + " minutes, "
                            + difference_In_Seconds
                            + " seconds");

            return new TimerModel(difference_In_Time,difference_In_Seconds,difference_In_Minutes,difference_In_Hours,difference_In_Days);
        }

        // Catch the Exception
        catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
