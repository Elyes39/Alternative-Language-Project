import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Collections;

// Class representing a Cell object
public class Cell {
    // Class fields
    private String oem;
    private String model;
    private Integer launchAnnounced;
    private String launchStatus;
    private String bodyDimensions;
    private Float bodyWeight;
    private String bodySim;
    private String displayType;
    private Float displaySize;
    private String displayResolution;
    private String featuresSensors;
    private String platformOs;

    public Cell(String oem, String model, String launchAnnounced, String launchStatus, String bodyDimensions, String bodyWeight, String bodySim, String displayType, String displaySize, String displayResolution, String featuresSensors, String platformOs) {
        // Assigning values to fields, with some validation and extraction
        this.oem = oem.isEmpty() ? null : oem;
        this.model = model.isEmpty() ? null : model;
        this.launchAnnounced = extractYear(launchAnnounced);
        this.launchStatus = validateLaunchStatus(launchStatus);
        this.bodyDimensions = bodyDimensions.isEmpty() ? null : bodyDimensions;
        this.bodyWeight = extractWeight(bodyWeight);
        this.bodySim = validateBodySim(bodySim);
        this.displayType = displayType.isEmpty() ? null : displayType;
        this.displaySize = extractDisplaySize(displaySize);
        this.displayResolution = displayResolution.isEmpty() ? null : displayResolution;
        this.featuresSensors = featuresSensors.isEmpty() ? null : featuresSensors;
        this.platformOs = extractPlatformOs(platformOs);
    }

    // Method to extract year from a string
    private Integer extractYear(String str) {
        Pattern pattern = Pattern.compile("\\d{4}");
        Matcher matcher = pattern.matcher(str);
        return matcher.find() ? Integer.parseInt(matcher.group()) : null;
    }

    // Method to validate launch status
    private String validateLaunchStatus(String str) {
        if (str.matches("\\d{4}") || str.equals("Discontinued") || str.equals("Cancelled")) {
            return str;
        }
        return null;
    }

    // Method to extract weight from a string
    private Float extractWeight(String str) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(str);
        return matcher.find() ? Float.parseFloat(matcher.group()) : null;
    }

    // Method to validate body sim
    private String validateBodySim(String str) {
        if (str.equals("No") || str.equals("Yes")) {
            return null;
        }
        return str;
    }

    // Method to extract display size from a string
    private Float extractDisplaySize(String str) {
        Pattern pattern = Pattern.compile("\\d+(\\.\\d+)?");
        Matcher matcher = pattern.matcher(str);
        return matcher.find() ? Float.parseFloat(matcher.group()) : null;
    }

    // Method to extract platform OS from a string
    private String extractPlatformOs(String str) {
        int commaIndex = str.indexOf(",");
        return commaIndex != -1 ? str.substring(0, commaIndex) : str;
    }

    // Getters and setters for all fields
    public String getOem() {
        return this.oem;
    }

    public void setOem(String oem) {
        this.oem = oem;
    }

    public String getModel() {
        return this.model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getLaunchAnnounced() {
        return this.launchAnnounced;
    }

    public void setLaunchAnnounced(Integer launchAnnounced) {
        this.launchAnnounced = launchAnnounced;
    }

    public String getLaunchStatus() {
        return this.launchStatus;
    }
    
    public void setLaunchStatus(String launchStatus) {
        this.launchStatus = launchStatus;
    }

    public String getBodyDimensions() {
        return this.bodyDimensions;
    }
    
    public void setBodyDimensions(String bodyDimensions) {
        this.bodyDimensions = bodyDimensions;
    }
    
    public Float getBodyWeight() {
        return this.bodyWeight;
    }
    
    public void setBodyWeight(Float bodyWeight) {
        this.bodyWeight = bodyWeight;
    }
    
    public String getBodySim() {
        return this.bodySim;
    }
    
    public void setBodySim(String bodySim) {
        this.bodySim = bodySim;
    }
    
    public String getDisplayType() {
        return this.displayType;
    }
    
    public void setDisplayType(String displayType) {
        this.displayType = displayType;
    }
    
    public Float getDisplaySize() {
        return this.displaySize;
    }
    
    public void setDisplaySize(Float displaySize) {
        this.displaySize = displaySize;
    }
    
    public String getDisplayResolution() {
        return this.displayResolution;
    }
    
    public void setDisplayResolution(String displayResolution) {
        this.displayResolution = displayResolution;
    }
    
    public String getFeaturesSensors() {
        return this.featuresSensors;
    }
    
    public void setFeaturesSensors(String featuresSensors) {
        this.featuresSensors = featuresSensors;
    }
    
    public String getPlatformOs() {
        return this.platformOs;
    }
    
    public void setPlatformOs(String platformOs) {
        this.platformOs = platformOs;
    }

    // Overriding the toString method to print the object in a readable format
    @Override
    public String toString() {
        return "Cell{" +
                "\n\toem='" + oem + '\'' +
                ",\n\tmodel='" + model + '\'' +
                ",\n\tlaunchAnnounced=" + launchAnnounced +
                ",\n\tlaunchStatus='" + launchStatus + '\'' +
                ",\n\tbodyDimensions='" + bodyDimensions + '\'' +
                ",\n\tbodyWeight=" + bodyWeight +
                ",\n\tbodySim='" + bodySim + '\'' +
                ",\n\tdisplayType='" + displayType + '\'' +
                ",\n\tdisplaySize=" + displaySize +
                ",\n\tdisplayResolution='" + displayResolution + '\'' +
                ",\n\tfeaturesSensors='" + featuresSensors + '\'' +
                ",\n\tplatformOs='" + platformOs + '\'' +
                '}';
    }

    // Method to calculate mean display size of a list of cells
    public static float calculateMeanDisplaySize(List<Cell> cells) {
        float sum = 0;
        for (Cell cell : cells) {
            sum += cell.getDisplaySize();
        }
        return sum / cells.size();
    }

    // Method to calculate median display size of a list of cells
    public static float calculateMedianDisplaySize(List<Cell> cells) {
        List<Float> sizes = new ArrayList<>();
        for (Cell cell : cells) {
            sizes.add(cell.getDisplaySize());
        }
        Collections.sort(sizes);
        if (sizes.size() % 2 == 0) {
            return (sizes.get(sizes.size() / 2 - 1) + sizes.get(sizes.size() / 2)) / 2;
        } else {
            return sizes.get(sizes.size() / 2);
        }
    }

    // Method to calculate standard deviation of display size of a list of cells
    public static float calculateStandardDeviationDisplaySize(List<Cell> cells) {
        float mean = calculateMeanDisplaySize(cells);
        float sum = 0;
        for (Cell cell : cells) {
            sum += Math.pow(cell.getDisplaySize() - mean, 2);
        }
        return (float) Math.sqrt(sum / cells.size());
    }

    // Method to get unique models from a list of cells
    public static Set<String> getUniqueModels(List<Cell> cells) {
        Set<String> models = new HashSet<>();
        for (Cell cell : cells) {
            models.add(cell.getModel());
        }
        return models;
    }

    // Method to add a cell to a list of cells
    public static void addCell(List<Cell> cells, Cell cell) {
        cells.add(cell);
    }

    // Method to remove a cell from a list of cells
    public static void removeCell(List<Cell> cells, Cell cell) {
        cells.remove(cell);
    }
}