package co.myahia.rssreader.features;

import co.myahia.rssreader.data.manager.DataManager;
import co.myahia.rssreader.data.remote.RSSApi;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module
public class AppModule {
    private RSSApp rssApp;

    public AppModule(RSSApp app) {
        this.rssApp = app;
    }

    @Singleton
    @Provides
    public RSSApp provideRssApp() {
        return this.rssApp;
    }

    @Singleton
    @Provides
    public DataManager providesDataManager(RSSApi api) {
        return new DataManager(api);
    }
}
