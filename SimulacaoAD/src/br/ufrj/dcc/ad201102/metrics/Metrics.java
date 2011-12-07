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
	}
	
	public static void addDownloadTime(int batchNumber, double time, double downloadTime) {
		Map<Double, Double> batchDownloadTimes = downloadTimes.get(batchNumber);
		if (batchDownloadTimes == null) {
			batchDownloadTimes = new TreeMap<Double, Double>();
			downloadTimes.put(batchNumber, batchDownloadTimes);
		}
		batchDownloadTimes.put(time, downloadTime);
	}
	
	public static void addPopulationSize(int batchNumber, double time, int size) {
		Map<Double, Integer> batchPopulationSize = populationSize.get(batchNumber);
		if (batchPopulationSize == null) {
			batchPopulationSize = new TreeMap<Double, Integer>();
			populationSize.put(batchNumber, batchPopulationSize);
		}
		batchPopulationSize.put(time, size);
	}
	
	public static void addExit(int batchNumber, double time) {
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
