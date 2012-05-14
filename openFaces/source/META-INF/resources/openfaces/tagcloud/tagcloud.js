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

O$.TagCloud = {
  _init: function(tagCloudId,
                  layout,
                  rolloverStyleClass,

                  itemRolloverStyleClass,
                  shadowScale3D,
                  rotationSpeed3D,
                  stopRotationPeriod3D
          ) {

    var tagCloud = O$.initComponent(tagCloudId, {rollover: rolloverStyleClass}, {
      _ITEM_MAX_HEIGHT: 0,
      _ITEM_MAX_WIDTH: 0,

      _ITEM_AMOUNT: 0,
      _ITEM_SQUARE: 0,

      _getItems: function() {
        var cloudChildren = tagCloud.childNodes;
        var chLength = cloudChildren.length;
        var items = new Array();
        var c = 0;
        var itemClass;
        var itemColor;
        var itemFontSize;

        for (var i = 0; i < chLength; i++) {
          if (cloudChildren[i] && cloudChildren[i].nodeName == "A") {
            itemClass = O$.getElementOwnStyle(cloudChildren[i]);
            itemColor = O$.getElementStyle(cloudChildren[i], "color");//O$.getStyleClassProperty(itemClass, "color");
            itemFontSize = O$.getElementStyle(cloudChildren[i], "font-size");//O$.getStyleClassProperty(itemClass, "font-size");

            items.push({_element:cloudChildren[i],
              _middlePlaneFontSize : O$.calculateNumericCSSValue(itemFontSize),
              _frontPlaneColor : tagCloud._rgbConvert(itemColor)});
          }
        }
        return items;
      },

      _initMaxParametersAndSquare: function() {
        var ch = tagCloud.childNodes;
        tagCloud._ITEM_SQUARE = 0;

        var itemWidth;
        var itemHeight;
        for (var i = 0; i < ch.length; i++) {
          if (ch[i].nodeName == "A") {
            tagCloud._ITEM_AMOUNT++;

            itemWidth = ch[i].clientWidth;
            itemHeight = ch[i].clientHeight;

            if (itemHeight > tagCloud._ITEM_MAX_HEIGHT) {
              tagCloud._ITEM_MAX_HEIGHT = itemHeight;
            }
            if (itemWidth > tagCloud._ITEM_MAX_WIDTH) {
              tagCloud._ITEM_MAX_WIDTH = itemWidth;
            }
          }
        }
        for (var i = 0; i < ch.length; i++) {
          if (ch[i].nodeName == "A") {
            tagCloud._ITEM_SQUARE += tagCloud._ITEM_MAX_HEIGHT * ch[i].clientWidth;           
          }
        }
      },

      _ovalPrepareSize: function() {
        var maxWidth = tagCloud._ITEM_MAX_WIDTH;
        var maxHeight = tagCloud._ITEM_MAX_HEIGHT;
        var tagCloudClass = O$.getElementOwnStyle(tagCloud);
        var paddingRight = O$.getNumericElementStyle(tagCloud, "padding-right");
        var paddingLeft = O$.getNumericElementStyle(tagCloud, "padding-left");
        var borderLeft = O$.getNumericElementStyle(tagCloud, "border-left-width");
        var borderRight = O$.getNumericElementStyle(tagCloud, "border-left-right");
        var width = O$.calculateNumericCSSValue(O$.getElementStyle(tagCloud, "width"));
       // var width = O$.calculateNumericCSSValue(O$.getStyleClassProperty(tagCloudClass, "width"));
        if (width < 200) {
          O$.setElementWidth(tagCloud, 200);
          width = 200;
        }
        width = width - (paddingLeft + paddingRight + borderLeft + borderRight);
//        var k = 0.4;
       // var computedWidth = Math.sqrt(tagCloud._ITEM_SQUARE * 4 / Math.PI) * (1.2+k);
       // O$.setElementWidth(tagCloud, computedWidth+paddingLeft + paddingRight + borderLeft + borderRight);
       // var computedHeight = Math.sqrt(tagCloud._ITEM_SQUARE *4/ Math.PI)* (1.2-k);
        var height = Math.floor(4 / ( Math.PI * width ) * tagCloud._ITEM_SQUARE * 1.3);
        //return {width:computedWidth, height:computedHeight}
        return {width:width, height:height}
      },

      _ovalItemsReposition: function() {
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
            items[i]._element.style.marginLeft = Math.floor(currentP.xLeft) + "px";
            items[i]._element.style.marginRight = 0;

            while ((i + k + 1 < items.length) &&
                   curPos + items[i + k]._element.clientWidth + items[i + k + 1]._element.clientWidth < currentP.xRight) {
              curPos += items[i + k]._element.clientWidth;
              k++;
            }
            curPos = currentP.xRight - curPos - items[i + k]._element.clientWidth;

            for (var j = 1; j <= k; j++) {
              items[i + j]._element.style.marginLeft = Math.floor(curPos / k) + "px";
              items[i + j]._element.style.marginRight = 0;
            }

            currentP = tagCloud._getNewLinePoints(a, b, Math.floor(currentP.y + maxItemHeight));
            curPos = currentP.xLeft;
            k = 0;
            i = i + j - 1;
            jQuery(document.createElement('br')).insertAfter(items[i]._element);
          }
        }
      },

      _isOvalContainPoint: function(a, b, x, y) {
        return (x - a) * ( x - a) / ( a * a ) + ( y - b) * (y - b) / ( b * b) <= 1;
      },

      _getStartY: function(a, b) {
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

      _getNewLinePoints: function(a, b, y) {
        if (y > 2 * b)
          return {xLeft: 0, xRight:2 * a, y:2 * b};

        var newX1 = Math.floor(a * (1 - Math.sqrt(1 - (y - b) * (y - b) / (b * b))));
        var newX2 = Math.floor(a * (1 + Math.sqrt(1 - (y - b) * (y - b) / (b * b))));

        return {xLeft:newX1, xRight:newX2, y:y};

      },
      //sphere layout
      _isOnSphere: function(x, y, z, R) {
        return x * x + y * y + z * z <= R * R;
      },

      _getRandomPoint3d: function() {
        var x1 = 1 - 2 * Math.random();
        var x2 = 1 - 2 * Math.random();
        var isStop = x1 * x1 + x2 * x2;
        while (isStop >= 1) {
          x1 = 1 - 2 * Math.random();
          x2 = 1 - 2 * Math.random();
          isStop = x1 * x1 + x2 * x2;
        }
        var x = 2 * x1 * Math.sqrt(1 - isStop);
        var y = 2 * x2 * Math.sqrt(1 - isStop);
        var z = 1 - 2 * isStop;
        return {x: x , y: y, z: z};

      },
      _getSphericPoint: function(radius, a, b) {
        var teta = a * Math.PI / 180;
        var fi = b * Math.PI / 180;

        var x = radius * Math.cos(teta) * Math.cos(fi);
        var z = radius * Math.sin(teta) * Math.cos(fi);
        var y = radius * Math.sin(fi);
        return {x: x, y:y, z: z};
      },

      _getLoxodromicPoint: function(radius, tetta) {
        var bb = 0.306349 / 3;

        var aa = 0.2;

        var xx = aa * Math.cos(tetta) * Math.exp(bb * tetta);
        var yy = aa * Math.sin(tetta) * Math.exp(bb * tetta);

        var dev = xx * xx + yy * yy + 1;

        var x = radius * 2 * xx / dev;
        var y = radius * 2 * yy / dev;

        var z = radius * (1 - 2 / dev);
        return {x: x, y:y, z: z};

      },

      _getSpherePoints: function(n, R) {
        var pointsToReturn = new Array();
        var points = new Array();

        for (var ii = 0; ii < n; ii++) {
          points[ii] = tagCloud._getRandomPoint3d();
        }

        correction();

        function getRandom(intValue) {
          return Math.floor(Math.random() * intValue);
        }

        function getRandomIndex(arrayValue) {
          return getRandom(1000) % arrayValue.length;
        }

        function shuffle(o) {
          var t;
          var index = getRandomIndex(o);
          var index2 = getRandomIndex(o);
          for (var i = 0; i < 500; i++) {
            t = o[index];
            o[index] = o[index2];
            o[index2] = t;
            index = getRandomIndex(o);
            index2 = getRandomIndex(o);
          }
          return o;
        }

        function correction() {           //
          // var tol = 1e-3;
          var iterationMax = 30;
          var d = Math.sqrt(2. * Math.PI / n);
          // var tol = tol * Math.sqrt(n);
          var vMax = 1.;
          var iteration = 0;
          var a = 0.6;
          var part = 1 / 2;
          var vv = new Array();
          var vH = new Array();
          var dx = 0;
          var dy = 0;
          var dz = 0;
          var sx = 0;
          var sy = 0;
          var sz = 0;
          var norm = 0;
          var projection = 0;
          var v2 = 0;
          var dt = 0;
          while (iteration < iterationMax) {
            part = part * 0.978;
            a = a * 0.99;
            iteration = iteration + 1;
            for (var ii = 0; ii < n; ii++) {
              vv[ii] = {x:0,y:0,z:0};
              sx = 0;
              sy = 0;
              sz = 0;

              for (var jj = 0; jj < n; jj++) {
                dx = points[ii].x - points[jj].x;
                dy = points[ii].y - points[jj].y;
                dz = points[ii].z - points[jj].z;

                norm = Math.sqrt(dx * dx + dy * dy + dz * dz);
                if (norm != 0) {
                  sx = sx + dx / norm;
                  sy = sy + dy / norm;
                  sz = sz + dz / norm;
                }
              }
              vv[ii].x = sx;
              vv[ii].y = sy;
              vv[ii].z = sz;
              projection = points[ii].x * vv[ii].x + points[ii].y * vv[ii].y + points[ii].z * vv[ii].z;

              vv[ii].x = vv[ii].x - projection * points[ii].x;
              vv[ii].y = vv[ii].y - projection * points[ii].y;
              vv[ii].z = vv[ii].z - projection * points[ii].z;
              if (iteration == 1) {
                vH[ii] = {x:0,y:0 ,z:0};
              }

              vv[ii].x = vH[ii].x * (1 - a) + vv[ii].x * a;
              vv[ii].y = vH[ii].y * (1 - a) + vv[ii].y * a;
              vv[ii].z = vH[ii].z * (1 - a) + vv[ii].z * a;

              vH[ii].x = vv[ii].x;
              vH[ii].y = vv[ii].y;
              vH[ii].z = vv[ii].z;

              v2 = Math.sqrt(vv[ii].x * vv[ii].x + vv[ii].y * vv[ii].y + vv[ii].z * vv[ii].z);
              if (v2 > vMax) {
                vMax = v2;
              }
            }
            //Time step
            dt = part * d / vMax;
            //Update coordinates
            for (var k = 0; k < n; k++) {
              points[k].x = points[k].x + dt * vv[k].x;
              points[k].y = points[k].y + dt * vv[k].y;
              points[k].z = points[k].z + dt * vv[k].z;

              //Normalize
              norm = Math.sqrt(points[k].x * points[k].x + points[k].y * points[k].y + points[k].z * points[k].z);
              points[k].x = points[k].x / norm;
              points[k].y = points[k].y / norm;
              points[k].z = points[k].z / norm;
            }

          }
          for (var fk = 0; fk < n; fk++) {
            points[fk] = {x:R * points[fk].x,y:R * points[fk].y,z:R * points[fk].z};
          }
        }

        return points;
      },

      _spherePrepareSize: function() {
       // var tagCloudClass = O$.getElementOwnStyle(tagCloud);
        var width = O$.calculateNumericCSSValue(O$.getElementStyle(tagCloud, "width"));

        var paddingRight = O$.getNumericElementStyle(tagCloud, "padding-right");
        var paddingLeft = O$.getNumericElementStyle(tagCloud, "padding-left");
        var borderLeft = O$.getNumericElementStyle(tagCloud, "border-left-width");
        var borderRight = O$.getNumericElementStyle(tagCloud, "border-left-right");

        if (width != null && width < 200) {
          O$.setElementWidth(tagCloud, 200);
          O$.setElementHeight(tagCloud, 200);
          width = 200;
        } else {
          O$.setElementHeight(tagCloud, width);
        }
        return width- (paddingLeft + paddingRight + borderLeft + borderRight);
      },

      _rgbConvert: function(str) {
        if (str === "undefined") {
          return "";
        }

        // in IE if color assigned not as color name, this function works bag 
        if (str.substring(0, 1) == "#") {
          O$.assertEquals(str.length, 7, "O$.TagCloud._rgbConvert error: ");
          return str;
        }
        O$.assertEquals("rgb(", str.substring(0, 4), "O$.TagCloud._rgbConvert error: ");

        str = str.replace(/rgb\(|\)/g, "").split(",");

        str[0] = parseInt(str[0], 10).toString(16).toLowerCase();
        str[1] = parseInt(str[1], 10).toString(16).toLowerCase();
        str[2] = parseInt(str[2], 10).toString(16).toLowerCase();
        str[0] = (str[0].length == 1) ? '0' + str[0] : str[0];
        str[1] = (str[1].length == 1) ? '0' + str[1] : str[1];
        str[2] = (str[2].length == 1) ? '0' + str[2] : str[2];
        return ('#' + str.join(""));
      },

      _renderSphere: function() {

        function displayPoint(item, point) {
          var depth = Math.floor((r + point.z) / 2 / r * 1000) * 0.001;
          var currentFontCoefficient = Math.floor(distanceToMiddlePlane / (distanceToMiddlePlane - point.z) * 10000) * 0.0001;//item._middlePlaneFontSize * distanceToMiddlePlane / (distanceToMiddlePlane - point.z);

          item._element.style.zIndex = 1 + Math.floor(r + point.z);

          var currentX = Math.floor((point.x + xCorrectionCenter) * 1000) * 0.001;
          var currentY = Math.floor((point.y + yCorrectionCenter) * 1000) * 0.001;
          if (O$.isMozillaFF() && item._element.style.MozTransform !== undefined) {
            currentX = Math.floor(point.x * 1000) * 0.001;
            item._element.style.MozTransform = "translate(" + currentX + "px," + currentY + "px) scale(" + currentFontCoefficient + ")";
          }

          if ((O$.isChrome() || O$.isSafari()) && item._element.style.WebkitTransform !== undefined) {
            if (O$.isChrome())
              currentX = Math.floor(point.x * 1000) * 0.001;
            item._element.style.WebkitTransform = "translate(" + currentX + "px," + currentY + "px) scale(" + currentFontCoefficient + ")";
          }

          if  (O$.isOpera() && item._element.style.OTransform !== undefined) {
            item._element.style.OTransform = "translate(" + currentX + "px," + currentY + "px) scale(" + currentFontCoefficient + ")";
          }

          if (O$.isExplorer()) {
            item._element.style.fontSize = item._middlePlaneFontSize * currentFontCoefficient + "px";
            O$.setElementPos(item._element,
            {x: currentX,
              y: currentY});
            //            item._element.style.filter = "progid:DXImageTransform.Microsoft.Matrix( M11 = " + currentFontCoefficient+
            //                                         ", M22 = " + currentFontCoefficient+ ", SizingMethod = 'auto expand')";

            //                        item._element.style.filter = "progid:DXImageTransform.Microsoft.Matrix( Dx = " + currentX+
            //                                                     ", Dy = " + currentY+ ")";

          }

          var currentColor = O$.getInterpolatedValue(backPlaneColor, item._frontPlaneColor,
                  (depth * shadowScale3D + (1 - shadowScale3D)));
          item._element.style.color = currentColor;
        }

        function rotateSpherePoint(direction, point) {
          var currentVector;
          var scalarProd;
          var vectorProd;
          var firstProd;
          var secondProd;
          var thirdProd;
          currentVector = point;
          scalarProd = scalarProduction(currentVector, direction);
          vectorProd = vectorProduction(direction, currentVector);
          firstProd = multiplyScalarAndVector(cosine, currentVector);
          secondProd = multiplyScalarAndVector(sine, vectorProd);
          thirdProd = multiplyScalarAndVector(cosineMinus * scalarProd, direction);
          currentVector = addVectors(firstProd, secondProd);
          point = addVectors(currentVector, thirdProd);
          return point;
        }

        function multiplyScalarAndVector(scalar, vector) {
          return {x:scalar * vector.x, y:scalar * vector.y, z:scalar * vector.z};
        }

        function addVectors(v1, v2) {
          return {x: v1.x + v2.x, y: v1.y + v2.y, z: v1.z + v2.z};
        }

        function scalarProduction(v1, v2) {
          return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
        }

        function vectorProduction(v1, v2) {
          return {x:v1.y * v2.z - v1.z * v2.y, y: v2.x * v1.z - v2.z * v1.x, z: v1.x * v2.y - v1.y * v2.x};
        }

        function mouseInsideRotation(ev) {
          for (var i = 0; i < points.length; i++) {
            displayPoint(items[i], points[i]);
          }
          time = new Date().getTime();
          angle = (time - startTime) * speed / 1000;
          startTime = new Date().getTime();
          var xMouse = (currentMousePoint.x - xCorrectionMouse);
          var yMouse = (currentMousePoint.y - yCorrectionMouse);
          var norm = Math.sqrt(xMouse * xMouse + yMouse * yMouse);

          if (norm > 0) {
            currentMouseVector = {x: yMouse / norm , y: -xMouse / norm, z: 0 };
            cosine = Math.cos(angle);
            sine = Math.sin(angle);
            cosineMinus = 1 - cosine;
            for (var j = 0; j < points.length; j++) {
              points[j] = rotateSpherePoint(currentMouseVector, points[j]);
            }
          }
        }

        function mouseOverAction(ev) {
          if (typeof(mouseOut) != "undefined") clearInterval(mouseOut);
          if (typeof(onLoadF) != "undefined") clearInterval(onLoadF);
          if (speed == null)  speed = maxSpeed;
          currentMousePoint = O$.getEventPoint(ev);
          startTime = new Date().getTime();
          mouseInsideRotation(ev);
          mouseInside = setInterval(function() {
            mouseInsideRotation(ev);
          }, interval);
        }

        function mouseMoveAction(ev) {
          currentMousePoint = O$.getEventPoint(ev);
          var x = currentMousePoint.x - xCorrectionMouse;
          var y = currentMousePoint.y - yCorrectionMouse;
          var distance = Math.sqrt(x * x + y * y);

          if (distance < r) {
            speed = maxSpeed * distance / r;
          } else {
            speed = maxSpeed;
          }
        }

        //        function onLoadAction() {
        //          var time = new Date().getTime();
        //          var timeFromLoading = time - startTimeLoading;
        //          if (timeFromLoading < 4000) {
        //            speed = 1.2 * maxSpeed * Math.exp(-timeFromLoading * 0.0003);
        //          } else {
        //            if (typeof(onLoadF) != "undefined") clearInterval(onLoadF);
        //          }
        //          angle = (interval + time - startTime) * speed / 1000;
        //
        //          startTime = new Date().getTime();
        //          cosine = Math.cos(angle);
        //          sine = Math.sin(angle);
        //          cosineMinus = 1 - cosine;
        //          for (var i = 0; i < points.length; i++) {
        //            points[i] = rotateSpherePoint(startRotation, points[i]);
        //            displayPoint(items[i], points[i]);
        //          }
        //        }

        function mouseLeaveAction() {
          if (typeof(mouseInside) != "undefined") clearInterval(mouseInside);
          currentMousePoint = {x:0,y:0};
          leaveStartTime = new Date().getTime();
          leaveSpeed = speed;
          mouseLeave();
          mouseOut = setInterval(function() {
            mouseLeave();
          }, interval);
        }

        function mouseLeave() {
          for (var j = 0; j < points.length; j++) {
            displayPoint(items[j], points[j]);
          }
          var time = new Date().getTime();
          var timeFromLeaving = time - leaveStartTime;
          if (timeFromLeaving < timeAttenuation) {
            speed = leaveSpeed * Math.exp(-timeFromLeaving * attenuation);
          } else {
            if (typeof(mouseOut) != "undefined") clearInterval(mouseOut);
          }
          angle = (time - startTime) * speed / 1000;
          startTime = new Date().getTime();

          cosine = Math.cos(angle);
          sine = Math.sin(angle);
          cosineMinus = 1 - cosine;

          for (var i = 0; i < points.length; i++) {
            points[i] = rotateSpherePoint(currentMouseVector, points[i]);
          }
        }

        if (layout && layout == "sphere") {
          tagCloud._initMaxParametersAndSquare();
          var size = tagCloud._spherePrepareSize();

          var r = Math.floor(size / 2 - tagCloud._ITEM_MAX_WIDTH / 4);
          var x0 = Math.floor(size / 2);
          var y0 = Math.floor(size / 2);

          var tagPos = O$.getElementPos(tagCloud);
          var points = tagCloud._getSpherePoints(tagCloud._ITEM_AMOUNT, r);
          var items = tagCloud._getItems();

          var mouseInside;
          var mouseOut;
          var onLoadF;

          var currentMousePoint = {x:0,y:0};
          var currentMouseVector;

          var iterationLoad;
          var startTime = new Date().getTime();
          var startTimeLoading = new Date().getTime();
          var time;

          var leaveStartTime;
          var leaveSpeed;
          var maxSpeed = rotationSpeed3D * Math.PI / 180;
          var interval = 30;
          var speed = null;
          var angle = Math.PI / 5;
          var timeAttenuation = Math.floor(stopRotationPeriod3D * 1000); //in milliseconds
          var attenuation = -Math.log(0.1) / timeAttenuation;

          var xCorrectionMouse = x0 + tagPos.x;
          var yCorrectionMouse = y0 + tagPos.y;

          var xCorrectionCenter = x0 - tagCloud._ITEM_MAX_WIDTH / 4;
          var yCorrectionCenter = y0 - tagCloud._ITEM_MAX_HEIGHT / 2;

          var distanceToMiddlePlane = 2 * r;
          if (shadowScale3D < 0) {
            shadowScale3D = 0;
          } else {
            if (shadowScale3D > 1) {
              shadowScale3D = 1;
            }
          }

          var tagCloudClass = O$.getElementOwnStyle(tagCloud);
          var backPlaneColor = O$.getStyleClassProperty(tagCloudClass, "background-color");
          backPlaneColor = backPlaneColor != null ? tagCloud._rgbConvert(backPlaneColor) : "#ffffff";
          backPlaneColor = backPlaneColor != "" ? backPlaneColor : "#ffffff";
          var startRotation = {x: -1 / 2 , y: -Math.sqrt(3) / 2, z:0 };

          var cosine = Math.cos(angle);
          var sine = Math.sin(angle);
          var cosineMinus = 1 - cosine;

          for (var i = 0; i < points.length; i++) {
            points[i] = rotateSpherePoint(startRotation, points[i]);
            displayPoint(items[i], points[i]);
          }
          //
          //          onLoadAction();
          //          onLoadF = setInterval(function() {
          //            onLoadAction();
          //          }, interval);

          O$.addEventHandler(tagCloud, "mouseover", mouseOverAction);
          O$.addEventHandler(tagCloud, "mouseout", mouseLeaveAction);
          O$.addEventHandler(tagCloud, "mousemove", mouseMoveAction);

        }

      }
    });


    O$.addLoadEvent(function() {
      try {
        tagCloud._renderSphere();
        tagCloud._ovalItemsReposition();
      } finally {
        tagCloud.style.visibility = "visible";
      }
    });

  }
}
        ;

