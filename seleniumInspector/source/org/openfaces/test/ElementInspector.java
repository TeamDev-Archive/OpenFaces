/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2009, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.test;

import com.thoughtworks.selenium.Selenium;
import junit.framework.Assert;
import org.openfaces.test.openfaces.LoadingMode;

import java.awt.*;
import java.util.AbstractList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The purpose of ElementInspector is to provide easy means for inspecting client-side DOM elements in Selenium
 * functional tests. An instance of ElementInspector is bound to a specific DOM node that is specified during the
 * element's creation.
 *
 * @author Dmitry Pikhulya
 */
public abstract class ElementInspector {

    protected Selenium getSelenium() {
        return SeleniumTestCase.getSelenium();
    }

    /**
     * @return a selenium inspector string that can be used in the ordinary methods of Selenium object.
     */
    public String asSeleniumLocator() {
        return "internalscript=" + getElementReferenceExpression();
    }

    /**
     * @return expression that evaluates to the inspected element. This expression is evaluated by the Selenium.getEval
     *         method and it is executed in context of the "selenium" object, not the tested window. Use "window" to refer to the
     *         tested window.
     */
    public abstract String getElementReferenceExpression();

    public String toString() {
        return getElementReferenceExpression();
    }

    protected String evalSeleniumInspectorExpression(String expression) {
        Selenium selenium = getSelenium();
        String fullExpression = "this." + expression;
        try {
            return selenium.getEval(fullExpression);
        } catch (RuntimeException e) {
            throw new RuntimeException("Error evaluating selenium expression: " + fullExpression, e);
        }
    }

    // ----------------------------------------- Element inspection/navigation methods

    /**
     * @return true if the element referred to by this element inspector really exists in browser's DOM
     */
    public boolean elementExists() {
        Selenium selenium = getSelenium();
        String elementExistsStr = selenium.getEval("!!(" + getElementReferenceExpression() + ")");
        return Boolean.valueOf(elementExistsStr);
    }

    public List<ElementInspector> childNodes() {
        return getElementsByScript("childNodes");
    }

    public List<ElementInspector> childNodesByName(String nodeName) {
        final String subElementsRetrievalScript = "this.getQ__getChildNodesWithNames(" + getElementReferenceExpression() + ", ['" + nodeName + "'])";
        String nodeCountStr = getSelenium().getEval(subElementsRetrievalScript + ".length");
        final int nodeCount = Integer.parseInt(nodeCountStr);
        return new AbstractList<ElementInspector>() {
            public ElementInspector get(int index) {
                return new ElementByExpressionInspector(subElementsRetrievalScript + "[" + index + "]");
            }

            public int size() {
                return nodeCount;
            }
        };
    }

    /**
     * Executes the JavaScript getElementsByTagName(tagName) function, and returns a list of corresponding element inspectors.
     */
    public List<ElementInspector> getElementsByTagName(String tagName) {
        final String listRetrievalScript = "getElementsByTagName('" + tagName + "')";
        return getElementsByScript(listRetrievalScript);
    }

    protected List<ElementInspector> getElementsByScript(final String listRetrievalScript) {
        final int nodeCount = evalIntExpression(listRetrievalScript + ".length");
        return new AbstractList<ElementInspector>() {
            public ElementInspector get(int index) {
                return new SubElementByExpressionInspector(ElementInspector.this, listRetrievalScript + "[" + index + "]");
            }

            public int size() {
                return nodeCount;
            }
        };
    }

    /**
     * Returns an ElementInspector object for a sub-element defined by the path relative to the element represented by
     * this ElementInspector. Note that element path is NOT specified as XPath, but
     * rather as a path in the format "tagName/subTagName/subSubTagName/etc"; tag name indexes are also allowed, e.g.
     * "tbody/tr[3]/td[0]/input". This path is relative to the element that this ElementInspector is associated with.
     */
    public ElementInspector subElement(String subElementPath) { // todo: add sub-element(s) search method(s) by selenium selectors. will they make this method obsolete?
        return new SubElementByPathInspector(this, subElementPath);
    }

    public boolean hasChildNodes() {
        return evalBooleanExpression("hasChildNodes()");
    }

