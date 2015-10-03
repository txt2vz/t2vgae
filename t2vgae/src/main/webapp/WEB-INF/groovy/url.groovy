import org.jsoup.Jsoup;

String strURL = params.get("u");

//System.out.println("in urlservet u: " +strURL);
//print "in url script"
def url = strURL.toURL()
int ml = params.get("maxLinks").toInteger()
String textOnly = Jsoup.parse(url.getText()).text();

GetJSONpairs gt = new GetJSONpairs();
print gt.getWordPairs(textOnly, ml);
