package algo.ml.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

public class CsvReader {

	@SuppressWarnings("unused")
	private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	public static double[][] XYValues(String csv, boolean hasHeader) {

		// ten rows, two columns
		double[][] matrix = new double[10][2];

		try(BufferedReader br = Files.newBufferedReader(Paths.get(csv), StandardCharsets.UTF_8)){

			@SuppressWarnings("unused")
			String header = br.readLine();

			String line = null;

			int i = 9;

			while((line = br.readLine()) != null && i>=0) {
				String [] barText = line.split(",");

				double y = Double.valueOf(barText[4]);
				matrix[i][0] = (double)i+1;
				matrix[i][1] = y;
				System.out.println("Actual:"+matrix[i][1]);
				i--;

			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return matrix;
	}

	@SuppressWarnings("unused")
	private static int rowsCount(String csv, boolean hasHeader) {
		int ret = 0;
		try (Stream<String> stream = Files.lines(Paths.get(csv), StandardCharsets.UTF_8)) {
			ret = stream.mapToInt(e -> 1).sum();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return (hasHeader) ? ret-1 : ret;
	}
}
