package algo.ml.math;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MathUtils {
	
	public static double getMeanSquareError(double[][] matrix) {
		double ret = 0;
		
		for(int  i = 0; i < matrix.length; i++) {
			double actual = matrix[i][1];
			double predicted = regress(matrix, matrix[i][0]);
			double error = predicted - actual;
			ret += Math.pow(error, 2);
		}
		
		ret = ret / matrix.length;
		
//		return Math.sqrt(ret);
		return ret;
	}
	
	public static double regress(double[][] matrix, double x) {
		Map<String, Double> coeffs = coefficients(matrix);
		double intercept = coeffs.get("intercept");
		double slope = coeffs.get("slope");
		
		return intercept + slope * x;
	}

	// y = b0 + b1 * x
	// y = intercept + slope * x
	public static Map<String, Double> coefficients(double[][] matrix) {
		Map<String, Double> ret = new HashMap<>();

		List<Double> xValues = getColumn(matrix, 0);
		List<Double> yValues = getColumn(matrix, 1);
		
		double b1 = covariance(xValues, yValues) / variance(xValues);
		double b0 = mean(yValues) - (b1 * mean(xValues));

		ret.put("intercept", b0);
		ret.put("slope", b1);
		
		return ret;
	}

	private static double mean(List<Double> values) {

		double ret = 0.0;

		for(int i = 0; i < values.size(); i++) {
			ret += values.get(i);
		}

		return ret/values.size();
	}

	private static double variance(List<Double> values) {
		double ret = 0.0;
		double mean = mean(values);

		for(int i = 0; i < values.size(); i++) {
			double val = values.get(i);
			ret += Math.pow((val - mean), 2);
		}

		return ret;
	}

	private static double covariance(List<Double> xValues, List<Double> yValues) {
		double ret = 0.0;
		double xMean = mean(xValues);
		double yMean = mean(yValues);

		for(int i = 0; i < xValues.size(); i++) {
			ret += (xValues.get(i) - xMean) * (yValues.get(i) - yMean);
		}

		return ret;
	}
	
	private static List<Double> getColumn(double[][] matrix, int index){
		
		List<Double> column = new ArrayList<>();
	    for(int i=0; i<matrix.length; i++){
	       column.add(matrix[i][index]);
	    }
	    return column;
	}
}