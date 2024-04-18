import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Cell {
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

    private Integer extractYear(String str) {
        Pattern pattern = Pattern.compile("\\d{4}");
        Matcher matcher = pattern.matcher(str);
        return matcher.find() ? Integer.parseInt(matcher.group()) : null;
    }

    private String validateLaunchStatus(String str) {
        if (str.matches("\\d{4}") || str.equals("Discontinued") || str.equals("Cancelled")) {
            return str;
        }
        return null;
    }

    private Float extractWeight(String str) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(str);
        return matcher.find() ? Float.parseFloat(matcher.group()) : null;
    }

    private String validateBodySim(String str) {
        if (str.equals("No") || str.equals("Yes")) {
            return null;
        }
        return str;
    }

    private Float extractDisplaySize(String str) {
        Pattern pattern = Pattern.compile("\\d+(\\.\\d+)?");
        Matcher matcher = pattern.matcher(str);
        return matcher.find() ? Float.parseFloat(matcher.group()) : null;
    }

    private String extractPlatformOs(String str) {
        int commaIndex = str.indexOf(",");
        return commaIndex != -1 ? str.substring(0, commaIndex) : str;
    }

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

}