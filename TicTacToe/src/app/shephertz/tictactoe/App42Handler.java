package app.shephertz.tictactoe;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import app.shephertz.social.FacebookService;
import app.shephertz.social.UserContext;

import com.shephertz.app42.paas.sdk.android.App42API;
import com.shephertz.app42.paas.sdk.android.App42CallBack;
import com.shephertz.app42.paas.sdk.android.game.Game;
import com.shephertz.app42.paas.sdk.android.game.ScoreBoardService;
import com.shephertz.app42.paas.sdk.android.social.Social;
import com.shephertz.app42.paas.sdk.android.social.SocialService;
import com.shephertz.app42.paas.sdk.android.storage.Query;
import com.shephertz.app42.paas.sdk.android.storage.QueryBuilder;
import com.shephertz.app42.paas.sdk.android.storage.QueryBuilder.Operator;
import com.shephertz.app42.paas.sdk.android.user.UserService;

public class App42Handler {
	
	private static App42Handler app42Handler;
	
	private String API_KEY = "4f98a47b868d882e0c68af8f3b51496702398bcab79fd7ca686ddacafb90cb48";
	private String SECRET_KEY = "8251fc8f770deadb254dacb959996ef2c4d557db936f937fbb83d3951c7afd69";
	
//	private String API_KEY = "42c04922c093f02822d5a87f628bdf5cad546a295e62ab05c35ee038a2e2f7c4";
//	private String SECRET_KEY = "aa7ab9b14cea8c7a2b6af683a07fb25df1505e4a763878057f5dba92b0ddedaa";
	
	private ScoreBoardService scoreBoardService;
	
	public static final String GAME_NAME = "TicTacToeSocial";
	
	public static final String FB_APP_ID = "820199234676828";//TicTacToe
	
	private App42Handler(Context ctx){
		App42API.initialize(ctx, API_KEY, SECRET_KEY);
//		App42API.setBaseURL("http://", "192.168.1.65", 8082);
		scoreBoardService = App42API.buildScoreBoardService();
	}
	public static void initialize(Context ctx){
		app42Handler = new App42Handler(ctx);
	}
	
	public static App42Handler instance(){
		return app42Handler;
	}
	
	public void setUserCredentials(){
		try {
			JSONObject data = new JSONObject();
			data.put("name", UserContext.MyDisplayName);
			data.put("id", UserContext.MyUserName);
			App42API.setDbName("QUERYTEST");
		    scoreBoardService.addJSONObject("tictactoe", data);
		} catch (Exception e) {
			Log.d("setUserCredentials", e.toString());
		}
	}
	
	public void submitScore(String uName, long score, App42CallBack callBack){
		scoreBoardService.saveUserScore(GAME_NAME, uName, new BigDecimal(score), callBack);
	}
	
	public void getLeaderBoard(App42CallBack callBack){
		scoreBoardService.getTopNRankers(GAME_NAME, 10, callBack);
	}
	
	public void getSocialLeaderBoard(String fbAccessToken, App42CallBack callBack){
		scoreBoardService.getTopNRankersFromFacebook(GAME_NAME, fbAccessToken, 10, callBack);
	}
	
	public static boolean isAuthenticated(){
		return (FacebookService.instance().isFacebookSessionValid() && UserContext.MyUserName.length() > 0);
	}
	
}
