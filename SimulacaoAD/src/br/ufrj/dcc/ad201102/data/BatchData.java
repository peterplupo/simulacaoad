package br.ufrj.dcc.ad201102.data;

import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;

public class BatchData implements Comparable<BatchData> {
	
	private double startTime;
	private double endTime;
	private int initialBatchPopulation;
	private String id;
//	private Map<Double, Double> downloadTimes = new TreeMap<Double, Double>();
	private DescriptiveStatistics downloadTimesStatistics = new DescriptiveStatistics();
	private Map<Double, Integer> populationSize = new TreeMap<Double, Integer>();
//	private Map<Double, Integer> downloadSize = new TreeMap<Double, Integer>();
	private double totalPopulation;
//	private Collection<Double> exits = new TreeSet<Double>();
	private DescriptiveStatistics exitsStatistics = new DescriptiveStatistics();
	private int batchNumber;
	
	
	public double getOutput() {
		return exitsStatistics.getN()/(endTime-startTime);
	}
	
	public double getTotalPopulation() {
		return totalPopulation;
	}

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
		if (Measurement.populationStatsOn) {
			populationSize.put(time, size);
			totalPopulation = totalPopulation + size;
//			downloadSize.put(time, (int)downloadTimesStatistics.getN());
		}
	}
	
//	public int getDownloadSizeAt(double time) {
//		return downloadSize.get(time);
//	}
	
	public void addExit(double time) {
		if (invalidTime(time)) {
			return;
		}
//		exits.add(time);
		exitsStatistics.addValue(time);
	}
	
	public double getMeanPopulation() {
		double lastTime = getStartTime();
		double lastPop = getInitialBatchPopulation();
		
		if (getPopulationSize().size() == 0) {
			return getInitialBatchPopulation();
		} else {
			DescriptiveStatistics stat = new DescriptiveStatistics();
			for (Map.Entry<Double, Integer> size : getPopulationSize().entrySet()) {
				stat.addValue(lastPop*(size.getKey()-lastTime));
				lastPop = size.getValue();
				lastTime = size.getKey();
			}
			//sumPairs(means.toArray(new Double[means.size()]))[0]
			return stat.getSum()/(getEndTime() - getStartTime());
		}
	}
	
	public Map<Integer, Double> getPopulationProbabilityDistribution() {
		double batchTime = endTime - startTime;
		double lastTime = getStartTime();
		int lastPop = getInitialBatchPopulation();
		Map<Integer, Double> popProbDistTemp = new TreeMap<Integer, Double>();
		
		if (getPopulationSize().size() == 0) {
			popProbDistTemp.put(getInitialBatchPopulation(), getEndTime() - getStartTime());
		} else {
			for (Map.Entry<Double, Integer> size : getPopulationSize().entrySet()) {
				if (popProbDistTemp.get(lastPop) == null) {
					popProbDistTemp.put(lastPop,(size.getKey()-lastTime));
				} else {
					popProbDistTemp.put(lastPop,popProbDistTemp.get(lastPop) + (size.getKey()-lastTime));
				}
				lastPop = size.getValue();
				lastTime = size.getKey();
			}
		}
		Map<Integer, Double> popProbDist = new TreeMap<Integer, Double>();
		for (Map.Entry<Integer, Double> fraction : popProbDistTemp.entrySet()) {
			popProbDist.put(fraction.getKey(), fraction.getValue()/batchTime);
		}
		
		return popProbDist;
	}
	
	public void addDownloadTime(double time, double downloadTime) {
		if (invalidTime(time)) {
			return;
		}
//		downloadTimes.put(time, downloadTime);
		downloadTimesStatistics.addValue(downloadTime);
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

	public int getInitialBatchPopulation() {
		return initialBatchPopulation;
	}

	public void setInitialBatchPopulation(int initialPopulation) {
		this.initialBatchPopulation = initialPopulation;
	}

	@Override
	public int compareTo(BatchData o) {
		return new Integer(batchNumber).compareTo(o.batchNumber);
	}

	public double getMeanDownloadTime() {
		return downloadTimesStatistics.getMean();
	}
	
	public double[] getDownloadTimes() {
		return downloadTimesStatistics.getValues();//values();
	}
	
//	public Collection<Double> getExits() {
//		return exits;
//	}
	
	public Map<Double, Double> getPopulationCDF() {
		double sumPopulation = 0;
		Map<Double, Double> timeSumPopulation = new TreeMap<Double, Double>();
		double[][] timePopulationSeries = new double[getPopulationSize().size()][2];
		
		int i = 0;
		for (Map.Entry<Double, Integer> population : getPopulationSize().entrySet()) {
			sumPopulation = sumPopulation + population.getValue();
			timePopulationSeries[i][0] = population.getKey();
			timePopulationSeries[i][1] = sumPopulation;
			i++;
		}
		
		for (double[] timePopulation : timePopulationSeries) {
			timeSumPopulation.put(timePopulation[0]-getStartTime(), timePopulation[1]/sumPopulation);
			
		}
		return timeSumPopulation;
	}
	
//	public double getMedianDownloadTime(){
//		double median = 0.0;
//		this.getPopulationCDF();
//		int divisao = 0;
//		 ArrayList<Double> times = new ArrayList<Double>();
//		 Collections.sort(times);
//		 Iterator<Double> it = this.getDownloadTimes().iterator();
//		 while(it.hasNext()){
//			 times.add(it.next());
//		 }
//		 
//		
//		if((this.getDownloadTimes().size() %2) == 1) {
//			divisao = this.getDownloadTimes().size()/2;
//			median = times.get(divisao);
//			
//		} else {
//			divisao = this.getDownloadTimes().size()/2;
//			int central = ((divisao-1) + (divisao+1))/2;
//			median = times.get(central);
//		}
//		
//		return median;
//	}
//	
//	public Map<Double, Double> getDownloadTimesPerInstant() {
//		return downloadTimes;
//	}

	
}
