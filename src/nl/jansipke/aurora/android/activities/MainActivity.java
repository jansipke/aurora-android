package nl.jansipke.aurora.android.activities;

import nl.jansipke.aurora.android.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}
	
	public void showCurrent(View view) {
		Intent intent = new Intent(MainActivity.this, CurrentActivity.class);
		startActivity(intent);
	}
	
	public void showEnergy(View view) {
		Intent intent = new Intent(MainActivity.this, ProductionGraphActivity.class);
		startActivity(intent);
	}
	
	public void showPower(View view) {
		Intent intent = new Intent(MainActivity.this, PowerActivity.class);
		startActivity(intent);
	}

	public void showProduction(View view) {
		Intent intent = new Intent(MainActivity.this, ProductionTableActivity.class);
		startActivity(intent);
	}
	
	public void showTemperature(View view) {
		Intent intent = new Intent(MainActivity.this, TemperatureActivity.class);
		startActivity(intent);
	}

	public void showVoltage(View view) {
		Intent intent = new Intent(MainActivity.this, VoltageActivity.class);
		startActivity(intent);
	}
}