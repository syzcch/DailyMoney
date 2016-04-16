package com.project.syz.account_management.graphic;

//import android.app.Activity;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.project.syz.account_management.R;
import com.project.syz.account_management.database.LineChartItem;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LineChartFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LineChartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LineChartFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "linechart";

    // TODO: Rename and change types of parameters
 //   private String mParam1;
    private LineChartItem lChartItem;
    private LinearLayout layout;
    private View view;
    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LineChartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LineChartFragment newInstance(LineChartItem lChartItem) {
        LineChartFragment fragment = new LineChartFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, lChartItem);
        fragment.setArguments(args);
        return fragment;
    }

    public LineChartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            lChartItem = (LineChartItem)getArguments().getSerializable(ARG_PARAM1);
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void setLineChart(){
        XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
        XYSeries seriesIncome = new XYSeries("Income");
        XYSeries seriesExpend = new XYSeries("Expenditure");
        XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();

        for(int i = 0; i < 12; i++){
            seriesIncome.add(i+1,lChartItem.getIncome()[i]);
            seriesExpend.add(i+1,lChartItem.getExpend()[i]);
        }
        mDataset.addSeries(seriesIncome);
        mDataset.addSeries(seriesExpend);

        //设置图表的X轴的当前方向
        mRenderer.setOrientation(XYMultipleSeriesRenderer.Orientation.HORIZONTAL);
        mRenderer.setXTitle("Date");//设置为X轴的标题
        mRenderer.setYTitle("Money");//设置y轴的标题
        mRenderer.setAxisTitleTextSize(15);//设置轴标题文本大小
        mRenderer.setChartTitle("Money per Month");//设置图表标题
        mRenderer.setZoomButtonsVisible(true);//设置显示放大缩小按钮
        mRenderer.setZoomEnabled(true);//设置不允许放大缩小.
        mRenderer.setChartTitleTextSize(15);//设置图表标题文字的大小
        mRenderer.setLabelsTextSize(10);//设置标签的文字大小
        mRenderer.setLegendTextSize(10);//设置图例文本大小
        mRenderer.setPointSize(10f);//设置点的大小
        mRenderer.setYAxisMin(0);//设置y轴最小值是0
//        mRenderer.setYAxisMax(15);
 //       mRenderer.setYLabels(10);//设置Y轴刻度个数（貌似不太准确）
        mRenderer.setXAxisMax(12);
        mRenderer.setShowGrid(true);//显示网格
        //将x标签栏目显示如：1,2,3,4替换为显示1月，2月，3月，4月
        mRenderer.addXTextLabel(1, "Jan");
        mRenderer.addXTextLabel(2, "Feb");
        mRenderer.addXTextLabel(3, "Mar");
        mRenderer.addXTextLabel(4, "Apr");
        mRenderer.addXTextLabel(5, "May");
        mRenderer.addXTextLabel(6, "Jun");
        mRenderer.addXTextLabel(7, "Jul");
        mRenderer.addXTextLabel(8, "Aug");
        mRenderer.addXTextLabel(9, "Sept");
        mRenderer.addXTextLabel(10, "Oct");
        mRenderer.addXTextLabel(11, "Nov");
        mRenderer.addXTextLabel(12, "Dec");
        mRenderer.setXLabels(0);//设置只显示如1月，2月等替换后的东西，不显示1,2,3等
 //       mRenderer.setMargins(new int[] { 20, 30, 15, 20 });//设置视图位置

        XYSeriesRenderer rIncome = new XYSeriesRenderer();//(类似于一条线对象)
        rIncome.setColor(Color.BLUE);//设置颜色
        rIncome.setPointStyle(PointStyle.CIRCLE);//设置点的样式
        rIncome.setFillPoints(true);//填充点（显示的点是空心还是实心）
        rIncome.setDisplayChartValues(true);//将点的值显示出来
        rIncome.setChartValuesSpacing(10);//显示的点的值与图的距离
        rIncome.setChartValuesTextSize(15);//点的值的文字大小

        //  r.setFillBelowLine(true);//是否填充折线图的下方
        //  r.setFillBelowLineColor(Color.GREEN);//填充的颜色，如果不设置就默认与线的颜色一致
        rIncome.setLineWidth(3);//设置线宽
        mRenderer.addSeriesRenderer(rIncome);


        XYSeriesRenderer rExpend = new XYSeriesRenderer();//(类似于一条线对象)
        rExpend.setColor(Color.RED);//设置颜色
        rExpend.setPointStyle(PointStyle.CIRCLE);//设置点的样式
        rExpend.setFillPoints(true);//填充点（显示的点是空心还是实心）
        rExpend.setDisplayChartValues(true);//将点的值显示出来
        rExpend.setChartValuesSpacing(10);//显示的点的值与图的距离
        rExpend.setChartValuesTextSize(15);//点的值的文字大小

        //  rTwo.setFillBelowLine(true);//是否填充折线图的下方
        //  rTwo.setFillBelowLineColor(Color.GREEN);//填充的颜色，如果不设置就默认与线的颜色一致
        rExpend.setLineWidth(3);//设置线宽

        mRenderer.addSeriesRenderer(rExpend);

        GraphicalView graphicalView = ChartFactory.getLineChartView(getActivity(), mDataset, mRenderer);
        layout.removeAllViews();
        layout.setBackgroundColor(Color.BLACK);
        layout.addView(graphicalView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_line_chart, container, false);
        layout=(LinearLayout)view.findViewById(R.id.linechart);
        setLineChart();
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
