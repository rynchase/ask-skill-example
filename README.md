# ask-skill-example
This is a simple Alexa Skills Kit app written in Java that runs as an AWS Lambda function. The skill allows the user to ask for updates from Twitter on specific people as defined in [PeopleLookup.java](https://github.com/rynchase/ask-skill-example/blob/master/src/main/java/co/thehotnews/twitter/PeopleLookup.java)

##Set Up
A couple of things worth pointing out. If you want to run this for realz you will need to get set up with a Twitter dev account to get API access. Once you have access you will need update TheHotNewsSpeechlet with your access tokens.

	private static final String TWITTER_OAUTH_CONSUMER_KEY = "";
	private static final String TWITTER_OAUTH_CONSUMER_SECRET = "";
	private static final String TWITTER_OAUTH_ACCESS_TOKEN = "";
	private static final String TWITTER_OAUTH_ACCESS_TOKEN_SECRET = "";
You will also need to configure your ASK Skill via developer.amazon.com. Once you have the skill configure you will get an app id. You will need to set your skill id in TheHotNewsRequestStreamHandler:

	private static final String ASK_SKILL_ID = "amzn1.ask.skill.000000000000000";

As part of the set up of your Skill you will need to provide sample utternaces, an intent schema and some custom slot types. You can find examples of all of these in src/main/resources

## Building
Build: mvn assembly:assembly -DdescriptorId=jar-with-dependencies package

This generates a jar that can be uploaded to AWS Lambda.

It is worth reading through the developer docs from [Amazon](https://developer.amazon.com/alexa-skills-kit "Amazon").

