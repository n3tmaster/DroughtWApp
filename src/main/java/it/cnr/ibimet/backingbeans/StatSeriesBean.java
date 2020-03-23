package it.cnr.ibimet.backingbeans;

import it.cnr.ibimet.dbutils.TDBManager;
import it.cnr.ibimet.entities.StatSeries;
import it.cnr.ibimet.restutil.HttpURLManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.primefaces.PrimeFaces;
import org.primefaces.model.chart.*;
import sun.util.calendar.Gregorian;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;


@ManagedBean(name="statSeriesBean")
@ViewScoped
public class StatSeriesBean implements Serializable {
    @ManagedProperty(value="#{olManager}")
    private OLManager olManager;
    private LineChartModel model;

    private String polyTxtPassed;
    private String polyTxtPassed2;

    private HttpURLManager httpMng=new HttpURLManager();

    private String retText;
    private String thisPoly;
    private String thisX;
    private String thisY;


    LineChartSeries series1;


    private List<StatSeries> statData;
    private StatSeries selectedStat;


    public String getThisPoly() {
        return thisPoly;
    }

    public void setThisPoly(String thisPoly) {
        this.thisPoly = thisPoly;
    }

    public String getThisX() {
        return thisX;
    }

    public void setThisX(String thisX) {
        this.thisX = thisX;
    }

    public String getThisY() {
        return thisY;
    }

    public void setThisY(String thisY) {
        this.thisY = thisY;
    }

    public LineChartModel getModel() {
        return model;
    }

    public void setModel(LineChartModel model) {
        this.model = model;
    }

    public String getPolyTxtPassed2() {
        return polyTxtPassed2;
    }

    public void setPolyTxtPassed2(String polyTxtPassed2) {
        this.polyTxtPassed2 = polyTxtPassed2;
    }

    public String getPolyTxtPassed() {
        return polyTxtPassed;
    }

    public void setPolyTxtPassed(String polyTxtPassed) {
        this.polyTxtPassed = polyTxtPassed;
    }

    public List<StatSeries> getStatData() {
        return statData;
    }

    public void setStatData(List<StatSeries> statData) {
        this.statData = statData;
    }

    public StatSeries getSelectedStat() {
        return selectedStat;
    }

    public void setSelectedStat(StatSeries selectedStat) {
        this.selectedStat = selectedStat;
    }

    public String getRetText() {
        return retText;
    }

    public void setRetText(String retText) {
        this.retText = retText;
    }

    public OLManager getOlManager() {
        return olManager;
    }

    public void setOlManager(OLManager olManager) {
        this.olManager = olManager;
    }


    @PostConstruct
    public void init(){
        System.out.println("statSeries - init");
        model = new LineChartModel();
        series1 =new LineChartSeries();

        series1.set("0",0.0);
        model.addSeries(series1);
    }


