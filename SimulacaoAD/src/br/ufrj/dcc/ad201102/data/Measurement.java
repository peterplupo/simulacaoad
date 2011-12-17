package br.ufrj.dcc.ad201102.data;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;

public class Measurement {
	
	private static boolean transientBatch;
	private static Map<Integer, BatchData> simulationData = new TreeMap<Integer, BatchData>();
	private static Map<Integer, Map<Integer, BatchData>> multipleSimulations = new TreeMap<Integer, Map<Integer, BatchData>>();
	
	public static void reset() {
		simulationData = new TreeMap<Integer, BatchData>();
	}
	
	public static void newRun(Integer tag) {
		multipleSimulations.put(tag, simulationData);
		reset();
	}
	
	public static  Map<Integer, Map<Integer, BatchData>> getRuns() {
		return multipleSimulations;
	}

	public static boolean hasTransientBatch() {
		return transientBatch;
	}

	public static void setTransientBatch(boolean transientBatch) {
		Measurement.transientBatch = transientBatch;
	}

	public static BatchData getTransientBatchData() {
		if (hasTransientBatch()) {
			return getBatchData(-1);
		}
		return null;
	}

	public static BatchData getBatchData(int batchNumber) {
		BatchData batchData = simulationData.get(batchNumber);
		if (batchData == null) {
			batchData = new BatchData(batchNumber);
			simulationData.put(batchNumber, batchData);
		}
		return batchData;
	}
	
	public static Collection<BatchData> getBatchData(boolean fetchTransient) {
		if (!hasTransientBatch() || fetchTransient == hasTransientBatch()) {
			return simulationData.values();
		} else {
			if (!fetchTransient && hasTransientBatch()) {
				Collection<BatchData> batches = new TreeSet<BatchData>(simulationData.values());
				Iterator<BatchData> removeFirst = batches.iterator();
				BatchData batch = removeFirst.next();
				if (batch.getBatchNumber() == -1) {
					removeFirst.remove();
				}
				return batches;
			} else {
				return simulationData.values();
			}
		}
	}
	
	public static boolean confidenceInterval95() {
		if (simulationData.size()<=30) {
			return false;
		}
		
		DescriptiveStatistics stat = new DescriptiveStatistics();
		for (BatchData batch : getBatchData(false)) {
			stat.addValue(batch.getMeanDownloadTime());
		}
		return valueConfidenceInterval95()<0.1*stat.getMean();
	}
	
	public static double valueConfidenceInterval95() {
		DescriptiveStatistics stat = new DescriptiveStatistics();
		for (BatchData batch : getBatchData(false)) {
			stat.addValue(batch.getMeanDownloadTime());
		}
		//sumPairs(means.toArray(new Double[means.size()]))[0]
		//2 * 1.96 = 3.92
		return getConfidenceInterval95(stat.getVariance(), stat.getN());
	}
	
	public static double getConfidenceInterval95(double var, long n) {
		return 3.92 * var/Math.sqrt(n);
	}
}
