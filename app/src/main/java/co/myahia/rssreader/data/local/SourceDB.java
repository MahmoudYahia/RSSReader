package co.myahia.rssreader.data.local;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by M.YAHIA on 12/01/2019.
 */
@Entity(tableName = "news_sources")
public class SourceDB {

    @PrimaryKey(autoGenerate = true)
    private int ID;

    @ColumnInfo(name ="source_id")
    String sourceID;
    @ColumnInfo(name = "source_name")
    String sourceName;

    public String getSourceID() {
        return sourceID;
    }

    public void setSourceID(String sourceID) {
        this.sourceID = sourceID;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
