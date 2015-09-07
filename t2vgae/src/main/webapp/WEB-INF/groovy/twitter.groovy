import groovy.json.*
import twitter4j.*
import twitter4j.conf.*;

//println "twitter.groovy: params distance: " + params.get("distance") + " query is " +params.get("query")
def q = params.get("q")
System.out.println("query is " + q);

//def distance = params.get("distance")
//if (!distance.isNumber()) distance=200
//distance = distance.toInteger()
def distance=20000;

Twitter twitter = getTwitterAuth();
Query query = new Query(q);
query.setCount(40)
GeoLocation sheffield = new GeoLocation(53.377127, -1.467705);
//query.setGeoCode(sheffield, distance, Query.KILOMETERS)
QueryResult result = twitter.search(query);

def twCount =0;
//def builder = new groovy.xml.MarkupBuilder(out)

List<Status> tweets = []//for (x=0;x<5;x++) {
for (;;) {
	result = twitter.search(query);
	tweets.addAll(result.getTweets())
	query = result.nextQuery()
	if (query == null) break;
}

//println "tweets size " + tweets.size()
def txtout

def columns, p = new StringBuffer()
columns =  "user, lat, lng, tweet \n"
//def txtout;
tweets.each {
	//def geo = it.getGeoLocation();

	//if (geo!=null){
	//	def uname = it.getUser().getScreenName()
	//		def lat = it.getGeoLocation().getLatitude()
	//	def lng = it.getGeoLocation().getLongitude()
	def txt =  it.getText()
	txtout  = txtout + txt;//          << txt ?: " "
	//	def plc = it.getPlace()?.getFullName()

	//	txtout=	 txt.replaceAll('[^A-Za-z0-9 %@#$£{}+*\'-_]', " ");
	//	txtout = txtout.replaceAll(",", "");
	//p  << uname + "," + lat + "," + lng + "," + txtout + "\n"
}
//}
//out.println(columns + p)

GgetJSONpairs gt = new GgetJSONpairs();

//println"+++++++++++++++++++++++++++++++++++++++++++++++++++++"
String json = gt.getWordPairs(txtout);  //s2.toString());

//System.out.println(set.toString());
//response.getWriter().println( set);
//print(set)
//println("json is $json")
print json
//def output= columns + p
//println output


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