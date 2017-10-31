

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class MathOperations {

	private static final double LOG2 = Math.log(2.0);

	/**
	 * Computes the natural logarithm of a BigInteger. Works for really big integers (practically unlimited).
	 * @param val
	 *            Argument, positive integer
	 * @return Natural logarithm, as in <tt>Math.log()</tt>
	 */
	public static double log(BigInteger val) {
		int blex = val.bitLength() - 1022; // any value in 60..1023 is ok
		if (blex > 0)
			val = val.shiftRight(blex);
		double res = Math.log(val.doubleValue());
		return blex > 0 ? res + blex * LOG2 : res;
	}

	public static double log(BigInteger val, long base) {
		return log(val) / Math.log(base);
	}

	public static double log(double n, long base) {
		return Math.log(n) / Math.log(base);
	}

	public static long nextPowerof2(long maxVoters) {
		int power = (int) Math.ceil(log(maxVoters, 2));
		return (long) Math.pow(2, power);
	}

	public static List<Long> convertBase(BigInteger n, long baseValue, int maxCandidates) {
		List<Long> coeff = new ArrayList<>();
		BigInteger base = BigInteger.valueOf(baseValue);

		while (n != BigInteger.ZERO) {
			long x = n.mod(base).longValue();
			coeff.add(x);
			n = n.divide(base);
		}

		while(coeff.size() < maxCandidates){
			coeff.add(new Long(0));
		}
		return coeff;
	}

}
