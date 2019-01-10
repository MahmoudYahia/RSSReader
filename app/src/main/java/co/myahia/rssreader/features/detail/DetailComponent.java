package co.myahia.rssreader.features.detail;

import co.myahia.rssreader.features.AppComponent;
import co.myahia.rssreader.utils.dagger.PerFragment;
import dagger.Component;

@PerFragment
@Component(dependencies = {AppComponent.class}, modules = {DetailModule.class})
public interface DetailComponent {
    void inject(DetailsActivity detailActivity);
}
