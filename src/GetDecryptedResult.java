
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.text.ParseException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class GetDecryptedResult {
	
	public static void main(String[] args) throws Exception {

		JSONParser parser = new JSONParser();

		try {
			//get keys from database
			VotingSystem vs = new VotingSystem();
			JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("JavaCodes/decrypt.json"));
//			JSONObject jsonObject = (JSONObject) parser.parse(new StringReader(args[0]));
			String s = (String)jsonObject.get("encryptedVote");
			BigInteger encryptedVote = new BigInteger(s);
			System.out.println(vs.decryptVote(encryptedVote));
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

}
