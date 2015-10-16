<!DOCTYPE html>
<html lang="en">

<head>
    <link rel="shortcut icon" href="favicon.ico">
    <link rel="stylesheet" href="css/wait.css">

    <link rel="stylesheet" href="http://netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css">
    <link href="http://getbootstrap.com/2.3.2/assets/css/bootstrap-responsive.css" rel="stylesheet">

    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <title>txt2vz</title>

    <script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/d3/3.5.6/d3.min.js"></script>
    <script src="js/colorbrewer.js" type="text/javascript"></script>

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
            stroke: #666;
            stroke-width: 2 px;
        }
    </style>
</head>

<body>
<div class="container">
    <em style="font-size: 48px"><b> <span
            style="color: blueviolet">t</span><span style="color: cyan">xt</span><span
            style="color: magenta">2</span><span style="color: cyan">vz</span></b></em>:
        <span style="font-family: arial; font-size: 17px;">Visualize
			your Documents.</span>

    <div id='content' class='row-fluid'>
        <div class='span10 main active'>

        </div>

        <div class='span2 sidebar'>
            <div id="modal">
                <img id="loader" src="images/ajax-loaderBig.gif" />
                <div id="fade"></div>
            </div>

            <p>Type in a Twitter query or Hashtag:</p>

            <input type="text" name="twitQ" id="twitQ" size=40 value="word cloud">
            <input type="button" value="viz Twitter" onclick="lookupTwit(twitQ.value)">
            <br/>
            <br/> Type in a URL
            <input type="text" id="url2" value="https://en.wikipedia.org/wiki/Tag_cloud" size=40>

            <button onclick="lookupURL(url2.value)">viz URL</button>
            <br/>
            <br/>

            <p>Paste in text</p>

            <textarea id="txta1" COLS="30" ROWS="5" placeholder="Type or paste text here"> </textarea>
            <input type="button" value="viz text" onclick="pasted(txta1.value)">
            <br />
            <br />

            <label for="linkTotal">Set Max Links to Show</label>
            <input type="range" min="1" max="200" value="40" id="linkInput" step="1" oninput="outputUpdate(value)">
            <output for="linkTotal" id="linkTotal">40</output>
        </div>
    </div>
</div>
<div class="panel-footer" style="font-family: arial; font-size: 10px; background-color:white;">
    By Laurie Hirsch Info: <a href="https://plus.google.com/u/0/b/117816592489907408686/117816592489907408686/about" target="_blank">txt2vz</a>
</div>

<script>
    function outputUpdate(ml2) {
        document.querySelector('#linkTotal').value = ml2;
    }
</script>


