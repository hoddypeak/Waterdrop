package number.android.waterdrop.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by user on 10/15/2016.
 */
public class DateTime {

    /**
     *
     * @param mysql_datetime
     * @param custom_format
     * @return
     */
    public static String ServerFormatToCustom(String mysql_datetime, String custom_format){
        String formatted_datetime = null;
        //String strDate = "2013-05-15T10:00:00-0700";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(mysql_datetime);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(custom_format);
            formatted_datetime = simpleDateFormat.format(convertedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatted_datetime;
    }
}
