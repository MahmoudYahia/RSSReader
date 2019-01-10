package co.myahia.rssreader.features.detail;

import co.myahia.rssreader.data.remote.model.ApiArticle;
import co.myahia.rssreader.features.detail.DetailContract.Presenter;
import co.myahia.rssreader.features.detail.DetailContract.View;
import javax.inject.Inject;

public class DetailPresenter implements Presenter {
    private ApiArticle mApiArticle;
    private View mDetailView;

    @Inject
    public DetailPresenter(View mDetailView, ApiArticle apiArticle) {
        this.mDetailView = mDetailView;
        this.mApiArticle = apiArticle;
    }

    public void onViewAllArticleClicked() {
        if (this.mApiArticle.getUrl() != null) {
            this.mDetailView.startToLoadWepPage(this.mApiArticle.getUrl());
        }
        this.mDetailView.showDetailLayout(false);
        this.mDetailView.showWebViewLayout(true);
    }

    public void onWebViewBackClicked() {
        this.mDetailView.showWebViewLayout(false);
        this.mDetailView.showDetailLayout(true);
    }

    public void onStart() {
        if (this.mApiArticle != null) {
            setArticleDetails();
        }
    }

    private void setArticleDetails() {
        this.mDetailView.setArticlePhoto(this.mApiArticle.getUrlToImage());
        this.mDetailView.setArticleDate(this.mApiArticle.getPublishedAt());
        this.mDetailView.setAuthorName(this.mApiArticle.getAuthor());
        this.mDetailView.setArticleTitle(this.mApiArticle.getTitle());
        this.mDetailView.setArticleDescription(this.mApiArticle.getContent());
        this.mDetailView.setSourceName(this.mApiArticle.getSource().getName());
    }

    public void onStop() {
    }

    @Inject
    public void setViewPresenter() {
        this.mDetailView.setPresenter(this);
    }
}
