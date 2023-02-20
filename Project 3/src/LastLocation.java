import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public class LastLocation {

    public static HashMap<Location, BigDecimal> get_distrib(String debug, int m, int n) {
        HashMap<Location, BigDecimal> lValues  = new HashMap<>();
        if (debug.equals("y"))
        {
            System.out.println("Last location distribution:");
        }
        for (int i = 0; i < m; i++)
        {
            for (int j = 0; j < n; j++)
            {
                Location location = new Location(i,j);
                BigDecimal prob = BigDecimal.valueOf(1.0 / (double)(m * n)).setScale(8, RoundingMode.HALF_UP);
                lValues.put(location, prob);
                if (debug.equals("y"))
                {
                    System.out.println("Last location: " + location + ", prob: " + prob);
                }
            }
        }
        return lValues;
    }
}