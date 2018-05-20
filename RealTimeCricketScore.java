import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cricbuzz.Cricbuzz;
/**
 * 
 * @author dhiraj Pandit
 * for get real time cricket score and show on desktop screen
 * @since 19-may-2018
 */
public class RealTimeCricketScore {
	public void showCricketScore() throws IOException, InterruptedException {
		
		//here use Crickbuzz class for initialization
		Cricbuzz crickbuzz = new Cricbuzz();
		//get list of matches currently in progress.
		Vector<HashMap<String, String>> matches = crickbuzz.matches();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(matches);
		String id = matches.get(0).get("id");
		List<String> matchInfoList = new ArrayList<String>();
		Map<String, Map> score = crickbuzz.livescore(id);
		 System.out.println(gson.toJson(score));
		//get match info, like status, who playing, and which events like that.
		Map<String, String> matchInfo = score.get("matchinfo");
		if(matchInfo.get("mchstate").equalsIgnoreCase("inprogress")) {
		//gets batting info. team, player name, scores
		Map<String, Map> batting = score.get("batting");
		//get bowling info team, if batting already done get score, bowler name
		Map<String, Map> bowling = score.get("bowling");
		
		
		@SuppressWarnings("unchecked")
		Vector<Map<String, String>> battingteam = (Vector<Map<String, String>>) batting.get("team");
		@SuppressWarnings("unchecked")
		Vector<Map<String, String>> bowlingteam = (Vector<Map<String, String>>) bowling.get("team");
		@SuppressWarnings("unchecked")
		Vector<Map<String, String>> battingScore = (Vector<Map<String, String>>) batting.get("score");
		@SuppressWarnings("unchecked")
		Vector<Map<String, String>> bowllingScore = (Vector<Map<String, String>>) bowling.get("score");
		@SuppressWarnings("unchecked")
		Vector<Map<String, String>> batsman = (Vector<Map<String, String>>) batting.get("batsman");
		@SuppressWarnings("unchecked")
		Vector<Map<String, String>> bowler = (Vector<Map<String, String>>) bowling.get("bowler");

       //here i am used string builder class bcz it perform fast.when u concat another string.	
		
		matchInfoList.add(matchInfo.get("srs").toString());
		matchInfoList.add(matchInfo.get("mchdesc"));
		StringBuilder battingInfo = new StringBuilder();
		battingInfo.append(battingteam.get(0).get("team")).append(":").append(battingScore.get(0).get("runs"))
				.append("-").append(battingScore.get(0).get("wickets"));
		matchInfoList.add(battingInfo.toString());
		StringBuilder batsmanInfo = new StringBuilder();
		batsmanInfo.append(batsman.get(0).get("name")).append(":").append(batsman.get(0).get("runs")).append("(")
				.append(batsman.get(0).get("balls")).append(")").append(" , ").append(batsman.get(1).get("name"))
				.append(":").append(batsman.get(1).get("runs")).append("(").append(batsman.get(1).get("balls"))
				.append(")");
		matchInfoList.add(batsmanInfo.toString());
		StringBuilder bowlerInfo = new StringBuilder();
		bowlerInfo.append(bowler.get(0).get("name")).append(":").append(bowler.get(0).get("overs")).append("-")
				.append(bowler.get(0).get("runs")).append("-").append(bowler.get(0).get("wickets"));
		matchInfoList.add(bowlerInfo.toString());
		if (bowllingScore.size() > 0 || bowllingScore != null) {
			StringBuilder bowlingScoreInfo = new StringBuilder();
			bowlingScoreInfo.append(bowlingteam.get(0).get("team")).append(":").append(bowllingScore.get(0).get("runs"))
					.append("-").append(bowllingScore.get(0).get("wickets")).append("(")
					.append(bowllingScore.get(0).get("overs")).append(")");
			matchInfoList.add(bowlingScoreInfo.toString());
		}
		  matchInfoList.add(matchInfo.get("status"));
		}else {
			matchInfoList.add(matchInfo.get("srs").toString());
			matchInfoList.add("No Match In Progress");
		}
		//pass this arraylist to where we have to show our data.
		Notification.showOnDesktop(matchInfoList);
	}

	public static void main(String[] args) {
		//create thread which run every 3 second for getting current score.
		RealTimeCricketScore scoreLive =new RealTimeCricketScore();
        Runnable showScore = new Runnable() {
			
			@Override
			public void run() {
				try {
					scoreLive.showCricketScore();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}			
			}
		};
		//refress every 3 seconds
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		executor.scheduleAtFixedRate(showScore, 0, 3, TimeUnit.SECONDS);
	}
}
