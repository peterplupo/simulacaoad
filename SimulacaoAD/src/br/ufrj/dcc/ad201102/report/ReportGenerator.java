package br.ufrj.dcc.ad201102.report;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

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
		
		
		for (BatchData batch : batches) {
			series.add(batch.getBatchNumber(), batch.getMeanPopulation());
		}
		
		data.addSeries(series);
		
        JFreeChart chart = ChartFactory.createXYLineChart(
            "Média",
            "Rodadas", 
            "Média da população", 
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
			
			for (Map.Entry<Double, Integer> population : batch.getPopulationSize().entrySet()) {
				sumPopulation = sumPopulation + population.getValue();
				System.out.println(sumPopulation);
				series.add(population.getKey()-batch.getStartTime(), (Number)sumPopulation);
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
