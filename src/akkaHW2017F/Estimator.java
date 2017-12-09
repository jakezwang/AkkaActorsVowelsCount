package akkaHW2017F;

import java.util.List;

import akka.actor.UntypedActor;

/**
 * This is the main actor and the only actor that is created directly under the
 * {@code ActorSystem} This actor creates more child actors
 * {@code WordCountInAFileActor} depending upon the number of files in the given
 * directory structure
 * 
 * @author Jake Wang
 *
 */
public class Estimator extends UntypedActor {

	private final double p1 = 1;
	private final double p2 = 0.8;
	private final double p3 = 1.2;
	private double pDecesion = 1;
	private int estimateCount = 0;
	
	private static int countOfreceiving = 0;
	
	public Estimator() {

	}

	@Override
	public void onReceive(Object msg) throws Throwable {
		Messages.getInstance().eventsLoop.add("Estimator receiverd message x " + ++countOfreceiving + " times");
		if (msg instanceof List<?>) {
			List<String> curMsg = (List<String>) msg; 
			estimateCount = estimateVowels(curMsg);
			Messages.getInstance().eventsLoop.add(getSelf().path().name() + " - vowels count " + estimateCount + " - from " + getSelf().path().name());
		} else if (msg instanceof Integer) {
			Integer curMsg = (Integer) msg;
			if (getSender() != null && getSender().path().name() == "firstCounterActor") {
				Messages.getInstance().eventsLoop.add(getSelf().path().name() + " - vowels count " + curMsg + " - from " + getSender().path().name());
				pDecesion = pAdjust(estimateCount, curMsg);
				Messages.getInstance().eventsLoop.add("Coefficient after 1st Counter adjust is: " + pDecesion);
				
			} else if (getSender() != null && getSender().path().name() == "secondCounterActor") {
				Messages.getInstance().eventsLoop.add(getSelf().path().name() + " - vowels count " + curMsg + " - from " + getSender().path().name());
				pDecesion = pAdjust(estimateCount, curMsg);
				Messages.getInstance().eventsLoop.add("Coefficient after 2nd Counter adjust is: " + pDecesion);
				User.actorRefUser.tell(Messages.getInstance().eventsLoop, getSelf());
			} else {
				Messages.getInstance().eventsLoop.add("Estimator received invalid msg");
				throw new Exception("Estimator received invalid msg");
			}
		}
	}
	
	private int estimateVowels(List<String> curMsg) {
		Integer counter = 0;
		for (String s : curMsg) {
			for (int i = 0; i < s.length(); i++) {
				if (isVowel(s.charAt(i))) counter++;
			}
		}
		return counter;
	}
	
	private boolean isVowel(char c) {
		char[] vowels = {'A', 'E', 'I', 'O', 'U', 'Y'};
		for (char vowel : vowels) {
			if (c == vowel) return true;
		}
		return false;
	}
	
	private double pAdjust(int estimateCount, int compareCount) {
		if (estimateCount == compareCount) return p1;
		return estimateCount > compareCount ? p2 : p3;
	}

}
