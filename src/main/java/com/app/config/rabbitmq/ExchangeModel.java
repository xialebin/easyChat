package com.app.config.rabbitmq;

public enum ExchangeModel {

    RANDOM("RANDOM"),
    ROOM_ID("ROOM_ID");

    private String model;

    ExchangeModel(String model)
    {
        this.model = model;
    }

    public String getModel()
    {
        return model;
    }

}

