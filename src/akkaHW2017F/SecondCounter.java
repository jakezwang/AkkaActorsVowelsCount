package akkaHW2017F;

import java.util.List;

import akka.actor.UntypedActor;

/**
 * this actor reads the file, counts the vowels and sends the result to
 * Estimator. 
 *
 * @author Jake Wang
 *
 */
public class SecondCounter extends UntypedActor {

	public SecondCounter() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onReceive(Object msg) throws Throwable {
		Messages.getInstance().eventsLoop.add("2nd Counter receiverd message");
		if (msg instanceof List<?>) {
			List<String> firstHalves = (List<String>) msg;
			Integer counter = 0;
			for (String s : firstHalves) {
				for (int i = 0; i < s.length(); i++) {
					if (isVowel(s.charAt(i))) counter++;
				}
			}
			getSender().tell(counter, getSelf());
		} else {
			Messages.getInstance().eventsLoop.add("SecondCounter received invalid msg");
			throw new Exception("SecondCounter received invalid msg");
		}
	}
	
	private boolean isVowel(char c) {
		char[] vowels = {'A', 'E', 'I', 'O', 'U', 'Y'};
		for (char vowel : vowels) {
			if (c == vowel) return true;
		}
		return false;
	}

}
