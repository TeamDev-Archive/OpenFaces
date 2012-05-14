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
jQuery.correctId = function(elementId) {
  return '#' + elementId.replace(/(:|\.)/g, '\\$1');
};

O$.LevelIndicator = {
  _init: function(levelIndicatorId,
                  value,
                  segmentSize,
                  orientation,
                  fillDirection,
                  defaultWidth,
                  defaultHeight,
                  colors,
                  transitionLevels,
                  inactiveSegmentIntensity) {
    var levelIndicator = O$.initComponent(levelIndicatorId, null, {
      _displayArea: O$(levelIndicatorId + "::displayArea"),
      _label: O$(levelIndicatorId + "::label"),
      _segment: O$(levelIndicatorId + "::segment"),
      _segmentSize: segmentSize,
      _value: value,
      _orientation: orientation,
      _fillDirection: fillDirection,
      _colors: colors,
      _transitionLevels: transitionLevels,
      _inactiveSegmentIntensity: inactiveSegmentIntensity,
      _displayAreaSize: 0,
      _segment_left_margin: 0,
      _segment_right_margin: 2,

      _setupIndicator: function() {
        var clientSpecifiedWidth = O$.getStyleClassProperty(levelIndicator.className, "width");
        var clientSpecifiedHeight = O$.getStyleClassProperty(levelIndicator.className, "height");

        if (clientSpecifiedWidth === undefined && clientSpecifiedHeight === undefined) {
          // This means that LevelIndicator component should use it's default state
          if (levelIndicator._orientation == "horizontal") {
            levelIndicator._width = defaultWidth;
            levelIndicator._height = defaultHeight;
            levelIndicator.style.width = defaultWidth;
            levelIndicator.style.height = defaultHeight;
          } else {
            if (O$.isExplorer() && O$.isQuirksMode()) {
              var indicatorPaddingTop = O$.getNumericElementStyle(levelIndicator, "padding-top", true);
              var indicatorPaddingBottom = O$.getNumericElementStyle(levelIndicator, "padding-bottom", true);
              defaultHeight = parseInt(defaultHeight) + parseInt(indicatorPaddingTop) + parseInt(indicatorPaddingBottom);
            }

            levelIndicator.style.width = defaultHeight;
            levelIndicator.style.height = defaultWidth;
            levelIndicator._width = defaultHeight;
            levelIndicator._height = defaultWidth;
          }
        } else {
          var parentWidth = jQuery(levelIndicator.parentNode).width();
          var parentHeight = jQuery(levelIndicator.parentNode).height();
          levelIndicator._width = O$.calculateNumericCSSValue(clientSpecifiedWidth, parentWidth);
          levelIndicator._height = O$.calculateNumericCSSValue(clientSpecifiedHeight, parentHeight);
        }

        if (this._orientation == "horizontal") {
          var labelPaddingLeft = O$.getNumericElementStyle(levelIndicator._label, "padding-left", true);
          var labelPaddingRight = O$.getNumericElementStyle(levelIndicator._label, "padding-right", true);

          var labelContainerWidth = (O$.isExplorer() && O$.isQuirksMode())
                  ? parseInt(levelIndicator._labelContainer.width()) + parseInt(labelPaddingLeft) + parseInt(labelPaddingRight)
                  : parseInt(levelIndicator._labelContainer.width());

          var levelIndicatorPaddingRight = O$.getNumericElementStyle(levelIndicator, "padding-right", true);
          var levelIndicatorPaddingLeft = O$.getNumericElementStyle(levelIndicator, "padding-left", true);

          levelIndicator._displayAreaSize = levelIndicator._width - labelContainerWidth - parseInt(levelIndicatorPaddingRight) - parseInt(levelIndicatorPaddingLeft);
        } else {
          var labelPaddingTop = O$.getNumericElementStyle(levelIndicator._label, "padding-top", true);
          var labelPaddingBottom = O$.getNumericElementStyle(levelIndicator._label, "padding-bottom", true);

          var labelContainerHeight = (O$.isExplorer() && O$.isQuirksMode())
                  ? parseInt(levelIndicator._labelContainer.height()) + parseInt(labelPaddingTop) + parseInt(labelPaddingBottom)
                  : parseInt(levelIndicator._labelContainer.height());

          var levelIndicatorPaddingTop = O$.getNumericElementStyle(levelIndicator, "padding-top", true);
          var levelIndicatorPaddingBottom = O$.getNumericElementStyle(levelIndicator, "padding-bottom", true);

          levelIndicator._displayAreaSize = levelIndicator._height - labelContainerHeight - parseInt(levelIndicatorPaddingBottom) - parseInt(levelIndicatorPaddingTop);
        }

        levelIndicator._totalSegmentsCount = levelIndicator._calculateRequiredSegmentsCount(levelIndicator._displayAreaSize);
        levelIndicator._transitionLevels.push(1.0);

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
          lampItem.addClass("o_levelIndicator_segment");
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

          levelIndicator._displayAreaContainer.append(lampItem);
        }
      },

      _adjustIndicatorSize: function() {
        if (levelIndicator._orientation == "horizontal") {
          jQuery(levelIndicator).css('width', levelIndicator._width);
          jQuery(levelIndicator).css('height', levelIndicator._height);
          levelIndicator._displayAreaContainer.css('width', levelIndicator._displayAreaSize);

          if (levelIndicator._height != 0) {
            var displayAreaPaddingTop = O$.getNumericElementStyle(levelIndicator._displayArea, 'padding-top', true);
            var displayAreaPaddingBottom = O$.getNumericElementStyle(levelIndicator._displayArea, 'padding-bottom', true);

            var totalProgressHeight = levelIndicator._height - parseInt(displayAreaPaddingTop) - parseInt(displayAreaPaddingBottom);
            levelIndicator._displayAreaContainer.css('height', totalProgressHeight);
          } else {
            levelIndicator._displayAreaContainer.css('height', Math.round(levelIndicator._width * 0.1));
          }

        } else {
          jQuery(levelIndicator).css('width', levelIndicator._width);
          jQuery(levelIndicator).css('height', levelIndicator._height);
          levelIndicator._displayAreaContainer.css('height', levelIndicator._displayAreaSize);

          if (O$.isExplorer() && O$.isQuirksMode()) {
            levelIndicator._displayAreaContainer.css('width', '100%');
          } else {
            if (levelIndicator._width != 0) {
              var displayAreaPaddingLeft = O$.getNumericElementStyle(levelIndicator._displayArea, 'padding-left', true);
              var displayAreaPaddingRight = O$.getNumericElementStyle(levelIndicator._displayArea, 'padding-right', true);

              levelIndicator._displayAreaContainer.css('width', levelIndicator._width - parseInt(displayAreaPaddingLeft) - parseInt(displayAreaPaddingRight));
            } else {
              levelIndicator._displayAreaContainer.css('width', Math.round(levelIndicator._width * 0.1));
            }
          }
        }

        var segmentSize = levelIndicator._segmentSize + levelIndicator._segment_left_margin + levelIndicator._segment_right_margin;
        var indicatorContainerSize = parseInt(levelIndicator._totalSegmentsCount * segmentSize);


        if (levelIndicator._orientation == "horizontal") {
          var paddingLeft = 2;
          var containerSizeAdjWidthDelta = (paddingLeft > segmentSize) ? paddingLeft : segmentSize - paddingLeft;

          if (O$.isExplorer() && O$.isQuirksMode()) {
            indicatorContainerSize -= containerSizeAdjWidthDelta;
            levelIndicator._totalSegmentsCount -= 1;
          }

          levelIndicator._displayAreaContainer.css('width', indicatorContainerSize);
          levelIndicator._displayAreaContainer.css('padding', '1px 0 1px 2px');
        } else {
          var padding = 1;
          var containerSizeAdjHeightDelta = (padding > segmentSize) ? padding : segmentSize - padding;

          if (O$.isExplorer() && O$.isQuirksMode()) {
            indicatorContainerSize -= containerSizeAdjHeightDelta;
            levelIndicator._totalSegmentsCount -= 1;
          }

          levelIndicator._displayAreaContainer.css('height', indicatorContainerSize);
          levelIndicator._displayAreaContainer.css('padding', '1px 1px 0 1px');
        }
      },

      _calculateRequiredSegmentsCount: function(progress_width) {
        var lamp_length = levelIndicator._segmentSize + levelIndicator._segment_left_margin + levelIndicator._segment_right_margin;
        return  Math.floor(progress_width / lamp_length);
      } ,

      _updateSegmentsColors: function(enabledLampsCount, disabledLampsCount, totalLampsCount) {
        var childSegments = jQuery(jQuery.correctId(levelIndicator.id + "::displayArea")).children();
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

          var blendedColorH = O$.blendColors(enabledColorH, "#000000", 1 - levelIndicator._inactiveSegmentIntensity);

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
          var blendedColorV = O$.blendColors(enabledColorV, "#000000", 1 - levelIndicator._inactiveSegmentIntensity);

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
        if (value < 0) {
          levelIndicator._value = 0;
        }
        else if (value > 1) {
          levelIndicator._value = 1;
        }
        else {
          levelIndicator._value = value;
        }

        levelIndicator._update();
      },

      getValue: function() {
        return levelIndicator._value;
      }
    });

    jQuery.noConflict();

    jQuery(document).ready(function() {
      var levelIndicatorElement = O$(levelIndicatorId);

      levelIndicatorElement._displayAreaContainer = jQuery(O$(levelIndicatorId + "::displayArea"));
      levelIndicatorElement._labelContainer = jQuery(O$(levelIndicatorId + "::label"));

      levelIndicatorElement._setupIndicator();
    });
  }
};