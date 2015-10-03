
//String s = request.getParameter("s");
def s = params.get("s")
//System.out.println(" s in groovy " + s);
int ml = params.get("maxLinks").toInteger()

GetJSONpairs gt = new GetJSONpairs();
println  gt.getWordPairs(s, ml);
//System.out.println(set.toString());

//response. getWriter().println( set);
//out.print(set)
