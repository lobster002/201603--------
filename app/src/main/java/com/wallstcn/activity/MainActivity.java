package com.wallstcn.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.wallstcn.R;
import com.wallstcn.adapter.RecyclerAdapter;
import com.wallstcn.adapter.ViewPagerAdapter;
import com.wallstcn.bean.ChannalIemBean;
import com.wallstcn.bean.NetworkImageHolderView;
import com.wallstcn.entity.Config;
import com.wallstcn.interfaces.EndlessRecyclerOnScrollListener;
import com.wallstcn.utils.JsonUtils;
import com.wallstcn.utils.NetUtils;
import com.wallstcn.utils.ScreenUtils;
import com.wallstcn.view.HorizontalDividerItemDecoration;
import com.wallstcn.view.XCRecyclerView;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends Activity {

    private final int LOAD_DATA_FINISH = 1;//异步数据加载完成
    private final int REFRESH_COMPLETE = 2;//刷新完成
    private final int LOADMORE_COMPLETE = 3;//上拉加载完成

    private ArrayList<String> channalItems = null;//导航栏显示的条目内容
    private JSONArray jarry = null;//服务器返回的String 解析出的 JSONArray

    private View[] viewContainer = null;//保存其他ViewPager 界面的集合

    private ViewPager mViewPager = null;//轮播图片控件
    private TabLayout mTablayout = null;//顶部导航栏

    private ExecutorService executor = null; //异步 线程池
    private ArrayList<ChannalIemBean>[] items = null;//数据集合

    private XCRecyclerView[] recyclerViews = null; //RecyclerView集合
    private RecyclerAdapter[] recyclerAdapters = null; //适配器
    private ViewPagerAdapter adapter = null; //ViewPager

    private SwipeRefreshLayout[] swipeRefreshLayout = null;
    private LinearLayoutManager[] layouts = null;
    private int addCount = 0;
    private int pageSize = 5;//暂时写死  可以根据api返回的JSONObject数组 length 动态修改 已完成自动适配

    private ConvenientBanner[] banners = null;//顶部的图片轮播控件

    private Handler mHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int key;
            switch (msg.what) {
                case LOAD_DATA_FINISH://数据记载完成
                    completeInitView();//完成剩下的初始化任务
                    break;
                case REFRESH_COMPLETE://下拉刷新 获取数据后 走到这里
                    key = msg.arg1;
                    swipeRefreshLayout[key].setRefreshing(false);
                    showToast("暂无更新数据");
                    break;
                case LOADMORE_COMPLETE://加载更多数据完成后走到这里
                    key = msg.arg1;
                    showToast("finish");
                    for (int i = 0; i < 3; i++) {
                        items[key].add(new ChannalIemBean(addCount++));
                    }
                    recyclerAdapters[key].notifyDataSetChanged();
                    break;
            }
        }
    };

    private void completeInitView() {//获取数据后 走到这里完成布局
        viewContainer = new View[pageSize];
        for (int i = 0; i < pageSize; i++) {
            viewContainer[i] = LayoutInflater.from(MainActivity.this).inflate(R.layout.view_content, null);
        }
        adapter = new ViewPagerAdapter(MainActivity.this, channalItems, viewContainer, items);
        mViewPager.setAdapter(adapter);
        mTablayout.setupWithViewPager(mViewPager);
        mTablayout.setTabsFromPagerAdapter(adapter);

        for (int i = 0; i < pageSize; i++) {//初始化RecyclerView
            final int finalI = i;
            swipeRefreshLayout[finalI] = (SwipeRefreshLayout) viewContainer[i].findViewById(R.id.swip_refresh_layout);
            swipeRefreshLayout[finalI].setColorSchemeResources(R.color.google_blue,
                    R.color.google_green, R.color.google_red, R.color.google_yellow);//加载时进度条的颜色
            swipeRefreshLayout[finalI].setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {//下拉时触发的事件
                @Override
                public void onRefresh() {
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {//模拟下拉刷新
                            SystemClock.sleep(1000);
                            Message msg = new Message();
                            msg.what = REFRESH_COMPLETE;
                            msg.arg1 = finalI;
                            mHander.sendMessage(msg);
                        }
                    });
                }
            });

            recyclerViews[finalI] = (XCRecyclerView) viewContainer[finalI].findViewById(R.id.recyclerView);
            layouts[finalI] = new LinearLayoutManager(this);
            recyclerViews[finalI].setLayoutManager(layouts[finalI]);
            if (0 == finalI) {//在这里判断是否在当前ViewPager的子布局中加载 顶部轮播图片控件  可以动态控制
                View header = LayoutInflater.from(this).inflate(R.layout.view_header, recyclerViews[finalI], false);
                recyclerViews[finalI].addHeaderView(header);
                banners[finalI] = (ConvenientBanner) header;
                banners[finalI].setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
                    @Override
                    public NetworkImageHolderView createHolder() {
                        return new NetworkImageHolderView();
                    }
                }, getImageUrls()).setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
                        .setCanLoop(true);
            }
            recyclerViews[finalI].setHasFixedSize(true);
            recyclerViews[finalI].addOnScrollListener(new EndlessRecyclerOnScrollListener(layouts[finalI]) {
                @Override
                public void onLoadMore(int currentPage) {
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {//模拟加载更多
                            SystemClock.sleep(1000);
                            Log.e("TAG", "正在加载");
                            Message msg = new Message();
                            msg.what = LOADMORE_COMPLETE;
                            msg.arg1 = finalI;
                            mHander.sendMessage(msg);
                        }
                    });
                }
            });
            HorizontalDividerItemDecoration divider = new HorizontalDividerItemDecoration.Builder(this).color(Color.WHITE).size(ScreenUtils.dp2px(this, 2)).margin(0, 0).build();
            recyclerViews[finalI].addItemDecoration(divider);//添加分隔线
            recyclerAdapters[finalI] = new RecyclerAdapter(this, items[i]);
            recyclerViews[finalI].setAdapter(recyclerAdapters[i]);//设置适配器
        }
    }

    private List getImageUrls() {//获取顶部图片轮播 图片url
        ArrayList<String> urls = new ArrayList<>();
        int len = Config.Urls.length;
        for (int i = 0; i < len; i++) {
            urls.add(Config.Urls[i]);
        }
        return urls;
    }


    private void showToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mTablayout = (TabLayout) findViewById(R.id.tabs);
        getWindow().setStatusBarColor(getResources().getColor(R.color.titleBackground));//设置状态栏颜色
        executor = Executors.newSingleThreadExecutor();//创建线程池对象
        recyclerViews = new XCRecyclerView[pageSize];
        recyclerAdapters = new RecyclerAdapter[pageSize];
        swipeRefreshLayout = new SwipeRefreshLayout[pageSize];
        layouts = new LinearLayoutManager[pageSize];
        banners = new ConvenientBanner[pageSize];
//        if (!NetUtils.isNetworkConnected(this)) {//判断网络是否可用
//            Toast.makeText(this, "网络不可用，无法初始化数据", Toast.LENGTH_SHORT).show();
//            System.exit(0);
//        }
//        判断网络状态 //是否Wifi
//        if (!NetUtils.isWifi(this)) {
//            //不是Wifi
//        }
        executor.execute(new Runnable() {
            @Override
            public void run() {//子线程访问api 获取数据
                jarry = NetUtils.connServerForJSONArray(Config.SERVER_URI);//约35毫秒
                channalItems = JsonUtils.getChannalItemNames(jarry);
                items = JsonUtils.getItems(jarry);
                Message msg = new Message();
                msg.what = LOAD_DATA_FINISH;
                mHander.sendMessage(msg);
            }
        });
    }

}
