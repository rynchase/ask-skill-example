package co.thehotnews.twitter;

import java.util.HashMap;
import java.util.Map;

import co.thehotnews.exception.EmptyNameException;
import co.thehotnews.exception.TwitterUserNotFoundException;

/**
 * Limited set of people that the skill supports.
 * @author rynchase
 *
 */
public class PeopleLookup {

	private static final TwitterUser TAYLOR_SWIFT = new TwitterUser("Taylor Swift", "taylorswift13");
	private static final TwitterUser KANYE_WEST = new TwitterUser("Kayne West", "kanyewest");
	private static final TwitterUser KIM_KARDASHIAN = new TwitterUser("Kim Kardashian", "KimKardashian");
	private static final TwitterUser KATY_PERRY = new TwitterUser("KATY PERRY", "katyperry");
	private static final TwitterUser JUSTIN_BIEBER = new TwitterUser("Justin Bieber", "justinbieber");
	private static final TwitterUser OBAMA = new TwitterUser("Barack Obama", "BarackObama");
	private static final TwitterUser RHIANNNA = new TwitterUser("Rihanna", "rihanna");
	private static final TwitterUser JT = new TwitterUser("Justin Timberlake", "jtimberlake");
	private static final TwitterUser FALLON = new TwitterUser("Jimmy Fallon", "jimmyfallon");
	private static final TwitterUser TRUMP = new TwitterUser("Donald J. Trump Jr.", "realDonaldTrump");
	private static final TwitterUser HIL = new TwitterUser("Hillary Clinton", "HillaryClinton");
	
	
	private static Map<String, TwitterUser> nameMap = new HashMap();
	static {
		nameMap.put("taylor", TAYLOR_SWIFT );
		nameMap.put("taylor swift", TAYLOR_SWIFT );
		
		// Had to put in some different spellings for the Kanye
		// since Alexa sometimes interprets what is said in some
		// funny ways. For Example: 'tanya'. Alexa sometimes hears
		// that instead of Kanye.
		nameMap.put("kanye", KANYE_WEST);
		nameMap.put("tanya", KANYE_WEST);
		nameMap.put("kanye west", KANYE_WEST);
		nameMap.put("yeezy", KANYE_WEST);
		nameMap.put("yeezus", KANYE_WEST);
		
		nameMap.put("kim", KIM_KARDASHIAN);
		nameMap.put("kim kardashian", KIM_KARDASHIAN);
		nameMap.put("kim kardashian west", KIM_KARDASHIAN);
		
		nameMap.put("katy", KATY_PERRY);
		nameMap.put("katie", KATY_PERRY);
		nameMap.put("katy perry", KATY_PERRY);
		nameMap.put("katy parry", KATY_PERRY);
		nameMap.put("katie perry", KATY_PERRY);
		nameMap.put("katie parry", KATY_PERRY);
		
		nameMap.put("justin bieber", JUSTIN_BIEBER);
		
		nameMap.put("potus",  OBAMA);
		nameMap.put("the president",  OBAMA);
		nameMap.put("president obama",  OBAMA);
		nameMap.put("barack",  OBAMA);
		nameMap.put("obama",  OBAMA);
		nameMap.put("barack obama",  OBAMA);
		
		nameMap.put("rhianna",  RHIANNNA);
		
		nameMap.put("j.t.",  JT);
		nameMap.put("timberlake",  JT);
		nameMap.put("justin timberlake",  JT);
		nameMap.put("justin",  JT);
		
		nameMap.put("jimmy fallon", FALLON);
		nameMap.put("jimmy", FALLON);
		
		nameMap.put("the donald",  TRUMP);
		nameMap.put("trump",  TRUMP);
		nameMap.put("donald trump",  TRUMP);
		nameMap.put("donald chump",  TRUMP);
		nameMap.put("donald j trump",  TRUMP);
		nameMap.put("donald j trump jr",  TRUMP);
		
		nameMap.put("hillary", HIL);
		nameMap.put("hillary clinton", HIL);
		nameMap.put("clinton", HIL);
		
	}
	
	public static TwitterUser get(String name) throws TwitterUserNotFoundException, EmptyNameException {
		if (name == null || name.trim().length() == 0) {
			throw new EmptyNameException();
		}
		TwitterUser twitterUser = nameMap.get(name.toLowerCase().trim());
		if ( twitterUser == null ) {
			throw new TwitterUserNotFoundException(name);
		}
		return twitterUser;
	}
	
}
