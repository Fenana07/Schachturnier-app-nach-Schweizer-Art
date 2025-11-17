package org.swisssystem.entity.enums;

public enum ResultCode {

    ONE_ZERO("1-0"),
    ZERO_ONE("0-1"),
    HALF_HALF("1/2-1/2"),
    F1_0("F1-0"),
    ZERO_F1("0-F1"),
    BYE("BYE"),
    ZERO_ZERO("0-0");

    private final String code;

    ResultCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return code;
    }
}