    public void updateStatSeries() {

        try {
            int icount;




            statData.clear();

            httpMng.setUrl("https://droughtsdi.fi.ibimet.cnr.it/dgws/api/calculate/series/"+
                    olManager.getSelectedImageType().getName().toLowerCase()+"/polygon/"+
                    polyTxtPassed
                    +"/srid_from/4326");




            JSONParser parser = new JSONParser();
            String aaa = httpMng.sendGet();
            JSONArray jArray = (JSONArray) parser.parse(aaa);

            for(icount=0; icount<jArray.size(); icount++) {
                JSONObject thiselement = (JSONObject) jArray.get(icount);

                statData.add(new StatSeries(Integer.parseInt(thiselement.get(it.cnr.ibimet.dbutils.SWH4EConst.PARAM_DOY).toString()),
                        Integer.parseInt(thiselement.get(it.cnr.ibimet.dbutils.SWH4EConst.PARAM_YEAR).toString()),
                        Integer.parseInt(thiselement.get(it.cnr.ibimet.dbutils.SWH4EConst.PARAM_COUNT).toString()),
                        Double.parseDouble(thiselement.get(it.cnr.ibimet.dbutils.SWH4EConst.PARAM_MEAN).toString().replace(",",".")),
                        Double.parseDouble(thiselement.get(it.cnr.ibimet.dbutils.SWH4EConst.PARAM_MIN).toString().replace(",",".")),
                        Double.parseDouble(thiselement.get(it.cnr.ibimet.dbutils.SWH4EConst.PARAM_MAX).toString().replace(",",".")),
                        Double.parseDouble(thiselement.get(it.cnr.ibimet.dbutils.SWH4EConst.PARAM_STDDEV).toString().replace(",",".")),
                        Double.parseDouble(thiselement.get(it.cnr.ibimet.dbutils.SWH4EConst.PARAM_Q25).toString().replace(",",".")),
                        Double.parseDouble(thiselement.get(it.cnr.ibimet.dbutils.SWH4EConst.PARAM_Q75).toString().replace(",","."))));



            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void extractSeriesFromPoint(String seriesColor){

        TDBManager dsm=null;

        try{
            dsm = new TDBManager("jdbc/ssdb");
            model.clear();
            series1 = new LineChartSeries();
            series1.setLabel(olManager.getSelectedImageType().getName().toLowerCase());


            model.setSeriesColors(seriesColor);
            String sqlString = "select * from postgis.calculate_stat_series('"+
                    olManager.getSelectedImageType().getName().toLowerCase()+"', ST_GeomFromText('POINT("+this.thisX+" "+this.thisY+")',4326))";



            dsm.setPreparedStatementRef(sqlString);

            System.out.println(sqlString);

            dsm.runPreparedQuery();
            JSONArray jArray= new JSONArray();
            while (dsm.next()) {

                GregorianCalendar gc = new GregorianCalendar();
                gc.set(Calendar.YEAR, dsm.getInteger(2));
                gc.set(Calendar.DAY_OF_YEAR, dsm.getInteger(1));

                series1.set("" + gc.get(Calendar.YEAR) + "-" + String.format("%02d", (gc.get(Calendar.MONTH) + 1)) +
                        "-" + String.format("%02d", (gc.get(Calendar.DAY_OF_MONTH))), dsm.getDouble(3));

            }


            model.setTitle(olManager.getSelectedImageType().getName() + " (" + this.thisX + " " + this.thisY + ")");
            model.setZoom(true);
            model.getAxis(AxisType.Y).setLabel(olManager.getSelectedImageType().getName());

            series1.setShowMarker(false);
            series1.setSmoothLine(true);



            DateAxis axis = new DateAxis("Dates");
            axis.setTickAngle(-50);

            axis.setTickFormat("%d-%m-%Y");
            model.getAxes().put(AxisType.X, axis);

        }catch (Exception e) {

            e.printStackTrace();
        } finally{
            try{
                dsm.closeConnection();
            }catch(Exception e){
                e.getStackTrace();
            }
        }
    }

    public void updateSPISeries() {
        TDBManager dsm=null;
        try {
            int icount;
            String sqlString;



            System.out.println("statSeriesBean - updateSPISeries - " + olManager.getSelectedImageType().getName());

            if(olManager.getSelectedImageType().getName().toLowerCase().matches("spi3") || olManager.getSelectedImageType().getName().toLowerCase().matches("spi6") ||
                    olManager.getSelectedImageType().getName().toLowerCase().matches("spi12")) {


                this.extractSeriesFromPoint("5E62B0, 94403A");
                model.addSeries(series1);
                //  model.addSeries(trend1);
                model.setExtender("chartExtender");
                PrimeFaces current = PrimeFaces.current();
                current.executeScript("PF('graphDlg').show();");

            }else if(olManager.getSelectedImageType().getName().toLowerCase().matches("tci") || olManager.getSelectedImageType().getName().toLowerCase().matches("vci") ||
                    olManager.getSelectedImageType().getName().toLowerCase().matches("evci") || olManager.getSelectedImageType().getName().toLowerCase().matches("vhi") ||
                    olManager.getSelectedImageType().getName().toLowerCase().matches("evhi")) {
                this.extractSeriesFromPoint("5E62B0");
                model.addSeries(series1);
                //  model.addSeries(trend1);
                model.setExtender("chartExtenderVCI");
                PrimeFaces current = PrimeFaces.current();
                current.executeScript("PF('graphDlg').show();");
            }else{

                PrimeFaces current = PrimeFaces.current();
                current.executeScript("PF('dlgError').show();");
            }



        }catch (Exception e) {

            e.printStackTrace();
        }



    }
}
