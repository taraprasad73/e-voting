

import java.math.BigInteger;

public class HomomorphicOperations {
	public PublicKey publicKey;

	public HomomorphicOperations(PublicKey publicKey) {
		this.publicKey = publicKey;
	}

	public BigInteger add(BigInteger encryptedA, BigInteger encryptedB) {
		return encryptedA.multiply(encryptedB).mod(publicKey.getnSquared());
	}
}
