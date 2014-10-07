package com.scorpion.neihan.activity;

import java.util.List;

import com.scorpion.neihan.R;
import com.scorpion.neihan.adapters.DetailPagerAdapter;
import com.scorpion.neihan.bean.DataStore;
import com.scorpion.neihan.bean.TextEntity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.widget.Adapter;

public class EssayDetailActivity extends FragmentActivity {
	private ViewPager pager;
	private DetailPagerAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_essay_detail);
		pager = (ViewPager)findViewById(R.id.detail_pager_content);
		
		Intent intent = new Intent();
		Bundle extras = intent.getExtras();
		int category = 1;
		int currentEssayPosition = 0;
		if (extras!=null) {
			category = extras.getInt("category",1);
		}
		DataStore dataStore = DataStore.getInstance();
		List<TextEntity> entities = null;
		if (category == 1) {
			entities = dataStore.getTextEntities();
		}else if (category == 2) {
			entities = dataStore.getImageEntities();
		}
		
		adapter = new DetailPagerAdapter(getSupportFragmentManager(), entities);
		pager.setAdapter(adapter); 
		
		if (currentEssayPosition>0) {
			
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.essay_detail, menu);
		return true;
	}

}
