import org.jsoup.Jsoup;

String strURL = params.get("u");
//System.out.println("in urlservet u: " +strURL);
//print "in url script"
def url = strURL.toURL()
GetJSONpairs gt = new GetJSONpairs();

int ml = params.get("maxLinks").toInteger()
//String textOnly = Jsoup.parse(url.getText()).text();
print gt.getWordPairs(Jsoup.parse(url.getText()).text(), ml);
