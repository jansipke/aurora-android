package nl.jansipke.aurora.android.activities;

import java.util.ArrayList;

import nl.jansipke.aurora.android.GraphActivity;
import nl.jansipke.aurora.android.utils.AuroraSerie;

import android.graphics.Color;

public class TemperatureActivity extends GraphActivity {

	public TemperatureActivity() {
		auroraSeries = new ArrayList<AuroraSerie>();
		auroraSeries.add(new AuroraSerie("booster_temperature", "Booster", Color.RED));
		auroraSeries.add(new AuroraSerie("inverter_temperature", "Inverter", Color.BLUE));
		minYValue = 0;
		maxYValue = 70;
	}
}
