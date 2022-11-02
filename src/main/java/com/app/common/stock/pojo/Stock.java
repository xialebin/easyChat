package com.app.common.stock.pojo;

public class Stock {

    private Long amount;

    private Byte Level;

    private Long num;

    private Integer serverNum;

    public Integer getServerNum() {
        return serverNum;
    }

    public void setServerNum(Integer serverNum) {
        this.serverNum = serverNum;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Byte getLevel() {
        return Level;
    }

    public void setLevel(Byte level) {
        Level = level;
    }

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }
}
