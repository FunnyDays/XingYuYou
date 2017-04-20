package com.xingyuyou.xingyuyou.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.GlideImageLoader;
import com.xingyuyou.xingyuyou.adapter.GameAdapter;
import com.xingyuyou.xingyuyou.adapter.TestAdapter;
import com.xingyuyou.xingyuyou.base.BaseFragment;
import com.xingyuyou.xingyuyou.bean.Game;
import com.youth.banner.Banner;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/28.
 */
public class OnlineGameFragment extends BaseFragment {

    private TabLayout mTabLayout;
    private ImageView mGameTypeMore;
    private PopupWindow mPopWindow;
    private TextView mTextView;

    //赛车竞速   棋牌天地	模拟养成		卡牌对战		休闲益智		策略冒险
    //脱出		恐怖	二次元	 RPG	大型游戏	塔防游戏		动作格斗   网络游戏 飞行射击		角色扮演
    public static OnlineGameFragment newInstance(String content) {
        Bundle args = new Bundle();
        args.putString("ARGS", content);
        OnlineGameFragment fragment = new OnlineGameFragment();
        fragment.setArguments(args);
        return fragment;
    }


    /**
     * 初始化数据
     */
    @Override
    public void initData() {

    }

    @Override
    protected View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_online_game, null);
        mTabLayout = (TabLayout) view.findViewById(R.id.tabs);
        mTabLayout.addTab(mTabLayout.newTab().setText("策略冒险"));
        mTabLayout.addTab(mTabLayout.newTab().setText("休闲益智"));
        mTabLayout.addTab(mTabLayout.newTab().setText("卡牌对战"));
        mTabLayout.addTab(mTabLayout.newTab().setText("模拟养成"));
        mTabLayout.addTab(mTabLayout.newTab().setText("棋牌天地"));
        mTabLayout.addTab(mTabLayout.newTab().setText("赛车竞速"));
        mTabLayout.addTab(mTabLayout.newTab().setText("角色扮演"));
        mTabLayout.addTab(mTabLayout.newTab().setText("飞行射击"));
        mTabLayout.addTab(mTabLayout.newTab().setText("网络游戏"));
        mTabLayout.addTab(mTabLayout.newTab().setText("飞行射击"));
        mTabLayout.addTab(mTabLayout.newTab().setText("动作格斗"));
        mTabLayout.addTab(mTabLayout.newTab().setText("塔防游戏"));
        mTabLayout.addTab(mTabLayout.newTab().setText("大型游戏"));
        mTabLayout.addTab(mTabLayout.newTab().setText("RPG"));
        mTabLayout.addTab(mTabLayout.newTab().setText("二次元"));
        mTabLayout.addTab(mTabLayout.newTab().setText("恐怖"));
        mTabLayout.addTab(mTabLayout.newTab().setText("脱出"));

        for(int i=0; i < mTabLayout.getTabCount(); i++) {
            View tab = ((ViewGroup) mTabLayout.getChildAt(0)).getChildAt(i);
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) tab.getLayoutParams();
            p.setMargins(0, 0, 50, 0);
            tab.requestLayout();
        }

        mGameTypeMore = (ImageView) view.findViewById(R.id.iv_game_type_more);
        mGameTypeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow();
            }
        });

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        ArrayList<Game> arrayList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            arrayList.add(new Game(i+"名字"));
        }
        TestAdapter gameAdapter = new TestAdapter(mActivity, arrayList);
        recyclerView.setAdapter(gameAdapter);
        return view;
    }

    private void popupWindow() {
        View popupView = LayoutInflater.from(mActivity).inflate(R.layout.popup_game_type_layout, null);
        mTextView = (TextView) popupView.findViewById(R.id.tv_one);
        mTextView.setTextColor(getResources().getColor(R.color.colorAccent));
        mTextView.setBackgroundResource(R.drawable.tab_background_selected);
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mActivity, "view"+view.getId(), Toast.LENGTH_SHORT).show();
                mPopWindow.dismiss();
            }
        });
        mPopWindow = new PopupWindow(popupView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mPopWindow.setAnimationStyle(R.style.animatorUP);  //设置加载动画
        mPopWindow.setBackgroundDrawable(getResources().getDrawable(R.color.colorControlNormal));    //要为popWindow设置一个背景才有效
        mPopWindow.showAsDropDown(mGameTypeMore);

    }

}
