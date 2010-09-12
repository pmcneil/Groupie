/* 
 * This js is applied to the create page and edit page views
 */
(EIP = {

    cmdShiftKeybdMap : {
        54 : {
            cmd: 'superscript'
        },  // ^
        61 : {
            cmd: 'increasefontsize'
        }, // +
        56 : {
            cmd: 'insertunorderedlist'
        }, // *
        51 : {
            cmd: 'insertorderedlist'
        }, // #
        109 : {
            cmd: 'inserthorizontalrule'
        }, // _
        90 : {
            cmd: 'redo'
        } // Z
    },
    cmdAltKeybdMap : {
        fontSize : 1,
        49 : {
            cmd: 'formatblock',
            val: '<h1>'
        }, //1
        50 : {
            cmd: 'formatblock',
            val: '<h2>'
        }, //2
        51 : {
            cmd: 'formatblock',
            val: '<h3>'
        }, //3
        52 : {
            cmd: 'formatblock',
            val: '<h4>'
        }, //4
        53 : {
            cmd: 'formatblock',
            val: '<h5>'
        }, //5
        54 : {
            cmd: 'formatblock',
            val: '<h6>'
        }, //6
        55 : {
            cmd: function(){
                EIP.insertContentFromSelection();
            }
        },  //7
        56 : {
            cmd: function() {
                if(document.selection) {
                    var r = document.selection.createRange();
                    r.pasteHTML('<div class="fred">' + r.text + '</div>');
                }
            }
        },
        67 : { //c
            cmd: function() {
                EIP.boxyStylist();
                return false;
            }
        }
    },
    cmdKeybdMap : {
        83 : { // s
            cmd: function() {
                EIP.save();
            }
        },
        66 : { // b
            cmd: 'bold'
        },
        69 : { //e
            cmd: function() {
                EIP.editSource();
            }
        },
        73 : {
            cmd: 'italic'
        }, // i
        85 : {
            cmd: 'underline'
        }, // u
        84 : {
            cmd: 'strikethrough'
        }, // t
        54 : {
            cmd: 'subscript'
        }, // 6
        80 : { // p
            cmd: 'removeformat'
        },
        109 : {
            cmd: 'decreasefontsize'
        }, // -
        37 : {
            cmd: 'justifyleft'
        }, //left arrow
        38 : {
            cmd: 'justifycenter'
        }, //up arrow
        39 : {
            cmd: 'justifyright'
        }, //right arrow
        40 : {
            cmd: 'justifyfull'
        }, //down arrow
        90 : {
            cmd: 'Undo'
        }, // z
        192: {
            cmd: function(){
                EIP.insertLinkFromSelection();
            }
        }, //~
        45: {
            cmd: function(event){
                EIP.boxyInsertContent(event);
            }
        } //insert

    },

    log : function(msg) {
        $('#console').prepend('<div>').prepend(document.createTextNode(msg)).prepend('</div>');
    },

    getValue : function(thing) {
        return typeof(thing) == 'function' ? thing() : thing;
    },

    executeCommand : function(cmd, value) {
        if(cmd) {
            if(typeof(cmd) == 'function') {
                cmd();
            } else {
                document.execCommand(cmd, false, value);
            }
            this.log('did ' + cmd + ' (' + value + ')');
            return true;
        }
        return false;
    },

    processCommand : function(event) {
        if(event.ctrlKey || event.altKey){
            var cmd = this.getCmd(event);
            if(cmd) {
                return this.executeCommand(cmd.cmd, this.getValue(cmd.val));
            } else {
                return false;
            }
        }
        return false; //
    },

    getCurrentItemId : function() {
        return this.activeEditor[0].id ? this.activeEditor[0].id : 'new';
    },

    fireChanged : function() {
        if(EIP.sourceMode){
            EIP.editSource();
        }
        this.hideBoxes();
        var itemId = this.getCurrentItemId();
        var dataCopy = this.activeEditor.clone()
        dataCopy.find('div.embeded_content').remove();
        
        this.log('fired event: ');
        jQuery.ajax({
            type:'POST',
            data: {
                id: itemId,
                content: dataCopy.html()
            },
            url:'content/version',
            success:function(data,textStatus){
                EIP.log(data);
            },
            error:function(XMLHttpRequest,textStatus,errorThrown){
                EIP.log(textStatus);
            }
        });
        this.activeEditor = undefined;
    },

    getCmd : function(event) {
        if(event.shiftKey) {
            return this.cmdShiftKeybdMap[event.which];
        }
        if(event.altKey) {
            return this.cmdAltKeybdMap[event.which];
        }
        return this.cmdKeybdMap[event.which];
    },

    getSelectedText : function(){
        if(window.getSelection){
            return window.getSelection().toString();
        }
        else if(document.getSelection){
            return document.getSelection();
        }
        else if(document.selection){
            return document.selection.createRange().text;
        }
        return '';
    },

    getSelectedRange : function(){
        if(window.getSelection){
            return window.getSelection();
        }
        else if(document.getSelection){
            return document.getSelection();
        }
        else if(document.selection){
            return document.selection.createRange();
        }
        return null;
    },

    surroundSelection : function(elementType){
        if(window.getSelection){
            var sel = window.getSelection();
            var range = sel.getRangeAt(0);
            var container = document.createElement(elementType ? elementType : "div");
            range.surroundContents(container);
            return container;
        }
    },
    
    insertLinkFromSelection: function(){
        var text = EIP.getSelectedText();
        if(text){
            var node = EIP.surroundSelection("a");
            $(node).attr('href', encodeURI(text));
        }
    },

    insertContentByName : function(itemName, here){
        jQuery.ajax({
            type:'POST',
            data: {
                name: itemName
            },
            url:'content/byname',
            success:function(data, textStatus){
                $(here).replaceWith(data);
            },
            error:function(XMLHttpRequest,textStatus,errorThrown){
                EIP.log(textStatus);
            }
        });
    },

    insertContentById : function(itemId, here){
        jQuery.ajax({
            type:'POST',
            data: {
                id: itemId
            },
            url:'content/byid',
            success:function(data, textStatus){
                $(here).replaceWith(data);
            },
            error:function(XMLHttpRequest, textStatus, errorThrown){
                EIP.log(textStatus);
            }
        });
    },

    insertMedia : function(itemId, here){
        jQuery.ajax({
            type:'POST',
            data: {
                id: itemId
            },
            url:'media/content',
            success:function(data, textStatus){
                $(here).replaceWith(data);
                $('.media.audio').media({
                    height: 60,
                    width: 320
                });
                $('.media.video').media({
                    height: 280,
                    width: 320
                });
                
            },
            error:function(XMLHttpRequest,textStatus,errorThrown){
                EIP.log(textStatus);
            }
        });
    },

    editMode : function() {
        if(!EIP.edit){
            EIP.edit = true;
            $('a[href=#EditContent]').html('Stop Editing').addClass('editMode');
            $('#edit_menu').addClass('editMode');
            $('.editModeToggle').toggle();
            
        } else {
            this.hideBoxes();
            this.save();
            EIP.edit = false;
            if(EIP.isStylistOpen) EIP.isStylistOpen.hide();
            if(EIP.isInsertContentOpen) EIP.isInsertContentOpen.hide();
            if(EIP.isToolbarOpen) EIP.isToolbarOpen.hide();
            if(EIP.isMediaOpen) EIP.isMediaOpen.hide();
            $('.editable').attr("contenteditable","false");
            $('a[href=#EditContent]').html('Content').removeClass('editMode');
            $('#edit_menu').removeClass('editMode');
            $('.editModeToggle').toggle();
            $("#editMenu").hide();
            $("#editImgMenu").hide();
        }
    },
    
    editTarget : function(target){
        if(EIP.edit){
            var editContainer = $(target).closest("div.editable");
            if(EIP.activeEditor &&  (editContainer.length > 0 && EIP.activeEditor[0] !== editContainer[0])) {
                EIP.activeEditor.find("div.embeded").removeClass('showEmbeded');
                EIP.fireChanged();
            }
            if (editContainer.length > 0) {
                EIP.activeEditor = editContainer;
                $('.editable').attr("contenteditable","false");
                editContainer.attr("contenteditable","true");
                editContainer.find("div.embeded").addClass('showEmbeded');
            }
        }
    },

    save : function() {
        if(EIP.activeEditor) {
            EIP.activeEditor.find("div.embeded").removeClass('showEmbeded');
            EIP.fireChanged();
            $('.editable').attr("contenteditable","false");
        }
    },

    hideBoxes : function() {
        $.each(Groupie.boxyElements, function(){
            var b = this;
            b.hideAndUnload();
        });
        Groupie.boxyElements = [];
    },

    insertContentFromSelection: function(){
        var text = EIP.getSelectedText();
        var node = EIP.surroundSelection("div");
        EIP.insertContentByName(text, node);
    },

    insertMediaAtSelection: function(id, event){
        var oev = event.originalEvent;
        var range = document.createRange();
        range.setStart(oev.rangeParent, oev.rangeOffset);
        range.setEnd(oev.rangeParent, oev.rangeOffset);
        var node = document.createElement("div");
        range.surroundContents(node);
        EIP.insertMedia(id, node);
    },

    sourceEditorSave : function() {
        $('#sourceEditor').val(editAreaLoader.getValue('sourceEditor'));
        EIP.editSource();
    },
    sourceEditorLoad : function() {
        $('#EditAreaArroundInfos_sourceEditor').attr('contenteditable', false);
    },
    editSource : function() {
        if(EIP.edit){
            if(!EIP.sourceMode){
                var source = EIP.activeEditor.html();
                var height = EIP.activeEditor.height();
                var width = EIP.activeEditor.width();
                EIP.activeEditor.html("<textarea id='sourceEditor'></textarea>");
                $('#sourceEditor').text(source).height(height).width(width);
                editAreaLoader.init({
                    id: 'sourceEditor',
                    syntax: 'html',
                    allow_resize: "y",
                    allow_toggle: true,
                    word_wrap: true,
                    start_highlight: true,
                    line_numbers: false,
                    replace_tab_by_spaces: 4,
                    save_callback: "EIP.sourceEditorSave",
                    EA_load_callback: "EIP.sourceEditorLoad",
                    toolbar: "save, |, fullscreen, |, search, go_to_line, |, undo, redo, |, select_font, |, word_wrap, change_smooth_selection, highlight, reset_highlight, |, help"
                });
                $('#edit_area_toggle_reg_syntax.js').attr('contenteditable', 'false');
                EIP.sourceMode = true;
            } else {
                var source = $('#sourceEditor').val();
                EIP.activeEditor.html(source);
                $('#sourceEditor').remove();
                EIP.sourceMode = false;
            }
        }
    },

    postForm : function(form, func) {

        $.post(form.action, $(form).serialize(), func);
        return false;
    },

    resizeImage: function() {
        var src = this.contextTarget[0].src;
        src = src.replace(/\?.*/, "");
        var params = $("#editImgForm").serialize();
        if(params){
            src = src + "?" + params;
        }
        this.contextTarget[0].src = src;
    },

    boxyInsertMedia: function() {
        var uri = 'media/insert?type=image';
        if(!this.isMediaOpen) {
            Boxy.load(uri, {
                modal: false,
                unloadOnHide: true,
                title: 'Insert Media',
                afterShow: function() {
                    $('#spinner').hide();
                    Groupie.ajaxUpload();
                    Groupie.ajaxPageLinks();
                    EIP.isMediaOpen = this;
                },
                afterHide: function(){
                    EIP.isMediaOpen = false;
                }

            });
        }
    },

    boxyInsertContent: function() {
        if(!this.isInsertContentOpen){
            this.isInsertContentOpen = new Boxy('#insert_content', {
                title: "Insert Content",
                afterHide: function(){
                    EIP.isInsertContentOpen = false;
                }
            });
        }
    },

    boxyTag: function(id) {
        var uri = 'tag/tag?id=';
        if(id) {
            uri = uri + id;
        } else {
            uri = uri + EIP.getCurrentItemId();
        }
        Boxy.load(uri, {
            modal: false,
            unloadOnHide: true,
            title: 'Tag content',
            afterShow: function() {
                $('#spinner').hide();
                Groupie.afterBoxyShow(this);
            }
        });
    },

    boxyStylist: function() {
        if(!this.isStylistOpen){
            this.isStylistOpen = new Boxy('#stylist', {
                title: "Stylist",
                afterHide: function(){
                    EIP.isStylistOpen = false;
                }
            });
        }
    },

    boxyToolbar: function() {
        if(!this.isToolbarOpen){
            this.isToolbarOpen = new Boxy('#toolbar', {
                title: "Toolbar",
                afterHide: function(){
                    EIP.isToolbarOpen = false;
                }
            });
        }
    },

    boxyRevert: function() {
        var uri = 'content/history?id='+EIP.getCurrentItemId();
        Boxy.load(uri, {
            modal: false,
            unloadOnHide: true,
            title: 'Revert content',
            afterShow: function() {
                $('#spinner').hide();
                Groupie.afterBoxyShow(this);
            }
        });
    },

    boxyWrapDiv: function() {
        var text = EIP.getSelectedText();
        if(text){
            var node = EIP.surroundSelection("div");
            $(node).attr('id', 'wrapping');
            $('#wrapform input').val("");
            new Boxy('#wrapform', {
                title: "Wrap selected"
            });
        }
    },

    wrapWithDiv: function(className) {
        $('#wrapping').addClass(className).removeAttr('id');
    },

    getParentOfSelection: function() {
        var range = this.getSelectedRange();
        if(range) {
            var parent = $(range.anchorNode).parent();
            return parent;
        }
        return null;
    },

    insertCSS: function(css) {
        if (document.styleSheets[0].addRule) {           // Browser is IE?
            var m = css.match("(\.[^{]*){([^}]*)");
            document.styleSheets[0].addRule(m[1], m[2]);      // Yes, add IE style
        } else {                                         // Browser is IE?
            document.styleSheets[0].insertRule(css, 0); // Yes, add Moz style.
        }                                                // End browser check
    },

    showMedia: function(el, type) {
        var container = $(el).closest('.boxy-content');
        var uri = 'media/insert';
        var data = 'type=' + type;
        return Groupie.postData(uri, data, function(ret){
            $(container).html(ret);
            Groupie.ajaxUpload();
            Groupie.ajaxPageLinks();
            $('a.ogg').media({
                height: 60,
                width: 320
            });
            $('a.ogv').media({
                height: 280,
                width: 320
            });
        }, true)
    }
});

