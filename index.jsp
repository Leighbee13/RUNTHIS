<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" type="text/css" href="main2.css">

        <!--Source: http://jqueryui.com/sortable/#default-->
        <link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css">
        <script src="http://code.jquery.com/jquery-1.9.1.js"></script>
        <script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
        
        <script>
            $(function() {
                $("#sortable").sortable();
                $("#sortable").disableSelection();
            });
        </script>
        <link rel="shortcut icon" type="image/x-icon" href="icon.ico">
        <title>RUNTHIS//</title>
    </head>

    <body>
        <div id="body">
            <div id="header">
                <hr>
                <h1>RUNTHIS<span class="darkgrey">/</span><span class="grey">/</span><span class="yellow">/</span></h1><h3>- Your own exercise program playlists with your own music!</h3>

            </div>

            <div id="instructions">
                <hr>
                <h3 id="showinstruct" >Click here to open Instructions.</h3> 
                <div id='instruct' style="display:none">
                    <hr>
                    <h2>Using <strong>RUNTHIS<span class="darkgrey">/</span><span class="grey">/</span><span class="yellow">/</span></strong> is easy! </h2>
                    <ol id="list" >
                        <li>Upload a file using the choose file button.</li>
                        <li>Add more file inputs using the add more button.</li>
                        <li>Select an exercise program using the drop down list.</li>
                        <li>Click GO! </li>
                        <li>Your file will be ready to download! Then just put this file on your music player and get running!</li>
                    </ol>
                </div>
            </div>
            <div id="musicuploader" class="side-by-side">
                <hr>
                <form id="file_upload" method="POST" action="Process" enctype="multipart/form-data" >
                    <div id="inputs">
                        <p><button type='button' id="add_more" style="float: right;">Add Song</button><!--<a id="add_more" href="#">Add song</a>--></p>
                        <h3>Songs:</h3>
                        <p> <input type="file" class= "addsort" id="file_1" name ="1" accept=".wav, .au*" required/></p>
                    </div>
                    <hr>

                    <h3>Program:</h3>

                    <select name="programselect" id="programselect" size="1">
                        <option title="couch to 5k week 1" value="c25kweek1.wav">C25k Week 1</option>
                        <option title="couch to 5k week 2" value="c25k2.wav">C25k Week 2</option>  
                        <option title="couch to 5k week 1" value="c25k3.wav">C25k Week 3</option>
                        <option title="couch to 5k week 1" value="c25k4.wav">C25k Week 4</option>
                        <option title="couch to 5k week 1" value="c25k5.wav">C25k Week 5</option>
                        <option title="couch to 5k week 1" value="c25k6.wav">C25k Week 6</option>
                        <option title="couch to 5k week 1" value="c25k7.wav">C25k Week 7</option>
                        <option title="couch to 5k week 1" value="c25k8.wav">C25k Week 8</option>
                        <option title="time spent exercising reminder every 2 minutes" value="c25k2.wav">2 min reminder</option>
                        <option title="time spent exercising reminder every 5 minutes" value="c25k2.wav">5 min reminder</option>
                        <option title="time spent exercising reminder every 10 minutes" value="c25k2.wav">10 min reminder</option>
                    </select>
                    <input type="submit" value="GO!"/>
                </form>
            </div>
            <div id="sorter" class="side-by-side" name="sorter">
                <hr name="hr">
                <button name="button" id='shuffle' style="float: right;">Shuffle</button> <h3>Sort the songs by drag and drop here:</h3>
                <ul id="sortable" name="sortable">
                    <li id ="song1" name = "1" class="ui-state-default"><span name= "span" class="ui-icon ui-icon-arrowthick-2-n-s"></span> <img class="deletesong" style="float: right;" src="images/delete.png" alt="del" width="20" height="20"/></li>

                </ul>
            </div>

            <div id='underbarwrapper'>
                <hr>
                <div  id="about" class="side-by-side">
                    <h3>About</h3>
                    <p>This software was created by Leigh Hayward for her Computing BSc Dissertation in response to many runners need to create simple playlists with their own music and running audio cues</p>
                </div>
                <div  id="contact" class="side-by-side">
                    <h3>Contact</h3>
                    <a href="https://twitter.com/leighbee3"><img src="images/twitter.png" alt="twitter" width="30" height="30"></a><a href="http://uk.linkedin.com/in/leighhayward/"><img src="images/linkedin.png" alt="linkedin" width="30" height="30"></a><a href="https://www.facebook.com/pages/Runthis/1431490097107811"><img src="images/facebook.png" alt="facebook" width="30" height="30"></a>
                </div>
                <div id="something" class="side-by-side">

                </div>
            </div>
        </div>
        <script type="text/javascript" src="js/ext.js"></script>
    </body>



</html>
