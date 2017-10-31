
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class GenerateKeys {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		JSONParser parser = new JSONParser();
		int maxCandidates = 10;
		long maxVoters = 10;
		long seed = 1;
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
		VotingSystem vs = new VotingSystem(maxCandidates, maxVoters, seed);
		
		// Put the private key and public key to json file
		JSONObject obj = new JSONObject();
        obj.put("bits", vs.publicKey.getBits());
        obj.put("n", vs.publicKey.getN().toString());
        obj.put("nn", vs.publicKey.getnSquared().toString());
        obj.put("g", vs.publicKey.getG().toString());
        
        obj.put("lambda", vs.keyPair.getPrivateKey().getLambda().toString());
        obj.put("denom", vs.keyPair.getPrivateKey().getPreCalculatedDenominator().toString());
        
        obj.put("maxBits", vs.getMaxBits());

        try (FileWriter file = new FileWriter("JavaCodes/keys.json")) {
            file.write(obj.toString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}
