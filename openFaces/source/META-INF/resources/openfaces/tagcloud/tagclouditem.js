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

O$.TagCloudItem = {
  _init: function(tagId,
                  cloudId,
                  itemRolloverStyleClass) {

    var tag = O$.initComponent(cloudId + "::" + tagId, {rollover: itemRolloverStyleClass}, {
      _cloudId : cloudId,
      _innerText : O$(cloudId + "::" + tagId + "::item_textValue").innerHTML,
      _weight : O$(cloudId + "::" + tagId + "::item_weight").innerHTML,

      _setVarParameters : function() {
        O$.setValue(tag._cloudId + "::var_title", tag.title);
        O$.setValue(tag._cloudId + "::var_textValue", tag._innerText);
        O$.setValue(tag._cloudId + "::var_weight", tag._weight);
        O$.setValue(tag._cloudId + "::var_url", tag.href);
      },

      _submitVarParameters : function() {
        tag._setVarParameters();
        var form = O$.getParentNode(tag, "form");
        form.submit();
      },

      _fixOpacity : function(tag) {
        var tagCloud = O$(tag._cloudId);
        var tagCloudClass = O$.getElementOwnStyle(tagCloud);
        var tagClass = O$.getElementOwnStyle(tag);
        var backGroundCloud = O$.getStyleClassProperty(tagCloudClass, "background");
        var backGroundSelf = O$.getStyleClassProperty(tagClass, "background");

        if (!backGroundSelf) {
          if (!backGroundCloud) {
            if (O$.isSafari()) {
              backGroundCloud = "white";
            } else {
              backGroundCloud = "Window";
            }
          }

          tag.style.background = backGroundCloud;

        }

      }
    });

    tag._fixOpacity(tag);

    O$.addEventHandler(tag, "mouseup", tag._submitVarParameters);
  }
};