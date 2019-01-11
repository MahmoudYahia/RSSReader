package co.myahia.rssreader.utils;

public interface BaseContract {

    public interface Presenter {
        void onStart();

        void onStop();

        void setViewPresenter();

    }

    public interface View<T> {
        void setPresenter(T t);

        void showLoading(boolean showing);

        boolean isActive();
    }
}
