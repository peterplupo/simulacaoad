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
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import br.ufrj.dcc.ad201102.data.BatchData;

public class ReportGenerator {
	
	public static void getPopulationPMF(Collection<BatchData> batches) {
		XYSeriesCollection data = new XYSeriesCollection();

		XYSeries series = new XYSeries("Media da população");
		
		Map<Integer, Double> popProbDistRun = new TreeMap<Integer, Double>();
		double startTime = 0;
		double endTime = 0;
		boolean firstBatch = true;
		for (BatchData batch : batches) {
			if (firstBatch) {
				startTime = batch.getStartTime();
				firstBatch = false;
			}
			endTime = batch.getEndTime();
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
			series.add((Number)(prob.getKey()/(endTime-startTime)), prob.getValue());
		}
		
		data.addSeries(series);
		
        JFreeChart chart = ChartFactory.createXYLineChart(
            "PMF população",
            "Tempo", 
            "Tamanho da população", 
            data,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        );
        
        try {
			ChartUtilities.saveChartAsPNG(new File("mediaPopPMF.png"), chart, 600, 400);
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
	
	public static void getPopulationCDF(Collection<BatchData> batches) {
		XYSeriesCollection data = new XYSeriesCollection();

		
		for (BatchData batch : batches) {
			XYSeries series = new XYSeries(batch.getBatchNumber());
			double sumPopulation = 0;
			
			double[][] timePopulationSeries = new double[batch.getPopulationSize().size()][2];
			
			int i = 0;
			for (Map.Entry<Double, Integer> population : batch.getPopulationSize().entrySet()) {
				sumPopulation = sumPopulation + population.getValue();
				timePopulationSeries[i][0] = population.getKey();
				timePopulationSeries[i][1] = sumPopulation;
				i++;
			}
			
			for (double[] timePopulation : timePopulationSeries) {
				series.add(timePopulation[0]-batch.getStartTime(), timePopulation[1]/sumPopulation);
			}
				
			data.addSeries(series);
		}
		
		
		
        JFreeChart chart = ChartFactory.createXYLineChart(
            "Média",
            "Rodadas", 
            "Média da população", 
            data,
            PlotOrientation.VERTICAL,
            false,
            false,
            false
        );
        
        try {
			ChartUtilities.saveChartAsPNG(new File("mediaPopCDF.png"), chart, 600, 400);
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
	
	public static void getOutput(Collection<BatchData> batches) {
		XYSeriesCollection data = new XYSeriesCollection();

		DescriptiveStatistics output = new DescriptiveStatistics();
		XYSeries series = new XYSeries("Output");
		
		for (BatchData batch : batches) {
			output.addValue(batch.getExits());
		}
		
		data.addSeries(series);
		
        JFreeChart chart = ChartFactory.createXYLineChart(
            "Média",
            "Rodadas", 
            "Média da população", 
            data,
            PlotOrientation.VERTICAL,
            false,
            false,
            false
        );
        
        try {
			ChartUtilities.saveChartAsPNG(new File("mediaPopCDF.png"), chart, 600, 400);
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

}
