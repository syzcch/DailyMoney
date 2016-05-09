package com.project.syz.account_management.database;

import java.io.Serializable;

/**
 * Created by Roger on 2016/4/4.
 */
public class GraphicItem implements Serializable {
    PairItem inItem[];

    public GraphicItem(String[] strName, float[] fMoney){
        inItem = new PairItem[strName.length];
        for(int i=0; i< strName.length; i++){
            inItem[i] = new PairItem();
            inItem[i].setMoney(fMoney[i]);
            inItem[i].setName(strName[i]);
        }
    }

    public int getCount(){
        return inItem.length;
    }

    public GraphicItem(){

    }

    public PairItem[] getInItem() {
        return inItem;
    }

    public void setInItem(PairItem[] inItem) {
        this.inItem = inItem;
    }
/*
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        int length=inItem.length;
        dest.writeInt(length); //先将数组长度写入
        dest.writeTypedArray(inItem,1);
    }
    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<IncomeItem> CREATOR = new Creator<IncomeItem>(){
        public IncomeItem createFromParcel(Parcel source){
            IncomeItem list=new IncomeItem();
            int length = source.readInt();//先获取数组长度
            list.inItem = new PairItem[length];
            source.readTypedArray(list.inItem,PairItem.CREATOR);
            return list;
        }
    };
    */
}
