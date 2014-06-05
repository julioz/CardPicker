package br.com.zynger.cardpicker;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

public class CardPagerContainer extends RelativeLayout {
	
	private ViewPager mPager;

	public CardPagerContainer(Context context) {
		super(context);
		
		init();
	}
	
	public CardPagerContainer(Context context, AttributeSet attrs) {
		super(context, attrs);

		init();
	}

	public CardPagerContainer(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		init();
	}

	private void init() {
		LayoutInflater.from(getContext()).inflate(R.layout.cardpager, this,
				true);
		
		mPager = (ViewPager) findViewById(R.id.cardpicker_pager);
	}

	public void setAdapter(CardAdapter cardAdapter) {
		mPager.setAdapter(cardAdapter);
	}

	public int getCurrentItem() {
		return mPager.getCurrentItem();
	}
	
	public void setOverlayColor(int color) {
		mPager.setBackgroundColor(color);
	}
}
