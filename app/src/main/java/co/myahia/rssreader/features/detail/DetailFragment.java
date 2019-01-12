package co.myahia.rssreader.features.detail;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.myahia.rssreader.R;

public class DetailFragment extends Fragment implements DetailContract.View {
    @BindView(R.id.detail_article_date_tv)
    TextView articleData;
    @BindView(R.id.detail_article_desc_tv)
    TextView articleDescription;
    @BindView(R.id.detail_article_image)
    ImageView articlePhoto;
    @BindView(R.id.detail_article_item_source_tv)
    TextView articleSource;
    @BindView(R.id.detail_article_title_tv)
    TextView articleTitle;
    @BindView(R.id.detail_article_author_tv)
    TextView authorName;
    @BindView(R.id.details_content_layout)
    ViewGroup detailsContentLayout;

    @BindView(R.id.web_view)
    WebView webView;
    @BindView(R.id.web_view_content_layout)
    ViewGroup webViewContentFrame;



    private boolean isFirstLoad = true;
    private DetailContract.Presenter mPresenter;


    public static DetailFragment getInstance() {
        return new DetailFragment();
    }

    @Nullable
    public android.view.View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        android.view.View view = inflater.inflate(R.layout.detail_fragement_layout, container, false);
        ButterKnife.bind(this, view);
        setWebViewSettings();
        return view;
    }

    public void onStart() {
        super.onStart();
        this.mPresenter.onStart();
    }

    private void setWebViewSettings() {
        WebSettings webSettings = this.webView.getSettings();
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(true);
        webSettings.setSupportZoom(true);
        // webView.setScrollBarStyle(0);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.getSettings().setPluginState(PluginState.ON);
        webView.getSettings().setAllowFileAccess(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // Insert your code here
                view.loadUrl(url);
                return true;
            }
        });
    }

    @OnClick(R.id.detail_view_full_article)
    public void onViewFullArticleClicked() {
        this.mPresenter.onViewAllArticleClicked();
    }

    @OnClick(R.id.view_full_content_back)
    public void onWebViewBackClicked() {
        this.mPresenter.onWebViewBackClicked();
    }

    public void setArticlePhoto(String url) {
        Picasso.with(getContext()).load(url).into(this.articlePhoto);
    }

    public void setAuthorName(String name) {
        this.authorName.setText(name);
    }

    public void setArticleDate(String date) {
        this.articleData.setText(date);
    }

    public void setArticleTitle(String title) {
        this.articleTitle.setText(title);
    }

    public void setSourceName(String name) {
        this.articleSource.setText(name);
    }

    public void setArticleDescription(String description) {
        this.articleDescription.setText(description);
    }

    public void startToLoadWepPage(String url) {
        if (this.isFirstLoad) {
            this.webView.loadUrl(url);
        }
        this.isFirstLoad = false;
    }

    @Override
    public void shareToPocket(String url) {
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");

        if (getActivity() != null)
            if (getActivity().getPackageManager() != null) {
                PackageManager pm = getActivity().getPackageManager();
                try {
                    pm.getPackageInfo("com.ideashower.readitlater.pro", PackageManager.GET_ACTIVITIES);
                    intent.putExtra(Intent.EXTRA_TEXT, url);
                    startActivity(intent);
                } catch (PackageManager.NameNotFoundException e) {
                    Toast.makeText(getContext(), "Ohh Sorry ,GetPocket App not installed", Toast.LENGTH_LONG).show();
                }
            }
    }

    public void showDetailLayout(boolean isShowing) {
        if (isShowing) {
            detailsContentLayout.setX(detailsContentLayout.getWidth());
            detailsContentLayout.animate().translationX(0).setDuration(200);

        } else {
            detailsContentLayout.setX(0);
            detailsContentLayout.animate()
                    .translationX(detailsContentLayout.getWidth())
                    .setDuration(200);
        }

    }

    public void showWebViewLayout(boolean isShowing) {
        if (isShowing) {
            webViewContentFrame.setX(webViewContentFrame.getWidth());
            webViewContentFrame.setVisibility(View.VISIBLE);
            webViewContentFrame.animate().translationX(0f).setDuration(200);
            return;
        }
        this.webViewContentFrame.animate().translationX(webViewContentFrame.getWidth()).setDuration(200);
    }

    @Override
    public void setPresenter(DetailContract.Presenter presenter) {
        mPresenter = presenter;
    }


    public void showLoading(boolean isShowing) {
    }

    @Override
    public boolean isActive() {
        return false;
    }

    public void onStop() {
        super.onStop();
        mPresenter.onStop();
    }

    @OnClick(R.id.share_pocket_btn)
    public void onSharePocketClicked(){
        mPresenter.onSharePocketClicked();
    }

}
