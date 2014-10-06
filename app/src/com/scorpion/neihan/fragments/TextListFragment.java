package com.scorpion.neihan.fragments;

import java.util.LinkedList;
import java.util.List;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.scorpion.neihan.R;

import android.R.integer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class TextListFragment extends Fragment implements OnClickListener,
OnScrollListener,OnRefreshListener2{

	public TextListFragment() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_textlist, container,false);
		
		//获取标题控件，增加点击，进行新消息悬浮框显示的功能
		View titleView = view.findViewById(R.id.textlist_title);
		titleView.setOnClickListener(this);
		
		//获取ListView并且设置数据
		PullToRefreshListView refreshListView = (PullToRefreshListView)view.findViewById(R.id.textlist_listview);
		
		//设置上拉与下拉的事件监听
		//OnRefreshListener2<ListView>
		refreshListView.setOnRefreshListener(this);
		
		refreshListView.setMode(Mode.BOTH);
		ListView listView = refreshListView.getRefreshableView();
		
		List<String> strings = new LinkedList<String>();
		for (int i = 0; i < 60; i++) {
			strings.add("第"+i+"条");
		}
		
		header = inflater.inflate(R.layout.textlist_header_tools, listView,false);
		listView.addHeaderView(header);
		
		//设置悬浮的工具条两个命令的事件
		View quickPublish = header.findViewById(R.id.quick_tools_publish);
		quickPublish.setOnClickListener(this);
		
		View quickReview = header.findViewById(R.id.quick_tools_review);
		quickReview.setOnClickListener(this);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, strings);
		listView.setAdapter(adapter);
		
		listView.setOnScrollListener(this);
		
		//TODO 获取快速的工具条（发布和审核），用于列表滚动的显示和隐藏
		
		quickTools = (LinearLayout)view.findViewById(R.id.textlist_quick_tools);
		quickTools.setVisibility(View.INVISIBLE);
		
		textNotify = (TextView)view.findViewById(R.id.textlist_new_notify);
		textNotify.setVisibility(View.INVISIBLE);
		
		return view;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
	}

	private Handler handler = new Handler(){
		
		
		@Override
		public void handleMessage(android.os.Message msg) {
			int what = msg.what;
			if (what == 1) {
				//TODO what = 1 代表有新消息提醒
				textNotify.setVisibility(View.INVISIBLE);
				
			}
		};
	};
	

	private TextView textNotify;
	private LinearLayout quickTools;	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.textlist_title:
			textNotify.setVisibility(View.VISIBLE);
			Message msg = Message.obtain();
			msg.what = 1;
			handler.sendMessageDelayed(msg,3000);
			break;

		default:
			break;
		}
		
	}
///////////////////列表滚动，显示工具条////////////////////////////////
	
	private int lastIndex = 0;
	private View header;
	
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		int offset = lastIndex - firstVisibleItem;
		if ( offset < 0||firstVisibleItem == 0) {
			if(quickTools!=null){
				//证明现在移动是向上移动
				quickTools.setVisibility(View.INVISIBLE);
			}
		}else if ( offset > 0){
			if(quickTools!=null){
				quickTools.setVisibility(View.VISIBLE);
//				if (header.getVisibility() == View.VISIBLE) {
//					header.setVisibility(View.INVISIBLE);
//				}
			}
		}
		
		lastIndex = firstVisibleItem;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		
	}
///////////////////列表滚动，显示工具条////////////////////////////////
	
/////////////////////列表下拉刷新与上拉加载//////////////////////
	/**
	 * 从上向下拉动列表，那么就要进行加载新数据的操作
	 */
	@Override
	public void onPullDownToRefresh(PullToRefreshBase refreshView) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 从下向上拉动，那么就要考虑是否进行加载旧的数据
	 */
	@Override
	public void onPullUpToRefresh(PullToRefreshBase refreshView) {
		// TODO Auto-generated method stub
		
	}
/////////////////////列表下拉刷新与上拉加载//////////////////////
}