    public ElementInspector firstChild() {
        return new SubElementByExpressionInspector(this, "firstChild");
    }

    public ElementInspector lastChild() {
        return new SubElementByExpressionInspector(this, "lastChild");
    }

    public ElementInspector nextSibling() {
        return new SubElementByExpressionInspector(this, "nextSibling");
    }

    public ElementInspector previousSibling() {
        return new SubElementByExpressionInspector(this, "previousSibling");
    }

    public ElementInspector parentNode() {
        return new SubElementByExpressionInspector(this, "parentNode");
    }


    /**
     * @param elementExpression The property of the element, or function call over the element, or a chain of
     *                          properties/function calls that should be evaluated, e.g. "checked", "style.width", or "_getSelectedItems().length"
     * @return the evaluated value
     * @see #evalBooleanExpression
     * @see #evalIntExpression
     */
    public String evalExpression(String elementExpression) {
        Selenium selenium = getSelenium();
        String fullExpression = getElementReferenceExpression() + "." + elementExpression;
        try {
            return selenium.getEval(fullExpression);
        } catch (RuntimeException e) {
            throw new RuntimeException("Error evaluating Selenium expression: " + fullExpression, e);
        }
    }

    /**
     * @param elementExpression The property of the element, or function call over the element, or a chain of
     *                          properties/function calls that should be evaluated, e.g. "checked", "_getContent().isVisible()"
     * @return the result of expression evaluation converted to boolean value according to JavaScript rules
     * @see #evalExpression
     */
    public boolean evalBooleanExpression(String elementExpression) {
        Selenium selenium = getSelenium();
        String fullExpression = "!!(" + getElementReferenceExpression() + "." + elementExpression + ")";
        try {
            return Boolean.parseBoolean(selenium.getEval(fullExpression));
        } catch (RuntimeException e) {
            throw new RuntimeException("Error evaluating Selenium expression: " + fullExpression, e);
        }
    }

    /**
     * @param elementExpression The property of the element, or function call over the element, or a chain of
     *                          properties/function calls that should be evaluated, e.g. "checked", "_getContent().isVisible()"
     * @return the result of expression evaluation converted to int
     * @see #evalExpression
     */
    public int evalIntExpression(String elementExpression) {
        String stringValue = evalExpression(elementExpression);
        return Integer.parseInt(stringValue);
    }

    protected String executeSeleniumCommand(String command, String locator, String[] params) {
        String commandMethodName = "do" + command.substring(0, 1).toUpperCase() + command.substring(1);
        StringBuffer commandInvocationScript = new StringBuffer(commandMethodName);
        commandInvocationScript.append(nullOrJsString(locator));
        for (String param : params) {
            commandInvocationScript.append(",").append(nullOrJsString(param));
        }
        commandInvocationScript.append(")");

        return evalSeleniumInspectorExpression(commandInvocationScript.toString());
    }

    /**
     * @return evaluates "nodeName" property of the inspected element. It returns the lower-case tag name for tag
     *         nodes, "#text" for text node, "#comment" for comment nodes, and "#document" for document nodes.
     */
    public String nodeName() {
        return evalExpression("nodeName").toLowerCase();
    }

    public boolean isTextNode() {
        return "#text".equals(nodeName());
    }

    public boolean isCommentNode() {
        return "#comment".equals(nodeName());
    }

    public boolean isDocumentNode() {
        return "#document".equals(nodeName());
    }

    /**
     * @return evaluates "nodeValue" property of the inspected element
     */
    public String nodeValue() {
        return evalExpression("nodeValue");
    }

    /**
     * @return element id
     */
    public String id() {
        return evalExpression("id");
    }

    public String text() {
        return evalSeleniumInspectorExpression("getQ__getNodeText(" + getElementReferenceExpression() + ")");
    }

    public String attribute(String attributeName) {
        return evalExpression("getAttribute('" + attributeName + "')");
    }

    public String className() {
        return evalExpression("className");
    }

    public String calculateStyleProperty(String propertyName) {
        return evalSeleniumInspectorExpression("getQ__calculateElementStyleProperty(" + getElementReferenceExpression() + ", '" + propertyName + "')");
    }

    public boolean isVisible() {
        return getSelenium().isVisible(asSeleniumLocator());
    }

