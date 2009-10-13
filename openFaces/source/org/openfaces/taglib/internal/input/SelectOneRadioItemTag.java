package org.openfaces.taglib.internal.input;

import org.openfaces.taglib.internal.AbstractComponentTag;

import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;

/**
 * Author: Oleg Marshalenko
 * Date: Sep 18, 2009
 * Time: 3:32:27 PM
 */
public class SelectOneRadioItemTag extends AbstractComponentTag {
    private static final String COMPONENT_TYPE = "org.openfaces.SelectOneRadioItem";

    public String getComponentType() {
        return COMPONENT_TYPE;
    }

    public String getRendererType() {
        return null;
    }

    @Override
    public void setComponentProperties(FacesContext facesContext, UIComponent component) {
        super.setComponentProperties(facesContext, component);
        setObjectProperty(component, "value");
        setStringProperty(component, "itemLabel");
        setBooleanProperty(component, "disabled");

        setStringProperty(component, "focusedItemStyle");
        setStringProperty(component, "focusedItemClass");
        setStringProperty(component, "selectedItemStyle");
        setStringProperty(component, "selectedItemClass");
        setStringProperty(component, "rolloverItemStyle");
        setStringProperty(component, "rolloverItemClass");
        setStringProperty(component, "pressedItemStyle");
        setStringProperty(component, "pressedItemClass");
    }
}
