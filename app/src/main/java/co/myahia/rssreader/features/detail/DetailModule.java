package co.myahia.rssreader.features.detail;

import co.myahia.rssreader.data.remote.model.ApiArticle;
import co.myahia.rssreader.features.detail.DetailContract.View;
import dagger.Module;
import dagger.Provides;

@Module
public class DetailModule {
    private ApiArticle mArticle;
    private View mDetailView;

    public DetailModule(View mDetailView, ApiArticle apiArticle) {
        this.mDetailView = mDetailView;
        this.mArticle = apiArticle;
    }

    @Provides
    public View providesDetailView() {
        return this.mDetailView;
    }

    @Provides
    public ApiArticle providesArticle() {
        return this.mArticle;
    }
}
