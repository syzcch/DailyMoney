package com.project.syz.account_management.database;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Roger on 2016/3/30.
 */
public class DBItem implements Parcelable {
    private int _id;
    private String type;
    private float money;
    private String time;
    private String memo;

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

/*
    public DBItem(Parcel in) {
        this._id = in.readInt();
        this.type = in.readString();
        this.money = in.readFloat();
        this.time = in.readString();
        this.memo = in.readString();
    }
*/
    public DBItem() {
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_id);
        dest.writeString(type);
        dest.writeFloat(money);
        dest.writeString(time);
        dest.writeString(memo);
    }

    @Override
    public int describeContents() {
        return 0;
    }
    public static final Parcelable.Creator<DBItem> CREATOR
            = new Creator<DBItem>() {
        @Override
        public DBItem createFromParcel(Parcel in) {
            DBItem dbItem = new DBItem();
            dbItem._id = in.readInt();
            dbItem.type = in.readString();
            dbItem.money = in.readFloat();
            dbItem.time = in.readString();
            dbItem.memo = in.readString();
            return dbItem;
        }
        @Override
        public DBItem[] newArray(int size) {
            return new DBItem[size];
        }
    };
}
