package nl.jansipke.aurora.android.activities;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Calendar;

import nl.jansipke.aurora.android.R;
import nl.jansipke.aurora.android.csv.CSVHandler;
import nl.jansipke.aurora.android.utils.AuroraDate;
import nl.jansipke.aurora.android.utils.AuroraHttp;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class ProductionTableActivity extends Activity {

	private final static int DAILY_ENERGY_MIN_COLOR = Color.RED;
	private final static int DAILY_ENERGY_MAX_COLOR = Color.GREEN;
	private final static double SYSTEM_WP = 1.410;
	private final static int WEEKEND_COLOR = Color.rgb(40, 40, 40);

	private final DecimalFormat decimalFormat = new DecimalFormat("0.000");
	
	private String energyData = "";
	private String statisticsData = "";
	private int monthsAgo = 0;
	private ProgressDialog progressDialog = null;

	private LinearLayout createDailyLayout(int year, int month, int day, Double dailyEnergy, Double min, Double max) {
		LinearLayout linearLayout = new LinearLayout(this);
		
		int backgroundColor = Color.BLACK;
		try {
			AuroraDate auroraDate = new AuroraDate(year, month, day);
			if (auroraDate.isWeekend()) {
				backgroundColor = WEEKEND_COLOR;
			}
		} catch (ParseException e) {
		}
		
		TextView timestampTextView = new TextView(this);
		timestampTextView.setBackgroundColor(backgroundColor);
		timestampTextView.setText("" + day);
		timestampTextView.setTextSize(16);
		linearLayout.addView(timestampTextView);
		
		TextView dailyEnergyTextView = new TextView(this);
		dailyEnergyTextView.setBackgroundColor(backgroundColor);
		dailyEnergyTextView.setText(decimalFormat.format(dailyEnergy));
		if (dailyEnergy < min) {
			dailyEnergyTextView.setTextColor(DAILY_ENERGY_MIN_COLOR);
		} else if (dailyEnergy > max) {
			dailyEnergyTextView.setTextColor(DAILY_ENERGY_MAX_COLOR);
		}
		dailyEnergyTextView.setTextSize(16);
		dailyEnergyTextView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		dailyEnergyTextView.setGravity(Gravity.RIGHT);
		linearLayout.addView(dailyEnergyTextView);
		
		return linearLayout;
	}
	
	private LinearLayout createLineLayout() {
		LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setBackgroundColor(Color.DKGRAY);
		linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, 2));
		
		return linearLayout;
	}
	
	private void drawText() {
		LinearLayout dailyLinearLayout1 = (LinearLayout) findViewById(R.id.production_daily_1);
		LinearLayout dailyLinearLayout2 = (LinearLayout) findViewById(R.id.production_daily_2);
		LinearLayout dailyLinearLayout3 = (LinearLayout) findViewById(R.id.production_daily_3);
		
		TextView periodLabelTextView = (TextView) findViewById(R.id.text_label_period);
		
        AuroraDate auroraDate = new AuroraDate(Calendar.MONTH, monthsAgo);
		periodLabelTextView.setText(auroraDate.getMonthYearString());
		
		TextView totalValueTextView = (TextView) findViewById(R.id.text_value_total);
		TextView averageValueTextView = (TextView) findViewById(R.id.text_value_average);
		TextView periodLobTextView = (TextView) findViewById(R.id.text_lob_period);
		TextView periodValueTextView = (TextView) findViewById(R.id.text_value_period);
		
		try {
			CSVHandler statisticsCsv = new CSVHandler(statisticsData);
			double pct10 = Double.parseDouble(statisticsCsv.getColumn("pct10")[0]);
			double average = Double.parseDouble(statisticsCsv.getColumn("average")[0]);
			double pct90 = Double.parseDouble(statisticsCsv.getColumn("pct90")[0]);
	        averageValueTextView.setText(decimalFormat.format(average) + "  kWh / day");

	        dailyLinearLayout1.removeAllViews();
	        dailyLinearLayout2.removeAllViews();
	        dailyLinearLayout3.removeAllViews();
	        
	        double periodEnergy = 0;
	        double totalEnergy = 0;
	        CSVHandler energyCsv = new CSVHandler(energyData);
	        for (int i = 0; i < energyCsv.getSize(); i++) {
	        	int year = Integer.parseInt(energyCsv.getColumn("year")[i]);
	        	int month = Integer.parseInt(energyCsv.getColumn("month")[i]);
	        	if (year == auroraDate.getYear() && month == auroraDate.getMonth()) {
		        	int day = Integer.parseInt(energyCsv.getColumn("day")[i]);
		        	double dailyEnergy = Double.parseDouble(energyCsv.getColumn("daily_energy")[i]);
		        	totalEnergy = Double.parseDouble(energyCsv.getColumn("total_energy")[i]);
	        		if (day <= 10) {
			        	dailyLinearLayout1.addView(createDailyLayout(year, month, day, dailyEnergy, pct10, pct90));
			        	dailyLinearLayout1.addView(createLineLayout());
	        		} else if (day <= 20) {
	        			dailyLinearLayout2.addView(createDailyLayout(year, month, day, dailyEnergy, pct10, pct90));
	        			dailyLinearLayout2.addView(createLineLayout());
	        		} else {
	        			dailyLinearLayout3.addView(createDailyLayout(year, month, day, dailyEnergy, pct10, pct90));
	        			dailyLinearLayout3.addView(createLineLayout());
	        		}
		        	periodEnergy += dailyEnergy;
	        	}
	        }
	        totalValueTextView.setText(decimalFormat.format(totalEnergy) + "  kWh");
	        periodLobTextView.setText(decimalFormat.format(periodEnergy / SYSTEM_WP) + "  kWh / kWp");
	        periodValueTextView.setText(decimalFormat.format(periodEnergy) + "  kWh");
		} catch (Exception e) {
			totalValueTextView.setText(e.getMessage());
		}
	}
	
	public void getHttpData() {
		try {
			energyData = AuroraHttp.getHttpContent("http://www.tjalk8.nl/pv/get_energy.php?oldest_first");
			statisticsData = AuroraHttp.getHttpContent("http://www.tjalk8.nl/pv/get_statistics.php");
		} catch (Exception e) {
		}
	}

    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.production);

		Button prevButton = (Button) findViewById(R.id.button_prev);
		prevButton.setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
		        monthsAgo++;
		        updateScreen();
		    }
		});
		
		Button nextButton = (Button) findViewById(R.id.button_next);
		nextButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (monthsAgo > 0) {
					monthsAgo--;
					updateScreen();
				}
			}
		});
		
		Button todayButton = (Button) findViewById(R.id.button_today);
		todayButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (monthsAgo != 0) {
					monthsAgo = 0;
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
		
		if (monthsAgo == 0) {
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
		if (energyData == "") {
			progressDialog = ProgressDialog.show(this, "", "Loading data...", true);
		}
    	
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
    			setButtonState();
    			drawText();
				progressDialog.dismiss();
			}
		};
    	
    	new Thread(new Runnable() {
    		public void run() {
    			if (energyData == "") {
    				getHttpData();
    			}
    			handler.sendEmptyMessage(0);
    		}
    	}).start();
    }
}
