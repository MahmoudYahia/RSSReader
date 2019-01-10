package co.myahia.rssreader.features.home;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import co.myahia.rssreader.data.remote.model.ApiArticle;
import co.myahia.rssreader.data.remote.model.NewsProvider;
import co.myahia.rssreader.data.remote.model.enumes.CategoryType;
import co.myahia.rssreader.features.home.HomeContract.Presenter;
import co.myahia.rssreader.features.home.HomeContract.View;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class HomePresenter implements Presenter {
    private HashMap<String, List<ApiArticle>> articlesMap;
    private List<String> defRes;
    private HomeContract.Data mHomeData;

    private CompositeDisposable mDisposable;
    private View mHomeView;

    @Inject
    public HomePresenter(View view, HomeContract.Data data) {
        mHomeView = view;
        mHomeData = data;
        articlesMap = new HashMap();
        defRes = new ArrayList();
        mDisposable = new CompositeDisposable();
        this.defRes.add("bbc-news");
        this.defRes.add("techcrunch");
        this.defRes.add("mashable");
        this.defRes.add("cnn");
    }

    public void onStart() {
        if (articlesMap.size() == 0) {
            getArticles();
        }
    }

    private void getArticles() {
        getArticleFromSourcse(defRes);
    }


    public void onStop() {
        mDisposable.clear();
    }

    private void classifyArticles(List<ApiArticle> list) {
        for (ApiArticle apiArticle : list) {
            if (apiArticle.getUrl() != null) {
                if (!(apiArticle.getSource() == null || apiArticle.getSource().getId() == null)) {
                    if (articlesMap.containsKey(apiArticle.getSource().getId())) {
                        articlesMap.get(apiArticle.getSource().getId()).add(apiArticle);
                    } else {
                        this.articlesMap.put(apiArticle.getSource().getId(), new ArrayList());
                        (articlesMap.get(apiArticle.getSource().getId())).add(apiArticle);
                    }
                }
            }

        }
        setAdaptersData(articlesMap);
    }

    private void setAdaptersData(HashMap map) {
        this.mHomeView.setSwipeRefreshing(false);
        this.mHomeView.setAdapterDate(map);
    }

    @Inject
    public void setViewPresenter() {
        this.mHomeView.setPresenter(this);
    }

    public void onArticleClicked(ApiArticle apiArticle) {
        this.mHomeView.navigateToArticleDetails(apiArticle);
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
            defRes.addAll(selected);
            getArticleFromSourcse(selected);
        }

        mHomeView.showCategoryList(false);
        mHomeView.showCategorySources(false);
        mHomeView.showTopHeadlinesLayout(true);

    }

    private void getArticleFromSourcse(List<String> defRes) {
        mDisposable.add(mHomeData.getArticlesList(defRes)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(apiArticles -> {
                    Log.i("articles", apiArticles.size() + "");
                    classifyArticles(apiArticles);
                }, throwable -> {

                }));
    }

    private void getSourcesList(CategoryType type) {

        mDisposable.add(mHomeData.getCategoryProviders(type)
                .flatMapIterable(providers -> providers)
                .filter(newsProvider -> !defRes.contains(newsProvider.getId()))
                .toList()
                .subscribe(providers -> {

                    mHomeView.setCategorySourceList(providers, type);
                    mHomeView.showCategoryList(false);
                    mHomeView.showCategorySources(true);
                    Log.i("tstProv", "" + providers.size());
                }, throwable -> {
                    Log.i("tstProvErr", throwable.toString());
                }));
    }
}
