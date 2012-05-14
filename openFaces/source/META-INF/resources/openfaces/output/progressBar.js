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

O$.ProgressBar = {
  _init: function(componentId, value, labelAlignment, labelFormat, defaultProgressImgUrl, defProgressClassName) {
    var progressBar = O$.initComponent(componentId, null);
    O$.extend(progressBar, {
      _progressValue : 0,
      _uploadedDiv : progressBar.childNodes[0],
      _notUploadedDiv : progressBar.childNodes[1],
      _labelDiv : progressBar.childNodes[2],
      _defaultProgressImgUrl : defaultProgressImgUrl,
      _labelAlignment : labelAlignment,
      _labelFormat : labelFormat,
      _previousValue : 0,
      _isBusy : false,
      _queue : [],
      _widthOfProgress: 0,
      _isEnabledQuirksMode:false,
      _defProgressClassName: defProgressClassName,
      _setWidthForAllComponent:function(width) {
        progressBar._widthOfProgress = width;
      },
      _enableQuirksMode: function() {
        progressBar._isEnabledQuirksMode = true;
      },
      _getWidthOfProgress:function() {
        if (progressBar._widthOfProgress != 0) {
          return progressBar._widthOfProgress;
        }
        return O$.getElementSize(progressBar).width;
      },
      _getHeightOfProgress: function() {
        if (O$.getElementClientRectangle(progressBar).height != 0) {
          return O$.getElementClientRectangle(progressBar).height;
        }
        if (progressBar.clientHeight>0) {
          return progressBar.clientHeight;
        }
        if ((O$.getElementStyle(progressBar,"height").replace("px", "") * 1) > 0 ) {
          return O$.getElementStyle(progressBar,"height").replace("px", "") * 1;
        }
        return progressBar.style.height.replace("px", "") * 1;
      },
      getValue : function() {
        return progressBar._progressValue;
      },
      setValue : function(progressValue, endHandler) {
        if (progressValue != null &&
                progressValue <= 100 && progressValue >= 0) {
          progressBar._progressValue = progressValue;
          if (progressBar._widthOfProgress == 0) {
            progressBar._widthOfProgress =  O$.getElementSize(progressBar).width;
          }
          var labelShouldDisplay = !(O$.getElementStyle(progressBar._labelDiv,"display") == "none");
          if (O$.isExplorer6() || O$.isExplorer7() || (O$.isExplorer() && O$.isQuirksMode())) {
            progressBar._labelDiv.style.display = "none";
          }
          progressBar._setLabelValue(progressValue);

          var val = progressValue / 100;//between 0 and 1
          progressBar._smoothChangeValueTo(val, endHandler);

          if (O$.isExplorer6() || O$.isExplorer7() || (O$.isExplorer() && O$.isQuirksMode())) {
            // weird bug from IE - without this row of code, label will be displayed not inside progressBar
            if (labelShouldDisplay) {
              progressBar._labelDiv.style.marginLeft = progressBar._labelDiv.style.marginLeft;
              progressBar._labelDiv.style.display = "inline";
            }
          }
        }
      },
      _getDefaultProgressImgUrl: function () {
        return progressBar._defaultProgressImgUrl;
      },
      _getLabelFormat:function() {
        return progressBar._labelFormat;
      },
      _getLabelAlignment:function() {
        return progressBar._labelAlignment;
      },
      _getDefaultClassName:function() {
        return progressBar._defProgressClassName;
      },
      _setWidthForProgress:function (uploadedWidth, notUploadedWidth) {

        if (progressBar._isEnabledQuirksMode && O$.isExplorer() && (O$.isQuirksMode() ||(O$.isExplorer7() || O$.isExplorer6()))) {
          var tempWidth = progressBar._getWidthOfProgress();
          if (tempWidth == uploadedWidth) {
            uploadedWidth--;
          }else if (tempWidth == notUploadedWidth) {
            notUploadedWidth--;
          }
          progressBar._uploadedDiv.style.width = uploadedWidth + "px";
          progressBar._notUploadedDiv.style.width = notUploadedWidth + "px";
        } else {
          progressBar._uploadedDiv.style.width = uploadedWidth + "px";
          progressBar._notUploadedDiv.style.width = notUploadedWidth + "px";
        }
        O$.setElementWidth(progressBar, progressBar._getWidthOfProgress());

      },
      _setLabelValue:function (value) {
        progressBar._labelDiv.innerHTML = progressBar._labelFormat.replace("{value}", value);
      },
      _smoothChangeValueTo:function (val, endHandler) {
        function resolveQueue() {
          var valInQueue = progressBar._queue.shift();
          if (valInQueue != null) {
            progressBar._smoothChangeValueTo(valInQueue.value, valInQueue.endHandler);
          } else {
            if (endHandler) {
              endHandler();
            }
          }
        }
        if (progressBar._isBusy) {
          progressBar._queue.push({value:val, endHandler:endHandler});
        } else {
          progressBar._isBusy = true;
          if (progressBar._previousValue < val) { //smooth part
            var TIME_TAKES = 340;
            var INTERVAL = 20;
            var TIMES_TO_CHANGE_PROGRESS = TIME_TAKES / INTERVAL;

            var goalUploadedWidth = progressBar._getWidthOfProgress() * val;
            var nowUploadedWidth = progressBar._uploadedDiv.clientWidth;

            var addByTime = (goalUploadedWidth - nowUploadedWidth) / TIMES_TO_CHANGE_PROGRESS;
            if (addByTime < 1) {
              addByTime = 1;
            }
            function changeProgress() {
              if (progressBar._uploadedDiv.clientWidth + addByTime >= goalUploadedWidth) {
                progressBar._setWidthForProgress(progressBar._getWidthOfProgress()  * val, progressBar._getWidthOfProgress()  * (1 - val));
                progressBar._setLabelValue(Math.round(val * 100));
                progressBar._isBusy = false;
                resolveQueue();
              } else {
                var uploadedWidth = progressBar._uploadedDiv.clientWidth + addByTime;
                var percentsNow = (uploadedWidth ) / progressBar._getWidthOfProgress() ;
                progressBar._setWidthForProgress(uploadedWidth,
                        (progressBar._notUploadedDiv.clientWidth - addByTime < 0) ? 0
                                : progressBar._notUploadedDiv.clientWidth - addByTime);
                progressBar._setLabelValue(Math.round(percentsNow * 100));
                setTimeout(changeProgress, INTERVAL);
              }
            }

            changeProgress();
          } else {
            progressBar._setWidthForProgress(progressBar._getWidthOfProgress()  * val, progressBar._getWidthOfProgress()  * (1 - val));
            progressBar._setLabelValue(Math.round(val * 100));
            progressBar._isBusy = false;
            resolveQueue();
          }
          progressBar._previousValue = val;
        }
      },
      _setDefBackgroundImage:function () {
        for (var i = 0; i < document.styleSheets.length; i++) {
          var rules = O$.isExplorer() ? document.styleSheets[i].rules : document.styleSheets[i].cssRules;
          for (var k = 0; k < rules.length; k++) {
            if (rules[k].selectorText == "." + defProgressClassName) {
              rules[k].style.backgroundImage = "url(" + progressBar._defaultProgressImgUrl + ")";
              return;
            }
          }
        }
      }
    });
    new function setHeightAndWidthForProgressEls() {
      /*IE 8 doesn't see if we assign height to some percents %*/
      if (O$.isExplorer8() || document.documentMode == 8) {
        progressBar._uploadedDiv.style.height  = progressBar._getHeightOfProgress() + "px";
        progressBar._notUploadedDiv.style.height = progressBar._getHeightOfProgress() + "px";
      }

    }();

    progressBar.setValue(value);
    if (labelAlignment == "center") {
      progressBar._labelDiv.style.marginLeft = progressBar._getWidthOfProgress()  / 2 - O$.getElementSize(progressBar._labelDiv).width / 2 + "px";
    }
    progressBar._setDefBackgroundImage();

    //progressBar._uploadedDiv.style.backgroundImage = "url('" + progressBar._defaultProgressImgUrl + "')";
  },
  initCopyOf: function (progressBar, copyOfProgressBar) {
    this._init(copyOfProgressBar.id,
            progressBar.getValue(),
            progressBar._getLabelAlignment(),
            progressBar._getLabelFormat(),
            progressBar._getDefaultProgressImgUrl(),
            progressBar._getDefaultClassName());
  }
};