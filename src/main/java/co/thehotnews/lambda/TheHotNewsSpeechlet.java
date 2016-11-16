/**
    Copyright 2014-2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package co.thehotnews.lambda;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.Image;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
import com.amazon.speech.ui.StandardCard;

import co.thehotnews.exception.EmptyNameException;
import co.thehotnews.exception.TwitterUserNotFoundException;
import co.thehotnews.twitter.PeopleLookup;
import co.thehotnews.twitter.TwitterUser;
import co.thehotnews.utils.ResponseUtils;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * This sample shows how to create a simple speechlet for handling speechlet
 * requests.
 */
public class TheHotNewsSpeechlet implements Speechlet {
	
	// Set up a dev account with Twitter to get API access to fill in these 
	// keys
	private static final String TWITTER_OAUTH_CONSUMER_KEY = "";
	private static final String TWITTER_OAUTH_CONSUMER_SECRET = "";
	private static final String TWITTER_OAUTH_ACCESS_TOKEN = "";
	private static final String TWITTER_OAUTH_ACCESS_TOKEN_SECRET = "";
	
	// Slot Names from sample utterances
	private static final String SLOT_LAST_NAME = "lastName";
	private static final String SLOT_FIRST_NAME = "firstName";
	
	// Intents
	private static final String INCOMPLETE_INTENT = "IncompleteIntent";
	private static final String AMAZON_HELP_INTENT = "AMAZON.HelpIntent";
	private static final String CANCEL_INTENT = "CancelIntent";
	private static final String WHATS_UP_WITH_PERSON_TWO_NAME_INTENT = "WhatsUpWithPersonTwoNameIntent";
	private static final String WHATS_UP_WITH_PERSON_INTENT = "WhatsUpWithPersonIntent";
	
	private static final String APP_NAME = "The Hot News";
	private static final String LAUNCH_MESSAGE = "Welcome to " + APP_NAME + ". Who do you want to get an update from?";
	private static final String EMPTY_NAME_RESPONSE = "Who would you like an update from?";
	private static final String ERROR_TEXT = "Sorry, we couldn't find an update.";
	private static final String HELP_TEXT = "You can ask the hot news about your favorite celebs. Try asking the hot news for an update about Taylor Swift or Kanye West. Who would you like to hear the Hot News about?";
	private static final String TWITTER_USER_NOT_FOUND_TEXT = "Sorry, but the Hot News couldn't find an update for %s";
	
	private static final Logger log = LoggerFactory.getLogger(TheHotNewsSpeechlet.class);
	
	private final TwitterFactory twitterFactory;
	
	public TheHotNewsSpeechlet() {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		  .setOAuthConsumerKey(TWITTER_OAUTH_CONSUMER_KEY)
		  .setOAuthConsumerSecret(TWITTER_OAUTH_CONSUMER_SECRET)
		  .setOAuthAccessToken(TWITTER_OAUTH_ACCESS_TOKEN)
		  .setOAuthAccessTokenSecret(TWITTER_OAUTH_ACCESS_TOKEN_SECRET);
		twitterFactory = new TwitterFactory(cb.build());	
	}
	
