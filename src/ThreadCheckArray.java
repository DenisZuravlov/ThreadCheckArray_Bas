import java.util.ArrayList;

/**
 * Checks part of a shared integer sequence for a combination matching its target.
 */
public class ThreadCheckArray implements Runnable {
    /** Indicates whether this worker has found a successful result. */
    private boolean flag;
    /** Marks the values participating in this worker's successful result. */
    private boolean[] winArray;
    /** Shared input and result state. */
    SharedData sd;
    /** Input values to check, stored as an {@code ArrayList<Integer>}. */
    ArrayList<Integer> array;
    /** Target value to check against. */
    int b;

    /**
     * Creates a worker using the supplied shared state.
     *
     * @param sd shared input and result state
     */
    public ThreadCheckArray(SharedData sd) {
        this.sd = sd;
        synchronized (sd) {
            array = sd.getArray();
            b = sd.getB();
        }
        winArray = new boolean[array.size()];
    }

    /**
     * Recursively checks choices from the first {@code n} values against a target.
     *
     * @param n number of values remaining to check
     * @param b remaining target value
     */
    void rec(int n, int b) {
        synchronized (sd) {
            if (sd.getFlag()) return;
        }
        if (n == 1) {
            if(b == 0 || b == array.get(n-1)) {
                flag = true;
                synchronized (sd) { sd.setFlag(true); }
            }
            if (b == array.get(n-1)) winArray[n-1] = true;
            return;
        }

        rec(n-1, b - array.get(n-1));
        if (flag) winArray[n-1] = true;
        synchronized (sd) {
            if (sd.getFlag()) return;
        }
        rec(n-1, b);
    }

    /**
     * Runs this worker's portion of the recursive check and publishes its result.
     */
    public void run() {
        if (array.size() != 1)
            if (Thread.currentThread().getName().equals("thread1"))
                rec(array.size()-1, b - array.get(array.size() - 1));
            else
                rec(array.size()-1, b);
        if (array.size() == 1)
            if (b == array.get(0) && !flag) {
                winArray[0] = true;
                flag = true;
                synchronized (sd) { sd.setFlag(true); }
            }
        if (flag) {
            if (Thread.currentThread().getName().equals("thread1"))
                winArray[array.size() - 1] = true;
            synchronized (sd) { sd.setWinArray(winArray); }
        }
    }
}
