package com.math.cleanarchex.infra.driven.blockchainProvider.utils;

import org.modelmapper.ModelMapper;

public class ModelMapperFactory {

    private static ModelMapperFactory INSTANCE;

    private ModelMapperFactory() {
    }

    public static ModelMapperFactory getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ModelMapperFactory();
        }

        return INSTANCE;
    }

    public ModelMapper createModelMapper() {
        return new ModelMapper();
    }
}
