package br.ufrj.dcc.ad201102.metrics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Metrics {
	
	private static int currentBatch = -1;
	
	private static Map<Integer, Map<Double, Double>> downloadTimes = new TreeMap<Integer, Map<Double, Double>>();
	private static Map<Integer, Map<Double, Integer>> populationSize = new TreeMap<Integer, Map<Double, Integer>>();
	private static Map<Integer, Collection<Double>> exits = new TreeMap<Integer, Collection<Double>>();
	
	static {
		reset();
	}
	
	public static void reset() {
		downloadTimes = new TreeMap<Integer, Map<Double, Double>>();
		populationSize = new TreeMap<Integer, Map<Double, Integer>>();
		exits = new TreeMap<Integer, Collection<Double>>();
		currentBatch = -1;
	}
	
	public static boolean checkCurrentBatch(int batchNumber) {
		//ignores from previous batches
		if (currentBatch > batchNumber) {
			return true;
		}
		//sets the current batch
		if (currentBatch < batchNumber) {
			currentBatch = batchNumber;
		}
		return false;
	}
	
	public static void addDownloadTime(int batchNumber, double time, double downloadTime) {
		if (checkCurrentBatch(batchNumber)) {
			return;
		}
		Map<Double, Double> batchDownloadTimes = downloadTimes.get(batchNumber);
		if (batchDownloadTimes == null) {
			batchDownloadTimes = new TreeMap<Double, Double>();
			downloadTimes.put(batchNumber, batchDownloadTimes);
		}
		batchDownloadTimes.put(time, downloadTime);
	}
	
	public static void addPopulationSize(int batchNumber, double time, int size) {
		if (checkCurrentBatch(batchNumber)) {
			return;
		}
		Map<Double, Integer> batchPopulationSize = populationSize.get(batchNumber);
		if (batchPopulationSize == null) {
			batchPopulationSize = new TreeMap<Double, Integer>();
			populationSize.put(batchNumber, batchPopulationSize);
		}
		batchPopulationSize.put(time, size);
	}
	
	public static void addExit(int batchNumber, double time) {
		if (checkCurrentBatch(batchNumber)) {
			return;
		}
		Collection<Double> batchExits = exits.get(batchNumber);
		if (batchExits == null) {
			batchExits = new ArrayList<Double>();
			exits.put(batchNumber, batchExits);
		}
		batchExits.add(time);
	}

	public static Map<Integer, Map<Double, Double>> getDownloadTimes() {
		return downloadTimes;
	}

	public static Map<Integer, Map<Double, Integer>> getPopulationSize() {
		return populationSize;
	}
	
	public static Map<Integer, Collection<Double>> getExits() {
		return exits;
	}
	
	public static Map<Double, Integer> getPopulationSize(int batchNumber) {
		return populationSize.get(batchNumber);
	}
	
	public static Map<Double, Double> getDownloadTimes(int batchNumber) {
		return downloadTimes.get(batchNumber);
	}

	public static Collection<Double> getExits(int batchNumber) {
		return exits.get(batchNumber);
	}
	
	public static double mean(Collection<Double> values) {
		if( values.size() == 0) {
			return 0;
		}
		return sumPairs(values.toArray(new Double[values.size()]))[0]/values.size();
	}
	
	public static double variance(Collection<Double> values) {
		if( values.size() == 0) {
			return 0;
		}
		return sumPairsVariance(values.toArray(new Double[values.size()]), mean(values))[0]/(values.size()-1);
	}
	
	private static Double[] sumPairsVariance(Double[] values, double mean) {
		for (int i = 0; i<values.length; i++) {
			values[i] = (values[i] - mean) * (values[i] - mean);
		}
		return sumPairs(values);
	}
	
	public static boolean ic95Integer(Collection<Integer> values) {
		Collection<Double> doubleValues = new ArrayList<Double>(values.size());
		for(Integer value : values) {
			doubleValues.add(value.doubleValue());
		}
		return ic95(doubleValues);
	}
	
	public static boolean ic95(Collection<Double> values) {
        if (((2 * 1.96 * variance(values))/Math.sqrt(values.size()))<0.1*mean(values)) {
        	return true;
        }
        return false;
	}
	
	private static Double[] sumPairs(Double[] values) {
		if (values.length == 0) {
			Double[] sums = new Double[1];
			sums[0] = 0d;
			return sums;
		}
		if (values.length == 1) {
			return values;
		}
		Double[] sums = new Double[values.length/2];
		int offset = 0;
		int counter = 0;
		if (values.length % 2 == 1) {
			sums[0] = values[0] + values[1] + values[2];
			offset = 2;
			counter = 1;
		}
		for (; counter < sums.length; counter++) {
			sums[counter] = values[offset + counter] + values[offset + counter+1];
		}
		return sumPairs(sums);
	}

	public static void generateReport() {
		XYSeriesCollection data = new XYSeriesCollection();
		int i = 0;
		int size = populationSize.size();
		Map.Entry<Double, Integer> populationTimeLastSeries = null;
		if (populationSize.keySet().iterator().next() == -1) {
			i = -1;
			size = populationSize.size()-1;
		}
		for (; i<size; i++) {
			
			XYSeries series = null;
			if (i == -1) {
				series = new XYSeries("Transiente");
			} else {
				series = new XYSeries((i+1) + "o. Batch");
			}
			if (populationTimeLastSeries != null) {
				series.add(populationTimeLastSeries.getKey(), populationTimeLastSeries.getValue());
			}
			for (Map.Entry<Double, Integer> populationTime : populationSize.get(i).entrySet()) {
		        series.add(populationTime.getKey(), populationTime.getValue());
		        populationTimeLastSeries = populationTime;
			}
	        data.addSeries(series);
		}
		
        JFreeChart chart = ChartFactory.createXYLineChart(
            "População",
            "Tempo", 
            "Peers", 
            data,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        );
        
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(chartPanel);
        frame.pack();
        frame.setVisible(true);
	}
	
}
