import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class Main {
    private static int m; // num of rows
    private static int n; // num of cols

    public static void main(String[] args) throws FileNotFoundException {
        Scanner s = new Scanner(System.in);
        System.out.println("Enter file name: ");
        String filename = s.nextLine();
        InputStream is = Main.class.getResourceAsStream(filename);
        if (is == null) {
            System.err.println("Bad filename " + filename);
            System.exit(1);
        }
        Scanner filescan = new Scanner(is);
        Scanner scan = new Scanner(System.in);
        System.out.print("Do you want debugging info? (y/n): ");
        String debug = scan.nextLine();
        int timestep = 0;

        String line = filescan.nextLine();
        String[] split = line.split(" ");
        m = Integer.parseInt(split[0]);
        n = Integer.parseInt(split[1]);

        HashMap<Location, BigDecimal> lastValues = LastLocation.get_distrib(debug, m, n);

        System.out.println("Initial distribution of monkey's last location:");
        BigDecimal probability;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                probability = lastValues.get(new Location(i,j));
                System.out.print(" " + probability.setScale(8, RoundingMode.HALF_UP));
            }
            System.out.println();
        }

        while (filescan.hasNextLine()) {
            line = filescan.nextLine();
            split = line.split(" ");
            boolean m1;
            boolean m2;
            m1 = split[0].equals("1");
            m2 = split[1].equals("1");
            int r = Integer.parseInt(split[2]);
            int c = Integer.parseInt(split[3]);
            Location sensorLoc = new Location(r,c);
            HashMap<Location, BigDecimal> currProbs = new HashMap<>();
            System.out.println("\nObservation: Motion1: " + m1 + ", Motion2: " + m2 + ", Sound location: " + sensorLoc);
            System.out.println("Monkey's predicted current location at time step: " + timestep);
            double alpha = 0; //denominator to divide with to get normalized probabilities

            for (Location currLocation : allLocations(m,n)) {
                if (debug.equals("y")) {
                    System.out.println("Calculating total prob for current location " + currLocation);
                }

                double probM1 = MotionSensors.getProbM1(debug,m,n,currLocation,m1).doubleValue();
                double probM2 = MotionSensors.getProbM2(debug,m,n,currLocation,m2).doubleValue();
                double probS = SoundSensor.getProb(debug,m,n,currLocation,sensorLoc).doubleValue();
                double probTotal = 0;

                for (Location lastLocation : lastValues.keySet()) {
                    double probL = lastValues.get(lastLocation).doubleValue();
                    double probCurrL = CurrentLocation.getProb(debug,m,n,currLocation, lastLocation).doubleValue();
                    double probProduct = probL * probCurrL * probM1 * probM2 * probS;
                    if (debug.equals("y")) {
                        System.out.println("  Probs being multiplied for last location " + lastLocation + ": " +
                                probL + " " + probCurrL + " " + probM1 + " " + probM2 + " " + probS);
                    }
                    probTotal += probProduct;
                }
                alpha += probTotal;
                currProbs.put(currLocation, new BigDecimal(probTotal));
            }

            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    Location tempLoc = new Location(i,j);
                    BigDecimal normalizedProbability = new BigDecimal(currProbs.get(tempLoc).doubleValue()/alpha);
                    lastValues.put(tempLoc, normalizedProbability);
                    if (normalizedProbability.doubleValue() == 0) {
                        System.out.print(" 0.00000000");
                    } else {
                        System.out.print(" " + normalizedProbability.setScale(8, RoundingMode.HALF_UP));
                    }
                }
                System.out.println();
            }

            timestep++;
        }
    }

    public static List<Location> allLocations(double r, double c) {
        List<Location> allLoc = new ArrayList<>();
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                Location l = new Location(i, j);
                allLoc.add(l);
            }
        }
        return allLoc;
    }

    public static List<Location> oneManLocations(Location l, int row, int col) {
        List<Location> oneManloc = new ArrayList<>();
        if (l.y() - 1 >= 0) {
            Location up = new Location(l.x(), l.y() - 1);
            oneManloc.add(up);
        }
        if (l.y() + 1 < row) {
            Location down = new Location(l.x(), l.y() + 1);
            oneManloc.add(down);
        }
        if (l.x() - 1 >= 0) {
            Location left = new Location(l.x() - 1, l.y());
            oneManloc.add(left);
        }
        if (l.x() + 1 < col) {
            Location right = new Location(l.x() + 1, l.y());
            oneManloc.add(right);
        }
        return oneManloc;
    }

    public static List<Location> twoManLocations(Location l, int row, int col) {
        List<Location> twoManloc = new ArrayList<>();
        if (l.y() - 1 >= 0 && l.x() + 1 < col) {
            Location up = new Location(l.x() + 1, l.y() - 1);
            twoManloc.add(up);
        }
        if (l.y() + 1 < row && l.x() + 1 < col) {
            Location down = new Location(l.x() + 1, l.y() + 1);
            twoManloc.add(down);
        }
        if (l.x() - 1 >= 0 && l.y() - 1 >= 0) {
            Location left = new Location(l.x() - 1, l.y() - 1);
            twoManloc.add(left);
        }
        if (l.x() - 1 >= 0 && l.y() + 1 < row) {
            Location right = new Location(l.x() - 1, l.y() + 1);
            twoManloc.add(right);
        }
        if (l.y() - 2 >= 0) {
            Location up = new Location(l.x(), l.y() - 2);
            twoManloc.add(up);
        }
        if (l.y() + 2 < row) {
            Location down = new Location(l.x(), l.y() + 2);
            twoManloc.add(down);
        }
        if (l.x() - 2 >= 0) {
            Location left = new Location(l.x() - 2, l.y());
            twoManloc.add(left);
        }
        if (l.x() + 2 < col) {
            Location right = new Location(l.x() + 2, l.y());
            twoManloc.add(right);
        }
        return twoManloc;
    }
}
