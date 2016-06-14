<!DOCTYPE html>
<meta charset="utf-8">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js">
<link
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css"
	rel="stylesheet">


<style type="text/css">
.node {
	cursor: pointer;
}

.overlay {
	background-color: #FFF;
}

.node circle {
	fill: blueviolet;
	stroke: steelblue;
	stroke-width: 1.5px;
}

.node text {
	font-size: 14px;
	font-family: sans-serif;
}

.link {
	fill: none;
	stroke: #ccc;
	stroke-width: 1.5px;
}

line.link {
	fill: none;
	stroke: #9ecae1;
	stroke-width: 1.5px;
}

.controls {
	font-size: 70%;
}

hr {
	background-color: blue;
	height: 1px;
	border: 0;
}

.well {
	background-color: #e6fff2;
}
</style>

<body>
	<div class="container  col-md-12">
		<h1>txt2vz Tree</h1>
		<p>Select fields, choose a valid text file and upload/draw:</p>
		<div class="well col-md-12">
			<div class="row">

				<div class="col-md-6">

					<p>Paste in text</p>
					<textarea id="txta1" COLS="50" ROWS="5"
						placeholder="Type or paste text here"> </textarea>
					<input type="button" value="viz text" onclick="pasted(txta1.value)">

					<br></br>
				</div>

				<div class="col-md-3">
					Show Tree:
					<form id="expanded">
						<input type="radio" id="full" name="type" value="full">
						Fully Expanded <br /> <input type="radio" id="oneLevel"
							name="type" value="oneLevel" checked="true"> One Level
					</form>
					<br> Network Type:
					<form id="networkType">
						<input type="radio" id="dendro" name="nt" value="dendro">
						Dendrogram <br /> <input type="radio" id="forceTree" name="nt"
							value="forceTree" checked="true"> Force (Tree) <br /> <input
							type="radio" id="forceNet" name="nt" value="forceNet">
						Force (Network)
					</form>
				</div>

				<div class="col-md-2 controls">

					<label for="maxLinks">Set Max Links to Show</label> <input
						type="range" min="1" max="200" value="30" id="maxLinks" step="1"
						oninput="outputMaxLinks(value)">

					<output for="maxLinks" id="maxLinks2">30</output>

					<hr />
					<label for="maxWords">Set Max Words to analyse</label> <input
						type="range" min="1" max="200" value="30" id="maxWords" step="1"
						oninput="outputMaxWords(value)">

					<output for="maxWords" id="maxWords2">30</output>

					<hr />
					<label for="cooc">Set Power Value for Cooccurence</label> <input
						type="range" min="0.3" max="0.9" value="0.5" id="cooc" step="0.2"
						list="steplist" oninput="outputCooc(value)">
					<datalist id="steplist">
						<option>0.3</option>
						<option>0.5</option>
						<option>0.7</option>
						<option>0.9</option>
					</datalist>
					<output for="cooc" id="cooc2">0.5</output>

				</div>

			</div>
		</div>
		<div class="row col-md-12">
			<div id="tree-container"></div>
			<div class="main"></div>
		</div>
	</div>

	<script src="https://code.jquery.com/jquery-2.1.4.min.js"></script>
	<script src="http://d3js.org/d3.v3.min.js"></script>
	<script src="js/drawForce.js"></script>
	<script src="js/drawDendrogram.js"></script>
	<script src="js/drawLinks.js"></script>
	<script src="js/colorbrewer.js"></script>
	<script src="js/frontPage.js"></script>

	<script>
		var cooc = 0.5;
		var maxLinks = 30;
		var maxWords = 50;
		var oneLevel = true;
		var networkType = "forceTree";
		var jsonDataTemp;

		frontPage();

		function outputCooc(coocIn) {
			document.querySelector('#cooc2').value = coocIn;
			cooc = document.getElementById("cooc").value;
			console.log("cooc set: " + cooc);
		}

		function outputMaxLinks(maxLinksIn) {
			document.querySelector('#maxLinks2').value = maxLinksIn;
			maxLinks = document.getElementById("maxLinks").value;
			console.log("maxLinks set: " + maxLinks);
		}

		function outputMaxWords(maxWordsIn) {
			document.querySelector('#maxWords2').value = maxWordsIn;
			maxLinks = document.getElementById("maxWords").value;
			console.log("maxWords set: " + maxWords);
		};
	
		// can't swap because JSON is different
	//	jQuery(document).ready(function () {
	//		jQuery('input[type=radio][name=nt]').change (function() {
	//			if (jsonDataTemp) {
	//				console.log ("change network type");
	//				setNetworkType();
	//				draw(jsonDataTemp);
	//			}				
	//		});
	//	});
				
		
		
	</script>

	<script>
		function draw(jsonData) {
			jsonDataTemp = jsonData;

			console.log("data back from draw " + jsonData);
			oneLevel = jQuery('input[name=type]:checked', '#expanded').val() == "oneLevel";
		

			if (networkType == "forceNet")
				drawLinks(jsonData);

			else if (networkType == "forceTree")
				drawForce(jsonData);
			else
				drawDendrogram(jsonData)
		};

		function setNetworkType() {
			networkType = jQuery('input[name=nt]:checked', '#networkType')
					.val();
		};

		function pasted(s) {
			s = s.replace(/%/g, '');
			setNetworkType();
			
			jQuery.ajax({
				type : "POST",
				url : "textIn.groovy",
				cache : false,
				data : {
					s : s,
					networkType : networkType,
					cooc : cooc,
					maxLinks : maxLinks,
					maxWords : maxWords
				},
				success : function(jsonData, textStatus, jqXHR) {

					console.log("jsonData back from draw " + jsonData);
					draw(jsonData);
				},

			});
		}
	</script>
</body>
</html>