package br.com.zynger.cardpicker;

import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class CardPagerAdapter extends PagerAdapter {

	private int CARDS[] = { R.drawable.credit_card_blue,
			R.drawable.credit_card_green, R.drawable.credit_card_red };
	
	private OnCardItemClickListener mOnCardItemClickListener;

	@Override
	public Object instantiateItem(ViewGroup viewPager, final int position) {
		RelativeLayout layout = new RelativeLayout(viewPager.getContext());

		ImageView iv = new ImageView(viewPager.getContext());
		Drawable cardDrawable = viewPager.getContext().getResources()
				.getDrawable(CARDS[position]);
		iv.setImageDrawable(cardDrawable);

		RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lps.addRule(RelativeLayout.CENTER_IN_PARENT);
		iv.setLayoutParams(lps);
		layout.addView(iv);
		
		iv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mOnCardItemClickListener != null) {
					mOnCardItemClickListener.onCardClick(v, position);
				}
			}
		});

		((ViewPager) viewPager).addView(layout, 0);
		return layout;
	}

	@Override
	public void destroyItem(ViewGroup viewPager, int position, Object view) {
		((ViewPager) viewPager).removeView((View) view);
	}

	public int getCardDrawableId(int position) {
		return CARDS[position];
	}

	@Override
	public int getCount() {
		return CARDS.length;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	public void setOnCardItemClickListener(OnCardItemClickListener onCardClickListener) {
		this.mOnCardItemClickListener = onCardClickListener;
	}
	
	public interface OnCardItemClickListener {
		void onCardClick(View card, int position);
	}
}