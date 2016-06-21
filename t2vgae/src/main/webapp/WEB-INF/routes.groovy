get "/", forward: "/WEB-INF/pages/index.gtpl"

get "/t2v", forward: "/WEB-INF/pages/t2v.gtpl"
get "/t2v2", forward: "/WEB-INF/pages/t2v2.gtpl"
get "/t2v3", forward: "/WEB-INF/pages/t2v3.gtpl"
get "/t2v4", forward: "/WEB-INF/pages/t2v4.gtpl"
//get "/datetime", forward: "/datetime.groovy"
get "twitter", forward: "/WEB-INF/groovy/twitter.groovy"

get "pastedText", forward: "/WEB-INF/groovy/pastedText.groovy"
get "textIn", forward: "/WEB-INF/groovy/textIn.groovy"
get "url", forward: "/WEB-INF/groovy/url.groovy"

//get "/favicon.ico", redirect: "/images/gaelyk-small-favicon.png"
