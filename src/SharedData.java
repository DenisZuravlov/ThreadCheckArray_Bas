import java.util.ArrayList;

/**
 * Stores the input values and shared state used by the worker threads.
 */
public class SharedData {
    private ArrayList<Integer> array;
    private boolean[] winArray;
    private boolean flag;
    private final int b;

    /**
     * Creates shared data for a target sum and a list of input values.
     *
     * @param array input values
     * @param b target sum
     */
    public SharedData(ArrayList<Integer> array, int b) {
        this.array = array;
        this.b = b;
    }

    /**
     * Returns the result markers. Each marker corresponds to an input value.
     *
     * @return the result markers
     */
    public boolean[] getWinArray() { return winArray; }

    /**
     * Sets the result markers produced by a worker.
     *
     * @param winArray result markers
     */
    public void setWinArray(boolean[] winArray) { this.winArray = winArray; }

    /**
     * Returns the input values.
     *
     * @return the input values
     */
    public ArrayList<Integer> getArray() { return array; }

    /**
     * Returns the target sum.
     *
     * @return the target sum
     */
    public int getB() { return b; }

    /**
     * Returns whether a solution has been found.
     *
     * @return {@code true} when a solution has been found
     */
    public boolean getFlag() { return flag; }

    /**
     * Updates whether a solution has been found.
     *
     * @param flag {@code true} when a solution has been found
     */
    public void setFlag(boolean flag) { this.flag = flag; }
}
