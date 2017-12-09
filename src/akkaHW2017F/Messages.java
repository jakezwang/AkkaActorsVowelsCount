package akkaHW2017F;

import java.util.ArrayList;
import java.util.List;

/**
 * Messages that are passed around the actors are usually immutable classes.
 * Think how you go about creating immutable classes:) Make them all static
 * classes inside the Messages class.
 * 
 * This class should have all the immutable messages that you need to pass
 * around actors. You are free to add more classes(Messages) that you think is
 * necessary
 * 
 * @author Jake Wang
 *
 */
public class Messages {
	public static List<String> fileContent;
	public List<String> firstHalves;
	public List<String> secondHalves;
	public String file;
	public Integer count;
	public List<String> eventsLoop;
	
	
	private static Messages instance = null;
	
	
	public Messages() {
		fileContent = new ArrayList<>();
		firstHalves = new ArrayList<>();
		secondHalves = new ArrayList<>();
		file = "data/Akka10.txt";
		count = 0;
		eventsLoop = new ArrayList<>();
	}
	
	public static Messages getInstance() {
	   if(instance == null) {
	      instance = new Messages();
	   }
	   return instance;
	}
}