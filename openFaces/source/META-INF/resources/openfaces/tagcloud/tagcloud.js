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

O$.TagCloud = {
  _init: function(tagCloudId,
                  layout,
                  rolloverStyleClass) {

    var tagCloud = O$.initComponent(tagCloudId, {rollover: rolloverStyleClass}, {
      _OVAL_VERTICAL_POINT_SPACING : 20,
      _OVAL_HORIZONTAL_POINT_SPACING : 50,
      _ITEM_MAX_HEIGHT : 0,
      _ITEM_MAX_WIDTH : 0,
      _ITEM_AMOUNT : 0,
      _ITEM_SQUARE : 0,

      _getItems : function() {
        var cloudChildren = tagCloud.childNodes;
        var chLength = cloudChildren.length;
        var items = new Array();
        var c = 0;
        for (var i = 0; i < chLength; i++) {
          if (cloudChildren[i] && cloudChildren[i].nodeName == "A") {
            items[c++] = cloudChildren[i];
          }
        }
        return items;
      },

      _initMaxParametersAndSquare: function() {
        var ch = tagCloud.childNodes;
        tagCloud._ITEM_SQUARE = 0;
        var itemSize;
        for (var i = 0; i < ch.length; i++) {
          if (ch[i].nodeName == "A") {
            tagCloud._ITEM_AMOUNT++;
            itemSize = O$.getElementSize(ch[i]);

            tagCloud._ITEM_SQUARE += itemSize.height * itemSize.width;
            if (itemSize.height > tagCloud._ITEM_MAX_HEIGHT) {
              tagCloud._ITEM_MAX_HEIGHT = itemSize.height;
            }
            if (itemSize.width > tagCloud._ITEM_MAX_WIDTH) {
              tagCloud._ITEM_MAX_WIDTH = itemSize.width;
            }
          }
        }
      },

      _ovalPrepareSize : function() {
        var maxWidth = tagCloud._ITEM_MAX_WIDTH;
        var maxHeight = tagCloud._ITEM_MAX_HEIGHT;
        var tagCloudClass = O$.getElementOwnStyle(tagCloud);
        var paddingRight = O$.getNumericElementStyle(tagCloud, "padding-right");
        var paddingLeft = O$.getNumericElementStyle(tagCloud, "padding-left");
        var borderLeft = O$.getNumericElementStyle(tagCloud, "border-left-width");
        var borderRight = O$.getNumericElementStyle(tagCloud, "border-left-right");

        var width = O$.calculateNumericCSSValue(O$.getStyleClassProperty(tagCloudClass, "width"));
        if (width < 400) {
          O$.setElementWidth(tagCloud, 400);
          width = 400;
        }
        width = width - (paddingLeft + paddingRight + borderLeft + borderRight);

        var height = Math.floor(4 / ( Math.PI * width ) * tagCloud._ITEM_SQUARE * 1.4);

        return {width:width, height:height}
      },

      _ovalItemsReposition : function() {
        if (layout && layout == "oval") {
          tagCloud._initMaxParametersAndSquare();

          var cloudSize = tagCloud._ovalPrepareSize();
          var maxItemWidth = tagCloud._ITEM_MAX_WIDTH;
          var maxItemHeight = tagCloud._ITEM_MAX_HEIGHT;

          var cloudChildren = tagCloud.childNodes;
          var chLength = cloudChildren.length;

          var a = cloudSize.width / 2;
          var b = cloudSize.height / 2;

          var startY = tagCloud._getStartY(a, b);
          var currentP = tagCloud._getNewLinePoints(a, b, startY);

          var items = tagCloud._getItems();
          var curPos = currentP.xLeft;

          var k = 0;

          for (var i = 0; i < items.length; i++) {
            items[i].style.marginLeft = Math.floor(currentP.xLeft) + "px";
            items[i].style.marginRight = 0;

            while ((i + k + 1 < items.length) &&
                   curPos + O$.getElementSize(items[i + k]).width + O$.getElementSize(items[i + k + 1]).width < currentP.xRight) {
              curPos += O$.getElementSize(items[i + k]).width;
              k++;
            }

            curPos = currentP.xRight - curPos - O$.getElementSize(items[i + k]).width;

            for (var j = 1; j <= k; j++) {
              items[i + j].style.marginLeft = Math.floor(curPos / k) + "px";
              items[i + j].style.marginRight = 0;
            }

            currentP = tagCloud._getNewLinePoints(a, b, Math.floor(currentP.y + maxItemHeight));
            curPos = currentP.xLeft;
            k = 0;
            i = i + j - 1;
            jQuery(document.createElement('br')).insertAfter(items[i]);

            /*if (curPos + items[i].offsetWidth + (items[i+1] ? items[i+1].offsetWidth : 0) > currentP.xRight ) {
             currentP = tagCloud._getNewLinePoints(a, b, Math.floor(currentP.y + maxItemHeight));
             jQuery(document.createElement('br')).insertAfter(items[i]);
             step = currentP.xLeft;
             curPos = step;
             } else {
             curPos += items[i].offsetWidth;
             step = 0;
             }*/
          }
        }
      },

      _isOvalContainPoint : function(a, b, x, y) {
        return (x - a) * ( x - a) / ( a * a ) + ( y - b) * (y - b) / ( b * b) <= 1;
      },

      _getStartY : function(a, b) {
        var rezY = 0;
        var currentP = tagCloud._getNewLinePoints(a, b, rezY);
        var lineLength = (currentP.xRight - currentP.xLeft);
        var itemToRenderOnLine = Math.floor(lineLength / tagCloud._ITEM_MAX_WIDTH);
        while (itemToRenderOnLine < 1) {
          rezY++;
          currentP = tagCloud._getNewLinePoints(a, b, rezY);
          lineLength = (currentP.xRight - currentP.xLeft);
          itemToRenderOnLine = Math.floor(lineLength / tagCloud._ITEM_MAX_WIDTH);
        }
        return  rezY;
      },

      _getNewLinePoints : function(a, b, y) {
        if (y > 2 * b)
          return {xLeft: 0, xRight:2 * a, y:2 * b};

        var newX1 = Math.floor(a * (1 - Math.sqrt(1 - (y - b) * (y - b) / (b * b))));
        var newX2 = Math.floor(a * (1 + Math.sqrt(1 - (y - b) * (y - b) / (b * b))));

        return {xLeft:newX1, xRight:newX2, y:y};

      },
      //sphere layout
      _isOnSphere : function(x, y, z, R) {
        return x * x + y * y + z * z <= R * R;
      },

      _getRandomPoint3d : function(R) {
        var x = Math.floor(2 * R * Math.random() - R);
        var y = Math.floor(2 * R * Math.random() - R);
        var z = Math.floor(2 * R * Math.random() - R);
        var norm = Math.sqrt(x * x + y * y + z * z);
        return {x:R * x / norm, y:R * y / norm, z:R * z / norm};

      },
      _getSphericPoint : function(radius, a, b) {
        var toRad = Math.PI / 180.0;

        var z = radius * Math.cos(b * toRad) * Math.cos(a * toRad);
        var y = radius * Math.sin(b * toRad) * Math.cos(a * toRad);
        var x = radius * Math.sin(a * toRad);

        return {x:x, y:y, z:z};

      },
      
      _getDivisor : function(k) {
        var maxPotentialDivisor = Math.floor(Math.sqrt(k));
        var cur;
        for (cur = maxPotentialDivisor; cur > 1; cur--) {
          if (k % cur == 0) break;
        }
        return cur
      },

      _getSpherePoints : function(n, R) {
        var points = new Array();
        var point;
        var divisorA = tagCloud._getDivisor(n);
        var divisorB = n / divisorA;

        var segmentsA = Math.floor(180. / divisorA);
        var segmentsB = Math.floor(360. / divisorB);

        for (var a = -90.0; a < 90.0; a += segmentsA) {
          for (var b = 0.0; b < 360.0; b += segmentsB) {
            point = tagCloud._getSphericPoint(R, a + segmentsA / 2, b + segmentsB / 2);
            points.push(point);
          }
        }
        //        var point;
        //        var points = new Array();
        //       for (var i = 0; i < n; i++) {
        //        point = tagCloud._getRandomPoint3d(R);

        //       while (!tagCloud._isOnSphere(point.x, point.y, point.z, R)) {
        //        point = tagCloud._getRandomPoint3d(R);
        //     }
        //     points[i] = point;
        //   }
        return points;
      },

      _spherePrepareSize : function() {
        var tagCloudClass = O$.getElementOwnStyle(tagCloud);
        var width = O$.calculateNumericCSSValue(O$.getStyleClassProperty(tagCloudClass, "width"));
        var paddingRight = O$.getNumericElementStyle(tagCloud, "padding-right");
        var paddingLeft = O$.getNumericElementStyle(tagCloud, "padding-left");
        var borderLeft = O$.getNumericElementStyle(tagCloud, "border-left-width");
        var borderRight = O$.getNumericElementStyle(tagCloud, "border-left-right");
        if (width < 300) {
          O$.setElementWidth(tagCloud, 300);
          O$.setElementHeight(tagCloud, 300);
          width = 300;
        } else {
          O$.setElementHeight(tagCloud, width);
        }
        return width;//-(paddingRight+paddingLeft+borderLeft+borderRight);
      },

      _applyZCoordinate : function(item, zCoordinate, R) {
        var itemClass = O$.getElementOwnStyle(item);
        var opacity = O$.calculateNumericCSSValue(O$.getStyleClassProperty(itemClass, "opacity"));
        var fontSize = O$.calculateNumericCSSValue(O$.getStyleClassProperty(itemClass, "font-size"));
        var depth = (zCoordinate + R) / 2 / R;
        item.style.fontSize = Math.floor(fontSize + fontSize * (depth - 0.3)) + "px";
        O$.setOpacityLevel(item, opacity * depth + 0.5);
        var maxZIndex = tagCloud._ITEM_AMOUNT;
        item.style.zIndex = 1 + Math.floor(maxZIndex * depth);
      },
      //todo make style class for item
      _displayPoints : function(points, r, x0, y0) {
        var ch = tagCloud._getItems();
        for (var i = 0; i < ch.length; i++) {
          O$.setElementPos(ch[i],
          {x:points[i].x + x0 - O$.getElementSize(ch[i]).width / 2,
            y:points[i].y + y0});
          ch[i].style.position = "absolute";
          
          O$.setOpacityLevel(ch[i], 1);
          tagCloud._applyZCoordinate(ch[i], points[i].z, r);
        }
      },

      _renderSphere : function() {
        tagCloud._initMaxParametersAndSquare();
        var size = tagCloud._spherePrepareSize();
        var r = Math.floor(size / 2 - 20);
        var x0 = size / 2;
        var y0 = size / 2;
        var points = tagCloud._getSpherePoints(tagCloud._ITEM_AMOUNT, r);
        tagCloud._displayPoints(points, r, x0, y0);

        var mouseIn;
        var mousePoint;
        var angle = 0.025;
        var tagPos = O$.getElementPos(tagCloud);

        function mouseInside() {
          var xMouse = (mousePoint.x - x0 - tagPos.x);
          var yMouse = (mousePoint.y - y0 - tagPos.y);

          var norm = Math.sqrt(xMouse * xMouse + yMouse * yMouse);

          var mouseVector = {x: yMouse / norm , y: -xMouse / norm, z:0 };

          points = tagCloud._rotateSphere(mouseVector, angle, points);
          tagCloud._displayPoints(points, r, x0, y0);
        }

        function mouseOverAction(ev) {
          mousePoint = O$.getEventPoint(ev);
          mouseInside();
          mouseIn = setInterval(function() {
            mouseInside();
          }, 25);

        }

        function mouseMoveAction(ev) {
          mousePoint = O$.getEventPoint(ev);

          var x = mousePoint.x - x0 - tagPos.x;
          var y = mousePoint.y - y0 - tagPos.y;
          if (Math.sqrt(x * x + y * y) < 10) {
            angle = 0.005;
          } else {
            if (Math.sqrt(x * x + y * y) < r) {
              angle = 0.02 * Math.sqrt(x * x + y * y) / r;
            } else {
              angle = 0.02;
            }
          }
        }

        function mouseLeaveAction() {
          if (typeof(mouseIn) != "undefined")
            clearTimeout(mouseIn);
        }

        O$.addEventHandler(tagCloud, "mouseover", mouseOverAction);
        O$.addEventHandler(tagCloud, "mouseout", mouseLeaveAction);
        O$.addEventHandler(tagCloud, "mousemove", mouseMoveAction);
        ///     


      },



      _rotateSphere : function(direction, angle, points) {

        function multiplyScalarAndVector(scalar, vector) {
          return {x:scalar * vector.x,
            y:scalar * vector.y,
            z:scalar * vector.z};
        }

        function addVectors(v1, v2) {
          return {x: v1.x + v2.x,
            y: v1.y + v2.y,
            z: v1.z + v2.z};
        }

        function scalarProduction(v1, v2) {
          return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
        }

        function vectorProduction(v1, v2) {
          return {x:v1.y * v2.z - v1.z * v2.y,
            y: v2.x * v1.z - v2.z * v1.x,
            z: v1.x * v2.y - v1.y * v2.x};
        }

        var currentVector;
        var scalarProd;
        var vectorProd;
        var firstProd;
        var secondProd;
        var thirdProd;
        for (var i = 0; i < points.length; i++) {
          currentVector = {x:points[i].x,y:points[i].y,z:points[i].z};

          scalarProd = scalarProduction(currentVector, direction);
          vectorProd = vectorProduction(direction, currentVector);

          firstProd = multiplyScalarAndVector(Math.cos(angle), currentVector);
          secondProd = multiplyScalarAndVector(Math.sin(angle), vectorProd);
          thirdProd = multiplyScalarAndVector((1 - Math.cos(angle)) * scalarProd, direction);
          points[i] = addVectors(firstProd, secondProd);
          points[i] = addVectors(points[i], thirdProd);
        }
        return points;
      }

    });


    O$.addLoadEvent(function() {
      try {
       // tagCloud._renderSphere();
        tagCloud._ovalItemsReposition();
      } finally {
        O$.excludeClassNames(tagCloud, ["o_initially_invisible"]);
      }
    });

  }
};

