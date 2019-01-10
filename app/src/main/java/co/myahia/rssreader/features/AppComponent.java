package co.myahia.rssreader.features;

import co.myahia.rssreader.data.manager.DataManager;
import co.myahia.rssreader.data.remote.NetworkModule;
import co.myahia.rssreader.data.remote.RSSApi;
import dagger.Component;
import javax.inject.Singleton;

@Singleton
@Component(modules = {AppModule.class, NetworkModule.class})
public interface AppComponent {

    DataManager dataManger();
    RSSApp rssApp();

    void inject(RSSApp rssApp);


}
