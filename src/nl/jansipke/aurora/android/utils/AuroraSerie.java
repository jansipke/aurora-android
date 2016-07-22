package nl.jansipke.aurora.android.utils;

public class AuroraSerie {

	private String valueName;
	private String legendName;
	private int color;
	
	public AuroraSerie(String valueName, String legendName, int color) {
		this.valueName = valueName;
		this.legendName = legendName;
		this.color = color;
	}
	
	public String getValueName() {
		return valueName;
	}

	public String getLegendName() {
		return legendName;
	}

	public int getColor() {
		return color;
	}
}
