/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2011, TeamDev Ltd.
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
                  onchangeHandler, onuploadstartHandler, onuploadendHandler,
                  onfileuploadstartHandler, onfileuploadinprogressHandler, onfileuploadendHandler,
                  dropTargetCrossoverClass) {

    var fileUpload = O$.initComponent(componentId, null, {
      _minQuantity : minQuantity,
      _maxQuantity : maxQuantity,
      _numberOfFilesToUpload : 0,
      _lengthUploadedFiles : lengthAlreadyUploadedFiles,
      _typesOfFile: (acceptedTypesOfFile != null) ? acceptedTypesOfFile.split(" ") : null,
      _isDisabled : isDisabled,
      _multiUpload : multiUpload,
      _isAutoUpload : isAutoUpload,
      _ID : ID,
      _filesHTML5:[],
      _events:{},
      _allFiles:[],
      _numberOfFailedRequest:15,   /*this number defines how many requests should have gone to presume that request is timedout if progress doesn't changed*/
      _numberOfErrorRequest: 3 /*this number defines how many requests should have gone to check that request throwed an Exception if progress doesn't changed*/

    });
    setAllEvents();
    if (!fileUpload._multiUpload){
      fileUpload._isAutoUpload = true;
      //fileUpload._maxQuantity = 1;
    }
    //getting clear,stop,cancel, progressBar facet for each info window
    var elements = O$(componentId + "::elements");
    var divForClearFacet = O$(componentId + "::elements::clearFacet");
    var clearFacet = divForClearFacet.lastChild;
    elements.removeChild(divForClearFacet);
    clearFacet.disabled = false;

    var divForCancelFacet = O$(componentId + "::elements::removeFacet");
    var cancelFacet = divForCancelFacet.lastChild;
    elements.removeChild(divForCancelFacet);
    cancelFacet.disabled = false;

    var divForStopFacet = O$(componentId + "::elements::stopFacet");
    var stopFacet = divForStopFacet.lastChild;
    elements.removeChild(divForStopFacet);
    stopFacet.disabled = false;

    var progressBar= O$(progressBarId);
    if (!O$.isExplorer())
      elements.removeChild(progressBar);

    var idOfInfoAndInputDiv = 1;

    var uploadButton = O$(componentId + "::header::uploadFacet").lastChild;
    var clearAllButtonFacet = O$(componentId + "::footer::clearAllFacet");
    var clearAllButton = clearAllButtonFacet.lastChild;
    setTabIndexForAllButtons(tabIndex);

    var allInfos = O$(componentId + "::infoDiv");
    if(O$.isExplorer()){
      if (O$.isQuirksMode() ||
              ((O$.isExplorer6() ||O$.isExplorer7() || O$.isExplorer8() || O$.isExplorer9()) &&
                      (document.documentMode == 7 || document.documentMode == 6)))
        allInfos = allInfos.firstChild;
    }


    var inputsStorage = createStructureForInputs();
    var inputsStorageId = inputsStorage.id;


    var couldCallChangeEvent = true;

    if (!O$.isExplorer())
      fileUpload.removeChild(elements);

    var addButton = O$(addButtonId);

    var divForInputInAddBtn = O$(addButtonId+"::forInput");
    var inputInAddBtn = createInputInAddBtn(idOfInfoAndInputDiv);
    var addButtonTitle = O$(addButtonId + "::title");
    var addButtonTitleInput = addButtonTitle.firstChild;
    setStylesForAddButton(addButton);
    if (O$.isExplorer() && O$.isQuirksMode()){
      divForInputInAddBtn.style.height = O$.getElementSize(addButtonTitle).height;
    }
    addButtonTitleInput.disabled = false;
    divForInputInAddBtn.appendChild(inputInAddBtn);//create first input

    //variables for focus/blur
    var onFocusWillBe = null;
    var wasFocused = null;
    var shouldInvokeFocusEvHandler = true;
    var shouldInvokeFocusNonModifiable = true;
    var ignoreBlurForChrome = true; // this variable only needed for specific behaviour of focus at Chrome browser
    var activeElementNow;
    var ignoreBlurForIE = false;
    O$.extend(fileUpload, {
              __uploadButtonClickHandler:function () {
                function callProgressRequest(fileInput) {
                  setTimeout(function () {
                    progressRequest(fileInput);
                  }, 500);
                }

                function callFileProgressForHTML5Request(file, request) {
                  setTimeout(function () {
                    progressHTMl5Request(file, request);
                  }, 500);
                }

                function getDocumentURI() {
                  var docURL = document.URL;
                  if (docURL.indexOf("&") == -1) {
                    return docURL + "?uniqueID=" + fileUpload._ID;
                  } else {
                    return docURL + "&uniqueID=" + fileUpload._ID;
                  }

                }

                inputInAddBtn.disabled = true;
                fileUpload._inUploading = true;
                addButtonTitleInput.className = addButtonDisabledClass;
                if (fileUpload._multiUpload) {
                  uploadButton.style.visibility = "hidden";
                  clearAllButton.style.visibility = "hidden";    // to prevent changing of file list when uploading in progress
                } else {
                  clearAllButton.style.display = "none";
                }
                fileUpload._listOfids = [];
                /*prepare a list of files that would be uploaded*/
                for (var inputsIndex = 0; inputsIndex < inputsStorage.childNodes.length; inputsIndex++) {
                  var file = [];
                  var form = inputsStorage.childNodes[inputsIndex].firstChild;
                  file.push(form.childNodes[0]._uniqueId);//id
                  file.push(form.childNodes[0].name);//name of file input
                  file.push(encodeURI(getFileName(form.childNodes[0].value)));//filename
                  file.push("IN_PROGRESS");//status
                  file.push(form.childNodes[0]._idInputAndDiv);//client id of div
                  fileUpload._listOfids.push(file);
                }
                /*specific behavior for uploading files with drag and drop*/
                for (var html5FilesIndex = 0; html5FilesIndex < fileUpload._filesHTML5.length; html5FilesIndex++) {
                  var html5File = fileUpload._filesHTML5[html5FilesIndex];
                  var file = [];
                  file.push(html5File._uniqueId);//id
                  file.push(html5File._fakeInput);//name of file input
                  file.push(encodeURI(getFileName(html5File.name)));//filename
                  file.push("IN_PROGRESS");//status
                  file.push(html5File._infoId);//client id of div
                  fileUpload._listOfids.push(file);
                }
                fileUpload._events._fireUploadStartEvent(fileUpload._allFiles);
                var uri = getDocumentURI();
                for (var inputsIndex = 0; inputsIndex < inputsStorage.childNodes.length; inputsIndex++) {
                  var inputDiv = inputsStorage.childNodes[inputsIndex];
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
                  callProgressRequest(fileInput);
                  fileOfAPI.status = O$.FileUpload.Status.IN_PROGRESS;
                  setupUIForUpload(fileInput, fileInfo, fileOfAPI);
                  fileUpload._events._fireFileUploadStartEvent(fileOfAPI);
                }
                function sendFileRequest(file) {
                  var xhr = new XMLHttpRequest();
                  xhr.open('POST', uri + "&idOfFile=" + file._infoId, true);
                  var data = new FormData();
                  data.append(file._fakeInput, file);
                  xhr.send(data);
                  return xhr;
                }

                for (var html5FilesIndex = 0; html5FilesIndex < fileUpload._filesHTML5.length; html5FilesIndex++) {
                  var html5File = fileUpload._filesHTML5[html5FilesIndex];
                  var fileInfo = O$(allInfos.id + html5File._infoId);
                  var fileOfAPI = fileUpload._getFile(html5File._infoId);
                  fileInfo.childNodes[3].firstChild.style.visibility = "hidden";

                  var request = sendFileRequest(html5File);
                  callFileProgressForHTML5Request(html5File, request);
                  fileOfAPI.status = O$.FileUpload.Status.IN_PROGRESS;
                  setupUIForUploadHTML5(html5File, fileInfo, request,fileOfAPI);
                  fileUpload._events._fireFileUploadStartEvent(fileOfAPI);
                }
                function progressRequest(inputForFile) {
                  O$.requestComponentPortions(fileUpload.id, ["nothing"],
                          JSON.stringify({progressRequest:"true", fileId:inputForFile._uniqueId}),
                          function (fileUpload, portionName, portionHTML, portionScripts, portionData) {
                            var infoDiv = O$(allInfos.id + inputForFile._idInputAndDiv);
                            var fileForAPI = fileUpload._getFile(inputForFile._idInputAndDiv);
                            if (portionData['isFileSizeExceed'] == "true") {
                              infoDiv._status = O$.FileUpload.Status.SIZE_LIMIT_EXCEEDED;
                              fileForAPI.status = O$.FileUpload.Status.SIZE_LIMIT_EXCEEDED;
                              inputsStorage.removeChild(inputForFile.parentNode.parentNode);
                              infoDiv.childNodes[2].innerHTML = statusLabelErrorSize;
                              setStatusforFileWithId(inputForFile._uniqueId, "SIZE_LIMIT_EXCEEDED");
                              fileUpload._events._fireFileUploadEndEvent(fileForAPI);
                              setClearBtnAndEventHandler(infoDiv, inputForFile._idInputAndDiv);
                              prepareUIWhenAllRequestsFinished(true);
                            } else {
                              var percents = portionData['progressInPercent'];
                              if (percents != 100) {
                                if (!inputForFile._isInterrupted) {
                                  if (inputForFile._wantToInterrupt) {
                                    sendIsStoppedRequest(inputForFile, infoDiv, fileForAPI);
                                  }
                                  if (infoDiv.childNodes[1].firstChild.getValue() == percents) {
                                    if (!inputForFile._percentsEqualsTimes) {
                                      inputForFile._percentsEqualsTimes = 0;
                                    }
                                    inputForFile._percentsEqualsTimes++;
                                    if (inputForFile._percentsEqualsTimes > fileUpload._numberOfErrorRequest) {
                                      sendIsErrorRequest(inputForFile, infoDiv, fileForAPI);
                                    }
                                    if (inputForFile._percentsEqualsTimes > fileUpload._numberOfFailedRequest) {
                                      inputForFile._isInterrupted = true;
                                      sendInformThatRequestFailed(inputForFile._uniqueId);
                                      infoDiv._status = O$.FileUpload.Status.FAILED;
                                      fileForAPI.status = O$.FileUpload.Status.FAILED;
                                      inputsStorage.removeChild(inputForFile.parentNode.parentNode);
                                      infoDiv.childNodes[2].innerHTML = statusLabelUnexpectedError;
                                      setStatusforFileWithId(id, "FAILED");
                                      fileUpload._events._fireFileUploadEndEvent(fileForAPI);
                                      setClearBtnAndEventHandler(infoDiv, inputForFile._idInputAndDiv);
                                      prepareUIWhenAllRequestsFinished(true);
                                    }
                                  }else{
                                    inputForFile._percentsEqualsTimes = 0;
                                  }
                                  infoDiv.childNodes[1].firstChild.setValue(percents);
                                  fileForAPI.progress = percents/100;
                                  fileUpload._events._fireFileUploadInProgressEvent(fileForAPI);
                                  setTimeout(function () {
                                    progressRequest(inputForFile);
                                  }, 500);
                                }
                              } else {// when file already uploaded
                                infoDiv.childNodes[1].firstChild.setValue(percents);
                                fileForAPI.progress = percents/100;
                                fileUpload._lengthUploadedFiles++;
                                /*removing input*/
                                inputsStorage.removeChild(inputForFile.parentNode.parentNode); // delete divForFileInput
                                // infoDiv updating
                                infoDiv._status = O$.FileUpload.Status.SUCCESSFUL;
                                infoDiv.childNodes[2].innerHTML = statusLabelUploaded;
                                setStatusforFileWithId(inputForFile._uniqueId, "SUCCESSFUL");
                                setClearBtnAndEventHandler(infoDiv, inputForFile._idInputAndDiv);
                                fileForAPI.status = O$.FileUpload.Status.SUCCESSFUL;
                                fileUpload._events._fireFileUploadEndEvent(fileForAPI);
                                prepareUIWhenAllRequestsFinished(true);
                              }
                            }

                          },
                          null,
                          true
                  );
                }

                function sendIsStoppedRequest(inputForFile, infoDiv, fileForAPI){
                  O$.requestComponentPortions(fileUpload.id, ["nothing"],
                          JSON.stringify({stoppedRequest:true, uniqueIdOfFile:inputForFile._uniqueId}),
                          function (fileUpload, portionName, portionHTML, portionScripts, portionData) {
                            inputForFile._isInterrupted = true;
                            infoDiv._status = O$.FileUpload.Status.STOPPED;
                            inputsStorage.removeChild(inputForFile.parentNode.parentNode);
                            infoDiv.childNodes[2].innerHTML = statusStoppedText;
                            setStatusforFileWithId(inputForFile._uniqueId, "STOPPED");
                            setClearBtnAndEventHandler(infoDiv, inputForFile._idInputAndDiv);
                            fileForAPI.status = O$.FileUpload.Status.STOPPED;
                            fileUpload._events._fireFileUploadEndEvent(fileForAPI);
                            prepareUIWhenAllRequestsFinished(true);
                          }, null, true);
                }

                function sendIsErrorRequest(inputForFile, infoDiv, fileForAPI){
                  O$.requestComponentPortions(fileUpload.id, ["nothing"],
                          JSON.stringify({stoppedRequest:true, uniqueIdOfFile:inputForFile._uniqueId}),
                          function (fileUpload, portionName, portionHTML, portionScripts, portionData) {
                            if (portionData['isStopped'] == "true") {
                              inputForFile._isInterrupted = true;
                              infoDiv._status = O$.FileUpload.Status.FAILED;
                              fileForAPI.status = O$.FileUpload.Status.FAILED;
                              inputsStorage.removeChild(inputForFile.parentNode.parentNode);
                              infoDiv.childNodes[2].innerHTML = statusLabelUnexpectedError;
                              setStatusforFileWithId(inputForFile._uniqueId, "FAILED");
                              fileUpload._events._fireFileUploadEndEvent(fileForAPI);
                              setClearBtnAndEventHandler(infoDiv, inputForFile._idInputAndDiv);
                              prepareUIWhenAllRequestsFinished(true);
                            }
                          }, null, true);
                }

                function setupUIForUpload(inputForFile, infoDiv, fileForAPI) {
                  function setStopButtonBehavior(inputForFile, infoDiv){
                    infoDiv.childNodes[3].removeChild(infoDiv.childNodes[3].firstChild);
                    var stopFileDiv = stopFacet.cloneNode(true);
                    stopFileDiv.setAttribute("id", stopFacet.id + inputForFile._idInputAndDiv);
                    infoDiv.childNodes[3].appendChild(stopFileDiv);
                    O$.addEventHandler(stopFileDiv, "focus", focusHandler);
                    O$.addEventHandler(stopFileDiv, "blur", blurHandler);
                    stopFileDiv._clickHandler = function () {
                      stopFileDiv.style.visibility = "hidden";
                      infoDiv.childNodes[2].innerHTML = statusStoppingText;
                      var iframe = inputForFile.nextSibling;
                      iframe.src = "";
                      inputForFile._wantToInterrupt = true;
                    };
                    O$.addEventHandler(stopFileDiv, "click", stopFileDiv._clickHandler);
                    if (O$.isChrome() || O$.isSafari()) {
                      O$.addEventHandler(stopFileDiv, "mousedown", function () {
                        shouldInvokeFocusNonModifiable = false;
                      });
                      O$.addEventHandler(stopFileDiv, "mouseup", function () {
                        shouldInvokeFocusNonModifiable = true;
                      });
                      O$.addEventHandler(stopFileDiv, "keyup", function () {
                        shouldInvokeFocusEvHandler = false;
                      });
                    }
                  }
                  infoDiv._status = O$.FileUpload.Status.IN_PROGRESS;
                  fileForAPI.status = O$.FileUpload.Status.IN_PROGRESS;
                  infoDiv.childNodes[2].innerHTML = statusLabelInProgress;
                  setStopButtonBehavior(inputForFile, infoDiv);
                }

                function progressHTMl5Request(file, request) {
                  O$.requestComponentPortions(fileUpload.id, ["nothing"],
                          JSON.stringify({progressRequest:"true", fileId:file._uniqueId}),
                          function (fileUpload, portionName, portionHTML, portionScripts, portionData) {
                            var infoDiv = O$(allInfos.id + file._infoId);
                            var fileForAPI = fileUpload._getFile(file._infoId);
                            if (portionData['isFileSizeExceed'] == "true") {
                              infoDiv._status = O$.FileUpload.Status.SIZE_LIMIT_EXCEEDED;
                              fileForAPI.status = O$.FileUpload.Status.SIZE_LIMIT_EXCEEDED;
                              removeHTML5File(file);
                              infoDiv.childNodes[2].innerHTML = statusLabelErrorSize;
                              setStatusforFileWithId(file._uniqueId, "SIZE_LIMIT_EXCEEDED");
                              fileUpload._events._fireFileUploadEndEvent(fileForAPI);
                              setClearBtnAndEventHandler(infoDiv, file._infoId);
                              prepareUIWhenAllRequestsFinished(true);
                            } else {
                              var percents = portionData['progressInPercent'];
                              if (percents != 100) {
                                if (!file._isInterrupted) {
                                  if (file._wantToInterrupt) {
                                    sendIsStoppedRequestHTML5(file, infoDiv, fileForAPI);
                                  }
                                  if (infoDiv.childNodes[1].firstChild.getValue() == percents) {
                                    if (!file._percentsEqualsTimes) {
                                      file._percentsEqualsTimes = 0;
                                    }
                                    file._percentsEqualsTimes++;
                                    if (file._percentsEqualsTimes > fileUpload._numberOfErrorRequest) {
                                      sendIsErrorRequestHTML5(file, infoDiv, fileForAPI);
                                    }
                                    if (file._percentsEqualsTimes > fileUpload._numberOfFailedRequest) {
                                      file._isInterrupted = true;
                                      sendInformThatRequestFailed(file._uniqueId);
                                      infoDiv._status = O$.FileUpload.Status.FAILED;
                                      fileForAPI.status = O$.FileUpload.Status.FAILED;
                                      removeHTML5File(file);
                                      infoDiv.childNodes[2].innerHTML = statusLabelUnexpectedError;
                                      setStatusforFileWithId(file._uniqueId, "FAILED");
                                      fileUpload._events._fireFileUploadEndEvent(fileForAPI);
                                      setClearBtnAndEventHandler(infoDiv, file._infoId);
                                      prepareUIWhenAllRequestsFinished(true);
                                    }
                                  } else {
                                    file._percentsEqualsTimes = 0;
                                  }
                                  infoDiv.childNodes[1].firstChild.setValue(percents);
                                  fileForAPI.progress = percents/100;
                                  fileUpload._events._fireFileUploadInProgressEvent(fileForAPI);
                                  setTimeout(function () {
                                    progressHTMl5Request(file, request);
                                  }, 500);
                                }
                              } else {// when file already uploaded
                                infoDiv.childNodes[1].firstChild.setValue(percents);
                                fileForAPI.progress = percents/100;
                                fileUpload._lengthUploadedFiles++;
                                /*removing file*/
                                removeHTML5File(file);
                                // infoDiv updating
                                infoDiv._status = O$.FileUpload.Status.SUCCESSFUL;
                                infoDiv.childNodes[2].innerHTML = statusLabelUploaded;
                                setStatusforFileWithId(file._uniqueId, "SUCCESSFUL");
                                setClearBtnAndEventHandler(infoDiv, file._infoId);
                                fileForAPI.status = O$.FileUpload.Status.SUCCESSFUL;
                                fileUpload._events._fireFileUploadEndEvent(fileForAPI);
                                prepareUIWhenAllRequestsFinished(true);
                              }
                            }

                          },
                          null,
                          true
                  );
                }

                function sendIsStoppedRequestHTML5(file, infoDiv, fileForAPI){
                  O$.requestComponentPortions(fileUpload.id, ["nothing"],
                          JSON.stringify({stoppedRequest:true, uniqueIdOfFile:file._uniqueId}),
                          function (fileUpload, portionName, portionHTML, portionScripts, portionData) {
                            if (portionData['isStopped'] == "true") {
                              file._isInterrupted = true;
                              infoDiv._status = O$.FileUpload.Status.STOPPED;
                              removeHTML5File(file);
                              infoDiv.childNodes[2].innerHTML = statusStoppedText;
                              setStatusforFileWithId(file._uniqueId, "STOPPED");
                              setClearBtnAndEventHandler(infoDiv, file._infoId);
                              fileForAPI.status = O$.FileUpload.Status.STOPPED;
                              fileUpload._events._fireFileUploadEndEvent(fileForAPI);
                              prepareUIWhenAllRequestsFinished(true);
                            }
                          }, null, true);
                }

                function sendIsErrorRequestHTML5(file, infoDiv, fileForAPI){
                  O$.requestComponentPortions(fileUpload.id, ["nothing"],
                          JSON.stringify({stoppedRequest:true, uniqueIdOfFile:file._uniqueId}),
                          function (fileUpload, portionName, portionHTML, portionScripts, portionData) {
                            if (portionData['isStopped'] == "true") {
                              file._isInterrupted = true;
                              infoDiv._status = O$.FileUpload.Status.FAILED;
                              fileForAPI.status = O$.FileUpload.Status.FAILED;
                              removeHTML5File(file);
                              infoDiv.childNodes[2].innerHTML = statusLabelUnexpectedError;
                              setStatusforFileWithId(file._uniqueId, "FAILED");
                              fileUpload._events._fireFileUploadEndEvent(fileForAPI);
                              setClearBtnAndEventHandler(infoDiv, file._infoId);
                              prepareUIWhenAllRequestsFinished(true);
                            }
                          }, null, true);
                }

                function setupUIForUploadHTML5(file, infoDiv, request, fileForAPI) {
                  function setStopButtonBehaviorHTML5(file, infoDiv, request) {
                    infoDiv.childNodes[3].removeChild(infoDiv.childNodes[3].firstChild);
                    var stopFileDiv = stopFacet.cloneNode(true);
                    stopFileDiv.setAttribute("id", stopFacet.id + file._infoId);
                    infoDiv.childNodes[3].appendChild(stopFileDiv);
                    O$.addEventHandler(stopFileDiv, "focus", focusHandler);
                    O$.addEventHandler(stopFileDiv, "blur", blurHandler);
                    stopFileDiv._clickHandler = function () {
                      stopFileDiv.style.visibility = "hidden";
                      infoDiv.childNodes[2].innerHTML = statusStoppingText;
                      request.abort();
                      file._wantToInterrupt = true;
                    };
                    O$.addEventHandler(stopFileDiv, "click", stopFileDiv._clickHandler);
                    if (O$.isChrome() || O$.isSafari()) {
                      O$.addEventHandler(stopFileDiv, "mousedown", function () {
                        shouldInvokeFocusNonModifiable = false;
                      });
                      O$.addEventHandler(stopFileDiv, "mouseup", function () {
                        shouldInvokeFocusNonModifiable = true;
                      });
                      O$.addEventHandler(stopFileDiv, "keyup", function () {
                        shouldInvokeFocusEvHandler = false;
                      });
                    }
                  }
                  infoDiv._status = O$.FileUpload.Status.IN_PROGRESS;
                  fileForAPI.status = O$.FileUpload.Status.IN_PROGRESS;
                  infoDiv.childNodes[2].innerHTML = statusLabelInProgress;
                  setStopButtonBehaviorHTML5(file, infoDiv, request);
                }

                function sendInformThatRequestFailed(fileId){
                  O$.requestComponentPortions(fileUpload.id, ["nothing"],
                          JSON.stringify({informFailedRequest:true, uniqueIdOfFile:fileId}),
                          function (fileUpload, portionName, portionHTML, portionScripts, portionData) {}, null, true);
                }

                function setStatusforFileWithId(id, status) {
                  for (var k = 0; k < fileUpload._listOfids.length; k++) {
                    if (id == fileUpload._listOfids[k][0]) {
                      fileUpload._listOfids[k][3] = status;
                      break;
                    }
                  }
                }

                function prepareUIWhenAllRequestsFinished(shouldCheckRequest) {
                  function sendCheckRequest() {
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
                                fileUpload._events._fireUploadEndEvent(fileUpload._allFiles);
                              } else {
                                setTimeout(sendCheckRequest, 500);
                              }
                            }, null, true);
                  }

                  if (inputsStorage.childNodes.length == 0 && fileUpload._filesHTML5.length == 0) { // if all files are  already uploaded
                    if (shouldCheckRequest)
                      setTimeout(sendCheckRequest, 500);

                    fileUpload._numberOfFilesToUpload = 0;
                    if (fileUpload._multiUpload) {
                      clearAllButton.style.visibility = "visible";
                      uploadButton.style.visibility = "visible";
                    }
                    inputInAddBtn.disabled = false;
                    fileUpload._inUploading = false;
                    addButtonTitleInput.className = addButtonClass;
                    initHeaderButtons();
                    //set focus on addButton
                    shouldInvokeFocusEvHandler = false;
                    setFocusOnComponent();
                    shouldInvokeFocusEvHandler = true;
                  }
                }

                function setClearBtnAndEventHandler(infoDiv, infoId) {
                  infoDiv.childNodes[3].removeChild(infoDiv.childNodes[3].firstChild);
                  var clearFileDiv = clearFacet.cloneNode(true);
                  clearFileDiv.setAttribute("id", clearFacet.id + infoId);
                  infoDiv.childNodes[3].appendChild(clearFileDiv);
                  O$.addEventHandler(clearFileDiv, "focus", focusHandler);
                  O$.addEventHandler(clearFileDiv, "blur", blurHandler);
                  clearFileDiv._clickHandler = function () {
                    fileUpload._removeFileFromAllFiles(infoId);
                    allInfos.removeChild(infoDiv);
                    if (allInfos.childNodes.length == 0) {
                      clearAllButton.style.display = "none";
                    }
                    shouldInvokeFocusNonModifiable = false;
                    setFocusOnComponent();
                    shouldInvokeFocusNonModifiable = true;
                  };
                  O$.addEventHandler(clearFileDiv, "click", clearFileDiv._clickHandler);
                  if (O$.isChrome() || O$.isSafari()) {
                    O$.addEventHandler(clearFileDiv, "mousedown", function () {
                      shouldInvokeFocusNonModifiable = false;
                    });
                    O$.addEventHandler(clearFileDiv, "mouseup", function () {
                      shouldInvokeFocusNonModifiable = true;
                    });
                    O$.addEventHandler(clearFileDiv, "keyup", function () {
                      shouldInvokeFocusEvHandler = false;
                    });
                  }
                }
              },
              __clearAllButtonClickHandler:function () {
                shouldInvokeFocusNonModifiable = false;
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

                clearAllButton.style.display = "none";
                //setFocusOnComponent();
                shouldInvokeFocusNonModifiable = true;
              }
            }
    );
    initHeaderButtons();
    setFocusBlurBehaviour();
    setUploadButtonBehaviour();

    setBehaviorForDragAndDropArea();
    O$.FileUpload._initFileUploadAPI(fileUpload);
    function setBehaviorForDragAndDropArea(){
      var area = O$(componentId + "::footer::dragArea");
      O$.extend(area.parentNode,{
        _findPosition: function () {
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
        _getDistanceTo: function(x, y){
          var areasXY = this.parentNode._findPosition();
          if (areasXY[0] < x && areasXY[1] < y) {
            return x - areasXY[0] + y - areasXY[1];
          } else if (areasXY[0] >= x && areasXY[1] >= y) {
            return areasXY[0] - x + areasXY[1] - y;
          } else if (areasXY[0] >= x && areasXY[1] < y) {
            return areasXY[0] - x + y - areasXY[1];
          } else if (areasXY[0] < x && areasXY[1] >= y){
            return x - areasXY[0] + areasXY[1] - y;
          }
        },
        _hideAllExceptThis:function () {
          for (var i = 0; i < O$._dropAreas.length; i++) {
            if (O$._dropAreas[i] != this) {
              O$._dropAreas[i].style.display = "none";
              O$._dropAreas[i]._isVisible = false;
            }
          }
        }
      });
      if (!O$._dropAreas){
        O$._dropAreas = [];
      }
      O$._dropAreas.push(area);
      var body = document.getElementsByTagName('body')[0];
      area._isVisible = false;
      setDragEventsForBody(body, area);
      setDragEventsForFileUpload(area);

      function isFilesDragged(evt) {
        if (isBrowserSupportHTML5FileUpload()){
          if (evt.dataTransfer.files && evt.dataTransfer.files.length > 0)
            return true;
          if (evt.dataTransfer.types)
            return evt.dataTransfer.types[2] == "Files" || evt.dataTransfer.types[0] == "Files";
        }
        return false;
      }

      function cancelDragEvent(evt) {
        evt.stopPropagation();
        evt.preventDefault();
      }

      function setDragEventsForBody(body, area) {
        O$.addEventHandler(body, "dragover", function (evt) {
          if (isFilesDragged(evt) && !inputInAddBtn.disabled){
            if (area._isNearest(evt.clientX, evt.clientY)){
              area._hideAllExceptThis();
              area.style.display = "block";
              area._isVisible = true;
            }
          }
          cancelDragEvent(evt);
        });
        O$.addEventHandler(body, "drop", function (evt) {
          if (isFilesDragged(evt)){
            area.style.display = "none";
            area._isVisible = false;
          }
          cancelDragEvent(evt);
        });
        O$.addEventHandler(body, "dragexit", cancelDragEvent);

        O$.addEventHandler(window, "mousemove", function () {
          if (area._isVisible){
            area.style.display = "none";
          }
        });
      }

      function setDragEventsForFileUpload(area) {
        O$.addEventHandler(area, "dragenter", cancelDragEvent);
        O$.addEventHandler(area, "dragleave", dragleaveEventHandler);
        O$.addEventHandler(area, "dragexit", cancelDragEvent);
        O$.addEventHandler(area, "dragover", dragoverEventHandler);
        O$.addEventHandler(area, "drop", dropEventHandler);

        function dragoverEventHandler(evt){
          cancelDragEvent(evt);
          if (isFilesDragged(evt)) {
            O$.setStyleMappings(area, {dragover : dropTargetCrossoverClass});
          }
        }

        function dragleaveEventHandler(evt) {
          cancelDragEvent(evt);
          O$.setStyleMappings(area, {dragover : null});
        }

        function dropEventHandler(evt) {
          cancelDragEvent(evt);
          if (isFilesDragged(evt)) {
            area.style.display = "none";
            area._isVisible = false;
            var files = evt.dataTransfer.files;
            var shouldCallOnChange = false;
            for (var i = 0; i < files.length; i++) {
              files[i]._fromDnD = true;
              if (processFileAddingHTML5(files[i])){
                shouldCallOnChange = true;
                if (!fileUpload._multiUpload){
                  break;
                }
              }
            }
            if (shouldCallOnChange){
              fileUpload._events._fireChangeEvent();
              setUploadButtonAfterFileHaveBeenAdded();
            }
          }
        }
      }
    }

    if (O$.isChrome() || O$.isSafari()){
      O$.addEventHandler(clearAllButton, "mousedown", function () {
        shouldInvokeFocusNonModifiable = false;
      });
      O$.addEventHandler(clearAllButton, "mouseup", function () {
        shouldInvokeFocusNonModifiable = true;
      });
    }
    O$.addEventHandler(clearAllButton, "click", fileUpload.__clearAllButtonClickHandler);

    function processFileAdding(inputForFile) {
      if (isFileNameNotApplied(inputForFile.value)) {
        inputForFile.value = "";
        shouldInvokeFocusEvHandler = true;
        return false;
      }
      addButtonTitleInput.className = addButtonClass;
      addButton._inFocus = false;
      inputForFile._idInputAndDiv = idOfInfoAndInputDiv;
      createAndAppendComplexInputWithId(inputForFile);
      fileUpload._allFiles.push(new O$.FileUpload.File(fileUpload, inputForFile._idInputAndDiv, inputForFile.value, O$.FileUpload.Status.NEW, null));
      fileUpload._numberOfFilesToUpload++;
      idOfInfoAndInputDiv++;
      inputInAddBtn = createInputInAddBtn(idOfInfoAndInputDiv);
      divForInputInAddBtn.appendChild(inputInAddBtn);

      O$.addEventHandler(inputInAddBtn,"focus",focusHandler);
      O$.addEventHandler(inputInAddBtn,"blur",blurHandler);

      fileUpload._events._fireChangeEvent();

      var fileInfo = createInfoWindowForFile(inputForFile._idInputAndDiv, inputForFile.value);
      var cancelFileDiv = fileInfo.childNodes[3].firstChild;
      setClickHandlerOnCancelButton(cancelFileDiv, inputForFile, fileInfo);

      function setClickHandlerOnCancelButton(cancelFileDiv, inputForFile, fileInfo) {
        cancelFileDiv.deleteFileInputFunction = function () {
          fileUpload._removeFileFromAllFiles(inputForFile._idInputAndDiv);
          deleteFileInput(inputForFile, fileInfo);
          if (couldCallChangeEvent){
            fileUpload._events._fireChangeEvent();
          }
          if (allInfos.childNodes.length == 0) {
            clearAllButton.style.display = "none";
          }
          shouldInvokeFocusEvHandler = false;
          setFocusOnComponent();
          if (!O$.isExplorer())
            shouldInvokeFocusEvHandler = true;
        };
        cancelFileDiv._clickHandler = cancelFileDiv.deleteFileInputFunction;
        O$.addEventHandler(cancelFileDiv, "click", cancelFileDiv._clickHandler);
      }

      if (fileUpload._maxQuantity != 0
              && fileUpload._maxQuantity <= (fileUpload._numberOfFilesToUpload + fileUpload._lengthUploadedFiles)) {
        inputInAddBtn.disabled = true;
        addButtonTitleInput.className = addButtonDisabledClass;
      }
      clearAllButton.style.display = "block";
      setUploadButtonAfterFileHaveBeenAdded();
      return true;
    }

    function deleteFileInput(inputForFile, infoWindow) {
      inputsStorage.removeChild(inputForFile.parentNode.parentNode);
      allInfos.removeChild(infoWindow);
      fileUpload._numberOfFilesToUpload--;
      if (inputInAddBtn.disabled
              && fileUpload._maxQuantity > (fileUpload._numberOfFilesToUpload + fileUpload._lengthUploadedFiles)) {
        inputInAddBtn.disabled = false;
        addButtonTitleInput.className = addButtonClass;
      }
      if ((fileUpload._minQuantity > (fileUpload._numberOfFilesToUpload + fileUpload._lengthUploadedFiles))
              || fileUpload._numberOfFilesToUpload == 0) {
        if (fileUpload._multiUpload){
          uploadButton.style.visibility = "hidden";
        }
      }
    }

    function isFileNameNotApplied(filename){
      function isFileNameAlreadyExist(filename) {
        var infos = allInfos.childNodes;
        for (var k = 0; k < infos.length; k++) {
          if (infos[k]._status != O$.FileUpload.Status.FAILED || infos[k]._status != O$.FileUpload.Status.STOPPED
                  || infos[k]._status != O$.FileUpload.Status.SIZE_LIMIT_EXCEEDED) {
            var value = infos[k].childNodes[0].innerHTML;
            if (value == getFileName(filename)) {
              return true;
            }
          }
        }
        return false;
      }

      function isAcceptedTypeOfFile(filename) {

        if (fileUpload._typesOfFile == null)
          return true;

        var isAccepted = false;
        fileUpload._typesOfFile.forEach(function(type) {
          if (filename.indexOf(type, filename.length - type.length) !== -1) {
            isAccepted = true;
          }
        });
        return isAccepted;
      }

      return (filename == "" || !isAcceptedTypeOfFile(filename)
              || (!isDuplicateAllowed && isFileNameAlreadyExist(filename)))
    }
    function createInfoWindowForFile(fileId, filename) {

      var infoWindow = document.createElement("tr");
      infoWindow._status = O$.FileUpload.Status.NEW;

      var fileNameTD = document.createElement("td");
      fileNameTD.className = infoTitleClass;
      fileNameTD.innerHTML = getFileName(filename);
      infoWindow.appendChild(fileNameTD);

      var progressTD = document.createElement("td");
      progressTD.className = progressBarClass;
      var progressFacet = progressBar.cloneNode(true);
      progressFacet.setAttribute("id", progressBarId + fileId);
      progressTD.appendChild(progressFacet);
      infoWindow.appendChild(progressTD);

      var statusTD = document.createElement("td");
      statusTD.className = infoStatusClass;
      if (statusLabelNotUploaded != null)
        statusTD.innerHTML = statusLabelNotUploaded;
      infoWindow.appendChild(statusTD);

      var cancelFileTD = document.createElement("td");
      cancelFileTD.style.width = "1px";
      var cancelFileDiv = cancelFacet.cloneNode(true);
      cancelFileDiv.setAttribute("id", cancelFacet.id + fileId);
      if (O$.isExplorer()) {
        O$.addEventHandler(cancelFileDiv, "mouseup", function () {
          shouldInvokeFocusNonModifiable = true;
        });
      }
      O$.addEventHandler(cancelFileDiv, "focus", focusHandler);
      O$.addEventHandler(cancelFileDiv, "blur", blurHandler);

      if (O$.isChrome() || O$.isSafari()) {
        O$.addEventHandler(cancelFileDiv, "mousedown", function () {
          shouldInvokeFocusNonModifiable = false;
        });
        O$.addEventHandler(cancelFileDiv, "mouseup", function () {
          shouldInvokeFocusNonModifiable = true;
        });
        O$.addEventHandler(cancelFileDiv, "keyup", function () {
          shouldInvokeFocusEvHandler = false;
        });
      }

      cancelFileTD.appendChild(cancelFileDiv);
      infoWindow.appendChild(cancelFileTD);

      infoWindow.setAttribute("id", allInfos.id + fileId);
      infoWindow.className = fileInfoClass;
      if (!fileUpload._multiUpload) {
        allInfos.innerHTML = "";
      }
      allInfos.appendChild(infoWindow);
      O$.ProgressBar.initCopyOf(progressBar, progressFacet);
      return infoWindow;
    }

    function processFileAddingHTML5(file) {
      /*Because of imperfect value of file's size when directory is chosen (I got values 4096*x where x is between 1 - 8)
      this method doesn't fully guarantee that file is directory.
      * */
      function isDirectory(file) {
        if (file.size == 0){
          return true;
        }
        if (file.type != ""){
          return false;
        }
        if (file.size % 4096 == 0) {
          var koef = file.size / 4096;
          if (koef > 0 && koef < 10) {
            var index = file.name.lastIndexOf(".");
            if (index == -1) {
              return true;
            }
            var extension = file.name.substr(index+1);
            if (!(extension.length >=3 &&  extension.match(/^[a-zA-Z]+$/))){
              return true;
            }

          }
        }
        return false;
      }
      if (isFileNameNotApplied(file.name) || inputInAddBtn.disabled || (file._fromDnD && isDirectory(file))) {
        return false;
      }
      file._uniqueId = fileUpload._ID + idOfInfoAndInputDiv;
      file._fakeInput = componentId + "::inputs::input" + idOfInfoAndInputDiv + "::form::fileInput";
      file._infoId = idOfInfoAndInputDiv;
      fileUpload._filesHTML5.push(file);
      fileUpload._allFiles.push(new O$.FileUpload.File(fileUpload, file._infoId, file.name, O$.FileUpload.Status.NEW, file.size));
      var fileInfo = createInfoWindowForFile(idOfInfoAndInputDiv, file.name);
      var cancelFileDiv = fileInfo.childNodes[3].firstChild;
      setSpecificClickHandlerForCancelBtn(cancelFileDiv, fileInfo);

      function setSpecificClickHandlerForCancelBtn(cancelFileDiv, fileInfo) {
        cancelFileDiv.deleteFileInputFunction = function () {
          fileUpload._removeFileFromAllFiles(file._infoId);
          allInfos.removeChild(fileInfo);
          removeHTML5File(file);
          fileUpload._numberOfFilesToUpload--;
          if (inputInAddBtn.disabled
                  && fileUpload._maxQuantity > (fileUpload._numberOfFilesToUpload + fileUpload._lengthUploadedFiles)) {
            inputInAddBtn.disabled = false;
            addButtonTitleInput.className = addButtonClass;
          }
          if ((fileUpload._minQuantity > (fileUpload._numberOfFilesToUpload + fileUpload._lengthUploadedFiles))
                  || fileUpload._numberOfFilesToUpload == 0) {
            if (fileUpload._multiUpload) {
              uploadButton.style.visibility = "hidden";
            }
          }

          if (allInfos.childNodes.length == 0) {
            clearAllButton.style.display = "none";
          }
          shouldInvokeFocusNonModifiable = false;
          setFocusOnComponent();
          shouldInvokeFocusNonModifiable = true;

          if (couldCallChangeEvent){
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
        inputInAddBtn.disabled = true;
        addButtonTitleInput.className = addButtonDisabledClass;
      }
      clearAllButton.style.display = "block";
      return true;
    }

    function initHeaderButtons() {
      if (fileUpload._isDisabled) {
        inputInAddBtn.disabled = true;
        addButtonTitleInput.className = addButtonDisabledClass;
        clearAllButton.style.display = "none";
      } else {
        if (fileUpload._maxQuantity != 0 && fileUpload._lengthUploadedFiles == fileUpload._maxQuantity){
          inputInAddBtn.disabled = true;
          addButtonTitleInput.className = addButtonDisabledClass;
        }else{
          inputInAddBtn.disabled = false;
        }

        if (allInfos.childNodes.length == 0) {
          clearAllButton.style.display = "none";
        }
      }

      if (!fileUpload._multiUpload){
        uploadButton.style.display = "none";
      } else {
        uploadButton.style.visibility = "hidden";
      }
    }

    function setUploadButtonBehaviour() {
      if (O$.isChrome() || O$.isSafari()) {
        O$.addEventHandler(uploadButton, "mousedown", function() {
          shouldInvokeFocusNonModifiable = false;
        });
        O$.addEventHandler(uploadButton, "mouseup", function() {
          shouldInvokeFocusNonModifiable = true;
        });
        O$.addEventHandler(uploadButton, "keyup", function() {
          shouldInvokeFocusEvHandler = false;
        });
      }
      O$.addEventHandler(uploadButton, "click", fileUpload.__uploadButtonClickHandler);

    }

    function createStructureForInputs(){
      var allInputsForFiles = document.createElement("div");
      allInputsForFiles.setAttribute("id", componentId + "::inputs");
      allInputsForFiles.style.display = "none";
      var body = document.getElementsByTagName('body')[0];
      body.appendChild(allInputsForFiles);
      return allInputsForFiles;
    }

    function createAndAppendComplexInputWithId(inputField) {
      var divForFileInput = document.createElement("div");
      divForFileInput.setAttribute("id", inputsStorage.id + "::input" + inputField._idInputAndDiv);

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

      inputsStorage.appendChild(divForFileInput);
      return divForFileInput;
    }

    function createInputInAddBtn(id){
      var input = document.createElement("input");
      input.setAttribute("type", "file");
      if (fileUpload._multiUpload && isBrowserSupportHTML5FileUpload())
        input.setAttribute("multiple", "");

      //todo move somewhere else these styles
      input.style.overflow = "hidden";
      input.style.position = "absolute";
      input.style.top = "0";
      input.style.right = "0";
      input.style.fontSize = "100px";
      input.style.opacity = "0";
      input.style.filter = "alpha(opacity=0)";
      input.style.sFilter = "progid:DXImageTransform.Microsoft.Alpha(Opacity=0)";

      if (tabIndex!=-1){
        input.setAttribute("tabindex", tabIndex);
      }
      O$.addEventHandler(input,"focus",function(){
        if (!inputInAddBtn.disabled && !addButton._afterMouseDown){
          addButtonTitleInput.className = addButtonOnFocusClass;
        }
        addButton._inFocus = true;
      });
      O$.addEventHandler(input,"blur",function(){
        if (!inputInAddBtn.disabled){
          addButtonTitleInput.className = addButtonClass;
          addButton._inFocus = false;
        }
      });
      O$.addEventHandler(input, "change", function(evt) {
        shouldInvokeFocusEvHandler = false;
        if (isMultipleFileSelectSupported(evt)){
          var files = evt.target.files;
          var shouldCallOnChange = false;
          for (var fileIndex = 0; fileIndex < files.length; fileIndex++) {
            if (processFileAddingHTML5(files[fileIndex])){
              shouldCallOnChange = true;
              if (!fileUpload._multiUpload){
                break;
              }
            }
          }
          if (shouldCallOnChange) {
            fileUpload._events._fireChangeEvent();
            setUploadButtonAfterFileHaveBeenAdded();
          }
        }else{
          var isAdded = processFileAdding(input);
        }
        if (isAdded){
          setTimeout(function () {
            if (O$.isChrome() || O$.isSafari())
              shouldInvokeFocusEvHandler = true;
            setFocusOnComponent();
            if (O$.isExplorer()) {
              shouldInvokeFocusEvHandler = false;
              ignoreBlurForIE = true;
            }
          }, 1);
        }else{
          if (O$.isChrome() || O$.isSafari()) {
            shouldInvokeFocusEvHandler = true;
            setFocusOnComponent();
          }
        }
      });
      if (O$.isChrome() || O$.isSafari()) { //chrome calls blur when file dialog popup
        O$.addEventHandler(input, "mousedown", function() {
          input.focus();
        });
      }
      return input;
    }

    function setStylesForAddButton(addButton){
      addButtonTitleInput.className = addButtonClass;
      O$.addEventHandler(addButton,"mouseover",function(){
        if (!inputInAddBtn.disabled) {
          addButtonTitleInput.className = addButtonOnMouseOverClass;
        }
      });
      O$.addEventHandler(addButton, "mouseout", function() {
        if (!inputInAddBtn.disabled) {
          if (addButton._inFocus) {
            addButtonTitleInput.className = addButtonOnFocusClass;
          } else {
            addButtonTitleInput.className = addButtonClass;
          }
        }
      });

      O$.addEventHandler(addButton,"mousedown",function(){
        if (!inputInAddBtn.disabled){
          addButtonTitleInput.className = addButtonOnMouseDownClass;
        }
        addButton._afterMouseDown = true;
      });
      O$.addEventHandler(window,"mouseup",function(){
        if (!inputInAddBtn.disabled){

          if (addButton._inFocus){
            addButtonTitleInput.className = addButtonOnFocusClass;
          }else{
            addButtonTitleInput.className = addButtonClass;
          }
        }
        addButton._afterMouseDown = false;
      });
    }

    function setFocusBlurBehaviour(){
      O$.addEventHandler(inputInAddBtn, "focus", focusHandler);
      O$.addEventHandler(uploadButton, "focus", focusHandler);
      O$.addEventHandler(clearAllButton, "focus", focusHandler);

      O$.addEventHandler(inputInAddBtn, "blur", blurHandler);
      O$.addEventHandler(uploadButton, "blur", blurHandler);
      O$.addEventHandler(clearAllButton, "blur", blurHandler);
      if (addButtonTitleInput.focus){
        O$.addEventHandler(addButtonTitleInput, "focus", focusHandler);
        O$.addEventHandler(addButtonTitleInput, "blur", blurHandler);
      }
    }

    function focusHandler() {
      var _this = this;
      if (activeElementNow == _this){
        return;

      }
      if (!shouldInvokeFocusEvHandler){
        shouldInvokeFocusEvHandler = true;
        return;
      }
      if (!shouldInvokeFocusNonModifiable){
        return;
      }
      activeElementNow = _this;
      if (wasFocused != null) {
        //previously was focused our element
      } else {
        fileUpload._events._fireFocusEvent();
      }               //2

      setTimeout(function() {
        onFocusWillBe = _this;     //4
      }, 1);
    }

    function blurHandler() {
      if (!shouldInvokeFocusEvHandler){
        shouldInvokeFocusEvHandler = true;
        return;
      }
      if ((O$.isChrome() || O$.isSafari()) && !ignoreBlurForChrome){
        ignoreBlurForChrome = true;
        return;
      }
      if ((O$.isChrome() || O$.isSafari()) && !shouldInvokeFocusNonModifiable){
        return;
      }

      wasFocused = this;   //1

      activeElementNow = null;
      var _this = this;
      setTimeout(function() {
        wasFocused = null;
        onFocusWillBe = null;//3
        setTimeout(function() {
          if (onFocusWillBe != null) {//5
            //next will be focused our element
          } else {
            if (O$.isExplorer() && ignoreBlurForIE) { //added for explorer - because of specific processing of adding file in input
              ignoreBlurForIE = false;
              return;
            }
            fileUpload._events._fireBlurEvent();
          }

        }, 1);
      }, 1);

    }

    function setFocusOnComponent() {
      if (!inputInAddBtn.disabled){
        inputInAddBtn.focus();
        return;
      }
      if (!uploadButton.disabled){
        uploadButton.focus();
        return;
      }
      if (!clearAllButton.disabled){
        clearAllButton.focus();
        return;
      }
      fileUpload._events._fireBlurEvent();
    }

    function getFileName(fullRoorOfFile) {
      var index = fullRoorOfFile.lastIndexOf('\\');
      return fullRoorOfFile.substring(index + 1);
    }

    function setTabIndexForAllButtons(tabIndex) {
      if (tabIndex != -1) {
        var buttons = [uploadButton,clearAllButton,clearFacet,stopFacet,cancelFacet];
        buttons.forEach(function(button) {
          button.setAttribute("tabindex", tabIndex);
        });
      }
    }

    function removeHTML5File(file){
      for (var index = 0; index < fileUpload._filesHTML5.length; index++) {
        if (fileUpload._filesHTML5[index] == file) {
          fileUpload._filesHTML5.splice(index, 1);
          break;
        }
      }
    }

    function isMultipleFileSelectSupported(evt){
      if (isBrowserSupportHTML5FileUpload()){
        if (evt.target.files)
          return true;
      }
      return false;
    }

    function isBrowserSupportHTML5FileUpload(){
      //Opera doesn't support file upload API , although it support multiupload
      //IE doesn't support at all
      //Safari has File Upload API, but with bugs. No solution for the moment. Look here: http://stackoverflow.com/questions/7231054/file-input-size-issue-in-safari-for-multiple-file-selection
      //Safari anytime can get not correct fileNames , so this feature is turned of, if Safari is  used.
      return !((!O$.isChrome() && O$.isSafari()) || typeof(window.FormData) == "undefined");
    }

    function setUploadButtonAfterFileHaveBeenAdded(){
      if (fileUpload._minQuantity <= (fileUpload._numberOfFilesToUpload + fileUpload._lengthUploadedFiles)) {
        if (fileUpload._isAutoUpload) {
          fileUpload.__uploadButtonClickHandler();
        } else {
          uploadButton.style.visibility = "visible";
        }
      }
    }
    function setAllEvents(){
      function createEventHandler(userHandler, eventName){
        return function(files){
          if (userHandler) {
            var event = O$.createEvent(eventName);
            if (files){
              if (files.length){
                event.files = files;
              }else{
                event.file = files;
              }
            }
            userHandler(event);
          }
        }
      }

      fileUpload._events._fireChangeEvent = createEventHandler(onchangeHandler, "change");
      fileUpload._events._fireUploadStartEvent = createEventHandler(onuploadstartHandler, "onuploadstart");
      fileUpload._events._fireUploadEndEvent = createEventHandler(onuploadendHandler, "onuploadend");
      fileUpload._events._fireFileUploadStartEvent = createEventHandler(onfileuploadstartHandler, "onfileuploadstart");
      fileUpload._events._fireFileUploadInProgressEvent = createEventHandler(onfileuploadinprogressHandler, "onfileuploadinprogress");
      fileUpload._events._fireFileUploadEndEvent = createEventHandler(onfileuploadendHandler, "onfileuploadend");


      //processing onfocus/onblur event
      fileUpload._events._fireBlurEvent = createEventHandler(fileUpload.onblur, "onblur");
      fileUpload.removeAttribute("onblur");
      fileUpload.onblur = null;

      fileUpload._events._fireFocusEvent = createEventHandler(fileUpload.onfocus, "onfocus");
      fileUpload.removeAttribute("onfocus");
      fileUpload.onfocus = null;

    }
  },
  _initFileUploadAPI:function (fileUpload) {
    //Helper methods for API
    O$.extend(fileUpload, {
      _removeFileFromAllFiles:function(id){
        for (var index = 0; index < fileUpload._allFiles.length; index++) {
          if (fileUpload._allFiles[index]._id == id) {
            fileUpload._allFiles.splice(index, 1);
            break;
          }
        }
      },
      _getFile: function(id){
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
      uploadAllFiles:function(){
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
      removeAllFiles:function(){
        if (!fileUpload._inUploading) {
          fileUpload.__clearAllButtonClickHandler();
          return true;
        }
        throw "Cannot remove files while they are uploading.";
      },
      /*This method is trying to stop all current files that in upload process. It is not guarantee that file will be stopped */
      stopAllUploads: function(){
        for (var index = 0; index < fileUpload._allFiles.length; index++) {
            if (fileUpload._allFiles[index].status == O$.FileUpload.Status.IN_PROGRESS) {
              var fileInfo = O$(fileUpload.id + "::infoDiv" + fileUpload._allFiles[index]._id);
              fileInfo.childNodes[3].firstChild._clickHandler();
            }
        }
      }
    });
  },
  File:O$.createClass(null,{
    constructor:function (component, id, name, status, size) {
      this._component = component;
      this._id = id;
      this.name = name;
      this.status = status;
      this.size = size;
      this.progress = null;
      O$.FileUpload._FileAPIInit(this);
    }
  }),
  _FileAPIInit:function(file){
    O$.extend(file,{
      remove:function(){
        for (var index = 0; index < file._component._allFiles.length; index++) {
          if (file._component._allFiles[index] == file) {  //check if uploading is go on if status uploading don't do it
            if (file.status != O$.FileUpload.Status.IN_PROGRESS) {
              O$(file._component.id + "::infoDiv" + file._id).childNodes[3].firstChild._clickHandler();
              return true;
            }
            throw "File cannot be removed when it's in progress(IN_PROGRESS status).";
          }
        }
        throw "There is no such file";
      },
      /*stopUpload() method is trying to stop uploading of file. It is not guarantee that file will be stopped*/
      stopUpload:function(){
        for (var index = 0; index < file._component._allFiles.length; index++) {
          if (file._component._allFiles[index] == file) {
            if (file._component._allFiles[index].status == O$.FileUpload.Status.IN_PROGRESS) {
              var fileInfo = O$(file._component.id + "::infoDiv" + file._id);
              fileInfo.childNodes[3].firstChild._clickHandler();
              return true;
            }
            throw "File cannot be stopped when it's not in progress(IN_PROGRESS status).";
          }
        }
        return "There is no such file";
      },
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
    })
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

