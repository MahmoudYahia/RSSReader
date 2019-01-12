package co.myahia.rssreader.features.home;

import android.content.Context;

import java.util.HashMap;
import java.util.List;

import co.myahia.rssreader.data.local.SourceDB;
import co.myahia.rssreader.data.remote.model.ApiArticle;
import co.myahia.rssreader.data.remote.model.NewsProvider;
import co.myahia.rssreader.data.remote.model.Source;
import co.myahia.rssreader.data.remote.model.enumes.CategoryType;
import co.myahia.rssreader.utils.BaseContract;
import io.reactivex.Observable;

public interface HomeContract {
    public interface View extends BaseContract.View<Presenter> {

        void navigateToArticleDetails(ApiArticle apiArticle);

        void removeProviderFromList(int i);

        void setAdapterDate(HashMap<String, List<ApiArticle>> hashMap);

        void setSwipeRefreshing(boolean isRefreshing);

        void showTopHeadlinesLayout(boolean isShowing);

        void showCategoryList(boolean isShowing);

        void showCategorySources(boolean isShowing);

        void setCategorySourceList(List<NewsProvider> list, CategoryType type);

        List<NewsProvider> getSelectedNewsSourced();
        Context getViewContext();
    }


    public interface Presenter extends BaseContract.Presenter {
        void onArticleClicked(ApiArticle apiArticle);

        void onAddSourceClicked();

        void onRemoveProvider(int pos, String str);

        void onSwipeToRefresh();

        void onCategoryListBackClicked();

        void onCategoryClicked(CategoryType type);

        void onSourcesOkBtnClicked();

        void onSourcesBackClicked();

    }

    public interface Data {

        Observable<List<ApiArticle>> getArticlesList( Context context);

        Observable<List<NewsProvider>> getCategoryProviders(CategoryType type);


        Observable<List<SourceDB>> getSourcesIDList();

        void removeSourceFromDB(String sourceID);

        void insertSourcesIntoDB(List<Source> sources);

    }

}
