package nl.jansipke.aurora.android.activities;

import java.util.ArrayList;

import nl.jansipke.aurora.android.GraphActivity;
import nl.jansipke.aurora.android.utils.AuroraSerie;

import android.graphics.Color;


public class PowerActivity extends GraphActivity {

	public PowerActivity() {
		auroraSeries = new ArrayList<AuroraSerie>();
		auroraSeries.add(new AuroraSerie("grid_power", "Grid", Color.RED));
		auroraSeries.add(new AuroraSerie("input1_power", "Input 1", Color.BLUE));
		minYValue = 0;
		maxYValue = 2100;
	}
}
