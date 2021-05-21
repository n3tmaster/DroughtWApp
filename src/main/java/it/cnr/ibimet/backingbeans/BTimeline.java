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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;



@ManagedBean(name="timeLine")
@ViewScoped
public class BTimeline implements Serializable {
    static Logger logger = Logger.getLogger(String.valueOf(BTimeline.class));
    private TimelineModel<String, ?> model;
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
    private LocalDateTime dF, dI;

    public boolean isUnselectable() {
        return unselectable;
    }

    public LocalDateTime getdF() {
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

    public LocalDateTime getdI() {
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
        logger.info("init");
        model = new TimelineModel();

        satelliteRasterList = new ArrayList<SatelliteRaster>();
        selectedRaster = new SatelliteRaster();

        firstLoad=true;


        GregorianCalendar newGregCal = new GregorianCalendar();
        //GregorianCalendar newGregCal2 = new GregorianCalendar(2018,8,20);



        newGregCal.add(Calendar.DAY_OF_YEAR, -120);
        dI = LocalDateTime.of(newGregCal.get(Calendar.YEAR), (newGregCal.get(Calendar.MONTH)+1), newGregCal.get(Calendar.DAY_OF_MONTH),0,0);

        newGregCal.add(Calendar.DAY_OF_YEAR, 127);
        dF = LocalDateTime.of(newGregCal.get(Calendar.YEAR), (newGregCal.get(Calendar.MONTH)+1), newGregCal.get(Calendar.DAY_OF_MONTH),0,0);
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

        //update params in JS session
        PrimeFaces.current().executeScript("changeParamsJS('"+selectedRaster.getRasterType()+"','"+selectedRaster.getYear()+"','"+selectedRaster.getMonth()+"','"+selectedRaster.getDay()+"','"+selectedRaster.getDoy()+"')");

        //change load images
        PrimeFaces.current().executeScript("changeImageJS()");

    }


    /**
     * create acquisition list
     */
    public void populateAcquisitions(int id_imgtype, String name, boolean once){
        satelliteRasterList.clear();
        model.clear();
        TDBManager dsm=null;
        try {

            logger.info("open connection");
            dsm = new TDBManager("jdbc/ssdb");

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

            logger.info("populate acquisitions for "+id_imgtype);


            String sqlString="select id_acquisizione, dtime from postgis.acquisizioni inner join postgis.imgtypes using (id_imgtype) where id_imgtype="+id_imgtype+" and once is false order by dtime desc";

            dsm.setPreparedStatementRef(sqlString);
            dsm.runPreparedQuery();

            //check if we have just only one image

            while(dsm.next()){

                satelliteRasterList.add(new SatelliteRaster(dsm.getInteger(1),dsm.getData(2), name));

                Calendar calendar = dsm.getData(2);

                model.add(TimelineEvent.<String>builder().data(name+" - "+sdf.format(calendar.getTime()))
                        .startDate(LocalDate.of(calendar.get(Calendar.YEAR), (calendar.get(Calendar.MONTH)+1), calendar.get(Calendar.DAY_OF_MONTH))).build());
            }




            if(firstLoad || selectedRaster.getId_acquisizione() == -1){
                selectedRaster = satelliteRasterList.get(0);

            }else{

           //     selectedEvent  = new TimelineEvent(name+" - "+sdf.format(selectedRaster.getDtime().getTime()),selectedEvent.getStartDate());

                selectedEvent = TimelineEvent.<String>builder().data(name+" - "+sdf.format(selectedRaster.getDtime().getTime()))
                        .startDate(selectedEvent.getStartDate()).build();

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

                   // PrimeFaces.current().executeScript("changeImageJS()");





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