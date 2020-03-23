package it.cnr.ibimet.backingbeans;


import it.cnr.ibimet.dbutils.TDBManager;
import it.cnr.ibimet.entities.SatelliteRaster;
import org.primefaces.PrimeFaces;
import org.primefaces.component.timeline.TimelineUpdater;

import org.primefaces.model.timeline.TimelineEvent;
import org.primefaces.model.timeline.TimelineModel;
import org.primefaces.event.timeline.TimelineSelectEvent;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;



@ManagedBean(name="timeLine")
@ViewScoped
public class BTimeline implements Serializable {
    private TimelineModel model;
    private boolean selectable = true;
    private boolean zoomable = true;
    private boolean moveable = true;
    private boolean stackEvents = true;
    private String eventStyle = "box";
    private boolean axisOnTop = false;
    private boolean showCurrentTime = true;
    private boolean showNavigation = true;
    private boolean unselectable = false;

    // one day in milliseconds for zoomMin
    private long zoomMin = 1000L * 60 * 60 * 24 * 15;

    // about three months in milliseconds for zoomMax
    private long zoomMax = 1000L * 60 * 60 * 24 * 31 * 12;
    private List<SatelliteRaster> satelliteRasterList;
    private SatelliteRaster selectedRaster;
    private TimelineEvent selectedEvent;
    private boolean firstLoad;
    private Date dF, dI;

    public boolean isUnselectable() {
        return unselectable;
    }

    public Date getdF() {
        return dF;
    }


    public SatelliteRaster getSelectedRaster() {
        return selectedRaster;
    }

    public void setSelectedRaster(SatelliteRaster selectedRaster) {
        this.selectedRaster = selectedRaster;
    }

    public TimelineEvent getSelectedEvent() {
        return selectedEvent;
    }

    public Date getdI() {
        return dI;
    }

    public void setSelectedEvent(TimelineEvent selectedEvent) {
        this.selectedEvent = selectedEvent;
    }

    public void setUnselectable(boolean unselectable) {
        this.unselectable = unselectable;
    }

    public TimelineModel getModel() {
        return model;
    }

    public void setModel(TimelineModel model) {
        this.model = model;
    }

    public long getZoomMin() {
        return zoomMin;
    }

    public void setZoomMin(long zoomMin) {
        this.zoomMin = zoomMin;
    }

    public long getZoomMax() {
        return zoomMax;
    }

    public void setZoomMax(long zoomMax) {
        this.zoomMax = zoomMax;
    }

    public boolean isSelectable() {
        return selectable;
    }

    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }

    public boolean isZoomable() {
        return zoomable;
    }

    public void setZoomable(boolean zoomable) {
        this.zoomable = zoomable;
    }

    public boolean isMoveable() {
        return moveable;
    }

    public void setMoveable(boolean moveable) {
        this.moveable = moveable;
    }

    public boolean isStackEvents() {
        return stackEvents;
    }

    public void setStackEvents(boolean stackEvents) {
        this.stackEvents = stackEvents;
    }

    public String getEventStyle() {
        return eventStyle;
    }

    public void setEventStyle(String eventStyle) {
        this.eventStyle = eventStyle;
    }

    public boolean isAxisOnTop() {
        return axisOnTop;
    }

    public void setAxisOnTop(boolean axisOnTop) {
        this.axisOnTop = axisOnTop;
    }

    public boolean isShowCurrentTime() {
        return showCurrentTime;
    }

    public void setShowCurrentTime(boolean showCurrentTime) {
        this.showCurrentTime = showCurrentTime;
    }

    public boolean isShowNavigation() {
        return showNavigation;
    }

    public void setShowNavigation(boolean showNavigation) {
        this.showNavigation = showNavigation;
    }

    @PostConstruct
    protected void initialize() {
        model = new TimelineModel();

        satelliteRasterList = new ArrayList<SatelliteRaster>();
        selectedRaster = new SatelliteRaster();

        firstLoad=true;


        GregorianCalendar newGregCal = new GregorianCalendar();
        //GregorianCalendar newGregCal2 = new GregorianCalendar(2018,8,20);



        newGregCal.add(Calendar.DAY_OF_YEAR, -120);
        dI = newGregCal.getTime();

        newGregCal.add(Calendar.DAY_OF_YEAR, 127);
        dF = newGregCal.getTime();
    }

    public void onSelect(TimelineSelectEvent e) {
        TimelineEvent timelineEvent = e.getTimelineEvent();


        List<SatelliteRaster> result = satelliteRasterList.stream()
                .filter(item -> item.getRasterName()
                        .equals( timelineEvent.getData().toString()))
                .collect(Collectors.toList());
        selectedRaster = result.get(0);
        selectedEvent = timelineEvent;
        firstLoad = false;
      //  RequestContext.getCurrentInstance().execute("changeParamsJS('"+selectedRaster.getRasterType()+"','"+selectedRaster.getYear()+"','"+selectedRaster.getMonth()+"','"+selectedRaster.getDay()+"','"+selectedRaster.getDoy()+"')");
        PrimeFaces.current().executeScript("changeParamsJS('"+selectedRaster.getRasterType()+"','"+selectedRaster.getYear()+"','"+selectedRaster.getMonth()+"','"+selectedRaster.getDay()+"','"+selectedRaster.getDoy()+"')");

//        RequestContext.getCurrentInstance().execute("changeImageJS()");
       PrimeFaces.current().executeScript("changeImageJS()");

    }


    /**
     * create acquisition list
     */
    public void populateAcquisitions(int id_imgtype, String name){
        satelliteRasterList.clear();
        model.clear();
        TDBManager dsm=null;
        try {

            dsm = new TDBManager("jdbc/ssdb");

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

            System.out.println("populate acquisitions for "+id_imgtype);


            String sqlString="select id_acquisizione, dtime from postgis.acquisizioni where id_imgtype="+id_imgtype+" order by dtime desc";

            dsm.setPreparedStatementRef(sqlString);
            dsm.runPreparedQuery();

            while(dsm.next()){

                satelliteRasterList.add(new SatelliteRaster(dsm.getInteger(1),dsm.getData(2), name));

                Calendar calendar = dsm.getData(2);

                model.add(new TimelineEvent(name+" - "+sdf.format(calendar.getTime()),calendar.getTime()));
            }



            if(firstLoad || selectedRaster.getId_acquisizione() == -1){
                selectedRaster = satelliteRasterList.get(0);

            }else{

                selectedEvent  = new TimelineEvent(name+" - "+sdf.format(selectedRaster.getDtime().getTime()),selectedEvent.getStartDate());


                List<SatelliteRaster> result = satelliteRasterList.stream()
                             .filter(item -> item.getRasterName()
                                     .equals( selectedEvent.getData().toString()))
                             .collect(Collectors.toList());


                if(!result.isEmpty()) {
                    //selecting new event, with the same timestamp of the previous event, in the timeline

                    selectedRaster = result.get(0);

                    TimelineUpdater tu = TimelineUpdater.getCurrentInstance("timelineComp");

                    model.select(selectedEvent,tu);


                    PrimeFaces.current().executeScript("changeParamsJS('"+selectedRaster.getRasterType()+"','"+selectedRaster.getYear()+"','"+selectedRaster.getMonth()+"','"+selectedRaster.getDay()+"','"+selectedRaster.getDoy()+"')");

                    PrimeFaces.current().executeScript("switchDataType()");

                    PrimeFaces.current().executeScript("changeImageJS()");


                }else{

                   // RequestContext.getCurrentInstance().execute("deleteRaster()");
                    PrimeFaces.current().executeScript("deleteRaster()");
                }

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


}