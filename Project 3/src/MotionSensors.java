import java.math.BigDecimal;
import java.util.HashMap;

public class MotionSensors
{
    private static HashMap<Location, BigDecimal> mValues  = new HashMap<>();
    public static HashMap<Location, BigDecimal> get_distrib(String debug, int m, int n, boolean b, Location ms) {
        for (Location curr : Main.allLocations(m,n)) {

            //If it is M1 at top left
            if (ms.x() == 0 && ms.y() == 0) {
                double sVal = 0.9; //sensor Value
                if (curr.x() == 0 || curr.y() == 0) {
                    if (curr.x() > 0) sVal -= (curr.x() * 0.1);
                    else if (curr.y() > 0) sVal -= (curr.y() * 0.1);
                    //System.out.println(" curr: " + curr + " val: " + sVal);
                    if (b) mValues.put(curr, new BigDecimal(sVal)); // If reading of m1 is true
                    else mValues.put(curr, new BigDecimal(1-sVal)); // If reading of m1 is false
                    //System.out.println(" mval curr: " + mValues.get(curr));
                }
                else {
                    if (b) mValues.put(curr, new BigDecimal(0.05));
                    else mValues.put(curr, new BigDecimal(0.95));
                    //System.out.println(" mval curr: " + mValues.get(curr));
                }

            } else { //If it is M2 at bottom right
                double sVal = 0.9; //sensor Value
                if (curr.x() == m - 1 || curr.y() == n - 1) {
                    if (curr.x() < m - 1) sVal -= ((m - 1 - curr.x()) * 0.1);
                    else if (curr.y() < n - 1) sVal -= ((n - 1 - curr.y()) * 0.1);

                    if (b) mValues.put(curr, new BigDecimal(sVal)); // If reading of m1 is true
                    else mValues.put(curr, new BigDecimal(1-sVal)); // If reading of m1 is false
                }
                else {
                    if (b) mValues.put(curr, new BigDecimal(0.05));
                    else mValues.put(curr, new BigDecimal(0.95));
                }
            }
        }

        return mValues;
    }

    public static BigDecimal getProbM1(String debug, int m, int n, Location C, boolean b)
    {
        Location m1 = new Location(0,0);
        //System.out.println(" C: " + C);
        return get_distrib(debug, m, n, b, m1).get(C);
    }

    public static BigDecimal getProbM2(String debug, int m, int n, Location C, boolean b)
    {
        Location m2 = new Location(m-1,n-1);

        return get_distrib(debug, m, n, b, m2).get(C);
    }

}
