package nl.jansipke.aurora.android.activities;

import java.util.ArrayList;

import nl.jansipke.aurora.android.GraphActivity;
import nl.jansipke.aurora.android.utils.AuroraSerie;

import android.graphics.Color;

public class VoltageActivity extends GraphActivity {

	public VoltageActivity() {
		auroraSeries = new ArrayList<AuroraSerie>();
		auroraSeries.add(new AuroraSerie("grid_voltage", "Grid", Color.RED));
		auroraSeries.add(new AuroraSerie("input1_voltage", "Input 1", Color.BLUE));
		minYValue = 0;
		maxYValue = 350;
	}
}
