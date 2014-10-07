package com.scorpion.neihan.fragments;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import pl.droidsonroids.gif.GifImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.Volley;
import com.scorpion.neihan.R;
import com.scorpion.neihan.adapters.CommentAdapter;
import com.scorpion.neihan.bean.Comment;
import com.scorpion.neihan.bean.CommentList;
import com.scorpion.neihan.bean.TextEntity;
import com.scorpion.neihan.bean.UserEntity;
import com.scorpion.neihan.client.ClientAPI;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

public class DetailContentFragment extends Fragment implements OnTouchListener,Listener<String>{
	private TextEntity entity;
	private ScrollView scrollView;
	private TextView txtHotCommentCoun;
	private TextView txtRecentCommentCoun;
	private RequestQueue queue;
	private int lastOffset = 0;
	private LinearLayout scrollContent;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (entity == null) {
			Bundle argumentsBundle = getArguments();
			Serializable serializable = argumentsBundle.getSerializable("entity");
			
			if (serializable instanceof TextEntity) {
				entity = (TextEntity)serializable;
			}
		}
		
		queue = Volley.newRequestQueue(getActivity());
		
	}
	
	private CommentAdapter hotAdapter;
	private CommentAdapter recentAdapter;
	
	private List<Comment> hotComments;
	private List<Comment> recentComments;
	private boolean hasMore;
	private Long groupId;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View ret = inflater.inflate(R.layout.fragment_detail_content, container,false);

		scrollView = (ScrollView)ret.findViewById(R.id.detail_scroll);
		scrollContent = (LinearLayout)ret.findViewById(R.id.scroll_content);
		
		scrollView.setOnTouchListener(this);
		
		// 设置段子主体内容
		setEssayContent(ret);
		
		txtHotCommentCoun = (TextView)ret.findViewById(R.id.txt_hot_comments_count);
		ListView hotComListView = (ListView)ret.findViewById(R.id.hot_comments_list);
		
		hotComments = new LinkedList<Comment>();
		hotAdapter = new CommentAdapter(getActivity(),hotComments);
		hotComListView.setAdapter(hotAdapter);
		
		
		
		txtRecentCommentCoun = (TextView)ret.findViewById(R.id.txt_recent_comments_count);
		ListView recentComListView = (ListView)ret.findViewById(R.id.recent_comments_list);
		recentComments = new LinkedList<Comment>();
		recentAdapter = new CommentAdapter(getActivity(),recentComments);
		hotComListView.setAdapter(recentAdapter);
		
		groupId = entity.getGroupId();
		ClientAPI.getComments(queue, groupId, 0, this);
		
		//		if (entity!=null) {
//			UserEntity user = entity.getUser();
//			if (user!=null) {
//				String avatarUrl = user.getAvatarUrl();
//				String nick = user.getName();
//				TextView txtNick = 
//			}
//		}
		
		return ret;
	}

	/**
	 * 设置段子主体内容
	 * @param ret
	 */
	private void setEssayContent(View ret) {
		TextView btnGifPlay = (TextView)ret.findViewById(R.id.btnGifPlay);
		ImageButton btnShare = (ImageButton)ret.findViewById(R.id.item_share);
		CheckBox chbBuryCount = (CheckBox)ret.findViewById(R.id.item_bury_count);
		CheckBox chbDiggCount = (CheckBox)ret.findViewById(R.id.item_digg_count);
		GifImageView gifImageView = (GifImageView)ret.findViewById(R.id.gifView);
		ImageView imgProfileImage = (ImageView)ret.findViewById(R.id.item_profile_image);
		ProgressBar pbDownloadProgress = (ProgressBar)ret.findViewById(R.id.item_image_download_progress);
		TextView txtCommentCount = (TextView)ret.findViewById(R.id.item_comment_count);
		TextView txtContent = (TextView)ret.findViewById(R.id.item_content);
		TextView txtProfileNick = (TextView)ret.findViewById(R.id.item_profile_nick);
		
		
		//先设置文本内容的数据
		UserEntity user = entity.getUser();
		String nick = "";
		if (user!=null) {
		    nick = user.getName();
		}
		txtProfileNick.setText(nick);
		
		String content = entity.getContent();
		txtContent.setText(content);
		
		int diggCount = entity.getDiggCount();
		chbDiggCount.setText(Integer.toString(diggCount));			
		int userDigg = entity.getUserDigg();//用户是否赞过			
		//如果userDigg是1的话，代表了，已经赞过，那么chbDiggCount必须禁用，所以用！=1
		chbDiggCount.setEnabled(userDigg != 1);
		
		int buryCount = entity.getBuryCount();
		chbBuryCount.setText(Integer.toString(buryCount));
		int userBury = entity.getUserBury();//用户是否赞过			
		//如果userBury是1的话，代表了，已经赞过，那么chebBuryCount必须禁用，所以用！=1
		chbBuryCount.setEnabled(userBury != 1);
		
		int commentCount = entity.getCommentCount();
		txtCommentCount.setText(Integer.toString(commentCount));
		
		//再设置图片内容的数据
	}
	
	private boolean hasMove = false;

	/**
	 * 处理ScrollView触摸事件，用于在ScrollView滚动到最下面的时候，自动加载数据
	 * @param v
	 * @param event
	 * @return
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		boolean bret = false;
		
		int action = event.getAction();
		
		if (action == MotionEvent.ACTION_DOWN) {
			bret = true;
			hasMove = false;
		}else if (action == MotionEvent.ACTION_MOVE) {
			hasMove = true;
		}else if (action == MotionEvent.ACTION_UP) {
			
			if (hasMove) {
				int sy = scrollView.getScrollY();
				int mh = scrollView.getMeasuredHeight();
				int ch = scrollContent.getHeight();
				
				if (sy+mh >= ch) {
					//TODO 进行评论分页加载
					ClientAPI.getComments(queue, groupId, lastOffset, this);
				}
				
			}
		}
		
		return bret;
	}

	@Override
	public void onResponse(String arg0) {
		JSONObject json;
		try {
			json = new JSONObject(arg0);
			arg0 = json.toString(4);
			
			Iterator<String> keys = json.keys();
			while(keys.hasNext()){
				String key = keys.next();
			}
			//解析获取到的评论列表
			CommentList commentList = new CommentList();
			//评论列表包含两组数据，一个是热门评论，一个是新鲜评论
			//热门评论和新鲜评论都有可能是空的
			commentList.parseJson(json);
			
			int totalNumber = commentList.getTotalNumber();
			hasMore = commentList.isHasMore();
			
			//热门评论
			List<Comment> topComments = commentList.getTopComments();
			if(topComments!=null){
				hotComments.addAll(topComments);
				hotAdapter.notifyDataSetChanged();
				
			}
			
			//新鲜评论可能有数据
			List<Comment> rtComments = commentList.getRecentComments();
			if(rtComments!=null){
				recentComments.addAll(rtComments);
				recentAdapter.notifyDataSetChanged();
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
