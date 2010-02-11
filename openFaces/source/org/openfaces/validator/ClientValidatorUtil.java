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

package org.openfaces.validator;

import org.openfaces.component.validation.VerifiableComponent;
import org.openfaces.util.RawScript;
import org.openfaces.util.Script;
import org.openfaces.util.ScriptBuilder;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.*;
import javax.faces.validator.DoubleRangeValidator;
import javax.faces.validator.LengthValidator;
import javax.faces.validator.LongRangeValidator;
import javax.faces.validator.Validator;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Vladimir Korenev
 */
public class ClientValidatorUtil {
    public static boolean validatorSupportsClientValidation(Validator validator) {
        return validator instanceof DoubleRangeValidator ||
                validator instanceof LengthValidator ||
                validator instanceof LongRangeValidator ||
                validator instanceof AbstractCustomValidator;
    }

    public static boolean converterSupportsClientValidation(Converter converter) {
        return converter instanceof ByteConverter ||
                converter instanceof ShortConverter ||
                converter instanceof IntegerConverter ||
                converter instanceof LongConverter ||
                converter instanceof DoubleConverter ||
                converter instanceof FloatConverter ||
                converter instanceof BigIntegerConverter ||
                converter instanceof BigDecimalConverter ||
                converter instanceof NumberConverter ||
                converter instanceof DateTimeConverter;
    }


    public static Script getScriptAddMessageById(FacesMessage message, String clientId) {
        if (message == null)
            return null;
        if (clientId == null)
            return null;

        Map<String,Object> requestMap = FacesContext.getCurrentInstance().getExternalContext().getRequestMap();
        String messagesKey = "org.openfaces.validator.ClientValidatorUtil.renderedMessages";
        List renderedMessages = (List) requestMap.get(messagesKey);
        if (renderedMessages == null) {
            renderedMessages = new ArrayList();
            requestMap.put(messagesKey, renderedMessages);
        }
        if (renderedMessages.contains(message))
            return new RawScript("");
        renderedMessages.add(message);

        return new ScriptBuilder().functionCall("O$.addMessageById",
                clientId,
                message.getSummary(),
                message.getDetail(),
                getMessageSeverity(message)).semicolon();
    }

    public static Script getScriptAddGlobalMessage(FacesMessage message) {
        if (message == null)
            return null;
        return new ScriptBuilder().functionCall("O$.addGlobalMessage",
                message.getSummary(),
                message.getDetail(),
                getMessageSeverity(message)).semicolon();
    }

    public static String getMessageSeverity(FacesMessage message) {
        if (message == null)
            return null;
        if (message.getSeverity() == null)
            return "info";
        String severity = message.getSeverity().toString();
        if (severity == null)
            return "info";
        int i = severity.indexOf(" ");
        if (i == -1)
            return severity.toLowerCase();
        return severity.substring(0, i).toLowerCase();
    }

    public static ClientValidator getClientValidator(Validator validator) {
        if (validator instanceof LengthValidator) {
            LengthValidator lengthValidator = (LengthValidator) validator;
            LengthClientValidator lengthClientValidator = new LengthClientValidator();
            lengthClientValidator.setLengthValidator(lengthValidator);
            return lengthClientValidator;
        }
        if (validator instanceof DoubleRangeValidator) {
            DoubleRangeValidator doubleRangeValidator = (DoubleRangeValidator) validator;
            DoubleRangeClientValidator doubleRangeClientValidator = new DoubleRangeClientValidator();
            doubleRangeClientValidator.setDoubleRangeValidator(doubleRangeValidator);
            return doubleRangeClientValidator;
        }
        if (validator instanceof LongRangeValidator) {
            LongRangeValidator longRangeValidator = (LongRangeValidator) validator;
            LongRangeClientValidator longRangeClientValidator = new LongRangeClientValidator();
            longRangeClientValidator.setLongRangeValidator(longRangeValidator);
            return longRangeClientValidator;
        }
        if (validator instanceof AbstractCustomValidator) {
            return (AbstractCustomValidator) validator;
        }
        return null;
    }

    public static ClientValidator getClientValidator(Converter converter, UIComponent component) {
        if (converter instanceof ByteConverter) {
            return new ByteConverterClientValidator();
        } else if (converter instanceof ShortConverter) {
            return new ShortConverterClientValidator();
        } else if (converter instanceof IntegerConverter) {
            return new IntegerConverterClientValidator();
        } else if (converter instanceof LongConverter) {
            return new LongConverterClientValidator();
        } else if (converter instanceof DoubleConverter) {
            return new DoubleConverterClientValidator();
        } else if (converter instanceof FloatConverter) {
            return new FloatConverterClientValidator();
        } else if (converter instanceof BigIntegerConverter) {
            return new BigIntegerConverterClientValidator();
        } else if (converter instanceof BigDecimalConverter) {
            return new BigDecimalConverterClientValidator();
        } else if (converter instanceof NumberConverter) {
            NumberConverterClientValidator numberConverterValidator = new NumberConverterClientValidator();
            numberConverterValidator.setNumberConverter((NumberConverter) converter);
            return numberConverterValidator;
        } else if (converter instanceof DateTimeConverter) {
            return getDateTimeClientConverter((DateTimeConverter) converter);
        }
        return null;
    }

    private static ClientValidator getDateTimeClientConverter(DateTimeConverter converter) {
        DateTimeConverterClientValidator dateTimeConverterClientValidator = new DateTimeConverterClientValidator();
        dateTimeConverterClientValidator.setDateTimeConverter(converter);
        return dateTimeConverterClientValidator;
    }

    public static Set<ClientValidator> getClientValidators(VerifiableComponent component) {
        if (component == null)
            return null;

        Set<ClientValidator> clientValidators = new HashSet<ClientValidator>();
        if (component.isRequired())
            clientValidators.add(new RequiredClientValidator());

        if (component.getConverter() != null) {
            ClientValidator clientValidator = getClientValidator(component.getConverter(), component.getComponent());
            if (clientValidator != null)
                clientValidators.add(clientValidator);
        }

        if (component.getValidators() != null) {
            for (Validator validator : component.getValidators()) {
                ClientValidator clientValidator = getClientValidator(validator);
                if (clientValidator != null)
                    clientValidators.add(clientValidator);
            }
        }

        if (clientValidators.size() == 0) clientValidators = null;

        return clientValidators;
    }

    public static Set<VerifiableComponent> getExcludedFromAutoValidation(Set<VerifiableComponent> components) {
        if (components == null)
            return null;
        if (components.size() == 0)
            return null;

        Set<VerifiableComponent> excludedComponents = new HashSet<VerifiableComponent>();
        for (VerifiableComponent component : components) {
            if (component.isExcludeFromAutoValidation())
                excludedComponents.add(component);
        }

        return excludedComponents;

    }
}
