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
import com.project.syz.account_management.database.GraphicItem;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link IncomeGraphicFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link IncomeGraphicFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IncomeGraphicFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "income";
//    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;

    private GraphicItem incomeItem;
    private View view;
    private LinearLayout layout;
//    private Context context;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MonthGraphicFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IncomeGraphicFragment newInstance(GraphicItem incomeItem) {
        IncomeGraphicFragment fragment = new IncomeGraphicFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, incomeItem);
        fragment.setArguments(args);
        return fragment;
    }

    public IncomeGraphicFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            incomeItem = (GraphicItem)getArguments().getSerializable(ARG_PARAM1);
//            DBItem dbItem = (DBItem)getIntent().getParcelableExtra("DBItem");
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
 //       context = this;

    }

    private void setGrapic(){
        GraphicalView graphicalView;
        int[] colors={Color.BLUE,Color.GREEN,Color.MAGENTA,Color.RED,Color.CYAN,Color.YELLOW};
        DefaultRenderer renderer=buildCategoryRenderer(colors);
        CategorySeries dataset = buildCategoryDataset("测试饼图", incomeItem);
        graphicalView= ChartFactory.getPieChartView(getActivity(), dataset, renderer);//饼状图

        layout.removeAllViews();
        layout.setBackgroundColor(Color.BLACK);
        layout.addView(graphicalView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));
    }

    protected DefaultRenderer buildCategoryRenderer(int[] colors) {
        DefaultRenderer renderer = new DefaultRenderer();

        renderer.setLegendTextSize(30);//设置左下角表注的文字大小
        renderer.setZoomButtonsVisible(true);//设置显示放大缩小按钮
        renderer.setZoomEnabled(true);//设置不允许放大缩小.
        renderer.setChartTitleTextSize(40);//设置图表标题的文字大小
        renderer.setChartTitle("统计结果");//设置图表的标题  默认是居中顶部显示
        renderer.setLabelsTextSize(30);//饼图上标记文字的字体大小
        //renderer.setLabelsColor(Color.WHITE);//饼图上标记文字的颜色
        renderer.setPanEnabled(false);//设置是否可以平移
        renderer.setDisplayValues(true);//是否显示值
        renderer.setClickEnabled(true);//设置是否可以被点击
//        renderer.setMargins(new int[] { 20, 20, 0,0 });
        //margins - an array containing the margin size values, in this order: top, left, bottom, right
        for (int color : colors) {
            SimpleSeriesRenderer r = new SimpleSeriesRenderer();
            r.setColor(color);
            renderer.addSeriesRenderer(r);
        }
        return renderer;
    }

    protected CategorySeries buildCategoryDataset(String title, GraphicItem incomeItem) {
        CategorySeries series = new CategorySeries(title);
        for(int i = 0; i < incomeItem.getCount(); i++){
            series.add(incomeItem.getInItem()[i].getName(),incomeItem.getInItem()[i].getMoney());
        }
        return series;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_income_graphic, container, false);
        layout=(LinearLayout)view.findViewById(R.id.month_graphic);
        setGrapic();
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
