import org.jsoup.Jsoup;

String strURL = params.get("u");

System.out.println("in urlservet u: " +strURL);
def url = strURL.toURL()
String textOnly = Jsoup.parse(url.getText()).text();

GgetJSONpairs gt = new GgetJSONpairs();
print gt.getWordPairs(textOnly);
