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

O$.ProgressBar = {
  _init: function(componentId, value, labelAlignment, labelFormat, uploadedProgressImgUrl, notUploadedProgressImgUrl) {
    var progressBar = O$.initComponent(componentId, null);
    O$.extend(progressBar, {
      _progressValue : 0,
      _uploadedDiv : progressBar.childNodes[0],
      _notUploadedDiv : progressBar.childNodes[1],
      _labelDiv : progressBar.childNodes[2],
      _uploadedProgressImgUrl : uploadedProgressImgUrl,
      _notUploadedProgressImgUrl : notUploadedProgressImgUrl,
      _labelAlignment : labelAlignment,
      _labelFormat: labelFormat,
      getValue : function() {
        return progressBar._progressValue;
      },
      setValue : function(progressValue) {
        if (progressValue != null &&
                progressValue <= 100 && progressValue >= 0) {
          progressBar._progressValue = progressValue;
          var labelShouldDisplay = !(O$.getElementStyle(progressBar._labelDiv,"display") == "none");
          if (O$.isExplorer6() || O$.isExplorer7() || (O$.isExplorer() && O$.isQuirksMode())) {
              progressBar._labelDiv.style.display = "none";
          }
          progressBar._labelDiv.innerHTML = progressBar._labelFormat.replace("{value}", progressValue);

          var val = progressValue / 100;//between 0 and 1
          progressBar._uploadedDiv.style.width = progressBar.clientWidth * val + "px";
          progressBar._notUploadedDiv.style.width = progressBar.clientWidth * (1 - val) + "px";

          if (O$.isExplorer6() || O$.isExplorer7() || (O$.isExplorer() && O$.isQuirksMode())) {
            // weird bug from IE - without this row of code, label will be displayed not inside progressBar
            if (labelShouldDisplay) {
              progressBar._labelDiv.style.marginLeft = progressBar._labelDiv.style.marginLeft;
              progressBar._labelDiv.style.display = "inline";
            }
          }
        }
      },
      getUploadedProgressImgUrl: function () {
        return progressBar._uploadedProgressImgUrl;
      },
      getNotUploadedProgressImgUrl: function () {
        return progressBar._notUploadedProgressImgUrl;
      },
      getLabelFormat:function(){
        return progressBar._labelFormat;
      },
      getLabelAlignment:function() {
        return progressBar._labelAlignment;
      }
    });
    new function setHeightAndWidthForProgressEls(){
      /*IE 8 doesn't see if we assign height to some percents %*/
      if (O$.isExplorer8() || document.documentMode == 8){
        progressBar._uploadedDiv.style.height  = O$.getElementClientRectangle(progressBar).height + "px";
        progressBar._notUploadedDiv.style.height = O$.getElementClientRectangle(progressBar).height + "px";
      }

    }();

    progressBar.setValue(value);
    if (labelAlignment == "center"){
        progressBar._labelDiv.style.marginLeft = progressBar.clientWidth / 2 - O$.getElementSize(progressBar._labelDiv).width / 2 + "px";
    }
    progressBar._uploadedDiv.style.backgroundImage = "url('" + progressBar._uploadedProgressImgUrl + "')";
    if (progressBar._notUploadedProgressImgUrl != null && progressBar._notUploadedProgressImgUrl != "") {
      progressBar._notUploadedDiv.style.backgroundImage = "url('" + progressBar._notUploadedProgressImgUrl + "')";
    }
  },
  initCopyOf: function (progressBar, copyOfProgressBar) {
    this._init(copyOfProgressBar.id,
            progressBar.getValue(),
            progressBar.getLabelAlignment(),
            progressBar.getLabelFormat(),
            progressBar.getUploadedProgressImgUrl(),
            progressBar.getNotUploadedProgressImgUrl());
  }
};