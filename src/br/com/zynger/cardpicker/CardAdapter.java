package br.com.zynger.cardpicker;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;

public abstract class CardAdapter extends PagerAdapter {

	private OnCardClickListener mOnCardClickListener;
	
	@Override
	public final Object instantiateItem(ViewGroup viewPager, final int position) {
		RelativeLayout layout = new RelativeLayout(viewPager.getContext());

		View card = instantiateCard((ViewPager) viewPager, position);

		RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lps.addRule(RelativeLayout.CENTER_IN_PARENT);
		card.setLayoutParams(lps);
		
		card.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mOnCardClickListener != null) {
					mOnCardClickListener.onCardClick(v, position);
				}
			}
		});
		layout.addView(card);

		((ViewPager) viewPager).addView(layout, 0);
		return layout;
	}
	
	public abstract View instantiateCard(ViewPager viewpager, final int position);
	
	public abstract Drawable getCardAsDrawable(Context context, final int position);
	
	@Override
	public void destroyItem(ViewGroup viewPager, int position, Object view) {
		((ViewPager) viewPager).removeView((View) view);
	}
	
	@Override
	public final boolean isViewFromObject(View view, Object obj) {
		return view == obj;
	}
	
	protected final void setOnCardClickListener(OnCardClickListener onCardClickListener) {
		this.mOnCardClickListener = onCardClickListener;
	}
	
	public interface OnCardClickListener {
		boolean onCardClick(View card, int position);
	}
}
