package co.myahia.rssreader.data.manager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import co.myahia.rssreader.data.local.ArticleDB;
import co.myahia.rssreader.data.local.ArticleDao;
import co.myahia.rssreader.data.local.SourceDB;
import co.myahia.rssreader.data.remote.RSSApi;
import co.myahia.rssreader.data.remote.model.ApiArticle;
import co.myahia.rssreader.data.remote.model.NewsProvider;
import co.myahia.rssreader.data.remote.model.Source;
import co.myahia.rssreader.data.remote.model.enumes.CategoryType;
import co.myahia.rssreader.data.remote.response.GetArticlesResponse;
import co.myahia.rssreader.features.home.HomeContract;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DataManager implements HomeContract.Data {
    private final String API_KEY = "4bb7ed87426c4ea0aea1c3ef459c20a7";
    private RSSApi mRssApi;
    private String lang = "en";
    private ArticleDao mArticleDao;

    @Inject
    public DataManager(RSSApi rssApi, ArticleDao articleDao) {
        this.mRssApi = rssApi;
        this.mArticleDao = articleDao;
        checkDefaults();
    }

    private void checkDefaults() {
        getSourcesIDList().subscribe(list -> {
            if (list.size() == 0) insertDefaultsSourcse();
        });
    }

    @Override
    public Observable<List<ApiArticle>> getArticlesList(List<String> defRes, Context context) {
        //  Log.i("isInternet", isInternetAvailable() + "");

        if (isInternetOn(context))
            return Observable.just(defRes)
                    .flatMapIterable(list -> list)
                    .doOnNext(this::deleteBySourceFromDB)
                    .flatMap(s -> mRssApi.getArticles(API_KEY, s).map(GetArticlesResponse::getApiArticles))
                    .doOnNext(this::saveArticlesIntoLocalDB);
        else {
            return Observable.just(defRes)
                    .subscribeOn(Schedulers.io())
                    .flatMapIterable(list -> list)
                    .map(s -> mArticleDao.getArticlesBySource(s)).map(this::transfromLocalToApi);
            //  return getArticlesFromDB().map(list -> transfromLocalToApi(list));
        }
    }


//    @Override
//    public Observable<List<ApiArticle>> getArticlesList(List<String> res) {
//        return null;
//    }

    @Override
    public Observable<List<NewsProvider>> getCategoryProviders(CategoryType type) {

        return mRssApi.getProviders(API_KEY, lang, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(resp -> resp.getSourcesProvider());

    }

    @Override
    public Observable<List<ArticleDB>> getArticlesFromDB() {
        return Observable.just(mArticleDao)
                .subscribeOn(Schedulers.io())
                .map(articleDao -> articleDao.getArticlesList());
    }

    @Override
    public Observable<List<SourceDB>> getSourcesIDList() {

        return Observable.just(mArticleDao)
                .subscribeOn(Schedulers.io())
                .map(articleDao -> articleDao.getAllSources());
    }

    @Override
    public void removeSourceFromDB(String sourceID) {
        deleteBySourceFromDB(sourceID);
    }

    private void deleteBySourceFromDB(String sourceID) {
        Observable.just(sourceID)
                .subscribeOn(Schedulers.io())
                .subscribe(s -> mArticleDao.deleteBySourceId(s));

    }

    private void saveArticlesIntoLocalDB(List<ApiArticle> list) {
        Log.i("klll", "" + list.size());
        Observable.just(list)
                .subscribeOn(Schedulers.io())
                .subscribe(list1 -> mArticleDao.insertAll(transformArticleObjects(list1)));
    }


    private List<ArticleDB> transformArticleObjects(List<ApiArticle> apiArticles) {

        ArrayList<ArticleDB> articleDBS = new ArrayList<>();

        for (ApiArticle apiArticle : apiArticles) {
            ArticleDB articleDB = new ArticleDB();
            articleDB.setAuthor(apiArticle.getAuthor());
            articleDB.setContent(apiArticle.getContent());
            articleDB.setDescription(apiArticle.getDescription());
            articleDB.setPublishedAt(apiArticle.getPublishedAt());
            articleDB.setSourceName(apiArticle.getSource().getName());
            articleDB.setSourceID(apiArticle.getSource().getId());
            articleDB.setTitle(apiArticle.getTitle());
            articleDB.setUrl(apiArticle.getUrl());
            articleDB.setUrlToImage(apiArticle.getUrlToImage());
            articleDBS.add(articleDB);
        }

        return articleDBS;
    }

    private List<ApiArticle> transfromLocalToApi(List<ArticleDB> list) {
        List<ApiArticle> apiArticles = new ArrayList<>();

        for (ArticleDB articleDB : list) {
            ApiArticle apiArticle = new ApiArticle();
            apiArticle.setAuthor(articleDB.getAuthor());
            apiArticle.setContent(articleDB.getContent());
            apiArticle.setDescription(articleDB.getDescription());
            apiArticle.setPublishedAt(articleDB.getPublishedAt());
            apiArticle.setTitle(articleDB.getTitle());
            apiArticle.setUrl(articleDB.getUrl());
            apiArticle.setUrlToImage(articleDB.getUrlToImage());
            apiArticle.setSource(new Source(articleDB.getSourceID(), articleDB.getSourceName()));

            apiArticles.add(apiArticle);
        }
        return apiArticles;
    }

    private void insertDefaultsSourcse() {

        List<SourceDB> sourceDBS = new ArrayList<>();
        SourceDB sourceDB1 = new SourceDB();
        sourceDB1.setSourceID("bbc-news");
        sourceDB1.setSourceName("BBC NEWS");

        SourceDB sourceDB2 = new SourceDB();
        sourceDB1.setSourceID("mashable");
        sourceDB1.setSourceName("MASHABLE");

        SourceDB sourceDB3 = new SourceDB();
        sourceDB1.setSourceID("cnn");
        sourceDB1.setSourceName("CNN");

        sourceDBS.add(sourceDB1);
        sourceDBS.add(sourceDB2);
        sourceDBS.add(sourceDB3);

        Observable.just(sourceDBS)
                .subscribeOn(Schedulers.io())
                .subscribe(articleDao -> mArticleDao.insertSources(sourceDBS));
    }

    public boolean isInternetOn(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}