<script>
    function drawLinks(jsonlinks) {

        d3.select("svg").remove();
        var linksobj;

        if (jsonlinks == null || typeof jsonlinks == 'object') {
            linksobj = jsonlinks;
        } else
            linksobj = JSON.parse(jsonlinks);

        //	var linksobj = jsonlinks; // JSON.parse(jsonlinks);
        var links = linksobj.links;
        var nodes = {};

        var w = 960,
                h = 500;
        var svg = d3.select(".main").append("svg").attr(
                "width", w).attr("height", h);

        // Compute the distinct nodes from the links.
        links.forEach(function(link) {
            var sc = link.source;
            var tg = link.target;
            link.source = nodes[sc] || (nodes[sc] = {
                        name: sc,
                        numberLinks: 0,
                        totalCooc: 0
                    });
            link.target = nodes[tg] || (nodes[tg] = {
                        name: tg,
                        numberLinks: 0,
                        totalCooc: 0
                    });

            //count number of links for each node
            nodes[sc].numberLinks++;
            nodes[tg].numberLinks++;
            nodes[sc].totalCooc += link.cooc;
            nodes[tg].totalCooc += link.cooc;
        });

        var force = d3.layout.force().gravity(.05).charge(-200)
                .size([w, h]);

        var linkCoocExtent = d3.extent(links, function(d) {
            return d.cooc
        });

        force.nodes(d3.values(nodes)).links(links)
                .linkDistance(
                function(d) {

                    var distanceScale = d3.scale
                            .linear().domain(
                            linkCoocExtent)
                            .range([180, 80]);
                    return distanceScale(d.cooc);

                }).start();

        //  console.log(" links cooc extent " + linkCoocExtent);
        var link = svg.selectAll(".link").data(links).enter()
                .append("line").attr(
                "stroke-width",
                function(d) {

                    var linkWidthScale = d3.scale
                            .linear().domain(
                            linkCoocExtent)
                            .range([0.1, 3]);
                    return linkWidthScale(d.cooc);

                }).attr("class", "link");

        var totalCoocArray = [];
        var nodeTotalCoocExtent = d3.extent(d3.values(nodes),
                function(d) {
                    totalCoocArray.push(d.totalCooc);
                    return d.totalCooc
                });

        var node = svg.selectAll(".node")
                .data(d3.values(nodes)).enter().append("g")
                .attr("class", "node").call(force.drag);

        var fontScale = d3.scale.linear().domain(
                nodeTotalCoocExtent).range([8, 20]);

        node
                .append("rect")
                .attr(
                "width",
                function(d) {

                    return d.name.length * (fontScale(d.totalCooc) / 2) + 20;
                })
                .attr("height", function(d) {
                    return 20;
                })
                .attr("ry", 8)
                .attr("rx", 8)
                .attr("y", -15)
                .attr("x", -5)

                .attr(
                "opacity",
                function(d) {
                    var opacityScale = d3.scale
                            .linear()
                            .domain(nodeTotalCoocExtent)
                            .range([0.4, 0.7]);
                    var opacityValue = opacityScale(d.totalCooc);
                    return opacityValue;
                })
                .attr(
                "fill",
                function(d) {

                    var colorScale = d3.scale
                            .quantile()
                            .domain(totalCoocArray)
                            .range(
                            colorbrewer
                                    .BuPu[8]
                            // .Greens[8]

                    );
                    //	[
                    //	 'blue', 'green' , 'yellow', 'red'
                    //   'aqua', 'magenta'
                    //		"cyan",	"blueviolet", "magenta"
                    //	]);
                    var colorValue = colorScale(d.totalCooc);
                    return colorValue;
                });

        node.append("text").attr(
                "font-size",
                function(d) {

                    var fontValue = fontScale(d.totalCooc);
                    //	console.log("TotalCooc" + d.totalCooc
                    //			+ " fontValue " + fontValue);
                    return fontValue;

                }).text(function(d) {
                    return d.name; // + " cooc:  " + d.totalCooc;
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
    var jl = {};
    jl.links = [{
        'source': 'txt2vz',
        'target': 'by',
        'cooc': 2,
        'rank': 7
    }, {
        'source': 'laurie',
        'target': 'by',
        'cooc': 3,
        'rank': 8
    }, {
        'source': 'txt2vz',
        'target': 'document',
        'cooc': 4,
        'rank': 0
    }, {
        'source': 'analysis',
        'target': 'visualization',
        'cooc': 4,
        'rank': 2
    }, {
        'source': 'summary',
        'target': 'visualization',
        'cooc': 2,
        'rank': 3
    }, {
        'source': 'concept',
        'target': 'visualization',
        'cooc': 5,
        'rank': 4
    }, {
        'source': 'document',
        'target': 'mind',
        'cooc': 4,
        'rank': 7
    }, {
        'source': 'mind',
        'target': 'map',
        'cooc': 4,
        'rank': 7
    }, {
        'source': 'document',
        'target': 'visualization',
        'cooc': 6,
        'rank': 1
    }, {
        'source': 'laurie',
        'target': 'hirsch',
        'cooc': 4,
        'rank': 2
    }];
    drawLinks(jl);
</script>

<script>
    function pasted(s) {
        s = s.replace(/%/g, '');
        openModal();

        jQuery.ajax({
            type: "POST",
            url: "pastedText.groovy",
            cache: false,
            data: "s=" + s + "&maxLinks=" + document.getElementById("linkInput").value,
            success: function(data, textStatus, jqXHR) {
                closeModal();
                drawLinks(data);
            }
        });
    }
</script>

<script type="text/javascript">
    function lookupURL(url) {
        openModal();
        jQuery.ajax({
            type: "POST",
            url: "url.groovy",
            cache: false,
            data: "u=" + url + "&maxLinks=" + document.getElementById("linkInput").value,
            success: function(data, textStatus, jqXHR) {
                closeModal();
                drawLinks(data);
            },
            error: function(jqXHR, textStatus, errorThrown) {
                closeModal();
                alert("error " + textStatus);
            }
        });
    }
</script>

<script type="text/javascript">
    function lookupTwit(twq) {
        openModal();
        jQuery.ajax({
            type: "POST",
            url: "twitter.groovy",
            cache: false,
            data: "q=" + twq + "&maxLinks=" + document.getElementById("linkInput").value,
            success: function(data, textStatus, jqXHR) {
                closeModal();
                drawLinks(data);
            },
            error: function(jqXHR, textStatus, errorThrown) {
                closeModal();
                alert("error " + textStatus);
            }
        });
    }
</script>
</body>
</html>