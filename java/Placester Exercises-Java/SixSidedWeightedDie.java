package com.placester.test;

/*
 * Implement a 6 sided die with weights on the sides, so that we don't have an even probability distribution, but it is 
 * weighted by a list of weights passed in at construction time
 * 
 * After 10k iterations of throwing this die, the results should closely match the desired distribution, and this should
 * be reproducible in the unit test in
 * 
 * src/test/com/placester/test/WeightedDiceTest
 */
public class SixSidedWeightedDie extends WeightedDie
{
	private float _weights[];

    //NOTE: since these are weights on a probability distribution, these should sum to one, and the incoming array
    // should be of length 6. You should throw if either of these preconditions is false
    public SixSidedWeightedDie(float weights[])
    {
        super(weights);

        if (weights.length != 6)
        	throw new IllegalArgumentException("Must provide 6 weight values. Provided: " + weights);

        float sum = 0f;
        for (int i = 0; i < weights.length; i++) sum += weights[i];
        if (sum != 1.0f)
        	throw new IllegalArgumentException(
        		"Weight values must sum to 1.0. Weights provided (" + weights + ") sum to: " + sum);

        _weights = weights;
    }

    //Throw the die: this should produce a value in [1,6]
    @Override
    public int throwDie()
    {
        float sum = 0, r = (float)Math.random();
        for (int i = 0; i < _weights.length; i++) {
        	sum += _weights[i];
        	if (sum >= r) return i+1;
        }
        return _weights.length;
    }
}
