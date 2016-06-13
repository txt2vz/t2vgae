
import PorterStemmer;
import java.util.Map

import groovy.json.*
import groovy.transform.*
import StopSet;

class GenerateWordLinks {

	def highFreqWords = 100
	def maxWordPairs = 80
    def coocIn = 0.5
	String networkType = "tree"

//	String getJSONnetwork(String s, int hfq, int mwp){
//		this.highFreqWords=hfq
//		this.maxWordPairs=mwp
//		getJSONnetwork(s)
//	}
//
//	String getJSONnetwork(String s, int mwp){
//		this.maxWordPairs=mwp
//		getJSONnetwork(s)
//	}

	String  getJSONnetwork(String s, String netType, Float cin, int maxL) {
		networkType = netType
		this.coocIn = cin
		this.maxWordPairs=maxL
		
		println "in CONSTRUCTOORRR cocoIn  $coocIn maxWordPairs $maxWordPairs"
		getJSONnetwork (s)
		
	}

	String getJSONnetwork(String s) {
		println "coocIn 22222:  $coocIn"

		//new File ('athenaBookChapter.txt').text
		s = s ?: "empty text"

		def words = s.replaceAll(/\W/, "  ").toLowerCase().tokenize().minus(StopSet.stopSet)
		// smallStopSet2);//  stopSet)

		println "---------------------------******************************"
		println " words size: " + words.size() + " unique words " + words.unique(false).size()

		def stemmer = new PorterStemmer()
		def stemInfo = [:] //stemmed word is key and value is a map of a particular word form and its frequency
		def wordToPositionsMap = [:] //stemmed word is key and value is a list of positions where any of the words occur

		words.findAll { it.size() > 1 }
		.eachWithIndex { it, indexWordPosition ->

			def stemmedWord = stemmer.stem(it)
			wordToPositionsMap[stemmedWord] = wordToPositionsMap.get(stemmedWord, []) << indexWordPosition

			def forms = [:]
			forms = stemInfo.get((stemmedWord), [(it): 0])

			def n = forms.get((it)) ?: 0
			forms.put((it), n + 1)

			stemInfo[(stemmedWord)] = forms
		}

		println "take 2 steminfo: " + stemInfo.take(2)

		//sort by size of list (word frequency)
		wordToPositionsMap = wordToPositionsMap.sort { -it.value.size() }

		//wordToFormsMap = wordToFormsMap.drop(wordToFormsMap.size() - highFreqWords)
		wordToPositionsMap = wordToPositionsMap.take(highFreqWords)

		println "after take wordposmap $wordToPositionsMap  wortopositmap.size " + wordToPositionsMap.size()

		def wordPairList = []

		wordToPositionsMap.eachWithIndex { a, index ->
			wordToPositionsMap.drop(index + 1).each { b ->
				def w0 = a.getKey()
				def w1 = b.getKey()
				def coocValue = getCooc(a.getValue(), b.getValue())

				wordPairList << new WordPair(word0: w0, word1: w1, cooc: coocValue)
			}
		}

		wordPairList = wordPairList.sort { -it.cooc }
		println "wordPairList take 5: " + wordPairList.take(5)

		wordPairList = wordPairList.take(maxWordPairs)
		//def json = getJSONgraph(wordPairList, stemInfo)
		def json;
		
		if (networkType == "tree")
			json = getJSONtree(wordPairList, stemInfo)
		else
			json= getJSONgraph(wordPairList, stemInfo)
			
		println "json is $json"
		return json
	}

	private String getJSONgraph( List wl, Map stemMap){

		def data = [

			links: wl.collect {

				def src = stemMap[it.word0].max { it.value }.key				
				def tgt = stemMap[it.word1].max { it.value }.key

				[source: src,
					target: tgt,
					cooc  : it.cooc,
				]
			}
		]

		def json = new JsonBuilder(data)
		return json
	}

	private String getJSONtree( List wl, Map stemMap){
		def tree= [:]

		wl.collect {
			def src =    stemMap[it.word0].max { it.value }.key
			def target = stemMap[it.word1].max { it.value }.key

			if (tree.isEmpty()){
				tree <<
						[name: src,
							children: [[name: target]]]
				//println "tree at start $tree"
			}
			else {
				addPairToMap(tree, src, target)
			}
		}
		def json = new JsonBuilder(tree)
		return json
	}

