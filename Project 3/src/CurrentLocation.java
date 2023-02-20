import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

public class CurrentLocation
{
    private static HashMap<Location, BigDecimal> currentValues = new HashMap<>();

    public static HashMap<Location, BigDecimal> getDistrib(String debug, int m, int n, Location currLoc)
    {

        List<Location> adjacentLocs =  Main.oneManLocations(currLoc, m, n);
        //System.out.println(" adjlocs: " + adjacentLocs);
        for (int i = 0; i < adjacentLocs.size(); i++)
        {
            currentValues.put(adjacentLocs.get(i), BigDecimal.valueOf(1.0/(double)adjacentLocs.size()));
        }

        List<Location> otherLocs =  Main.allLocations(m,n);
        for (Location l : otherLocs) {
            if (!adjacentLocs.contains(l))
            currentValues.put(l, BigDecimal.valueOf(0));
        }

        return currentValues;
    }

    // P(C|L)
    public static BigDecimal getProb(String debug, int m, int n, Location C, Location L)
    {
        return getDistrib(debug, m, n, L).get(C);
    }
}
