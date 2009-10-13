package org.openfaces.taglib.jsp.input;

import org.openfaces.taglib.jsp.AbstractComponentJspTag;
import org.openfaces.taglib.internal.input.SelectOneRadioItemsTag;

import javax.el.ValueExpression;

/**
 * Author: Oleg Marshalenko
 * Date: Sep 18, 2009
 * Time: 3:30:51 PM
 */
public class SelectOneRadioItemsJspTag  extends AbstractComponentJspTag {

    public SelectOneRadioItemsJspTag() {
        super(new SelectOneRadioItemsTag());
    }

    public void setValue(ValueExpression value) {
        getDelegate().setPropertyValue("value", value);
    }
}
