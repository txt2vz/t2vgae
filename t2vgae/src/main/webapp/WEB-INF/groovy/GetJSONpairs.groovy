import PorterStemmer;
import StopSet;
import groovy.json.*
import groovy.transform.*

class GetJSONpairs {

	def highFreqWords = 50
	def maxWordPairs = 40

	String getWordPairs(String s, int hfq, int mwp){
		this.highFreqWords=hfq
		this.maxWordPairs=mwp
		getWordPairs(s)
	}

	String getWordPairs(String s, int mwp){
		this.maxWordPairs=mwp
		getWordPairs(s)
	}
	String getWordPairs(String s) {
		s = s ?: "empty text"

		def words = s.replaceAll(/\W/, "  ").toLowerCase().tokenize().minus(StopSet.stopSet)
		// smallStopSet2);//  stopSet)

       println "---------------------------******************************"

		println " words size: " + words.size() + " unique words " + words.unique(false).size()

		def stemmer = new PorterStemmer()
		def steminfo = [:] //stemmed word is key and value is a map of a particular word form to its frequency
		def wordToPositionsMap = [:] //stemmed word is key and value is a list of postions where any of the words occur

		words.findAll { it.size() > 2 }
				.eachWithIndex { it, indexWordPosition ->

			def stemmedWord = stemmer.stem(it)
			wordToPositionsMap[stemmedWord] = wordToPositionsMap.get(stemmedWord, []) << indexWordPosition

			def forms = [:]
			forms = steminfo.get((stemmedWord), [(it): 0])

			def n = forms.get((it)) ?: 0
			forms.put((it), n + 1)

			steminfo[(stemmedWord)] = forms
		}

		println "take 2 wordTopositionsMap: " + wordToPositionsMap.take(2)
		println "take 2 steminfo: " + steminfo.take(2)

		//sort by size of list (word frequency)
		wordToPositionsMap = wordToPositionsMap.sort { -it.value.size() }

		//wordToFormsMap = wordToFormsMap.drop(wordToFormsMap.size() - highFreqWords)
		wordToPositionsMap = wordToPositionsMap.take(highFreqWords)

		println "after; take wortopositmap.size " + wordToPositionsMap.size()

		def wordPairList = []

		def n = 1;
		wordToPositionsMap.each { a ->
			wordToPositionsMap.drop(n).each { b ->
				def w0 = a.getKey()
				def w1 = b.getKey()
				def coocValue = getCooc(a.getValue(), b.getValue())
			//	def wFreq = wordToPositionsMap.get(w0).size + wordToPositionsMap.get(w1).size - 2
				//  '' def srtVal = coocValue//wFreq + coocValue
				wordPairList << new WordPair(word0: w0, word1: w1, cooc: coocValue)// , wordFreq: wFreq)//, sortValue: srtVal)
			}
			n++
		}

		//println "count hos " + wordToPositionsMap.get("hous").size
		wordPairList = wordPairList.sort { -it.cooc }
		println "wordPairList take 5: " + wordPairList.take(5)

		wordPairList = wordPairList.take(maxWordPairs)

		def data = [

				links: wordPairList.collect {

					def src = steminfo[it.word0].max { it.value }.key
					def tgt = steminfo[it.word1].max { it.value }.key

					[source: src,
					 target: tgt,
					 cooc  : it.cooc,
					]
				}
		]
		//println " data " + data
		def json = new JsonBuilder(data)
		//println "json: " + json
		return json
	}

	//powers for 0.9
	def powers = [
			0 : 1,
			1 : 0.9,
			2 : 0.81,
			3 : 0.729,
			4 : 0.6561,
			5 : 0.59049,
			6 : 0.531441,
			7 : 0.4782969,
			8 : 0.43046721,
			9 : 0.387420489,
			10: 0.34867844,
			11: 0.313810596,
			12: 0.282429536,
			13: 0.254186583,
			14: 0.228767925
	]

	def getCooc(List w0Positions, List w1Positions) {
		final int MAX_DISTANCE = 10;
		def coocVal =
				[w0Positions, w1Positions].combinations().collect
				{ a, b -> Math.abs(a - b) - 1 }
						.findAll { it <= MAX_DISTANCE }
						.sum {
					powers[it] //    Math.pow(0.9, it)
				}

		return coocVal ?: 0.0;
	}

	static main(args) {
		def y = new GetJSONpairs()
		y.getWordPairs("""houses tonight  houses tonight content contents contents housed house houses housed zoo zoo2""")
		//println y.getWordPairs("abc def")
	}
}