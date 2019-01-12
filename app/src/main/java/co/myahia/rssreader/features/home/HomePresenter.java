package co.myahia.rssreader.features.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import co.myahia.rssreader.data.remote.model.ApiArticle;
import co.myahia.rssreader.data.remote.model.NewsProvider;
import co.myahia.rssreader.data.remote.model.Source;
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
    private List<String> defRes;
    private HomeContract.Data mHomeData;

    private CompositeDisposable mDisposable;
    private View mHomeView;

    @Inject
    public HomePresenter(View view, HomeContract.Data data) {

        mHomeView = view;
        mHomeData = data;
        articlesMap = new HashMap<>();
        defRes = new ArrayList<>();
        mDisposable = new CompositeDisposable();
        getSourcesListIds();
    }

    private void getSourcesListIds() {
        mDisposable.add(mHomeData.getSourcesIDList()
                .flatMapIterable(list -> list)
                .map(sourceDB -> defRes.add(sourceDB.getSourceID()))
                .subscribe());

    }
    public void onStart() {
        if (articlesMap.size() == 0) {
            getArticles();
            counter();
        }

    }

    private void getArticles() {
        getArticleFromSources();
    }


    public void onStop() {
        mDisposable.clear();
    }


    private void classifyArticles(List<ApiArticle> list) {
        mDisposable.add(Observable.just(list)
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
                }));

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

    public void onRemoveProvider(int pos, String providerID) {
        articlesMap.remove(providerID);
        mHomeData.removeSourceFromDB(providerID);
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

        List<Source> selected = new ArrayList<>();
        for (NewsProvider provider : mHomeView.getSelectedNewsSourced()) {
            Source source=new Source();
            source.setId(provider.getId());
            source.setName(provider.getName());
            selected.add(source);
        }
        mHomeData.insertSourcesIntoDB(selected);
        onSwipeToRefresh();

        mHomeView.showCategorySources(false);
        mHomeView.showCategoryList(false);
        mHomeView.showTopHeadlinesLayout(true);

    }

    @Override
    public void onSourcesBackClicked() {
        mHomeView.showCategoryList(true);
        mHomeView.showCategorySources(false);

    }

    private void getArticleFromSources() {

        mDisposable.add(mHomeData.getArticlesList(mHomeView.getViewContext())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(apiArticles -> {
                    if (mHomeView.isActive()) {
                        // classifyArticles(apiArticles);
                        classifyArticles(apiArticles);
                    }

                }, throwable -> {
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

    private void filterRepeatedSources(List<NewsProvider> newsProviders, CategoryType type) {
        ArrayList<NewsProvider> filtedList = new ArrayList<>();
        mDisposable.add(mHomeData.getSourcesIDList()
                .flatMapIterable(list -> list)
                .subscribe(sourcesDB -> {
                    for (NewsProvider newsProvider : newsProviders) {
                        if (!newsProvider.getId().equals(sourcesDB.getSourceID()))
                            filtedList.add(newsProvider);
                    }
                    setNewsProvidersList(filtedList, type);
                }));

    }

    private void setNewsProvidersList(ArrayList<NewsProvider> list, CategoryType type) {

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
