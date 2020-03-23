package it.cnr.ibimet.entities;

import java.io.Serializable;

public class StatSeries implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private int doy;
    private int year;
    private int count;
    private double mean;
    private double min;
    private double max;
    private double stddev;
    private double q25;
    private double q75;


    public StatSeries(int doy, int year, int count,
                      double mean, double min, double max,
                      double stddev,double q25,double q75) {
        this.doy = doy;
        this.year = year;
        this.count = count;
        this.mean = mean;
        this.min = min;
        this.max = max;
        this.stddev = stddev;
        this.q25 = q25;
        this.q75 = q75;
    }


    public int getDoy() {
        return doy;
    }


    public void setDoy(int doy) {
        this.doy = doy;
    }


    public int getYear() {
        return year;
    }


    public void setYear(int year) {
        this.year = year;
    }


    public int getCount() {
        return count;
    }


    public void setCount(int count) {
        this.count = count;
    }


    public double getMean() {
        return mean;
    }


    public void setMean(double mean) {
        this.mean = mean;
    }


    public double getMin() {
        return min;
    }


    public void setMin(double min) {
        this.min = min;
    }


    public double getMax() {
        return max;
    }


    public void setMax(double max) {
        this.max = max;
    }


    public double getStddev() {
        return stddev;
    }


    public void setStddev(double stddev) {
        this.stddev = stddev;
    }


    public double getQ25() {
        return q25;
    }


    public void setQ25(double q25) {
        this.q25 = q25;
    }


    public double getQ75() {
        return q75;
    }


    public void setQ75(double q75) {
        this.q75 = q75;
    }


}

