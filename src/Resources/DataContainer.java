package Resources;

/**
 * A class holder for the data returned by the model.
 */
public class DataContainer {
    // member
    private int count;
    private String[] columns;
    private String[] data;

    /**
     *
     * Constructor.
     * @param data the data to store
     * @param count the row count of the data
     */
    public DataContainer(String[] data, String[] columns, int count) {
        this.count = count;
        this.data = data;
        this.columns = columns;
    }

    /**
     *
     * @return the stored data.
     */
    public String[] getData() {
        return data;
    }

    /**
     *
     * @return the data row count.
     */
    public int getCount() {
        return count;
    }

    /**
     *
     * @return the datas columns
     */
    public String[] getColumns() {
        return columns;
    }
}
