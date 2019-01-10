package co.myahia.rssreader.data.manager;

import java.util.List;

import javax.inject.Inject;

import co.myahia.rssreader.data.remote.RSSApi;
import co.myahia.rssreader.data.remote.model.ApiArticle;
import co.myahia.rssreader.data.remote.model.NewsProvider;
import co.myahia.rssreader.data.remote.model.enumes.CategoryType;
import co.myahia.rssreader.features.home.HomeContract;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DataManager implements HomeContract.Data {
    private final String API_KEY = "4bb7ed87426c4ea0aea1c3ef459c20a7";
    private RSSApi mRssApi;
    private String lang = "en";

    @Inject
    public DataManager(RSSApi rssApi) {
        this.mRssApi = rssApi;
    }


    @Override
    public Observable<List<NewsProvider>> getCategoryProviders(CategoryType type) {
        return mRssApi.getProviders(API_KEY, lang, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(resp -> resp.getSourcesProvider());

    }

    private void saveProvidersIntoLocalDB(List<NewsProvider> list) {
    }

    private void saveArticlesIntoLocalDB(List<ApiArticle> list) {
    }

    public Observable<List<ApiArticle>> getArticlesList(List<String> defRes) {
        return Observable.just(defRes)
                .flatMapIterable(list -> list)
                .flatMap(s -> mRssApi.getArticles(API_KEY, s)
                        .map(articlesResponse -> articlesResponse.getApiArticles()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


}
