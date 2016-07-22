package nl.jansipke.aurora.android;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import nl.jansipke.aurora.android.csv.CSVHandler;
import nl.jansipke.aurora.android.utils.AuroraDate;
import nl.jansipke.aurora.android.utils.AuroraGraph;
import nl.jansipke.aurora.android.utils.AuroraHttp;
import nl.jansipke.aurora.android.utils.AuroraSerie;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.LegendAlign;
import com.jjoe64.graphview.LineGraphView;

public abstract class GraphActivity extends Activity {

	private int daysAgo = 0;
	
	private String graphData = "";
	private ProgressDialog progressDialog= null;

	protected List<AuroraSerie> auroraSeries = null;
	protected double minYValue = 0;
	protected double maxYValue = 1;

	private void drawGraph() {
		try {
	        AuroraDate auroraDate = new AuroraDate(Calendar.DAY_OF_YEAR, daysAgo);
	        
	        CSVHandler graphCsv = new CSVHandler(graphData);
			final String[] timestamps = graphCsv.getColumn("timestamp");
	
			GraphView graphView = new LineGraphView(this, auroraDate.toString()) {
				@Override
				protected String formatLabel(double value, boolean isValueX) {
					if (isValueX) {
						if (value == timestamps.length) {
							value--;
						}
						int index = (int) value;
						return getHourMin(timestamps[index]);
					} else {
						return super.formatLabel(value, isValueX);
					}
				}

				private String getHourMin(String timestamp) {
					Date date = new Date(Long.parseLong(timestamp) * 1000);
					SimpleDateFormat format = new SimpleDateFormat("HH:mm");
					return format.format(date);
				}
			};
	        for (AuroraSerie auroraSerie : auroraSeries) {
	        	String legendName = auroraSerie.getLegendName();
	        	String valueName = auroraSerie.getValueName();
	        	int color = auroraSerie.getColor();
	        	Double[] values = graphCsv.getDoubleColumn(valueName);
	        	graphView.addSeries(AuroraGraph.getGraphViewSeries(legendName, color, values));
	        }
	        graphView.setShowLegend(true);
	        graphView.setLegendAlign(LegendAlign.TOP);
	        graphView.setScalable(true);
	        graphView.setScrollable(true);
	        graphView.setManualYAxisBounds(maxYValue, minYValue);
	        graphView.setViewPort(0, timestamps.length);
	        
	        LinearLayout layout = (LinearLayout) findViewById(R.id.graph);
	        layout.removeAllViews();
	        layout.addView(graphView);
		} catch (Exception e) {
		}
	}

	private void getHttpData() {
		Calendar calendarStart = new GregorianCalendar();
		calendarStart.add(Calendar.DATE, -daysAgo);
		int yearStart = calendarStart.get(Calendar.YEAR);
		int monthStart = calendarStart.get(Calendar.MONTH);
		int dayStart = calendarStart.get(Calendar.DAY_OF_MONTH);
		
		Calendar calendarEnd = new GregorianCalendar();
		calendarEnd.add(Calendar.DATE, 1 - daysAgo);
		int yearEnd = calendarEnd.get(Calendar.YEAR);
		int monthEnd = calendarEnd.get(Calendar.MONTH);
		int dayEnd = calendarEnd.get(Calendar.DAY_OF_MONTH);
		
		long start = new GregorianCalendar(yearStart, monthStart, dayStart, 0, 0).getTimeInMillis() / 1000;
		long end = new GregorianCalendar(yearEnd, monthEnd, dayEnd, 0, 1).getTimeInMillis() / 1000;
		
		try {
			graphData = AuroraHttp.getHttpContent("http://www.tjalk8.nl/pv/get_measurements.php?start=" + start + "&end=" + end);
		} catch (Exception e) {
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.graph);
		
		Button prevButton = (Button) findViewById(R.id.button_prev);
		prevButton.setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
		        daysAgo++;
		        updateScreen();
		    }
		});
		
		Button nextButton = (Button) findViewById(R.id.button_next);
		nextButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (daysAgo > 0) {
					daysAgo--;
					updateScreen();
				}
			}
		});
		
		Button thisButton = (Button) findViewById(R.id.button_today);
		thisButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (daysAgo != 0) {
					daysAgo = 0;
					updateScreen();
				}
			}
		});
		
		updateScreen();
	}

	private void setButtonState() {
		Button prevButton = (Button) findViewById(R.id.button_prev);
		Button nextButton = (Button) findViewById(R.id.button_next);
		Button todayButton = (Button) findViewById(R.id.button_today);
		
		if (daysAgo == 0) {
			prevButton.setEnabled(true);
			nextButton.setEnabled(false);
			todayButton.setEnabled(false);
		} else {
			prevButton.setEnabled(true);
			nextButton.setEnabled(true);
			todayButton.setEnabled(true);
		}
    }
    
    private void updateScreen() {
    	progressDialog = ProgressDialog.show(this, "", "Loading data...", true);
    	
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
    			setButtonState();
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
