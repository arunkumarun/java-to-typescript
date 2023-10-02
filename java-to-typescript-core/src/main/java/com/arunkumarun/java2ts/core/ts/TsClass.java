package com.arunkumarun.java2ts.core.ts;

import java.util.List;

public class TsClass {
    private String className;
    private boolean isExport;
    private List<TsField> fields;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public boolean isExport() {
        return isExport;
    }

    public void setExport(boolean export) {
        isExport = export;
    }

    public List<TsField> getFields() {
        return fields;
    }

    public void setFields(List<TsField> fields) {
        this.fields = fields;
    }
}
