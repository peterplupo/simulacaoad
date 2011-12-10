package br.ufrj.dcc.ad201102.report;

import java.util.ArrayList;
import java.util.Collection;

public class Statistic {
	
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
	
	public static Double[] sumPairsVariance(Double[] values, double mean) {
		for (int i = 0; i<values.length; i++) {
			values[i] = (values[i] - mean) * (values[i] - mean);
		}
		return sumPairs(values);
	}
	
	public static Collection<Double> integerToDouble(Collection<Integer> values) {
		Collection<Double> doubleValues = new ArrayList<Double>(values.size());
		for(Integer value : values) {
			doubleValues.add(value.doubleValue());
		}
		return doubleValues;
	}
	
	public static boolean ic95(Collection<Double> values) {
        if (((2 * 1.96 * variance(values))/Math.sqrt(values.size()))<0.1*mean(values)) {
        	return true;
        }
        return false;
	}
	
	public static Double[] sumPairs(Double[] values) {
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

}
