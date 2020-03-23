package it.cnr.ibimet.models;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import it.cnr.ibimet.entities.StatSeries;

public class StatSeriesModel extends ListDataModel<StatSeries> implements SelectableDataModel<StatSeries> {




    public StatSeriesModel(List<StatSeries> data){
        super(data);
    }



    public StatSeries getRowData(String rowKey) {
        List<StatSeries> zone = (List<StatSeries>) getWrappedData();

        for(StatSeries zona : zone){
            String aaa = ""+zona.getDoy() + zona.getYear();

            //       System.out.println("aaa: "+aaa);
            if(aaa.equals(rowKey))
                return zona;
        }

        return null;
    }


    public Object getRowKey(StatSeries zona) {
        //   System.out.println("zona: "+zona.getId_acquisizione());
        return zona.getDoy() + zona.getYear();
    }

}