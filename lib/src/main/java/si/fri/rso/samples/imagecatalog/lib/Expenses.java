package si.fri.rso.samples.imagecatalog.lib;

import java.util.Date;

public class Expenses {

    private Integer expenseId;
    private String kind;
    private Date date_occurrence;
    private String description;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDateOccurrence(Date date_occurrence) {
        this.date_occurrence = date_occurrence;
    }

    public Date getDateOccurrence() {
        return date_occurrence;
    }

    public Integer getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(Integer expenseId) {
        this.expenseId = expenseId;
    }
}
