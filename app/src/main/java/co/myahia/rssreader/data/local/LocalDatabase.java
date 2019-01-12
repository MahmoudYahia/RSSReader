package co.myahia.rssreader.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Created by M.YAHIA on 12/01/2019.
 */

@Database(entities = {ArticleDB.class, SourceDB.class}, version = 2 ,exportSchema = false)
public abstract class LocalDatabase extends RoomDatabase {

    private static LocalDatabase localDatabaseInstance;

    public abstract ArticleDao getArticleDao();

    public static LocalDatabase getInstance(Context context) {
        if (localDatabaseInstance != null)
            return localDatabaseInstance;

        localDatabaseInstance = getLocalDatabase(context);
        return localDatabaseInstance;
    }


    private static LocalDatabase getLocalDatabase(Context context) {
        return Room.databaseBuilder(context.getApplicationContext(), LocalDatabase.class, "Articles_database")
                .fallbackToDestructiveMigration()
                .build();
    }
}
