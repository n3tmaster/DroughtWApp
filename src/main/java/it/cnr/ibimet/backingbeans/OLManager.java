package it.cnr.ibimet.backingbeans;

import it.cnr.ibimet.dbutils.SWH4EConst;
import it.cnr.ibimet.dbutils.TDBManager;
import it.cnr.ibimet.entities.ImageType;
import it.cnr.ibimet.entities.SatelliteRaster;
import it.lr.libs.DBManager;
import org.primefaces.PrimeFaces;
import org.primefaces.component.tabview.Tab;

import org.primefaces.event.TabChangeEvent;

import javax.annotation.PostConstruct;
import javax.faces.bean.*;

import java.io.Serializable;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

/**
 * class for OpenLayer client component management
 */
@ManagedBean(name="olManager")
@ViewScoped
public class OLManager implements Serializable,SWH4EConst {

    private String indexType;
    public String getIndexType() {
        return indexType;
    }
    private List<ImageType> imageTypeList;
    private ImageType selectedImageType;
    private boolean valueDefault, valueFree, valueIdentify,value;
    private String the_geom, ol_the_geom;

    @ManagedProperty(value="#{timeLine}")
    private BTimeline bTimeline;



    public List<ImageType> getImageTypeList() {
        return imageTypeList;
    }

    public boolean isValueDefault() {
        return valueDefault;
    }

    public void setValueDefault(boolean valueDefault) {
        this.valueDefault = valueDefault;
    }

    public boolean isValueFree() {
        return valueFree;
    }

    public void setValueFree(boolean valueFree) {
        this.valueFree = valueFree;
    }

    public boolean isValueIdentify() {
        return valueIdentify;
    }

    public void setValueIdentify(boolean valueIdentify) {
        this.valueIdentify = valueIdentify;
    }

    public void setImageTypeList(List<ImageType> imageTypeList) {
        this.imageTypeList = imageTypeList;
    }

    public ImageType getSelectedImageType() {
        return selectedImageType;
    }

    public boolean isValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public void setSelectedImageType(ImageType selectedImageType) {
        this.selectedImageType = selectedImageType;
    }

    public void setIndexType(String indexType) {
        this.indexType = indexType;
    }

    public BTimeline getbTimeline() {
        return bTimeline;
    }

    public String getThe_geom() {
        return the_geom;
    }

    public void setThe_geom(String the_geom) {
        this.the_geom = the_geom;
    }

    public void setbTimeline(BTimeline bTimeline) {
        this.bTimeline = bTimeline;
    }

    @PostConstruct
    public void init(){
        System.out.println("olManager - init");


        valueDefault = true;
        valueFree = false;
        valueIdentify = false;

        //value = false;
        populateRasterTypeList();

        bTimeline.populateAcquisitions(selectedImageType.getId_imgtype(), selectedImageType.getName());

        the_geom = BASE_EXTENT;


        extractRegion();

        System.out.println("GEOM: "+the_geom);

       // RequestContext.getCurrentInstance().execute("initJS('"+BASEURL+"','"+the_geom+"')"); //,'"+ol_the_geom+"')");
        PrimeFaces.current().executeScript("initJS('"+BASEURL+"','"+the_geom+"')"); //,'"+ol_the_geom+"')");

     //   bTimeline.getLast();
    }


    /**
     * callback function that fires Javascript func, changeParamsJS, in order to set parameters for client side gis operations
     */
    public void changeParams(){

        //this.legendOut = NDVI_RES;
        //RequestContext.getCurrentInstance().execute("changeParamsJS('"+imgType+"','"+selectedSatelliteRaster.getYear()+"','"+selectedSatelliteRaster.getMonth()+"','"+selectedSatelliteRaster.getDay()+"')");
        //RequestContext.getCurrentInstance().execute("changeImageJS()");


    }


    /**
     *
     * Extract extent of images
     */
    public void getExtent(){

    }

    /**
     * extract the base polygon for clipping and managing images into OpenLayer map component
     *
     */
    public void extractRegion(){
        TDBManager dsm=null;
        try {

            dsm = new TDBManager("jdbc/ssdb");



            System.out.println("extractRegion");

            String sqlString="select ST_AsText(the_geom) from postgis.region_geoms inner join postgis.regions using (_id_region) where name = ?";

            dsm.setPreparedStatementRef(sqlString);
            dsm.setParameter(DBManager.ParameterType.STRING, BASE_EXTENT, 1);
            dsm.runPreparedQuery();



            if(dsm.next()){

            //    the_geom = URLEncoder.encode(dsm.getString(1), "UTF-8");
                the_geom = dsm.getString(1);
            }


        } catch (Exception e) {

            e.printStackTrace();
        } finally{
            try{
                dsm.closeConnection();
            }catch(Exception e){
                e.getStackTrace();
            }
        }
    }


    /**
     * create Raster type list
     */
    public void populateRasterTypeList(){
        TDBManager dsm=null;
        try {

            dsm = new TDBManager("jdbc/ssdb");



            System.out.println("populateRasterTypeList - connected");

            String sqlString="select id_imgtype, imgtype, description, calculated, info from postgis.imgtypes where output=true order by thisorder";

            dsm.setPreparedStatementRef(sqlString);
            dsm.runPreparedQuery();

            imageTypeList = new ArrayList<ImageType>();

            while(dsm.next()){
                this.imageTypeList.add(new ImageType(dsm.getInteger(1), dsm.getString(2), dsm.getString(3), dsm.getBoolean(4), dsm.getString(5)));
            }

            this.selectedImageType = this.imageTypeList.get(0);


        } catch (Exception e) {

            e.printStackTrace();
        } finally{
            try{
                dsm.closeConnection();
            }catch(Exception e){
                e.getStackTrace();
            }
        }
    }

    /**
     * method for managing tab selection
     * @param event
     */
    public void onChange(TabChangeEvent event) {


        Tab activeTab = event.getTab();

        List<ImageType> result = imageTypeList.stream()
                .filter(item -> item.getDescription()
                        .equals( activeTab.getTitle()))
                .collect(Collectors.toList());

        selectedImageType = result.get(0);
        bTimeline.populateAcquisitions(selectedImageType.getId_imgtype(),selectedImageType.getName());



    }

    public void changeMode(){

        System.out.println("prima: "+value);

        System.out.println("e ora: "+value);
    }


    private void updateComponent() {
        PrimeFaces.current().ajax().update("actDefault");
        PrimeFaces.current().ajax().update("actFree");
        PrimeFaces.current().ajax().update("actIdentify");


    }

    public void activateDefault() {
        this.valueDefault = true;
        this.valueFree = false;
        this.valueIdentify = false;

        updateComponent();
    }

    public void activateFree() {
        this.valueDefault = false;
        this.valueFree = true;
        this.valueIdentify = false;
        updateComponent();
    }

    public void activateIdentify() {
        this.valueDefault = false;
        this.valueFree = false;
        this.valueIdentify = true;
        updateComponent();
    }
}