    public Dimension size() {
        String sizeStr = evalSeleniumInspectorExpression("getQ__getElementSize(" + getElementReferenceExpression() + ")");
        String[] sizeArr = sizeStr.split(",");
        int width = Integer.parseInt(sizeArr[0]);
        int height = Integer.parseInt(sizeArr[1]);
        return new Dimension(width, height);
    }

    public Point position() {
        String positionStr = evalSeleniumInspectorExpression("getQ__getElementPos(" + getElementReferenceExpression() + ")");
        String[] positionArr = positionStr.split(",");
        int x = Integer.parseInt(positionArr[0]);
        int y = Integer.parseInt(positionArr[1]);
        return new Point(x, y);
    }

    public Rectangle rectangle() {
        String rectangleStr = evalSeleniumInspectorExpression("getQ__getElementRect(" + getElementReferenceExpression() + ")");
        String[] positionArr = rectangleStr.split(",");
        int x = Integer.parseInt(positionArr[0]);
        int y = Integer.parseInt(positionArr[1]);
        int width = Integer.parseInt(positionArr[2]);
        int height = Integer.parseInt(positionArr[3]);
        return new Rectangle(x, y, width, height);
    }

    // ----------------------------------------- Element manipulation methods

    /**
     * Fires event with the specified name over this element.
     *
     * @param eventName name of the event that should be fired. Event name should start with "on" prefix,
     *                  e.g. "onclick", "onkeypress", etc.
     */
    // todo: see if there's a good argument to make events without "on" prefix, like in Selenium's fireEvent method
    public void fireEvent(String eventName) {
        fireEvent(eventName, true);
    }

    protected void fireEvent(String eventName, boolean checkElementExistence) {
        if (checkElementExistence)
            assertElementExists();
        if (!eventName.startsWith("on"))
            throw new IllegalArgumentException("eventName should start with 'on' prefix: " + eventName);
        eventName = eventName.substring(2);
        getSelenium().fireEvent(asSeleniumLocator(), eventName);
    }

    public void click() {
        assertElementExists();
        getSelenium().click(asSeleniumLocator());
    }

    public void clickAndWait() {
        clickAndWait(LoadingMode.SERVER);
    }

    //todo: replace using LoadingMode with some generic waiting mechanism to decopule ElementInspector from OpenFaces Ajax
    public void clickAndWait(LoadingMode loadingMode) {
        click();
        SeleniumTestCase.waitForLoadCompletion(loadingMode);
    }

    public void doubleClick() {
        assertElementExists();
        getSelenium().doubleClick(asSeleniumLocator());
    }

    public void mouseDown() {
        getSelenium().mouseDown(asSeleniumLocator());
    }

    public void mouseUp() {
        getSelenium().mouseUp(asSeleniumLocator());
    }


    public void mouseOver() {
        getSelenium().mouseOver(asSeleniumLocator());
    }

    public void mouseMove() {
        getSelenium().mouseMove(asSeleniumLocator());
    }

    public void mouseOut() {
        getSelenium().mouseOut(asSeleniumLocator());
    }

    public void dragAndDrop(int moveX, int moveY) {
        String movementString = String.valueOf(moveX) + ',' + String.valueOf(moveY);
        getSelenium().dragAndDrop(asSeleniumLocator(), movementString);
    }


    public void focus() {
        getSelenium().focus(asSeleniumLocator());
    }

    public void keyDown(char character) {
        getSelenium().keyDown(asSeleniumLocator(), String.valueOf(character));
    }

    public void keyDown(int keyCode) {
        getSelenium().keyDown(asSeleniumLocator(), "\\" + keyCode);
    }

    public void keyUp(char character) {
        getSelenium().keyUp(asSeleniumLocator(), String.valueOf(character));
    }

    public void keyUp(int keyCode) {
        getSelenium().keyUp(asSeleniumLocator(), "\\" + keyCode);
    }

    public void keyPress(char character) {
        getSelenium().keyPress(asSeleniumLocator(), String.valueOf(character));
    }

    public void keyPress(int keyCode) {
        getSelenium().keyPress(asSeleniumLocator(), "\\" + keyCode);
    }

    public void setCursorPosition(int position) {
        getSelenium().setCursorPosition(asSeleniumLocator(), String.valueOf(position));
    }

