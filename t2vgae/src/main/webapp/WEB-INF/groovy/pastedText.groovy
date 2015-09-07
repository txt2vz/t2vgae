
//String s = request.getParameter("s");
def s = params.get("s")
//System.out.println(" s in groovy " + s);

GgetJSONpairs gt = new GgetJSONpairs();
println  gt.getWordPairs(s);
//System.out.println(set.toString());

//response. getWriter().println( set);
//out.print(set)
