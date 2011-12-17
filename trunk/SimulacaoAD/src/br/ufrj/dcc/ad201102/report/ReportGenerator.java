package br.ufrj.dcc.ad201102.report;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYErrorRenderer;
import org.jfree.data.xy.XYIntervalSeries;
import org.jfree.data.xy.XYIntervalSeriesCollection;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import br.ufrj.dcc.ad201102.data.BatchData;
import br.ufrj.dcc.ad201102.data.Measurement;

public class ReportGenerator {
	
	public static void getPopulationSize(String filePrefix, Collection<BatchData> batches) {
		XYSeriesCollection data = new XYSeriesCollection();

		XYSeries series = new XYSeries("Tamanho da população");
		XYSeries series2 = new XYSeries("Vazão média");
		XYSeries series3 = new XYSeries("Tempo médio de download");
		
		for (BatchData batch : batches) {
			series2.add(batch.getStartTime(), batch.getOutput());
			series3.add(batch.getStartTime(), batch.getMeanDownloadTime()/100);
			for (Map.Entry<Double, Integer> popTime : batch.getPopulationSize().entrySet()) {
				series.add((Number)popTime.getKey(), popTime.getValue());
			}
		}
		
		data.addSeries(series);
		data.addSeries(series2);
		data.addSeries(series3);
		
        JFreeChart chart = ChartFactory.createXYLineChart(
            "PMF população",
            "Tempo", 
            "Tamanhos", 
            data,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        );
        
        try {
			ChartUtilities.saveChartAsPNG(new File(filePrefix + "popVazaoDownloadTempo.png"), chart, 600, 400);
		} catch (IOException e) {
		}
        
	}
	
	public static void getPopulationPMF(String filePrefix, Collection<BatchData> batches) {
		XYSeriesCollection data = new XYSeriesCollection();

		XYSeries series = new XYSeries("Media da população");
		
		Map<Integer, Double> popProbDistRun = new TreeMap<Integer, Double>();
		for (BatchData batch : batches) {
//			series.add(batch.getBatchNumber(), batch.getMeanPopulation());
			Map<Integer, Double> popProbDistBatch = batch.getPopulationProbabilityDistribution();
			for (Map.Entry<Integer, Double> prob : popProbDistBatch.entrySet()) {
				if (popProbDistRun.get(prob.getKey()) == null) {
					popProbDistRun.put(prob.getKey(), prob.getValue());
				} else {
					popProbDistRun.put(prob.getKey(), popProbDistRun.get(prob.getKey()) + prob.getValue());
				}
			}
			
		}
		
		for (Map.Entry<Integer, Double> prob : popProbDistRun.entrySet()) {
			series.add((Number)prob.getKey(), prob.getValue()/batches.size());
		}
		
		data.addSeries(series);
		
        JFreeChart chart = ChartFactory.createXYLineChart(
            "PMF população",
            "Tamanho da população", 
            "Tempo", 
            data,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        );
        
        try {
			ChartUtilities.saveChartAsPNG(new File(filePrefix + "mediaPopPMF.png"), chart, 600, 400);
		} catch (IOException e) {
		}
        
//        ChartPanel chartPanel = new ChartPanel(chart);
//        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
//        JFrame frame = new JFrame();
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.getContentPane().add(chartPanel);
//        frame.pack();
//        frame.setVisible(true);
	}
	
	public static void getDownloadTimeCDF(String filePrefix, Collection<BatchData> batches) {
		XYSeriesCollection data = new XYSeriesCollection();

		for (BatchData batch : batches) {
			XYSeries series = new XYSeries(batch.getBatchNumber());
			
			TreeMap<Double, Integer> timesFrequency = new TreeMap<Double, Integer>();
			
			for (Double downloadTime : batch.getDownloadTimes()) {
				Integer times = timesFrequency.get(downloadTime);
				if (times == null) {
					timesFrequency.put(downloadTime, 1);
				} else {
					timesFrequency.put(downloadTime, timesFrequency.get(downloadTime) + 1);
				}
			}
			
			double acc = 0;
			for (Map.Entry<Double, Integer> timeFrequency : timesFrequency.entrySet()) {
				acc = acc + ((double)timeFrequency.getValue())/batch.getDownloadTimes().length;
				series.add((Number)timeFrequency.getKey(), acc);
			}
			
			data.addSeries(series);
		}
		
		
		
        JFreeChart chart = ChartFactory.createXYLineChart(
            "CDF Tempo de Download",
            "Tempo de download", 
            "Tempo acumulado normalizado", 
            data,
            PlotOrientation.VERTICAL,
            false,
            false,
            false
        );

        try {
			ChartUtilities.saveChartAsPNG(new File(filePrefix + "downloadTimeCDF.png"), chart, 600, 400);
		} catch (IOException e) {
		}
        
	}
	
	public static void getOutput(String filePrefix, boolean fetchTransient) {

		XYIntervalSeries xyintervalseries = new XYIntervalSeries("");
		
		int runsCounter = 0;
		for (Map<Integer, BatchData> simulationData : Measurement.getRuns().values()) {
			runsCounter++;
			
			DescriptiveStatistics output = new DescriptiveStatistics();
			for(BatchData batch : simulationData.values()) {
				if (batch.getBatchNumber() == -1 && !fetchTransient) {
					continue;
				}
				output.addValue(batch.getOutput());
			}
			xyintervalseries.add(runsCounter, runsCounter, runsCounter, output.getMean(), output.getMean() - Measurement.getConfidenceInterval95(output.getVariance(), output.getN()), output.getMean() + Measurement.getConfidenceInterval95(output.getVariance(), output.getN()));
		}
		
		XYIntervalSeriesCollection xyintervalseriescollection = new XYIntervalSeriesCollection();
		xyintervalseriescollection.addSeries(xyintervalseries);
		NumberAxis numberaxis = new NumberAxis("Rodada");
		NumberAxis numberaxis1 = new NumberAxis("Média da vazão (com IC)");
		XYErrorRenderer xyerrorrenderer = new XYErrorRenderer();
		XYPlot xyplot = new XYPlot(xyintervalseriescollection, numberaxis, numberaxis1, xyerrorrenderer);
		
		
		JFreeChart chart = new JFreeChart("Vazão média", xyplot);
		
        try {
			ChartUtilities.saveChartAsPNG(new File(filePrefix + "vazaoMedia.png"), chart, 600, 400);
		} catch (IOException e) {
		}
        
	}
	
	public static void getMeanDownloadTime(String filePrefix, Collection<BatchData> batches) {
		DescriptiveStatistics stats = new DescriptiveStatistics();
		DescriptiveStatistics statsMeanPopulation = new DescriptiveStatistics();
		for (BatchData batch : batches) {
			stats.addValue(batch.getMeanDownloadTime());
			statsMeanPopulation.addValue(batch.getMeanPopulation());
		}
		System.out.println();
		System.out.println("=============================== Statistics");
		System.out.println("Download Time - Mean " + stats.getMean());
		System.out.println("Download Time - Lower CI " + (stats.getMean() - Measurement.getConfidenceInterval95(stats.getVariance(), stats.getN())));
		System.out.println("Download Time - Higher CI " + (stats.getMean() + Measurement.getConfidenceInterval95(stats.getVariance(), stats.getN())));
		System.out.println();
		System.out.println("Population - Mean: " + statsMeanPopulation.getMean());
	}

}
