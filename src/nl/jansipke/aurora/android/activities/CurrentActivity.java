package nl.jansipke.aurora.android.activities;

import java.util.ArrayList;

import nl.jansipke.aurora.android.GraphActivity;
import nl.jansipke.aurora.android.utils.AuroraSerie;

import android.graphics.Color;

public class CurrentActivity extends GraphActivity {

	public CurrentActivity() {
		auroraSeries = new ArrayList<AuroraSerie>();
		auroraSeries.add(new AuroraSerie("input1_current", "Input 1", Color.BLUE));
		minYValue = 0;
		maxYValue = 14;
	}
}
