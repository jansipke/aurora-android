package nl.jansipke.aurora.android.utils;

import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphView.GraphViewSeries;

public class AuroraGraph {

	public static GraphViewSeries getGraphViewSeries(String legendText, int lineColor, Double[] valueArray) {
        int size = valueArray.length;
        GraphViewData[] graphViewDatas = new GraphViewData[size];
        for (int i = 0; i < size; i++) {
        	Double value = (Double) valueArray[i];
        	graphViewDatas[i] = new GraphViewData(i, value);
        }
        return new GraphViewSeries(legendText, lineColor, graphViewDatas);
	}
	
	public static GraphViewSeries getGraphViewSeries(String legendText,	int lineColor, int size, int value) {
        GraphViewData[] graphViewDatas = new GraphViewData[size];
        for (int i = 0; i < size; i++) {
        	graphViewDatas[i] = new GraphViewData(i, value);
        }
        return new GraphViewSeries(legendText, lineColor, graphViewDatas);
	}
}
