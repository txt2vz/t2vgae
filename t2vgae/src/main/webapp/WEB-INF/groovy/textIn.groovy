import org.jsoup.Jsoup
import twitter4j.*
import twitter4j.conf.*;

import twitter4j.Twitter;;

String text = params.get("s")

System.out.println  "zzzzzzzzzzzzzz in twitter text $text"

//twitter query
if ( params.get("tw")){
	System.out.println  "FOund tw " +  params.get("tw")
	Twitter twitter = getTwitterAuth();
	Query query = new Query(text + "-filter:retweets");
	query.setCount(40)
	//GeoLocation sheffield = new GeoLocation(53.377127, -1.467705);
	//query.setGeoCode(sheffield, distance, Query.KILOMETERS)
	QueryResult result = twitter.search(query);

	def twCount =0;
	def twitterText= ""

	for   (i in 0..4){//(;;) {
		result = twitter.search(query);
		def tweets = result.getTweets()

		tweets.each {
			twCount++
			twitterText = twitterText + it.getText()
		}

		query = result.nextQuery()
		if (query == null) break;
	}
	System.out.println( "twCount : $twCount" )
	text = twitterText
} else 

// should use apache commons URLValidator
if (text.startsWith("http") && text.size() < 256) {
	def url = text.toURL()
	text = Jsoup.parse(url.getText()).text()
}

def networkType = params.get("networkType")
float cooc = params.get("cooc").toFloat()
int maxLinks = params.get("maxLinks").toInteger()
System.out.println "***TEXTIN: networkType is $networkType cooc: $cooc maxLinks $maxLinks"

def gwl = new GenerateWordLinks()

def nt = (networkType == "forceNet") ? "graph" : "tree"


def	json = gwl.getJSONnetwork(text, nt, cooc, maxLinks)

print json

private Twitter getTwitterAuth(){

	ConfigurationBuilder cb = new ConfigurationBuilder();
	cb.setDebugEnabled(true)
			.setOAuthConsumerKey("u59ay8TtPUn5p9VTHxdFg")
			.setOAuthConsumerSecret("LOkS2Vl9KTWXH5VMuwhb9RfIcXBXzkvyzzwD0HQtr14")
			.setOAuthAccessToken("560297710-4UmsMLOILUgIkLx6V5mdH1lbvG8ew8xvQm5YgBhY")
			.setOAuthAccessTokenSecret("mlwDtpbx9bUKTTk4wpBYVdUagGmBX6bzAYJbktoNM");
	TwitterFactory tf = new TwitterFactory(cb.build());
	Twitter twitter = tf.getInstance();

	return tf.getInstance();
}