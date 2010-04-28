/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2010, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
jQuery.correctId = function(elementId) {
  return '#' + elementId.replace(/(:|\.)/g, '\\$1');
};

O$.LevelIndicator = {
  _init: function(levelIndicatorId,
                  value,
                  segmentSize,
                  orientation,
                  fillDirection,
                  width,
                  height,
                  defaultWidth,
                  defaultHeight,
                  colors,
                  transitionLevels,
                  backgroundIntensity) {
    var levelIndicator = O$.initComponent(levelIndicatorId, null, {
      _indicator: O$(levelIndicatorId + "::indicator"),
      _label: O$(levelIndicatorId + "::label"),
      _indicatorSegment: O$(levelIndicatorId + "::indicatorSegment"),
      _segmentSize: segmentSize,
      _value: value,
      _orientation: orientation,
      _width: width,
      _height: height,
      _fillDirection: fillDirection,
      _colors: colors,
      _transitionLevels: transitionLevels,
      _backgroundIntensity: backgroundIntensity,
      _color_arr :[
        [0.3,'#00cc00','#339933'],
        [0.6,'#ffff00','#999933'],
        [0.75,'#ff6600','#664488'],
        [1,'#ff0000','#993333']
      ],
      _indicatorSize : 0,
      _lamp_l_margin : 0,
      _lamp_r_margin : 2,

      _setupIndicator: function() {
        var clientSpecifiedWidth = O$.getStyleClassProperty(levelIndicator.className, "width");
        var clientSpecifiedHeight = O$.getStyleClassProperty(levelIndicator.className, "height");

        if (clientSpecifiedWidth === undefined && clientSpecifiedHeight === undefined) {
          // This means that LevelIndicator component should use it's default state
          if (levelIndicator._orientation == "horizontal") {
            levelIndicator._width = defaultWidth;
            levelIndicator._height = defaultHeight;
            jQuery(levelIndicator).css('width', defaultWidth);
            jQuery(levelIndicator).css('height', defaultHeight);
          } else {
            if (O$.isExplorer() && O$.isQuirksMode()) {
              var indicatorPaddingTop = parseInt(jQuery(levelIndicator).css('padding-top'));
              var indicatorPaddingBottom = parseInt(jQuery(levelIndicator).css('padding-bottom'));
              defaultHeight = parseInt(defaultHeight) + parseInt(indicatorPaddingTop) + parseInt(indicatorPaddingBottom);
            }

            jQuery(levelIndicator).css('width', defaultHeight);
            jQuery(levelIndicator).css('height', defaultWidth);
            levelIndicator._width = defaultHeight;
            levelIndicator._height = defaultWidth;
          }
        }

        if (this._orientation == "horizontal") {
          var labelContainerWidth = (O$.isExplorer() && O$.isQuirksMode())
                  ? parseInt(levelIndicator._labelContainer.width()) + parseInt(levelIndicator._labelContainer.css('padding-left')) + parseInt(levelIndicator._labelContainer.css('padding-left'))
                  : parseInt(levelIndicator._labelContainer.width());
          levelIndicator._indicatorSize = levelIndicator._width - labelContainerWidth - parseInt(jQuery(levelIndicator).css('padding-right'));
        } else {
          var labelContainerHeight = (O$.isExplorer() && O$.isQuirksMode())
                  ? parseInt(levelIndicator._labelContainer.height()) + parseInt(levelIndicator._labelContainer.css('padding-top')) + parseInt(levelIndicator._labelContainer.css('padding-bottom'))
                  : parseInt(levelIndicator._labelContainer.height());
          levelIndicator._indicatorSize = levelIndicator._height - labelContainerHeight - parseInt(jQuery(levelIndicator).css('padding-bottom'));
        }

        levelIndicator._totalSegmentsCount = levelIndicator._calculateRequiredSegmentsCount(levelIndicator._indicatorSize);

        levelIndicator._appendIndicatorSegments();
        levelIndicator._adjustIndicatorSize();
        levelIndicator._setupLabel();
        levelIndicator._updateIndicator();
      },

      _updateIndicator: function() {
        var enabledLampsCount = Math.floor(levelIndicator._totalSegmentsCount * (levelIndicator._value));
        var disabledLampsCount = levelIndicator._totalSegmentsCount - enabledLampsCount;

        levelIndicator._updateSegmentsColors(enabledLampsCount, disabledLampsCount, levelIndicator._totalSegmentsCount);
      },

      _setupLabel: function() {
        if (levelIndicator._orientation == "horizontal") {
          levelIndicator._labelContainer.css('height', '100%');
          levelIndicator._labelContainer.css('line-height', levelIndicator._height + 'px');
        } else {
          levelIndicator._labelContainer.css('width', '100%');
        }

        levelIndicator._updateLabel();
      },

      _updateLabel: function() {
        var percentValueString = parseInt(levelIndicator._value * 100) + '%';
        levelIndicator._labelContainer.text(percentValueString);
      } ,

      _appendIndicatorSegments: function() {
        for (var lampsCounter = 0; lampsCounter < levelIndicator._totalSegmentsCount; lampsCounter++) {
          var lampItem = jQuery(document.createElement('div'));
          var orientationClass = (levelIndicator._orientation == "horizontal") ? "h" : "v";
          lampItem.addClass("o_levelIndicator_indicatorSegment");
          lampItem.addClass(orientationClass);
          lampItem.addClass("on");

          if (O$.isExplorer() && O$.isQuirksMode()) {
            lampItem.css('font-size', 0);
          }

          if (levelIndicator._orientation == "horizontal") {
            lampItem.css('width', levelIndicator._segmentSize + 'px');
          } else {
            lampItem.css('height', levelIndicator._segmentSize + 'px');
          }

          levelIndicator._indicatorContainer.append(lampItem);
        }
      },

      _adjustIndicatorSize: function() {
        if (levelIndicator._orientation == "horizontal") {
          jQuery(levelIndicator).css('width', levelIndicator._width);
          jQuery(levelIndicator).css('height', levelIndicator._height);
          levelIndicator._indicatorContainer.css('width', levelIndicator._indicatorSize);

          if (levelIndicator._height != 0) {
            var paddingTop = levelIndicator._indicatorContainer.css('padding-top');
            var progressContainerPaddingTop = (paddingTop) ? paddingTop.replace('px', '') : 0;
            var paddingBottom = levelIndicator._indicatorContainer.css('padding-bottom');
            var progressContainerPaddingBottom = (paddingBottom) ? paddingBottom.replace('px', '') : 0;

            var totalProgressHeight = levelIndicator._height - parseInt(progressContainerPaddingTop) - parseInt(progressContainerPaddingBottom);
            levelIndicator._indicatorContainer.css('height', totalProgressHeight);
          } else {
            levelIndicator._indicatorContainer.css('height', Math.round(levelIndicator._width * 0.1));
          }

        } else {
          jQuery(levelIndicator).css('width', levelIndicator._width);
          jQuery(levelIndicator).css('height', levelIndicator._height);
          levelIndicator._indicatorContainer.css('height', levelIndicator._indicatorSize);

          if (O$.isExplorer() && O$.isQuirksMode()) {
            levelIndicator._indicatorContainer.css('width', '100%');
          } else {
            if (levelIndicator._width != 0) {
              levelIndicator._indicatorContainer.css('width', levelIndicator._width - parseInt(levelIndicator._indicatorContainer.css('padding-left').replace('px', '')) - parseInt(levelIndicator._indicatorContainer.css('padding-right').replace('px', '')));
            } else {
              levelIndicator._indicatorContainer.css('width', Math.round(levelIndicator._width * 0.1));
            }
          }
        }

        var segmentSize = levelIndicator._segmentSize + levelIndicator._lamp_l_margin + levelIndicator._lamp_r_margin;
        var indicatorContainerSize = parseInt(levelIndicator._totalSegmentsCount * segmentSize);


        if (levelIndicator._orientation == "horizontal") {
          var paddingLeft = 2;
          var containerSizeAdjWidthDelta = (paddingLeft > segmentSize) ? paddingLeft : segmentSize - paddingLeft;

          if (O$.isExplorer() && O$.isQuirksMode()) {
            indicatorContainerSize -= containerSizeAdjWidthDelta;
            levelIndicator._totalSegmentsCount -= 1;
          }

          levelIndicator._indicatorContainer.css('width', indicatorContainerSize);
          levelIndicator._indicatorContainer.css('padding', '1px 0 1px 2px');
        } else {
          var padding = 1;
          var containerSizeAdjHeightDelta = (padding > segmentSize) ? padding : segmentSize - padding;

          if (O$.isExplorer() && O$.isQuirksMode()) {
            indicatorContainerSize -= containerSizeAdjHeightDelta;
            levelIndicator._totalSegmentsCount -= 1;
          }

          levelIndicator._indicatorContainer.css('height', indicatorContainerSize);
          levelIndicator._indicatorContainer.css('padding', '1px 1px 0 1px');
        }
      },

      _calculateRequiredSegmentsCount: function(progress_width) {
        var lamp_length = levelIndicator._segmentSize + levelIndicator._lamp_l_margin + levelIndicator._lamp_r_margin;
        return  Math.floor(progress_width / lamp_length);
      } ,

      _updateSegmentsColors: function(enabledLampsCount, disabledLampsCount, totalLampsCount) {
        var childSegments = jQuery(jQuery.correctId(levelIndicator.id + "::indicator")).children();
        childSegments.each(function (idx) {
          levelIndicator._updateSegmentColor(this, idx, enabledLampsCount, totalLampsCount);
        });
      },

      _updateSegmentColor: function(lamp_item, index, enabledLampsCount, totalLampsCount) {
        var currentColorIndex = 0;


        if (levelIndicator._fillDirection == "fromStart") {
          while (index > (Math.floor(totalLampsCount * (levelIndicator._transitionLevels[currentColorIndex])))
                  && currentColorIndex < (levelIndicator._transitionLevels.length - 1)) {
            currentColorIndex++;
          }

          var enabledColorH = (currentColorIndex < levelIndicator._colors.length - 1)
                  ? levelIndicator._colors[currentColorIndex]
                  : levelIndicator._colors[levelIndicator._colors.length - 1];

          var blendedColorH = O$.blendColors(enabledColorH, "#000000", 1 - levelIndicator._backgroundIntensity);

          if (index < enabledLampsCount) {
            jQuery(lamp_item).css('background-color', enabledColorH);
          } else {
            jQuery(lamp_item).css('background-color', blendedColorH);
          }

        } else {
          while ((totalLampsCount - index) > (Math.floor(totalLampsCount * (levelIndicator._transitionLevels[currentColorIndex])))
                  && currentColorIndex < (levelIndicator._transitionLevels.length - 1)) {
            currentColorIndex++;
          }

          var enabledColorV = (currentColorIndex < levelIndicator._colors.length - 1)
                  ? levelIndicator._colors[currentColorIndex]
                  : levelIndicator._colors[levelIndicator._colors.length - 1];
          var blendedColorV = O$.blendColors(enabledColorV, "#000000", 1 - levelIndicator._backgroundIntensity);

          if ((totalLampsCount - index) < enabledLampsCount) {
            jQuery(lamp_item).css('background-color', enabledColorV);
          } else {
            jQuery(lamp_item).css('background-color', blendedColorV);
          }
        }
      },

      _update: function() {
        levelIndicator._updateIndicator();
        levelIndicator._updateLabel();
      },

      setValue: function(value) {
        levelIndicator._value = value;

        levelIndicator._update();
      },

      getValue: function() {
        return levelIndicator._value;
      }
    });

    jQuery.noConflict();

    jQuery(document).ready(function() {
      var levelIndicatorElement = O$(levelIndicatorId);

      levelIndicatorElement._indicatorContainer = jQuery(O$(levelIndicatorId + "::indicator"));
      levelIndicatorElement._labelContainer = jQuery(O$(levelIndicatorId + "::label"));
      levelIndicatorElement.lamp_l_margin = parseInt(jQuery('.o_levelIndicator_indicatorSegment').css('margin-left').replace('px', ''));
      levelIndicatorElement.lamp_r_margin = parseInt(jQuery('.o_levelIndicator_indicatorSegment').css('margin-right').replace('px', ''));

      levelIndicatorElement._setupIndicator();
    });
  }
};