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
    <script src="js/frontPage.js"></script>
    <script src="js/drawLinks.js"></script>

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

            <br>
            <p> Select color scheme: </p>

            <input type="radio" id="bupu" name="colorRadio" value="BluePurple" checked> BluePurple
            <br>
            <input type="radio" id="burd" name="colorRadio" value="BlueRed"> BlueRed
            <br>
            <input type="radio" id="t2v" name="colorRadio" value="t2v"> t2v

        </div>
    </div>
</div>
<div class="panel-footer" style="font-family: arial; font-size: 10px; background-color:white;">
    By Laurie Hirsch Info: <a href="https://plus.google.com/u/0/b/117816592489907408686/117816592489907408686/about" target="_blank">txt2vz</a>
</div>

<script>
    function outputUpdate(ml2) {
        document.querySelector('#linkTotal').value = ml2;    }
</script>

<script>
    frontPage();
</script>

<script>
    jQuery('input').click(function() {
        jQuery(this).select();
    });
</script>

<script>
    //Bind keypress event to textbox
    jQuery('#txta1').keypress(function(event) {
        var keycode = (event.keyCode ? event.keyCode : event.which);
        if (keycode == '13') {
            console.log('You pressed a "enter" key in textbox');
            pasted(txta1.value);
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
            lookupTwit(twitQ.value);
        }
        //Stop the event from propogation to other handlers
        //If this line will be removed, then keypress event handler attached
        //at document level will also be triggered
        event.stopPropagation();
    });

    //Bind keypress event to textbox
    jQuery('#url2').keypress(function(event) {
        var keycode = (event.keyCode ? event.keyCode : event.which);
        if (keycode == '13') {
            console.log('You pressed a "enter" key in url2');
            lookupURL(url2.value);
        }
        //Stop the event from propogation to other handlers
        //If this line will be removed, then keypress event handler attached
        //at document level will also be triggered
        event.stopPropagation();
    });
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