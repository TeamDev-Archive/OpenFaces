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
                        render,tabIndex,dropTargetCrossoverClass,acceptedMimeTypes,
                        directoryDroppedText, wrongFileTypeText, externalDropTargetId) {
    O$.FileUploadUtil._initGeneralFunctions(fileUpload);
    O$.FileUploadUtil._initGeneralFields(fileUpload,
            lengthAlreadyUploadedFiles, acceptedTypesOfFile, isDisabled, ID,
            addButtonClass, addButtonOnMouseOverClass, addButtonOnMouseDownClass,addButtonOnFocusClass,
            statusLabelInProgress,statusLabelUploaded,statusLabelErrorSize,
            statusLabelNotUploaded,statusStoppedText,statusLabelUnexpectedError,
            render,tabIndex,dropTargetCrossoverClass, acceptedMimeTypes,
            directoryDroppedText, wrongFileTypeText, externalDropTargetId);
  },
  _initGeneralFields:function (fileUpload,
                               lengthAlreadyUploadedFiles, acceptedTypesOfFile, isDisabled, ID,
                               addButtonClass, addButtonOnMouseOverClass, addButtonOnMouseDownClass,addButtonOnFocusClass,
                               statusLabelInProgress,statusLabelUploaded,statusLabelErrorSize,
                               statusLabelNotUploaded,statusStoppedText,statusLabelUnexpectedError,
                               render, tabIndex, dropTargetCrossoverClass, acceptedMimeTypes,
                               directoryDroppedText, wrongFileTypeText, externalDropTargetId) {
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
      _render:render,
      _tabIndex:tabIndex,
      _dropTargetCrossoverClass:dropTargetCrossoverClass,
      _acceptedMimeTypes:acceptedMimeTypes,
      _directoryDroppedText:directoryDroppedText,
      _wrongFileTypeText:wrongFileTypeText,
      _externalDropTargetId:externalDropTargetId
    });
  },
  _initGeneralFunctions: function(fileUpload) {
    O$.extend(fileUpload,{
      _setupExternalDropTarget: function() {
        if (fileUpload._externalDropTargetId) {
          var el = O$(fileUpload._externalDropTargetId);
          if (el == null) {
            throw "externalDropTarget id '" +fileUpload._externalDropTargetId + "' is not correct";
          }
          fileUpload._externalElForDropTarget = el;
          return;
        }
        fileUpload._externalElForDropTarget = undefined;
      },
      _getNumProperty:function (el, prop) {
        var res = O$.getElementStyle(el, prop).replace("px", "") * 1;
        if (isNaN(res)) {
          return 0;
        }
        return  res;
      },
      _getDropTargetArea:function(id) {
        var dropTarget = O$(id);
        if (fileUpload._externalElForDropTarget) {
          if (dropTarget == null) {
            dropTarget = O$(fileUpload.id + "::externalDropTargetSkeleton");
          }
          dropTarget = new function correctExternalDropArea(externalDiv, area) {
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

            area.style.width = (size.width - padLeft - borderTopWidth * 2 - extBorderTopWidth * 2 - marginLeft - marginRight) + "px";
            area.style.height = (size.height - padTop - borderLeftWidth * 2 - extBorderLeftWidth * 2 - marginTop - marginBottom) + "px";

            externalDiv.style.position = "relative";

            var remoteDropTarget = area.cloneNode(true);
            if (!fileUpload._externalDropTargetskeleton) {
              fileUpload._externalDropTargetskeleton = area;
              area.id = fileUpload.id + "::externalDropTargetSkeleton";
            }
            remoteDropTarget._isExternal = true;
            externalDiv.appendChild(remoteDropTarget);
            return remoteDropTarget;
          }(fileUpload._externalElForDropTarget, dropTarget);
        }
        return dropTarget;
      },
      _setDragEventsForBody:function (body, area) {
        if (fileUpload._isBrowserSupportHTML5FileUpload()) {
          O$.addEventHandler(body, "dragover", function dragOverHandler(evt) {
            if (fileUpload._isFilesDragged(evt) && !fileUpload._buttons.browseInput.disabled) {
              if (fileUpload._removeOddDragHandler(body, "dragover", dragOverHandler, area)) {
                fileUpload._initializeDropTarget();
              } else {
                if (area._isNearest(evt.clientX, evt.clientY) ) {
                  area._hideAllExceptThis();
                  area.show();
                  area._isVisible = true;
                }
              }
            }
            fileUpload._cancelDragEvent(evt);
          });
          O$.addEventHandler(body, "drop", function dropEvHandler(evt) {
            if (!fileUpload._removeOddDragHandler(body, "drop", dropEvHandler, area)) {
              if (fileUpload._isFilesDragged(evt)) {
                area.hide();
                area._isVisible = false;
              }
            }
            fileUpload._cancelDragEvent(evt);
          });
          O$.addEventHandler(body, "dragexit", function dragExitHandler(evt) {
            fileUpload._removeOddDragHandler(body, "dragexit", dragExitHandler, area);
            fileUpload._cancelDragEvent(evt);
          });

          O$.addEventHandler(window, "mousemove", function mouseMoveHandler() {
            if (!fileUpload._removeOddDragHandler(window, "mousemove", mouseMoveHandler, area)) {
              if (area._isVisible) {
                area.hide();
              }
            }
          });
        }
      },
      _removeOddDragHandler: function(el, eventName, dragHandler, area) {
        if (!area) {
          area = el;
        }
        if (area._isExternal && !O$.FileUploadUtil._inTheDOM(area)) {
          area._remove();
          O$.removeEventHandler(el, eventName, dragHandler);
          return true;
        }
        return false;
      },
      _setDragEventsForFileUpload:function (area) {
        if (fileUpload._isBrowserSupportHTML5FileUpload()) {

          O$.addEventHandler(area, "dragenter", function dragEnterHandler(evt) {
            fileUpload._removeOddDragHandler(area, "dragenter", dragEnterHandler);
            fileUpload._cancelDragEvent(evt);
          });
          O$.addEventHandler(area, "dragleave", function dragleaveEventHandler(evt) {
            if (!fileUpload._removeOddDragHandler(area, "dragleave", dragleaveEventHandler)) {
              O$.setStyleMappings(area, {dragover:null});
            }
            fileUpload._cancelDragEvent(evt);
          });
          O$.addEventHandler(area, "dragexit", function dragExitHandler(evt) {
            fileUpload._removeOddDragHandler(area, "dragexit", dragExitHandler);
            fileUpload._cancelDragEvent(evt);
          });
          O$.addEventHandler(area, "dragover", function dragoverEventHandler(evt) {
            if (!fileUpload._removeOddDragHandler(area, "dragover", dragoverEventHandler)) {
              if (fileUpload._isFilesDragged(evt)) {
                O$.setStyleMappings(area, {dragover:fileUpload._dropTargetCrossoverClass});
              }
            }
            fileUpload._cancelDragEvent(evt);
          });
          O$.addEventHandler(area, "drop",function dropEventHandler(evt) {
            if (!fileUpload._removeOddDragHandler(area, "drop", dropEventHandler)) {
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
                  fileUpload._restoreBorder();
                  fileUpload._showPopupIfPopupNeeded();
                }
              }
              fileUpload._cancelDragEvent(evt);
            }
          });
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
        O$.Ajax.requestComponentPortions(fileUpload.id, ["nothing"],
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
                    setTimeout(function() {
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
        if (!isAccepted) {
          fileUpload._events._fireWrongFileTypeEvent();
        }
        return isAccepted;
      },
      _setAllEvents:function (onchangeHandler,onstartHandler,onendHandler,
                              onfilestartHandler,onfileinprogressHandler, onfileendHandler,
                              onwrongfiletypeHandler, ondirectorydroppedHandler) {
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
        fileUpload._events._fireFileStartEvent = createEventHandler(onfilestartHandler, "onfilestart");
        fileUpload._events._fireFileInProgressEvent = createEventHandler(onfileinprogressHandler, "onfileinprogress");
        fileUpload._events._fireFileEndEvent = createEventHandler(onfileendHandler, "onfileend");
        fileUpload._events._fireWrongFileTypeEvent = function () {
          var event = O$.createEvent("onwrongfiletype");
          event.allowedTypes = (fileUpload._typesOfFile != null) ? fileUpload._typesOfFile : "*";
          var callDefault = true;
          if (onwrongfiletypeHandler) {
            callDefault  = !(onwrongfiletypeHandler(event) == false);
          }
          if (callDefault) {
            alert(fileUpload._wrongFileTypeText);
          }
        };
        fileUpload._events._fireDirectoryDroppedEvent = function() {
          var event = O$.createEvent("ondirectorydropped");
          var callDefault = true;
          if (ondirectorydroppedHandler) {
            callDefault =!(ondirectorydroppedHandler(event) == false);
          }
          if (callDefault) {
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
      _addFileToInfoFileList:function(form) {
        var file = [];
        file.push(form.childNodes[0]._uniqueId);//id
        file.push(form.childNodes[0].name);//name of file input
        file.push(encodeURIComponent(fileUpload._getFileName(form.childNodes[0].value)));//filename
        file.push("IN_PROGRESS");//status
        file.push(form.childNodes[0]._idInputAndDiv);//client id of div
        fileUpload._listOfids.push(file);
      },
      _addFileToInfoFileListHTML5:function(html5File) {
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
        if (!this._documentURL) {
          var form = O$.getParentNode(this, "FORM");
          if (!form) form = document.forms[0];

          var encodedUrlField = form.elements["javax.faces.encodedURL"];
          var url = encodedUrlField ? encodedUrlField.value : form.action;

          if (url.indexOf("?") == -1) {
            url = url + "?uniqueID=" + fileUpload._ID;
          } else {
            url = url + "&uniqueID=" + fileUpload._ID;
          }

          this._documentURL = url;
        }
        return this._documentURL;

      },
    /*Because of imperfect value of file's size when directory is chosen (I got values 4096*x where x is between 1 - 8)
     this method doesn't fully guarantee that file is directory.
     * */
       _isDirectory:function (file) {
         if (file.size == 0) {
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
        if (fileUpload._multiUpload && fileUpload._isBrowserSupportHTML5FileUpload()) {
          input.setAttribute("multiple", "");
        }
        if (fileUpload._acceptedMimeTypes) {
          input.setAttribute("accept", fileUpload._acceptedMimeTypes);
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
          fileUpload._restoreBorder();
          fileUpload._showPopupIfPopupNeeded();
        });
        if (O$.isChrome() || O$.isSafari()) { //chrome calls blur when file dialog popup
          O$.addEventHandler(input, "mousedown", function () {
            input.focus();
          });
        }
        return input;
      },
      _setUpBrowseButton:function(addButtonId) {
        fileUpload._buttons.browse = O$(addButtonId);

        fileUpload._buttons.browseDivForInput = O$(addButtonId+"::forInput");
        fileUpload._buttons.browseInput = fileUpload._createInputInAddBtn();
        fileUpload._buttons.browseTitle = O$(addButtonId + "::title");
        fileUpload._buttons.browseTitleInput = fileUpload._buttons.browseTitle.lastChild;
        fileUpload._setStylesForAddButton(fileUpload._buttons.browse);
        if (O$.isExplorer() && O$.isQuirksMode()) {
          fileUpload._buttons.browseDivForInput.style.height = O$.getElementSize(fileUpload._buttons.browseTitle).height;
        }
        fileUpload._buttons.browseTitleInput.disabled = false;
        fileUpload._buttons.browseDivForInput.appendChild(fileUpload._buttons.browseInput);//create first input
      },
      _setUpExternalBrowseButton:function (externalBrowseButtonId) {
        fileUpload._externalBrowseButtonId = externalBrowseButtonId;
        var browseButton = fileUpload._buttons.browseDivForInput;
        browseButton._updatePosition = function () {
          var externalBrowseButton = O$(fileUpload._externalBrowseButtonId);
          var currentPosition = O$.getElementPos(externalBrowseButton, true);
          if (browseButton._oldPosition && currentPosition.x == browseButton._oldPosition.x && currentPosition.y == browseButton._oldPosition.y) {
            return;
          }
          browseButton._oldPosition = O$.getElementPos(externalBrowseButton, true);
          if (!externalBrowseButton)
            O$.removeEventHandler(externalBrowseButton, "mouseover", browseButton._updatePosition);
          externalBrowseButton.parentNode.insertBefore(browseButton, externalBrowseButton);
          var size = O$.getElementSize(externalBrowseButton);
          browseButton.style.width = size.width + "px";
          browseButton.style.height = size.height + "px";
          browseButton.style.position = "absolute";
          O$.alignPopupByElement(browseButton, externalBrowseButton, "center", "center");
        };
        O$.addLoadEvent(function () {
          var externalBrowseButton = O$(externalBrowseButtonId);
          if (!externalBrowseButton)
            throw "Element with ID " + externalBrowseButtonId + " cannot be found.";
          O$.addEventHandler(externalBrowseButton, "mouseover", browseButton._updatePosition);
          O$.addEventHandler(browseButton, "mouseover", browseButton._updatePosition);
        });
      },
      _moveExternalBrowseButtonClickArea:function (newExternalBrowseButtonId){
        O$(fileUpload._externalBrowseButtonId).disabled = true;
        var newExternalBrowseButton = O$(newExternalBrowseButtonId);
        if (!newExternalBrowseButton)
          throw "Element with ID " + newExternalBrowseButtonId + " cannot be found.";
        fileUpload._externalBrowseButtonId = newExternalBrowseButtonId;
        O$.correctElementZIndex(fileUpload._buttons.browseDivForInput, newExternalBrowseButton);
        fileUpload._buttons.browseDivForInput._updatePosition();
        O$(newExternalBrowseButtonId).disabled = false;
      },
      _showPopup:function (popupPositionedByID, horizAlignment, vertAlignment, horizDistance, vertDistance) {
        if (fileUpload._popupPositionedByElement == undefined){
          if (!popupPositionedByID){
            fileUpload._popupPositionedByElement = fileUpload._buttons.browse;
          } else {
            fileUpload._popupPositionedByElement = O$(popupPositionedByID);
            if (!fileUpload._popupPositionedByElement){
              throw "Position By element id '" + popupPositionedByID + "' is not found";
            }
          }
        }
        fileUpload.style.visibility = "visible";
        O$.alignPopupByElement(fileUpload, fileUpload._popupPositionedByElement, horizAlignment, vertAlignment, horizDistance, vertDistance);
        fileUpload._popupIsShown = true;
      },
      _hidePopup:function () {
        fileUpload.style.visibility = "hidden";
        fileUpload._popupIsShown = false;
      },
      _initCloseButton:function(componentId){
        fileUpload._actionButtonContainer.appendChild(fileUpload._buttons.closePopup);
        fileUpload._buttons.closePopup.onclick = function (e) {
          fileUpload._hidePopup();
        };
      },
      _moveBrowseButtonOutOfComponent:function () {
        fileUpload.parentNode.insertBefore(fileUpload._buttons.browse, fileUpload);
        if (!!fileUpload._buttons.fakeBrowseTitleInput){
          fileUpload._buttons.fakeBrowseTitleInput.style.display = "none";
        }
      },
      _moveBrowseButtonBackToComponent:function () {
        fileUpload._addButtonParent.insertBefore(fileUpload._buttons.browse, fileUpload._addButtonParent.firstChild);
        if (!fileUpload._buttons.fakeBrowseTitleInput) {
          fileUpload._buttons.fakeBrowseTitleInput = fileUpload._buttons.browseTitleInput.cloneNode(true);
          // TODO remove all IDS
          fileUpload._buttons.fakeBrowseTitleInput.id = "";
          fileUpload._setButtonSizeAsInBrowse(fileUpload._buttons.fakeBrowseTitleInput);
          fileUpload.parentNode.insertBefore(fileUpload._buttons.fakeBrowseTitleInput, fileUpload);
          fileUpload._buttons.fakeBrowseTitleInput.disabled = "disabled";
        }
        fileUpload._buttons.fakeBrowseTitleInput.style.display = "block";
      },
      _initPopup:function () {
        fileUpload._hidePopup();
        fileUpload.style.position = "absolute";
      },
      _hideBorder:function () {
        fileUpload.style.border = "0 none transparent";
        fileUpload._hiddenBorder = true;
      },
      _restoreBorder:function () {
        // restore default style border
        if (fileUpload._hiddenBorder)
          fileUpload.style.border = "";
        fileUpload._hiddenBorder = false;
      },
      _setButtonSizeAsInBrowse:function (button) {
        var browseContainer = O$.getElementSize(fileUpload._buttons.browse);
        var titleInput = O$.getElementSize(fileUpload._buttons.browseTitleInput);

        button.style.width = titleInput.width + "px";
        var margin = (browseContainer.width - titleInput.width) / 2;
        button.style.marginLeft = margin + "px";
        button.style.marginRight = margin + "px";

        button.style.height = titleInput.height + "px";
        var marginTopBottom = (browseContainer.height - titleInput.height) / 2;
        button.style.marginTop = marginTopBottom + "px";
        button.style.marginBottom = marginTopBottom + "px";
      },
      _getFacet:function (id) {
        var divForFacet = O$(fileUpload._elementsCont.id + id);
        fileUpload._elementsCont.removeChild(divForFacet);
        var facet = divForFacet.lastChild;
        facet.disabled = false;
        return facet;
      },
      _renderAfterUploadEnd : function() {
        if (fileUpload._render) {
          if (fileUpload._render.length > 0) {
            O$.Ajax._reload(fileUpload._render, {"executeRenderedComponents":true,"immediate":false});
          }
        }
      },
      _transformStatusToFormatted:function(statusText) {
        function updateStatus(uploaded, size) {
          var text = this.text;
          if (size != "") {
            text = text.replace("{size}", Math.ceil((size / Math.pow(2, this.pow))));
          } else {
            text = text.replace("{size}", size);
          }
          if (uploaded != null) {
            text = text.replace("{uploaded}", Math.ceil(uploaded / Math.pow(2, this.pow)));
          }
          return text;
        }
        var sizeDimensions = [
          {title:"[KB]",
            pow:10},
          {title:"[MB]",
            pow:20},
          {title:"[B]",
            pow:0}
        ];
        for (var dimIndex = 0; dimIndex < sizeDimensions.length; dimIndex++) {
          var index = statusText.indexOf(sizeDimensions[dimIndex].title);
          if (index != -1) {
            var modified = statusText.replace(sizeDimensions[dimIndex].title, "");
            return {text:modified, pow:sizeDimensions[dimIndex].pow, _update:updateStatus};
          }
        }
        return {text:statusText, pow:10, _update:updateStatus};
      },
      _createStructureForInputs:    function () {
        var allInputsForFiles = document.createElement("div");
        allInputsForFiles.setAttribute("id", fileUpload.id + "::inputs");
        allInputsForFiles.style.display = "none";
        // Trick to make this code work under some versions of IE6 - 8, in witch you can't appendChilds
        // into not completly loaded tags
        if (document.body.firstChild) {
          document.body.insertBefore(allInputsForFiles, document.body.firstChild);
        } else {
          document.getElementsByTagName('body')[0].appendChild(allInputsForFiles);
        }
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
                        || (O$.isExplorer() && (document.documentMode == 7 || document.documentMode == 6)))) {
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
      _setStylesForAddButton:    function (addButton) {
        O$.setStyleMappings(fileUpload._buttons.browseTitleInput, {std : fileUpload._browseBtnStyles.def});
        O$.addEventHandler(addButton,"mouseover",function() {
          if (!fileUpload._buttons.browseInput.disabled) {
            O$.setStyleMappings(fileUpload._buttons.browseTitleInput, {mouseover : fileUpload._browseBtnStyles.mouseover});
          }
        });
        O$.addEventHandler(addButton, "mouseout", function() {
          if (!fileUpload._buttons.browseInput.disabled) {
            O$.setStyleMappings(fileUpload._buttons.browseTitleInput, {mouseover : null});
          }
        });

        O$.addEventHandler(addButton,"mousedown",function() {
          if (!fileUpload._buttons.browseInput.disabled) {
            O$.setStyleMappings(fileUpload._buttons.browseTitleInput, {mousedown : fileUpload._browseBtnStyles.mousedown});
          }
          addButton._afterMouseDown = true;
        });
        O$.addEventHandler(window,"mouseup",function mouseUpHandler() {

          if (O$.FileUploadUtil._inTheDOM(fileUpload)) {
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
        if (fileUpload._activeElementNow == _this) {
          return;

        }
        if (!fileUpload._shouldInvokeFocusEvHandler) {
          fileUpload._shouldInvokeFocusEvHandler = true;
          return;
        }
        if (!fileUpload._shouldInvokeFocusNonModifiable) {
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
        if (!fileUpload._shouldInvokeFocusEvHandler) {
          fileUpload._shouldInvokeFocusEvHandler = true;
          return;
        }
        if ((O$.isChrome() || O$.isSafari()) && !fileUpload._ignoreBlurForChrome) {
          fileUpload._ignoreBlurForChrome = true;
          return;
        }
        if ((O$.isChrome() || O$.isSafari()) && !fileUpload._shouldInvokeFocusNonModifiable) {
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
              // temporary fix for IE caused by _ajaxCleanupRequired functionality after FileUpload reloaded with Ajax
              if (fileUpload._events)
                fileUpload._events._fireBlurEvent();
            }

          }, 1);
        }, 1);

      },
      _isMultipleFileSelectSupported:    function (evt) {
        if (fileUpload._isBrowserSupportHTML5FileUpload()) {
          if (evt.target.files)
            return true;
        }
        return false;
      },
      _isBrowserSupportHTML5FileUpload:    function () {
        //Opera doesn't support file upload API , although it support multiupload
        //IE doesn't support at all
        //Safari has File Upload API, but with bugs. No solution for the moment. Look here: http://stackoverflow.com/questions/7231054/file-input-size-issue-in-safari-for-multiple-file-selection
        //Safari anytime can get not correct fileNames , so this feature is turned of, if Safari is  used.
        return !((!O$.isChrome() && O$.isSafari()) || typeof(window.FormData) == "undefined");
      },
      _chromeAndSafariFocusFix:    function (button) {
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
  _inTheDOM:function (element) {
    var parent = element.parentNode;
    while (parent != null && parent != document) {
      parent = parent.parentNode;
    }
    return (parent == document);
  },
  _initDragArea:function(area) {
    O$.extend(area.parentNode, {
      _findPosition:function () {
        var size = O$.getElementSize(area.parentNode);
        var pos = O$.getElementPos(area.parentNode);
        return [pos.x + size.width/2, pos.y + size.height/2];
      }
    });
    O$.extend(area, {
      _hideAllExceptThis:function () {
        for (var i = 0; i < O$._dropAreas.length; i++) {
          if (O$._dropAreas[i] != area) {
            O$._dropAreas[i].hide();
            O$._dropAreas[i]._isVisible = false;
          }
        }
      },
      _isNearest:function (clientX, clientY) {

        var ourDistance = area._getDistanceTo(clientX, clientY);
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
        var areasXY = area.parentNode._findPosition();
        if (areasXY[0] < x && areasXY[1] < y) {
          return x - areasXY[0] + y - areasXY[1];
        } else if (areasXY[0] >= x && areasXY[1] >= y) {
          return areasXY[0] - x + areasXY[1] - y;
        } else if (areasXY[0] >= x && areasXY[1] < y) {
          return areasXY[0] - x + y - areasXY[1];
        } else if (areasXY[0] < x && areasXY[1] >= y) {
          return x - areasXY[0] + areasXY[1] - y;
        }
      },
      _remove:function() {
        for (var i = 0; i < O$._dropAreas.length; i++) {
          if (O$._dropAreas[i] == area) {
            O$._dropAreas.splice(i, 1);
            return;
          }
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
  _initFileAPI:function(file) {
    O$.extend(file,{
      getName:function() {
        return file.name;
      },
      getStatus:function() {
        return file.status;
      },
      getSize:function() {
        return file.size;
      },
      getProgress:function() {
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

