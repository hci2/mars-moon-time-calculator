package technikum.wien.at.marsmoontimecalculator;

public class Moon {

    private String name;

    private long riseTimestamp;

    private long setTimestamp;

    public Moon(long riseTimestamp, long setTimestamp, String name) {
        this.riseTimestamp = riseTimestamp;
        this.setTimestamp = setTimestamp;
        this.name = name;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getRiseTimestamp() {
        return riseTimestamp;
    }

    public void setRiseTimestamp(long riseTimestamp) {
        this.riseTimestamp = riseTimestamp;
    }

    public long getSetTimestamp() {
        return setTimestamp;
    }

    public void setSetTimestamp(long setTimestamp) {
        this.setTimestamp = setTimestamp;
    }

    @Override
    public String toString() {
        return "Moon: " + name + "\n"
                + "riseTimestamp: " + riseTimestamp + "\n"
                + "setTimestamp: " + setTimestamp + "\n";
    }
}
