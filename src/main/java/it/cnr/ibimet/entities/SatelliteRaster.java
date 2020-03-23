package it.cnr.ibimet.entities;

import java.io.Serializable;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by lerocchi on 01/09/17.
 *
 *
 * SatelliteRaster entity
 */
public class SatelliteRaster implements Serializable {

    private int id_acquisizione;
    private GregorianCalendar dtime;
    private String year;
    private String month;
    private String day;
    private String doy;
    private String datstr;
    private String rasterName;
    private String rasterType;


    public int getId_acquisizione() {
        return id_acquisizione;
    }

    public void setId_acquisizione(int id_acquisizione) {
        this.id_acquisizione = id_acquisizione;
    }

    public GregorianCalendar getDtime() {
        return dtime;
    }

    public void setDtime(GregorianCalendar dtime) {
        this.dtime = dtime;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getRasterType() {
        return rasterType;
    }

    public void setRasterType(String rasterType) {
        this.rasterType = rasterType;
    }

    public String getMonth() {
        return month;
    }

    public String getDoy() {
        return doy;
    }

    public void setDoy(String doy) {
        this.doy = doy;
    }

    public String getRasterName() {
        return rasterName;
    }

    public void setRasterName(String rasterName) {
        this.rasterName = rasterName;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDatstr() {
        return datstr;
    }

    public void setDatstr(String datstr) {
        this.datstr = datstr;
    }

    public SatelliteRaster(){
        this.id_acquisizione = -1;
        this.month = "0";
        this.day = "0";
        this.year = "0";
    }

    public SatelliteRaster(int id_acquisizione,
                           GregorianCalendar dtime, String rasterName){

        this.id_acquisizione = id_acquisizione;
        this.dtime = dtime;

        this.year = ""+dtime.get(Calendar.YEAR);
        this.month = ""+(dtime.get(Calendar.MONTH) + 1);
        this.day = ""+dtime.get(Calendar.DAY_OF_MONTH);
        this.doy = ""+dtime.get(Calendar.DAY_OF_YEAR);
        SimpleDateFormat formatter=new SimpleDateFormat("dd-MM-yyyy");
        this.datstr = formatter.format( this.dtime.getTime() );
        this.rasterType = rasterName.toLowerCase();
        this.rasterName = rasterName + " - " + this.datstr;
    }



}
