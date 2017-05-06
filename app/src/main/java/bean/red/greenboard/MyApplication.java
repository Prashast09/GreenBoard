package bean.red.greenboard;
import android.app.Application;

public class MyApplication extends Application {
    private static MyApplication _instance;

    @Override
    public void onCreate() {
        super.onCreate();

        _instance = this;
    }

    public static MyApplication getInstance() {
        return _instance;
    }
}