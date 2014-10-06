package com.scorpion.neihan.test;

import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.scorpion.neihan.R;
import com.scorpion.neihan.bean.Comment;
import com.scorpion.neihan.bean.CommentList;
import com.scorpion.neihan.bean.EntityList;
import com.scorpion.neihan.bean.TextEntity;
import com.scorpion.neihan.client.ClientAPI;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * 这个文件就是一个测试入口，所有的测试内容，都可以放在这里
 * @author aaa
 *
 */
public class TestActivity extends Activity implements Response.Listener<String>{
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
	private int lastOffset = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);		

		queue = Volley.newRequestQueue(this);
		
//		Button button = (Button)this.findViewById(R.id.gengxin);
//		
//		button.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				int itemCount = 30;
//				ClientAPI.getList(queue, CATEGORY_TEXT, itemCount, lastTime, TestActivity.this);
//				
//			}
//		});
		
		Button button = (Button)this.findViewById(R.id.gengxin);
		
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//3551461874这个ID是对应文本段子的ID
				long groupId = 3551461874L;
				
				ClientAPI.getComments(queue,groupId, lastOffset,TestActivity.this);
				
			}
		});
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.test, menu);
		return true;
	}
	
	/**
	 * 列表网络获取回调部分
	 * @param arg0 列表JSON数据字符串
	 */
	public void listOnResponse(String arg0) {
		//Log.i("TestActivity", "List:"+arg0);
		try {
			JSONObject json = new JSONObject(arg0);
			arg0 = json.toString(4);
			System.out.println(arg0);
			
			//获取根节点下面的data对象
			JSONObject obj = json.getJSONObject("data");
			
			//解析段子列表的完整数据
			EntityList entityList = new EntityList();
			//这个方法时解析JSON的方法，其中包含的支持图片、文本、广告的解析
			entityList.parseJson(obj);//相当于获取数据内容
			
			//如果isHashMore返回true，代表还可以更新一次数据
			if(entityList.isHasMore()){
				lastTime = entityList.getMinTime();//获取更新时间标识
				Log.i("TestActivity", "lastTime:"+lastTime);
			}else{//没有更多数据了，休息一会儿
				String tip = entityList.getTip();
				Log.i("TestActivity", "tip:"+tip);
			}
			//获取段子内容列表
			
			//TODO 把entityList这个段子的数据集合体，传递给ListView之类的Adapter即可显示
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	@Override
	public void onResponse(String arg0) {
		Log.i("Test", arg0);
		JSONObject json;
		try {
			json = new JSONObject(arg0);
			arg0 = json.toString(4);
			
			Iterator<String> keys = json.keys();
			while(keys.hasNext()){
				String key = keys.next();
				Log.i("Test", "json key:"+key);
			}
			//解析获取到的评论列表
			CommentList commentList = new CommentList();
			//评论列表包含两组数据，一个是热门评论，一个是新鲜评论
			//热门评论和新鲜评论都有可能是空的
			commentList.parseJson(json);
			
			long groupId = commentList.getGroupId();
			int totalNumber = commentList.getTotalNumber();
			//表示评论列表是否还可以继续加载
			boolean hasMore = commentList.isHasMore();
			
			Log.i("Test", "groupId:"+groupId);
			Log.i("Test", "hasMore:"+hasMore);
			
			//热门评论
			List<Comment> topComments = commentList.getTopComments();
			if(topComments!=null){
				for (Comment comment:topComments) {
					Log.i("Test", "topComments:"+comment.getText());
				}
				
			}
			
			//新鲜评论可能有数据
			List<Comment> recentComments = commentList.getRecentComments();
			if(topComments!=null){
				for (Comment comment:topComments) {
					Log.i("Test", "recentComments:"+comment.getText());
				}
			}
			
			//TODO直接把commentList提交给ListView的Adapter，这样可以进行是否还有内容的判断
			//利用Adapter更新数据
			//分页标识，要求服务器每次返回20条评论，通过hasMore来进行是否还需要分页
			
			lastOffset += 20;
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}