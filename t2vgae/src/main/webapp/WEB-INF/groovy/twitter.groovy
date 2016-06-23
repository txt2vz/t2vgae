import twitter4j.*
import twitter4j.conf.*;

//println "twitter.groovy: params distance: " + params.get("distance") + " query is " +params.get("query")
def q = params.get("q")
int ml = params.get("maxLinks").toInteger()

Twitter twitter = getTwitterAuth();
Query query = new Query(q + "-filter:retweets");
query.setCount(40)
//GeoLocation sheffield = new GeoLocation(53.377127, -1.467705);
//query.setGeoCode(sheffield, distance, Query.KILOMETERS)
QueryResult result = twitter.search(query);

def twCount =0;
def txtout

for   (i in 0..4){//(;;) {
	result = twitter.search(query);
	def tweets = result.getTweets()
	
	tweets.each {
		twCount++
		txtout  = txtout + it.getText()
	}	
	
	query = result.nextQuery()
	if (query == null) break;
}
System.out.println( "twCount : $twCount" )

def gjp = new GetJSONpairs();
String json = gjp.getWordPairs(txtout, ml);
print json

private Twitter getTwitterAuth(){

	ConfigurationBuilder cb = new ConfigurationBuilder();
	cb.setDebugEnabled(true)
//**keys
	TwitterFactory tf = new TwitterFactory(cb.build());
	Twitter twitter = tf.getInstance();

	return tf.getInstance();
}