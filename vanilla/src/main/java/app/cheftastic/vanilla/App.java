package app.cheftastic.vanilla;

import android.app.Application;
import android.content.Context;

public class App extends Application {
    public static final int ARG_ERROR = -999;

    public static final String WEB_SERVICE_URL = "http://cheftastic-web-services.appspot.com/";
    public static final String WEB_SERVICE_PARAMS_PREFIX = "?";
    public static final String WEB_SERVICE_UPDATER = "cheftasticupdater";
    public static final String WEB_SERVICE_UPDATER_PARAM_LAST_UPDATED = "lastUpdate=";
    public static final String WEB_SERVICE_UPDATER_SEPARATOR = ";";


    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }
}