$(function(){
    $('body').bind('click', function(event) {
        EIP.editTarget(event.target);
    });
});

$(function(){
    $('.editable').bind('keydown', function(event) {
        if($(event.target).closest(this)){
            if(event.which != 16 && event.which != 17 && event.which != 18) { //not interested in ctrl/shift/alt key events
                EIP.log('keydown: ' + event.which + ' c: ' + event.ctrlKey + ' s: ' + event.shiftKey);
                return !EIP.processCommand(event);
            }
        }
        return true;
    }).bind('contextmenu', function(event){
        if(EIP.edit && EIP.activeEditor){
            var target;
            target = EIP.contextTarget = $(event.target);
            if(target.context.nodeName == "IMG"){
                $("#editImgMenu").show().css({
                    top:event.pageY+"px",
                    left:event.pageX+"px",
                    position:"absolute",
                    opacity: 1.0,
                    zIndex: 2000
                });
                var patt = new RegExp(/\?w=(\d*)\&h=(\d*)/);
                var results = patt.exec(target.context.src);
                if(results){
                    var form = $("#editImgForm");
                    form[0]["w"].value = results[1];
                    form[0]["h"].value = results[2];
                }
                patt.compile(/\&a=on/)
                if(patt.test(target.context.src)) {
                    $(form[0]["a"]).attr("checked", "checked");
                } else {
                    $(form[0]["a"]).removeAttr("checked");
                }
            } else {
                $("#editMenu").show().css({
                    top:event.pageY+"px",
                    left:event.pageX+"px",
                    position:"absolute",
                    opacity: 1.0,
                    zIndex: 2000
                });
            }
            return false;
        }
    }).bind('drop', function(event){
        var data = event.originalEvent.dataTransfer.getData('text/plain');
        if(data.indexOf('media-') == 0){
            event.preventDefault();
            EIP.insertMediaAtSelection(data, event)
            return false;
        }
    });

    $(document).bind("click",function(e){
        $("#editMenu").hide();
        $("#editImgMenu").hide();
    });
    
    $("#doc div, #doc span, #doc p, #doc h1, #doc h2, #doc h3, #doc h4").bind('mouseover',function(event) {
        event.stopPropagation();
        var target = $(event.target);
        var id = target.attr('id');
        var classes = target.attr('class');
        var style = target.attr('style');
        var status = '<' + target.get(0).tagName +
            (id ? ' id="' + id +'"' : '') +
            (classes ? ' class="' + classes + '"' : '') +
            (style ? ' style="' + style + '"' : '') + '>';
        window.status = status;
    }).click(function(event) {
        var target = event.target;
        $("#target-tag").val('').val(target.tagName).data('target', $(target));
        $("#target-id").val('').val($(target).attr('id'));
        $("#target-classes").val('').val($(target).attr('class'));
        $("#target-style").val('').val($(target).attr('style'));
    });
    $("#stylist_form").submit(function(event){
        event.preventDefault();
        var target = $("#target-tag").data('target');
        var tag = target.get(0).tagName;
        var newtag = $('#target-tag').val();
        if(tag != newtag) {
            target.wrapInner("<" + newtag + " id='_newtarget'>");
            var newtarget = $('#_newtarget');
            newtarget.unwrap();
            target = newtarget;
        }
        target.attr('class', $("#target-classes").val());
        target.attr('id', $("#target-id").val());
        target.attr('style', $("#target-style").val());
        return false;
    });
    $('#stylist_parent').click(function(){
        var target = $("#target-tag").data('target').parent();
        $("#target-tag").data('target', target);
        $("#target-tag").val('').val(target.get(0).tagName)
        $("#target-id").val('').val(target.attr('id'));
        $("#target-classes").val('').val(target.attr('class'));
        $("#target-style").val('').val(target.attr('style'));
    });

    $('.tagEntry').jSuggest({
        url: "tag/suggest",
        type: "GET",
        data: "q",
        minchar: 1,
        delay: 100,
        loadingImg: "images/spinner.gif",
        autochange: true
    });
    $('.classEntry').jSuggest({
        url: "style/suggest",
        type: "GET",
        data: "q",
        minchar: 1,
        delay: 100,
        loadingImg: "images/spinner.gif",
        autochange: true,
        click: function(text) {
            if(text){
                EIP.wrapWithDiv(text);
            }
        }
    });
    $('#stylist_insertform').bind('submit', function(event){
        event.preventDefault();
        var val = $('#contentcss').val();
        Groupie.postForm(this, function(data, opts) {
            if(data.success){
                EIP.insertCSS(val);
            } else {
                EIP.log('Problem inserting Style in CSS');
            }
        });
        return false;
    });
    $('#insert_content_type, #insert_content_tags').bind('change', function(event){
        return Groupie.postForm(this.form, function(ret){
            var target = $('#insert_content_select');
            target.html(ret);
        }, true);
    });
    $('#insert_content_button').click(function(event){
        var node = EIP.surroundSelection('div');
        EIP.insertContentById($(this.form.item).val(), node);
    });
    $("div.embeded").not("editable").attr('contenteditable','false');
});
