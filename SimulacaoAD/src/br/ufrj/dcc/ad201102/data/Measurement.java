package br.ufrj.dcc.ad201102.data;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

public class Measurement {
	
	private static boolean transientBatch;
	private static Map<Integer, BatchData> simulationData = new TreeMap<Integer, BatchData>();
	
	public static void reset() {
		simulationData.clear();
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
		if (fetchTransient == hasTransientBatch()) {
			return simulationData.values();
		} else {
			Collection<BatchData> batches = new TreeSet<BatchData>(simulationData.values());
			Iterator<BatchData> removeFirst = batches.iterator();
			removeFirst.next();
			removeFirst.remove();
			return batches;
		}
	}
}
