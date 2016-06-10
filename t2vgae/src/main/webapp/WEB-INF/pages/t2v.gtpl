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


				<div class="col-md-3">
					Show Tree:
					<form id="expanded">
						<input type="radio" id="full" name="type" value="full">
						Fully Expanded <br /> <input type="radio" id="oneLevel"
							name="type" value="oneLevel" checked="true"> One Level
					</form>
					<br> Tree Type:
					<form id="treeType">
						<input type="radio" id="dendro" name="tt" value="dendro">
						Dendrogram <br /> <input type="radio" id="force" name="tt"
							value="force" checked="true"> Force
					</form>

				</div>

				<div class="col-md-3">

					<p>Paste in text</p>

					<textarea id="txta1" COLS="30" ROWS="5"
						placeholder="Type or paste text here"> </textarea>
					<input type="button" value="viz text" onclick="pasted(txta1.value)">

					<br></br>
				</div>

			</div>
		</div>
		<div class="row col-md-12">
			<div id="tree-container"></div>
		</div>
	</div>

	<script src="https://code.jquery.com/jquery-2.1.4.min.js"></script>
	<script src="http://d3js.org/d3.v3.min.js"></script>
	<script src="js/drawForce.js"></script>
	<script src="js/drawDendrogram.js"></script>

	<script>
		function pasted(s) {
			s = s.replace(/%/g, '');

			jQuery
					.ajax({
						type : "POST",
						url : "textIn.groovy",
						cache : false,
						data : "s=" + s,
						success : function(data, textStatus, jqXHR) {
							//drawLinks(data);
							console.log("data back from draw " + data);

							var force = jQuery('input[name=tt]:checked', '#treeType')
									.val() == "force";
							console.log("force is " + force);

							//drawDendrogram(data);
							if (force)
								drawForce(data);
							else
								drawDendrogram(data);
						},

					});
		}
	</script>
</body>
</html>