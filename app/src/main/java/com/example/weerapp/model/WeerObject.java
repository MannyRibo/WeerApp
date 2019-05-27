package com.example.weerapp.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "weerobject")
public class WeerObject implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    @Embedded
    @SerializedName("main")
    @Expose
    private Main main;

    @ColumnInfo
    @SerializedName("datum")
    @Expose
    private String datum;

    public WeerObject(Main main, String datum) {
        this.id = id;
        this.main = main;
        this.datum = datum;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    protected WeerObject(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        main = in.readParcelable(Main.class.getClassLoader());
        datum = in.readString();
    }

    public static final Creator<WeerObject> CREATOR = new Creator<WeerObject>() {
        @Override
        public WeerObject createFromParcel(Parcel in) {
            return new WeerObject(in);
        }

        @Override
        public WeerObject[] newArray(int size) {
            return new WeerObject[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeParcelable(main, flags);
        dest.writeString(datum);
    }

}