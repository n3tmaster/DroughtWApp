package it.cnr.ibimet.entities;

import java.io.Serializable;

public class ImageType implements Serializable {
    private int id_imgtype;
    private String name;
    private String description;
    private String legend;
    private boolean calculated;
    private String calculatedStr;
    private String info;


    private final static String CALCULATED = "(Calculated)";
    private final static String ONTHEFLY = "(On-the-fly)";

    public int getId_imgtype() {
        return id_imgtype;
    }

    public void setId_imgtype(int id_imgtype) {
        this.id_imgtype = id_imgtype;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getLegend() {
        return legend;
    }

    public void setLegend(String legend) {
        this.legend = legend;
    }

    public boolean isCalculated() {
        return calculated;
    }

    public void setCalculated(boolean calculated) {
        this.calculated = calculated;
    }

    public String getCalculatedStr() {
        return calculatedStr;
    }

    public void setCalculatedStr(String calculatedStr) {
        this.calculatedStr = calculatedStr;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ImageType(int id_imgtype, String name, String description){
        this.id_imgtype = id_imgtype;
        this.name       = name;
        this.description=description;
        this.legend = name.toLowerCase()+"_legend.svg";
    }

    public ImageType(int id_imgtype, String name, String description, boolean calculated){
        this(id_imgtype,name,description);
        this.calculated = calculated;
        if(calculated)
            this.calculatedStr=CALCULATED;
        else
            this.calculatedStr=ONTHEFLY;
    }
    public ImageType(int id_imgtype, String name, String description, boolean calculated, String info){
        this(id_imgtype,name,description,calculated);
        this.info = info;

    }
}
