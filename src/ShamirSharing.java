import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ShamirSharing {
	int numShares; // number of shares to generate
	int threshold; // number of shares to solve the secret (t <= n)
	int numBits; // number of bits of p

	public ShamirSharing(int numShares, int threshold, int numBits, long seed) {
		this.numShares = numShares;
		this.threshold = threshold;
		this.numBits = numBits;
	}

	@SuppressWarnings("unchecked")
	public void writeSharesToFile(ShamirKey[] shares) {
		JSONArray list = new JSONArray();
		for (int i = 0; i < shares.length; i++) {
			JSONObject obj = new JSONObject();
			obj.put("p", shares[i].getP().toString());
			obj.put("f", shares[i].getF().toString());
			obj.put("x", shares[i].getX().toString());
			list.add(obj);
		}
		try (FileWriter file = new FileWriter("JavaCodes/shares.json")) {
			file.write(list.toString());
			file.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ShamirKey[] getSharesFromFile(int[] indexes) {
		JSONParser parser = new JSONParser();
		try {
			VotingSystem vs = new VotingSystem();
			JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("JavaCodes/indices.json"));
			List<Integer> indices = (List<Integer>) jsonObject.get("indices");
		} catch (Exception e) {

		}
		return null;
	}

	public String interpolate(ShamirKey[] secretKeys) {
		return new String(Shamir.calculateLagrange(secretKeys));
	}

	public ShamirKey[] split(String secret) throws ShamirException {
		BigInteger[] shamirParameters = Shamir.generateParameters(threshold, numBits, secret.getBytes());
		return Shamir.generateKeys(numShares, threshold, numBits, shamirParameters);
	}

	public static void main(String args[]) {	
		// params, n, k, seed
		demo();
	}

	private static void demo() {
		String secret = "Hello";
		System.out.println("Secret to split = " + secret);
		ShamirSharing ex = new ShamirSharing(6, 5, 50, 2);
		// Create key
		ShamirKey[] sk;
		try {
			sk = ex.split(secret);
		} catch (ShamirException e) {
			System.out.println("Some error happened during Shamir split.");
			return;
		}
		ex.writeSharesToFile(sk);

		ShamirKey[] sk2 = new ShamirKey[ex.threshold];
		// select t keys
		for (int i = 0; i < ex.threshold; i++) {
			sk2[i] = sk[i];
		}

		// solve scheme, calculate parameter 0 (secret)
		String text = ex.interpolate(sk2);
		System.out.println("After merging, Secret = " + text);
	}
}