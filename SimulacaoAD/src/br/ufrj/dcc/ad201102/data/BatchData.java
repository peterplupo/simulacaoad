package br.ufrj.dcc.ad201102.data;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

public class BatchData implements Comparable<BatchData> {
	
	private double startTime;
	private double endTime;
	private int initialPopulation;
	private String id;
	private Map<Double, Double> downloadTimes = new TreeMap<Double, Double>();
	private Map<Double, Integer> populationSize = new TreeMap<Double, Integer>();
	private Collection<Double> exits = new TreeSet<Double>();
	private int batchNumber;

	public BatchData(int batchNumber) {
		this.setBatchNumber(batchNumber);
		id = "Batch #" + batchNumber;
	}
	
	@Override
	public String toString() {
		return id;
	}

	public double getEndTime() {
		return endTime;
	}

	public void setEndTime(double endTime) {
		this.endTime = endTime;
	}

	public double getStartTime() {
		return startTime;
	}

	public void setStartTime(double startTime) {
		this.startTime = startTime;
	}

	public void addPopulationSize(double time, int size) {
		if (invalidTime(time)) {
			return;
		}
		populationSize.put(time, size);
	}

	public void addExit(double time) {
		if (invalidTime(time)) {
			return;
		}
		exits.add(time);
	}

	public void addDownloadTime(double time, double downloadTime) {
		if (invalidTime(time)) {
			return;
		}
		downloadTimes.put(time, downloadTime);
	}

	public int getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(int batchNumber) {
		this.batchNumber = batchNumber;
	}
	
	private boolean invalidTime(double time) {
		return !(endTime == 0 || time < endTime);
	}

	public Map<Double, Integer> getPopulationSize() {
		return populationSize;
	}

	public int getInitialPopulation() {
		return initialPopulation;
	}

	public void setInitialPopulation(int initialPopulation) {
		this.initialPopulation = initialPopulation;
	}

	@Override
	public int compareTo(BatchData o) {
		return new Integer(batchNumber).compareTo(o.batchNumber);
	}
}
