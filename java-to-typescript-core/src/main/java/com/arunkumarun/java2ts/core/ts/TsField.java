package com.arunkumarun.java2ts.core.ts;

public class TsField {
    private String fieldName;
    private String fieldType;
    private String accessModifier;
    private boolean isOptional;
    private boolean isStatic;
    private boolean isReadonly;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getAccessModifier() {
        return accessModifier;
    }

    public void setAccessModifier(String accessModifier) {
        this.accessModifier = accessModifier;
    }

    public boolean isOptional() {
        return isOptional;
    }

    public void setOptional(boolean optional) {
        isOptional = optional;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean aStatic) {
        isStatic = aStatic;
    }

    public boolean isReadonly() {
        return isReadonly;
    }

    public void setReadonly(boolean readonly) {
        isReadonly = readonly;
    }
}
