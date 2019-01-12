package co.myahia.rssreader.features.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.myahia.rssreader.R;
import co.myahia.rssreader.data.local.ArticleDao;
import co.myahia.rssreader.data.local.LocalDatabase;
import co.myahia.rssreader.data.remote.model.ApiArticle;
import co.myahia.rssreader.data.remote.model.NewsProvider;
import co.myahia.rssreader.data.remote.model.enumes.CategoryType;
import co.myahia.rssreader.features.detail.DetailsActivity;
import co.myahia.rssreader.features.home.HomeContract.Presenter;
import io.reactivex.Observable;

public class HomeFragment extends Fragment implements HomeContract.View,
        OnRefreshListener,
        ProviderAdapter.ProvidersListener {

    private int maxX;
    private int maxY;
    private Presenter mPresenter;
    private ProviderAdapter providerAdapter;

    @BindView(R.id.providers_recycler)
    RecyclerView newsHeadlinesRecycler;

    @BindView(R.id.home_swipe_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.category_list_content_frame)
    FrameLayout categoryListFrame;
    @BindView(R.id.category_sources_content_frame)
    FrameLayout categorySourcesFrame;

    @BindView(R.id.category_sources_recycler)
    RecyclerView categorySourcesRecycler;
    @BindView(R.id.category_sources_title_tv)
    TextView categorySourcesTitleTv;

    private SourcesAdapter sourcesAdapter;


    public static HomeFragment getInstance() {
        return new HomeFragment();
    }

    public void onResume() {
        super.onResume();
        this.mPresenter.onStart();
    }

    @Nullable
    public android.view.View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment_layout, container, false);
        ButterKnife.bind(this, view);
        newsHeadlinesRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        newsHeadlinesRecycler.setHasFixedSize(true);
        providerAdapter = new ProviderAdapter(this);
        newsHeadlinesRecycler.setAdapter(providerAdapter);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        sourcesAdapter = new SourcesAdapter();
        categorySourcesRecycler.setAdapter(sourcesAdapter);
        categorySourcesRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        categorySourcesRecycler.setHasFixedSize(true);
        Log.i("fddc0", "X >>" + categoryListFrame.getX() + " Y >>" + categoryListFrame.getY());

        displaySettings();
        //initTheLayoutPos();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.onStart();
    }



    private void displaySettings() {
        Display mdisp = getActivity().getWindowManager().getDefaultDisplay();
        Point mdispSize = new Point();
        mdisp.getSize(mdispSize);
        this.maxX = mdispSize.x;
        this.maxY = mdispSize.y;
    }

    private void initTheLayoutPos() {

        // bottom
        categoryListFrame.setY(maxY);
        categoryListFrame.setX(0);
        // right
        categorySourcesFrame.setY(0);
        categorySourcesFrame.setX(maxX);
    }

    @OnClick(R.id.home_add_provider_btn)
    public void onAddProviderClicked() {
        mPresenter.onAddSourceClicked();
    }

    @OnClick(R.id.category_list_cancel_btn)
    public void onCancelClicked() {
        mPresenter.onCategoryListBackClicked();
    }

    @OnClick(R.id.category_list_back_btn)
    public void onCategoryBackClicked() {
        mPresenter.onCategoryListBackClicked();
    }

    @OnClick(R.id.category_business_btn)
    public void onBusinessClicked() {
        mPresenter.onCategoryClicked(CategoryType.BUSINESS);
    }

    @OnClick(R.id.category_technology_btn)
    public void onTechClicked() {
        mPresenter.onCategoryClicked(CategoryType.TECHNOLOGY);
    }

    @OnClick(R.id.category_general_btn)
    public void onGeneralClicked() {
        mPresenter.onCategoryClicked(CategoryType.GENERAL);
    }

    @OnClick(R.id.category_health_btn)
    public void onHealthClicked() {
        mPresenter.onCategoryClicked(CategoryType.HEALTH);
    }

    @OnClick(R.id.category_entertainment_btn)
    public void onEntertainmentClicked() {
        mPresenter.onCategoryClicked(CategoryType.ENTERTAINMENT);
    }

    @OnClick(R.id.category_sports_btn)
    public void onSportsClicked() {
        mPresenter.onCategoryClicked(CategoryType.SPORTS);
    }

    @OnClick(R.id.category_sources_back_btn)
    public void onSourcesBackClicked() {
        mPresenter.onSourcesBackClicked();
    }

    @OnClick(R.id.category_sources_ok_btn)
    public void onSourcedOkClicked() {
        mPresenter.onSourcesOkBtnClicked();
    }

    public void setPresenter(Presenter presenter) {
        this.mPresenter = presenter;
    }

    public void showLoading(boolean isShowing) {
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    public void setAdapterDate(HashMap<String, List<ApiArticle>> listHashMap) {
        this.providerAdapter.setItemList(listHashMap);
    }

    public void setSwipeRefreshing(boolean refresh) {
        this.swipeRefreshLayout.setRefreshing(refresh);
    }

    @Override
    public void showTopHeadlinesLayout(boolean isShowing) {
        if (isShowing) {
            swipeRefreshLayout.animate().translationY(0).setDuration(200);
        } else {
            swipeRefreshLayout.animate().translationY(-maxY).setDuration(200);
        }
    }

    @Override
    public void showCategoryList(boolean isShowing) {
        categoryListFrame.setVisibility(isShowing ? View.VISIBLE : View.GONE);
    }



    @Override
    public void showCategorySources(boolean isShowing) {
        categorySourcesFrame.setVisibility(isShowing ? View.VISIBLE : View.GONE);
//        if (isShowing) {
//            categorySourcesFrame.setVisibility(View.VISIBLE);
//            categorySourcesFrame.setX(categorySourcesFrame.getWidth());
//            categorySourcesFrame.animate().translationX(0).setDuration(200);
//        } else {
//            categorySourcesFrame.animate()
//                    .translationX(categorySourcesFrame.getWidth())
//                    .setDuration(200);
//        }


    }

    @Override
    public void setCategorySourceList(List<NewsProvider> list, CategoryType type) {
        sourcesAdapter.setNewsProviderList(list);
        categorySourcesTitleTv.setText(String.valueOf(type));
    }

    @Override
    public List<NewsProvider> getSelectedNewsSourced() {
        return sourcesAdapter.getSelectedPositions();
    }

    @Override
    public Context getViewContext() {
        return getContext();
    }

    public void navigateToArticleDetails(ApiArticle article) {
        Intent intent = new Intent(getContext(), DetailsActivity.class);
        intent.putExtra("apiArticle", article);
        startActivity(intent);
    }

    public void removeProviderFromList(int pos) {
    }

    public void onStop() {
        super.onStop();
        this.mPresenter.onStop();
    }

    public void onArticleClicked(ApiArticle apiArticle) {
        this.mPresenter.onArticleClicked(apiArticle);
    }

    public void onRemoveProviderClicked(int pos, String providerID) {
        this.mPresenter.onRemoveProvider(pos, providerID);
    }

    public void onRefresh() {
        this.mPresenter.onSwipeToRefresh();
    }

    private void swipeTop(ViewGroup viewGroup){
    }
    private void swipeBottom(){

    }
    private void SwipeRight(){

    }
    private void swipeLeft(ViewGroup viewGroup) {
        if (true) {
            // swiping right show
            if (categoryListFrame.getX() < 0) {
                categoryListFrame.setVisibility(View.VISIBLE);
                categoryListFrame.animate().translationX(0).setDuration(200);
            } else {
                //swiping top show
                categoryListFrame.setVisibility(View.VISIBLE);
                categoryListFrame.animate().translationY(0).setDuration(200);
            }
        } else { // hiding
            // swiping left
            if (categoryListFrame.getX() == 0 && categoryListFrame.getY() == 0) {
                categoryListFrame.animate().translationX(-maxX).setDuration(200);
            } else if (categoryListFrame.getX() < 0 && categoryListFrame.getY() == 0) {
                categoryListFrame.animate().translationX(0).setDuration(200);
            }


        }
    }




}
