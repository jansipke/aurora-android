package nl.jansipke.aurora.android.csv;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVHandler {

	private final List<String> headers;
	private final List<String[]> body;
	
	public CSVHandler(String content) {
		if (content == null || content == "") {
			throw new IllegalArgumentException("CSV content may not be empty");
		}
		String[] lines = content.split("\n");
		headers = new ArrayList<String>(Arrays.asList(lines[0].split(",")));
		body = new ArrayList<String[]>();
		for (int i = 1; i < lines.length; i++) {
			body.add(lines[i].split(","));
		}
	}
	
	public String[] getColumn(String columnName) {
		if (!headers.contains(columnName)) {
			throw new IllegalArgumentException("Column with name " + columnName + " does not exist");
		}
		int columnIndex = headers.indexOf(columnName);
		List<String> columnValues = new ArrayList<String>();
		for (String[] line : body) {
			columnValues.add(line[columnIndex]);
		}
		return columnValues.toArray(new String[0]);
	}
	
	public Double[] getDoubleColumn(String columnName) {
		String[] strings = getColumn(columnName);
		Double[] doubles = new Double[strings.length];
		for (int i = 0; i < strings.length; i++) {
			doubles[i] = Double.parseDouble(strings[i]);
		}
		return doubles;
	}
	
	public int getSize() {
		return body.size();
	}
}
