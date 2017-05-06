package bean.red.greenboard.parser;

/**
 * Created by Shivam Seth on 4/29/2016.
 */




import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Shivam Seth on 4/26/2016.
 */
public class GeneralParser {

    public static String parseGetResponseforResult(JSONObject jsonAnnouncement) {
        String result="";
        try {
           result = jsonAnnouncement.getString("result");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return  result;
    }
}
