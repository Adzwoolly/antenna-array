package uk.adamwoollen.antennaarray;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class Main
{
    public static void main(String[] args)
    {
        AntennaArray antennaArray = new AntennaArray(3, 90);

        double[] antennaArrayDesign = new double[]{0.5, 1.0, 1.5};
        double peakSSL = antennaArray.evaluate(antennaArrayDesign);
        System.out.println(peakSSL);

        double lowestRandomPeakSSL = solveUsingRandom(antennaArray, 5);
        System.out.println(lowestRandomPeakSSL);
    }

    private static double solveUsingRandom(AntennaArray antennaArray, int secondsToRunFor)
    {
        long startTime = System.nanoTime();
        long stopTime = startTime + (secondsToRunFor * 1000000000L);
        double lowestPeakSSl = Double.MAX_VALUE;
        while (System.nanoTime() < stopTime)
        {
            double[] antennaArrayDesign = getRandomDesign(antennaArray);
            double peakSSL = antennaArray.evaluate(antennaArrayDesign);
            lowestPeakSSl = peakSSL < lowestPeakSSl ? peakSSL : lowestPeakSSl;
        }
        return lowestPeakSSl;
    }

    private static double[] getRandomDesign(AntennaArray antennaArray)
    {
        double[] randomVector = getRandomVector(antennaArray);
        double[] design = Arrays.copyOf(randomVector, randomVector.length + 1);
        design[design.length - 1] = antennaArray.getNumberOfAntenna() / 2.0;
        return design;
    }

    private static double[] getRandomVector(AntennaArray antennaArray)
    {
        double[] vector = new double[antennaArray.getNumberOfAntenna() - 1];
        for (int i = 0; i < vector.length; i++)
        {
            vector[i] = ThreadLocalRandom.current().nextDouble(0.0, antennaArray.getNumberOfAntenna() / 2.0);
        }
        return vector;
    }
}
