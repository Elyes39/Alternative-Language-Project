import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Arrays;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.opencsv.CSVWriter;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

public class Main {
    public static void main(String[] args) {
        Map<Integer, Cell> cells = new HashMap<>();
        try (CSVReader reader = new CSVReader(new FileReader("cells.csv"));
                CSVWriter writer = new CSVWriter(new FileWriter("cleaned_cells.csv"))) {

            String[] columns;
            int index = 0;
            while ((columns = reader.readNext()) != null) {
                if (index++ == 0) {
                    writer.writeNext(columns);
                    continue;
                }
                while (columns.length < 12) {
                    columns = Arrays.copyOf(columns, columns.length + 1);
                    columns[columns.length - 1] = "null";
                }
                for (int i = 0; i < columns.length; i++) {
                    if (columns[i] == null || columns[i].isEmpty() || columns[i].equals("-")) {
                        columns[i] = "null"; 

                    } else {
                        Matcher m; 
                        switch (i) {
                            case 2: // launch_announced
                            case 3: // launch_status
                                if (columns[i].equals("Discontinued") || columns[i].equals("Cancelled")) {
                                    // Keep the original value
                                } else {
                                    m = Pattern.compile("\\d{4}").matcher(columns[i]);
                                    if (m.find()) {
                                        columns[i] = m.group();
                                    } else {
                                        columns[i] = "null";
                                    }
                                }
                                break;
                            case 5: // body_weight
                                m = Pattern.compile("\\d+").matcher(columns[i]);
                                if (m.find()) {
                                    columns[i] = m.group();
                                } else {
                                    columns[i] = "null";
                                }
                                break;
                            case 6: // body_sim
                                if (columns[i].equalsIgnoreCase("No") || columns[i].equalsIgnoreCase("Yes")) {
                                    columns[i] = "null";
                                }
                                break;
                            case 8: // display_size
                                m = Pattern.compile("(\\d+(\\.\\d+)?)\\s*inches").matcher(columns[i]);
                                if (m.find()) {
                                    try {
                                        // Convert the number before "inches" to a float
                                        columns[i] = String.valueOf(Float.parseFloat(m.group(1)));
                                    } catch (NumberFormatException e) {
                                        columns[i] = "null";
                                    }
                                } else {
                                    columns[i] = "null";
                                }
                                break;
                            case 11: // platform_os
                                int commaIndex = columns[i].indexOf(',');
                                if (commaIndex != -1) {
                                    columns[i] = columns[i].substring(0, commaIndex);
                                }
                                break;
                        }
                    }
                    if (i == 3 && columns[i].equals("null")) {
                        Matcher m = Pattern.compile("\\d{4}").matcher(columns[4]);
                        if (m.find()) {
                            columns[i] = m.group(); // Move the year to the launch_status field
                            columns[4] = columns[4].replace(m.group(), "").trim(); // Remove the year from the launch_announced field
                        }
                    }
                }
                Cell cell = new Cell(columns[0],
                        columns[1],
                        columns[2],
                        columns[3],
                        columns[4],
                        columns[5],
                        columns[6],
                        columns[7],
                        columns[8],
                        columns[9],
                        columns[10],
                        columns[11]);
                cells.put(index, cell);
                System.out.println("Cell at index " + index + ": " + cell);
                writer.writeNext(columns);



        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }
}