get "/", forward: "/WEB-INF/pages/index.gtpl"

get "/t2v", forward: "/WEB-INF/pages/t2v.gtpl"
//get "/datetime", forward: "/datetime.groovy"
get "twitter", forward: "/WEB-INF/groovy/twitter.groovy"

get "pastedText", forward: "/WEB-INF/groovy/pastedText.groovy"
get "url", forward: "/WEB-INF/groovy/url.groovy"

//get "/favicon.ico", redirect: "/images/gaelyk-small-favicon.png"
