package org.openfaces.taglib.internal.filter;

import org.openfaces.component.filter.FilterProperty;
import org.openfaces.component.filter.FilterType;
import org.openfaces.taglib.internal.AbstractComponentTag;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Natalia Zolochevska
 */
public class FilterPropertyTag extends AbstractComponentTag {

    public String getComponentType() {
        return FilterProperty.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return null;
    }

    @Override
    public void setComponentProperties(FacesContext facesContext, UIComponent component) {
        super.setComponentProperties(facesContext, component);

        setStringProperty(component, "name");
        setObjectProperty(component, "value");
        setEnumerationProperty(component, "type", FilterType.class);

        setPropertyBinding(component, "dataProvider");
        setConverterProperty(facesContext, component, "converter");
        setNumberProperty(component, "maxValue");
        setNumberProperty(component, "minValue");
        setNumberProperty(component, "step");

        setTimeZoneProperty(component, "timeZone");
        setStringProperty(component, "pattern");

        setBooleanProperty(component, "caseSensitive");
        
    }
}