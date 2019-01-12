package co.myahia.rssreader.features;

import javax.inject.Singleton;

import co.myahia.rssreader.data.local.ArticleDao;
import co.myahia.rssreader.data.local.LocalDatabase;
import co.myahia.rssreader.data.manager.DataManager;
import co.myahia.rssreader.data.remote.NetworkModule;
import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, NetworkModule.class})
public interface AppComponent {

    DataManager dataManger();

    RSSApp rssApp();

    LocalDatabase localDatabase();

    ArticleDao articleDao();

    void inject(RSSApp rssApp);

}
