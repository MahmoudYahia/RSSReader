package co.myahia.rssreader.features;

import javax.inject.Singleton;

import co.myahia.rssreader.data.local.ArticleDao;
import co.myahia.rssreader.data.local.LocalDatabase;
import co.myahia.rssreader.data.manager.DataManager;
import co.myahia.rssreader.data.remote.RSSApi;
import dagger.Module;
import dagger.Provides;

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
    public DataManager providesDataManager(RSSApi api, ArticleDao articleDao) {
        return new DataManager(api, articleDao);
    }

    @Singleton
    @Provides
    public LocalDatabase providesLocalDatabase(RSSApp rssApp) {
        return LocalDatabase.getInstance(rssApp.getApplicationContext());
    }

    @Singleton
    @Provides
    public ArticleDao providesArticleDeo(LocalDatabase localDatabase) {
        return localDatabase.getArticleDao();
    }
}
