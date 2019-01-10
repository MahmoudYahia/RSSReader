package co.myahia.rssreader.features.detail;

import co.myahia.rssreader.utils.BaseContract;

public interface DetailContract {

    public interface View extends BaseContract.View<Presenter> {
        void setArticleDate(String str);

        void setArticleDescription(String str);

        void setArticlePhoto(String str);

        void setArticleTitle(String str);

        void setAuthorName(String str);

        void setSourceName(String str);

        void showDetailLayout(boolean z);

        void showWebViewLayout(boolean z);

        void startToLoadWepPage(String str);
    }

    public interface Presenter extends BaseContract.Presenter {
        void onViewAllArticleClicked();

        void onWebViewBackClicked();
    }
}
