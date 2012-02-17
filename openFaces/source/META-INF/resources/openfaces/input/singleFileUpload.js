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

O$.SingleFileUpload = {
  _init: function(componentId, lengthAlreadyUploadedFiles,
                  statusLabelNotUploaded, statusLabelInProgress, statusLabelUploaded, statusLabelErrorSize, statusLabelUnexpectedError,
                  acceptedTypesOfFile,  isDuplicateAllowed,
                  addButtonId, addButtonClass, addButtonOnMouseOverClass, addButtonOnMouseDownClass,addButtonOnFocusClass,
                  addButtonDisabledClass,
                  isDisabled,
                  tabIndex, progressBarId, statusStoppedText, statusStoppingText, ID,
                  onchangeHandler, onuploadstartHandler, onuploadendHandler,
                  onfileuploadstartHandler, onfileuploadinprogressHandler, onfileuploadendHandler,
                  dropTargetCrossoverClass, renderAfterUpload, externalDropTarget, acceptDialogFormats,
                  layoutMode, defStopUrl, stopIcoClassMin) {

    var fileUpload = O$.initComponent(componentId, null, {
      _isAutoUpload:true,
      _fileHTML5:null,
      _currentFile:null,
      _layoutMode:function (){
        switch(layoutMode){
          case "full":
            return O$.SingleFileUpload._LayoutMode.FULL;
          case "compact":
            return O$.SingleFileUpload._LayoutMode.COMPACT;
          case "minimalistic":
            return O$.SingleFileUpload._LayoutMode.MINIMALISTIC;
        }
      }(),
      _processFileAddingHTML5:function (file) {
        if (isFileNameNotApplied(file.name) || fileUpload._buttons.browseInput.disabled ||
                (file.size == 0) || (file._fromDnD && fileUpload._isDirectory(file))) {
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


          O$.removeAllChildNodes(fileUpload._addButtonParent);
          fileUpload._addButtonParent.appendChild(fileUpload._buttons.browse);
          if ((fileUpload._layoutMode == O$.SingleFileUpload._LayoutMode.COMPACT
                  || fileUpload._layoutMode == O$.SingleFileUpload._LayoutMode.MINIMALISTIC)
                  && !fileUpload._els.dropTarget._isExternal){
            fileUpload._addButtonParent.appendChild(fileUpload._els.dropTarget);
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
        O$.requestComponentPortions(fileUpload.id, ["nothing"],
                JSON.stringify({progressRequest:"true", fileId:inputForFile._uniqueId}),
                function (fileUpload, portionName, portionHTML, portionScripts, portionData) {
                  var fileForAPI = fileUpload._getFile(inputForFile._idInputAndDiv);
                  if (portionData['isFileSizeExceed'] == "true") {
                    fileUpload._els.info._status = O$.FileUploadUtil.Status.SIZE_LIMIT_EXCEEDED;
                    fileForAPI.status = O$.FileUploadUtil.Status.SIZE_LIMIT_EXCEEDED;
                    fileUpload._inputsStorage.removeChild(inputForFile.parentNode.parentNode);
                    if (fileUpload._els.status){
                      fileUpload._els.status.innerHTML = fileUpload._statuses.sizeLimit._update(null, portionData['size']);
                    }
                    fileUpload._setStatusforFileWithId(inputForFile._uniqueId, "SIZE_LIMIT_EXCEEDED");
                    fileUpload._events._fireFileUploadEndEvent(fileForAPI);
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
                        }else{
                          if (portionData['size']) {
                            if (fileUpload._els.status){
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
                            if (fileUpload._els.status){
                              fileUpload._els.status.innerHTML = fileUpload._statuses.failed;
                            }
                            fileUpload._setStatusforFileWithId(inputForFile._uniqueId, "FAILED");
                            fileUpload._els.progressBar.setValue(0);
                            fileForAPI.progress = 0;
                            fileUpload._events._fireFileUploadEndEvent(fileForAPI);
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
                        fileUpload._events._fireFileUploadInProgressEvent(fileForAPI);
                        fileUpload._callProgressRequest(inputForFile, endHandler);
                      }
                    } else {// when file already uploaded
                      fileUpload._els.progressBar.setValue(percents, function callOnEnd(){
                        function processEndUpload(){
                          fileForAPI.progress = percents / 100;
                          fileUpload._lengthUploadedFiles++;
                          /*removing input*/
                          fileUpload._inputsStorage.removeChild(inputForFile.parentNode.parentNode); // delete divForFileInput
                          // infoDiv updating
                          fileUpload._els.info._status = O$.FileUploadUtil.Status.SUCCESSFUL;
                          if (fileUpload._els.status){
                            fileUpload._els.status.innerHTML = fileUpload._statuses.uploaded._update(null, portionData['size']);
                          }
                          fileUpload._setStatusforFileWithId(inputForFile._uniqueId, "SUCCESSFUL");
                          if (endHandler) {
                            endHandler();
                          }
                          fileForAPI.status = O$.FileUploadUtil.Status.SUCCESSFUL;
                          fileUpload._events._fireFileUploadEndEvent(fileForAPI);
                          fileUpload._prepareUIWhenAllRequestsFinished(true);
                        }
                        if (fileUpload._layoutMode == O$.SingleFileUpload._LayoutMode.MINIMALISTIC){
                          setTimeout(processEndUpload, 200);
                        }else{
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
        O$.requestComponentPortions(fileUpload.id, ["nothing"],
                JSON.stringify({progressRequest:"true", fileId:file._uniqueId}),
                function (fileUpload, portionName, portionHTML, portionScripts, portionData) {
                  var fileForAPI = fileUpload._getFile(file._infoId);
                  if (portionData['isFileSizeExceed'] == "true") {
                    fileUpload._els.info._status = O$.FileUploadUtil.Status.SIZE_LIMIT_EXCEEDED;
                    fileForAPI.status = O$.FileUploadUtil.Status.SIZE_LIMIT_EXCEEDED;
                    removeHTML5File();
                    if (fileUpload._els.status){
                      fileUpload._els.status.innerHTML = fileUpload._statuses.sizeLimit._update(null, portionData['size']);
                    }
                    fileUpload._setStatusforFileWithId(file._uniqueId, "SIZE_LIMIT_EXCEEDED");
                    fileUpload._events._fireFileUploadEndEvent(fileForAPI);
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
                        }else{
                          if (portionData['size']) {
                            if (fileUpload._els.status){
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
                            if (fileUpload._els.status){
                              fileUpload._els.status.innerHTML = fileUpload._statuses.failed;
                            }
                            fileUpload._setStatusforFileWithId(file._uniqueId, "FAILED");
                            fileUpload._els.progressBar.setValue(0);
                            fileForAPI.progress = 0;
                            fileUpload._events._fireFileUploadEndEvent(fileForAPI);
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
                        fileUpload._events._fireFileUploadInProgressEvent(fileForAPI);
                        setTimeout(function () {
                          fileUpload._progressHTMl5Request(file, request, endHandler);
                        }, 500);
                      }
                    } else {// when file already uploaded
                      fileUpload._els.progressBar.setValue(percents, function callOnEnd(){
                        function processEndUpload(){
                          fileForAPI.progress = percents / 100;
                          fileUpload._lengthUploadedFiles++;
                          /*removing file*/
                          removeHTML5File();
                          // infoDiv updating
                          fileUpload._els.info._status = O$.FileUploadUtil.Status.SUCCESSFUL;
                          if (fileUpload._els.status){
                            fileUpload._els.status.innerHTML = fileUpload._statuses.uploaded._update(null, portionData['size']);
                          }
                          fileUpload._setStatusforFileWithId(file._uniqueId, "SUCCESSFUL");
                          fileForAPI.status = O$.FileUploadUtil.Status.SUCCESSFUL;
                          fileUpload._events._fireFileUploadEndEvent(fileForAPI);
                          if (endHandler) {
                            endHandler();
                          }
                          fileUpload._prepareUIWhenAllRequestsFinished(true);
                        }
                        if (fileUpload._layoutMode == O$.SingleFileUpload._LayoutMode.MINIMALISTIC){
                          setTimeout(processEndUpload, 200);
                        }else{
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
        O$.requestComponentPortions(fileUpload.id, ["nothing"],
                JSON.stringify({stoppedRequest:true, uniqueIdOfFile:inputForFile._uniqueId}),
                function (fileUpload, portionName, portionHTML, portionScripts, portionData) {
                  if (portionData['isStopped'] == "true") {
                    inputForFile._isInterrupted = true;
                    fileUpload._els.info._status = O$.FileUploadUtil.Status.FAILED;
                    fileForAPI.status = O$.FileUploadUtil.Status.FAILED;
                    fileUpload._inputsStorage.removeChild(inputForFile.parentNode.parentNode);
                    if (fileUpload._els.status){
                      fileUpload._els.status.innerHTML = fileUpload._statuses.failed;
                    }
                    fileUpload._setStatusforFileWithId(inputForFile._uniqueId, "FAILED");
                    fileUpload._els.progressBar.setValue(0);
                    fileForAPI.progress = 0;
                    fileUpload._events._fireFileUploadEndEvent(fileForAPI);
                    if (endHandler) {
                      endHandler();
                    }
                    fileUpload._prepareUIWhenAllRequestsFinished(true);
                  }
                }, null, true);
      },
      _sendIsErrorRequestHTML5:function (file, infoDiv, fileForAPI, endHandler){
        O$.requestComponentPortions(fileUpload.id, ["nothing"],
                JSON.stringify({stoppedRequest:true, uniqueIdOfFile:file._uniqueId}),
                function (fileUpload, portionName, portionHTML, portionScripts, portionData) {
                  if (portionData['isStopped'] == "true") {
                    file._isInterrupted = true;
                    fileUpload._els.info._status = O$.FileUploadUtil.Status.FAILED;
                    fileForAPI.status = O$.FileUploadUtil.Status.FAILED;
                    removeHTML5File();
                    if (fileUpload._els.status){
                      fileUpload._els.status.innerHTML = fileUpload._statuses.failed;
                    }
                    fileUpload._setStatusforFileWithId(file._uniqueId, "FAILED");
                    fileUpload._els.progressBar.setValue(0);
                    fileForAPI.progress = 0;
                    fileUpload._events._fireFileUploadEndEvent(fileForAPI);
                    if (endHandler){
                      endHandler();
                    }
                    fileUpload._prepareUIWhenAllRequestsFinished(true);
                  }
                }, null, true);
      },
      _sendIsStoppedRequest:function (inputForFile, infoDiv, fileForAPI,endHandler){
        O$.requestComponentPortions(fileUpload.id, ["nothing"],
                JSON.stringify({stoppedRequest:true, uniqueIdOfFile:inputForFile._uniqueId}),
                function (fileUpload) {
                  inputForFile._isInterrupted = true;
                  fileUpload._els.info._status = O$.FileUploadUtil.Status.STOPPED;
                  fileUpload._inputsStorage.removeChild(inputForFile.parentNode.parentNode);
                  if (fileUpload._els.status){
                    fileUpload._els.status.innerHTML = fileUpload._statuses.stopped;
                  }
                  fileUpload._setStatusforFileWithId(inputForFile._uniqueId, "STOPPED");
                  if (endHandler){
                    endHandler();
                  }
                  fileForAPI.status = O$.FileUploadUtil.Status.STOPPED;
                  fileUpload._els.progressBar.setValue(0);
                  fileForAPI.progress = 0;
                  fileUpload._events._fireFileUploadEndEvent(fileForAPI);
                  fileUpload._prepareUIWhenAllRequestsFinished(true);
                }, null, true);
      },
      _sendIsStoppedRequestHTML5:function (file, infoDiv, fileForAPI, endHandler){
        O$.requestComponentPortions(fileUpload.id, ["nothing"],
                JSON.stringify({stoppedRequest:true, uniqueIdOfFile:file._uniqueId}),
                function (fileUpload, portionName, portionHTML, portionScripts, portionData) {
                  if (portionData['isStopped'] == "true") {
                    file._isInterrupted = true;
                    fileUpload._els.info._status = O$.FileUploadUtil.Status.STOPPED;
                    removeHTML5File();
                    if (fileUpload._els.status){
                      fileUpload._els.status.innerHTML = fileUpload._statuses.stopped;
                    }
                    fileUpload._setStatusforFileWithId(file._uniqueId, "STOPPED");
                    fileForAPI.status = O$.FileUploadUtil.Status.STOPPED;
                    fileUpload._els.progressBar.setValue(0);
                    fileForAPI.progress = 0;
                    fileUpload._events._fireFileUploadEndEvent(fileForAPI);
                    if (endHandler){
                      endHandler();
                    }
                    fileUpload._prepareUIWhenAllRequestsFinished(true);
                  }
                }, null, true);
      }
    });
    O$.FileUploadUtil._initGeneral(fileUpload,
            lengthAlreadyUploadedFiles, acceptedTypesOfFile, isDisabled, ID,
            addButtonClass, addButtonOnMouseOverClass, addButtonOnMouseDownClass,addButtonOnFocusClass,
            statusLabelInProgress,statusLabelUploaded,statusLabelErrorSize,
            statusLabelNotUploaded,statusStoppedText,statusLabelUnexpectedError,
            renderAfterUpload,tabIndex,dropTargetCrossoverClass, externalDropTarget, acceptDialogFormats);

    fileUpload._setAllEvents(onchangeHandler,onuploadstartHandler,onuploadendHandler,
            onfileuploadstartHandler,onfileuploadinprogressHandler,onfileuploadendHandler);

    //getting clear,stop,cancel, progressBar facet for each info window
    fileUpload._elementsCont = O$(componentId + "::elements");
    if (fileUpload._layoutMode != O$.SingleFileUpload._LayoutMode.MINIMALISTIC) {
      fileUpload._buttons.stop = fileUpload._getFacet("::stopFacet");
    }

    fileUpload._els = [];
    if (fileUpload._layoutMode == O$.SingleFileUpload._LayoutMode.FULL){
      fileUpload._els.infoTable = O$(componentId + "::fileInfo");
      fileUpload._els.info =  fileUpload._els.infoTable.firstChild.firstChild;
      fileUpload._els.fileName = fileUpload._els.info.childNodes[0];
      fileUpload._els.status = fileUpload._els.info.childNodes[1];
    }else{
      fileUpload._els.info = {};
    }
    fileUpload._els.progressBar = O$(progressBarId);
    if (fileUpload._layoutMode == O$.SingleFileUpload._LayoutMode.MINIMALISTIC){
      fileUpload._elementsCont.style.visibility = "hidden";
      fileUpload._elementsCont.style.display = "";
      var stopBtn = O$(fileUpload._elementsCont.id + "::stopFacet").lastChild;
      var _sizeStop = O$.getElementSize(stopBtn);
      var marginStop = fileUpload._getNumProperty(stopBtn, "margin-left");
      var marginTopStop = fileUpload._getNumProperty(stopBtn, "margin-top");
      fileUpload._buttons.stop = fileUpload._getFacet("::stopFacet");
      if (defStopUrl){
        fileUpload._buttons.stop.style.backgroundImage = "url('" + defStopUrl + "')";
        if (O$.isExplorer7() || O$.isExplorer6() || (O$.isExplorer() && O$.isQuirksMode())){
          fileUpload._buttons.stop.style.marginTop = marginTopStop - 1;
        }
      }else{
        O$.setStyleMappings(fileUpload._buttons.stop, {def:stopIcoClassMin});
      }
      // because when stop has display:none - different browsers works different with width and height
      fileUpload._buttons.stop._size = _sizeStop;
      fileUpload._buttons.stop._margin = marginStop;

      fileUpload._els.progressBar._size = O$.getElementSize(fileUpload._els.progressBar);
      fileUpload._elementsCont.style.display = "none";
      fileUpload._elementsCont.style.visibility = "visible";
      fileUpload._size = O$.getElementSize(fileUpload);
    }
    var idOfInfoAndInputDiv = 1;

    setTabIndexForAllButtons(tabIndex);

    fileUpload._inputsStorage = fileUpload._createStructureForInputs();

    if (!O$.isExplorer())
      fileUpload.removeChild(fileUpload._elementsCont);
    fileUpload._setUpBrowseButton(addButtonId);
    fileUpload._addButtonParent = fileUpload._buttons.browse.parentNode;

    O$.extend(fileUpload, {
              __uploadButtonClickHandler:function () {
                fileUpload._buttons.browseInput.disabled = true;
                fileUpload._inUploading = true;
                O$.setStyleMappings(fileUpload._buttons.browseTitleInput, {disabled : addButtonDisabledClass});
                /* IE 7 cannot define what width in browse button because it's not rendered yet*/
                if (fileUpload._layoutMode == O$.SingleFileUpload._LayoutMode.FULL
                        || fileUpload._layoutMode == O$.SingleFileUpload._LayoutMode.COMPACT) {
                  if (!fileUpload._buttons.stop._defined) {
                    new function setStopBtnWidthAsInBrowse() {
                      var widthOfBrowseContainer = O$.getElementSize(fileUpload._buttons.browse).width;
                      var widthOfTitleInput = O$.getElementSize(fileUpload._buttons.browseTitleInput).width;
                      fileUpload._buttons.stop.style.width = widthOfTitleInput + "px";
                      var margin = (widthOfBrowseContainer - widthOfTitleInput) / 2;
                      fileUpload._buttons.stop.style.marginLeft = margin + "px";
                      fileUpload._buttons.stop.style.marginRight = margin + "px";
                      fileUpload._buttons.stop._defined = true;
                    }();
                    if (fileUpload._layoutMode == O$.SingleFileUpload._LayoutMode.COMPACT) {
                      var titleInputHeight = O$.getElementSize(fileUpload._buttons.browseTitleInput).height;
                      var container = O$.getElementSize(fileUpload._buttons.browse).height;
                      fileUpload._buttons.stop.style.height = titleInputHeight + "px";
                      var margin = (container - titleInputHeight) / 2;
                      fileUpload._buttons.stop.style.marginTop = margin + "px";
                      fileUpload._buttons.stop.style.marginBottom = margin + "px";
                    }
                  }
                } else if (fileUpload._layoutMode == O$.SingleFileUpload._LayoutMode.MINIMALISTIC) {
                  if (!fileUpload._buttons.stop._defined) {
                    new function setProgressWidthAndStopBtnAsInBrowse() {
                      function getNumberProperty(el, textProp, real){
                        var res= fileUpload._getNumProperty(el, textProp);
                        if (res == 0 || isNaN(res)){
                          return real.replace("px", "") * 1;
                        }else{
                          return res;
                        }
                      }
                      var sizeOfBrowse = O$.getElementSize(fileUpload._buttons.browse);
                      var heightOfBrowseContainer = sizeOfBrowse.height;
                      var widthOfTitleInput = O$.getElementSize(fileUpload._buttons.browseTitleInput).width;
                      var widthStopBtn = fileUpload._buttons.stop._size.width;
                      widthStopBtn= widthStopBtn +
                              + fileUpload._buttons.stop._margin
                              + getNumberProperty(fileUpload._els.progressBar, "margin-right",fileUpload._buttons.stop.style.marginRight);
                      fileUpload._els.progressBar.style.width = widthOfTitleInput - widthStopBtn + "px";
                      var margin = (heightOfBrowseContainer - fileUpload._els.progressBar._size.height) / 2;
                      fileUpload._els.progressBar.style.marginTop = (O$.isExplorer9() ? margin + 1 : margin ) + "px";
                      if (margin % 1 != 0) {
                        if (!O$.isMozillaFF()){
                          margin++;
                        }
                      }
                      fileUpload._els.progressBar.style.marginBottom = margin + "px";
                      fileUpload._buttons.stop._defined = true;
                    }();
                  }
                }

                fileUpload._listOfids = [];
                /*prepare a list of files that would be uploaded*/
                if (fileUpload._inputsStorage.childNodes[0]){
                  var form = fileUpload._inputsStorage.childNodes[0].firstChild;
                  fileUpload._addFileToInfoFileList(form);
                }else if (fileUpload._fileHTML5 != null){/*specific behavior for uploading files with drag and drop*/
                  var html5File = fileUpload._fileHTML5;
                  fileUpload._addFileToInfoFileListHTML5(html5File);
                }
                fileUpload._events._fireUploadStartEvent(fileUpload._currentFile);
                var uri = fileUpload._getDocumentURI();

                function setupUIForUpload(inputForFile, infoDiv, fileForAPI) {
                  function setStopButtonBehavior(inputForFile, infoDiv){
                    var stopFileDiv = fileUpload._buttons.stop.cloneNode(true);
                    stopFileDiv.setAttribute("id", fileUpload._buttons.stop.id + inputForFile._idInputAndDiv);
                    fileUpload._addButtonParent.removeChild(fileUpload._buttons.browse);
                    if (fileUpload._layoutMode == O$.SingleFileUpload._LayoutMode.MINIMALISTIC) {
                      fileUpload._els.progressBar.style.display = "block";
                      fileUpload._els.progressBar.setValue(0);
                      if (O$.isExplorer() &&( O$.isQuirksMode() || (O$.isExplorer7() || O$.isExplorer6()))){
                        fileUpload.style.height = fileUpload._size.height  + "px";
                      }
                      fileUpload._addButtonParent.appendChild(fileUpload._els.progressBar);
                    }
                    fileUpload._addButtonParent.appendChild(stopFileDiv);
                    O$.addEventHandler(stopFileDiv, "focus", fileUpload._focusHandler);
                    O$.addEventHandler(stopFileDiv, "blur", fileUpload._blurHandler);
                    stopFileDiv._clickHandler = function () {
                      stopFileDiv.style.visibility = "hidden";
                      if (fileUpload._els.status){
                        fileUpload._els.status.innerHTML = statusStoppingText;
                      }
                      var iframe = inputForFile.nextSibling;
                      iframe.src = "";
                      inputForFile._wantToInterrupt = true;
                    };
                    O$.addEventHandler(stopFileDiv, "click", stopFileDiv._clickHandler);
                    fileUpload._chromeAndSafariFocusFix(stopFileDiv);
                  }
                  fileUpload._els.info._status = O$.FileUploadUtil.Status.IN_PROGRESS;
                  fileForAPI.status = O$.FileUploadUtil.Status.IN_PROGRESS;
                  if (fileUpload._els.status){
                    fileUpload._els.status.innerHTML = fileUpload._statuses.inProgress._update(0, "unknown");
                  }
                  setStopButtonBehavior(inputForFile, infoDiv);
                }

                function setupUIForUploadHTML5(file, infoDiv, request, fileForAPI) {
                  function setStopButtonBehaviorHTML5(file, infoDiv, request) {
                    var stopFileDiv = fileUpload._buttons.stop.cloneNode(true);
                    stopFileDiv.setAttribute("id", fileUpload._buttons.stop.id + file._infoId);
                    fileUpload._addButtonParent.removeChild(fileUpload._buttons.browse);
                    if (fileUpload._layoutMode == O$.SingleFileUpload._LayoutMode.MINIMALISTIC) {
                      fileUpload._els.progressBar.style.display = "block";
                      fileUpload._els.progressBar.setValue(0);
                      fileUpload._addButtonParent.appendChild(fileUpload._els.progressBar);
                    }
                    fileUpload._addButtonParent.appendChild(stopFileDiv);
                    O$.addEventHandler(stopFileDiv, "focus", fileUpload._focusHandler);
                    O$.addEventHandler(stopFileDiv, "blur", fileUpload._blurHandler);
                    stopFileDiv._clickHandler = function () {
                      stopFileDiv.style.visibility = "hidden";
                      if (fileUpload._els.status){
                        fileUpload._els.status.innerHTML = statusStoppingText;
                      }
                      request.abort();
                      file._wantToInterrupt = true;
                    };
                    O$.addEventHandler(stopFileDiv, "click", stopFileDiv._clickHandler);
                    fileUpload._chromeAndSafariFocusFix(stopFileDiv);
                  }
                  fileUpload._els.info._status = O$.FileUploadUtil.Status.IN_PROGRESS;
                  fileForAPI.status = O$.FileUploadUtil.Status.IN_PROGRESS;
                  if (fileUpload._els.status){
                    fileUpload._els.status.innerHTML = fileUpload._statuses.inProgress._update(0, file.size);
                  }
                  setStopButtonBehaviorHTML5(file, infoDiv, request);
                }

                  return new function(){
                    /*in single only one file could be uploaded at the moment*/
                    if (fileUpload._inputsStorage.childNodes.length != 0){
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
                      fileUpload._events._fireFileUploadStartEvent(fileOfAPI);
                    }

                    if (fileUpload._fileHTML5 != null) {
                      var html5File = fileUpload._fileHTML5;
                      var fileAPI = fileUpload._getFile(html5File._infoId);

                      var request = fileUpload._sendFileRequest(html5File,uri);
                      fileUpload._callFileProgressForHTML5Request(html5File, request);
                      fileAPI.status = O$.FileUploadUtil.Status.IN_PROGRESS;
                      setupUIForUploadHTML5(html5File, fileUpload._els.info, request, fileAPI);
                      fileUpload._events._fireFileUploadStartEvent(fileAPI);
                    }
                  }();

              }
            }
    );
    initHeaderButtons(true);
    setFocusBlurBehaviour();

    fileUpload._els.dropTarget = setBehaviorForDragAndDropArea();
    O$.SingleFileUpload._initFileUploadAPI(fileUpload);
    function setBehaviorForDragAndDropArea(){
      var area = fileUpload._getDropTargetArea(fileUpload.id + "::dragArea");
      area._firstShow = true;

      O$.FileUploadUtil._initDragArea(area);
      O$.extend(area, {
        hide:function(){
          if (!area._isExternal && fileUpload._els.infoTable){
            fileUpload._els.infoTable.style.display = "";
          }else if(fileUpload._layoutMode ==O$.SingleFileUpload._LayoutMode.COMPACT) {
            fileUpload._buttons.browse.style.display = "";
            if (fileUpload._currentFile!=null){
              fileUpload._els.progressBar.style.display = "";
            }
          }else if(fileUpload._layoutMode ==O$.SingleFileUpload._LayoutMode.MINIMALISTIC) {
            fileUpload._buttons.browse.style.display = "";
          }
          area.style.display = "none";
          area._firstShow = true;
        },
        show:function(){
          if (fileUpload._els.infoTable) {
            if (!area._isExternal && area._firstShow) {
              function getCorrectWidthForArea(area) {
                return O$.getElementSize(fileUpload._els.infoTable).width - fileUpload._getNumProperty(area, "border-top-width") * 2;
              }

              function getCorrectHeightForArea(area) {
                function getGreaterHeight() {
                  if (O$.getElementSize(fileUpload._els.infoTable).height > O$.getElementSize(fileUpload._buttons.browse).height) {
                    return O$.getElementSize(fileUpload._els.infoTable).height;
                  } else {
                    return O$.getElementSize(fileUpload._buttons.browse).height;
                  }
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
              fileUpload._els.infoTable.style.display = "none";
            }
          }else if (fileUpload._layoutMode == O$.SingleFileUpload._LayoutMode.COMPACT){
            if (!area._isExternal && area._firstShow) {

              area.style.height = function getHeightForAreaCompact(area){
                return function getHeight(){
                  return O$.getElementSize(fileUpload._buttons.browse).height
                          + O$.getElementSize(fileUpload._els.progressBar.parentNode).height ;
                }()- function getDiffForArea(area){
                  var diff = fileUpload._getNumProperty(area, "border-top-width");
                  diff *= 2;
                  diff += fileUpload._getNumProperty(area, "padding-top");
                  return diff;
                }(area);
              }(area) + "px";
              area.style.width = function getWidthForAreaCompact(area){
                return O$.getElementSize(fileUpload._buttons.browse).width - fileUpload._getNumProperty(area, "border-top-width") * 2;
              }(area) + "px";
              area._firstShow = false;
            }
            if (!area._isExternal) {
              fileUpload._buttons.browse.style.display = "none";
              fileUpload._els.progressBar.style.display  = "none";
            }
          }else if (fileUpload._layoutMode == O$.SingleFileUpload._LayoutMode.MINIMALISTIC){
            if (!area._isExternal && area._firstShow) {

              area.style.height = function getHeightForAreaMin(area){
                return function getHeight(){
                  return O$.getElementSize(fileUpload._buttons.browse).height;
                }()- function getDiffForArea(area){
                  var diff = fileUpload._getNumProperty(area, "border-top-width");
                  diff *= 2;
                  diff += fileUpload._getNumProperty(area, "padding-top");
                  return diff;
                }(area);
              }(area) + "px";
              area.style.width = function getWidthForAreaMin(area){
                return O$.getElementSize(fileUpload._buttons.browse).width - fileUpload._getNumProperty(area, "border-top-width") * 2;
              }(area) + "px";
              area._firstShow = false;
            }
            if (!area._isExternal) {
              fileUpload._buttons.browse.style.display = "none";
            }
          }
          area.style.display = "block";
        }
      });
      if (!O$._dropAreas){
        O$._dropAreas = [];
      }
      O$._dropAreas.push(area);
      var body = document.getElementsByTagName('body')[0];
      area._isVisible = false;
      fileUpload._setDragEventsForBody(body, area);
      fileUpload._setDragEventsForFileUpload(area);
      return area;
    }

    O$.addEventHandler(document, "sessionexpired", function (){
      var f = fileUpload._currentFile;
      if (f.getStatus() == O$.FileUploadUtil.Status.IN_PROGRESS) {
        fileUpload._els.info._status = O$.FileUploadUtil.Status.FAILED;
        f.status = O$.FileUploadUtil.Status.FAILED;
        if (fileUpload._els.status){
          fileUpload._els.status.innerHTML = fileUpload._statuses.failed;
        }
      }
      f.remove();
      fileUpload._numberOfFilesToUpload = 0;
      fileUpload._buttons.browseInput.disabled = false;
      fileUpload._inUploading = false;

      O$.setStyleMappings(fileUpload._buttons.browseTitleInput, {disabled:null});
      if (fileUpload._layoutMode == O$.SingleFileUpload._LayoutMode.MINIMALISTIC){
        fileUpload.style.display = "none";
      }else{
        initHeaderButtons();
      }
    });

    function isFileNameNotApplied(filename){
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
      if (firstTime){
        fileUpload._els.progressBar.style.display = "none";
      }
      if (fileUpload._isDisabled) {
        fileUpload._buttons.browseInput.disabled = true;
        O$.setStyleMappings(fileUpload._buttons.browseTitleInput, {disabled : addButtonDisabledClass});
      } else {
        if (fileUpload._maxQuantity != 0 && fileUpload._lengthUploadedFiles == fileUpload._maxQuantity){
          fileUpload._buttons.browseInput.disabled = true;
          O$.setStyleMappings(fileUpload._buttons.browseTitleInput, {disabled : addButtonDisabledClass});
        }else{
          fileUpload._buttons.browseInput.disabled = false;
        }
      }
    }

    function setFocusBlurBehaviour(){
      O$.addEventHandler(fileUpload._buttons.browseInput, "focus", fileUpload._focusHandler);
      O$.addEventHandler(fileUpload._buttons.browseInput, "blur", fileUpload._blurHandler);
      if (fileUpload._buttons.browseTitleInput.focus){
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

    function removeHTML5File(){
      fileUpload._fileHTML5 = null;
    }

    function setInfoWindow(filename){
      fileUpload._currentFile = null;
      if (fileUpload._layoutMode == O$.SingleFileUpload._LayoutMode.FULL) {
        fileUpload._els.fileName.innerHTML = fileUpload._getFileName(filename); //filename
        fileUpload._els.status.innerHTML = fileUpload._statuses.newOne;//status
        fileUpload._els.fileName._widthShouldBe = O$.getElementSize(fileUpload._els.fileName).width + "px";
      }
      if (fileUpload._layoutMode != O$.SingleFileUpload._LayoutMode.MINIMALISTIC) {
        fileUpload._els.progressBar.style.display = "block";
        fileUpload._els.progressBar.setValue(0);
      }
      if (fileUpload._els.fileName) {
        fileUpload._els.fileName.style.width = fileUpload._els.fileName._widthShouldBe;
      }
    }
  },
  _initFileUploadAPI:function (fileUpload) {
    //Helper methods for API
    O$.extend(fileUpload, {
      _removeFileFromAllFiles:function(){
        fileUpload._currentFile = null;
      },
      _getFile: function(){
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
  _SpecFileAPIInit:function(file){
    O$.extend(file,{
      remove:function(){
        if (file != null) {
          if (file.status != O$.FileUploadUtil.Status.IN_PROGRESS) {
            var fileUpload = file._component;
            if (fileUpload._layoutMode == O$.SingleFileUpload._LayoutMode.FULL){
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
            file._component._addButtonParent.lastChild._clickHandler();
            return true;
          }
          throw "File cannot be stopped when it's not in progress(IN_PROGRESS status).";
        }
        return "There is no such file";
      }
    })
  },
  _LayoutMode:{
    FULL:{value:"full"},
    COMPACT:{value:"compact"},
    MINIMALISTIC:{value:"minimalistic"}
  }
};

