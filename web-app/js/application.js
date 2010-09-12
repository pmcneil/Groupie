(Groupie = {
    boxyElements : [],
    afterBoxyShow: function (el) {
        this.boxyElements.push(el);
        if($('#styleEditor').length == 1){
            editAreaLoader.init({
                id: 'content',
                syntax: 'css',
                start_highlight: true,
                replace_tab_by_spaces: 4
            });
        }
        $('#tagEntry').jSuggest({
            minchar: 1,
            delay: 100,
            loadingImg: 'images/spinner.gif',
            url: "tag/suggest",
            type: "GET",
            data: "q",
            autochange: true
        });
        $('#j_username').focus();

        this.hideOptions();
        this.ajaxPageLinks()
    },

    ajaxPageLinks: function () {
        $('.paginateButtons a').click(function() {
            return Groupie.ajaxPage(this);
        });
    },

    hideOptions: function () {
        $('#showOptions').click(function(){
            $(this).toggleClass("optionsOpen").toggleClass("optionsClosed").next().toggle();
            $('.options').toggle();
            return false;
        });
        $('.options').hide();
    },

    ajaxSubmitAndReplace : function(button, container) {
        this.isAjaxSubmit = true;
        var form = $(button.form);
        var uri = form[0].action
        var data = form.serialize() + "&" + button.name + "=" + button.value;
        if(!container) {
            container = $(button).closest('.boxy-content');
        }
        return this.postData(uri, data, function(ret){
            $(container).html(ret);
        }, true)
    },

    postForm : function(form, func, html) {
        return this.postData(form.action, $(form).serialize(), func, html)
    },

    postData : function(uri, data, func, html, opts) {
        $.ajax({
            type: "POST",
            url: uri,
            data: data,
            success: function(resp){
                if(html){
                    func(resp);
                } else {
                    var d = eval('(' + resp + ')')
                    if(d.log) {
                        EIP.log(d.log);
                    }
                    if(d.redirect){
                        if(d.uri == '.'){
                            window.location.reload();
                        } else {
                            window.location = d.uri;
                        }
                        return
                    }

                    if(d.html){
                        func(d.html);
                        if(d.editor){
                            editAreaLoader.init({
                                id: 'content',
                                syntax: d.editor,
                                start_highlight: true,
                                allow_resize: "y",
                                allow_toggle: true,
                                word_wrap: true,
                                replace_tab_by_spaces: 4
                            });
                        }
                        $('#tagEntry').jSuggest({
                            minchar: 1,
                            delay: 100,
                            loadingImg: 'images/spinner.gif',
                            url: "tag/suggest",
                            type: "GET",
                            data: "q",
                            autochange: true
                        });
                    } else {
                        func(d, opts);
                    }
                }
            },
            cache: false
        });
        return true;
    },

    status: function(d, opts){
        if(opts){
            var el = jQuery(opts.el);
            var pos = el.position();
            var top = pos.top - (el.height());
            jQuery('#memberDetailMsg').css('top',top);
        }
        jQuery('#memberDetailErrors').html(d.errors);
        jQuery('#memberDetailMsg').html(d.ok).show().fadeOut(2000);
    },

    ajaxPage: function (el) {
        var jthis = $(el);
        var contentPane = jthis.closest('.boxy-content');
        $.get(jthis.attr('href'), function(data) {
            $(contentPane).html(data);
            Groupie.ajaxPageLinks()
            Groupie.ajaxUpload(el);
        });
        return false;
    },

    ajaxUpload: function(){

        var el = $('a.upload');
        if(el.length == 0) {
            return;
        }

        var button = el, interval;
        new AjaxUpload(button,{
            action: button.attr('href'),
            name: 'media',
            onSubmit : function(file, ext){
                // change button text, when user selects file
                button.text('Uploading');

                // If you want to allow uploading only 1 file at time,
                // you can disable upload button
                this.disable();

                // Uploding -> Uploading. -> Uploading...
                interval = window.setInterval(function(){
                    var text = button.text();
                    if (text.length < 13){
                        button.text(text + '.');
                    } else {
                        button.text('Uploading');
                    }
                }, 200);
            },
            onComplete: function(file, response){
                button.text('Upload');

                window.clearInterval(interval);

                // enable upload button
                this.enable();
                var boxyContent = button.closest('.boxy-content');
                boxyContent.html(response);
                Groupie.ajaxPageLinks();
                Groupie.ajaxUpload();
                // add file to the list
                EIP.log(file);
            }
        });
    }
});

$(function() {
    $('body').mousemove(function(e){
        Groupie.x = e.pageX;
        Groupie.y = e.pageY;
    });

    $('ul.dropmenu').droppy({
        speed: 10,
        delay: 100
    });
    
    $('a.boxymodal').boxy({
        modal: true,
        afterShow: function() {
            $('#spinner').hide();
            Groupie.afterBoxyShow(this);
        },
        unloadOnHide: true
    });
    $('a.boxy').boxy({
        modal: false,
        afterShow: function() {
            $('#spinner').hide();
            Groupie.afterBoxyShow(this);
        },
        unloadOnHide: true
    });
    $('.media.audio').media({
        height: 60,
        width: 320
    });
    $('.media.video').media({
        height: 280,
        width: 320
    });
    $('#spinner').ajaxStart(function(e){
        $(this).css({
            left: Groupie.x,
            top: Groupie.y
        }).show();
    }).ajaxComplete(function(){
        $(this).hide();
    });
    $('.toggleNext').click(function(){
        jQuery(this).next('div').toggle('blind',500);
    }).next('div').hide();
    $('input.date').datepicker({
        dateFormat: 'yy-mm-dd',
        showOn: 'button',
        buttonImage: '../images/icons/silk/date.png',
        buttonImageOnly: true,
        changeMonth: true,
        changeYear: true
    });
    $('input.time').timeEntry();
});
