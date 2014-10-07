package com.scorpion.neihan.fragments;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.scorpion.neihan.R;
import com.scorpion.neihan.activity.EssayDetailActivity;
import com.scorpion.neihan.adapters.EssayAdapter;
import com.scorpion.neihan.bean.DataStore;
import com.scorpion.neihan.bean.EntityList;
import com.scorpion.neihan.bean.TextEntity;
import com.scorpion.neihan.client.ClientAPI;

import android.R.integer;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 1.列表界面，第一次启动，并且数据为空的时候，自动加载数据
 * 2.只要列表没有数据，进入这个界面的时候就尝试加载数据
 * 3.只要有数据，就不进行数据的加载
 * 4.进入这个界面，并且有数据的情况下，尝试检查新信息的，？？接口
 * @author aaa
 *
 */
public class TextListFragment extends Fragment implements OnClickListener,
OnScrollListener,OnRefreshListener2,OnItemClickListener{

	private TextView textNotify;
	private LinearLayout quickTools;
	private EssayAdapter adapter;
	//private List<TextEntity> entities;
	/**
	 * 分类ID，代表文本
	 */
	public static final int CATEGORY_TEXT = 1;
	/**
	 * 分类ID，代表图片
	 */
	public static final int CATEGORY_IMAGE = 2;
	private RequestQueue queue;
	private long lastTime = 0;
	
	//请求的分类类型ID
	private int requestCategory = CATEGORY_TEXT;

	public TextListFragment() {
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		queue = Volley.newRequestQueue(getActivity());
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			Log.i("打印打印lasttime", ""+lastTime);
			lastTime = savedInstanceState.getLong("lastTime");
		}
		
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
		
		header = inflater.inflate(R.layout.textlist_header_tools, listView,false);
		listView.addHeaderView(header);
		
		//设置悬浮的工具条两个命令的事件
		View quickPublish = header.findViewById(R.id.quick_tools_publish);
		quickPublish.setOnClickListener(this);
		
		View quickReview = header.findViewById(R.id.quick_tools_review);
		quickReview.setOnClickListener(this);
		
		List<TextEntity> entities = DataStore.getInstance().getTextEntities();
//		if (entities == null) {
//			entities = new LinkedList<TextEntity>();
//		}
		
		adapter = new EssayAdapter(getActivity(), entities);
		adapter.setListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (v instanceof TextView) {
					String string = (String)v.getTag();
					if (string!=null) {
						int position = Integer.parseInt(string);
						Intent intent = new Intent(getActivity(),EssayDetailActivity.class);
						intent.putExtra("currentEssayPosition", position);
						intent.putExtra("category", requestCategory);
						startActivity(intent);
					}
				}
				
			}
		});
		
		listView.setAdapter(adapter);
		
		listView.setOnScrollListener(this);
		
		listView.setOnItemClickListener(this);
		
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
		super.onDestroyView();
		
		this.adapter = null;
		this.header = null;
		this.quickTools = null;
		this.textNotify = null;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		outState.putLong("lastTime", lastTime);
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
		
	
	@Override
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
	 * 列表网络获取回调部分，这个方法时在Volley联网相应返回的时候调用
	 * 它是在主线程执行的，因此可以直接更新UI
	 * @param arg0 列表JSON数据字符串
	 */
	public void listOnResponse(String arg0) {
		try {
			JSONObject json = new JSONObject(arg0);
			
			//获取根节点下面的data对象
			JSONObject obj = json.getJSONObject("data");
			
			//解析段子列表的完整数据
			EntityList entityList = new EntityList();
			//这个方法时解析JSON的方法，其中包含的支持图片、文本、广告的解析
			entityList.parseJson(obj);//相当于获取数据内容
			
			//如果isHashMore返回true，代表还可以更新一次数据
			if(entityList.isHasMore()){
				lastTime = entityList.getMinTime();//获取更新时间标识
			}else{//没有更多数据了，休息一会儿
				String tip = entityList.getTip();
			}
			//获取段子内容列表
			
			//TODO 把entityList这个段子的数据集合体，传递给ListView之类的Adapter即可显示
			List<TextEntity> ets = entityList.getEntities();
			if (ets != null) {
				if (!ets.isEmpty()) {
					//把ets中的内容按照迭代器的顺序添加，需要验证一下
					
					DataStore.getInstance().addTextEntities(ets);
					
//					entities.addAll(0,ets);
					
					//手动添加
//					int len = ets.size();
//					
//					for (int index = len-1; index >= 0; index--) {
//						//把object添加到指定的location位置，原有的loaction以及以后的内容
//						entities.add(0, ets.get(index));
//					}
					
					adapter.notifyDataSetChanged();
					
				}else {
					//TODO 没有更多的数据了，需要提示一下
				}
			}else {
				//TODO 没有获取到网络数据，可能是数据解析错误、网络错误，需要提示一下
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 从上向下拉动列表，那么就要进行加载新数据的操作
	 */
	@Override
	public void onPullDownToRefresh(final PullToRefreshBase refreshView) {

		ClientAPI.getList(queue, requestCategory, 30, lastTime, 
				new Response.Listener<String>() {

					@Override
					public void onResponse(String arg0) {
						// TODO 加载新数据
						refreshView.onRefreshComplete();
						listOnResponse(arg0);
					}
				});
		
	}

	/**
	 * 从下向上拉动，那么就要考虑是否进行加载旧的数据
	 */
	@Override
	public void onPullUpToRefresh(PullToRefreshBase refreshView) {
		// TODO Auto-generated method stub
		
	}
/////////////////////列表下拉刷新与上拉加载//////////////////////

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		position--;
		Intent intent = new Intent(getActivity(),EssayDetailActivity.class);
		intent.putExtra("currentEssayPosition", position);
		intent.putExtra("category", requestCategory);
		startActivity(intent);
		
	}
	
	
}
