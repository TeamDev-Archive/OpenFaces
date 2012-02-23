/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2012, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

O$.FileUploadUtil = {
  _initGeneral:function(fileUpload,
                        lengthAlreadyUploadedFiles, acceptedTypesOfFile, isDisabled, ID,
                        addButtonClass, addButtonOnMouseOverClass, addButtonOnMouseDownClass,addButtonOnFocusClass,
                        statusLabelInProgress,statusLabelUploaded,statusLabelErrorSize,
                        statusLabelNotUploaded,statusStoppedText,statusLabelUnexpectedError,
                        renderAfterUpload,tabIndex,dropTargetCrossoverClass,externalDropTarget,acceptDialogFormats,
                        directoryDroppedText){
    O$.FileUploadUtil._initGeneralFunctions(fileUpload);
    O$.FileUploadUtil._initGeneralFields(fileUpload,
            lengthAlreadyUploadedFiles, acceptedTypesOfFile, isDisabled, ID,
            addButtonClass, addButtonOnMouseOverClass, addButtonOnMouseDownClass,addButtonOnFocusClass,
            statusLabelInProgress,statusLabelUploaded,statusLabelErrorSize,
            statusLabelNotUploaded,statusStoppedText,statusLabelUnexpectedError,
            renderAfterUpload,tabIndex,dropTargetCrossoverClass,externalDropTarget, acceptDialogFormats,
            directoryDroppedText);
  },
  _initGeneralFields:function (fileUpload,
                               lengthAlreadyUploadedFiles, acceptedTypesOfFile, isDisabled, ID,
                               addButtonClass, addButtonOnMouseOverClass, addButtonOnMouseDownClass,addButtonOnFocusClass,
                               statusLabelInProgress,statusLabelUploaded,statusLabelErrorSize,
                               statusLabelNotUploaded,statusStoppedText,statusLabelUnexpectedError,
                               renderAfterUpload, tabIndex, dropTargetCrossoverClass, externalDropTarget, acceptDialogFormats,
                               directoryDroppedText) {
    O$.extend(fileUpload, {
      _numberOfFilesToUpload:0,
      _lengthUploadedFiles:lengthAlreadyUploadedFiles,
      _typesOfFile:(acceptedTypesOfFile != null) ? acceptedTypesOfFile.split(" ") : null,
      _isDisabled:isDisabled,
      _ID:ID,
      _events:{},
      _numberOfFailedRequest:35, /*this number defines how many requests should have gone to presume that request is timedout if progress doesn't changed*/
      _numberOfErrorRequest:3, /*this number defines how many requests should have gone to check that request throwed an Exception if progress doesn't changed*/
      _buttons:{},
      _browseBtnStyles:{
        def:addButtonClass,
        mouseover:addButtonOnMouseOverClass,
        mousedown:addButtonOnMouseDownClass,
        onfocus:addButtonOnFocusClass
      },
      _statuses:{
        inProgress:fileUpload._transformStatusToFormatted(statusLabelInProgress),
        uploaded:fileUpload._transformStatusToFormatted(statusLabelUploaded),
        sizeLimit:fileUpload._transformStatusToFormatted(statusLabelErrorSize),
        newOne:statusLabelNotUploaded,
        stopped:statusStoppedText,
        failed:statusLabelUnexpectedError
      },
      //variables for focus/blur
      _onFocusWillBe:null,
      _wasFocused:null,
      _shouldInvokeFocusEvHandler:true,
      _shouldInvokeFocusNonModifiable:true,
      _ignoreBlurForChrome:true, // this variable only needed for specific behaviour of focus at Chrome browser
      _activeElementNow:null,
      _ignoreBlurForIE:false,
      _renderAfterUpload:renderAfterUpload,
      _tabIndex:tabIndex,
      _dropTargetCrossoverClass:dropTargetCrossoverClass,
      _externalElForDropTarget: function(){
        if (externalDropTarget) {
          var el = O$(externalDropTarget);
          if (el == null){
            throw "externalDropTarget id is not correct";
          }
          return el;
        }
        return undefined;
      }(),
      _acceptDialogFormats:acceptDialogFormats,
      _directoryDroppedText:directoryDroppedText
    });
  },
  _initGeneralFunctions: function(fileUpload){
    O$.extend(fileUpload,{
      _getNumProperty:function (el, prop) {
        var res = O$.getElementStyle(el, prop).replace("px", "") * 1;
        if (isNaN(res)) {
          return 0;
        }
        return  res;
      },
      _getDropTargetArea:function(id){
        var dropTarget = O$(id);
        if (fileUpload._externalElForDropTarget) {
          dropTarget._isExternal = true;
          new function correctExternalDropArea(externalDiv, area) {
            var padTop = fileUpload._getNumProperty(area, "padding-top");
            var padLeft = fileUpload._getNumProperty(area, "padding-left");
            var borderTopWidth = fileUpload._getNumProperty(area, "border-top-width");
            var borderLeftWidth = fileUpload._getNumProperty(area, "border-left-width");
            var marginLeft = fileUpload._getNumProperty(area, "margin-left");
            var marginRight = fileUpload._getNumProperty(area, "margin-right");
            var marginTop = fileUpload._getNumProperty(area, "margin-top");
            var marginBottom = fileUpload._getNumProperty(area, "margin-bottom");

            var extBorderTopWidth = fileUpload._getNumProperty(externalDiv, "border-top-width");
            var extBorderLeftWidth = fileUpload._getNumProperty(externalDiv, "border-left-width");
            var size = O$.getElementSize(externalDiv);

            area.style.width = (size.width - padLeft - borderTopWidth*2 - extBorderTopWidth*2 - marginLeft-marginRight) + "px";
            area.style.height = (size.height - padTop - borderLeftWidth*2- extBorderLeftWidth*2 - marginTop - marginBottom) + "px";

            externalDiv.style.position = "relative";
            area.parentNode.removeChild(area);
            externalDiv.appendChild(area);
          }(fileUpload._externalElForDropTarget, dropTarget);
        }
        return dropTarget;
      },
      _setDragEventsForBody:function (body, area) {
        if (fileUpload._isBrowserSupportHTML5FileUpload()) {
          O$.addEventHandler(body, "dragover", function (evt) {
            if (fileUpload._isFilesDragged(evt) && !fileUpload._buttons.browseInput.disabled) {
              if (area._isNearest(evt.clientX, evt.clientY)) {
                area._hideAllExceptThis();
                area.show();
                area._isVisible = true;
              }
            }
            fileUpload._cancelDragEvent(evt);
          });
          O$.addEventHandler(body, "drop", function (evt) {
            if (fileUpload._isFilesDragged(evt)) {
              area.hide();
              area._isVisible = false;
            }
            fileUpload._cancelDragEvent(evt);
          });
          O$.addEventHandler(body, "dragexit", fileUpload._cancelDragEvent);

          O$.addEventHandler(window, "mousemove", function () {
            if (area._isVisible) {
              area.hide();
            }
          });
        }
      },

      _setDragEventsForFileUpload:function (area) {
        if (fileUpload._isBrowserSupportHTML5FileUpload()) {
          function dragoverEventHandler(evt) {
            fileUpload._cancelDragEvent(evt);
            if (fileUpload._isFilesDragged(evt)) {
              O$.setStyleMappings(area, {dragover:fileUpload._dropTargetCrossoverClass});
            }
          }
          function dragleaveEventHandler(evt) {
            fileUpload._cancelDragEvent(evt);
            O$.setStyleMappings(area, {dragover:null});
          }
          function dropEventHandler(evt) {
            fileUpload._cancelDragEvent(evt);
            O$.setStyleMappings(area, {dragover:null});
            if (fileUpload._isFilesDragged(evt)) {
              area.hide();
              area._isVisible = false;
              var files = evt.dataTransfer.files;
              var shouldCallOnChange = false;
              for (var i = 0; i < files.length; i++) {
                files[i]._fromDnD = true;
                if (fileUpload._processFileAddingHTML5(files[i])) {
                  shouldCallOnChange = true;
                  if (!fileUpload._multiUpload) {
                    break;
                  }
                }
              }
              if (shouldCallOnChange) {
                fileUpload._events._fireChangeEvent();
                fileUpload._setUploadButtonAfterFileHaveBeenAdded();
              }
            }
          }

          O$.addEventHandler(area, "dragenter", fileUpload._cancelDragEvent);
          O$.addEventHandler(area, "dragleave", dragleaveEventHandler);
          O$.addEventHandler(area, "dragexit", fileUpload._cancelDragEvent);
          O$.addEventHandler(area, "dragover", dragoverEventHandler);
          O$.addEventHandler(area, "drop", dropEventHandler);
        }
      },
      _isFilesDragged:function (evt) {
        if (fileUpload._isBrowserSupportHTML5FileUpload()) {
          if (evt.dataTransfer.files && evt.dataTransfer.files.length > 0)
            return true;
          if (evt.dataTransfer.types)
            return evt.dataTransfer.types[2] == "Files" || evt.dataTransfer.types[0] == "Files";
        }
        return false;
      },
      _cancelDragEvent:function (evt) {
        evt.stopPropagation();
        evt.preventDefault();
      },
      _sendCheckRequest:function (sendDataToEvent) {
        O$.requestComponentPortions(fileUpload.id, ["nothing"],
                JSON.stringify({listOfFilesRequest:"true", idOfFiles:fileUpload._listOfids}),
                function (fileUpload, portionName, portionHTML, portionScripts, portionData) {
                  if (portionData['allUploaded'] == "true") {
                    var fileSizes = portionData['fileSizes'];
                    for (var fileIndex = 0; fileIndex < fileSizes.length; fileIndex++) {
                      var fileApi = fileUpload._getFile(fileSizes[fileIndex][0]);
                      if (fileApi != null && fileApi.size == null) {
                        fileApi.size = fileSizes[fileIndex][1];
                      }
                    }
                    fileUpload._listOfids = [];
                    fileUpload._events._fireEndEvent(sendDataToEvent);
                    fileUpload._renderAfterUploadEnd();
                  } else {
                    setTimeout(function(){
                      fileUpload._sendCheckRequest(sendDataToEvent);
                    }, 500);
                  }
                }, null, true);
      },
      _sendFileRequest:function (file, uri) {
        var xhr = new XMLHttpRequest();
        xhr.open('POST', uri + "&idOfFile=" + file._infoId, true);
        var data = new FormData();
        data.append(file._fakeInput, file);
        xhr.send(data);
        return xhr;
      },
      _isAcceptedTypeOfFile:function (filename) {

        if (fileUpload._typesOfFile == null)
          return true;

        var isAccepted = false;
        fileUpload._typesOfFile.forEach(function (type) {
          if (filename.indexOf(type, filename.length - type.length) !== -1) {
            isAccepted = true;
          }
        });
        if (!isAccepted){
          fileUpload._events._fireWrongFileAddedEvent();
        }
        return isAccepted;
      },
      _setAllEvents:function (onchangeHandler,onstartHandler,onendHandler,
                              onuploadstartHandler,onuploadinprogressHandler, onuploadendHandler,
                              onwrongfileaddedHandler, ondirectorydroppedHandler) {
        function createEventHandler(userHandler, eventName) {
          return function (files) {
            if (userHandler) {
              var event = O$.createEvent(eventName);
              if (files) {
                if (files.length) {
                  event.files = files;
                } else {
                  event.file = files;
                }
              }
              userHandler(event);
            }
          }
        }

        fileUpload._events._fireChangeEvent = createEventHandler(onchangeHandler, "onchange");
        fileUpload._events._fireStartEvent = createEventHandler(onstartHandler, "onstart");
        fileUpload._events._fireEndEvent = createEventHandler(onendHandler, "onend");
        fileUpload._events._fireUploadStartEvent = createEventHandler(onuploadstartHandler, "onuploadstart");
        fileUpload._events._fireUploadInProgressEvent = createEventHandler(onuploadinprogressHandler, "onuploadinprogress");
        fileUpload._events._fireUploadEndEvent = createEventHandler(onuploadendHandler, "onuploadend");
        fileUpload._events._fireWrongFileAddedEvent = function () {
          if (onwrongfileaddedHandler) {
            var event = O$.createEvent("onwrongfileadded");
            event.allowedTypes = (fileUpload._typesOfFile != null) ? fileUpload._typesOfFile : "*";
            onwrongfileaddedHandler(event);
          } else {
            alert("Wrong type of file");
          }
        };
        fileUpload._events._fireDirectoryDroppedEvent = function(){
          if (ondirectorydroppedHandler) {
            var event = O$.createEvent("ondirectorydropped");
            ondirectorydroppedHandler(event);
          } else {
            alert(fileUpload._directoryDroppedText);
          }
        };


        //processing onfocus/onblur event
        fileUpload._events._fireBlurEvent = createEventHandler(fileUpload.onblur, "onblur");
        fileUpload.removeAttribute("onblur");
        fileUpload.onblur = null;

        fileUpload._events._fireFocusEvent = createEventHandler(fileUpload.onfocus, "onfocus");
        fileUpload.removeAttribute("onfocus");
        fileUpload.onfocus = null;

      },
      _sendInformThatRequestFailed:function (fileId) {
        O$.requestComponentPortions(fileUpload.id, ["nothing"],
                JSON.stringify({informFailedRequest:true, uniqueIdOfFile:fileId}),
                function (fileUpload, portionName, portionHTML, portionScripts, portionData) {
                }, null, true);
      },
    _setStatusforFileWithId:function (id, status) {
      for (var k = 0; k < fileUpload._listOfids.length; k++) {
        if (id == fileUpload._listOfids[k][0]) {
          fileUpload._listOfids[k][3] = status;
          break;
        }
      }
    },
      _getFileName:function (fullRoorOfFile) {
      var index = fullRoorOfFile.lastIndexOf('\\');
      return fullRoorOfFile.substring(index + 1);
    },
      _addFileToInfoFileList:function(form){
        var file = [];
        file.push(form.childNodes[0]._uniqueId);//id
        file.push(form.childNodes[0].name);//name of file input
        file.push(encodeURIComponent(fileUpload._getFileName(form.childNodes[0].value)));//filename
        file.push("IN_PROGRESS");//status
        file.push(form.childNodes[0]._idInputAndDiv);//client id of div
        fileUpload._listOfids.push(file);
      },
      _addFileToInfoFileListHTML5:function(html5File){
        var file = [];
        file.push(html5File._uniqueId);//id
        file.push(html5File._fakeInput);//name of file input
        file.push(encodeURIComponent(fileUpload._getFileName(html5File.name)));//filename
        file.push("IN_PROGRESS");//status
        file.push(html5File._infoId);//client id of div
        fileUpload._listOfids.push(file);
      },
      _callProgressRequest:function (fileInput, endHandler) {
        setTimeout(function () {
          fileUpload._progressRequest(fileInput, endHandler);
        }, 500);
      },
      _callFileProgressForHTML5Request:function (file, request, endHandler) {
        setTimeout(function () {
          fileUpload._progressHTMl5Request(file, request, endHandler);
        }, 500);
      },
      _getDocumentURI:function () {
        var docURL = document.URL;
        if (docURL.indexOf("&") == -1) {
          return docURL + "?uniqueID=" + fileUpload._ID;
        } else {
          return docURL + "&uniqueID=" + fileUpload._ID;
        }

      },
    /*Because of imperfect value of file's size when directory is chosen (I got values 4096*x where x is between 1 - 8)
     this method doesn't fully guarantee that file is directory.
     * */
       _isDirectory:function (file) {
         if (file.size == 0){
           fileUpload._events._fireDirectoryDroppedEvent();
           return true;
         }
        if (file.type != "") {
          return false;
        }
        if (file.size % 4096 == 0) {
          var koef = file.size / 4096;
          if (koef > 0 && koef < 10) {
            var index = file.name.lastIndexOf(".");
            if (index == -1) {
              fileUpload._events._fireDirectoryDroppedEvent();
              return true;
            }
            var extension = file.name.substr(index + 1);
            if (!(extension.length >= 3 && extension.match(/^[a-zA-Z]+$/))) {
              fileUpload._events._fireDirectoryDroppedEvent();
              return true;
            }

          }
        }
        return false;
      },
      _createInputInAddBtn:function () {
        var input = document.createElement("input");
        input.setAttribute("type", "file");
        if (fileUpload._multiUpload && fileUpload._isBrowserSupportHTML5FileUpload()){
          input.setAttribute("multiple", "");
        }
        if (fileUpload._acceptDialogFormats){
          input.setAttribute("accept", fileUpload._acceptDialogFormats);
        }
        //todo move somewhere else these styles
        input.style.overflow = "hidden";
        input.style.position = "absolute";
        input.style.top = "0";
        input.style.right = "0";
        input.style.fontSize = "100px";
        input.style.opacity = "0";
        input.style.filter = "alpha(opacity=0)";
        input.style.sFilter = "progid:DXImageTransform.Microsoft.Alpha(Opacity=0)";

        if (fileUpload._tabIndex != -1) {
          input.setAttribute("tabindex", fileUpload._tabIndex);
        }
        O$.addEventHandler(input, "focus", function () {
          if (!fileUpload._buttons.browseInput.disabled && !fileUpload._buttons.browse._afterMouseDown) {
            O$.setStyleMappings(fileUpload._buttons.browseTitleInput, {focused:fileUpload._browseBtnStyles.onfocus});
          }
          fileUpload._buttons.browse._inFocus = true;
        });
        O$.addEventHandler(input, "blur", function () {
          if (!fileUpload._buttons.browseInput.disabled) {
            O$.setStyleMappings(fileUpload._buttons.browseTitleInput, {focused:null});
            fileUpload._buttons.browse._inFocus = false;
          }
        });
        O$.addEventHandler(input, "change", function (evt) {
          fileUpload._shouldInvokeFocusEvHandler = false;
          if (fileUpload._isMultipleFileSelectSupported(evt)) {
            var files = evt.target.files;
            var shouldCallOnChange = false;
            for (var fileIndex = 0; fileIndex < files.length; fileIndex++) {
              if (fileUpload._processFileAddingHTML5(files[fileIndex])) {
                shouldCallOnChange = true;
                if (!fileUpload._multiUpload) {
                  break;
                }
              }
            }
            if (shouldCallOnChange) {
              fileUpload._events._fireChangeEvent();
              fileUpload._setUploadButtonAfterFileHaveBeenAdded();
            }
          } else {
            var isAdded = fileUpload._processFileAdding(input);
          }
          if (isAdded) {
            setTimeout(function () {
              if (O$.isChrome() || O$.isSafari())
                fileUpload._shouldInvokeFocusEvHandler = true;
              fileUpload._setFocusOnComponent();
              if (O$.isExplorer()) {
                fileUpload._shouldInvokeFocusEvHandler = false;
                fileUpload._ignoreBlurForIE = true;
              }
            }, 1);
          } else {
            if (O$.isChrome() || O$.isSafari()) {
              fileUpload._shouldInvokeFocusEvHandler = true;
              fileUpload._setFocusOnComponent();
            }
          }
        });
        if (O$.isChrome() || O$.isSafari()) { //chrome calls blur when file dialog popup
          O$.addEventHandler(input, "mousedown", function () {
            input.focus();
          });
        }
        return input;
      },
      _setUpBrowseButton:function(addButtonId){
        fileUpload._buttons.browse = O$(addButtonId);

        fileUpload._buttons.browseDivForInput = O$(addButtonId+"::forInput");
        fileUpload._buttons.browseInput = fileUpload._createInputInAddBtn();
        var addButtonTitle = O$(addButtonId + "::title");
        fileUpload._buttons.browseTitleInput = addButtonTitle.lastChild;
        fileUpload._setStylesForAddButton(fileUpload._buttons.browse);
        if (O$.isExplorer() && O$.isQuirksMode()){
          fileUpload._buttons.browseDivForInput.style.height = O$.getElementSize(addButtonTitle).height;
        }
        fileUpload._buttons.browseTitleInput.disabled = false;
        fileUpload._buttons.browseDivForInput.appendChild(fileUpload._buttons.browseInput);//create first input
      },
      _getFacet:function (id) {
        var divForFacet = O$(fileUpload._elementsCont.id + id);
        fileUpload._elementsCont.removeChild(divForFacet);
        var facet = divForFacet.lastChild;
        facet.disabled = false;
        return facet;
      },
      _renderAfterUploadEnd : function(){
        if (fileUpload._renderAfterUpload){
          O$._ajaxReload([fileUpload._renderAfterUpload], {"executeRenderedComponents":true,"immediate":false});
        }
      },
      _transformStatusToFormatted:function(statusText){
        function updateStatus(uploaded, size) {
          var text = this.text;
          if (size != "") {
            text = text.replace("{size}", (size / Math.pow(2, this.pow)).toFixed(2));
          } else {
            text = text.replace("{size}", size);
          }
          if (uploaded != null) {
            text = text.replace("{uploaded}", (uploaded / Math.pow(2, this.pow)).toFixed(2));
          }
          return text;
        }
        var sizeDimensions = [
          {title:"{KB}",
            pow:10},
          {title:"{MB}",
            pow:20},
          {title:"{B}",
            pow:1}
        ];
        for (var dimIndex = 0; dimIndex < sizeDimensions.length; dimIndex++) {
          var index = statusText.indexOf(sizeDimensions[dimIndex].title);
          if (index != -1) {
            var modified = statusText.replace(sizeDimensions[dimIndex].title, "");
            return {text:modified, pow:sizeDimensions[dimIndex].pow, _update:updateStatus};
          }
        }
        return {text:statusText, pow:1, _update:updateStatus};
      },
      _createStructureForInputs:    function (){
        var allInputsForFiles = document.createElement("div");
        allInputsForFiles.setAttribute("id", fileUpload.id + "::inputs");
        allInputsForFiles.style.display = "none";
        var body = document.getElementsByTagName('body')[0];
        body.appendChild(allInputsForFiles);
        return allInputsForFiles;
      },
      _createAndAppendComplexInputWithId:    function (inputField) {
        var divForFileInput = document.createElement("div");
        divForFileInput.setAttribute("id", fileUpload._inputsStorage.id + "::input" + inputField._idInputAndDiv);

        var formForInput = document.createElement("form");
        formForInput.setAttribute("id", divForFileInput.id + "::form");
        formForInput.setAttribute("method", "POST");
        if (O$.isExplorer() &&
                (O$.isQuirksMode()
                        || ((O$.isExplorer6() ||O$.isExplorer7() || O$.isExplorer8() || O$.isExplorer9()) &&
                        (document.documentMode == 7 || document.documentMode == 6)))){
          formForInput.setAttribute("encoding", "multipart/form-data");
        } else {
          formForInput.setAttribute("enctype", "multipart/form-data");
        }
        inputField._uniqueId = fileUpload._ID + inputField._idInputAndDiv;

        inputField.setAttribute("id", formForInput.id + "::fileInput");
        inputField.setAttribute("name", inputField.id);

        inputField.removeAttribute("tabindex");
        formForInput.appendChild(inputField);

        var iframe;
        if (O$.isExplorer() &&
                (O$.isQuirksMode()
                        ||  ((O$.isExplorer6() ||O$.isExplorer7() || O$.isExplorer8() || O$.isExplorer9()) &&
                        (document.documentMode == 7 || document.documentMode == 6)))) {
          iframe = document.createElement('<iframe name="' + formForInput.id + "::iframe" + '">');
          iframe.width = 0;
          iframe.height = 0;
          iframe.marginHeight = 0;
          iframe.marginWidth = 0;
        }
        else {
          iframe = document.createElement("iframe");
          iframe.name = formForInput.id + "::iframe";
        }

        formForInput.appendChild(iframe);

        divForFileInput.appendChild(formForInput);

        fileUpload._inputsStorage.appendChild(divForFileInput);
        return divForFileInput;
      },
      _setStylesForAddButton:    function (addButton){
        O$.setStyleMappings(fileUpload._buttons.browseTitleInput, {std : fileUpload._browseBtnStyles.def});
        O$.addEventHandler(addButton,"mouseover",function(){
          if (!fileUpload._buttons.browseInput.disabled) {
            O$.setStyleMappings(fileUpload._buttons.browseTitleInput, {mouseover : fileUpload._browseBtnStyles.mouseover});
          }
        });
        O$.addEventHandler(addButton, "mouseout", function() {
          if (!fileUpload._buttons.browseInput.disabled) {
            O$.setStyleMappings(fileUpload._buttons.browseTitleInput, {mouseover : null});
          }
        });

        O$.addEventHandler(addButton,"mousedown",function(){
          if (!fileUpload._buttons.browseInput.disabled){
            O$.setStyleMappings(fileUpload._buttons.browseTitleInput, {mousedown : fileUpload._browseBtnStyles.mousedown});
          }
          addButton._afterMouseDown = true;
        });
        O$.addEventHandler(window,"mouseup",function mouseUpHandler(){
          function inTheDOM(element) {
            var parent = element.parentNode;
            while (parent != null && parent != document) {
              parent = parent.parentNode;
            }
            return (parent == document);
          }

          if (inTheDOM(fileUpload)) {
            if (!fileUpload._buttons.browseInput.disabled) {
              O$.setStyleMappings(fileUpload._buttons.browseTitleInput, {mousedown:null});
            }

            addButton._afterMouseDown = false;
          } else {
            O$.removeEventHandler(window, "mouseup", mouseUpHandler);
          }
        });
      },
      _focusHandler:    function () {
        var _this = this;
        if (fileUpload._activeElementNow == _this){
          return;

        }
        if (!fileUpload._shouldInvokeFocusEvHandler){
          fileUpload._shouldInvokeFocusEvHandler = true;
          return;
        }
        if (!fileUpload._shouldInvokeFocusNonModifiable){
          return;
        }
        fileUpload._activeElementNow = _this;
        if (fileUpload._wasFocused != null) {
          //previously was focused our element
        } else {
          fileUpload._events._fireFocusEvent();
        }               //2

        setTimeout(function() {
          fileUpload._onFocusWillBe = _this;     //4
        }, 1);
      },
      _blurHandler:    function () {
        if (!fileUpload._shouldInvokeFocusEvHandler){
          fileUpload._shouldInvokeFocusEvHandler = true;
          return;
        }
        if ((O$.isChrome() || O$.isSafari()) && !fileUpload._ignoreBlurForChrome){
          fileUpload._ignoreBlurForChrome = true;
          return;
        }
        if ((O$.isChrome() || O$.isSafari()) && !fileUpload._shouldInvokeFocusNonModifiable){
          return;
        }

        fileUpload._wasFocused = this;   //1

        fileUpload._activeElementNow = null;
        var _this = this;
        setTimeout(function() {
          fileUpload._wasFocused = null;
          fileUpload._onFocusWillBe = null;//3
          setTimeout(function() {
            if (fileUpload._onFocusWillBe != null) {//5
              //next will be focused our element
            } else {
              if (O$.isExplorer() && fileUpload._ignoreBlurForIE) { //added for explorer - because of specific processing of adding file in input
                fileUpload._ignoreBlurForIE = false;
                return;
              }
              fileUpload._events._fireBlurEvent();
            }

          }, 1);
        }, 1);

      },
      _isMultipleFileSelectSupported:    function (evt){
        if (fileUpload._isBrowserSupportHTML5FileUpload()){
          if (evt.target.files)
            return true;
        }
        return false;
      },
      _isBrowserSupportHTML5FileUpload:    function (){
        //Opera doesn't support file upload API , although it support multiupload
        //IE doesn't support at all
        //Safari has File Upload API, but with bugs. No solution for the moment. Look here: http://stackoverflow.com/questions/7231054/file-input-size-issue-in-safari-for-multiple-file-selection
        //Safari anytime can get not correct fileNames , so this feature is turned of, if Safari is  used.
        return !((!O$.isChrome() && O$.isSafari()) || typeof(window.FormData) == "undefined");
      },
      _chromeAndSafariFocusFix:    function (button){
        if (O$.isChrome() || O$.isSafari()) {
          O$.addEventHandler(button, "mousedown", function () {
            fileUpload._shouldInvokeFocusNonModifiable = false;
          });
          O$.addEventHandler(button, "mouseup", function () {
            fileUpload._shouldInvokeFocusNonModifiable = true;
          });
          O$.addEventHandler(button, "keyup", function () {
            fileUpload._shouldInvokeFocusEvHandler = false;
          });
        }
      }
    });
  },
  _initDragArea:function(area){
    O$.extend(area.parentNode, {
      _findPosition:function () {
        var curtop = 0;
        var curleft = 0;
        var obj = this;
        if (obj.offsetParent) {
          curleft = obj.offsetLeft;
          curtop = obj.offsetTop;
          while (obj = obj.offsetParent) {
            curleft += obj.offsetLeft;
            curtop += obj.offsetTop;
          }
        }
        return [curleft, curtop];
      }
    });
    O$.extend(area, {
      _hideAllExceptThis:function () {
        for (var i = 0; i < O$._dropAreas.length; i++) {
          if (O$._dropAreas[i] != this) {
            O$._dropAreas[i].hide();
            O$._dropAreas[i]._isVisible = false;
          }
        }
      },
      _isNearest:function (clientX, clientY) {

        var ourDistance = this._getDistanceTo(clientX, clientY);
        var alienDistance;
        for (var i = 0; i < O$._dropAreas.length; i++) {
          if (O$._dropAreas[i] != this) {
            alienDistance = O$._dropAreas[i]._getDistanceTo(clientX, clientY);
            if (alienDistance < ourDistance) {
              return false;
            }
          }
        }
        return true;
      },
      _getDistanceTo:function (x, y) {
        var areasXY = this.parentNode._findPosition();
        if (areasXY[0] < x && areasXY[1] < y) {
          return x - areasXY[0] + y - areasXY[1];
        } else if (areasXY[0] >= x && areasXY[1] >= y) {
          return areasXY[0] - x + areasXY[1] - y;
        } else if (areasXY[0] >= x && areasXY[1] < y) {
          return areasXY[0] - x + y - areasXY[1];
        } else if (areasXY[0] < x && areasXY[1] >= y) {
          return x - areasXY[0] + areasXY[1] - y;
        }
      }});
  },
  File:O$.createClass(null,{
    constructor:function (initSpecAPIFunc, component, id, name, status, size) {
      this._component = component;
      this._id = id;
      this.name = name;
      this.status = status;
      this.size = size;
      this.progress = null;
      O$.FileUploadUtil._initFileAPI(this);
      initSpecAPIFunc(this);
    }
  }),
  _initFileAPI:function(file){
    O$.extend(file,{
      getName:function(){
        return file.name;
      },
      getStatus:function(){
        return file.status;
      },
      getSize:function(){
        return file.size;
      },
      getProgress:function(){
        return file.progress;
      }
    });
  },
  Status:{
    NEW:{value:"New"},
    IN_PROGRESS:{value:"In progress"},
    SUCCESSFUL:{value:"Successful"},
    FAILED:{value:"Failed"},
    STOPPED:{value:"Stopped"},
    SIZE_LIMIT_EXCEEDED:{value:"Size limit exceed"}
  }
};

