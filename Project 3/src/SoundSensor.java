import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

public class SoundSensor
{
    private static HashMap<Location, BigDecimal> sCurr = new HashMap<>();

    public static HashMap<Location, BigDecimal> getDistrib(String debug, int m, int n, Location currLoc)
    {
        sCurr.put(currLoc, BigDecimal.valueOf(0.6));
        List<Location> adjacentLocs =  Main.oneManLocations(currLoc, m, n);
        //System.out.println("adjLocs: " + adjacentLocs);
        for (Location l1 : adjacentLocs)
        {
            sCurr.put(l1, BigDecimal.valueOf(0.3 / adjacentLocs.size()));
        }

        List<Location> adjacentLocs2 =  Main.twoManLocations(currLoc, m, n);
        //System.out.println("adjLocs2: " + adjacentLocs2);
        for (Location l2 : adjacentLocs2)
        {
            sCurr.put(l2, BigDecimal.valueOf(0.1 / adjacentLocs2.size()));
        }

        //System.out.println("allLocs: " + Main.allLocations(m,n));
        for (Location l : Main.allLocations(m,n)) {
            if (!sCurr.containsKey(l)) sCurr.put(l, new BigDecimal(0));
        }

        return sCurr;
    }

    public static BigDecimal getProb(String debug, int m, int n, Location currLoc, Location s) {
        sCurr = new HashMap<>();
        return getDistrib(debug, m, n, currLoc).get(s);
    }
}
