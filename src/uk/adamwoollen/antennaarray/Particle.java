package uk.adamwoollen.antennaarray;

import java.util.Arrays;

public class Particle
{
    private static double[] globalBest;
    private static double globalBestValue;

    private double[] position;
    private double[] velocity;
    private double[] personalBest;
    private double personalBestValue;

    static
    {
    	globalBestValue = Double.MAX_VALUE;
    }
    
    public Particle(AntennaArray antennaArray)
    {
        position = Main.getRandomVector(antennaArray);
        velocity = Main.getRandomUniformVector(antennaArray.getNumberOfAntenna() - 1);
        personalBestValue = Double.MAX_VALUE;
        personalBest = position;

        evaluate(antennaArray);
    }
    
    public static double getGlobalBestValue()
    {
    	return globalBestValue;
    }

    public void move(AntennaArray antennaArray, double inertialCoefficient, double cognitiveCoefficient, double socialCoefficient)
    {
        //move
        //newVelocity = (currentVelocity * inertialCoefficient) + (vectorToPersonalBest * cognitiveCoefficient * randomVector) + (vectorToGlobalBest * socialCoefficient * randomVector)
        double[] inertialVector = multiplyVector(velocity, inertialCoefficient);
        
        double[] vectorToPersonalBest = vectorFromAToB(position, personalBest);
        double[] randomUniformVector1 = Main.getRandomUniformVector(antennaArray.getNumberOfAntenna() - 1);
        double[] cognitiveVector = multiplyVector(multiplyVectors(randomUniformVector1, vectorToPersonalBest), cognitiveCoefficient);
        
        double[] vectorToGlobalBest = vectorFromAToB(position, globalBest);
        double[] randomUniformVector2 = Main.getRandomUniformVector(antennaArray.getNumberOfAntenna() - 1);
        double[] socialVector = multiplyVector(multiplyVectors(randomUniformVector2, vectorToGlobalBest), socialCoefficient);

//        System.out.println("---");
//        System.out.println(Arrays.toString(inertialVector));
//        System.out.println(Arrays.toString(cognitiveVector));
//        System.out.println(Arrays.toString(socialVector));
        
        addVectors(inertialVector, cognitiveVector, socialVector);
        
        position = addVectors(position, velocity);
        
        evaluate(antennaArray);
    }
    
    private void evaluate(AntennaArray antennaArray)
    {
    	double currentValue = antennaArray.evaluate(Main.convertVectorToAntennaArrayDesign(antennaArray, position));
        if (currentValue < personalBestValue)
        {
            personalBest = position;
            personalBestValue = currentValue;
            if (currentValue < globalBestValue)
            {
                globalBest = position;
                globalBestValue = currentValue;
            }
        }
    }

    private double[] multiplyVector(double[] vector, double multiplier)
    {
    	double[] newVector = Arrays.copyOf(vector, vector.length);
        for (int i = 0; i<  newVector.length; i++)
        {
            newVector[i] = newVector[i] * multiplier;
        }
        return newVector;
    }
    
    private double[] multiplyVectors(double[] vectorA, double[] vectorB)
    {
    	checkVectorMathArguments(vectorA, vectorB);
    	double[] newVector = new double[vectorA.length];
    	for (int i = 0; i < newVector.length; i++)
    	{
    		newVector[i] = vectorA[i] * vectorB[i];
    	}
    	return newVector;
    }
    
    private double[] addVectors(double[] ... vectors)
    {
    	checkVectorMathArguments(vectors);
    	double[] newVector = new double[vectors[0].length];
    	
    	for (int i = 0; i < newVector.length; i++)
    	{
    		for (double[] vector : vectors)
    		{
    			newVector[i] =+ vector[i];
    		}
    	}
    	return newVector;
    }
    
    private double[] vectorFromAToB(double[] vectorA, double[] vectorB)
    {
    	checkVectorMathArguments(vectorA, vectorB);
    	double[] vectorFromAToB = new double[vectorA.length];
    	for (int i = 0; i < vectorA.length; i++)
    	{
    		vectorFromAToB[i] = vectorB[i] - vectorA[i];
    	}
    	
    	return vectorFromAToB;
    }
    
    private void checkVectorMathArguments(double[] ... vectors) throws IllegalArgumentException
    {
    	if (vectors.length == 0) throw new IllegalArgumentException("No vectors provided");
    	for (int i = 0; i < vectors.length - 1; i++)
    	{
    		if (vectors[i].length != vectors[i + 1].length)
        	{
        		throw new IllegalArgumentException("Can't perform operation on arrays whose lengths don't match");
        	}
    	}
    }
}
