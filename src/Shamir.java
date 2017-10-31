import java.math.BigInteger;
import java.util.Random;

/**
 * <p> Calculate Shamir scheme. You need t shares of n for resolve the secret </p> 
 */
public class Shamir{
	/**
	 * Create a share using Shamir's scheme 
	 * @param x Shamir's shares
	 * @param k array of shamirKeys
	 * @return true is x is in k
	*/
	public static boolean isRepeat(BigInteger x, ShamirKey[] k){
		if(k.length == 0)
			return false;
		
		for(int i = 0; i < k.length; i++){
			if(k[i] == null)
				break;
			if(k[i].getX() == x)
				return true;		
		}
		
		return false;
	}
	
	/**
	 * Calculate polynomial
	 * @param s parameters of polynomial
	 * @param x variable of polynomial
	 * @param p prime number (for calulate mod)	
	 * @return solution of polynomial
	*/
	public static BigInteger calculatePolynomial(BigInteger s[], BigInteger x, BigInteger p){
		BigInteger result = BigInteger.ZERO;
		for(int i = 0; i < s.length; i++)
			result = result.add(s[i].multiply(x.pow(i)));
		
		result = result.mod(p);
		return result;
	}	
	
	/**
	 * Generate parameters of polynomial
	 * @param t number of shares for solve Shamir scheme
	 * @param numBits number of bits of parameters
	 * @param SecretBytes secret	
	 * @return parameters of polynomial
	 * @throw shamirException
	*/
	public static BigInteger[] generateParameters(int t, int numBits, byte[] SecretBytes)throws ShamirException{
		BigInteger secret = new BigInteger(SecretBytes);
		
		if(secret.bitLength() >= numBits)
			throw new ShamirException("numBits is too small");
						
		BigInteger s[] = new BigInteger[t];
		s[0] = secret;
		//System.out.println("s(0) = " + secret + " (secret)" );
		
		for(int i = 1; i < t; i++){
			s[i] = new BigInteger(numBits, new Random());
			//System.out.println("s("+i+") = " +s[i]);
		}
		
		return s;
	}
	
	/**
	 * Solve Shamir's scheme
	 * @param sk array of shamirKeys
	 * @return secret
	*/
	public static byte[] calculateLagrange(ShamirKey[] sk){
		BigInteger p = sk[0].getP();		
		BigInteger d;
		BigInteger D;		
		BigInteger c;
		BigInteger S = BigInteger.ZERO;		
		for(int i = 0; i < sk.length; i++){
			d = BigInteger.ONE;
			D = BigInteger.ONE;		
			for(int j = 0; j < sk.length; j++){
				if(j==i)
					continue;					
				d = d.multiply(sk[j].getX());
				D = D.multiply(sk[j].getX().subtract(sk[i].getX()));
			}
			c = d.multiply(D.modInverse(p)).mod(p);
			S = S.add(c.multiply(sk[i].getF())).mod(p);
		}
		return S.toByteArray();
	}
	
	/**
	 * Generate shares
	 * @param n number of shares
	 * @param t number of shares for solve Shamir scheme
	 * @param numBits number of bits of shares
	 * @param s parameters of polynomial
	 * @return array of Shamir's shares
	 * @throw shamirException	 
	 * @throw shamirException	 
	*/
	public static ShamirKey[] generateKeys(int n, int t, int numBits, BigInteger[] s)throws ShamirException{
		ShamirKey[] keys = new ShamirKey[n];
		if(s[0].bitLength() >= numBits)
			throw new ShamirException("numBits is too small");			
		if(t > n)
			throw new ShamirException("number of need shares greater than number of shares");

		BigInteger prime = BigInteger.probablePrime(numBits, new Random());

		BigInteger fx, x;
		for(int i = 1; i <= n; i++){
			do{
				x = new BigInteger(numBits, new Random());
			}while(isRepeat(x, keys));		
			fx = calculatePolynomial(s, x, prime);
			keys[i-1] = new ShamirKey();
			keys[i-1].setP(prime);
			keys[i-1].setX(x);
			keys[i-1].setF(fx);
			//System.out.println(i+"-> f("+x+") = " +keys[i-1].getF());
		}	
		return keys;
	}
}