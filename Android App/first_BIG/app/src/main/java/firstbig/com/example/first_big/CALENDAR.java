package firstbig.com.example.first_big;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by 짹짹 on 2014-08-20.
 */
public class CALENDAR {

    private Calendar Today;
    private Calendar Cal;

    public CALENDAR(){
        Today = Calendar.getInstance();
        Cal = new GregorianCalendar();
    }

    public int getNowYear(){
        return Today.get(Calendar.YEAR);
    }

    public int getNowMonth(){
        return Today.get(Calendar.MONTH);
    }

    public int getNowDate(){
        return Today.get(Calendar.DATE);
    }

    public int getDayOfMonth(int Year, int Month){
        int DayOfMonth = 0;

        Cal.set(Calendar.YEAR, Year);
        Cal.set(Calendar.MONTH, Month);

        for(int i = Cal.getMinimum(Calendar.DAY_OF_MONTH) ; i <= Cal.getMaximum(Calendar.DAY_OF_MONTH) ; i++){

            Cal.set(Calendar.DATE, i);
            if(Cal.get(Calendar.MONTH) != Month){
                break;
            }

            DayOfMonth++;
        }

        return DayOfMonth;
    }
}
