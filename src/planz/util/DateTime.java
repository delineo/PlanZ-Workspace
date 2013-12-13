package planz.util;

import java.text.SimpleDateFormat;
import java.util.*;

public class DateTime
{
    public long getTime()           { return _time; }
    public void setTime(long time)  { _time = time; }
    private long _time = 0; // milliseconds
    
    public DateTime(long time)
    {
        _time = time;
    }

    public DateTime(Date date)
    {
        _time = date.getTime();
    }

    public DateTime(int yyyy, int mm, int dd)
    {
        _time = (new Date(yyyy, mm, dd)).getTime();
    }
    
    public DateTime() { }

    public Calendar getCalendar()
    {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(_time);
        return cal;
    }
    public int  getYear()
    {
        Calendar cal = getCalendar();
        return cal.get(Calendar.YEAR);
    }
    public void setYear(int year)   
    {
        Calendar cal = getCalendar();
        cal.set(Calendar.YEAR, year);
        _time = cal.getTimeInMillis();
    }
    public int  getMonth()
    {
        Calendar cal = getCalendar();
        return cal.get(Calendar.MONTH)+1;
    }
    public void setMonth(int month)
    {
        Calendar cal = getCalendar();
        cal.set(Calendar.MONTH, month);
        _time = cal.getTimeInMillis();
    }
    public int getDay()
    {
        Calendar cal = getCalendar();
        return cal.get(Calendar.DAY_OF_MONTH);
    }
    public void setDay(int day)
    {
        Calendar cal = getCalendar();
        cal.set(Calendar.DAY_OF_MONTH, day);
        _time = cal.getTimeInMillis();
    }

    public DateTime add(DateTime time)
    {
        return new DateTime(_time + time.getTime());
    }
    
    public DateTime addYears(int years)
    {
        Calendar cal = getCalendar();
        cal.add(Calendar.YEAR, years);
        return new DateTime(cal.getTimeInMillis());        
    }

    public DateTime addMonths(int months)
    {
        Calendar cal = getCalendar();
        cal.add(Calendar.MONTH, months);
        return new DateTime(cal.getTimeInMillis());        
    }
    
    public DateTime addDays(int days)
    {
        Calendar cal = getCalendar();
        cal.add(Calendar.DATE, days);
        return new DateTime(cal.getTimeInMillis());
    }

    public DateTime addHours(int hours)
    {
        Calendar cal = getCalendar();
        cal.add(Calendar.HOUR, hours);
        return new DateTime(cal.getTimeInMillis());
    }

    public DateTime addMinutes(int minutes)
    {
        Calendar cal = getCalendar();
        cal.add(Calendar.MINUTE, minutes);
        return new DateTime(cal.getTimeInMillis());
    }

    public DateTime addSeconds(int seconds)
    {
        Calendar cal = getCalendar();
        cal.add(Calendar.SECOND, seconds);
        return new DateTime(cal.getTimeInMillis());
    }

    public DateTime addMilliseconds(int milliseconds)
    {
        return new DateTime(_time + milliseconds);
    }
    
    public DateTime subtraction(DateTime time)
    {
        return new DateTime(_time - time.getTime());
    }

    public boolean between(DateTime start, DateTime end)
    {
        long time= _time;
        long pdt = time - start.getTime();
        long ndt = end.getTime() - time;
        
        return (pdt >= 0 && ndt >= 0);
    }

    public boolean after(DateTime when)
    {
        return (this._time > when._time);
    }

    public boolean after(Date when)
    {
        return (this._time > when.getTime());
    }

    public boolean before(DateTime when)
    {
        return (this._time < when._time);
    }

    public boolean before(Date when)
    {
        return (this._time < when.getTime());
    }

    public boolean equals(DateTime when)
    {
        return (this._time == when._time);
    }

    public String format(String format)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = new Date(_time);
        return sdf.format(date);
    }

    public Date   toDate()   { return new Date(this.getTime()); }
    public String toString(String format) { return format(format); }
    public String toString() { return format("yyyy-MM-dd HH:mm:ss"); }  // "yyyy-MM-dd HH:mm:ss.SSS"

    public static DateTime now()
    {
        return new DateTime(System.currentTimeMillis());
    }

    public static DateTime parse(String dateTime, String format)
    {
        DateTime retDate = null;

        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            retDate = new DateTime((sdf.parse(dateTime)).getTime());
        }
        catch(Exception ex) {}

        return retDate;
    }

    public DateTime getLunar()
    {   // 음력으로 변환
        return DateTime.now();
    }

    public DateTime getSolar()
    {   // 양력으로 변환
        return DateTime.now();
    }
}