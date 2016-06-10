def s = params.get("s")
def networkType = params.get("networkType")

System.out.println "***networkType is $networkType s = $s"

def gwl = new GenerateWordLinks();

def nt
if (networkType == "forceNet") nt = "graph" else nt = "tree"

def json = gwl.getJSONnetwork(s, nt)
print json