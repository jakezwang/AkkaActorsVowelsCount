package akkaHW2017F;

import java.util.*;

import akka.actor.UntypedActor;

/**
 * this actor reads the file, counts the vowels and sends the result to
 * Estimator. 
 *
 * @author Jake Wang
 *
 */
public class FirstCounter extends UntypedActor {
	
	public FirstCounter() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onReceive(Object msg) throws Throwable {
		if (msg instanceof List<?>) {
			Messages.getInstance().eventsLoop.add("1st Counter receiverd message");
			List<String> firstHalves = (List<String>) msg;
			Integer counter = 0;
			for (String s : firstHalves) {
				for (int i = 0; i < s.length(); i++) {
					if (isVowel(s.charAt(i))) counter++;
				}
			}
			getSender().tell(counter, getSelf());
		} else {
			Messages.getInstance().eventsLoop.add("FirstCounter received invalid msg");
			throw new Exception("FirstCounter received invalid msg");
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
