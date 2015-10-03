import twitter4j.*
import twitter4j.conf.*;

//println "twitter.groovy: params distance: " + params.get("distance") + " query is " +params.get("query")
def q = params.get("q")
int ml = params.get("maxLinks").toInteger()

Twitter twitter = getTwitterAuth();
Query query = new Query(q);
query.setCount(40)
//GeoLocation sheffield = new GeoLocation(53.377127, -1.467705);
//query.setGeoCode(sheffield, distance, Query.KILOMETERS)
QueryResult result = twitter.search(query);

def twCount =0;
List<Status> tweets = []
for   (i in 0..4){//(;;) {
	result = twitter.search(query);
	tweets.addAll(result.getTweets())
	query = result.nextQuery()
	if (query == null) break;
}

def txtout
tweets.each {
	txtout  = txtout + it.getText()
}

def gjp = new GetJSONpairs();
String json = gjp.getWordPairs(txtout, ml);
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