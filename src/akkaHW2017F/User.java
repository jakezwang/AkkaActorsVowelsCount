package akkaHW2017F;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.concurrent.TimeUnit;

import akka.actor.*;

/**
 * Main class for your estimation actor system.
 *
 * @author Jake Wang
 *
 */
public class User extends UntypedActor {
	

	
	static ActorRef actorRefUser;
	static ActorRef actorRefEstimator;
	static ActorRef actorRefFirstCounter;
	static ActorRef actorRefSecondCounter;
	
	
	public static void main(String[] args) throws Exception {
		ActorSystem system = ActorSystem.create("EstimationSystem");

		/*
		 * Create the Estimator Actor and send it the StartProcessingFolder
		 * message. Once you get back the response, use it to print the result.
		 * Remember, there is only one actor directly under the ActorSystem.
		 * Also, do not forget to shutdown the actorsystem
		 */
		
		Props propsUser = Props.create(User.class);
		Props propsEstimator = Props.create(Estimator.class);
		Props propsFirstCounter = Props.create(FirstCounter.class);
		Props propsSecondCounter = Props.create(SecondCounter.class);
		
		actorRefUser = system.actorOf(propsUser, "userActor");
		actorRefEstimator = system.actorOf(propsEstimator, "estimatorActor");
		actorRefFirstCounter = system.actorOf(propsFirstCounter, "firstCounterActor");
		actorRefSecondCounter = system.actorOf(propsSecondCounter, "secondCounterActor");
		
		actorRefUser.tell("start", null);
		system.terminate();
	}

	@Override
	public void onReceive(Object msg) throws Throwable {
		if (msg instanceof String) {
			String curMsg = (String) msg;
			if (curMsg == "start") {
				System.out.println("Program started from: " + getSelf().path().name());
				System.out.println("Current testing file: " + Messages.getInstance().file);
				
				// Divide into 2 halves
				try (BufferedReader br = new BufferedReader(new FileReader(Messages.getInstance().file))) {
				    String line;
				    while ((line = br.readLine()) != null) {
				       Messages.getInstance().fileContent.add(line);
				    }
				} catch (Exception e) {
					System.out.println("Error: " + e);
				}
				
				for (int i = 0; i < Messages.getInstance().fileContent.size(); i++) {
					if (i < Messages.getInstance().fileContent.size() / 2) {
						Messages.getInstance().firstHalves.add(Messages.getInstance().fileContent.get(i));
					} else {
						Messages.getInstance().secondHalves.add(Messages.getInstance().fileContent.get(i));
					}
				}
				System.out.println("Content size: " + Messages.getInstance().fileContent.size() + " lines");
				System.out.println("1st Halves size: " + Messages.getInstance().firstHalves.size() + " lines");
				System.out.println("2nd Halves size: " + Messages.getInstance().secondHalves.size() + " lines");
				System.out.println();	
						
				// Send to actors
				System.out.println("User sending message to Estimator......");
				actorRefEstimator.tell(Messages.getInstance().firstHalves, getSelf());
				TimeUnit.MILLISECONDS.sleep(1);
				System.out.println("User sending message to 1st Counter......");
				actorRefFirstCounter.tell(Messages.getInstance().firstHalves, actorRefEstimator);
				TimeUnit.MILLISECONDS.sleep(1);
				System.out.println("User sending message to 2nd Counter......");
				actorRefSecondCounter.tell(Messages.getInstance().secondHalves, actorRefEstimator);
			} else {
				System.out.println("------------Results from User---------");
				System.out.println(curMsg);
			}
		} else if (msg instanceof List<?>) {
			System.out.println();
			System.out.println("------------Results message loop from Estimator---------");
			List<String> curMsg = (List<String>) msg;
			for (String s : curMsg) {
				System.out.println(s);
			}
		} else {
			throw new Exception("User received invalid msg");
		}
		
	}

}
