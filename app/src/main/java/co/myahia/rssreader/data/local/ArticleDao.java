package co.myahia.rssreader.data.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by M.YAHIA on 12/01/2019.
 */
@Dao
public interface ArticleDao {

    @Query("SELECT * FROM articles_table")
    List<ArticleDB> getArticlesList();

    @Insert
    void insertAll(List<ArticleDB> articleDBS);

    @Query("SELECT * FROM articles_table where source_id like :sourceID ")
    List<ArticleDB> getArticlesBySource(String sourceID);

    @Query("DELETE FROM articles_table WHERE source_id = :sourceId")
    void deleteArticlesBySourceId(String sourceId);

    @Query("DELETE FROM articles_table")
    void deleteAllArticles();

    @Query("SELECT * FROM news_sources")
    List<SourceDB> getAllSources();

    @Insert
    void insertSources(List<SourceDB> list);

    @Query("DELETE FROM news_sources Where source_id = :sourceID ")
    void deleteSourceFromSources(String sourceID);
}