    public String[] selectOptions() {
        return getSelenium().getSelectOptions(asSeleniumLocator());
    }

    public void selectByLabel(String label) {
        getSelenium().select(asSeleniumLocator(), "label=" + label);
    }
    // ----------------------------------------- Assert methods

    public void assertElementExists() {
        // todo: ubiquitious checking for element's existence can significantly affect performance, check whether the "silent" (no checking) mode is required
        assertElementExists(true);
    }

    public void assertElementExists(boolean exists) {
        Assert.assertEquals("Element doesn't exist: " + this, exists, elementExists());
    }

    public void assertExpressionEquals(String expression, String expectedValue) {
        Assert.assertEquals("Checking expression evaluation result. Expression: " + expression + "; element: " + this,
                expectedValue, evalExpression(expression));
    }

    public void assertExpressionEquals(String expression, int expectedValue) {
        Assert.assertEquals("Checking expression evaluation result. Expression: " + expression + "; element: " + this,
                expectedValue, evalIntExpression(expression));
    }

    public void assertExpressionEquals(String expression, int expectedValue, int allowedError) {
        int actualValue = evalIntExpression(expression);
        Assert.assertTrue("Checking expression evaluation result. Expression: " + expression + "; element: " + this +
                "; expected: " + expectedValue + "; but was: " + actualValue,
                Math.abs(actualValue - expectedValue) <= allowedError);
    }

    public void assertExpressionEquals(String expression, boolean expectedValue) {
        Assert.assertEquals("Checking expression evaluation result. Expression: " + expression + "; element: " + this,
                expectedValue, evalBooleanExpression(expression));
    }

    public void assertExpressionStartsWith(String expression, String expectedStringStart) {
        String evaluationResult = evalExpression(expression);
        int end = expectedStringStart.length();
        if (end > evaluationResult.length())
            end = evaluationResult.length();
        String resultSubstring = evaluationResult.substring(0, end);
        Assert.assertEquals("Checking expression evaluation result. Expression: " + expression + "; element: " + this,
                expectedStringStart, resultSubstring);
    }

    public void assertAttribute(String attributeName, String expectedAttributeValue) {
        Assert.assertEquals("Checking value for attribute: " + attributeName + "; element: " + this,
                expectedAttributeValue, attribute(attributeName));
    }

    public void assertAttributeStartsWith(String attributeName, String expectedStringStart) {
        String evaluationResult = attribute(attributeName);
        int end = expectedStringStart.length();
        if (end > evaluationResult.length())
            end = evaluationResult.length();
        String resultSubstring = evaluationResult.substring(0, end);
        Assert.assertEquals("Checking string start for attribute: " + attributeName + "; element: " + this,
                expectedStringStart, resultSubstring);
    }

    public void assertNodeName(String expectedNodeName) {
        Assert.assertEquals("Checking node name at " + this, expectedNodeName.toLowerCase(), nodeName());
    }

    public void assertText(String expectedText) {
        Assert.assertEquals("Checking element text at " + this, expectedText, text());
    }

    public void assertSubtext(int startPos, int endPos, String expectedText) {
        Assert.assertEquals("Checking element subtext at " + this, expectedText, text().substring(startPos, endPos));
    }

    public void assertContainsText(String expectedContainedText) {
        Assert.assertTrue("Checking text that element contains at " + this, text().contains(expectedContainedText));
    }

    private void assertStyleProperty(String propertyName, String expectedValue) {
        if (propertyName.equals("border") ||
                propertyName.equals("border-left") ||
                propertyName.equals("border-right") ||
                propertyName.equals("border-top") ||
                propertyName.equals("border-bottom")) {
            // border declaration can't be tested as a whole, so we need to test it on a per-component basis
            assertBorderValue(propertyName, expectedValue);
            return;
        }

        // "background" declaration can't be tested as a whole
        if (propertyName.equals("background")) {
            boolean assumeThisIsColorDeclaration = !expectedValue.contains("url(");
            if (assumeThisIsColorDeclaration)
                propertyName = "background-color";
        }

        if (propertyName.indexOf("color") != -1)
            expectedValue = adaptColorString(expectedValue);
        if (propertyName.equals("font-weight"))
            expectedValue = adaptFontWeightString(expectedValue);
        String actualValue = calculateStyleProperty(propertyName);
        Assert.assertEquals("Checking style property '" + propertyName + "' for element at " + this, expectedValue, actualValue);
    }

