$(document).ready(function() {
    
    $('#add_more').click(function() {
        var current_count;
        if ($('.addsort').length !== 0) {
            current_count = parseInt($('#inputs').children(':last-child').children().first().attr('name'));
        }else{
            current_count=1;
        }
        var next_count = current_count + 1;
        $('#sortable').append('<li id= "song' + next_count + '" name="'+next_count+
                '" class="ui-state-default"><span class="ui-icon ui-icon-arrowthick-2-n-s"></span> <img class="deletesong" src="images/delete.png" alt="del" width="20" height="20" style="float: right;"></li>');
        $('#inputs').append('<p><input type="file" id="file_' + next_count + '" class = "addsort" name ="' + next_count + '" accept=".wav, .au*" required/></p>');
    });

    $('#sortable').on('click', '.deletesong', function() {
        var number = $(this).parent().attr('name');
        $(this).parent().remove();
        $('#file_'+number).parent().remove();

    });

    $('#showinstruct').click(function() {
        $("#instruct").slideToggle();
    });

    $('#shuffle').click(function() {
        var ul = document.getElementById("sortable");
        for (var i = ul.children.length; i >= 0; i--)
            ul.appendChild(ul.children[Math.random() * i | 0]);
    });

    $('[id^="file_"]').on('change', '.addsort', function() {
        var filename = $(this).val();
        filename = filename.split('\\').pop();

        var number = $(this).attr('name');

        $('#song' + number).html('<span class="ui-icon ui-icon-arrowthick-2-n-s"></span>' + filename + 
                '<img class="deletesong" src="images/delete.png" alt="del" width="20" height="20" style="float: right;">');
    });



});
