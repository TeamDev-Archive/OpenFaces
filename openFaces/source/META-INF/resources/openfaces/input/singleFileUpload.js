/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2012, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

O$.SingleFileUpload = {
  _init: function(componentId, lengthAlreadyUploadedFiles,
                  statusLabelNotUploaded, statusLabelInProgress, statusLabelUploaded, statusLabelErrorSize, statusLabelUnexpectedError,
                  acceptedTypesOfFile,  isDuplicateAllowed,
                  addButtonId, addButtonClass, addButtonOnMouseOverClass, addButtonOnMouseDownClass,addButtonOnFocusClass,
                  addButtonDisabledClass,
                  isDisabled,
                  tabIndex, progressBarId, statusStoppedText, statusStoppingText, ID,
                  onchangeHandler, onstartHandler, onendHandler,
                  onfilestartHandler, onfileinprogressHandler, onfileendHandler, onwrongfiletypeHandler, ondirectorydroppedHandler,
                  dropTargetCrossoverClass, render, externalDropTargetId, acceptedMimeTypes,
                  layoutMode, defStopUrl, stopIcoClassMin,
                  showInfoAfterUpload, uploadBtnBehavior, showStopNearProgress, directoryDroppedText, wrongFileTypeText,
                  externalBrowseButtonId, showInPopup, popupPositionedByID, popupHorizontalAlignment, popupVerticalAlignment,
                  popupHorizontalDistance, popupVerticalDistance) {

    var fileUpload = O$.initComponent(componentId, null, {
      _showInfoAfterUpload : showInfoAfterUpload,
      _uploadBtnBehavior : uploadBtnBehavior,
      _showStopNearProgress : showStopNearProgress,
      _isAutoUpload : true,
      _fileHTML5 : null,
      _currentFile : null,
      _layoutMode : function () {
        switch(layoutMode) {
          case "full" :
            return O$.SingleFileUpload._LayoutMode.FULL;
          case "compact" :
            return O$.SingleFileUpload._LayoutMode.COMPACT;
        }
      }(),
      _showPopupIfPopupNeeded:function () {
        if(showInPopup){
          var positionedByID = !popupPositionedByID ? externalBrowseButtonId : popupPositionedByID;
          fileUpload._showPopup(positionedByID, popupHorizontalAlignment, popupVerticalAlignment,
                  popupHorizontalDistance, popupVerticalDistance);
        }
      },
      _processFileAddingHTML5:function (file) {
        if (isFileNameNotApplied(file.name) || fileUpload._buttons.browseInput.disabled ||
                (!file._fromDnD && file.size == 0) || (file._fromDnD && fileUpload._isDirectory(file))) {
          return false;
        }
        setInfoWindow(file.name);
        file._uniqueId = fileUpload._ID + idOfInfoAndInputDiv;
        file._fakeInput = componentId + "::inputs::input" + idOfInfoAndInputDiv + "::form::fileInput";
        file._infoId = idOfInfoAndInputDiv;
        fileUpload._fileHTML5 = file;
        fileUpload._currentFile = new O$.FileUploadUtil.File(O$.SingleFileUpload._SpecFileAPIInit, fileUpload, file._infoId, file.name, O$.FileUploadUtil.Status.NEW, file.size);

        fileUpload._numberOfFilesToUpload++;
        idOfInfoAndInputDiv++;

        if (fileUpload._maxQuantity != 0
                && fileUpload._maxQuantity <= (fileUpload._numberOfFilesToUpload + fileUpload._lengthUploadedFiles)) {
          fileUpload._buttons.browseInput.disabled = true;
          O$.setStyleMappings(fileUpload._buttons.browseTitleInput, {disabled:addButtonDisabledClass});
        }
        return true;
      },
      _processFileAdding:function (inputForFile) {

        if (isFileNameNotApplied(inputForFile.value)) {
          inputForFile.value = "";
          fileUpload._shouldInvokeFocusEvHandler = true;
          return false;
        }
        setInfoWindow(inputForFile.value);
        fileUpload._buttons.browse._inFocus = false;
        inputForFile._idInputAndDiv = idOfInfoAndInputDiv;
        fileUpload._createAndAppendComplexInputWithId(inputForFile);
        fileUpload._currentFile = new O$.FileUploadUtil.File(O$.SingleFileUpload._SpecFileAPIInit, fileUpload, inputForFile._idInputAndDiv, inputForFile.value, O$.FileUploadUtil.Status.NEW, null);
        fileUpload._numberOfFilesToUpload++;
        idOfInfoAndInputDiv++;
        fileUpload._buttons.browseInput = fileUpload._createInputInAddBtn();
        fileUpload._buttons.browseDivForInput.appendChild(fileUpload._buttons.browseInput);

        O$.addEventHandler(fileUpload._buttons.browseInput, "focus", fileUpload._focusHandler);
        O$.addEventHandler(fileUpload._buttons.browseInput, "blur", fileUpload._blurHandler);

        fileUpload._events._fireChangeEvent();

        if (fileUpload._maxQuantity != 0
                && fileUpload._maxQuantity <= (fileUpload._numberOfFilesToUpload + fileUpload._lengthUploadedFiles)) {
          fileUpload._buttons.browseInput.disabled = true;
          O$.setStyleMappings(fileUpload._buttons.browseTitleInput, {disabled:addButtonDisabledClass});
        }
        fileUpload.__uploadButtonClickHandler();
        return true;
      },
      _setFocusOnComponent:function () {
        if (!fileUpload._buttons.browseInput.disabled && fileUpload._buttons.browseInput.focus) {
          fileUpload._buttons.browseInput.focus();
          return;
        }
        fileUpload._events._fireBlurEvent();
      },
      _setUploadButtonAfterFileHaveBeenAdded:function () {
        fileUpload.__uploadButtonClickHandler();
      },
      _prepareUIWhenAllRequestsFinished:function (shouldCheckRequest) {

        if (fileUpload._inputsStorage.childNodes.length == 0 && fileUpload._fileHTML5 == null) { // if all files are  already uploaded
          if (shouldCheckRequest)
            setTimeout(function () {
              fileUpload._sendCheckRequest(fileUpload._currentFile);
            }, 500);

          fileUpload._numberOfFilesToUpload = 0;
          fileUpload._buttons.browseInput.disabled = false;
          fileUpload._inUploading = false;

          O$.removeAllChildNodes(fileUpload._actionButtonContainer);
          if (showInPopup) {
            if (fileUpload._layoutMode != O$.SingleFileUpload._LayoutMode.COMPACT)
              fileUpload._actionButtonContainer.appendChild(fileUpload._buttons.closePopup);
          } else if (!externalBrowseButtonId)
            fileUpload._actionButtonContainer.appendChild(fileUpload._buttons.browse);
          if (fileUpload._showStopNearProgress) {
            var elToDel = O$(fileUpload.id + "::stopIcon");
            var stopSize = fileUpload._getNumProperty(elToDel, "margin-left")
                    + O$.getElementSize(elToDel).width;
            elToDel.parentNode.removeChild(elToDel);
            var progressSize = O$.getElementSize(fileUpload._els.progressBar);
            if (!fileUpload._els.progressBar._defaultSize)
              fileUpload._els.progressBar._defaultSize = progressSize;
            fileUpload._els.progressBar._setWidthForAllComponent(fileUpload._els.progressBar._defaultSize.width + stopSize);
            fileUpload._els.progressBar.setValue(fileUpload._els.progressBar.getValue());
          }
          if (!fileUpload._showInfoAfterUpload) {
            if (showInPopup)
              fileUpload._hidePopup();
            else if (externalBrowseButtonId)
              fileUpload._hideBorder();
            fileUpload._els.progressBar.style.display = "none";
            fileUpload._currentFile = null;
            if (fileUpload._layoutMode == O$.SingleFileUpload._LayoutMode.FULL) {
              fileUpload._els.fileName.innerHTML = ""; //filename
              fileUpload._els.status.innerHTML = "";//status
            }
            if (fileUpload._uploadBtnBehavior == "hide") {
              if (fileUpload._layoutMode == O$.SingleFileUpload._LayoutMode.FULL) {
                fileUpload._addButtonParent.style.display = "";
              } else if (fileUpload._layoutMode == O$.SingleFileUpload._LayoutMode.COMPACT) {
                fileUpload._addButtonParent.parentNode.style.display = "";
              }
            }
          }

          O$.setStyleMappings(fileUpload._buttons.browseTitleInput, {disabled:null});
          initHeaderButtons();
          //set focus on addButton
          fileUpload._shouldInvokeFocusEvHandler = false;
          fileUpload._setFocusOnComponent();
          fileUpload._shouldInvokeFocusEvHandler = true;
        }
      },
      _progressRequest:function (inputForFile, endHandler) {
        O$.Ajax.requestComponentPortions(fileUpload.id, ["nothing"],
                JSON.stringify({progressRequest:"true", fileId:inputForFile._uniqueId}),
                function (fileUpload, portionName, portionHTML, portionScripts, portionData) {
                  var fileForAPI = fileUpload._getFile(inputForFile._idInputAndDiv);
                  if (portionData['isFileSizeExceed'] == "true") {
                    fileUpload._els.info._status = O$.FileUploadUtil.Status.SIZE_LIMIT_EXCEEDED;
                    fileForAPI.status = O$.FileUploadUtil.Status.SIZE_LIMIT_EXCEEDED;
                    fileUpload._inputsStorage.removeChild(inputForFile.parentNode.parentNode);
                    if (fileUpload._els.status) {
                      fileUpload._els.status.innerHTML = fileUpload._statuses.sizeLimit._update(null, portionData['size']);
                    }
                    fileUpload._setStatusforFileWithId(inputForFile._uniqueId, "SIZE_LIMIT_EXCEEDED");
                    fileUpload._events._fireFileEndEvent(fileForAPI);
                    if (endHandler) {
                      endHandler();
                    }
                    fileUpload._prepareUIWhenAllRequestsFinished(true);
                  } else {
                    var percents = portionData['progressInPercent'];
                    if (percents != 100) {
                      if (!inputForFile._isInterrupted) {
                        if (inputForFile._wantToInterrupt) {
                          fileUpload._sendIsStoppedRequest(inputForFile, fileUpload._els.info, fileForAPI, endHandler);
                        } else {
                          if (portionData['size']) {
                            if (fileUpload._els.status) {
                              fileUpload._els.status.innerHTML = fileUpload._statuses.inProgress._update(portionData['size'] * (percents / 100), portionData['size']);
                            }
                          }
                        }
                        if (fileUpload._els.progressBar.getValue() == percents) {
                          if (!inputForFile._percentsEqualsTimes) {
                            inputForFile._percentsEqualsTimes = 0;
                          }
                          inputForFile._percentsEqualsTimes++;
                          if (inputForFile._percentsEqualsTimes > fileUpload._numberOfErrorRequest) {
                            fileUpload._sendIsErrorRequest(inputForFile, fileUpload._els.info, fileForAPI, endHandler);
                          }
                          if (inputForFile._percentsEqualsTimes > fileUpload._numberOfFailedRequest) {
                            inputForFile._isInterrupted = true;
                            fileUpload._sendInformThatRequestFailed(inputForFile._uniqueId);
                            fileUpload._els.info._status = O$.FileUploadUtil.Status.FAILED;
                            fileForAPI.status = O$.FileUploadUtil.Status.FAILED;
                            fileUpload._inputsStorage.removeChild(inputForFile.parentNode.parentNode);
                            if (fileUpload._els.status) {
                              fileUpload._els.status.innerHTML = fileUpload._statuses.failed;
                            }
                            fileUpload._setStatusforFileWithId(inputForFile._uniqueId, "FAILED");
                            fileUpload._els.progressBar.setValue(0);
                            fileForAPI.progress = 0;
                            fileUpload._events._fireFileEndEvent(fileForAPI);
                            if (endHandler) {
                              endHandler();
                            }
                            fileUpload._prepareUIWhenAllRequestsFinished(true);
                          }
                        } else {
                          inputForFile._percentsEqualsTimes = 0;
                        }
                        fileUpload._els.progressBar.setValue(percents);
                        fileForAPI.progress = percents / 100;
                        fileUpload._events._fireFileInProgressEvent(fileForAPI);
                        fileUpload._callProgressRequest(inputForFile, endHandler);
                      }
                    } else {// when file already uploaded
                      fileUpload._els.progressBar.setValue(percents, function callOnEnd() {
                        function processEndUpload() {
                          fileForAPI.progress = percents / 100;
                          fileUpload._lengthUploadedFiles++;
                          /*removing input*/
                          fileUpload._inputsStorage.removeChild(inputForFile.parentNode.parentNode); // delete divForFileInput
                          // infoDiv updating
                          fileUpload._els.info._status = O$.FileUploadUtil.Status.SUCCESSFUL;
                          if (fileUpload._els.status) {
                            fileUpload._els.status.innerHTML = fileUpload._statuses.uploaded._update(null, portionData['size']);
                          }
                          fileUpload._setStatusforFileWithId(inputForFile._uniqueId, "SUCCESSFUL");
                          if (endHandler) {
                            endHandler();
                          }
                          fileForAPI.status = O$.FileUploadUtil.Status.SUCCESSFUL;
                          fileUpload._events._fireFileEndEvent(fileForAPI);
                          fileUpload._prepareUIWhenAllRequestsFinished(true);
                        }
                        if (!fileUpload._showInfoAfterUpload) {
                          setTimeout(processEndUpload, 200);
                        } else {
                          processEndUpload();
                        }
                      });
                    }
                  }

                },
                null,
                true
        );
      },
      _progressHTMl5Request:function (file, request, endHandler) {
        O$.Ajax.requestComponentPortions(fileUpload.id, ["nothing"],
                JSON.stringify({progressRequest:"true", fileId:file._uniqueId}),
                function (fileUpload, portionName, portionHTML, portionScripts, portionData) {
                  var fileForAPI = fileUpload._getFile(file._infoId);
                  if (portionData['isFileSizeExceed'] == "true") {
                    fileUpload._els.info._status = O$.FileUploadUtil.Status.SIZE_LIMIT_EXCEEDED;
                    fileForAPI.status = O$.FileUploadUtil.Status.SIZE_LIMIT_EXCEEDED;
                    removeHTML5File();
                    if (fileUpload._els.status) {
                      fileUpload._els.status.innerHTML = fileUpload._statuses.sizeLimit._update(null, portionData['size']);
                    }
                    fileUpload._setStatusforFileWithId(file._uniqueId, "SIZE_LIMIT_EXCEEDED");
                    fileUpload._events._fireFileEndEvent(fileForAPI);
                    if (endHandler) {
                      endHandler();
                    }
                    fileUpload._prepareUIWhenAllRequestsFinished(true);
                  } else {
                    var percents = portionData['progressInPercent'];
                    if (percents != 100) {
                      if (!file._isInterrupted) {
                        if (file._wantToInterrupt) {
                          fileUpload._sendIsStoppedRequestHTML5(file, fileUpload._els.info, fileForAPI, endHandler);
                        } else {
                          if (portionData['size']) {
                            if (fileUpload._els.status) {
                              fileUpload._els.status.innerHTML = fileUpload._statuses.inProgress._update(portionData['size'] * (percents / 100), portionData['size']);
                            }
                          }
                        }
                        if (fileUpload._els.progressBar.getValue() == percents) {
                          if (!file._percentsEqualsTimes) {
                            file._percentsEqualsTimes = 0;
                          }
                          file._percentsEqualsTimes++;
                          if (file._percentsEqualsTimes > fileUpload._numberOfErrorRequest) {
                            fileUpload._sendIsErrorRequestHTML5(file, fileUpload._els.info, fileForAPI, endHandler);
                          }
                          if (file._percentsEqualsTimes > fileUpload._numberOfFailedRequest) {
                            file._isInterrupted = true;
                            fileUpload._sendInformThatRequestFailed(file._uniqueId);
                            fileUpload._els.info._status = O$.FileUploadUtil.Status.FAILED;
                            fileForAPI.status = O$.FileUploadUtil.Status.FAILED;
                            removeHTML5File();
                            if (fileUpload._els.status) {
                              fileUpload._els.status.innerHTML = fileUpload._statuses.failed;
                            }
                            fileUpload._setStatusforFileWithId(file._uniqueId, "FAILED");
                            fileUpload._els.progressBar.setValue(0);
                            fileForAPI.progress = 0;
                            fileUpload._events._fireFileEndEvent(fileForAPI);
                            if (endHandler) {
                              endHandler();
                            }
                            fileUpload._prepareUIWhenAllRequestsFinished(true);
                          }
                        } else {
                          file._percentsEqualsTimes = 0;
                        }
                        fileUpload._els.progressBar.setValue(percents);
                        fileForAPI.progress = percents / 100;
                        fileUpload._events._fireFileInProgressEvent(fileForAPI);
                        setTimeout(function () {
                          fileUpload._progressHTMl5Request(file, request, endHandler);
                        }, 500);
                      }
                    } else {// when file already uploaded
                      fileUpload._els.progressBar.setValue(percents, function callOnEnd() {
                        function processEndUpload() {
                          fileForAPI.progress = percents / 100;
                          fileUpload._lengthUploadedFiles++;
                          /*removing file*/
                          removeHTML5File();
                          // infoDiv updating
                          fileUpload._els.info._status = O$.FileUploadUtil.Status.SUCCESSFUL;
                          if (fileUpload._els.status) {
                            fileUpload._els.status.innerHTML = fileUpload._statuses.uploaded._update(null, portionData['size']);
                          }
                          fileUpload._setStatusforFileWithId(file._uniqueId, "SUCCESSFUL");
                          fileForAPI.status = O$.FileUploadUtil.Status.SUCCESSFUL;
                          fileUpload._events._fireFileEndEvent(fileForAPI);
                          if (endHandler) {
                            endHandler();
                          }
                          fileUpload._prepareUIWhenAllRequestsFinished(true);
                        }
                        if (!fileUpload._showInfoAfterUpload) {
                          setTimeout(processEndUpload, 200);
                        } else {
                          processEndUpload();
                        }
                      });
                    }
                  }

                },
                null,
                true
        );
      },
      _sendIsErrorRequest:function (inputForFile, infoDiv, fileForAPI, endHandler) {
        O$.Ajax.requestComponentPortions(fileUpload.id, ["nothing"],
                JSON.stringify({stoppedRequest:true, uniqueIdOfFile:inputForFile._uniqueId}),
                function (fileUpload, portionName, portionHTML, portionScripts, portionData) {
                  if (portionData['isStopped'] == "true") {
                    inputForFile._isInterrupted = true;
                    fileUpload._els.info._status = O$.FileUploadUtil.Status.FAILED;
                    fileForAPI.status = O$.FileUploadUtil.Status.FAILED;
                    fileUpload._inputsStorage.removeChild(inputForFile.parentNode.parentNode);
                    if (fileUpload._els.status) {
                      fileUpload._els.status.innerHTML = fileUpload._statuses.failed;
                    }
                    fileUpload._setStatusforFileWithId(inputForFile._uniqueId, "FAILED");
                    fileUpload._els.progressBar.setValue(0);
                    fileForAPI.progress = 0;
                    fileUpload._events._fireFileEndEvent(fileForAPI);
                    if (endHandler) {
                      endHandler();
                    }
                    fileUpload._prepareUIWhenAllRequestsFinished(true);
                  }
                }, null, true);
      },
      _sendIsErrorRequestHTML5:function (file, infoDiv, fileForAPI, endHandler) {
        O$.Ajax.requestComponentPortions(fileUpload.id, ["nothing"],
                JSON.stringify({stoppedRequest:true, uniqueIdOfFile:file._uniqueId}),
                function (fileUpload, portionName, portionHTML, portionScripts, portionData) {
                  if (portionData['isStopped'] == "true") {
                    file._isInterrupted = true;
                    fileUpload._els.info._status = O$.FileUploadUtil.Status.FAILED;
                    fileForAPI.status = O$.FileUploadUtil.Status.FAILED;
                    removeHTML5File();
                    if (fileUpload._els.status) {
                      fileUpload._els.status.innerHTML = fileUpload._statuses.failed;
                    }
                    fileUpload._setStatusforFileWithId(file._uniqueId, "FAILED");
                    fileUpload._els.progressBar.setValue(0);
                    fileForAPI.progress = 0;
                    fileUpload._events._fireFileEndEvent(fileForAPI);
                    if (endHandler) {
                      endHandler();
                    }
                    fileUpload._prepareUIWhenAllRequestsFinished(true);
                  }
                }, null, true);
      },
      _sendIsStoppedRequest:function (inputForFile, infoDiv, fileForAPI,endHandler) {
        O$.Ajax.requestComponentPortions(fileUpload.id, ["nothing"],
                JSON.stringify({stoppedRequest:true, uniqueIdOfFile:inputForFile._uniqueId}),
                function (fileUpload) {
                  inputForFile._isInterrupted = true;
                  fileUpload._els.info._status = O$.FileUploadUtil.Status.STOPPED;
                  fileUpload._inputsStorage.removeChild(inputForFile.parentNode.parentNode);
                  if (fileUpload._els.status) {
                    fileUpload._els.status.innerHTML = fileUpload._statuses.stopped;
                  }
                  fileUpload._setStatusforFileWithId(inputForFile._uniqueId, "STOPPED");
                  if (endHandler) {
                    endHandler();
                  }
                  fileForAPI.status = O$.FileUploadUtil.Status.STOPPED;
                  fileUpload._els.progressBar.setValue(0);
                  fileForAPI.progress = 0;
                  fileUpload._events._fireFileEndEvent(fileForAPI);
                  fileUpload._prepareUIWhenAllRequestsFinished(true);
                }, null, true);
      },
      _sendIsStoppedRequestHTML5:function (file, infoDiv, fileForAPI, endHandler) {
        O$.Ajax.requestComponentPortions(fileUpload.id, ["nothing"],
                JSON.stringify({stoppedRequest:true, uniqueIdOfFile:file._uniqueId}),
                function (fileUpload, portionName, portionHTML, portionScripts, portionData) {
                  if (portionData['isStopped'] == "true") {
                    file._isInterrupted = true;
                    fileUpload._els.info._status = O$.FileUploadUtil.Status.STOPPED;
                    removeHTML5File();
                    if (fileUpload._els.status) {
                      fileUpload._els.status.innerHTML = fileUpload._statuses.stopped;
                    }
                    fileUpload._setStatusforFileWithId(file._uniqueId, "STOPPED");
                    fileForAPI.status = O$.FileUploadUtil.Status.STOPPED;
                    fileUpload._els.progressBar.setValue(0);
                    fileForAPI.progress = 0;
                    fileUpload._events._fireFileEndEvent(fileForAPI);
                    if (endHandler) {
                      endHandler();
                    }
                    fileUpload._prepareUIWhenAllRequestsFinished(true);
                  }
                }, null, true);
      },
      _initializeDropTarget : function() {
        if (showInPopup && !externalDropTargetId)
          return;
        fileUpload._setupExternalDropTarget();
        fileUpload._els.dropTarget = function setBehaviorForDragAndDropArea() {
          var area = fileUpload._getDropTargetArea(fileUpload.id + "::dragArea");
          area._firstShow = true;

          O$.FileUploadUtil._initDragArea(area);
          O$.extend(area, {
            hide:function () {
              if (!area._isExternal) {
                fileUpload.firstChild.style.display = "";
              }
              area.style.display = "none";
              area._firstShow = true;
            },
            show:function () {
              if (!area._isExternal && area._firstShow) {
                function getCorrectWidthForArea(area) {
                  return O$.getElementSize(fileUpload).width - fileUpload._getNumProperty(area, "border-top-width") * 2
                          - fileUpload._getNumProperty(area, "margin-left") - fileUpload._getNumProperty(area, "margin-right");
                }

                function getCorrectHeightForArea(area) {
                  function getGreaterHeight() {
                    return O$.getElementSize(fileUpload).height - fileUpload._getNumProperty(fileUpload, "border-top-width") * 2
                            - fileUpload._getNumProperty(area, "margin-top") - fileUpload._getNumProperty(area, "margin-bottom");
                  }

                  function getDiff(area) {
                    var diff = fileUpload._getNumProperty(area, "border-top-width");
                    diff *= 2;
                    diff += fileUpload._getNumProperty(area, "padding-top");
                    return diff;
                  }

                  return getGreaterHeight() - getDiff(area);
                }

                area.style.height = getCorrectHeightForArea(area) + "px";
                area.style.width = getCorrectWidthForArea(area) + "px";
                area._firstShow = false;
              }
              if (!area._isExternal) {
                fileUpload.firstChild.style.display = "none";
              }
              area.style.display = "block";
            }
          });
          if (!O$._dropAreas) {
            O$._dropAreas = [];
          }
          O$._dropAreas.push(area);
          var body = document.getElementsByTagName('body')[0];
          area._isVisible = false;
          fileUpload._setDragEventsForBody(body, area);
          fileUpload._setDragEventsForFileUpload(area);
          return area;
        }();
    }
    });
    O$.FileUploadUtil._initGeneral(fileUpload,
            lengthAlreadyUploadedFiles, acceptedTypesOfFile, isDisabled, ID,
            addButtonClass, addButtonOnMouseOverClass, addButtonOnMouseDownClass,addButtonOnFocusClass,
            statusLabelInProgress,statusLabelUploaded,statusLabelErrorSize,
            statusLabelNotUploaded,statusStoppedText,statusLabelUnexpectedError,
            render,tabIndex,dropTargetCrossoverClass, acceptedMimeTypes,
            directoryDroppedText, wrongFileTypeText, externalDropTargetId,
            showInPopup, popupPositionedByID,popupHorizontalAlignment,
            popupVerticalAlignment, popupHorizontalDistance, popupVerticalDistance);

    fileUpload._setAllEvents(onchangeHandler,onstartHandler,onendHandler,
            onfilestartHandler,onfileinprogressHandler,onfileendHandler,
            onwrongfiletypeHandler,ondirectorydroppedHandler);
    if (fileUpload._uploadBtnBehavior == "showStop" && fileUpload._showStopNearProgress) {
      fileUpload._showStopNearProgress = false;
    }
    //getting clear,stop,cancel, progressBar facet for each info window
    fileUpload._elementsCont = O$(componentId + "::elements");
    fileUpload._buttons.stop = fileUpload._getFacet("::stopFacet");
    if (showInPopup && fileUpload._layoutMode != O$.SingleFileUpload._LayoutMode.COMPACT)
      fileUpload._buttons.closePopup = fileUpload._getFacet("::closeFacet");

    fileUpload._els = [];
    if (fileUpload._layoutMode == O$.SingleFileUpload._LayoutMode.FULL) {
      fileUpload._els.infoTable = O$(componentId + "::fileInfo");
      if (fileUpload._uploadBtnBehavior == "showStop" || fileUpload._uploadBtnBehavior == "disable") {
       // in IE7 table's layout is not correct
        if (O$.isExplorer() && (O$.isQuirksMode() || O$.isExplorer7() || O$.isExplorer6())) {
          fileUpload._els.infoTable.parentNode.style.width = "";
        }
      }
      fileUpload._els.info =  fileUpload._els.infoTable.firstChild.firstChild;
      fileUpload._els.fileName = fileUpload._els.info.childNodes[0].firstChild;
      fileUpload._els.status = fileUpload._els.info.childNodes[1];
    }else if (fileUpload._layoutMode == O$.SingleFileUpload._LayoutMode.COMPACT) {
      var elToRemove = O$(componentId + "::fileInfo").parentNode;
      elToRemove.parentNode.removeChild(elToRemove);
      fileUpload._els.info = {};
    }
    fileUpload._els.progressBar = O$(progressBarId);
    if (fileUpload._uploadBtnBehavior == "hide" && fileUpload._showStopNearProgress && O$.isExplorer() && O$.isQuirksMode()) {
      fileUpload._els.progressBar._enableQuirksMode();
    }
    var idOfInfoAndInputDiv = 1;

    setTabIndexForAllButtons(tabIndex);

    fileUpload._inputsStorage = fileUpload._createStructureForInputs();

    /*var dragAndDropArea = O$(elements.id + "::dragArea");
    elements.removeChild(dragAndDropArea);*/
    if (!O$.isExplorer())
      fileUpload.removeChild(fileUpload._elementsCont);
    fileUpload._setUpBrowseButton(addButtonId, externalBrowseButtonId);
    if (externalBrowseButtonId){
      if (!showInPopup)
        fileUpload._hideBorder();
      fileUpload._setUpExternalBrowseButton(externalBrowseButtonId);
    }
    fileUpload._addButtonParent = fileUpload._buttons.browse.parentNode;
    fileUpload._actionButtonContainer = O$(componentId + "::actionButtonContainer");
    if(showInPopup){
      fileUpload._initPopup();
      if (!externalBrowseButtonId)
        fileUpload._moveBrowseButtonOutOfComponent();
      if (fileUpload._layoutMode != O$.SingleFileUpload._LayoutMode.COMPACT)
        fileUpload._initCloseButton(componentId);
    }

    O$.extend(fileUpload, {
              __uploadButtonClickHandler:function () {
                fileUpload._buttons.browseInput.disabled = true;
                fileUpload._inUploading = true;
                O$.setStyleMappings(fileUpload._buttons.browseTitleInput, {disabled : addButtonDisabledClass});
                /* IE 7 cannot define what width in browse button because it's not rendered yet*/
                if (fileUpload._uploadBtnBehavior == "showStop") {
                  if (!fileUpload._buttons.stop._defined) {
                    if (showInPopup) {
                      new function setStopBtnWidthAsInClosePopup() {
                        var closePopupSize = O$.getElementSize(fileUpload._buttons.closePopup);
                        fileUpload._buttons.stop.style.width = closePopupSize.width + "px";
                        fileUpload._buttons.stop.style.height = closePopupSize.height + "px";
                      }();
                    } else if (!externalBrowseButtonId) {
                      fileUpload._setButtonSizeAsInBrowse(fileUpload._buttons.stop);
                      fileUpload._buttons.stop._defined = true;
                    }
                  }
                }


                fileUpload._listOfids = [];
                /*prepare a list of files that would be uploaded*/
                if (fileUpload._inputsStorage.childNodes[0]) {
                  var form = fileUpload._inputsStorage.childNodes[0].firstChild;
                  fileUpload._addFileToInfoFileList(form);
                }else if (fileUpload._fileHTML5 != null) {/*specific behavior for uploading files with drag and drop*/
                  var html5File = fileUpload._fileHTML5;
                  fileUpload._addFileToInfoFileListHTML5(html5File);
                }
                fileUpload._events._fireStartEvent(fileUpload._currentFile);
                var uri = fileUpload._getDocumentURI();

                function setupUIForUpload(inputForFile, infoDiv, fileForAPI) {
                  function setStopButtonBehavior(inputForFile, infoDiv) {
                    var stopFileDiv = fileUpload._buttons.stop.cloneNode(true);
                    stopFileDiv.setAttribute("id", fileUpload._buttons.stop.id + inputForFile._idInputAndDiv);
                    if (fileUpload._uploadBtnBehavior == "showStop") {
                      O$.removeAllChildNodes(fileUpload._actionButtonContainer);
                      fileUpload._actionButtonContainer.appendChild(stopFileDiv);
                    }
                    O$.addEventHandler(stopFileDiv, "focus", fileUpload._focusHandler);
                    O$.addEventHandler(stopFileDiv, "blur", fileUpload._blurHandler);
                    stopFileDiv._clickHandler = function () {
                      stopFileDiv.style.visibility = "hidden";
                      if (fileUpload._els.status) {
                        fileUpload._els.status.innerHTML = statusStoppingText;
                      }
                      var iframe = inputForFile.nextSibling;
                      iframe.src = "";
                      inputForFile._wantToInterrupt = true;
                    };
                    O$.addEventHandler(stopFileDiv, "click", stopFileDiv._clickHandler);
                    fileUpload._chromeAndSafariFocusFix(stopFileDiv);
                    if (fileUpload._showStopNearProgress) {
                      var stopIcon = O$(fileUpload.id + "::stopIcon");
                      stopIcon._clickHandler = function () {
                        stopIcon.style.visibility = "hidden";
                        if (fileUpload._els.status) {
                          fileUpload._els.status.innerHTML = statusStoppingText;
                        }
                        var iframe = inputForFile.nextSibling;
                        iframe.src = "";
                        inputForFile._wantToInterrupt = true;
                      };
                      O$.addEventHandler(stopIcon, "click", stopIcon._clickHandler);
                    }
                  }
                  fileUpload._els.info._status = O$.FileUploadUtil.Status.IN_PROGRESS;
                  fileForAPI.status = O$.FileUploadUtil.Status.IN_PROGRESS;
                  if (fileUpload._els.status) {
                    fileUpload._els.status.innerHTML = fileUpload._statuses.inProgress._update(0, "");
                  }
                  if (fileUpload._uploadBtnBehavior == "hide") {
                    if (fileUpload._layoutMode == O$.SingleFileUpload._LayoutMode.FULL) {
                      fileUpload._addButtonParent.style.display = "none";
                    } else if (fileUpload._layoutMode == O$.SingleFileUpload._LayoutMode.COMPACT && !showInPopup) {
                      fileUpload._addButtonParent.parentNode.style.display = "none";
                    }
                  }
                  setStopButtonBehavior(inputForFile, infoDiv);
                }

                function setupUIForUploadHTML5(file, infoDiv, request, fileForAPI) {
                  function setStopButtonBehaviorHTML5(file, infoDiv, request) {
                    var stopFileDiv = fileUpload._buttons.stop.cloneNode(true);
                    stopFileDiv.setAttribute("id", fileUpload._buttons.stop.id + file._infoId);
                    if (fileUpload._uploadBtnBehavior == "showStop") {
                      O$.removeAllChildNodes(fileUpload._actionButtonContainer);
                      fileUpload._actionButtonContainer.appendChild(stopFileDiv);
                    }
                    O$.addEventHandler(stopFileDiv, "focus", fileUpload._focusHandler);
                    O$.addEventHandler(stopFileDiv, "blur", fileUpload._blurHandler);
                    stopFileDiv._clickHandler = function () {
                      stopFileDiv.style.visibility = "hidden";
                      if (fileUpload._els.status) {
                        fileUpload._els.status.innerHTML = statusStoppingText;
                      }
                      request.abort();
                      file._wantToInterrupt = true;
                    };
                    O$.addEventHandler(stopFileDiv, "click", stopFileDiv._clickHandler);
                    fileUpload._chromeAndSafariFocusFix(stopFileDiv);
                    if (fileUpload._showStopNearProgress) {
                      var stopIcon = O$(fileUpload.id + "::stopIcon");
                      stopIcon._clickHandler = function () {
                        stopIcon.style.visibility = "hidden";
                        if (fileUpload._els.status) {
                          fileUpload._els.status.innerHTML = statusStoppingText;
                        }
                        request.abort();
                        file._wantToInterrupt = true;
                      };
                      O$.addEventHandler(stopIcon, "click", stopIcon._clickHandler);
                    }
                  }
                  fileUpload._els.info._status = O$.FileUploadUtil.Status.IN_PROGRESS;
                  fileForAPI.status = O$.FileUploadUtil.Status.IN_PROGRESS;
                  if (fileUpload._els.status) {
                    fileUpload._els.status.innerHTML = fileUpload._statuses.inProgress._update(0, file.size);
                  }
                  if (fileUpload._uploadBtnBehavior == "hide") {
                    if (fileUpload._layoutMode == O$.SingleFileUpload._LayoutMode.FULL) {
                      fileUpload._addButtonParent.style.display = "none";
                    } else if (fileUpload._layoutMode == O$.SingleFileUpload._LayoutMode.COMPACT && !showInPopup) {
                      fileUpload._addButtonParent.parentNode.style.display = "none";
                    }
                  }
                  setStopButtonBehaviorHTML5(file, infoDiv, request);
                }

                  return new function() {
                    /*in single only one file could be uploaded at the moment*/
                    if (fileUpload._inputsStorage.childNodes.length != 0) {
                      var inputDiv = fileUpload._inputsStorage.childNodes[0];
                      var form = inputDiv.childNodes[0];
                      var iframe = form.childNodes[1];
                      var fileInput = form.childNodes[0];
                      var fileOfAPI = fileUpload._getFile(fileInput._idInputAndDiv);
                      form.target = iframe.name;
                      form.action = uri + "&idOfFile=" + fileInput._idInputAndDiv;
                      form.submit();
                      form.target = "_self";
                      fileUpload._callProgressRequest(fileInput);
                      fileOfAPI.status = O$.FileUploadUtil.Status.IN_PROGRESS;
                      setupUIForUpload(fileInput, fileUpload._els.info, fileOfAPI);
                      fileUpload._events._fireFileStartEvent(fileOfAPI);
                    }

                    if (fileUpload._fileHTML5 != null) {
                      var html5File = fileUpload._fileHTML5;
                      var fileAPI = fileUpload._getFile(html5File._infoId);

                      var request = fileUpload._sendFileRequest(html5File,uri);
                      fileUpload._callFileProgressForHTML5Request(html5File, request);
                      fileAPI.status = O$.FileUploadUtil.Status.IN_PROGRESS;
                      setupUIForUploadHTML5(html5File, fileUpload._els.info, request, fileAPI);
                      fileUpload._events._fireFileStartEvent(fileAPI);
                    }
                  }();

              }
            }
    );
    initHeaderButtons(true);
    setFocusBlurBehaviour();
    if (window._loaded) {
      fileUpload._initializeDropTarget();
    } else {
      O$.addEventHandler(window, "load", function setupDropTarget() {
        O$.removeEventHandler(window, "load", setupDropTarget);
        fileUpload._initializeDropTarget();
        window._loaded = true;
      });
    }

    O$.SingleFileUpload._initFileUploadAPI(fileUpload);

    O$.addEventHandler(document, "sessionexpired", function () {
      var f = fileUpload._currentFile;
      if (f.getStatus() == O$.FileUploadUtil.Status.IN_PROGRESS) {
        fileUpload._els.info._status = O$.FileUploadUtil.Status.FAILED;
        f.status = O$.FileUploadUtil.Status.FAILED;
        if (fileUpload._els.status) {
          fileUpload._els.status.innerHTML = fileUpload._statuses.failed;
        }
      }
      f.remove();
      fileUpload._numberOfFilesToUpload = 0;
      fileUpload._buttons.browseInput.disabled = false;
      fileUpload._inUploading = false;

      O$.setStyleMappings(fileUpload._buttons.browseTitleInput, {disabled:null});
      if (fileUpload._layoutMode == O$.SingleFileUpload._LayoutMode.COMPACT) {
        fileUpload.style.display = "none";
      } else {
        initHeaderButtons();
      }
    });

    function isFileNameNotApplied(filename) {
      /*function isFileNameAlreadyExist(filename) {
        var infos = allInfos.childNodes;
        for (var k = 0; k < infos.length; k++) {
          if (infos[k]._status != O$.FileUploadUtil.Status.FAILED || infos[k]._status != O$.FileUploadUtil.Status.STOPPED
                  || infos[k]._status != O$.FileUploadUtil.Status.SIZE_LIMIT_EXCEEDED) {
            var value = infos[k].childNodes[0].innerHTML;
            if (value == fileUpload._getFileName(filename)) {
              return true;
            }
          }
        }
        return false;
      }*/

      return (filename == "" || !fileUpload._isAcceptedTypeOfFile(filename)
              /*|| (!isDuplicateAllowed && isFileNameAlreadyExist(filename))*/)
    }

    function initHeaderButtons(firstTime) {
      if (firstTime) {
        fileUpload._els.progressBar.style.display = "none";
      }
      if (fileUpload._isDisabled) {
        fileUpload._buttons.browseInput.disabled = true;
        O$.setStyleMappings(fileUpload._buttons.browseTitleInput, {disabled : addButtonDisabledClass});
      } else {
        if (fileUpload._maxQuantity != 0 && fileUpload._lengthUploadedFiles == fileUpload._maxQuantity) {
          fileUpload._buttons.browseInput.disabled = true;
          O$.setStyleMappings(fileUpload._buttons.browseTitleInput, {disabled : addButtonDisabledClass});
        } else {
          fileUpload._buttons.browseInput.disabled = false;
        }
      }
    }

    function setFocusBlurBehaviour() {
      O$.addEventHandler(fileUpload._buttons.browseInput, "focus", fileUpload._focusHandler);
      O$.addEventHandler(fileUpload._buttons.browseInput, "blur", fileUpload._blurHandler);
      if (fileUpload._buttons.browseTitleInput.focus) {
        O$.addEventHandler(fileUpload._buttons.browseTitleInput, "focus", fileUpload._focusHandler);
        O$.addEventHandler(fileUpload._buttons.browseTitleInput, "blur", fileUpload._blurHandler);
      }
    }

    function setTabIndexForAllButtons(tabIndex) {
      if (tabIndex != -1) {
        var buttons = [fileUpload._buttons.stop];
        buttons.forEach(function(button) {
          button.setAttribute("tabindex", tabIndex);
        });
      }
    }

    function removeHTML5File() {
      fileUpload._fileHTML5 = null;
    }

    function setInfoWindow(filename) {
      fileUpload._currentFile = null;
      if (fileUpload._layoutMode == O$.SingleFileUpload._LayoutMode.FULL) {
        fileUpload._els.fileName.innerHTML = fileUpload._getFileName(filename); //filename
        fileUpload._els.status.innerHTML = fileUpload._statuses.newOne;//status
      }
      if (fileUpload._showStopNearProgress) {
        fileUpload._els.progressBar._setWidthForAllComponent(0);
        fileUpload._els.progressBar.style.visibility = "hidden";
        fileUpload._els.progressBar.style.display = "block";
        if (O$.isExplorer() &&
                (O$.isQuirksMode() || (O$.isExplorer6() || O$.isExplorer7() || O$.isExplorer8()))) {
          fileUpload._els.progressBar.style.styleFloat = "left";
        } else {
          fileUpload._els.progressBar.style.cssFloat = "left";
        }
        function setupStopIconBtn() {
          var stopFileDiv = fileUpload._buttons.stop.cloneNode(true);
          stopFileDiv.id = fileUpload.id + "::stopIcon";
          O$.addEventHandler(stopFileDiv, "focus", fileUpload._focusHandler);
          O$.addEventHandler(stopFileDiv, "blur", fileUpload._blurHandler);
          fileUpload._chromeAndSafariFocusFix(stopFileDiv);
          fileUpload._els.progressBar.parentNode.appendChild(stopFileDiv);
          if (defStopUrl) {
            stopFileDiv.style.backgroundImage = "url('" + defStopUrl + "')";
            if (O$.isExplorer7() || O$.isExplorer6() || (O$.isExplorer() && O$.isQuirksMode())) {
              stopFileDiv.style.marginTop = fileUpload._getNumProperty(stopFileDiv, "margin-top") - 1;
            }
          } else {
            O$.setStyleMappings(stopFileDiv, {def:stopIcoClassMin});
          }
          return stopFileDiv;
        }
        var stopBtn = setupStopIconBtn();
        fileUpload._els.progressBar.style.width = (O$.getElementSize(fileUpload._els.progressBar).width - function countWidthForStopBtn() {
          return fileUpload._getNumProperty(stopBtn,"margin-left") + fileUpload._getNumProperty(fileUpload._els.progressBar,"margin-right")
                  + O$.getElementSize(stopBtn).width;
        }()) + "px";
        // to avoid elements jumping in min layout
        fileUpload._els.progressBar.setValue(0);
        fileUpload._els.progressBar.style.visibility = "visible";
      } else {
        fileUpload._els.progressBar.style.display = "block";
        fileUpload._els.progressBar.setValue(0);
      }
    }
  },
  _initFileUploadAPI:function (fileUpload) {
    //Helper methods for API
    O$.extend(fileUpload, {
      _removeFileFromAllFiles:function() {
        fileUpload._currentFile = null;
      },
      _getFile: function() {
        return fileUpload._currentFile;
      }
    });
    //API for fileUpload component
    O$.extend(fileUpload, {
      getFile:function () {
        return fileUpload._currentFile;
      }
    });
  },
  _SpecFileAPIInit:function(file) {
    O$.extend(file,{
      remove:function() {
        if (file != null) {
          if (file.status != O$.FileUploadUtil.Status.IN_PROGRESS) {
            var fileUpload = file._component;
            if (fileUpload._layoutMode == O$.SingleFileUpload._LayoutMode.FULL) {
              fileUpload._els.fileName.innerHTML = "";
              fileUpload._els.status.innerHTML = "";
            }
            fileUpload._els.progressBar.style.display = "none";
            if (file.status == O$.FileUploadUtil.Status.NEW) {
              fileUpload._inputsStorage.childNodes = [];
              fileUpload._numberOfFilesToUpload++;
              fileUpload._events._fireChangeEvent();
            }
            file._component._currentFile = null;
            return true;
          }
          throw "File cannot be removed when it's in progress(IN_PROGRESS status).";
        }
        throw "There is no such file";
      },
      /*stopUpload() method is trying to stop uploading of file. It is not guarantee that file will be stopped*/
      stopUpload:function () {
        if (file != null) {
          if (file.status == O$.FileUploadUtil.Status.IN_PROGRESS) {
            if (file._component._addButtonParent.lastChild._clickHandler) {
              file._component._addButtonParent.lastChild._clickHandler();
              return true;
            } else {
              var stopDiv = O$(file._component.id + "::stopIcon");
              if (stopDiv != null) {
                stopDiv._clickHandler();
                return true;
              }
              throw "Stop file uploading at this mode is unavailable";
            }
          }
          throw "File cannot be stopped when it's not in progress(IN_PROGRESS status).";
        }
        throw "There is no such file";
      }
    })
  },
  _LayoutMode:{
    FULL:{value:"full"},
    COMPACT:{value:"compact"}
  }
};

