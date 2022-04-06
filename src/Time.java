public class Time {

    private final int hour;
    private final int min;
    private final int sec;

    Time(String hour, String min, String sec) {
        this.hour = Integer.parseInt(hour);
        this.min = Integer.parseInt(min);
        this.sec = Integer.parseInt(sec);
    }

    public static boolean isEqual(Time time1, Time time2)
    {
        if (time1 == null || time2 == null) return false;
        return time1.hour == time2.hour && time1.min == time2.min && time1.sec == time2.sec;
    }

    public static boolean isInvalid(Time time)
    {
        return time.hour >= 24 || time.min >= 60 || time.sec >= 60;
    }

    public static boolean isInvalidFormat(String time)
    {
        try
        {
            String[] timeSplit = time.split(":");
            Integer.parseInt(timeSplit[0]);
            Integer.parseInt(timeSplit[1]);
            Integer.parseInt(timeSplit[2]);
        } catch (Exception error)
        {
            return true;
        }

        return false;
    }

    @Override
    public String toString()
    {
        String hourStr = String.valueOf(hour).length() < 2? "0" + hour : String.valueOf(hour);
        String minStr = String.valueOf(min).length() < 2? "0" + min : String.valueOf(min);
        String secStr = String.valueOf(sec).length() < 2? "0" + sec : String.valueOf(sec);
        return hourStr + ":" + minStr + ":" + secStr;
    }
}
