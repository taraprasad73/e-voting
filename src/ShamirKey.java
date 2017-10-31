import java.math.BigInteger;

/**
 * <p> Contains a Shamir's key <br>
 * f = poly(x) mod p </p> 
 */
public class ShamirKey{
	@Override
	public String toString() {
		return "ShamirKey [p=" + p + ", f=" + f + ", x=" + x + "]";
	}

	private BigInteger p;
	private BigInteger f;
	private BigInteger x;
	
	/**
	 * Set p value
	 * @param p Prime
	*/
	public void setP(BigInteger p){this.p = p;}

	/**
	 * Set f value
	 * @param f Polynomial result
	*/
	public void setF(BigInteger f){this.f = f;}

	/**
	 * Set x value
	 * @param x Public part
	*/
	public void setX(BigInteger x){this.x = x;}	

	/**
	 * Set p value
	 * @return p
	*/	
	public BigInteger getP(){return p;}

	/**
	 * Set f value
	 * @return f
	*/	
	public BigInteger getF(){return f;}	

	/**
	 * Set x value
	 * @return x
	*/	
	public BigInteger getX(){return x;}
}