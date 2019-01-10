package co.myahia.rssreader.features.detail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import co.myahia.rssreader.R;
import co.myahia.rssreader.data.remote.model.ApiArticle;
import co.myahia.rssreader.features.RSSApp;
import co.myahia.rssreader.utils.ActivityUtils;

public class DetailsActivity extends AppCompatActivity {

    @Inject
    DetailPresenter detailPresenter;

    protected void onCreate(Bundle savedInstanceState) {
        ApiArticle apiArticle;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        DetailFragment fragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (getIntent().getExtras() == null) {
            apiArticle = new ApiArticle();
        } else if (getIntent().getExtras().getSerializable("apiArticle") != null) {
            apiArticle = (ApiArticle) getIntent().getExtras().getSerializable("apiArticle");
        } else {
            apiArticle = new ApiArticle();
        }
        if (fragment == null) {
            fragment = DetailFragment.getInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.content_frame);
        }
        DaggerDetailComponent.builder()
                .appComponent(RSSApp.getApplicationComponent())
                .detailModule(new DetailModule(fragment, apiArticle))
                .build().inject(this);
    }
}
