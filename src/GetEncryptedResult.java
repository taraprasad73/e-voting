
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class GetEncryptedResult {
	
	public static void main(String [] args) throws Exception {
		JSONParser parser = new JSONParser();
		try {
			VotingSystem vs = new VotingSystem();
			JSONObject jsonObject = (JSONObject)parser.parse(new FileReader("JavaCodes/encrypt.json"));
//			JSONObject jsonObject = (JSONObject) parser.parse(new StringReader(args[0]));
			String candidate = ((String)jsonObject.get("candidateId"));
			System.out.println(vs.encryptVote(Integer.parseInt(candidate)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

}
