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

O$._RangeMap = O$.createClass(null, {
          constructor: function() {
            this._disjointRanges = [];
          },

          _rangesIntersect: function(range1, range2) {
            // Returns true if the ranges intersect. End-point intersection is also not considered as an intersection.
            return range2.end >= range1.start && range2.start <= range1.end;
          },

          _rangesIntersectExclude: function(range1, range2) {
            // Returns true if the ranges intersect. End-point intersection is NOT considered as an intersection.
            return this._rangesIntersect(range1, range2) && !(range2.end == range1.start || range2.start == range1.end);
          },

          _rangeContainsRange: function(range1, range2) {
            // Returns true if range2 is entirely in range1. Equal ranges result in returning true as well.
            return range2.start >= range1.start && range2.end <= range1.end;
          },

          _mergeRanges: function(range1, range2) {
            if (!this._rangesIntersect(range1, range2))
              throw "An attempt to merge non-intersecting ranges";
            return {
              start: Math.min(range1.start, range2.start),
              end: Math.max(range1.end, range2.end)
            };
          },

          addRange: function(start, end) {
            if (!start && !end) {
              this._infiniteRange = true;
              return;
            }
            if (start == end)
              return;
            if (start > end)
              throw "O$._RangeMap.prototype.addRange: start (" + start + ") can't be greater than end(" + end + ")";
            var addedRange = {start: start, end: end};
            var extendedRange = addedRange;
            for (var i = 0; i < this._disjointRanges.length;) {
              var range = this._disjointRanges[i];
              if (!this._rangesIntersect(range, addedRange)) {
                i++;
                continue;
              }
              if (!this._rangeContainsRange(addedRange, range)) {
                extendedRange = this._mergeRanges(extendedRange, range);
              }
              this._disjointRanges.splice(i, 1);
            }
            this._disjointRanges.push(extendedRange);
          },

          isRangeFullyInMap: function(start, end) {
            if (this._infiniteRange)
              return true;
            var testedRange = {start: start, end: end};
            for (var i = 0, count = this._disjointRanges.length; i < count; i++) {
              var range = this._disjointRanges[i];
              if (this._rangeContainsRange(range, testedRange))
                return true;
            }
            return false;
          }
        });