	@Override
	public void onSessionStarted(final SessionStartedRequest request, final Session session) throws SpeechletException {
		log.info("onSessionStarted requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());
		// any initialization logic goes here
	}

	@Override
	public SpeechletResponse onLaunch(final LaunchRequest request, final Session session) throws SpeechletException {
		log.info("onLaunch requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());
		return getLaunchResponse();
	}

	@Override
	public SpeechletResponse onIntent(final IntentRequest request, final Session session) throws SpeechletException {
		SpeechletResponse response;
		log.info("onIntent requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());
		Intent intent = request.getIntent();
		String intentName = intent.getName();
		try {
			if (WHATS_UP_WITH_PERSON_INTENT.equals(intentName)) {
				String name = intent.getSlots().get(SLOT_FIRST_NAME).getValue();
				TwitterUser twitterUser = PeopleLookup.get(name);
				response = getCurrentStatus(twitterUser);
			} else if(WHATS_UP_WITH_PERSON_TWO_NAME_INTENT.equals(intentName)) {
				String name = intent.getSlots().get(SLOT_FIRST_NAME).getValue();
				String lastName = intent.getSlots().get(SLOT_LAST_NAME).getValue();
				String fullname = String.format("%s %s", name, lastName);
				TwitterUser twitterUser = PeopleLookup.get(fullname);
				return getCurrentStatus(twitterUser);
			} else if (CANCEL_INTENT.equals(intentName) ) {
				response = SpeechletResponse.newTellResponse(new PlainTextOutputSpeech());
			} else if (AMAZON_HELP_INTENT.equals(intentName)) {
				response = getHelpResponse();
			} else if(INCOMPLETE_INTENT.equals(intentName)) {
				response = getEmptyNameResponse();
			} else {
				response = getErrorResponse();
			}
		} catch (TwitterUserNotFoundException tunfe) {
			response = getExceptionResponse(tunfe);
		} catch (EmptyNameException ene) {
			response = getEmptyNameResponse();
		}
		return response;
	}
	
	

	@Override
	public void onSessionEnded(final SessionEndedRequest request, final Session session) throws SpeechletException {
		log.info("onSessionEnded requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());
		// any cleanup logic goes here
	}

	
	private SpeechletResponse getCurrentStatus(TwitterUser twitterUser) {
		return getCurrentStatus(twitterUser.getName(), twitterUser.getHandle());
	}
	
	private SpeechletResponse getCurrentStatus(String name, String screenName) {
		List<Status> statuses = null;
		try {
			Twitter twitter = twitterFactory.getInstance();
			statuses = twitter.getUserTimeline(screenName);
			
		} catch (Exception e) {
			log.error("Problem getting current status for " + screenName, e);
			return getErrorResponse();
		}

		Status status = statuses.get(0);
		
		String statusText = ResponseUtils.removeUrl(status.getText());
		if ( statusText == null || statusText.trim().length() == 0 ) {
			// assume there was only a link in the tweet.
			statusText = " a link. ";
		}
		
		String verb = status.isRetweet() ? " retweeted: " : " tweeted: ";
		String speechText = name+ verb + statusText;
		String cardText = name+ verb + status.getText();
		String mediaURl = null;
		if ( status.getMediaEntities() != null && status.getMediaEntities().length > 0) {
			mediaURl = status.getMediaEntities()[0].getMediaURLHttps();
		}

		StandardCard card = new StandardCard();
		card.setTitle(APP_NAME);
		card.setText(cardText);
		if (mediaURl != null) {
			Image image = new Image();
			image.setLargeImageUrl(mediaURl);
			card.setImage(image);
		}

		// Create the plain text output.
		PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText(speechText);
		
		return SpeechletResponse.newTellResponse(speech, card);
	}

	
	private SpeechletResponse getLaunchResponse() {
		// Create the Simple card content.
		SimpleCard card = new SimpleCard();
		card.setTitle(APP_NAME);
		card.setContent(LAUNCH_MESSAGE);

		// Create the plain text output.
		PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText(LAUNCH_MESSAGE);

		// Create reprompt
		Reprompt reprompt = new Reprompt();
		reprompt.setOutputSpeech(speech);

		return SpeechletResponse.newAskResponse(speech, reprompt, card);
	}

	private SpeechletResponse getEmptyNameResponse() {
		// Create the plain text output.
		PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText(EMPTY_NAME_RESPONSE);

		// Create reprompt
		Reprompt reprompt = new Reprompt();
		reprompt.setOutputSpeech(speech);
		return SpeechletResponse.newAskResponse(speech, reprompt);
	}
	
	
	private SpeechletResponse getErrorResponse() {
		// Create the Simple card content.
		SimpleCard card = new SimpleCard();
		card.setTitle(APP_NAME);
		card.setContent(ERROR_TEXT);

		// Create the plain text output.
		PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText(ERROR_TEXT);
		return SpeechletResponse.newTellResponse(speech, card);
	}
	
	private SpeechletResponse getExceptionResponse(TwitterUserNotFoundException t) {
		PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText(String.format(TWITTER_USER_NOT_FOUND_TEXT, t.getName()));
		return SpeechletResponse.newTellResponse(speech);
	}
	
	private SpeechletResponse getHelpResponse() {
		PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText(HELP_TEXT);
		Reprompt reprompt = new Reprompt();
		reprompt.setOutputSpeech(speech);
		return SpeechletResponse.newAskResponse(speech, reprompt);
	}
	
	
}