    /**
     * Checks whether all of the CSS properties specified in the "style" parameter are actually applied to the inspected
     * element. Note that the "style" parameter should not necessarily include all of the style declarations applied
     * to the element -- it just should include declarations that need to be checked. If at least one of the style
     * property declarations passed in the "style" parameter doesn't match the element's current style, this method will
     * fail with an appropriate message.
     * <p/>
     * This method doesn't just check the value of the element's "style" property, but check's element's computed style
     * including the entire CSS cascade applied to the element.
     * <p/>
     * Note also that not all of the complex css properties can be checked directly with this method. As a solution you
     * might need to split the complex declaration into several subproperty declarations, e.g. you might need
     * to specify "font-size: 12pt; font-family: Arial; font-weight: bold" instead of "font: 12px Arial bold".
     *
     * @param styleDeclaration CSS attribute declarations
     */
    public void assertStyle(String styleDeclaration) {
        String[] propertyDeclarations = styleDeclaration.split(";");
        for (String declaration : propertyDeclarations) {
            declaration = declaration.trim();
            if (declaration.length() == 0)
                continue;
            String[] keyValuePair = declaration.split(":");
            if (keyValuePair.length != 2)
                throw new IllegalArgumentException("Illegal CSS attribute declaration: \"" + declaration + "\" ; it should be in the following format: \"attribute-name: attribute value\"");
            String propertyName = keyValuePair[0].trim();
            String expectedValue = keyValuePair[1].trim();
            assertStyleProperty(propertyName, expectedValue);
        }
    }

    /**
     * Checks the css "border" property correctness.
     *
     * @param value border declaration in the format "width style color", where each part can be either the appropriate
     *              CSS value that should be tested, or a question sign if that part shouldn't be tested.
     *              Examples: "1px solid black", "2px ? ?", or "? none ?"
     */
    private void assertBorderValue(String value) {
        assertBorderValue("border-left", value);
        assertBorderValue("border-top", value);
        assertBorderValue("border-right", value);
        assertBorderValue("border-bottom", value);
    }

    /**
     * Checks the correctness of one of the CSS border sides.
     *
     * @param borderProperty one of the following CSS border properties: border-left, border-top, border-right, border-bottom
     * @param value          border declaration in the format "width style color", where each part can be either the appropriate
     *                       CSS value that should be tested, or a question sign if that part shouldn't be tested.
     *                       Examples: "1px solid black", "2px ? ?", or "? none ?"
     */
    private void assertBorderValue(String borderProperty, String value) {
        if (borderProperty.equals("border")) {
            assertBorderValue(value);
            return;
        }
        String[] values = value.split(" ");
        String width = values[0].equals("?") ? null : values[0];
        String style = values[1].equals("?") ? null : values[1];
        String color = values[2].equals("?") ? null : values[2];
        if (!borderProperty.equalsIgnoreCase("border-left") &&
                !borderProperty.equalsIgnoreCase("border-top") &&
                !borderProperty.equalsIgnoreCase("border-right") &&
                !borderProperty.equalsIgnoreCase("border-bottom"))
            throw new IllegalArgumentException("borderProperty should be one of border-left, border-top, border-right, border-bottom: " + borderProperty);
        if (width != null)
            Assert.assertEquals("Checking " + borderProperty + "-width", width, calculateStyleProperty(borderProperty + "-width"));
        if (style != null)
            Assert.assertEquals("Checking " + borderProperty + "-style", style, calculateStyleProperty(borderProperty + "-style"));
        if (color != null)
            Assert.assertEquals("Checking " + borderProperty + "-color", adaptColorString(color), calculateStyleProperty(borderProperty + "-color"));
    }

    public void assertVisible(boolean visible) {
        Assert.assertEquals("Checking element visibility: " + this, visible, isVisible());
    }

    public void assertWidth(int width) {
        assertWidth(width, 0);
    }

