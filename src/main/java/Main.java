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
        // Create a map to store cell data
        Map<Integer, Cell> cells = new HashMap<>();
        try (
            // Create a CSV reader to read from the cells.csv file
            CSVReader reader = new CSVReader(new FileReader("src/main/java/cells.csv"));
            // Create a CSV writer to write to the cleaned_cells.csv file
            CSVWriter writer = new CSVWriter(new FileWriter("src/main/java/cleaned_cells.csv"))
        ) {
            // Create additional data structures for the questions
            Map<String, List<Float>> weightsByOem = new HashMap<>();
            List<Cell> differentYearPhones = new ArrayList<>();
            int singleSensorPhones = 0;
            Map<Integer, Integer> phonesByYear = new HashMap<>();

            String[] columns;
            int index = 0;
            // Read each line from the CSV file
            while ((columns = reader.readNext()) != null) {
                if (index++ == 0) {
                    // Write the header to the cleaned_cells.csv file
                    writer.writeNext(columns);
                    continue;
                }
                // Ensure that each line has 12 columns
                while (columns.length < 12) {
                    columns = Arrays.copyOf(columns, columns.length + 1);
                    columns[columns.length - 1] = "null";
                }
                // Process each column in the line
                for (int i = 0; i < columns.length; i++) {
                    // Replace empty values with "null"
                    if (columns[i] == null || columns[i].isEmpty() || columns[i].equals("-")) {
                        columns[i] = "null";
                    } else {
                        Matcher m; // Declare the Matcher variable here
                        switch (i) {
                            // Process the launch_announced and launch_status columns
                            case 2:
                            case 3:
                                if (columns[i].equals("Discontinued") || columns[i].equals("Cancelled")) {
                                    // Keep the original value
                                } else {
                                    // Extract the year from the column
                                    m = Pattern.compile("\\d{4}").matcher(columns[i]);
                                    if (m.find()) {
                                        columns[i] = m.group();
                                    } else {
                                        columns[i] = "null";
                                    }
                                }
                                break;
                            // Process the body_weight column
                            case 5:
                                // Extract the weight from the column
                                m = Pattern.compile("\\d+").matcher(columns[i]);
                                if (m.find()) {
                                    columns[i] = m.group();
                                } else {
                                    columns[i] = "null";
                                }
                                break;
                            // Process the body_sim column
                            case 6:
                                if (columns[i].equalsIgnoreCase("No") || columns[i].equalsIgnoreCase("Yes")) {
                                    columns[i] = "null";
                                }
                                break;
                            // Process the display_size column
                            case 8:
                                // Extract the size from the column
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
                            // Process the platform_os column
                            case 11:
                                int commaIndex = columns[i].indexOf(',');
                                if (commaIndex != -1) {
                                    columns[i] = columns[i].substring(0, commaIndex);
                                }
                                break;
                        }
                    }
                    // Move the year to the launch_status field if it's null
                    if (i == 3 && columns[i].equals("null")) {
                        Matcher m = Pattern.compile("\\d{4}").matcher(columns[4]);
                        if (m.find()) {
                            columns[i] = m.group();
                            columns[4] = columns[4].replace(m.group(), "").trim();
                        }
                    }
                }
                // Create a new Cell object and add it to the map
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
                // Write the processed line to the cleaned_cells.csv file
                writer.writeNext(columns);

                // Collect data for the questions
                if (cell.getBodyWeight() != null) {
                    weightsByOem.computeIfAbsent(cell.getOem(), k -> new ArrayList<>()).add(cell.getBodyWeight());
                }
                if (cell.getLaunchAnnounced() != null && cell.getLaunchStatus() != null
                        && !String.valueOf(cell.getLaunchAnnounced()).equals(cell.getLaunchStatus())) {
                    differentYearPhones.add(cell);
                }
                if (cell.getFeaturesSensors() != null && cell.getFeaturesSensors().split(",").length == 1) {
                    singleSensorPhones++;
                }
                if (cell.getLaunchAnnounced() != null && cell.getLaunchAnnounced() > 1999) {
                    phonesByYear.merge(cell.getLaunchAnnounced(), 1, Integer::sum);
                }
            }

            // Answer the questions
            Map.Entry<String, List<Float>> maxWeightOem = weightsByOem.entrySet().stream()
                    .max(Comparator.comparingDouble(
                            e -> e.getValue().stream().mapToDouble(Float::floatValue).average().orElse(0)))
                    .orElse(null);
            System.out.println("Company with the highest average weight: "
                    + (maxWeightOem != null ? maxWeightOem.getKey() : "N/A"));

            System.out.println("Phones announced in one year and released in another:");
            for (Cell cell : differentYearPhones) {
                System.out.println("OEM: " + cell.getOem() + ", Model: " + cell.getModel());
            }

            System.out.println("Number of phones with only one feature sensor: " + singleSensorPhones);

            Map.Entry<Integer, Integer> maxYear = phonesByYear.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .orElse(null);
            System.out.println("Year with the most phones launched: " + (maxYear != null ? maxYear.getKey() : "N/A"));

        } catch (IOException | CsvValidationException e) {
            // Print the stack trace if an exception is thrown
            e.printStackTrace();
        }
    }
}