package com.project.syz.account_management.database;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by Roger on 2016/4/6.
 */
public class LineChartItem implements Serializable {
    float income[];
    float expend[];

    public LineChartItem(float[] in, float[] ex){
        income =  Arrays.copyOf(in,in.length);
        expend =  Arrays.copyOf(ex,ex.length);
    }
    public LineChartItem(){

    }

    public float[] getIncome() {
        return income;
    }

    public void setIncome(float[] income) {
        this.income = income;
    }

    public float[] getExpend() {
        return expend;
    }

    public void setExpend(float[] expend) {
        this.expend = expend;
    }
}
