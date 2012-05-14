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
package org.openfaces.validator;

import org.openfaces.org.json.JSONException;
import org.openfaces.org.json.JSONObject;
import org.openfaces.util.NewInstanceScript;
import org.openfaces.util.Resources;
import org.openfaces.util.MessageUtil;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.NumberConverter;
import java.util.Locale;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class NumberConverterClientValidator extends AbstractClientValidator implements AjaxSupportedConverter {
    private static final String CONVERSION_MESSAGE_ID = "javax.faces.convert.NumberConverter.CONVERSION";

    private NumberConverter numberConverter;

    public NumberConverterClientValidator() {
        addJavascriptLibrary(new ValidationJavascriptLibrary("numberConverterValidator.js"));
        addJavascriptLibrary(new ValidationJavascriptLibrary("requestHelper.js"));
        addJavascriptLibrary(new ValidationJavascriptLibrary("/" + Resources.JSON_JS_PATH));
    }

    public String getJsValidatorName() {
        return "O$._NumberConverterValidator";
    }

    public void setNumberConverter(NumberConverter numberConverter) {
        this.numberConverter = numberConverter;
    }

    @Override
    protected Object[] getJsValidatorParametersAsString(FacesContext context, UIComponent component) {
        Object[] args = {component.getId()};
        FacesMessage message = MessageUtil.getMessage(context, FacesMessage.SEVERITY_ERROR,
                new String[]{CONVERSION_MESSAGE_ID, UIInput.CONVERSION_MESSAGE_ID}, args);

        Locale locale = numberConverter.getLocale();
        return new Object[]{
                message.getSummary(),
                message.getDetail(),
                new NewInstanceScript("O$.NumberConverter",
                        numberConverter.getCurrencyCode(),
                        numberConverter.getCurrencySymbol(),
                        locale,
                        numberConverter.getMaxFractionDigits(),
                        numberConverter.getMaxIntegerDigits(),
                        numberConverter.getMinFractionDigits(),
                        numberConverter.getMinFractionDigits(),
                        numberConverter.getPattern(),
                        numberConverter.getType(),
                        numberConverter.isGroupingUsed(),
                        numberConverter.isIntegerOnly(),
                        this.getClass().getName())
        };
    }


    public Converter getConverter(FacesContext context, JSONObject jConverter) {
        Converter result;

        try {
            Object currencyCode = jConverter.get("currencyCode");
            Object currencySymbol = jConverter.get("currencySymbol");
            Object locale = jConverter.get("locale");
            int maxFractionDigits = jConverter.getInt("maxFractionDigits");
            int maxIntegerDigits = jConverter.getInt("maxIntegerDigits");
            int minFractionDigits = jConverter.getInt("minFractionDigits");
            int minIntegerDigits = jConverter.getInt("minIntegerDigits");
            Object pattern = jConverter.get("pattern");
            Object type = jConverter.get("type");
            boolean groupingUsed = jConverter.getBoolean("groupingUsed");
            boolean integerOnly = jConverter.getBoolean("integerOnly");
            String validatorId = jConverter.getString("validatorId");

            result = context.getApplication().createConverter(validatorId);
            if (result instanceof NumberConverter) {
                //apply properties
                NumberConverter nc = (NumberConverter) result;

                if (currencyCode != JSONObject.NULL && currencyCode.toString().length() > 0)
                    nc.setCurrencyCode(currencyCode.toString());

                if (currencySymbol != JSONObject.NULL && currencySymbol.toString().length() > 0)
                    nc.setCurrencySymbol(currencySymbol.toString());

                if (locale != JSONObject.NULL && locale.toString().length() > 0)
                    nc.setLocale(new Locale(locale.toString()));

                nc.setMaxFractionDigits(maxFractionDigits);
                nc.setMaxIntegerDigits(maxIntegerDigits);

                nc.setMinFractionDigits(minFractionDigits);
                nc.setMinIntegerDigits(minIntegerDigits);

                if (pattern != JSONObject.NULL && pattern.toString().length() > 0)
                    nc.setPattern(pattern.toString());

                if (type != JSONObject.NULL && type.toString().length() > 0)
                    nc.setType(type.toString());

                nc.setGroupingUsed(groupingUsed);
                nc.setIntegerOnly(integerOnly);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return result;
    }


}

