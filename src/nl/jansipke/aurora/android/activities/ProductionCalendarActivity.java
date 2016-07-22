package nl.jansipke.aurora.android.activities;

import java.text.DecimalFormat;
import java.util.Calendar;

import nl.jansipke.aurora.android.R;
import nl.jansipke.aurora.android.utils.AuroraDate;
import nl.jansipke.aurora.android.utils.AuroraHttp;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

public class ProductionCalendarActivity extends Activity {

	private final DecimalFormat decimalFormat = new DecimalFormat("0.000");
	
	private String httpData = "";
	private int monthsAgo = 0;
	private ProgressDialog progressDialog = null;

	private void drawText() {
		LinearLayout dailyLinearLayout =(LinearLayout) findViewById(R.id.production_daily_1);
		
		TextView totalLabelTextView = (TextView) findViewById(R.id.text_label_total);
		totalLabelTextView.setText("Total");
		
		TextView periodLabelTextView = (TextView) findViewById(R.id.text_label_period);
		
        AuroraDate auroraDate = new AuroraDate(Calendar.MONTH, monthsAgo);
		periodLabelTextView.setText(auroraDate.getMonthYear());
		String timestampMustStartWith = auroraDate.getYearMonth();
		
		TextView totalValueTextView = (TextView) findViewById(R.id.text_value_total);

		TextView periodValueTextView = (TextView) findViewById(R.id.text_value_period);
		
	    TableLayout calendarTableLayout = (TableLayout) findViewById(R.id.calendar);
		
		try {
	        final JSONObject jsonObject = new JSONObject(httpData);
	        final JSONArray timestampArray = jsonObject.getJSONArray("timestamp");
	        final JSONArray dailyEnergyArray = jsonObject.getJSONArray("daily_energy");
	        final JSONArray totalEnergyArray = jsonObject.getJSONArray("total_energy");
	        
	        dailyLinearLayout.removeAllViews();
	        double periodEnergy = 0;
	        for (int i = dailyEnergyArray.length() - 1; i >= 0; i--) {
	        	String timestamp = (String) timestampArray.get(i);
	        	Double dailyEnergy = (Double) dailyEnergyArray.get(i);
	        	if (timestamp.startsWith(timestampMustStartWith)) {
		        	periodEnergy += dailyEnergy;
	        	}
	        }
	        Double totalEnergy = (Double) totalEnergyArray.get(totalEnergyArray.length() - 1);
	        totalValueTextView.setText(decimalFormat.format(totalEnergy) + "   kWh");
	        periodValueTextView.setText(decimalFormat.format(periodEnergy) + "   kWh");
		} catch (Exception e) {
			totalValueTextView.setText(e.getMessage());
		}
	}
	
	public String getHttpData() {
		try {
			httpData = AuroraHttp.getHttpContent("http://home.tjalk8.nl/aurora/energy?nr_items=365");
			return httpData;
		} catch (Exception e) {
			return "";
		}
	}

    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.production_calendar);

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
		if (httpData == "") {
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
    			if (httpData == "") {
    				getHttpData();
    			}
    			handler.sendEmptyMessage(0);
    		}
    	}).start();
    }
}
