package uk.adamwoollen.antennaarray;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class Main
{
    public static void main(String[] args)
    {
        AntennaArray antennaArray = new AntennaArray(3, 90);

        System.out.println("Testing uniform design");
        double[] antennaArrayDesign = new double[]{0.5, 1.0, 1.5};
        double peakSSL = antennaArray.evaluate(antennaArrayDesign);
        System.out.println(peakSSL);

        System.out.println("Running random for 5 seconds");
        double lowestRandomPeakSSL = solveUsingRandom(antennaArray, 5);
        System.out.println(lowestRandomPeakSSL);
        
        System.out.println("Running PSO for 10 seconds");
        double bestParticleSolution = solveUsingParticleSwarmOptimisation(antennaArray, 10);
        System.out.println(bestParticleSolution);
    }
    
    private static double solveUsingParticleSwarmOptimisation(AntennaArray antennaArray, int secondsToRunFor)
    {
    	Particle[] swarm = new Particle[20];
    	Arrays.parallelSetAll(swarm, i -> new Particle(antennaArray));
    	
    	long startTime = System.nanoTime();
        long stopTime = startTime + (secondsToRunFor * 1000000000L);
        while (System.nanoTime() < stopTime)
        {
	        for (Particle particle : swarm)
	    	{
	    		particle.move(antennaArray, 0.721, 1.1193, 1.1193);
	    	}
        }
    	return Particle.getGlobalBestValue();
    }

    private static double solveUsingRandom(AntennaArray antennaArray, int secondsToRunFor)
    {
        long startTime = System.nanoTime();
        long stopTime = startTime + (secondsToRunFor * 1000000000L);
        double lowestPeakSSl = Double.MAX_VALUE;
        while (System.nanoTime() < stopTime)
        {
            double[] antennaArrayDesign = getRandomAntennaArrayDesign(antennaArray);
            double peakSSL = antennaArray.evaluate(antennaArrayDesign);
            lowestPeakSSl = peakSSL < lowestPeakSSl ? peakSSL : lowestPeakSSl;
        }
        return lowestPeakSSl;
    }

    private static double[] getRandomAntennaArrayDesign(AntennaArray antennaArray)
    {
        double[] randomVector = getRandomVector(antennaArray);
        double[] design = convertVectorToAntennaArrayDesign(antennaArray, randomVector);
        return design;
    }
    
    public static double[] convertVectorToAntennaArrayDesign(AntennaArray antennaArray, double[] vector)
    {
    	double[] design = Arrays.copyOf(vector, vector.length + 1);
        design[design.length - 1] = antennaArray.getNumberOfAntenna() / 2.0;
        return design;
    }

    public static double[] getRandomVector(AntennaArray antennaArray)
    {
        return getRandomVectorInRange(antennaArray.getNumberOfAntenna() - 1, 0, antennaArray.getNumberOfAntenna() / 2.0);
    }
    
    public static double[] getRandomUniformVector(int length)
    {
    	return getRandomVectorInRange(length, 0.0, 1.0);
    }
    
    private static double[] getRandomVectorInRange(int length, double lowerBound, double upperBound)
    {
    	double[] vector = new double[length];
        for (int i = 0; i < vector.length; i++)
        {
            vector[i] = ThreadLocalRandom.current().nextDouble(lowerBound, upperBound);
        }
        return vector;
    }
}
