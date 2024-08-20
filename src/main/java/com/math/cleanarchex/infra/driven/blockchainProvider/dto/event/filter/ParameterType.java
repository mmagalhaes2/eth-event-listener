package com.math.cleanarchex.infra.driven.blockchainProvider.dto.event.filter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.math.cleanarchex.infra.handler.exception.ValidationException;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class ParameterType {

    public static final String INT = "INT";
    public static final String UINT = "UINT";
    public static final String ADDRESS = "ADDRESS";
    public static final String BYTES = "BYTES";
    public static final String BYTE = "BYTE";
    public static final String BOOL = "BOOL";
    public static final String STRING = "STRING";

    private static final Map<String, SupportedType> SUPPORTED_TYPES = new HashMap<>();

    static {
        SUPPORTED_TYPES.put(UINT, new SupportedType(UINT, 256, 8));
        SUPPORTED_TYPES.put(INT, new SupportedType(INT, 256, 8));
        SUPPORTED_TYPES.put(UINT, new SupportedType(UINT));
        SUPPORTED_TYPES.put(INT, new SupportedType(INT));
        SUPPORTED_TYPES.put(ADDRESS, new SupportedType(ADDRESS));
        SUPPORTED_TYPES.put(BYTES, new SupportedType(BYTES, 32, 1));
        SUPPORTED_TYPES.put(BYTE, new SupportedType(BYTE));
        SUPPORTED_TYPES.put(BOOL, new SupportedType(BOOL));
        SUPPORTED_TYPES.put(STRING, new SupportedType(STRING));
    }

    @JsonValue
    private String type;

    public ParameterType(String type) {
        setType(type);
    }

    @JsonCreator
    public static ParameterType build(String type) {
        final ParameterType paramType = new ParameterType();
        paramType.setType(type);

        return paramType;
    }

    public void setType(String type) {
        validateType(type);
        this.type = type;
    }

    //TODO regexp
    private void validateType(String type) {
        final String prefix = getPrefix(type);

        if (SUPPORTED_TYPES.containsKey(prefix)) {
            final SupportedType supportedType = SUPPORTED_TYPES.get(prefix);

            //Get size suffix, ignoring arrays
            if (type.contains("[")) {
                final String sizeString = type
                        .substring(0, type.indexOf("["))
                        .replace(prefix, "");

                if (supportedType.getMaxSizeSuffix() != null) {
                    try {
                        final Integer sizeSuffix = Integer.parseInt(sizeString);

                        if (sizeSuffix % supportedType.getSizeInterval() != 0
                                || sizeSuffix > supportedType.getMaxSizeSuffix()) {
                            throw new ValidationException("Size suffix specified is not supported");
                        }
                    } catch (NumberFormatException t) {
                        throw new ValidationException("Size suffix is not a valid number");
                    }
                }
            }
        }
    }

    private String getPrefix(String type) {
        return type
                .replaceAll("\\D", "")
                .replace("[", "")
                .replace("]", "");
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class SupportedType {

        private String prefix;
        private Integer maxSizeSuffix;
        private Integer sizeInterval;

        public SupportedType(String prefix) {
            this.prefix = prefix;
        }
    }
}
