import groovy.json.JsonBuilder

class GgetJSONpairs {	

	String getWordPairs (String s){
		//def file = new File("C:\\Users\\laurie\\Documents\\cloud.txt" )
		def final highFreqWords= 40
		def final maxWordPairs = 20

		//def words = s.replaceAll(/\W/, "  ").toLowerCase().tokenize().minus(StopSet.smallStopSet)
		s = s ?: "empty text"
		
		def s3 = new StopSet();

		def words = s.replaceAll(/\W/, "  ").toLowerCase().tokenize().minus(StopSet.stopSet)  // smallStopSet2);//  stopSet)

		def wordMap = [:]
		def outMap = [:]

		//println " words. size " + words.size() + " unique words " + words.unique(false).size()

		def stemmer = new PorterStemmer()
		def steminfo = [:]

		words.findAll{it.size()>2}
		.eachWithIndex {it, i ->
			//if (it.size()>2){
			def st = stemmer.stem(it)
			wordMap[st] = wordMap.get(st,[]) << i

			def forms=[:]
			forms = steminfo.get((st), [(it):0])

			def n = forms.get((it)) ?: 0
			forms.put((it),n +1)

			steminfo[(st) ] =forms
			//}
		}

		wordMap = wordMap.sort{it.value.size()}

		println "wordMapzzz after sort $wordMap"
		wordMap = wordMap.drop(wordMap.size() - highFreqWords)
		//wordMap = wordMap.take(wordPairs)
		//	println " wordmap  size after take " +wordMap//+ " " + wordMap

		def n=1;
		for (e in wordMap){
			def z=wordMap.drop(n)
			for (f in z){
				def t = getCooc2(e.getValue(), f.getValue())
				String q=e.getKey() +"#" + f.getKey()
				outMap.put(q, t)
			}
			n++
		}

		outMap =outMap.sort{-it.value}
		//outMap =outMap.sort{a,b -> b.value <=> a.value}

		//	println "outmap before drop $outMap"
		//	outMap = outMap.drop(outMap.size() - maxWordPairs)
		outMap = outMap.take( maxWordPairs)

		//println "outmap size " + outMap.size()
		def wlinks=outMap.keySet()

		int z=0;
		def data = [

			links: wlinks.collect {

				def a= it.split("#")
				def src = steminfo[a[0]].max{it.value}.key
				def tgt = steminfo[a[1]].max{it.value}.key

				[source: src,
					target:  tgt,
					rank: z++]
			}
		]
		print " data " +data
		def json = new JsonBuilder(data)
	}

	def getCooc2(List w0Positions, List w1Positions) {
		final int MAX_DISTANCE = 14;
		def y =
				[w0Positions, w1Positions].combinations().collect
				{ a, b -> Math.abs( a - b ) - 1 }
				.findAll { it <= MAX_DISTANCE }
				.sum { Math.pow( 0.8, it ) }

		return y ?:0.0;
	}


	private double getCooc(List w0, List w1) {
		//lookup table of 0.8
		def powers = [
			0:1,
			1:0.8,
			2:0.74,
			3:0.64,
			4:0.51,
			5:0.33,
			6:0.26,
			7:0.21,
			8:0.17,
			9:0.13,
			10:0.11,
			11:0.09,
			12:0.07,
			13:0.05,
			14:0.04,
			15:0.03
		]

		final int MAX_DISTANCE = 14;
		double cooccur = 0.0d;
		for (p in w0){
			for (q in w1){
				final int pqDistance = Math.abs(p - q)-1;
				if (pqDistance <= MAX_DISTANCE)
				//cooccur = cooccur + Math.pow(0.8, pqDistance);
				cooccur =cooccur + powers[pqDistance];
			}
		}
		return cooccur;
	}

	static main (args)
	{
		def y = new GgetJSONpairs()
		y.getWordPairs ("""om tat sat houses tonight  houses tonight content contents contents housed house houses housed zoo""")
		//y.getWordPairs ("""om tat sat om house houses houses house one two three four""")
		def x
	}
}