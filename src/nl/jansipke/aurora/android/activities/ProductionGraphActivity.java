package nl.jansipke.aurora.android.activities;

import nl.jansipke.aurora.android.R;
import nl.jansipke.aurora.android.csv.CSVHandler;
import nl.jansipke.aurora.android.utils.AuroraGraph;
import nl.jansipke.aurora.android.utils.AuroraHttp;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.LinearLayout;

import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.GraphView.LegendAlign;

public class ProductionGraphActivity extends Activity {

	private String energyData = "";
	private ProgressDialog progressDialog;

	protected int minYValue = 0;
	protected int maxYValue = 10;

	private void drawGraph() {
		try {
			CSVHandler energyCsv = new CSVHandler(energyData);
			final String[] years = energyCsv.getColumn("year");
			final String[] months = energyCsv.getColumn("month");
			final String[] days = energyCsv.getColumn("day");
			final Double[] dailyEnergies = energyCsv.getDoubleColumn("daily_energy");

			BarGraphView graphView = new BarGraphView(this, "") {
				@Override
				protected String formatLabel(double value, boolean isValueX) {
					if (isValueX) {
						if (value == dailyEnergies.length) {
							value--;
						}
						int index = (int) value;
						return years[index] + "-" + months[index] + "-" + days[index];
					} else {
						return super.formatLabel(value, isValueX);
					}
				}
			};
			graphView.addSeries(AuroraGraph.getGraphViewSeries("Daily", Color.rgb(255, 0, 0), dailyEnergies));
			graphView.setShowLegend(true);
			graphView.setLegendAlign(LegendAlign.TOP);
			graphView.setScalable(true);
			graphView.setScrollable(true);
			graphView.setManualYAxisBounds(maxYValue, minYValue);
			graphView.setViewPort(0, years.length);

			LinearLayout layout = (LinearLayout) findViewById(R.id.graph);
			layout.addView(graphView);
		} catch (Exception e) {
		}
	}

	private void getHttpData() {
		try {
			energyData = AuroraHttp
					.getHttpContent("http://www.tjalk8.nl/pv/get_energy.php?oldest_first");
		} catch (Exception e) {
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.energy);

		updateScreen();
	}

	private void updateScreen() {
		progressDialog = ProgressDialog.show(this, "", "Loading data...", true);

		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				drawGraph();
				progressDialog.dismiss();
			}
		};

		new Thread(new Runnable() {
			public void run() {
				getHttpData();
				handler.sendEmptyMessage(0);
			}
		}).start();
	}
}
