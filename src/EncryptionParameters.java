

public class EncryptionParameters {

	/*
	 * (n = pq) > maxVoters^maxCandidates Therefore, maxBits = log10(maxVoters)
	 * * maxCandidates. Example: maxVoters = 1E10, maxCandidates = 100, then
	 * maxBits = 1000
	 */
	public static int getMaxBits(long maxVoters, int maxCandidates) {
		int power = (int) Math.ceil(MathOperations.log(maxVoters, 2));
		return power * maxCandidates;
	}
}
