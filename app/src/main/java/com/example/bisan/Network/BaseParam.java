package com.example.bisan.Network;

public class BaseParam {

    private String ParamName,ParamID;
    private String Type;
    private String DefaultVal,MaxLength,Val;
    private boolean isPrimary;

    public BaseParam(String name, String id,String Val, String type, String defVal, boolean isPrimary){
        this.ParamName=name;
        this.ParamID=id;
        this.Val=Val;
        this.Type=type;
        this.DefaultVal=defVal;
        this.isPrimary=isPrimary;
    }

    public BaseParam(String name, String id,String Val, String type,String maxLength, String defVal, boolean isPrimary){
        this.ParamName=name;
        this.ParamID=id;
        this.Val=Val;
        this.Type=type;
        this.DefaultVal=defVal;
        this.isPrimary=isPrimary;
        this.MaxLength=maxLength;
    }

    public String getVal() {
        return Val;
    }

    public String getDefaultVal() {
        return DefaultVal;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public String getMaxLength() {
        return MaxLength;
    }

    public String getParamID() {
        return ParamID;
    }

    public String getParamName() {
        return ParamName;
    }

    public String getType() {
        return Type;
    }

    public void setVal(String val) {
        Val = val;
    }
}
