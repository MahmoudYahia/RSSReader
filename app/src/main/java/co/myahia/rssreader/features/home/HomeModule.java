package co.myahia.rssreader.features.home;

import co.myahia.rssreader.data.manager.DataManager;
import co.myahia.rssreader.features.home.HomeContract.Data;
import co.myahia.rssreader.features.home.HomeContract.View;
import dagger.Module;
import dagger.Provides;

@Module
public class HomeModule {
    private View mView;

    public HomeModule(View mView) {
        this.mView = mView;
    }

    @Provides
    public View providesHomeView() {
        return this.mView;
    }

    @Provides
    public Data providesHomeData(DataManager manager) {
        return manager;
    }
}
