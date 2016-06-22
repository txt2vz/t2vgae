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

output {
	font-size: 90%;
	display: inline;
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
<title>txt2vz</title>

<body>
	<div class="container  col-md-12">
		<a
			href="https://plus.google.com/u/0/b/117816592489907408686/117816592489907408686/about"
			target="_blank"> <em style="font-size: 28px"><b> <span
					style="color: blueviolet">t</span><span style="color: cyan">xt</span><span
					style="color: magenta">2</span><span style="color: cyan">vz</span></b></em>
		</a>
		<div class="well col-md-12">
			<div class="row">
				<div class="col-md-4">

					<textarea id="txta1" COLS="40" ROWS="6"
						placeholder="Type or paste text here"></textarea>
					<br> <input type="button" value="viz text"
						onclick="textIn(txta1.value)">
				</div>
				<div class="col-md-4">
					Enter Twitter query or hashtag: <input type="text" name="twitQ"
						id="twitQ" size=40 value="word cloud"> <input
						type="button" value="viz Twitter"
						onclick="textIn(twitQ.value, true )"> <br /> <br />
					Enter URL: <input type="text" id="url"
						value="https://en.wikipedia.org/wiki/Tag_cloud" size=40>

					<button onclick="textIn(url.value)">viz URL</button>
					<br />

					<div id="modal">
						<img id="loader" src="images/ajax-loaderBig.gif" />
						<div id="fade"></div>
					</div>
				</div>

				<div class="col-md-2">
							
					<br> Network Type:
					<form id="networkType">
						<input type="radio" id="radial" name="nt" value="radial"
							checked="true"> Radial (tree)<br /> <input type="radio"
							id="forceTree" name="nt" value="forceTree"> Force (tree)
						<br /> <input type="radio" id="forceNet" name="nt"
							value="forceNet"> Force (network) <br />
						<input type="radio" id="dendro" name="nt" value="dendro">
						Dendrogram (tree)<br />

					</form>
				</div>

				<div class="col-md-2 controls">
					<label for="maxWords">Max Words</label>
					<output for="maxWords" id="maxWords2">100</output>
					<input type="range" min="1" max="1000" value="50" id="maxWords"
						step="1" oninput="outputMaxWords(value)"> <br> <br>

					<label for="maxLinks">Max Word Pairs: </label>
					<output for="maxLinks" id="maxLinks2">100</output>
					<input type="range" min="1" max="1000" value="30" id="maxLinks"
						step="1" oninput="outputMaxLinks(value)"> <br> <br>

					<label for="cooc">Power Value: </label>
					<output for="cooc" id="cooc2">0.5</output>
					<input type="range" min="0.05" max="0.95" value="0.5" id="cooc"
						step="0.05" oninput="outputCooc(value)">
				</div>
			</div>
		</div>
		<div class="row col-md-12">

			<div id="tree-container"></div>
			<div class="main"></div>
		</div>

		<div class="navbar navbar-fixed-bottom"
			style="font-family: arial; font-size: 10px; background-color: white;">
			By Laurie Hirsch. Info: <a
				href="https://plus.google.com/u/0/b/117816592489907408686/117816592489907408686/about"
				target="_blank">txt2vz</a>
		</div>
	</div>

	<script src="https://code.jquery.com/jquery-2.1.4.min.js"></script>
	<script src="http://d3js.org/d3.v3.min.js"></script>
	<script src="js/drawForce.js"></script>
	<script src="js/drawDendrogram.js"></script>
	<script src="js/drawLinks.js"></script>
	<script src="js/drawRadial.js"></script>

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

		jQuery('input').click(function() {
			jQuery(this).select();
		});

		function openModal() {
			document.getElementById('modal').style.display = 'block';
			document.getElementById('fade').style.display = 'block';
		}

		function closeModal() {
			document.getElementById('modal').style.display = 'none';
			document.getElementById('fade').style.display = 'none';
		}

		function outputCooc(coocIn) {
			document.querySelector('#cooc2').value = coocIn;
			cooc = document.getElementById("cooc").value;
			console.log("cooc set: " + cooc);
		}

		function outputMaxLinks(maxLinksIn) {
			document.querySelector('#maxLinks2').value = maxLinksIn;
			maxLinks = document.getElementById("maxLinks").value;
			//	console.log("maxLinks set: " + maxLinks);
		}

		function outputMaxWords(maxWordsIn) {
			document.querySelector('#maxWords2').value = maxWordsIn;
			maxWords = document.getElementById("maxWords").value;
			//console.log("maxWords set: " + maxWords);
		};

		jQuery('#txta1').keypress(function(event) {
			var keycode = (event.keyCode ? event.keyCode : event.which);
			if (keycode == '13') {
				console.log('You pressed a "enter" key in textbox');
				textIn(txta1.value);
			}
			//Stop the event from propogation to other handlers
			//If this line will be removed, then keypress event handler attached
			//at document level will also be triggered
			event.stopPropagation();
		});

		//Bind keypress event to textbox
		jQuery('#twitQ').keypress(function(event) {
			var keycode = (event.keyCode ? event.keyCode : event.which);
			if (keycode == '13') {
				console.log('You pressed a "enter" key in Twitter Q');
				textIn(twitQ.value, true);
			}
			//Stop the event from propogation to other handlers
			//If this line will be removed, then keypress event handler attached
			//at document level will also be triggered
			event.stopPropagation();
		});

		//Bind keypress event to textbox
		jQuery('#url').keypress(function(event) {
			var keycode = (event.keyCode ? event.keyCode : event.which);
			if (keycode == '13') {
				//console.log('You pressed a "enter" key in url2');
				textIn(url.value);
			}
			//Stop the event from propogation to other handlers
			//If this line will be removed, then keypress event handler attached
			//at document level will also be triggered
			event.stopPropagation();
		});

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
			else if (networkType == "radial")
				drawRadial(jsonData)
			else
				drawDendrogram;
		};

		function setNetworkType() {
			networkType = jQuery('input[name=nt]:checked', '#networkType')
					.val();
		};

		function textIn(s, tw) {
			s = s.replace(/%/g, '');
			setNetworkType();
			openModal();

			jQuery.ajax({
				type : "POST",
				url : "textIn.groovy",
				cache : false,
				data : {
					s : s,
					tw : tw,
					networkType : networkType,
					cooc : cooc,
					maxLinks : maxLinks,
					maxWords : maxWords
				},
				success : function(jsonData, textStatus, jqXHR) {

					console.log("jsonData back from draw " + jsonData);
					draw(jsonData);
					closeModal();
				},

			});
		};

		closeModal();
	</script>
</body>
</html>