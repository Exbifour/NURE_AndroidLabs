package ua.nure.stanchyk.lab1;

public class FilterParameters {
    private int filteredImportance;
    private String filteredText;
    private String filteredDate;

    public FilterParameters(int filteredImportance, String filteredText, String filteredDate) {
        this.filteredImportance = filteredImportance;
        this.filteredText = filteredText;
        this.filteredDate = filteredDate;
    }

    public int getFilteredImportance() {
        return filteredImportance;
    }

    public void setFilteredImportance(int filteredImportance) {
        this.filteredImportance = filteredImportance;
    }

    public String getFilteredText() {
        return filteredText;
    }

    public void setFilteredText(String filteredText) {
        this.filteredText = filteredText;
    }


    public String getFilteredDate() {
        return filteredDate;
    }

    public void setFilteredDate(String filteredDate) {
        this.filteredDate = filteredDate;
    }
}
