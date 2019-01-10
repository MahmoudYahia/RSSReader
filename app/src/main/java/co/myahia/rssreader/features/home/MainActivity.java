package co.myahia.rssreader.features.home;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import co.myahia.rssreader.R;
import co.myahia.rssreader.features.RSSApp;
import co.myahia.rssreader.utils.ActivityUtils;

public class MainActivity extends AppCompatActivity {

    @Inject
    HomePresenter homePresenter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HomeFragment fragment = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);

        if (fragment == null) {
            fragment = HomeFragment.getInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.content_frame);
        }

        DaggerHomeComponent.builder()
                .appComponent(RSSApp.getApplicationComponent())
                .homeModule(new HomeModule(fragment))
                .build().inject(this);

    }
}
