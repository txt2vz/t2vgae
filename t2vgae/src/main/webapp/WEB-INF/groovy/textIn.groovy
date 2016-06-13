def text = params.get("s")
def networkType = params.get("networkType")

float cooc = params.get("cooc").toFloat()
int maxLinks = params.get("maxLinks").toInteger()
System.out.println "***TEXTIN: networkType is $networkType text = $text cooc: $cooc maxLinks $maxLinks"

def gwl = new GenerateWordLinks();

def nt
if (networkType == "forceNet") nt = "graph" else nt = "tree"

def json = gwl.getJSONnetwork(text, nt, cooc, maxLinks)
print json