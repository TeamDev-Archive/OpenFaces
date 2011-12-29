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

//todo facet addbutton
//todo check statuses

O$.FileUpload = {
  _init: function(componentId, minQuantity, maxQuantity, lengthAlreadyUploadedFiles,
                  fileInfoClass, infoTitleClass, progressBarClass, infoStatusClass,
                  statusLabelNotUploaded, statusLabelInProgress, statusLabelUploaded, statusLabelErrorSize,
                  acceptedTypesOfFile,  isDuplicateAllowed,
                  addButtonId, addButtonClass, addButtonOnMouseOverClass, addButtonOnMouseDownClass,addButtonOnFocusClass,
                  addButtonDisabledClass,
                  uploadButtonId, clearAllButtonId, isDisabled,
                  isAutoUpload, tabIndex, progressBarId, statusStoppedText, multiUpload,ID) {
    var fileUpload = O$.initComponent(componentId, null, {
      _minQuantity : minQuantity,
      _maxQuantity : maxQuantity,
      _numberOfFilesToUpload : 0,
      _lengthUploadedFiles : lengthAlreadyUploadedFiles,
      _typesOfFile: (acceptedTypesOfFile != null) ? acceptedTypesOfFile.split(" ") : null,
      _isDisabled : isDisabled,
      _multiUpload : multiUpload,
      _isAutoUpload : isAutoUpload,
      _ID : ID
    });
    if (!fileUpload._multiUpload){
      fileUpload._isAutoUpload = true;
      //fileUpload._maxQuantity = 1;
    }
    //getting clear,stop,cancel, progressBar facet for each info window
    var elements = O$(componentId + "::elements");
    var divForClearFacet = O$(componentId + "::elements::clearFacet");
    var clearFacet = divForClearFacet.firstChild;
    elements.removeChild(divForClearFacet);
    clearFacet.disabled = false;

    var divForCancelFacet = O$(componentId + "::elements::removeFacet");
    var cancelFacet = divForCancelFacet.firstChild;
    elements.removeChild(divForCancelFacet);
    cancelFacet.disabled = false;

    var divForStopFacet = O$(componentId + "::elements::stopFacet");
    var stopFacet = divForStopFacet.firstChild;
    elements.removeChild(divForStopFacet);
    stopFacet.disabled = false;

    var progressBar= O$(progressBarId);
    if (!O$.isExplorer())
      elements.removeChild(progressBar);

    var idOfInfoAndInputDiv = 1;

    var uploadButton = O$(uploadButtonId);
    var clearAllButton = O$(clearAllButtonId);
    setTabIndexForAllButtons(tabIndex);
    //clearAllButton.style.float = "right";

    var allInfos = O$(componentId + "::infoDiv");
    if(O$.isExplorer()){
      if (O$.isQuirksMode() ||
              ((O$.isExplorer6() ||O$.isExplorer7() || O$.isExplorer8() || O$.isExplorer9()) &&
                      (document.documentMode == 7 || document.documentMode == 6)))
        allInfos = allInfos.firstChild;
    }


    var inputsStorage = createStructureForInputs();
    var inputsStorageId = inputsStorage.id;
    var inputFieldName = inputsStorageId + "::fileInput";

    //processing onChange event
    var inputForChange = O$(componentId + "::elements::helpfulInput");
    var onChangeFunc = inputForChange.onchange;
    elements.removeChild(inputForChange);
    if (!O$.isExplorer())
      fileUpload.removeChild(elements);
    var couldCallChangeEvent = true;

    //processing event onuploadstart,onuploadend
    var onUploadStartEventHandler = fileUpload.getAttribute('onuploadstart');
    fileUpload.removeAttribute("onuploadstart");
    var onUploadEndEventHandler = fileUpload.getAttribute('onuploadend');
    fileUpload.removeAttribute("onuploadend");

    //processing onfocus/onblur event
    var onBlur = fileUpload.onblur;
    fileUpload.removeAttribute("onblur");
    fileUpload.onblur = null;

    var onFocus = fileUpload.onfocus;
    fileUpload.removeAttribute("onfocus");
    fileUpload.onfocus = null;

    var  addButton = O$(addButtonId);
    setStylesForAddButton(addButton);
    var divForInputInAddBtn = O$(addButtonId+"::forInput");
    var inputInAddBtn = createInputInAddBtn(idOfInfoAndInputDiv);
    var addButtonTitle = O$(addButtonId + "::title");
    if (O$.isExplorer() && O$.isQuirksMode()){
      divForInputInAddBtn.style.height = O$.getElementSize(addButtonTitle).height;
    }
    addButtonTitle.firstChild.disabled = false;
    divForInputInAddBtn.appendChild(inputInAddBtn);//create first input

    //variables for focus/blur
    var onFocusWillBe = null;
    var wasFocused = null;
    var shouldInvokeFocusEvHandler = true;
    var shouldInvokeFocusNonModifiable = true;
    var ignoreBlurForChrome = true; // this variable only needed for specific behaviour of focus at Chrome browser
    var activeElementNow;
    var ignoreBlurForIE = false;

    initHeaderButtons();
    setFocusBlurBehaviour();
    setUploadButtonBehaviour();

    if (O$.isChrome()){
      O$.addEventHandler(clearAllButton,"mousedown",function(){
        shouldInvokeFocusNonModifiable = false;
      });
      O$.addEventHandler(clearAllButton,"mouseup",function(){
        shouldInvokeFocusNonModifiable = true;
      });
    }
    O$.addEventHandler(clearAllButton,"click",function(){
      shouldInvokeFocusNonModifiable = false;
      var infosLength = allInfos.childNodes.length;
      couldCallChangeEvent = false;
      var isCallingChangeEventNeeded = false;
      for (var index = 0; index < infosLength; index++){
        if (allInfos.childNodes[0].childNodes[3].firstChild.deleteFileInputFunction){
          isCallingChangeEventNeeded = true;
        }
        allInfos.childNodes[0].childNodes[3].firstChild.click();
      }
      couldCallChangeEvent = true;
      if (isCallingChangeEventNeeded){
        if (onChangeFunc != null) // onchangeHandler
          onChangeFunc();
      }


      if (!clearAllButton.disabled) {
        clearAllButton.disabled = true;
        clearAllButton.style.display = "none";
        //setFocusOnComponent();
      }
      shouldInvokeFocusNonModifiable = true;
    });


    function addFileInputAfter(inputForFile) {
      if (inputForFile.value == "" || !isAcceptedTypeOfFile(inputForFile.value)
              || (!isDuplicateAllowed && isFileNameAlreadyExist(inputForFile))) {
        inputForFile.value = "";
        shouldInvokeFocusEvHandler = true;
        return false;
      }
      addButton.className = addButtonClass;
      addButton._inFocus = false;
      createAndAppendComplexInputWithId(idOfInfoAndInputDiv,inputForFile);
      fileUpload._numberOfFilesToUpload++;
      idOfInfoAndInputDiv++;
      inputInAddBtn = createInputInAddBtn(idOfInfoAndInputDiv);
      divForInputInAddBtn.appendChild(inputInAddBtn);

      O$.addEventHandler(inputInAddBtn,"focus",focusHandler);
      O$.addEventHandler(inputInAddBtn,"blur",blurHandler);

      if (onChangeFunc != null) // onchangeHandler
        onChangeFunc();
      addFileToInfoWindow(inputForFile);

      if (fileUpload._maxQuantity != 0
              && fileUpload._maxQuantity <= (fileUpload._numberOfFilesToUpload + fileUpload._lengthUploadedFiles)) {
        inputInAddBtn.disabled = true;
        addButton.className = addButtonDisabledClass;
      }
      clearAllButton.disabled = false;
      clearAllButton.style.display = "block";


      if (fileUpload._minQuantity <= (fileUpload._numberOfFilesToUpload + fileUpload._lengthUploadedFiles)) {
        uploadButton.disabled = false;
        if (fileUpload._isAutoUpload){
          uploadButtonClickHandler();
        }else{
            uploadButton.style.visibility = "visible";
        }
      }

      function addFileToInfoWindow(inputForFile) {
        var infoWindow = document.createElement("tr");
        infoWindow._status = O$.statusEnum.NEW_ONE;

        var fileNameTD = document.createElement("td");
        fileNameTD.className = infoTitleClass;
        fileNameTD.innerHTML = getFileName(inputForFile.value);
        infoWindow.appendChild(fileNameTD);

        var progressTD = document.createElement("td");
        progressTD.className = progressBarClass;
        var progressFacet = progressBar.cloneNode(true);
        progressFacet.setAttribute("id", progressBarId + inputForFile._idInputAndDiv);
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
        cancelFileDiv.setAttribute("id",cancelFacet.id + inputForFile._idInputAndDiv);
        cancelFileDiv.deleteFileInputFunction = function () {
          deleteFileInput(inputForFile, infoWindow);
          if (onChangeFunc != null && couldCallChangeEvent) // onchangeHandler
            onChangeFunc();
          if (allInfos.childNodes.length == 0) {
            clearAllButton.style.display = "none";
            clearAllButton.disabled = true;
          }
          shouldInvokeFocusEvHandler = false;
          setFocusOnComponent();
          if (!O$.isExplorer())
            shouldInvokeFocusEvHandler = true;
        };
        if (O$.isExplorer()) {
          O$.addEventHandler(cancelFileDiv, "mouseup", function() {
            shouldInvokeFocusNonModifiable = true;
          });
        }
        O$.addEventHandler(cancelFileDiv, "focus", focusHandler);
        O$.addEventHandler(cancelFileDiv, "blur", blurHandler);

        if (O$.isChrome()) {
          O$.addEventHandler(cancelFileDiv, "mousedown", function() {
            shouldInvokeFocusNonModifiable = false;
          });
          O$.addEventHandler(cancelFileDiv, "mouseup", function() {
            shouldInvokeFocusNonModifiable = true;
          });
        }

        O$.addEventHandler(cancelFileDiv, "click", cancelFileDiv.deleteFileInputFunction);

        cancelFileTD.appendChild(cancelFileDiv);
        infoWindow.appendChild(cancelFileTD);

        infoWindow.setAttribute("id", allInfos.id + inputForFile._idInputAndDiv);
        //infoWindow.setAttribute("class", fileInfoClass);
        infoWindow.className = fileInfoClass;
        if (!fileUpload._multiUpload){
          allInfos.innerHTML="";
        }
        allInfos.appendChild(infoWindow);
        O$.ProgressBar.initCopyOf(progressBar, progressFacet);
      }

      function isFileNameAlreadyExist(inputForFile) {
        var infos = allInfos.childNodes;
        for (var k = 0; k < infos.length; k++) {
          if (infos[k]._status != O$.statusEnum.ERROR) {
            var value = infos[k].childNodes[0].innerHTML;
            if (value == getFileName(inputForFile.value)) {
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

      return true;
    }

    function deleteFileInput(inputForFile, infoWindow) {
      inputsStorage.removeChild(inputForFile.parentNode.parentNode);
      allInfos.removeChild(infoWindow);
      fileUpload._numberOfFilesToUpload--;
      if (inputInAddBtn.disabled
              && fileUpload._maxQuantity > (fileUpload._numberOfFilesToUpload + fileUpload._lengthUploadedFiles)) {
        inputInAddBtn.disabled = false;
        addButton.className = addButtonClass;
      }
      if (!uploadButton.disabled
              && ((fileUpload._minQuantity > (fileUpload._numberOfFilesToUpload + fileUpload._lengthUploadedFiles))
              || fileUpload._numberOfFilesToUpload == 0)) {
        uploadButton.disabled = true;
      }
    }

    function initHeaderButtons() {
      if (fileUpload._isDisabled) {
        inputInAddBtn.disabled = true;
        clearAllButton.style.display = "none";
        clearAllButton.disabled = true;
      } else {

        if (fileUpload._maxQuantity != 0 && fileUpload._lengthUploadedFiles == fileUpload._maxQuantity){
          inputInAddBtn.disabled = true;
          addButton.className = addButtonDisabledClass;
        }else{
          inputInAddBtn.disabled = false;
        }

        if (allInfos.childNodes.length == 0) {
          clearAllButton.style.display = "none";
          clearAllButton.disabled = true;
        }
      }

      uploadButton.disabled = true;
      if (!fileUpload._multiUpload){
        uploadButton.style.display = "none";
      } else {
        uploadButton.style.visibility = "hidden";
      }
    }

    function setUploadButtonBehaviour() {
      if (O$.isChrome()) {
        O$.addEventHandler(uploadButton, "mousedown", function() {
          shouldInvokeFocusNonModifiable = false;
        });
        O$.addEventHandler(uploadButton, "mouseup", function() {
          shouldInvokeFocusNonModifiable = true;
        });
      }
      O$.addEventHandler(uploadButton, "click",uploadButtonClickHandler);

    }

    function createStructureForInputs(){
      var allInputsForFiles = document.createElement("div");
      allInputsForFiles.setAttribute("id", componentId+"::inputs");
      allInputsForFiles.style.display = "none";
      var body = document.getElementsByTagName('body')[0];
      body.appendChild(allInputsForFiles);
      return allInputsForFiles;
    }

    function createAndAppendComplexInputWithId(id, inputField) {
      var divForFileInput = document.createElement("div");
      divForFileInput.setAttribute("id", inputsStorage.id + "::input" + id);

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

      inputField.setAttribute("id", formForInput.id + "::fileInput");
      inputField.removeAttribute("tabindex");
      formForInput.appendChild(inputField);

      var inputId = document.createElement("input");
      inputId.setAttribute("type", "text");
      inputId.setAttribute("name", "FILE_ID");
      inputId.setAttribute("value", fileUpload._ID + inputField._idInputAndDiv);
      formForInput.appendChild(inputId);

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
          addButton.className = addButtonOnFocusClass;
        }
        addButton._inFocus = true;
      });
      O$.addEventHandler(input,"blur",function(){
        if (!inputInAddBtn.disabled){
          addButton.className = addButtonClass;
          addButton._inFocus = false;
        }
      });
      input.setAttribute("name", inputFieldName);
      input._idInputAndDiv = id;
      O$.addEventHandler(input, "change", function() {
        shouldInvokeFocusEvHandler = false;
        var isAdded = addFileInputAfter(input);
        if (isAdded){
          setTimeout(function () {
            if (O$.isChrome())
              shouldInvokeFocusEvHandler = true;
            setFocusOnComponent();
            if (O$.isExplorer()) {
              shouldInvokeFocusEvHandler = false;
              ignoreBlurForIE = true;
            }
          }, 1);
        }else{
          if (O$.isChrome()) {
            shouldInvokeFocusEvHandler = true;
            setFocusOnComponent();
          }
        }
      });
      if (O$.isChrome()) { //chrome calls blur when file dialog popup
        O$.addEventHandler(input, "mousedown", function() {
          input.focus();
        });
      }
      return input;
    }

    function setStylesForAddButton(addButton){
      addButton.className = addButtonClass;
      O$.addEventHandler(addButton,"mouseover",function(){
        if (!inputInAddBtn.disabled) {
          addButton.className = addButtonOnMouseOverClass;
        }
      });
      O$.addEventHandler(addButton, "mouseout", function() {
        if (!inputInAddBtn.disabled) {
          if (addButton._inFocus) {
            addButton.className = addButtonOnFocusClass;
          } else {
            addButton.className = addButtonClass;
          }
        }
      });

      O$.addEventHandler(addButton,"mousedown",function(){
        if (!inputInAddBtn.disabled){
          addButton.className = addButtonOnMouseDownClass;
        }
        addButton._afterMouseDown = true;
      });
      O$.addEventHandler(window,"mouseup",function(){
        if (!inputInAddBtn.disabled){

          if (addButton._inFocus){
            addButton.className = addButtonOnFocusClass;
          }else{
            addButton.className = addButtonClass;
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
         if (onFocus != null){ // onFocus  handler
            onFocus();
         }
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
      if (O$.isChrome() && !ignoreBlurForChrome){
        ignoreBlurForChrome = true;
        return;
      }
      if (O$.isChrome() && !shouldInvokeFocusNonModifiable){
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
            if (onBlur != null) //  onBlur handler
              onBlur();
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
      if (onBlur != null) //  onBlur handler
        onBlur();
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

    function uploadButtonClickHandler() {
      function callProgressRequest(fileInput) {
        setTimeout(function() {
          progressRequest(fileInput);
        }, 500);
      }
      inputInAddBtn.disabled = true;
      addButton.className = addButtonDisabledClass;
      if (fileUpload._multiUpload){
        uploadButton.style.visibility = "hidden";
        clearAllButton.style.visibility = "hidden";    // to prevent changing of file list when uploading in progress
      }else{
        clearAllButton.style.display = "none";
      }
      fileUpload._listOfids = [];
      for (var inputsIndex = 0; inputsIndex < inputsStorage.childNodes.length; inputsIndex++) {
        var file = [];
        var form = inputsStorage.childNodes[inputsIndex].firstChild;
        file.push(form.childNodes[1].value);//id
        file.push(encodeURI(getFileName(form.childNodes[0].value)));//filename
        file.push("STARTED_UPLOAD");//status
        fileUpload._listOfids.push(file);
      }
      for (var inputsIndex = 0; inputsIndex < inputsStorage.childNodes.length; inputsIndex++) {
        var inputDiv = inputsStorage.childNodes[inputsIndex];
        var form = inputDiv.childNodes[0];
        var iframe = form.childNodes[2];
        var fileInput = form.childNodes[0];

        O$(allInfos.id + fileInput._idInputAndDiv).childNodes[3].firstChild.style.visibility = "hidden";
        form.target = iframe.name;
        form.submit();
        form.target = "_self";
        callProgressRequest(fileInput);
      }
      eval(onUploadStartEventHandler);
      function progressRequest(inputForFile) {
        O$.requestComponentPortions(fileUpload.id, ["nothing"],
                JSON.stringify({progressRequest: "true", fileName : encodeURI(getFileName(inputForFile.value))}),
                function(fileUpload, portionName, portionHTML, portionScripts, portionData) {
                  var infoDiv = O$(allInfos.id + inputForFile._idInputAndDiv);
                  if (portionData['status'] == "error") {//todo:description add what kind of error
                    infoDiv._status = O$.statusEnum.ERROR;
                    inputsStorage.removeChild(inputForFile.parentNode.parentNode);
                    infoDiv.childNodes[2].innerHTML = statusLabelErrorSize;
                    var id = inputForFile.nextSibling.value;
                    for (var k = 0; k < fileUpload._listOfids.length; k++) {
                      if (id == fileUpload._listOfids[k][0]) {
                        fileUpload._listOfids[k][2] = "ERROR";
                        break;
                      }
                    }
                    setClearBtnAndEventHandler(infoDiv);
                    prepareUIWhenAllRequestsFinished(true);
                  } else {
                    var percents = portionData['progressInPercent'];
                    if (percents != 100) {
                      if (infoDiv._status != O$.statusEnum.IN_PROGRESS) {
                        infoDiv._status = O$.statusEnum.IN_PROGRESS; //temporary
                        infoDiv.childNodes[2].innerHTML = statusLabelInProgress;
                        infoDiv.childNodes[3].removeChild(infoDiv.childNodes[3].firstChild);
                        var stopFileDiv = stopFacet.cloneNode(true);
                        stopFileDiv.setAttribute("id", stopFacet.id + inputForFile._idInputAndDiv);
                        infoDiv.childNodes[3].appendChild(stopFileDiv);   //todo with button event handler
                        O$.addEventHandler(stopFileDiv, "focus", focusHandler);
                        O$.addEventHandler(stopFileDiv, "blur", blurHandler);
                        O$.addEventHandler(stopFileDiv, "click", function(){
                          var iframe = inputForFile.nextSibling;
                          iframe.src = "";
                          inputForFile._wantToInterrupt = true;
                        });
                      }
                      if (inputForFile._wantToInterrupt) {
                        if (infoDiv.childNodes[1].firstChild.getValue() == percents) {
                          inputForFile._isInterrupted = true;
                          infoDiv._status = O$.statusEnum.ERROR;
                          inputsStorage.removeChild(inputForFile.parentNode.parentNode);
                          infoDiv.childNodes[2].innerHTML = statusStoppedText;
                          var id = inputForFile.nextSibling.value;
                          for (var k = 0; k < fileUpload._listOfids.length; k++) {
                            if (id == fileUpload._listOfids[k][0]) {
                              fileUpload._listOfids[k][2] = "STOPPED";
                              break;
                            }
                          }
                          setClearBtnAndEventHandler(infoDiv);
                          prepareUIWhenAllRequestsFinished(true);
                        }
                      }
                      if (!inputForFile._isInterrupted){
                        infoDiv.childNodes[1].firstChild.setValue(percents);
                        setTimeout(function() {
                          progressRequest(inputForFile);
                        }, 500);
                      }
                    } else {// when file already uploaded
                      infoDiv.childNodes[1].firstChild.setValue(percents);
                      fileUpload._lengthUploadedFiles++;
                      /*removing input*/
                      inputsStorage.removeChild(inputForFile.parentNode.parentNode); // delete divForFileInput
                      // infoDiv updating
                      infoDiv._status = O$.statusEnum.UPLOADED;
                      infoDiv.childNodes[2].innerHTML = statusLabelUploaded;
                      var id = inputForFile.nextSibling.value;
                      for (var k = 0; k < fileUpload._listOfids.length; k++) {
                        if (id == fileUpload._listOfids[k][0]) {
                          fileUpload._listOfids[k][2] = "UPLOADED";
                          break;
                        }
                      }
                      setClearBtnAndEventHandler(infoDiv);
                      prepareUIWhenAllRequestsFinished(true);
                    }
                  }

                  function setClearBtnAndEventHandler(infoDiv) {
                    infoDiv.childNodes[3].removeChild(infoDiv.childNodes[3].firstChild);
                    var clearFileDiv = clearFacet.cloneNode(true);
                    clearFileDiv.setAttribute("id", clearFacet.id + inputForFile._idInputAndDiv);
                    infoDiv.childNodes[3].appendChild(clearFileDiv);
                    O$.addEventHandler(clearFileDiv, "focus", focusHandler);
                    O$.addEventHandler(clearFileDiv, "blur", blurHandler);
                    O$.addEventHandler(clearFileDiv, "click", function() {
                      allInfos.removeChild(infoDiv);
                      if (allInfos.childNodes.length == 0) {
                        clearAllButton.style.display = "none";
                        clearAllButton.disabled = true;
                      }
                      shouldInvokeFocusNonModifiable = false;
                      setFocusOnComponent();
                      shouldInvokeFocusNonModifiable = true;
                    });
                  }

                  function sendCheckRequest() {
                    O$.requestComponentPortions(fileUpload.id, ["nothing"],
                            JSON.stringify({listOfFilesRequest: "true", idOfFiles : fileUpload._listOfids}),
                            function(fileUpload, portionName, portionHTML, portionScripts, portionData) {
                              if (portionData['allUploaded'] == "true") {
                                fileUpload._listOfids = [];
                                eval(onUploadEndEventHandler);
                              }else{
                                setTimeout(sendCheckRequest, 500);
                              }
                            }, null, true);
                  }
                  function prepareUIWhenAllRequestsFinished(shouldCheckRequest) {
                    if (inputsStorage.childNodes.length == 0) { // if all files are  already uploaded
                      if (shouldCheckRequest)
                        setTimeout(sendCheckRequest, 500);

                      fileUpload._numberOfFilesToUpload = 0;
                      if (fileUpload._multiUpload){
                        clearAllButton.style.visibility = "visible";
                        uploadButton.style.visibility = "visible";
                      }
                      inputInAddBtn.disabled = false;
                      addButton.className = addButtonClass;
                      initHeaderButtons();
                      //set focus on addButton
                      shouldInvokeFocusEvHandler = false;
                      setFocusOnComponent();
                      shouldInvokeFocusEvHandler = true;
                    }
                  }
                },
                null,
                true
        );
      }
    }
  }

};

O$.statusEnum = {
  NEW_ONE : {value:0},
  IN_PROGRESS : {value:1},
  UPLOADED : {value:2},
  ERROR : {value:3}
};

