
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class FetchTallyResults {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		JSONParser parser = new JSONParser();

		try {
			VotingSystem vs = new VotingSystem();
			JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("JavaCodes/tally.json"));

			List<String> votes = (List<String>) jsonObject.get("votes");
			List<BigInteger> v = new ArrayList<>();
			for (String vote : votes) {
				v.add(new BigInteger(vote));
			}		
			System.out.println(vs.tallyVotes(v));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
