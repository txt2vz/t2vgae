<!DOCTYPE html>
<html lang="en">

<head>
<!-- Bootstrap CSS Toolkit styles -->
<link rel="stylesheet" href="css/wait.css">

<link rel="stylesheet"
	href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css">
<!--  	href="http://blueimp.github.com/cdn/css/bootstrap.min.css"> -->

<!-- Bootstrap styles for responsive website layout, supporting different screen sizes -->
<link
	href="http://getbootstrap.com/2.3.2/assets/css/bootstrap-responsive.css"
	rel="stylesheet">

<meta http-equiv="Content-type" content="text/html; charset=utf-8">
<title>t2vd3</title>
<script src="//ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>

<script src="http://malsup.github.com/jquery.form.js"></script>
<script type="text/javascript"
	src="http://mbostock.github.com/d3/d3.js?2.9.2"></script>
	
	<script>
	function openModal() {
        document.getElementById('modal').style.display = 'block';
        document.getElementById('fade').style.display = 'block';
}

function closeModal() {
    document.getElementById('modal').style.display = 'none';
    document.getElementById('fade').style.display = 'none';
}

	</script>

<style type="text/css">
.link {
	stroke: #666; //
	stroke-width: 2px;
}
</style>
</head>
<body>
	<div class="container">
		<em style="font-size: 48px"><b> <span
				style="color: blueviolet">t</span><span style="color: cyan">xt</span><span
				style="color: magenta">2</span><span style="color: cyan">vz</span></b></em>:
		<span style="font-family: arial; font-size: 17px;  font-style: italic;">Visualize
			your text.</span>

		<div id='content' class='row-fluid'>
			<div class='span10 main active'>

				<script>
					function drawLinks(jsonlinks) {
						//alert("in drawlinks " + jsonlinks);
						d3.select("svg").remove();
						var linksobj;
						
						if (jsonlinks==null || typeof jsonlinks =='object'){
							linksobj = jsonlinks; 
						}
						else{ 
							console.log("in drawlk");
							linksobj = JSON.parse(jsonlinks);
						};

					//	var linksobj = jsonlinks; // JSON.parse(jsonlinks);
						var links = linksobj.links;

						var w = 960, h = 500;
						var nodes = {};

						var svg = d3.select(".main").append("svg").attr("width",
								w).attr("height", h);

						// Compute the distinct nodes from the links.
						links.forEach(function(link) {
							var sc = link.source;
							var tg = link.target;
							link.source = nodes[sc] || (nodes[sc] = {
								name : sc,
								rank : link.rank,
								numberLinks:0
							});
							link.target = nodes[tg] || (nodes[tg] = {
								name : tg,
								rank : link.rank,
								numberLinks:0
							});

							//count number of links for each node
							nodes[sc].numberLinks++;
							nodes[tg].numberLinks++;
							});

						var force = d3.layout.force().gravity(.05)
										.charge(-200).size([ w, h ]);

						
						force.nodes(d3.values(nodes)).links(links)
								.linkDistance(function(d) {
									if (d.rank<7) return 80;
									else if (d.rank <11) return 100;
									else return 170;
								}).start();

						var link = svg.selectAll(".link").data(links).enter()
								.append("line").attr("stroke-width",
										function(d) {
											if (d.rank <4)
												return 1;
											else
												return 0.1;
										}).attr("class", "link");

						var node = svg.selectAll(".node")
								.data(d3.values(nodes)).enter().append("g")
								.attr("class", "node").call(force.drag);
						
						node.append("rect").attr("width", function(d){
							return 120;
						}).attr("height" , function (d){
							return 20;
						}).attr("ry" , 8)
						.attr("rx" , 8)
						.attr("y", -15)
						.attr("x", -5)
						
	
						.attr("opacity", function (d) {
							if (d.rank < 1)
								return 1;
							else
								if (d.rank<3 ||d.numberLinks>2)
								return 0.7
								else return 0.4;
						})
						.attr("fill", function(d) {
					
					
				    	if (d.rank < 2)
							return "magenta";//"#66FFFF";							
						else
							if (d.numberLinks >2) 
								return     "blueviolet";//"blue"; 
				    	               // "blueviolet";
							else
							return  "cyan";//"yellowgreen"
					});

						node.append("text").attr("font-size", function(d) {
							if (d.rank < 1)		
								return 20;
							else
								if (d.rank < 4 ||d.numberLinks>2)
								return 14;
								else return 10;
						})
						//.attr("x", 1)
					   //  .attr("y", 15)
						.text(function(d) {
							return d.name
						});

						force.on("tick", function() {
							link.attr("x1", function(d) {
								return d.source.x;
							}).attr("y1", function(d) {
								return d.source.y;
							}).attr("x2", function(d) {
								return d.target.x;
							}).attr("y2", function(d) {
								return d.target.y;
							});

							node.attr("transform", function(d) {
								return "translate(" + d.x + "," + d.y + ")";
							});
						});
					}
				</script>
				<script>
	//	var jl = "{"links":[{"source":"laurie", "target":"hirsch, "rank":0}]}";
	var jl ={};
	jl.links =[{'source':'txt2vz', 'target' : 'by', 'rank':7},
	           {'source':'laurie', 'target' : 'by', 'rank':8},
	           {'source':'txt2vz', 'target' : 'document', 'rank':0},	         
	           {'source':'analysis', 'target' : 'visualization', 'rank':2},
	           {'source':'summary', 'target' : 'visualization', 'rank':3},
	           {'source':'concept', 'target' : 'visualization', 'rank':4},
	           {'source':'document', 'target' : 'mind', 'rank':7},
	           {'source':'mind', 'target' : 'map', 'rank':7},
	           {'source':'document', 'target' : 'visualization', 'rank':1},
	           {'source':'laurie', 'target' : 'hirsch', 'rank':2}	
	
	];
		drawLinks(jl);
		</script>
			</div>
			
			 <div id="modal">
             <img id="loader" src="images/ajax-loaderBig.gif" />
             <div id="fade"></div>
        </div>

			<div class='span2 sidebar'>

					<p style= "color:blue">Twitter query or Hashtag:</p>

				<input type="text" name="twitQ" id="twitQ" size=40 value="cloud">
				<input type="button" value="viz Twitter"
					onclick="lookupTwit(twitQ.value)"> 
						<p style= "color:blue"> <br><br>Enter a
				a URL: </p> <input type="text" id="url2"
					value="https://en.wikipedia.org/wiki/Tag_cloud" size=40>

				<button onclick="lookupURL(url2.value)">viz URL</button>

				<br /> <br />
				<p style= "color:blue">Paste in text:</p>

				<textarea id="txta1" COLS="30" ROWS="5"
					placeholder="Type or paste text here"> </textarea>
				<input type="button" value="viz text" onclick="pasted(txta1.value)">

				<br></br> <a href="http://txt2vz.eu01.aws.af.cm">See our AWS
					version with file upload option</a>

				<script>
					function pasted(theText) {
						//s= s.replace(/%/g, '');

						jQuery.post("/pastedText.groovy",
								{
 							      s:theText
							    },
					    function(data,status){
								    console.log("status in pasted is " +status);
                             drawLinks(data);
						    });
					}

				</script>

				<script>
			
					function lookupURL(url) {
						openModal();
						jQuery.ajax({
							type : "POST",
							url : "url.groovy",
							cache : false,
							data : "u=" + url,
							success : function(data, textStatus, jqXHR) {
                            closeModal();
								drawLinks(data);
							},
							error : function(jqXHR, textStatus, errorThrown) {
								alert("error " + textStatus);
							}
						});
					}
				</script>

				<script type="text/javascript">
					function lookupTwit(twq) {
						openModal();

						jQuery.ajax({
							type : "POST",						
							url : "/twitter.groovy",
							cache : false,
							data : "q=" + twq,
							success : function(data, status) {
								closeModal();
								console.log("in twit jq " + data);
								drawLinks(data); 
							},
							error : function(jqXHR, textStatus, errorThrown) {
								alert("error " + textStatus);
							}
						});
					}
				</script>
			</div>
		</div>
</body>

</html>