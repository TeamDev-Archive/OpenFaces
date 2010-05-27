/*
* Print Element Plugin 1.1
*
* Copyright (c) 2010 Erik Zaadi
*
* Inspired by PrintArea (http://plugins.jquery.com/project/PrintArea) and
* http://stackoverflow.com/questions/472951/how-do-i-print-an-iframe-from-javascript-in-safari-chrome
*
*  jQuery plugin page : http://plugins.jquery.com/project/printElement
*  Wiki : http://wiki.github.com/erikzaadi/jQueryPlugins/jqueryprintelement
*  Home Page : http://erikzaadi.github.com/jQueryPlugins/jQuery.printElement
*
*  Thanks to David B (http://github.com/ungenio) and icgJohn (http://www.blogger.com/profile/11881116857076484100)
*  For their great contributions!
*
* Dual licensed under the MIT and GPL licenses:
*   http://www.opensource.org/licenses/mit-license.php
*   http://www.gnu.org/licenses/gpl.html
*
*   Note, Iframe Printing is not supported in Opera and Chrome 3.0, a popup window will be shown instead
*/

(function($){
    $.fn.printElement = function(options){
        var mainOptions = $.extend({}, $.fn.printElement.defaults, options);
        //iframe mode is not supported for opera and chrome 3.0 (it prints the entire page).
        //http://www.google.com/support/forum/p/Webmasters/thread?tid=2cb0f08dce8821c3&hl=en
        if (mainOptions.printMode == 'iframe') {
            if ($.browser.opera || (/chrome/.test(navigator.userAgent.toLowerCase())))
                mainOptions.printMode = 'popup';
        }
        //Remove previously printed iframe if exists
        $("[id^='printElement_']").remove();

        return this.each(function(){
            //Support Metadata Plug-in if available
            var opts = $.meta ? $.extend({}, mainOptions, $this.data()) : mainOptions;
            _printElement($(this), opts);
        });
    };
    $.fn.printElement.defaults = {
        printMode: 'popup', //Usage : iframe / popup
        pageTitle: '', //Print Page Title
        overrideElementCSS: null,
        /* Can be one of the following 3 options:
         * 1 : boolean (pass true for stripping all css linked)
         * 2 : array of $.fn.printElement.cssElement (s)
         * 3 : array of strings with paths to alternate css files (optimized for print)
         */
        printBodyOptions: {
            styleToAdd: 'padding:10px;margin:10px;', //style attributes to add to the body of print document
            classNameToAdd: '' //css class to add to the body of print document
        },
        leaveOpen: false, // in case of popup, leave the print page open or not
        iframeElementOptions: {
            styleToAdd: 'border:none;position:absolute;width:0px;height:0px;bottom:0px;left:0px;', //style attributes to add to the iframe element
            classNameToAdd: '' //css class to add to the iframe element
        }
    };
    $.fn.printElement.cssElement = {
        href: '',
        media: ''
    };
    function _printElement(element, opts){
        //Create markup to be printed
        var html = _getMarkup(element, opts);

        var popupOrIframe = null;
        var documentToWriteTo = null;
        if (opts.printMode.toLowerCase() == 'popup') {
          var width = (!($.browser.msie)) ? element.width() + 100 : element.width() + 120;
          var height = (!($.browser.msie))? element.height() + 120 : element.height() + 140;
          popupOrIframe = window.open('about:blank', 'printElementWindow', 'width=' + width + ',height=' + height + ',scrollbars=yes');
            documentToWriteTo = popupOrIframe.document;
        }
        else {
            //The random ID is to overcome a safari bug http://www.cjboco.com.sharedcopy.com/post.cfm/442dc92cd1c0ca10a5c35210b8166882.html
            var printElementID = "printElement_" + (Math.round(Math.random() * 99999)).toString();
            //Native creation of the element is faster..
            var iframe = document.createElement('IFRAME');
            $(iframe).attr({
                style: opts.iframeElementOptions.styleToAdd,
                id: printElementID,
                className: opts.iframeElementOptions.classNameToAdd,
                frameBorder: 0,
                scrolling: 'no',
                src: 'about:blank'
            });
            document.body.appendChild(iframe);
            documentToWriteTo = (iframe.contentWindow || iframe.contentDocument);
            if (documentToWriteTo.document)
                documentToWriteTo = documentToWriteTo.document;
            iframe = document.frames ? document.frames[printElementID] : document.getElementById(printElementID);
            popupOrIframe = iframe.contentWindow || iframe;
        }

        documentToWriteTo.open();
        documentToWriteTo.write(html);
        documentToWriteTo.close();

    };

    function _callPrint(element){
        if (element && element.printPage)
            element.printPage();
        else
            setTimeout(function(){
                _callPrint(element);
            }, 50);
    }

    function _getElementHTMLIncludingFormElements(element){
        var $element = $(element);
        //Radiobuttons and checkboxes
        $(":checked", $element).each(function(){
            this.setAttribute('checked', 'checked');
        });
        //simple text inputs
        $("input[type='text']", $element).each(function(){
            this.setAttribute('value', $(this).val());
        });
        $("select", $element).each(function(){
            var $select = $(this);
            $("option", $select).each(function(){
                if ($select.val() == $(this).val())
                    this.setAttribute('selected', 'selected');
            });
        });
        $("textarea", $element).each(function(){
            //Thanks http://blog.ekini.net/2009/02/24/jquery-getting-the-latest-textvalue-inside-a-textarea/
            var value = $(this).attr('value');
            if ($.browser.mozilla)
                this.textContent = value;
            else
                this.innerHTML = value;
        });
        //http://dbj.org/dbj/?p=91
        var elementHtml = $('<div></div>').append($element.clone()).html();
        return elementHtml;
    }

	//http://github.com/erikzaadi/jQueryPlugins/issues#issue/3
    function _getBaseHref(){
		return window.location.protocol + "//" + window.location.hostname + (window.location.port ? ":" + window.location.port : "") + window.location.pathname;
	}


    function _getMarkup(element, opts){
        var $element = $(element);
        var elementHtml = _getElementHTMLIncludingFormElements(element);

        var html = new Array();
        html.push('<html><head><title>' + opts.pageTitle + '</title>');
        if (opts.overrideElementCSS) {
            if (opts.overrideElementCSS.length > 0) {
                for (var x = 0; x < opts.overrideElementCSS.length; x++) {
                    var current = opts.overrideElementCSS[x];
                    if (typeof(current) == 'string')
                        html.push('<link type="text/css" rel="stylesheet" href="' + current + '" >');
                    else
                        html.push('<link type="text/css" rel="stylesheet" href="' + current.href + '" media="' + current.media + '" >');
                }
            }
        }
        else {
            $(document).find("link").filter(function(){
                return $(this).attr("rel").toLowerCase() == "stylesheet";
            }).each(function(){
                var media = $(this).attr('media');
                html.push('<link type="text/css" rel="stylesheet" ' +
                             'href="' + $(this).attr("href") +
                             (media ? '" media="' + media : "") +
                          '" >');
            });
        }
        var width = (!($.browser.msie)) ? $element.width() + 40 : $element.width() + 60;
        var height = (!($.browser.msie))? $element.height() + 80 : $element.height() + 100;
        //Ensure that relative links work
        html.push('<base href="' + _getBaseHref() + '" />');
        html.push('</head><body style="min-width:' + width + 'px; ' + 'width:' + width + 'px;' + ' min-height:' + height + 'px;' + ' height:' + height + 'px; ' + opts.printBodyOptions.styleToAdd + '" class="' + opts.printBodyOptions.classNameToAdd + '">');
        html.push('<div  style="width:' + width +'px;">'+'<input type="button" onclick="print(); return false;" value="Print" style="float:right; margin-bottom: 10px; margin-top:10px;"/></div>');
        
        html.push('<div  class="' + $element.attr('class') + '">' + elementHtml + '</div>');
        html.push('<div  style="width:' + width  + 'px;">'+'<input type="button" onclick="print(); return false;" value="Print" style="float:right; margin-top:10px;"/></div>');
        html.push('<div style="clear:both;"/>');
        html.push('<script type="text/javascript">function printPage(){}</script>');
        html.push('</body></html>');

        return html.join('');
    };
    })(jQuery);