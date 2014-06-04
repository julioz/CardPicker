package br.com.zynger.cardpicker.sample;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import br.com.zynger.cardpicker.CardAdapter.OnCardClickListener;
import br.com.zynger.cardpicker.CardPicker;
import br.com.zynger.cardpicker.R;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button button = (Button) findViewById(R.id.cardpicker_button);
		CardPicker cardPicker = (CardPicker) findViewById(R.id.cardpicker);
		CardPagerAdapter cardAdapter = new CardPagerAdapter();
		cardPicker.setCardAdapter(cardAdapter);
		
		cardPicker.setOnCardClickListener(new OnCardClickListener() {
			@Override
			public boolean onCardClick(View card, int position) {
				Toast.makeText(card.getContext(), "Clicked card " + position, Toast.LENGTH_SHORT).show();
				Log.d("CardPicker", "clicked card " + position);
				return true;
			}
		});
		
		button.setVisibility(View.GONE);
	}
}