	def addedNodes = [] as Set
	def addedChildren = [] as Set

	private void addPairToMap (Map m, String source, String target){

		assert source !=target

		m.each {

			if (it.value in List ){
				it.value.each{
					assert it in Map

					addPairToMap(it, source, target)
				}
			}else{

				if (it.value == target){
					//println "skipping it.value ${it.value} source $source target $target"
				} else {

					if (it.value ==source) {

						if (m.children  && ! addedNodes.contains(target)){
							if (addedChildren.add(target)){
								m.children << ["name": target]
							}
						}else{
							//do not create a new node if one already exists
							if (addedNodes.add( it.value) && ! addedNodes.contains(target) && ! addedChildren.contains(target)  ){

							//	println "adding " + it.value + " to added $addedNodes"
								addedChildren.add(target)

								m  << ["name": it.value, "children": [["name" : target]]]
							}
							else {
								//println "already added so skipping source $source target $target it.value " + it.value
							}
						}
					}
				}
			}
		}
	}


	//powers for 0.9
	def final powers = [
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

	private def getCooc(List w0Positions, List w1Positions) {
		final int MAX_DISTANCE = 10;
		def coocVal =
				[w0Positions, w1Positions].combinations().collect
				{ a, b -> Math.abs(a - b) - 1 }
				.findAll { it <= MAX_DISTANCE }
				.sum {
					//powers[it]
					Math.pow(0.5, it)
				}

		return coocVal ?: 0.0;
	}

	static main(args) {
		def gwl = new GenerateWordLinks()
		//y.getWordPairs("""houses tonight  houses tonight content contents contents housed house houses housed zoo zoo2""")
		//println y.getWordPairs("abc def")
		gwl.getJSONnetwork(mAli)
	}

	def final static mAli =
			'''
“I am America. I am the part you won’t recognise. But get used to me – black, confident, cocky; my name, not yours; my religion, not yours; my goals, my own. Get used to me.”
Muhammad Ali: the man behind the icon Read moreMuhammad Ali loved the sound of his own voice, and so did everyone else. His words were predictably impossible to top on Saturday, as America mourned the loss of a colossus not only in the boxing ring but the arenas of politics, religion and popular culture.
Born in the south before Rosa Parks refused to give up her seat for a white bus passenger, he died at the age of 74, having seen the first African American elected to the White House. Barack Obama led tributes to the incandescent athlete, activist, humanitarian, poet and showman with a statement that caught the mood of many.
It said: “Muhammad Ali was the Greatest. Period. If you just asked him, he’d tell you. He’d tell you he was the double greatest; that he’d ‘handcuffed lightning, thrown thunder into jail’. But what made the Champ the greatest – what truly separated him from everyone else – is that everyone else would tell you pretty much the same thing.”
The president continued: “Like everyone else on the planet, Michelle and I mourn his passing. But we’re also grateful to God for how fortunate we are to have known him, if just for a while; for how fortunate we all are that the Greatest chose to grace our time.”
Muhammad Ali shook up the world. And the world is better for it. We are all better for it
Barack Obama Obama said he kept a pair of Ali’s gloves on display in his private study, under a celebrated photo of him, aged 23, “roaring like a lion” over a fallen Sonny Liston. His name was “as familiar to the downtrodden in the slums of south-east Asia and the villages of Africa as it was to cheering crowds in Madison Square Garden”, the president said.
“Muhammad Ali shook up the world. And the world is better for it. We are all better for it.”
The three-time world heavyweight champion died late on Friday evening, a day after he was admitted to a Phoenix-area hospital with a respiratory ailment. His family were gathered around him.
Ali had long battled Parkinson’s disease, which impaired his speech and made the irrepressible athlete – known for saying he could float like a butterfly and sting like a bee – something of a prisoner in his own body.
On Saturday, family spokesman Bob Gunnell said Ali died from septic shock due to unspecified natural causes. He did not suffer, Gunnell said. A White House statement said Obama had called Lonnie Ali, the champion’s fourth wife, “to offer his family’s deepest condolences for the passing of her husband”.
View image on Twitter
'''
}