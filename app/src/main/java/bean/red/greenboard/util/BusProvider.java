package bean.red.greenboard.util;

import com.squareup.otto.Bus;

/**
 * Created by Shivam Seth on 4/26/2016.
 */
public class BusProvider {
    private static final Bus BUS = new Bus();

    public static Bus getInstance() {
        return BUS;
    }

    private BusProvider() {
        // No instances.
    }
}
