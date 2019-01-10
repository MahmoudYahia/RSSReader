package co.myahia.rssreader.utils;

public class NullableUtils {
    private NullableUtils() {
        throw new AssertionError();
    }

    public static <T> T checkNotNull(T reference) {
        if (reference != null) {
            return reference;
        }
        throw new NullPointerException();
    }
}
