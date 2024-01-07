package si.fri.rso.samples.imagecatalog.lib;

import java.util.Date;

public class Stat {

    private String statName;
    private Double stat;
    private Date date;
    private String flag;


    public Stat(String statName, Double stat, Date date, String flag) {
        this.statName = statName;
        this.stat = stat;
        this.date = date;
        this.flag = flag;
    }

    public String getStatName() {
        return statName;
    }

    public void setStatName(String statName) {
        this.statName = statName;
    }

    public Double getStat() {
        return stat;
    }

    public void setStat(Double stat) {
        this.stat = stat;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
