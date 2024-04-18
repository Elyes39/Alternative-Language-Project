import java.io.FileReader;
import java.io.IOException;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        try (CSVReader reader = new CSVReader(new FileReader("cells.csv"))) {
            String[] columns;
            int index = 0;
            while ((columns = reader.readNext()) != null) {
                System.out.println("Row at index " + index + ": " + Arrays.toString(columns));
                index++;
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }
}