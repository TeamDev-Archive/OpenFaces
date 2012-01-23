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
                  onfileuploadstartHandler, onfileuploadsuccessfulHandler, onfileuploadinprogressHandler,
                  onfileuploadstoppedHandler, onfileuploadfailedHandler,
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
      _events:{}
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

    initHeaderButtons();
    setFocusBlurBehaviour();
    setUploadButtonBehaviour();

    setBehaviorForDragAndDropArea();
    function setBehaviorForDragAndDropArea(){
      var area = O$(componentId + "::footer::dragArea");
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
            area.style.display = "block";
            area._isVisible = true;
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
    O$.addEventHandler(clearAllButton, "click", function () {
      shouldInvokeFocusNonModifiable = false;
      var infosLength = allInfos.childNodes.length;
      couldCallChangeEvent = false;
      var isCallingChangeEventNeeded = false;
      for (var index = 0; index < infosLength; index++){
        if (allInfos.childNodes[0].childNodes[3].firstChild.deleteFileInputFunction){
          isCallingChangeEventNeeded = true;
        }
        allInfos.childNodes[0].childNodes[3].firstChild._clickHandler();
      }
      couldCallChangeEvent = true;
      if (isCallingChangeEventNeeded){
        fileUpload._events._fireChangeEvent();
      }

      clearAllButton.style.display = "none";
        //setFocusOnComponent();
      shouldInvokeFocusNonModifiable = true;
    });

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
          if (infos[k]._status != O$.statusEnum.ERROR) {
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
      infoWindow._status = O$.statusEnum.NEW_ONE;

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
      if (isFileNameNotApplied(file.name) || inputInAddBtn.disabled || isDirectory(file)) {
        return false;
      }
      file._uniqueId = fileUpload._ID + idOfInfoAndInputDiv;
      file._fakeInput = componentId + "::inputs::input" + idOfInfoAndInputDiv + "::form::fileInput";
      file._infoId = idOfInfoAndInputDiv;
      fileUpload._filesHTML5.push(file);
      var fileInfo = createInfoWindowForFile(idOfInfoAndInputDiv, file.name);
      var cancelFileDiv = fileInfo.childNodes[3].firstChild;
      setSpecificClickHandlerForCancelBtn(cancelFileDiv, fileInfo);

      function setSpecificClickHandlerForCancelBtn(cancelFileDiv, fileInfo) {
        cancelFileDiv.deleteFileInputFunction = function () {
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
      O$.addEventHandler(uploadButton, "click",uploadButtonClickHandler);

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
      var inputId = document.createElement("input");
      inputId.setAttribute("type", "text");
      inputId.setAttribute("name", "FILE_ID");
      inputId.setAttribute("value", fileUpload._ID + inputField._idInputAndDiv);
      formForInput.appendChild(inputId);

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

    function uploadButtonClickHandler() {
      function callProgressRequest(fileInput) {
        setTimeout(function() {
          progressRequest(fileInput);
        }, 500);
      }
      function callFileProgressForHTML5Request(file,request) {
        setTimeout(function() {
          progressHTMl5Request(file, request);
        }, 500);
      }
      inputInAddBtn.disabled = true;
      addButtonTitleInput.className = addButtonDisabledClass;
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
        file.push(form.childNodes[0].value);//id
        file.push(form.childNodes[1].name);//name of file input
        file.push(encodeURI(getFileName(form.childNodes[1].value)));//filename
        file.push("STARTED_UPLOAD");//status
        fileUpload._listOfids.push(file);
      }
      /*specific behavior for uploading files with drag and drop*/
      for (var html5FilesIndex = 0; html5FilesIndex < fileUpload._filesHTML5.length; html5FilesIndex++) {
        var html5File = fileUpload._filesHTML5[html5FilesIndex];
        var file = [];
        file.push(html5File._uniqueId);//id
        file.push(html5File._fakeInput);//name of file input
        file.push(encodeURI(getFileName(html5File.name)));//filename
        file.push("STARTED_UPLOAD");//status
        fileUpload._listOfids.push(file);
      }
      fileUpload._events._fireUploadStartEvent();
      for (var inputsIndex = 0; inputsIndex < inputsStorage.childNodes.length; inputsIndex++) {
        var inputDiv = inputsStorage.childNodes[inputsIndex];
        var form = inputDiv.childNodes[0];
        var iframe = form.childNodes[2];
        var fileInput = form.childNodes[1];

        O$(allInfos.id + fileInput._idInputAndDiv).childNodes[3].firstChild.style.visibility = "hidden";
        form.target = iframe.name;
        form.submit();
        form.target = "_self";
        callProgressRequest(fileInput);
        fileUpload._events._fireFileUploadStartEvent();
      }
      function sendFileRequest(file){
        var xhr = new XMLHttpRequest();
        xhr.open('POST', document.URL, true);
        var data = new FormData();
        data.append("FILE_ID", file._uniqueId);
        data.append(file._fakeInput, file);
        xhr.send(data);
        return xhr;
      }
      for (var html5FilesIndex = 0; html5FilesIndex < fileUpload._filesHTML5.length; html5FilesIndex++) {
        var html5File = fileUpload._filesHTML5[html5FilesIndex];

        O$(allInfos.id + html5File._infoId).childNodes[3].firstChild.style.visibility = "hidden";

        var request = sendFileRequest(html5File);
        callFileProgressForHTML5Request(html5File, request);
        fileUpload._events._fireFileUploadStartEvent();
      }
      function progressRequest(inputForFile) {
        O$.requestComponentPortions(fileUpload.id, ["nothing"],
                JSON.stringify({progressRequest: "true", fieldName : inputForFile.name}),
                function(fileUpload, portionName, portionHTML, portionScripts, portionData) {
                  var infoDiv = O$(allInfos.id + inputForFile._idInputAndDiv);
                  if (portionData['status'] == "error") {//todo:description add what kind of error
                    infoDiv._status = O$.statusEnum.ERROR;
                    inputsStorage.removeChild(inputForFile.parentNode.parentNode);
                    if (portionData['isFileSizeExceed'] == "true"){
                      infoDiv.childNodes[2].innerHTML = statusLabelErrorSize;
                      var id = inputForFile.previousSibling.value;
                      setStatusforFileWithId(id, "SIZE_LIMIT_EXCEEDED");
                    }else{
                      infoDiv.childNodes[2].innerHTML = statusLabelUnexpectedError;
                      var id = inputForFile.previousSibling.value;
                      setStatusforFileWithId(id, "ERROR");
                    }
                    fileUpload._events._fireFileUploadFailedEvent();
                    setClearBtnAndEventHandler(infoDiv, inputForFile._idInputAndDiv);
                    prepareUIWhenAllRequestsFinished(true);
                  } else {
                    var percents = portionData['progressInPercent'];
                    if (percents != 100) {
                      if (infoDiv._status != O$.statusEnum.IN_PROGRESS) {
                        infoDiv._status = O$.statusEnum.IN_PROGRESS;
                        infoDiv.childNodes[2].innerHTML = statusLabelInProgress;
                        infoDiv.childNodes[3].removeChild(infoDiv.childNodes[3].firstChild);
                        var stopFileDiv = stopFacet.cloneNode(true);
                        stopFileDiv.setAttribute("id", stopFacet.id + inputForFile._idInputAndDiv);
                        infoDiv.childNodes[3].appendChild(stopFileDiv);
                        O$.addEventHandler(stopFileDiv, "focus", focusHandler);
                        O$.addEventHandler(stopFileDiv, "blur", blurHandler);
                        stopFileDiv._clickHandler = function(){
                          stopFileDiv.style.visibility = "hidden";
                          infoDiv.childNodes[2].innerHTML = statusStoppingText;
                          var iframe = inputForFile.nextSibling;
                          iframe.src = "";
                          inputForFile._wantToInterrupt = true;
                        };
                        O$.addEventHandler(stopFileDiv, "click", stopFileDiv._clickHandler);
                        if (O$.isChrome() || O$.isSafari()) {
                          O$.addEventHandler(stopFileDiv, "mousedown", function() {
                            shouldInvokeFocusNonModifiable = false;
                          });
                          O$.addEventHandler(stopFileDiv, "mouseup", function() {
                            shouldInvokeFocusNonModifiable = true;
                          });
                          O$.addEventHandler(stopFileDiv, "keyup", function() {
                            shouldInvokeFocusEvHandler = false;
                          });
                        }
                      }
                      if (inputForFile._wantToInterrupt) {
                        if (infoDiv.childNodes[1].firstChild.getValue() == percents) {
                          inputForFile._isInterrupted = true;
                          infoDiv._status = O$.statusEnum.ERROR;
                          inputsStorage.removeChild(inputForFile.parentNode.parentNode);
                          infoDiv.childNodes[2].innerHTML = statusStoppedText;
                          var id = inputForFile.previousSibling.value;
                          setStatusforFileWithId(id, "STOPPED");
                          fileUpload._events._fireFileUploadStoppedEvent();
                          setClearBtnAndEventHandler(infoDiv, inputForFile._idInputAndDiv);
                          prepareUIWhenAllRequestsFinished(true);
                        }
                      }
                      if (!inputForFile._isInterrupted){
                        infoDiv.childNodes[1].firstChild.setValue(percents);
                        fileUpload._events._fireFileUploadInProgressEvent();
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
                      var id = inputForFile.previousSibling.value;
                      setStatusforFileWithId(id, "UPLOADED");
                      fileUpload._events._fireFileUploadSuccessfulEvent();
                      setClearBtnAndEventHandler(infoDiv, inputForFile._idInputAndDiv);
                      prepareUIWhenAllRequestsFinished(true);
                    }
                  }

                },
                null,
                true
        );
      }
      function progressHTMl5Request(file, request){
        O$.requestComponentPortions(fileUpload.id, ["nothing"],
                JSON.stringify({progressRequest: "true", fieldName : file._fakeInput}),
                function(fileUpload, portionName, portionHTML, portionScripts, portionData) {
                  var infoDiv = O$(allInfos.id + file._infoId);
                  if (portionData['status'] == "error") {//todo:description add what kind of error
                    infoDiv._status = O$.statusEnum.ERROR;
                    removeHTML5File(file);
                    if (portionData['isFileSizeExceed'] == "true"){
                      infoDiv.childNodes[2].innerHTML = statusLabelErrorSize;
                      setStatusforFileWithId(file._uniqueId, "SIZE_LIMIT_EXCEEDED");
                    }else{
                      infoDiv.childNodes[2].innerHTML = statusLabelUnexpectedError;
                      setStatusforFileWithId(file._uniqueId, "ERROR");
                    }
                    fileUpload._events._fireFileUploadFailedEvent();
                    setClearBtnAndEventHandler(infoDiv, file._infoId);
                    prepareUIWhenAllRequestsFinished(true);
                  } else {
                    var percents = portionData['progressInPercent'];
                    if (percents != 100) {
                      if (infoDiv._status != O$.statusEnum.IN_PROGRESS) {
                        infoDiv._status = O$.statusEnum.IN_PROGRESS;
                        infoDiv.childNodes[2].innerHTML = statusLabelInProgress;
                        infoDiv.childNodes[3].removeChild(infoDiv.childNodes[3].firstChild);
                        var stopFileDiv = stopFacet.cloneNode(true);
                        stopFileDiv.setAttribute("id", stopFacet.id + file._infoId);
                        infoDiv.childNodes[3].appendChild(stopFileDiv);
                        O$.addEventHandler(stopFileDiv, "focus", focusHandler);
                        O$.addEventHandler(stopFileDiv, "blur", blurHandler);
                        stopFileDiv._clickHandler = function(){
                          stopFileDiv.style.visibility = "hidden";
                          infoDiv.childNodes[2].innerHTML = statusStoppingText;
                          request.abort();
                          file._wantToInterrupt = true;
                        };
                        O$.addEventHandler(stopFileDiv, "click", stopFileDiv._clickHandler);
                        if (O$.isChrome() || O$.isSafari()) {
                          O$.addEventHandler(stopFileDiv, "mousedown", function() {
                            shouldInvokeFocusNonModifiable = false;
                          });
                          O$.addEventHandler(stopFileDiv, "mouseup", function() {
                            shouldInvokeFocusNonModifiable = true;
                          });
                          O$.addEventHandler(stopFileDiv, "keyup", function() {
                            shouldInvokeFocusEvHandler = false;
                          });
                        }
                      }
                      if (file._wantToInterrupt) {
                        if (infoDiv.childNodes[1].firstChild.getValue() == percents) {
                          file._isInterrupted = true;
                          infoDiv._status = O$.statusEnum.ERROR;
                          removeHTML5File(file);
                          infoDiv.childNodes[2].innerHTML = statusStoppedText;
                          setStatusforFileWithId(file._uniqueId, "STOPPED");
                          fileUpload._events._fireFileUploadStoppedEvent();
                          setClearBtnAndEventHandler(infoDiv, file._infoId);
                          prepareUIWhenAllRequestsFinished(true);
                        }
                      }
                      if (!file._isInterrupted){
                        infoDiv.childNodes[1].firstChild.setValue(percents);
                        fileUpload._events._fireFileUploadInProgressEvent();
                        setTimeout(function() {
                          progressHTMl5Request(file,request);
                        }, 500);
                      }
                    } else {// when file already uploaded
                      infoDiv.childNodes[1].firstChild.setValue(percents);
                      fileUpload._lengthUploadedFiles++;
                      /*removing file*/
                      removeHTML5File(file);
                      // infoDiv updating
                      infoDiv._status = O$.statusEnum.UPLOADED;
                      infoDiv.childNodes[2].innerHTML = statusLabelUploaded;
                      setStatusforFileWithId(file._uniqueId, "UPLOADED");
                      fileUpload._events._fireFileUploadSuccessfulEvent();
                      setClearBtnAndEventHandler(infoDiv, file._infoId);
                      prepareUIWhenAllRequestsFinished(true);
                    }
                  }

                },
                null,
                true
        );
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
                  JSON.stringify({listOfFilesRequest: "true", idOfFiles : fileUpload._listOfids}),
                  function(fileUpload, portionName, portionHTML, portionScripts, portionData) {
                    if (portionData['allUploaded'] == "true") {
                      fileUpload._listOfids = [];
                      fileUpload._events._fireUploadEndEvent();
                    }else{
                      setTimeout(sendCheckRequest, 500);
                    }
                  }, null, true);
        }
        if (inputsStorage.childNodes.length == 0 && fileUpload._filesHTML5.length ==0) { // if all files are  already uploaded
          if (shouldCheckRequest)
            setTimeout(sendCheckRequest, 500);

          fileUpload._numberOfFilesToUpload = 0;
          if (fileUpload._multiUpload){
            clearAllButton.style.visibility = "visible";
            uploadButton.style.visibility = "visible";
          }
          inputInAddBtn.disabled = false;
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
        clearFileDiv._clickHandler = function() {
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
          O$.addEventHandler(clearFileDiv, "mousedown", function() {
            shouldInvokeFocusNonModifiable = false;
          });
          O$.addEventHandler(clearFileDiv, "mouseup", function() {
            shouldInvokeFocusNonModifiable = true;
          });
          O$.addEventHandler(clearFileDiv, "keyup", function() {
            shouldInvokeFocusEvHandler = false;
          });
        }
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
          uploadButtonClickHandler();
        } else {
          uploadButton.style.visibility = "visible";
        }
      }
    }
    function setAllEvents(){
      function createEventHandler(userHandler, eventName){
        return function(){
          if (userHandler) {
            var event = O$.createEvent(eventName);
            userHandler(event);
          }
        }
      }

      fileUpload._events._fireChangeEvent = createEventHandler(onchangeHandler, "change");
      fileUpload._events._fireUploadStartEvent = createEventHandler(onuploadstartHandler, "onuploadstart");
      fileUpload._events._fireUploadEndEvent = createEventHandler(onuploadendHandler, "onuploadend");
      fileUpload._events._fireFileUploadStartEvent = createEventHandler(onfileuploadstartHandler, "onfileuploadstart");
      fileUpload._events._fireFileUploadSuccessfulEvent = createEventHandler(onfileuploadsuccessfulHandler, "onfileuploadsuccessful");
      fileUpload._events._fireFileUploadInProgressEvent = createEventHandler(onfileuploadinprogressHandler, "onfileuploadinprogress");
      fileUpload._events._fireFileUploadStoppedEvent = createEventHandler(onfileuploadstoppedHandler, "onfileuploadstopped");
      fileUpload._events._fireFileUploadFailedEvent = createEventHandler(onfileuploadfailedHandler, "onfileuploadfailed");

      //processing onfocus/onblur event
      fileUpload._events._fireBlurEvent = createEventHandler(fileUpload.onblur, "onblur");
      fileUpload.removeAttribute("onblur");
      fileUpload.onblur = null;

      fileUpload._events._fireFocusEvent = createEventHandler(fileUpload.onfocus, "onfocus");
      fileUpload.removeAttribute("onfocus");
      fileUpload.onfocus = null;

    }
  }

};

O$.statusEnum = {
  NEW_ONE : {value:0},
  IN_PROGRESS : {value:1},
  UPLOADED : {value:2},
  ERROR : {value:3}
};

