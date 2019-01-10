package co.myahia.rssreader.features;

import android.support.multidex.MultiDexApplication;
import co.myahia.rssreader.data.remote.NetworkModule;

public class RSSApp extends MultiDexApplication {
    private static AppComponent appComponent;

    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
        appComponent.inject(this);
    }

    public static AppComponent getApplicationComponent() {
        return appComponent;
    }
}
