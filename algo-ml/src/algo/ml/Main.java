package algo.ml;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Calendar;

import org.apache.commons.math3.stat.regression.SimpleRegression;

import algo.ml.data.FinanceApiWrapper;
import algo.ml.math.MathUtils;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import yahoofinance.histquotes.Interval;

public class Main extends Application {

//	private static final double[][] matrix = CsvReader.XYValues("C:\\Users\\workstation\\Desktop\\ML\\ibm_stock\\ibm_H_2021.csv", true);
	
	private static final Calendar from = Calendar.getInstance();
	
	static {
		from.add(Calendar.DAY_OF_WEEK, -365);
	}
	
	private static final double[][] matrix = FinanceApiWrapper.getMatrix("IBM", from, Calendar.getInstance(), Interval.DAILY);
	
	public static void main(String[] args) throws Exception {
		//		double[][] matrix = 
		//				CsvReader.XYValues("C:\\Users\\workstation\\Desktop\\ML\\ibm_stock\\ibm_H_2021.csv", true);

		//		double x = matrix[matrix.length-1][0] + 1;
		//		Map<String, Double> coeffs = MathUtils.coefficients(matrix);
		//		SimpleRegression regression = new SimpleRegression();
		//		regression.addData(matrix);
		//		
		//		System.out.println("Apache Intercept: "+regression.getIntercept()+ " Custom Intercept: "+coeffs.get("intercept"));
		//		System.out.println("Apache Slope: "+regression.getSlope()+ " Custom Slope: "+coeffs.get("slope"));
		//		
		//		double y = coeffs .get("intercept") + coeffs.get("slope") * x;
		//		double y2 = regression.predict(x);
		//		System.out.println("Apache Prediction: "+y2+" Custom Prediction:  "+y);

		//		System.out.println(getStock("IBM").getQuote());

//		Calendar from = Calendar.getInstance();
//		from.add(Calendar.YEAR, -1);
//		Calendar to = Calendar.getInstance();
//		Interval interval = Interval.DAILY;
//		String symbol = "IBM";
//
//		double [][] matrix = FinanceApiWrapper.getMatrix(symbol, from, to, interval);
//
//		SimpleRegression regression = new SimpleRegression();
//		regression.addData(matrix);
//
//		System.out.println("Custom: "+MathUtils.coefficients(matrix)+" Prediction: "+MathUtils.predict(matrix, 1628539200000D));
//		System.out.println("Apache: "+regression.getIntercept()+" "+regression.getSlope()+" Prediction "+regression.predict(1628539200000D));

		Main.launch(args);
	}

	private static double[][] matrixRegression(double[][]trainSet){

		double[][] ret = new double[trainSet.length][2];
		SimpleRegression regression = new SimpleRegression();
		regression.addData(trainSet);
		System.out.println("Apache error: "+regression.getMeanSquareError());
		System.out.println("Custom error: "+MathUtils.getMeanSquareError(trainSet));
		
		for(int i = 0; i<trainSet.length; i++) {

			ret [i][0] = trainSet[i][0];

			//custom
			ret[i][1] = MathUtils.regress(trainSet, ret[i][0]);

			//apache
//			ret[i][1] = regression.predict(ret[i][0]);

		}

		return ret;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		init(primaryStage);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void init(Stage primaryStage) {

		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		
		HBox root = new HBox();
		Scene scene = new Scene(root, size.getWidth(), (size.getHeight()-200));
		
		CategoryAxis x = new CategoryAxis();
		x.setLabel("time");

		NumberAxis y = new NumberAxis();
		y.setLabel("close");

		LineChart lineChart = new LineChart(x, y);
		lineChart.setTitle("Pattern Chart");
		lineChart.setCreateSymbols(false);
		lineChart.setMinWidth(size.getWidth());

		XYChart.Series<CategoryAxis, Number> series = new XYChart.Series<>();
		series.setName("Actual");

		XYChart.Series<CategoryAxis, Number> predictionSeries = new XYChart.Series<>();
		predictionSeries.setName("Linear Regression");

		toChart(matrix, series);
		toChart(matrixRegression(matrix), predictionSeries);

		lineChart.getData().addAll(series, predictionSeries);

		root.getChildren().add(lineChart);

		primaryStage.setTitle("Pattern Visualizer");
		primaryStage.setScene(scene);

		primaryStage.show();

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void toChart(double[][] matrix, XYChart.Series series) {
		Calendar c = Calendar.getInstance();
		for(int i = 0; i < matrix.length; i ++) {
			
			c.setTimeInMillis((long)matrix[i][0]);
			
			String x = c.get(Calendar.DAY_OF_MONTH)+"/"+(c.get(Calendar.MONTH)+1)+"/"+c.get(Calendar.YEAR);
			series.getData().add(new XYChart.Data(x, matrix[i][1]));
		}
	}
}