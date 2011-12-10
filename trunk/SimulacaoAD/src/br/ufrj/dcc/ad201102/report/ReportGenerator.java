package br.ufrj.dcc.ad201102.report;

import java.util.Collection;
import java.util.Map;

import javax.swing.JFrame;

import org.apache.commons.math.stat.descriptive.SummaryStatistics;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import br.ufrj.dcc.ad201102.data.BatchData;

public class ReportGenerator {
	
	public static void getPopulationReport(Collection<BatchData> batches) {
		XYSeriesCollection data = new XYSeriesCollection();

		XYSeries series = new XYSeries("Media da população");
		
		
		for (BatchData batch : batches) {
			
			double lastTime = batch.getStartTime();
			double lastPop = batch.getInitialPopulation();
			
			SummaryStatistics stat = new SummaryStatistics();
			for (Map.Entry<Double, Integer> size : batch.getPopulationSize().entrySet()) {
				stat.addValue(lastPop*(size.getKey()-lastTime));
				lastPop = size.getValue();
				lastTime = size.getKey();
				
			}
			//sumPairs(means.toArray(new Double[means.size()]))[0]
			series.add(batch.getBatchNumber(),  stat.getSum()/(batch.getEndTime() - batch.getStartTime()));
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
        
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(chartPanel);
        frame.pack();
        frame.setVisible(true);
	}

}
