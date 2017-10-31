

import java.math.BigInteger;

/**
 * A class that represents the private part of the Paillier key pair.
 */
public class PrivateKey {

	private final BigInteger lambda;
    private final BigInteger preCalculatedDenominator;

    @Override
	public String toString() {
		return "PrivateKey [lambda=" + lambda + ", preCalculatedDenominator=" + preCalculatedDenominator + "]";
	}
    
    PrivateKey(BigInteger lambda, BigInteger preCalculatedDenominator) {
        this.lambda = lambda;

        this.preCalculatedDenominator = preCalculatedDenominator;
    }

    public BigInteger getLambda() {
        return lambda;
    }

    public BigInteger getPreCalculatedDenominator() {
        return preCalculatedDenominator;
    }
}