    public void assertWidth(int width, int allowedError) {
        int actualValue = size().width;
        Assert.assertTrue("Checking element width; element: " + this +
                "; expected: " + width + "; but was: " + actualValue,
                Math.abs(actualValue - width) <= allowedError);
    }

    public void assertHeight(int height) {
        assertHeight(height, 0);
    }

    public void assertHeight(int height, int allowedError) {
        int actualValue = size().height;
        Assert.assertTrue("Checking element height; element: " + this +
                "; expected: " + height + "; but was: " + actualValue,
                Math.abs(actualValue - height) <= allowedError);
    }

    public void assertPosition(Point position) {
        Point actualPosition = position();
        Assert.assertEquals("Checking x-position for element: " + this, position.x, actualPosition.x);
        Assert.assertEquals("Checking y-position for element: " + this, position.y, actualPosition.y);
    }

    public void assertPosition(int x, int y) {
        assertPosition(new Point(x, y));
    }

    public void assertSize(Dimension dimension) {
        Dimension actualSize = size();
        Assert.assertEquals("Checking width for element: " + this, dimension.width, actualSize.width);
        Assert.assertEquals("Checking height for element: " + this, dimension.height, actualSize.height);
    }

    public void assertSize(int width, int height) {
        assertSize(new Dimension(width, height));
    }

    // ----------------------------------------- Utility methods

    private static Map<String, String> adaptedColorStrings = new HashMap<String, String>();

    public String adaptColorString(String color) {
        if (color == null || color.trim().length() == 0)
            return color;
        String adaptedColor = adaptedColorStrings.get(color);
        if (adaptedColor == null) {
            Selenium selenium = getSelenium();
            adaptedColor = selenium.getEval("var referenceEl = document.createElement('div'); referenceEl.style.color = '" + color + "';" +
                    "this.page().getCurrentWindow().O$.getElementStyleProperty(referenceEl, 'color');");
            adaptedColorStrings.put(color, adaptedColor);
        }
        return adaptedColor;
    }

    private static Map<String, String> adaptedFontWeightStrings = new HashMap<String, String>();

    public String adaptFontWeightString(String fontWeight) {
        if (fontWeight == null || fontWeight.trim().length() == 0)
            return fontWeight;
        String adaptedFontWeight = adaptedFontWeightStrings.get(fontWeight);
        if (adaptedFontWeight == null) {
            Selenium selenium = getSelenium();
            adaptedFontWeight = selenium.getEval("var referenceEl = document.createElement('div'); referenceEl.style.fontWeight = '" + fontWeight + "';" +
                    "this.page().getCurrentWindow().O$.getElementStyleProperty(referenceEl, 'font-weight');");
            adaptedFontWeightStrings.put(fontWeight, adaptedFontWeight);
        }
        return adaptedFontWeight;
    }


    private String nullOrJsString(String str) {
        if (str == null)
            return "null";
        else
            return '\'' + str + '\'';
    }

    protected static String escapeStringForJSAndQuote(String str) {
        if (str == null)
            return "null";
        return '\'' + escapeStringForJS(str) + '\'';
    }


    protected static String escapeStringForJS(String str) {
        if (str == null)
            return "";

        int len = str.length();
        StringBuffer buf = new StringBuffer(len << 2);
        for (int i = 0; i < len; i++) {
            char chr = str.charAt(i);
            switch (chr) {
                case '\\':
                    buf.append("\\x5c");
                    break;
                case '\'':
                    buf.append("\\x27");
                    break;
                case '\"':
                    buf.append("\\x22");
                    break;
                case '\n':
                    buf.append("\\n");
                    break;
                case '[':
                    buf.append("\\x5b");
                    break;
                case ']':
                    buf.append("\\x5d");
                    break;
                case '\r':
                    buf.append("\\r");
                    break;
                case '<':
                    buf.append("\\x3C");
                    break;
                default:
                    if (((int) chr) >= 0x80) {
                        final String hex = Integer.toString(chr, 16);
                        buf.append("\\u");
                        switch (hex.length()) {
                            case 2:
                                buf.append("00");
                                break;
                            case 3:
                                buf.append('0');
                        }
                        buf.append(hex);
                    } else {
                        buf.append(chr);
                    }
            }
        }

        return buf.toString();
    }

}