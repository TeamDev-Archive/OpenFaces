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

O$.FileUpload = {
  _init: function(componentId, minQuantity, maxQuantity, lengthAlreadyUploadedFiles,
                  fileInfoClass, infoTitleClass, progressBarClass, infoStatusClass,
                  statusLabelNotUploaded, statusLabelInProgress, statusLabelUploaded, statusLabelErrorSize, statusLabelUnexpectedError,
                  acceptedTypesOfFile,  isDuplicateAllowed,
                  addButtonId, addButtonClass, addButtonOnMouseOverClass, addButtonOnMouseDownClass,addButtonOnFocusClass,
                  addButtonDisabledClass,
                  isDisabled,
                  isAutoUpload, tabIndex, progressBarId, statusStoppedText, statusStoppingText, multiUpload,ID,
                  onchangeHandler, onstartHandler, onendHandler,
                  onfilestartHandler, onfileinprogressHandler, onfileendHandler, onwrongfiletypeHandler, ondirectorydroppedHandler,
                  dropTargetCrossoverClass, uploadMode, render, externalDropTargetId, acceptedMimeTypes,
                  directoryDroppedText, wrongFileTypeText, externalBrowseButtonId, showInPopup, popupPositionedByID,
                  popupHorizontalAlignment, popupVerticalAlignment, popupHorizontalDistance, popupVerticalDistance) {

    var fileUpload = O$.initComponent(componentId, null, {
      _minQuantity : minQuantity,
      _maxQuantity : maxQuantity,
      _multiUpload : multiUpload,
      _isAutoUpload : isAutoUpload,
      _filesHTML5:[],
      _allFiles:[],
      _showPopupIfPopupNeeded:function () {
        if (showInPopup && !fileUpload._popupIsShown) {
          var positionedByID = !popupPositionedByID ? externalBrowseButtonId : popupPositionedByID;
          fileUpload._showPopup(positionedByID, popupHorizontalAlignment, popupVerticalAlignment,
                  popupHorizontalDistance, popupVerticalDistance);
          if (!externalBrowseButtonId)
            fileUpload._moveBrowseButtonBackToComponent();
          else
            fileUpload._moveExternalBrowseButtonClickArea(addButtonId);
        }
      },
      _processFileAddingHTML5:function (file) {

      if (isFileNameNotApplied(file.name) || fileUpload._buttons.browseInput.disabled ||
              (!file._fromDnD && file.size == 0) || (file._fromDnD && fileUpload._isDirectory(file))) {
        return false;
      }
      clearAllInfosForUploadedFiles();
      allInfos.style.display = "";
      file._uniqueId = fileUpload._ID + idOfInfoAndInputDiv;
      file._fakeInput = componentId + "::inputs::input" + idOfInfoAndInputDiv + "::form::fileInput";
      file._infoId = idOfInfoAndInputDiv;
      fileUpload._filesHTML5.push(file);
      fileUpload._allFiles.push(new O$.FileUploadUtil.File(O$.FileUpload._SpecFileAPIInit, fileUpload, file._infoId, file.name, O$.FileUploadUtil.Status.NEW, file.size));
      var fileInfo = createInfoWindowForFile(idOfInfoAndInputDiv, file.name);
      var cancelFileDiv = fileInfo.childNodes[3].firstChild;
      setSpecificClickHandlerForCancelBtn(cancelFileDiv, fileInfo);

      function setSpecificClickHandlerForCancelBtn(cancelFileDiv, fileInfo) {
        cancelFileDiv.deleteFileInputFunction = function () {
          fileUpload._removeFileFromAllFiles(file._infoId);
          allInfos.removeChild(fileInfo);
          removeHTML5File(file);
          fileUpload._numberOfFilesToUpload--;
          if (fileUpload._buttons.browseInput.disabled
                  && fileUpload._maxQuantity > (fileUpload._numberOfFilesToUpload + fileUpload._lengthUploadedFiles)) {
            fileUpload._buttons.browseInput.disabled = false;
            O$.setStyleMappings(fileUpload._buttons.browseTitleInput, {disabled : null});
          }
          if ((fileUpload._minQuantity > (fileUpload._numberOfFilesToUpload + fileUpload._lengthUploadedFiles))
                  || fileUpload._numberOfFilesToUpload == 0) {
            if (fileUpload._multiUpload) {
              fileUpload._buttons.upload.style.visibility = "hidden";
            }
          }

          if (allInfos.childNodes.length == 0) {
            if (externalBrowseButtonId && !showInPopup)
              fileUpload._hideBorder();
            if (!showInPopup) {
              fileUpload._buttons.removeAll.style.display = "none";
            }
            allInfos.style.display = "none";
          }
          fileUpload._shouldInvokeFocusNonModifiable = false;
          fileUpload._setFocusOnComponent();
          fileUpload._shouldInvokeFocusNonModifiable = true;

          if (couldCallChangeEvent) {
            fileUpload._events._fireChangeEvent();
          }
        };
        cancelFileDiv._clickHandler = cancelFileDiv.deleteFileInputFunction;
        O$.addEventHandler(cancelFileDiv, "click", cancelFileDiv._clickHandler);
      }

      fileUpload._numberOfFilesToUpload++;
      idOfInfoAndInputDiv++;

      if (fileUpload._maxQuantity != 0
              && fileUpload._maxQuantity <= (fileUpload._numberOfFilesToUpload + fileUpload._lengthUploadedFiles)) {
        fileUpload._buttons.browseInput.disabled = true;
        O$.setStyleMappings(fileUpload._buttons.browseTitleInput, {disabled : addButtonDisabledClass});
      }
      fileUpload._buttons.removeAll.style.display = "block";
      return true;
    },
      _processFileAdding:function (inputForFile) {
        if (isFileNameNotApplied(inputForFile.value)) {
          inputForFile.value = "";
          fileUpload._shouldInvokeFocusEvHandler = true;
          return false;
        }
        clearAllInfosForUploadedFiles();
        allInfos.style.display = "";
        fileUpload._buttons.browse._inFocus = false;
        inputForFile._idInputAndDiv = idOfInfoAndInputDiv;
        fileUpload._createAndAppendComplexInputWithId(inputForFile);
        fileUpload._allFiles.push(new O$.FileUploadUtil.File(O$.FileUpload._SpecFileAPIInit, fileUpload, inputForFile._idInputAndDiv, inputForFile.value, O$.FileUploadUtil.Status.NEW, null));
        fileUpload._numberOfFilesToUpload++;
        idOfInfoAndInputDiv++;
        fileUpload._buttons.browseInput = fileUpload._createInputInAddBtn();
        fileUpload._buttons.browseDivForInput.appendChild(fileUpload._buttons.browseInput);

        O$.addEventHandler(fileUpload._buttons.browseInput, "focus", fileUpload._focusHandler);
        O$.addEventHandler(fileUpload._buttons.browseInput, "blur", fileUpload._blurHandler);

        fileUpload._events._fireChangeEvent();

        var fileInfo = createInfoWindowForFile(inputForFile._idInputAndDiv, inputForFile.value);
        var cancelFileDiv = fileInfo.childNodes[3].firstChild;
        setClickHandlerOnCancelButton(cancelFileDiv, inputForFile, fileInfo);

        function setClickHandlerOnCancelButton(cancelFileDiv, inputForFile, fileInfo) {
          cancelFileDiv.deleteFileInputFunction = function () {
            fileUpload._removeFileFromAllFiles(inputForFile._idInputAndDiv);
            deleteFileInput(inputForFile, fileInfo);
            if (couldCallChangeEvent) {
              fileUpload._events._fireChangeEvent();
            }
            if (allInfos.childNodes.length == 0) {
              fileUpload._buttons.removeAll.style.display = "none";
              allInfos.style.display = "none";
            }
            fileUpload._shouldInvokeFocusEvHandler = false;
            fileUpload._setFocusOnComponent();
            if (!O$.isExplorer()) {
              fileUpload._shouldInvokeFocusEvHandler = true;
            }
          };
          cancelFileDiv._clickHandler = cancelFileDiv.deleteFileInputFunction;
          O$.addEventHandler(cancelFileDiv, "click", cancelFileDiv._clickHandler);
        }

        if (fileUpload._maxQuantity != 0
                && fileUpload._maxQuantity <= (fileUpload._numberOfFilesToUpload + fileUpload._lengthUploadedFiles)) {
          fileUpload._buttons.browseInput.disabled = true;
          O$.setStyleMappings(fileUpload._buttons.browseTitleInput, {disabled:addButtonDisabledClass});
        }
        fileUpload._buttons.removeAll.style.display = "block";
        fileUpload._setUploadButtonAfterFileHaveBeenAdded();
        return true;
      },
      _setFocusOnComponent:function () {
        if (!fileUpload._buttons.browseInput.disabled && fileUpload._buttons.browseInput.focus) {
          fileUpload._buttons.browseInput.focus();
          return;
        }
        if (!fileUpload._buttons.upload.disabled && fileUpload._buttons.upload.focus) {
          fileUpload._buttons.upload.focus();
          return;
        }
        if (!fileUpload._buttons.removeAll.disabled && fileUpload._buttons.removeAll.focus) {
          fileUpload._buttons.removeAll.focus();
          return;
        }
        if (!fileUpload._buttons.stopAll.disabled && fileUpload._buttons.stopAll.focus) {
          fileUpload._buttons.stopAll.focus();
          return;
        }
        fileUpload._events._fireBlurEvent();
      },
      _setUploadButtonAfterFileHaveBeenAdded:function () {
        if (fileUpload._minQuantity <= (fileUpload._numberOfFilesToUpload + fileUpload._lengthUploadedFiles)) {
          if (fileUpload._isAutoUpload) {
            fileUpload.__uploadButtonClickHandler();
          } else {
            fileUpload._buttons.upload.style.visibility = "visible";
          }
        }
      },
      _setClearBtnAndEventHandler:function (infoDiv, infoId) {
        infoDiv.childNodes[3].removeChild(infoDiv.childNodes[3].firstChild);
        var clearFileDiv = fileUpload._buttons.clear.cloneNode(true);
        clearFileDiv.setAttribute("id", fileUpload._buttons.clear.id + infoId);
        infoDiv.childNodes[3].appendChild(clearFileDiv);
        O$.addEventHandler(clearFileDiv, "focus", fileUpload._focusHandler);
        O$.addEventHandler(clearFileDiv, "blur", fileUpload._blurHandler);
        clearFileDiv._clickHandler = function () {
          fileUpload._removeFileFromAllFiles(infoId);
          allInfos.removeChild(infoDiv);
          if (allInfos.childNodes.length == 0) {
            if (externalBrowseButtonId && !showInPopup)
              fileUpload._hideBorder();
            if (!showInPopup) {
              fileUpload._buttons.removeAll.style.display = "none";
            }
            allInfos.style.display = "none";
          }
          fileUpload._shouldInvokeFocusNonModifiable = false;
          fileUpload._setFocusOnComponent();
          fileUpload._shouldInvokeFocusNonModifiable = true;
        };
        O$.addEventHandler(clearFileDiv, "click", clearFileDiv._clickHandler);
        fileUpload._chromeAndSafariFocusFix(clearFileDiv);
      },
      _prepareUIWhenAllRequestsFinished:function (shouldCheckRequest) {
        if (fileUpload._inputsStorage.childNodes.length == 0 && fileUpload._filesHTML5.length == 0) { // if all files are  already uploaded
          if (shouldCheckRequest)
            setTimeout(function() {
              fileUpload._sendCheckRequest(fileUpload._allFiles);
            }, 500);

          fileUpload._numberOfFilesToUpload = 0;
          if (fileUpload._multiUpload) {
            fileUpload._buttons.stopAll.style.display = "none";
            fileUpload._buttons.stopAll.style.visibility = "visible";
            fileUpload._buttons.removeAll.style.display = "block";
            fileUpload._buttons.upload.style.visibility = "visible";
            fileUpload._buttons.stopAll._clicked = false;
          }
          fileUpload._buttons.browseInput.disabled = false;
          fileUpload._inUploading = false;
          O$.setStyleMappings(fileUpload._buttons.browseTitleInput, {disabled : null});
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
                var infoDiv = O$(allInfos.id + inputForFile._idInputAndDiv);
                var fileForAPI = fileUpload._getFile(inputForFile._idInputAndDiv);
                if (portionData['isFileSizeExceed'] == "true") {
                  infoDiv._status = O$.FileUploadUtil.Status.SIZE_LIMIT_EXCEEDED;
                  fileForAPI.status = O$.FileUploadUtil.Status.SIZE_LIMIT_EXCEEDED;
                  fileUpload._inputsStorage.removeChild(inputForFile.parentNode.parentNode);
                  infoDiv.childNodes[2].innerHTML = fileUpload._statuses.sizeLimit._update(null, portionData['size']);
                  fileUpload._setStatusforFileWithId(inputForFile._uniqueId, "SIZE_LIMIT_EXCEEDED");
                  fileUpload._events._fireFileEndEvent(fileForAPI);
                  fileUpload._setClearBtnAndEventHandler(infoDiv, inputForFile._idInputAndDiv);
                  if (endHandler) {
                    endHandler();
                  }
                  fileUpload._prepareUIWhenAllRequestsFinished(true);
                } else {
                  var percents = portionData['progressInPercent'];
                  if (percents != 100) {
                    if (!inputForFile._isInterrupted) {
                      if (inputForFile._wantToInterrupt) {
                        fileUpload._sendIsStoppedRequest(inputForFile, infoDiv, fileForAPI,endHandler);
                      } else {
                        if (portionData['size']) {
                          infoDiv.childNodes[2].innerHTML = fileUpload._statuses.inProgress._update(portionData['size'] * (percents / 100), portionData['size']);
                        }
                      }
                      if (infoDiv.childNodes[1].firstChild.getValue() == percents) {
                        if (!inputForFile._percentsEqualsTimes) {
                          inputForFile._percentsEqualsTimes = 0;
                        }
                        inputForFile._percentsEqualsTimes++;
                        if (inputForFile._percentsEqualsTimes > fileUpload._numberOfErrorRequest) {
                          fileUpload._sendIsErrorRequest(inputForFile, infoDiv, fileForAPI,endHandler);
                        }
                        if (inputForFile._percentsEqualsTimes > fileUpload._numberOfFailedRequest) {
                          inputForFile._isInterrupted = true;
                          fileUpload._sendInformThatRequestFailed(inputForFile._uniqueId);
                          infoDiv._status = O$.FileUploadUtil.Status.FAILED;
                          fileForAPI.status = O$.FileUploadUtil.Status.FAILED;
                          fileUpload._inputsStorage.removeChild(inputForFile.parentNode.parentNode);
                          infoDiv.childNodes[2].innerHTML = fileUpload._statuses.failed;
                          fileUpload._setStatusforFileWithId(inputForFile._uniqueId, "FAILED");
                          infoDiv.childNodes[1].firstChild.setValue(0);
                          fileForAPI.progress = 0;
                          fileUpload._events._fireFileEndEvent(fileForAPI);
                          fileUpload._setClearBtnAndEventHandler(infoDiv, inputForFile._idInputAndDiv);
                          if (endHandler) {
                            endHandler();
                          }
                          fileUpload._prepareUIWhenAllRequestsFinished(true);
                        }
                      } else {
                        inputForFile._percentsEqualsTimes = 0;
                      }
                      infoDiv.childNodes[1].firstChild.setValue(percents);
                      fileForAPI.progress = percents/100;
                      fileUpload._events._fireFileInProgressEvent(fileForAPI);
                      fileUpload._callProgressRequest(inputForFile, endHandler);
                    }
                  } else {// when file already uploaded
                    infoDiv.childNodes[1].firstChild.setValue(percents);
                    fileForAPI.progress = percents/100;
                    fileUpload._lengthUploadedFiles++;
                    /*removing input*/
                    fileUpload._inputsStorage.removeChild(inputForFile.parentNode.parentNode); // delete divForFileInput
                    // infoDiv updating
                    infoDiv._status = O$.FileUploadUtil.Status.SUCCESSFUL;
                    infoDiv.childNodes[2].innerHTML = fileUpload._statuses.uploaded._update(null, portionData['size']);
                    fileUpload._setStatusforFileWithId(inputForFile._uniqueId, "SUCCESSFUL");
                    fileUpload._setClearBtnAndEventHandler(infoDiv, inputForFile._idInputAndDiv);
                    if (endHandler) {
                      endHandler();
                    }
                    fileForAPI.status = O$.FileUploadUtil.Status.SUCCESSFUL;
                    fileUpload._events._fireFileEndEvent(fileForAPI);
                    fileUpload._prepareUIWhenAllRequestsFinished(true);
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
                  var infoDiv = O$(allInfos.id + file._infoId);
                  var fileForAPI = fileUpload._getFile(file._infoId);
                  if (portionData['isFileSizeExceed'] == "true") {
                    infoDiv._status = O$.FileUploadUtil.Status.SIZE_LIMIT_EXCEEDED;
                    fileForAPI.status = O$.FileUploadUtil.Status.SIZE_LIMIT_EXCEEDED;
                    removeHTML5File(file);
                    infoDiv.childNodes[2].innerHTML = fileUpload._statuses.sizeLimit._update(null, portionData['size']);
                    fileUpload._setStatusforFileWithId(file._uniqueId, "SIZE_LIMIT_EXCEEDED");
                    fileUpload._events._fireFileEndEvent(fileForAPI);
                    fileUpload._setClearBtnAndEventHandler(infoDiv, file._infoId);
                    if (endHandler) {
                      endHandler();
                    }
                    fileUpload._prepareUIWhenAllRequestsFinished(true);
                  } else {
                    var percents = portionData['progressInPercent'];
                    if (percents != 100) {
                      if (!file._isInterrupted) {
                        if (file._wantToInterrupt) {
                          fileUpload._sendIsStoppedRequestHTML5(file, infoDiv, fileForAPI, endHandler);
                        } else {
                          if (portionData['size']) {
                            infoDiv.childNodes[2].innerHTML = fileUpload._statuses.inProgress._update(portionData['size'] * (percents / 100), portionData['size']);
                          }
                        }
                        if (infoDiv.childNodes[1].firstChild.getValue() == percents) {
                          if (!file._percentsEqualsTimes) {
                            file._percentsEqualsTimes = 0;
                          }
                          file._percentsEqualsTimes++;
                          if (file._percentsEqualsTimes > fileUpload._numberOfErrorRequest) {
                            fileUpload._sendIsErrorRequestHTML5(file, infoDiv, fileForAPI, endHandler);
                          }
                          if (file._percentsEqualsTimes > fileUpload._numberOfFailedRequest) {
                            file._isInterrupted = true;
                            fileUpload._sendInformThatRequestFailed(file._uniqueId);
                            infoDiv._status = O$.FileUploadUtil.Status.FAILED;
                            fileForAPI.status = O$.FileUploadUtil.Status.FAILED;
                            removeHTML5File(file);
                            infoDiv.childNodes[2].innerHTML = fileUpload._statuses.failed;
                            fileUpload._setStatusforFileWithId(file._uniqueId, "FAILED");
                            infoDiv.childNodes[1].firstChild.setValue(0);
                            fileForAPI.progress = 0;
                            fileUpload._events._fireFileEndEvent(fileForAPI);
                            fileUpload._setClearBtnAndEventHandler(infoDiv, file._infoId);
                            if (endHandler) {
                              endHandler();
                            }
                            fileUpload._prepareUIWhenAllRequestsFinished(true);
                          }
                        } else {
                          file._percentsEqualsTimes = 0;
                        }
                        infoDiv.childNodes[1].firstChild.setValue(percents);
                        fileForAPI.progress = percents / 100;
                        fileUpload._events._fireFileInProgressEvent(fileForAPI);
                        setTimeout(function () {
                          fileUpload._progressHTMl5Request(file, request, endHandler);
                        }, 500);
                      }
                    } else {// when file already uploaded
                      infoDiv.childNodes[1].firstChild.setValue(percents);
                      fileForAPI.progress = percents / 100;
                      fileUpload._lengthUploadedFiles++;
                      /*removing file*/
                      removeHTML5File(file);
                      // infoDiv updating
                      infoDiv._status = O$.FileUploadUtil.Status.SUCCESSFUL;
                      infoDiv.childNodes[2].innerHTML = fileUpload._statuses.uploaded._update(null, portionData['size']);
                      fileUpload._setStatusforFileWithId(file._uniqueId, "SUCCESSFUL");
                      fileUpload._setClearBtnAndEventHandler(infoDiv, file._infoId);
                      fileForAPI.status = O$.FileUploadUtil.Status.SUCCESSFUL;
                      fileUpload._events._fireFileEndEvent(fileForAPI);
                      if (endHandler) {
                        endHandler();
                      }
                      fileUpload._prepareUIWhenAllRequestsFinished(true);
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
                    infoDiv._status = O$.FileUploadUtil.Status.FAILED;
                    fileForAPI.status = O$.FileUploadUtil.Status.FAILED;
                    fileUpload._inputsStorage.removeChild(inputForFile.parentNode.parentNode);
                    infoDiv.childNodes[2].innerHTML = fileUpload._statuses.failed;
                    fileUpload._setStatusforFileWithId(inputForFile._uniqueId, "FAILED");
                    infoDiv.childNodes[1].firstChild.setValue(0);
                    fileForAPI.progress = 0;
                    fileUpload._events._fireFileEndEvent(fileForAPI);
                    fileUpload._setClearBtnAndEventHandler(infoDiv, inputForFile._idInputAndDiv);
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
                    infoDiv._status = O$.FileUploadUtil.Status.FAILED;
                    fileForAPI.status = O$.FileUploadUtil.Status.FAILED;
                    removeHTML5File(file);
                    infoDiv.childNodes[2].innerHTML = fileUpload._statuses.failed;
                    fileUpload._setStatusforFileWithId(file._uniqueId, "FAILED");
                    infoDiv.childNodes[1].firstChild.setValue(0);
                    fileForAPI.progress = 0;
                    fileUpload._events._fireFileEndEvent(fileForAPI);
                    fileUpload._setClearBtnAndEventHandler(infoDiv, file._infoId);
                    if (endHandler) {
                      endHandler();
                    }
                    fileUpload._prepareUIWhenAllRequestsFinished(true);
                  }
                }, null, true);
      },
      _sendIsStoppedRequest:function (inputForFile, infoDiv, fileForAPI, endHandler) {
        O$.Ajax.requestComponentPortions(fileUpload.id, ["nothing"],
                JSON.stringify({stoppedRequest:true, uniqueIdOfFile:inputForFile._uniqueId}),
                function (fileUpload, portionName, portionHTML, portionScripts, portionData) {
                  inputForFile._isInterrupted = true;
                  infoDiv._status = O$.FileUploadUtil.Status.STOPPED;
                  fileUpload._inputsStorage.removeChild(inputForFile.parentNode.parentNode);
                  infoDiv.childNodes[2].innerHTML = fileUpload._statuses.stopped;
                  fileUpload._setStatusforFileWithId(inputForFile._uniqueId, "STOPPED");
                  fileUpload._setClearBtnAndEventHandler(infoDiv, inputForFile._idInputAndDiv);
                  if (endHandler) {
                    endHandler();
                  }
                  fileForAPI.status = O$.FileUploadUtil.Status.STOPPED;
                  infoDiv.childNodes[1].firstChild.setValue(0);
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
                    infoDiv._status = O$.FileUploadUtil.Status.STOPPED;
                    removeHTML5File(file);
                    infoDiv.childNodes[2].innerHTML = fileUpload._statuses.stopped;
                    fileUpload._setStatusforFileWithId(file._uniqueId, "STOPPED");
                    fileUpload._setClearBtnAndEventHandler(infoDiv, file._infoId);
                    fileForAPI.status = O$.FileUploadUtil.Status.STOPPED;
                    infoDiv.childNodes[1].firstChild.setValue(0);
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
        new function setBehaviorForDragAndDropArea() {
          var area = fileUpload._getDropTargetArea(componentId + "::footer::dragArea");
          O$.FileUploadUtil._initDragArea(area);
          O$.extend(area, {
            hide:function () {
              area.style.display = "none";
            },
            show:function () {
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
            onwrongfiletypeHandler, ondirectorydroppedHandler);
    if (!fileUpload._multiUpload) {
      fileUpload._isAutoUpload = true;
      //fileUpload._maxQuantity = 1;
    }
    //getting clear,stop,cancel, progressBar facet for each info window
    fileUpload._elementsCont = O$(componentId + "::elements");

    fileUpload._buttons.clear = fileUpload._getFacet("::clearFacet");
    fileUpload._buttons.cancel = fileUpload._getFacet("::removeFacet");
    fileUpload._buttons.stop = fileUpload._getFacet("::stopFacet");

    var progressBar= O$(progressBarId);
    if (!O$.isExplorer())
      fileUpload._elementsCont.removeChild(progressBar);

    var idOfInfoAndInputDiv = 1;

    if (externalBrowseButtonId && !showInPopup)
      fileUpload._buttons.upload = O$(componentId + "::footer::uploadFacet").lastChild;
    else
      fileUpload._buttons.upload = O$(componentId + "::header::uploadFacet").lastChild;
    fileUpload._buttons.removeAll = O$(componentId + "::footer::removeAllFacet").lastChild;
    fileUpload._buttons.stopAll = O$(componentId + "::footer::stopAllFacet").lastChild;
    setTabIndexForAllButtons(tabIndex);

    var allInfos = O$(componentId + "::infoDiv");
    if (O$.isExplorer()) {
      if (O$.isQuirksMode() ||
              (O$.isExplorer() && (document.documentMode == 7 || document.documentMode == 6))) {
        /*In IE6-7 because of extra tag <tbody>*/
        var parentOfAllInfos = allInfos;
        allInfos = parentOfAllInfos.firstChild;
        var tempId = parentOfAllInfos.id;
        parentOfAllInfos.id = "";
        allInfos.id = tempId;
      }

    }

    fileUpload._inputsStorage = fileUpload._createStructureForInputs();
    var couldCallChangeEvent = true;

    if (!O$.isExplorer())
      fileUpload.removeChild(fileUpload._elementsCont);

    fileUpload._setUpBrowseButton(addButtonId);

    fileUpload._addButtonParent = fileUpload._buttons.browse.parentNode;
    if (externalBrowseButtonId){
      if (!showInPopup)
        fileUpload._hideBorder();
      fileUpload._setUpExternalBrowseButton(externalBrowseButtonId);
    }
    if(showInPopup){
      fileUpload._initPopup();
      if (!externalBrowseButtonId)
        fileUpload._moveBrowseButtonOutOfComponent();
    }


    O$.extend(fileUpload, {
              __uploadButtonClickHandler:function () {
                fileUpload._buttons.browseInput.disabled = true;
                fileUpload._inUploading = true;
                O$.setStyleMappings(fileUpload._buttons.browseTitleInput, {disabled : addButtonDisabledClass});
                if (fileUpload._multiUpload) {
                  fileUpload._buttons.upload.style.visibility = "hidden";
                  fileUpload._buttons.removeAll.style.display = "none";    // to prevent changing of file list when uploading in progress
                  fileUpload._buttons.stopAll.style.display = "block";
                } else {
                  fileUpload._buttons.removeAll.style.display = "none";
                }
                fileUpload._listOfids = [];
                /*prepare a list of files that would be uploaded*/
                for (var inputsIndex = 0; inputsIndex < fileUpload._inputsStorage.childNodes.length; inputsIndex++) {
                  var form = fileUpload._inputsStorage.childNodes[inputsIndex].firstChild;
                  fileUpload._addFileToInfoFileList(form);
                }
                /*specific behavior for uploading files with drag and drop*/
                for (var html5FilesIndex = 0; html5FilesIndex < fileUpload._filesHTML5.length; html5FilesIndex++) {
                  var html5File = fileUpload._filesHTML5[html5FilesIndex];
                  fileUpload._addFileToInfoFileListHTML5(html5File);
                }
                fileUpload._events._fireStartEvent(fileUpload._allFiles);
                var uri = fileUpload._getDocumentURI();
                function setupUIForUpload(inputForFile, infoDiv, fileForAPI) {
                  function setStopButtonBehavior(inputForFile, infoDiv) {
                    var deleteFunc = infoDiv.childNodes[3].firstChild.deleteFileInputFunction;
                    infoDiv.childNodes[3].removeChild(infoDiv.childNodes[3].firstChild);
                    var stopFileDiv = fileUpload._buttons.stop.cloneNode(true);
                    stopFileDiv.deleteFileInputFunction = deleteFunc;
                    stopFileDiv.setAttribute("id", fileUpload._buttons.stop.id + inputForFile._idInputAndDiv);
                    infoDiv.childNodes[3].appendChild(stopFileDiv);
                    O$.addEventHandler(stopFileDiv, "focus", fileUpload._focusHandler);
                    O$.addEventHandler(stopFileDiv, "blur", fileUpload._blurHandler);
                    stopFileDiv._clickHandler = function () {
                      stopFileDiv.style.visibility = "hidden";
                      infoDiv.childNodes[2].innerHTML = statusStoppingText;
                      var iframe = inputForFile.nextSibling;
                      iframe.src = "";
                      inputForFile._wantToInterrupt = true;
                    };
                    O$.addEventHandler(stopFileDiv, "click", stopFileDiv._clickHandler);
                    fileUpload._chromeAndSafariFocusFix(stopFileDiv);
                  }
                  infoDiv._status = O$.FileUploadUtil.Status.IN_PROGRESS;
                  fileForAPI.status = O$.FileUploadUtil.Status.IN_PROGRESS;
                  infoDiv.childNodes[2].innerHTML = fileUpload._statuses.inProgress._update(0, "");
                  setStopButtonBehavior(inputForFile, infoDiv);
                }

                function setupUIForUploadHTML5(file, infoDiv, request, fileForAPI) {
                  function setStopButtonBehaviorHTML5(file, infoDiv, request) {
                    var deleteFunc = infoDiv.childNodes[3].firstChild.deleteFileInputFunction;
                    infoDiv.childNodes[3].removeChild(infoDiv.childNodes[3].firstChild);
                    var stopFileDiv = fileUpload._buttons.stop.cloneNode(true);
                    stopFileDiv.deleteFileInputFunction = deleteFunc;
                    stopFileDiv.setAttribute("id", fileUpload._buttons.stop.id + file._infoId);
                    infoDiv.childNodes[3].appendChild(stopFileDiv);
                    O$.addEventHandler(stopFileDiv, "focus", fileUpload._focusHandler);
                    O$.addEventHandler(stopFileDiv, "blur", fileUpload._blurHandler);
                    stopFileDiv._clickHandler = function () {
                      stopFileDiv.style.visibility = "hidden";
                      infoDiv.childNodes[2].innerHTML = statusStoppingText;
                      request.abort();
                      file._wantToInterrupt = true;
                    };
                    O$.addEventHandler(stopFileDiv, "click", stopFileDiv._clickHandler);
                    fileUpload._chromeAndSafariFocusFix(stopFileDiv);
                  }
                  infoDiv._status = O$.FileUploadUtil.Status.IN_PROGRESS;
                  fileForAPI.status = O$.FileUploadUtil.Status.IN_PROGRESS;
                  infoDiv.childNodes[2].innerHTML = fileUpload._statuses.inProgress._update(0, file.size);
                  setStopButtonBehaviorHTML5(file, infoDiv, request);
                }

                if (uploadMode == "parallel") {
                  return new function() {

                    for (var inputsIndex = 0; inputsIndex < fileUpload._inputsStorage.childNodes.length; inputsIndex++) {
                      var inputDiv = fileUpload._inputsStorage.childNodes[inputsIndex];
                      var form = inputDiv.childNodes[0];
                      var iframe = form.childNodes[1];
                      var fileInput = form.childNodes[0];
                      var fileOfAPI = fileUpload._getFile(fileInput._idInputAndDiv);
                      var fileInfo = O$(allInfos.id + fileInput._idInputAndDiv);
                      fileInfo.childNodes[3].firstChild.style.visibility = "hidden";
                      form.target = iframe.name;
                      form.action = uri + "&idOfFile=" + fileInput._idInputAndDiv;
                      form.submit();
                      form.target = "_self";
                      fileUpload._callProgressRequest(fileInput);
                      fileOfAPI.status = O$.FileUploadUtil.Status.IN_PROGRESS;
                      setupUIForUpload(fileInput, fileInfo, fileOfAPI);
                      fileUpload._events._fireFileStartEvent(fileOfAPI);
                    }

                    for (var html5FilesIndex = 0; html5FilesIndex < fileUpload._filesHTML5.length; html5FilesIndex++) {
                      var html5File = fileUpload._filesHTML5[html5FilesIndex];
                      var fileInfo = O$(allInfos.id + html5File._infoId);
                      var fileOfAPI = fileUpload._getFile(html5File._infoId);
                      fileInfo.childNodes[3].firstChild.style.visibility = "hidden";

                      var request = fileUpload._sendFileRequest(html5File,uri);
                      fileUpload._callFileProgressForHTML5Request(html5File, request);
                      fileOfAPI.status = O$.FileUploadUtil.Status.IN_PROGRESS;
                      setupUIForUploadHTML5(html5File, fileInfo, request,fileOfAPI);
                      fileUpload._events._fireFileStartEvent(fileOfAPI);
                    }
                  }();
                } else {
                  return new function() {
                    function onEndHandler() {
                      for (var inputsIndex = 0; inputsIndex < fileUpload._inputsStorage.childNodes.length; inputsIndex++) {
                        var inputDiv = fileUpload._inputsStorage.childNodes[inputsIndex];
                        var form = inputDiv.childNodes[0];
                        var iframe = form.childNodes[1];
                        var fileInput = form.childNodes[0];
                        var fileOfAPI = fileUpload._getFile(fileInput._idInputAndDiv);
                        var fileInfo = O$(allInfos.id + fileInput._idInputAndDiv);
                        if (fileOfAPI.status == O$.FileUploadUtil.Status.NEW) {
                          fileInfo.childNodes[3].firstChild.style.visibility = "hidden";
                          form.target = iframe.name;
                          form.action = uri + "&idOfFile=" + fileInput._idInputAndDiv;
                          form.submit();
                          form.target = "_self";
                          fileUpload._callProgressRequest(fileInput, onEndHandler);
                          fileOfAPI.status = O$.FileUploadUtil.Status.IN_PROGRESS;
                          setupUIForUpload(fileInput, fileInfo, fileOfAPI);
                          fileUpload._events._fireFileStartEvent(fileOfAPI);
                          if (fileUpload._buttons.stopAll._clicked) {
                            fileOfAPI.stopUpload();
                          }
                          return;
                        }
                      }

                      for (var html5FilesIndex = 0; html5FilesIndex < fileUpload._filesHTML5.length; html5FilesIndex++) {
                        var html5File = fileUpload._filesHTML5[html5FilesIndex];
                        var fileInfo = O$(allInfos.id + html5File._infoId);
                        var fileOfAPI = fileUpload._getFile(html5File._infoId);
                        if (fileOfAPI.status  == O$.FileUploadUtil.Status.NEW) {
                          fileInfo.childNodes[3].firstChild.style.visibility = "hidden";
                          var request = fileUpload._sendFileRequest(html5File,uri);
                          fileUpload._callFileProgressForHTML5Request(html5File, request, onEndHandler);
                          fileOfAPI.status = O$.FileUploadUtil.Status.IN_PROGRESS;
                          setupUIForUploadHTML5(html5File, fileInfo, request, fileOfAPI);
                          fileUpload._events._fireFileStartEvent(fileOfAPI);
                          if (fileUpload._buttons.stopAll._clicked) {
                            fileOfAPI.stopUpload();
                          }
                          return;
                        }
                      }
                      //all uploaded
                    }
                    onEndHandler();
                  }();
                }
              },
              __clearAllButtonClickHandler:function () {
                if (showInPopup){
                  fileUpload._hidePopup();
                  if(!externalBrowseButtonId)
                    fileUpload._moveBrowseButtonOutOfComponent();
                  else
                    fileUpload._moveExternalBrowseButtonClickArea(externalBrowseButtonId);
                }
                fileUpload._shouldInvokeFocusNonModifiable = false;
                var infosLength = allInfos.childNodes.length;
                couldCallChangeEvent = false;
                var isCallingChangeEventNeeded = false;
                for (var index = 0; index < infosLength; index++) {
                  if (allInfos.childNodes[0].childNodes[3].firstChild.deleteFileInputFunction) {
                    isCallingChangeEventNeeded = true;
                  }
                  allInfos.childNodes[0].childNodes[3].firstChild._clickHandler();
                }
                couldCallChangeEvent = true;
                if (isCallingChangeEventNeeded) {
                  fileUpload._events._fireChangeEvent();
                }

                fileUpload._buttons.removeAll.style.display = "none";
                //setFocusOnComponent();
                fileUpload._shouldInvokeFocusNonModifiable = true;
                allInfos.style.display = "none";
              }
            }
    );
    initHeaderButtons(true);
    setFocusBlurBehaviour();
    setUploadButtonBehaviour();
    if (window._loaded) {
      fileUpload._initializeDropTarget();
    } else {
      O$.addEventHandler(window, "load", function setupDropTarget() {
        O$.removeEventHandler(window, "load", setupDropTarget);
        fileUpload._initializeDropTarget();
        window._loaded = true;
      });
    }
    O$.FileUpload._initFileUploadAPI(fileUpload);

    if (O$.isChrome() || O$.isSafari()) {
      O$.addEventHandler(fileUpload._buttons.removeAll, "mousedown", function () {
        fileUpload._shouldInvokeFocusNonModifiable = false;
      });
      O$.addEventHandler(fileUpload._buttons.removeAll, "mouseup", function () {
        fileUpload._shouldInvokeFocusNonModifiable = true;
      });
    }
    O$.addEventHandler(fileUpload._buttons.removeAll, "click", fileUpload.__clearAllButtonClickHandler);

    if (O$.isChrome() || O$.isSafari()) {
      O$.addEventHandler(fileUpload._buttons.stopAll, "mousedown", function () {
        fileUpload._shouldInvokeFocusNonModifiable = false;
      });
      O$.addEventHandler(fileUpload._buttons.stopAll, "mouseup", function () {
        fileUpload._shouldInvokeFocusNonModifiable = true;
      });
    }
    O$.addEventHandler(fileUpload._buttons.stopAll, "click", fileUpload.stopAllUploads);

    O$.addEventHandler(document, "sessionexpired", function () {
      for (var i = 0; i < fileUpload._allFiles.length; i++) {
        var f = fileUpload._allFiles[i];
        if (f.getStatus() == O$.FileUploadUtil.Status.IN_PROGRESS) {
          var infoDiv = O$(allInfos.id + f._id);
          infoDiv._status = O$.FileUploadUtil.Status.FAILED;
          f.status = O$.FileUploadUtil.Status.FAILED;
          infoDiv.childNodes[2].innerHTML = fileUpload._statuses.failed;
          infoDiv.childNodes[3].firstChild._clickHandler = infoDiv.childNodes[3].firstChild.deleteFileInputFunction;
        }
      }
      fileUpload._numberOfFilesToUpload = 0;
      fileUpload._buttons.browseInput.disabled = false;
      fileUpload._inUploading = false;
      fileUpload.removeAllFiles();

      if (fileUpload._multiUpload) {
        fileUpload._buttons.stopAll.style.display = "none";
        fileUpload._buttons.stopAll.style.visibility = "visible";
        fileUpload._buttons.removeAll.style.display = "block";
        fileUpload._buttons.upload.style.visibility = "visible";
      }

      O$.setStyleMappings(fileUpload._buttons.browseTitleInput, {disabled:null});
      initHeaderButtons();
    });

    function deleteFileInput(inputForFile, infoWindow) {
      fileUpload._inputsStorage.removeChild(inputForFile.parentNode.parentNode);
      allInfos.removeChild(infoWindow);
      fileUpload._numberOfFilesToUpload--;
      if (fileUpload._buttons.browseInput.disabled
              && fileUpload._maxQuantity > (fileUpload._numberOfFilesToUpload + fileUpload._lengthUploadedFiles)) {
        fileUpload._buttons.browseInput.disabled = false;
        O$.setStyleMappings(fileUpload._buttons.browseTitleInput, {disabled : null});
      }
      if ((fileUpload._minQuantity > (fileUpload._numberOfFilesToUpload + fileUpload._lengthUploadedFiles))
              || fileUpload._numberOfFilesToUpload == 0) {
        if (fileUpload._multiUpload) {
          fileUpload._buttons.upload.style.visibility = "hidden";
        }
      }
    }

    function isFileNameNotApplied(filename) {
      function isFileNameAlreadyExist(filename) {
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
      }

      return (filename == "" || !fileUpload._isAcceptedTypeOfFile(filename)
              || (!isDuplicateAllowed && isFileNameAlreadyExist(filename)))
    }
    function createInfoWindowForFile(fileId, filename) {

      var infoWindow = document.createElement("tr");
      infoWindow._status = O$.FileUploadUtil.Status.NEW;

      var fileNameTD = document.createElement("td");
      var fileNameEl = document.createElement("div");
      O$.setStyleMappings(fileNameEl, {fileName : infoTitleClass});
      fileNameEl.innerHTML = fileUpload._getFileName(filename);
      fileNameTD.appendChild(fileNameEl);
      infoWindow.appendChild(fileNameTD);

      var progressTD = document.createElement("td");
      O$.setStyleMappings(progressTD, {progress : progressBarClass});
      var progressFacet = progressBar.cloneNode(true);
      progressFacet.setAttribute("id", progressBarId + fileId);
      progressTD.appendChild(progressFacet);
      infoWindow.appendChild(progressTD);

      var statusTD = document.createElement("td");
      O$.setStyleMappings(statusTD, {status : infoStatusClass});
      if (fileUpload._statuses.newOne != null)
        statusTD.innerHTML = fileUpload._statuses.newOne;
      infoWindow.appendChild(statusTD);

      var cancelFileTD = document.createElement("td");
      cancelFileTD.style.width = "1px";
      cancelFileTD.style.verticalAlign = "middle";
      var cancelFileDiv = fileUpload._buttons.cancel.cloneNode(true);
      cancelFileDiv.setAttribute("id", fileUpload._buttons.cancel.id + fileId);
      if (O$.isExplorer()) {
        O$.addEventHandler(cancelFileDiv, "mouseup", function () {
          fileUpload._shouldInvokeFocusNonModifiable = true;
        });
      }
      O$.addEventHandler(cancelFileDiv, "focus", fileUpload._focusHandler);
      O$.addEventHandler(cancelFileDiv, "blur", fileUpload._blurHandler);

      fileUpload._chromeAndSafariFocusFix(cancelFileDiv);

      cancelFileTD.appendChild(cancelFileDiv);
      infoWindow.appendChild(cancelFileTD);

      infoWindow.setAttribute("id", allInfos.id + fileId);
      O$.setStyleMappings(infoWindow, {row : fileInfoClass});
      if (!fileUpload._multiUpload) {
        allInfos.innerHTML = "";
      }
      allInfos.appendChild(infoWindow);
      O$.ProgressBar.initCopyOf(progressBar, progressFacet);
      return infoWindow;
    }

    function initHeaderButtons(firstTime) {
      if (firstTime) {
        fileUpload._buttons.stopAll.style.display = "none";
      }
      if (fileUpload._isDisabled) {
        fileUpload._buttons.browseInput.disabled = true;
        O$.setStyleMappings(fileUpload._buttons.browseTitleInput, {disabled : addButtonDisabledClass});
        fileUpload._buttons.removeAll.style.display = "none";
      } else {
        if (fileUpload._maxQuantity != 0 && fileUpload._lengthUploadedFiles == fileUpload._maxQuantity) {
          fileUpload._buttons.browseInput.disabled = true;
          O$.setStyleMappings(fileUpload._buttons.browseTitleInput, {disabled : addButtonDisabledClass});
        } else {
          fileUpload._buttons.browseInput.disabled = false;
        }

        if (allInfos.childNodes.length == 0) {
          fileUpload._buttons.removeAll.style.display = "none";
        }
      }

      if (!fileUpload._multiUpload) {
        fileUpload._buttons.upload.style.display = "none";
      } else {
        fileUpload._buttons.upload.style.visibility = "hidden";
      }
    }

    function setUploadButtonBehaviour() {
      fileUpload._chromeAndSafariFocusFix(fileUpload._buttons.upload);
      O$.addEventHandler(fileUpload._buttons.upload, "click", fileUpload.__uploadButtonClickHandler);

    }

    function setFocusBlurBehaviour() {
      O$.addEventHandler(fileUpload._buttons.browseInput, "focus", fileUpload._focusHandler);
      O$.addEventHandler(fileUpload._buttons.upload, "focus", fileUpload._focusHandler);
      O$.addEventHandler(fileUpload._buttons.removeAll, "focus", fileUpload._focusHandler);
      O$.addEventHandler(fileUpload._buttons.stopAll, "focus", fileUpload._focusHandler);

      O$.addEventHandler(fileUpload._buttons.browseInput, "blur", fileUpload._blurHandler);
      O$.addEventHandler(fileUpload._buttons.upload, "blur", fileUpload._blurHandler);
      O$.addEventHandler(fileUpload._buttons.removeAll, "blur", fileUpload._blurHandler);
      O$.addEventHandler(fileUpload._buttons.stopAll, "blur", fileUpload._blurHandler);
      if (fileUpload._buttons.browseTitleInput.focus) {
        O$.addEventHandler(fileUpload._buttons.browseTitleInput, "focus", fileUpload._focusHandler);
        O$.addEventHandler(fileUpload._buttons.browseTitleInput, "blur", fileUpload._blurHandler);
      }
    }

    function setTabIndexForAllButtons(tabIndex) {
      if (tabIndex != -1) {
        var buttons = [fileUpload._buttons.upload,fileUpload._buttons.removeAll,fileUpload._buttons.stopAll,fileUpload._buttons.clear,fileUpload._buttons.stop,fileUpload._buttons.cancel];
        buttons.forEach(function(button) {
          button.setAttribute("tabindex", tabIndex);
        });
      }
    }

    function removeHTML5File(file) {
      for (var index = 0; index < fileUpload._filesHTML5.length; index++) {
        if (fileUpload._filesHTML5[index] == file) {
          fileUpload._filesHTML5.splice(index, 1);
          break;
        }
      }
    }

    function clearAllInfosForUploadedFiles() {
      for (var index = 0; index < fileUpload._allFiles.length; index++) {
        var file = fileUpload._allFiles[index];
        if (file.status == O$.FileUploadUtil.Status.SUCCESSFUL
                || file.status == O$.FileUploadUtil.Status.STOPPED
                || file.status == O$.FileUploadUtil.Status.FAILED
                || file.status == O$.FileUploadUtil.Status.SIZE_LIMIT_EXCEEDED) {
          var fileInfo = O$(fileUpload.id + "::infoDiv" + file._id);
          fileInfo.childNodes[3].firstChild._clickHandler();
          index--;
        }
      }
    }
  },
  _initFileUploadAPI:function (fileUpload) {
    //Helper methods for API
    O$.extend(fileUpload, {
      _removeFileFromAllFiles:function(id) {
        for (var index = 0; index < fileUpload._allFiles.length; index++) {
          if (fileUpload._allFiles[index]._id == id) {
            fileUpload._allFiles.splice(index, 1);
            break;
          }
        }
      },
      _getFile: function(id) {
        for (var index = 0; index < fileUpload._allFiles.length; index++) {
          if (fileUpload._allFiles[index]._id == id) {
            return fileUpload._allFiles[index];
          }
        }
        return null;
      }
    });
    //API for fileUpload component
    O$.extend(fileUpload, {
      getFiles:function () {
        return fileUpload._allFiles;
      },
      uploadAllFiles:function() {
        if (!fileUpload._inUploading) {
          if (fileUpload._numberOfFilesToUpload > 0
                  && fileUpload._minQuantity <= (fileUpload._numberOfFilesToUpload + fileUpload._lengthUploadedFiles)) {
            fileUpload.__uploadButtonClickHandler();
            return true;
          }
          throw "There is nothing to upload.";
        }
        throw "Uploading is already started.";
      },
      /*This method is removing all information windows. Make sure, that there is no files which in upload process*/
      removeAllFiles:function() {
        if (!fileUpload._inUploading) {
          fileUpload.__clearAllButtonClickHandler();
          return true;
        }
        throw "Cannot remove files while they are uploading.";
      },
      /*This method is trying to stop all current files that in upload process. It is not guarantee that file will be stopped */
      stopAllUploads: function() {
        fileUpload._buttons.stopAll.style.visibility = "hidden";
        fileUpload._buttons.stopAll._clicked = true;
        for (var index = 0; index < fileUpload._allFiles.length; index++) {
            if (fileUpload._allFiles[index].status == O$.FileUploadUtil.Status.IN_PROGRESS) {
              var fileInfo = O$(fileUpload.id + "::infoDiv" + fileUpload._allFiles[index]._id);
              fileInfo.childNodes[3].firstChild._clickHandler();
            }
        }
      }
    });
  },
  _SpecFileAPIInit:function(file) {
    O$.extend(file,{
      remove:function() {
        for (var index = 0; index < file._component._allFiles.length; index++) {
          if (file._component._allFiles[index] == file) {  //check if uploading is go on if status uploading don't do it
            if (file.status != O$.FileUploadUtil.Status.IN_PROGRESS) {
              O$(file._component.id + "::infoDiv" + file._id).childNodes[3].firstChild._clickHandler();
              return true;
            }
            throw "File cannot be removed when it's in progress(IN_PROGRESS status).";
          }
        }
        throw "There is no such file";
      },
      /*stopUpload() method is trying to stop uploading of file. It is not guarantee that file will be stopped*/
      stopUpload:function() {
        for (var index = 0; index < file._component._allFiles.length; index++) {
          if (file._component._allFiles[index] == file) {
            if (file._component._allFiles[index].status == O$.FileUploadUtil.Status.IN_PROGRESS) {
              var fileInfo = O$(file._component.id + "::infoDiv" + file._id);
              fileInfo.childNodes[3].firstChild._clickHandler();
              return true;
            }
            throw "File cannot be stopped when it's not in progress(IN_PROGRESS status).";
          }
        }
        return "There is no such file";
      }
    })
  }
};

