package br.com.zynger.cardpicker.sample;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import br.com.zynger.cardpicker.CardAdapter;
import br.com.zynger.cardpicker.R;

public class CardPagerAdapter extends CardAdapter {

	private int CARDS[] = { R.drawable.credit_card_blue,
			R.drawable.credit_card_green, R.drawable.credit_card_red };
	
	@Override
	public View instantiateCard(ViewPager viewpager, int position) {
		ImageView iv = new ImageView(viewpager.getContext());
		int padding = Math.round(25 * viewpager.getContext().getResources().getDisplayMetrics().density);
		iv.setPadding(padding , padding, padding, padding);
		Drawable cardDrawable = viewpager.getContext().getResources()
				.getDrawable(CARDS[position]);
		iv.setImageDrawable(cardDrawable);
		return iv;
	}
	
	@Override
	public int getCount() {
		return CARDS.length;
	}

	@Override
	public Drawable getCardAsDrawable(Context context, final int position) {
		int cardId = CARDS[position];
		return context.getResources().getDrawable(cardId);
	}
}