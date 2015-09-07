
get "/", forward: "/WEB-INF/pages/twit.gtpl"
get "/datetime", forward: "/datetime.groovy"
get "/twitter", forward: "/WEB-INF/groovy/twitter.groovy"

get "/favicon.ico", redirect: "/images/gaelyk-small-favicon.png"
