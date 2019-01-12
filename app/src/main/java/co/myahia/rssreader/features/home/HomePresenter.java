package co.myahia.rssreader.features.home;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import co.myahia.rssreader.data.remote.model.ApiArticle;
import co.myahia.rssreader.data.remote.model.NewsProvider;
import co.myahia.rssreader.data.remote.model.enumes.CategoryType;
import co.myahia.rssreader.features.home.HomeContract.Presenter;
import co.myahia.rssreader.features.home.HomeContract.View;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HomePresenter implements Presenter {
    private HashMap<String, List<ApiArticle>> articlesMap;
    private HashMap<String, List<ApiArticle>> tempArticles;
    private List<String> defRes;
    private HomeContract.Data mHomeData;

    private CompositeDisposable mDisposable;
    private View mHomeView;

    @Inject
    public HomePresenter(View view, HomeContract.Data data) {

        mHomeView = view;
        mHomeData = data;
        articlesMap = new HashMap<>();
       // tempArticles = new HashMap<>();
        defRes = new ArrayList<>();
        mDisposable = new CompositeDisposable();
        this.defRes.add("bbc-news");
        this.defRes.add("mashable");
        this.defRes.add("cnn");
    }

    public void onStart() {
        if (articlesMap.size() == 0) {
            getArticles();
            counter();
        }

    }

    private void getArticles() {
        getArticleFromSources(defRes);
    }


    public void onStop() {
        mDisposable.clear();
    }


    private void classifyArticles(List<ApiArticle> list) {
        Observable.just(list)
                .flatMapIterable(apiArticles -> apiArticles)
                .filter(apiArticle -> apiArticle.getSource().getId() != null)
                .toList()
                .subscribe(apiArticles -> {
                    if (apiArticles.size() > 0)
                        if (articlesMap.containsKey(apiArticles.get(0).getSource().getId())) {

                            articlesMap.put(apiArticles.get(0).getSource().getId(), apiArticles);
                        } else {
                            articlesMap.put(apiArticles.get(0).getSource().getId(), apiArticles);
                        }
                });

        setAdaptersData(articlesMap);
    }


    private void setAdaptersData(HashMap map) {
        mHomeView.setSwipeRefreshing(false);
        mHomeView.setAdapterDate(map);
    }

    @Inject
    public void setViewPresenter() {
        this.mHomeView.setPresenter(this);
    }

    public void onArticleClicked(ApiArticle apiArticle) {
        mHomeView.navigateToArticleDetails(apiArticle);
    }

    @Override
    public void onAddSourceClicked() {
        mHomeView.showTopHeadlinesLayout(false);
        mHomeView.showCategoryList(true);
    }

    public void onRemoveProvider(int pos, String articleID) {
        if (defRes.remove(articleID)) {
            articlesMap.remove(articleID);
        }
    }

    public void onSwipeToRefresh() {
        articlesMap.keySet().removeAll(articlesMap.keySet());
        mHomeView.setSwipeRefreshing(true);
        getArticles();
    }

    @Override
    public void onCategoryListBackClicked() {
        mHomeView.showCategoryList(false);
        mHomeView.showTopHeadlinesLayout(true);
    }

    @Override
    public void onCategoryClicked(CategoryType type) {
        getSourcesList(type);
        mHomeView.showCategoryList(false);
        mHomeView.showCategorySources(true);
    }

    @Override
    public void onSourcesOkBtnClicked() {

        List<String> selected = new ArrayList<>();
        for (NewsProvider provider : mHomeView.getSelectedNewsSourced()) {
            selected.add(provider.getId());
        }
        defRes.addAll(selected);
        getArticleFromSources(selected);

        mHomeView.showCategoryList(false);
        mHomeView.showCategorySources(false);
        mHomeView.showTopHeadlinesLayout(true);

    }

    private void getArticleFromSources(List<String> defRes) {

        mDisposable.add(mHomeData.getArticlesList(defRes, mHomeView.getViewContext())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(apiArticles -> {
                    if (mHomeView.isActive()) {
                        Log.i("articles", apiArticles.size() + "");
                        // classifyArticles(apiArticles);
                        classifyArticles(apiArticles);
                    }

                }, throwable -> {
                    Log.i("articles", throwable.toString());
                }));

    }

    private void getSourcesList(CategoryType type) {
        mDisposable.add(mHomeData.getCategoryProviders(type)
                .flatMapIterable(providers -> providers)
                .filter(newsProvider -> !defRes.contains(newsProvider.getId()))
                .toList()
                .subscribe(providers -> {
                    if (mHomeView.isActive()) {
                        mHomeView.setCategorySourceList(providers, type);
                        mHomeView.showCategoryList(false);
                        mHomeView.showCategorySources(true);
                    }
                }, throwable -> {
                }));
    }

    private void counter() {
        Completable.timer(10, TimeUnit.MINUTES)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        getArticles();
                        counter();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }
}

/*
    private void classifyArticles(List<ApiArticle> list) {
        for (ApiArticle apiArticle : list) {
            if (apiArticle.getUrl() != null) {
                if (!(apiArticle.getSource() == null || apiArticle.getSource().getId() == null)) {
                    if (articlesMap.containsKey(apiArticle.getSource().getId())) {
                        articlesMap.get(apiArticle.getSource().getId()).add(apiArticle);

                    } else {
                        articlesMap.put(apiArticle.getSource().getId(), new ArrayList<>());
                        articlesMap.get(apiArticle.getSource().getId()).add(apiArticle);
                    }
                }
            }

        }
        setAdaptersData(articlesMap);
    }

 */