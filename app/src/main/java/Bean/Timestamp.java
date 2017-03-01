package Bean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Timestamp {

    long seconds;
    int nanos;

    public Timestamp() {
    }

    public Timestamp(long seconds, int nanos) {
        this.seconds = seconds;
        this.nanos = nanos;
    }

    public long getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public int getNanos() {
        return nanos;
    }

    public void setNanos(int nanos) {
        this.nanos = nanos;
    }

    @Override
    public String toString() {
        return "Timestamp{" +
                "seconds=" + seconds +
                ", nanos=" + nanos +
                '}';
    }

    public String getDate(){
//        Long time=newLong(445555555);
        SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd");
        String date = format.format(seconds);
        return date;
    }

    public long getTimestamp(String time) throws ParseException {
        //        String time="1970-01-06 11:45:55";
        SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse(time);
        return date.getTime()/1000; //getTime()返回的是毫秒级别，除以1000变成秒级别并返回，与Unix时间统一
    }
}
