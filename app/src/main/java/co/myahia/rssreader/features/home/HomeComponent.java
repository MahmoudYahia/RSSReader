package co.myahia.rssreader.features.home;

import co.myahia.rssreader.features.AppComponent;
import co.myahia.rssreader.utils.dagger.PerFragment;
import dagger.Component;

@PerFragment
@Component(dependencies = {AppComponent.class}, modules = {HomeModule.class})
public interface HomeComponent {
    void inject(MainActivity mainActivity);
}
