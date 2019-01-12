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
            if (list.size() == 0) insertDefaultsSources();
        });
    }

    private void getProvidersId(Context context) {
        getSourcesIDList().flatMapIterable(list -> list)
                .map(sourceDB -> sourceDB.getSourceID())
                .doOnNext(this::deleteBySourceFromDB)
                .flatMap(s -> mRssApi.getArticles(API_KEY, s).map(GetArticlesResponse::getApiArticles))
                .doOnNext(this::saveArticlesIntoLocalDB);

    }
    @Override
    public Observable<List<ApiArticle>> getArticlesList( Context context) {
        //  Log.i("isInternet", isInternetAvailable() + "");
        if (isInternetOn(context))
            return getSourcesIDList()
                    .flatMapIterable(list -> list)
                    .map(sourceDB -> sourceDB.getSourceID())
                    .doOnNext(s -> Log.i("eerrhON", s))
                    .doOnNext(this::deleteBySourceFromDB)
                    .flatMap(s -> mRssApi.getArticles(API_KEY, s).map(GetArticlesResponse::getApiArticles))
                    .doOnNext(this::saveArticlesIntoLocalDB);
        else {
            return getSourcesIDList()
                    .flatMapIterable(list -> list)
                    .map(sourceDB -> sourceDB.getSourceID())
                    .doOnNext(s -> Log.i("eerrhLO", s))
                    .map(s -> mArticleDao.getArticlesBySource(s)).map(this::transformLocalToApi);
            //  return getArticlesFromDB().map(list -> transformLocalToApi(list));
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

//    private Observable<List<ArticleDB>> getArticlesFromDB() {
//        return Observable.just(mArticleDao)
//                .subscribeOn(Schedulers.io())
//                .map(articleDao -> articleDao.getArticlesList());
//    }

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

    @Override
    public void insertSourcesIntoDB(List<Source> sources) {
        insertNewsSourcesDB(transformApiSourceToLocal(sources));
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


    private void insertDefaultsSources() {

        List<SourceDB> sourceDBS = new ArrayList<>();
        SourceDB sourceDB1 = new SourceDB();
        sourceDB1.setSourceID("bbc-news");
        sourceDB1.setSourceName("BBC NEWS");

        SourceDB sourceDB2 = new SourceDB();
        sourceDB2.setSourceID("mashable");
        sourceDB2.setSourceName("MASHABLE");

        SourceDB sourceDB3 = new SourceDB();
        sourceDB3.setSourceID("cnn");
        sourceDB3.setSourceName("CNN");

        sourceDBS.add(sourceDB1);
        sourceDBS.add(sourceDB2);
        sourceDBS.add(sourceDB3);

        insertNewsSourcesDB(sourceDBS);
    }

    private void insertNewsSourcesDB(List<SourceDB> sourceDBS) {
        Observable.just(sourceDBS)
                .subscribeOn(Schedulers.io())
                .subscribe(articleDao -> mArticleDao.insertSources(sourceDBS));
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

    private List<ApiArticle> transformLocalToApi(List<ArticleDB> list) {
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

    private List<SourceDB> transformApiSourceToLocal(List<Source> list) {
        List<SourceDB> sourceDBList = new ArrayList<>();
        for (Source source : list) {
            SourceDB sourceDB = new SourceDB();
            sourceDB.setSourceID(source.getId());
            sourceDB.setSourceName(source.getName());
            sourceDBList.add(sourceDB);
        }
        return sourceDBList;
    }


    public boolean isInternetOn(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}