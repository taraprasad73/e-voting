import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class VotingSystem {
	private KeyPairBuilder keyGen;
	public KeyPair keyPair;
	public PublicKey publicKey;
	public int bits;
	private long maxVoters; // base
	private int maxCandidates;

	public int getMaxBits() {
		return bits;
	}
	
	public VotingSystem() {
		readParams();
//		readKeys();
		generateKeys();
	}
	
	private void generateKeys() {
		
	}

	public VotingSystem(int maxCandidates, long maxVoters, long seed) {
		this.maxCandidates = maxCandidates;
		this.maxVoters = maxVoters;
		this.bits = EncryptionParameters.getMaxBits(maxVoters, maxCandidates);
	
		keyGen = new KeyPairBuilder();
		keyGen.bits(this.bits);
		Random rng = new SecureRandom();
		rng.setSeed(seed); // don't use 0
		keyGen.randomNumberGenerator(rng);
		this.keyPair = keyGen.generateKeyPair();
		this.publicKey = keyPair.getPublicKey();
	}

	public void readParams() {
		JSONParser parser = new JSONParser();
		try {
			JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("JavaCodes/params.json"));
			maxCandidates = ((Long) jsonObject.get("candidateCount")).intValue();
			maxVoters = (Long) jsonObject.get("voterCount");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("Some problem occurred while loading the params from the JSON file.");
			e.printStackTrace();
		}
	}
	
	private void readKeys() {
		JSONParser parser = new JSONParser();
		try {
			JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("JavaCodes/keys.json"));
			int bits = ((Long) jsonObject.get("bits")).intValue();
			BigInteger n = new BigInteger((String) jsonObject.get("n"));
			BigInteger nn = new BigInteger((String) jsonObject.get("nn"));
			BigInteger g = new BigInteger((String) jsonObject.get("g"));
			this.publicKey = new PublicKey(n, nn, g, bits);
			
			BigInteger lambda = new BigInteger((String) jsonObject.get("lambda"));
			BigInteger denom = new BigInteger((String) jsonObject.get("denom"));
			PrivateKey privateKey = new PrivateKey(lambda, denom);
			this.keyPair = new KeyPair(privateKey, publicKey, null);
			this.bits = ((Long) jsonObject.get("maxBits")).intValue();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("Some problem occurred while loading the keys from the JSON file.");
			e.printStackTrace();
		}
	}
	
	/*
	 * Dummy method. For testing purposes only.
	 */
//	private void setKeys() {
//		this.maxCandidates = 5;
//		this.maxVoters = 9;
//		this.publicKey = new PublicKey(new BigInteger("521473"), new BigInteger("271934089729"),
//				new BigInteger("719778"), 20);
//		PrivateKey privateKey = new PrivateKey(new BigInteger("86670"), new BigInteger("365350"));
//		this.keyPair = new KeyPair(privateKey, publicKey, null);
//	}

	public List<Long> tallyVotes(List<BigInteger> votes) {
		PrivateKey pk = new PrivateKey(interpolateL(), interpolateD());
		BigInteger sum = keyPair.decrypt(sumEncyptedVotes(votes));
		List<Long> counts = MathOperations.convertBase(sum, maxVoters, maxCandidates);

		// System.out.println("- Results -");
		// for (int i = 0; i < counts.size(); i++) {
		// System.out.println("Candidate " + i + ": " + counts.get(i));
		// }
		return counts;
	}

	private BigInteger interpolateD() {
		// TODO Auto-generated method stub
		return null;
	}

	private BigInteger interpolateL() {
		return null;
	}

	public BigInteger encryptVote(int candidate) {
		if (candidate < 0 || candidate >= maxCandidates) {
			System.err.println("candidate = " + candidate + " is invalid.");
			throw new IndexOutOfBoundsException();
		}
		BigInteger x = BigInteger.valueOf(maxVoters);
		return publicKey.encrypt(x.pow(candidate));
	}

	public long decryptVote(BigInteger vote) {
		return (long) MathOperations.log(keyPair.decrypt(vote), maxVoters);
	}

	private BigInteger sumEncyptedVotes(List<BigInteger> votes) {
		HomomorphicOperations homomorphicOperations = new HomomorphicOperations(publicKey);
		BigInteger sum = BigInteger.ONE;
		for (BigInteger vote : votes) {
			sum = homomorphicOperations.add(sum, vote);
		}
		return sum;
	}

	public static void main(String[] args) {
		VotingSystem votingSystem = new VotingSystem(5, 9, 1);
		System.out.println(votingSystem.keyPair);
		System.out.println(votingSystem.publicKey);
		List<BigInteger> votes = new ArrayList<>();

		votes.add(votingSystem.encryptVote(4));
		votes.add(votingSystem.encryptVote(3));
		votes.add(votingSystem.encryptVote(3));
		votes.add(votingSystem.encryptVote(3));
		votes.add(votingSystem.encryptVote(3));
		votes.add(votingSystem.encryptVote(3));
		votes.add(votingSystem.encryptVote(2));
		votes.add(votingSystem.encryptVote(2));
		votes.add(votingSystem.encryptVote(1));
		votes.add(votingSystem.encryptVote(1));
		votes.add(votingSystem.encryptVote(1));
		votes.add(votingSystem.encryptVote(0));
		votes.add(votingSystem.encryptVote(0));

		System.out.println(votingSystem.tallyVotes(votes));
		System.out.println(votes);

		System.out.println(votingSystem.decryptVote(votingSystem.encryptVote(4)));
		System.out.println(votingSystem.decryptVote(votingSystem.encryptVote(3)));
		System.out.println(votingSystem.decryptVote(votingSystem.encryptVote(1)));
	}
}
