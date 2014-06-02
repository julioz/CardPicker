package br.com.zynger.cardpicker;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button button = (Button) findViewById(R.id.cardpicker_button);
		final CardPicker cardPicker = (CardPicker) findViewById(R.id.cardpicker_container);
		
		button.setVisibility(View.GONE);
	}
}
