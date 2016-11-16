package co.thehotnews.lambda;

import java.util.Arrays;
import java.util.HashSet;

import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;

/**
 * This class is the handler for an AWS Lambda function powering the ASK Skill
 * To do this, simply set the handler field in the AWS Lambda console to
 * "co.thehotnews.lambda.TheHotNewsRequestStreamHandler" For this to work you will need
 * to build the app with the following Maven command: 
 * 		mvn assembly:assembly -DdescriptorId=jar-with-dependencies package
 * 
 */
public final class TheHotNewsRequestStreamHandler extends SpeechletRequestStreamHandler {
    
	// Application ID issued when you create your skill in the Amazon Developer Console.
	private static final String ASK_SKILL_ID = "amzn1.ask.skill.000000000000000";
	
    public TheHotNewsRequestStreamHandler() {    	
        super(new TheHotNewsSpeechlet(), new HashSet<String>(Arrays.asList(ASK_SKILL_ID)));   
    }
}
