package it.cnr.ibimet.models;


import it.cnr.ibimet.entities.ImageType;
import org.primefaces.model.SelectableDataModel;

import javax.faces.model.ListDataModel;
import java.util.List;

public class ImageTypeModel extends ListDataModel<ImageType> implements SelectableDataModel<ImageType> {




    public ImageTypeModel(List<ImageType> data){
        super(data);
    }



    public ImageType getRowData(String rowKey) {
        List<ImageType> zone = (List<ImageType>) getWrappedData();

        for(ImageType zona : zone){
            String aaa = ""+zona.getId_imgtype();

            //       System.out.println("aaa: "+aaa);
            if(aaa.equals(rowKey))
                return zona;
        }

        return null;
    }


    public Object getRowKey(ImageType zona) {
        //   System.out.println("zona: "+zona.getId_acquisizione());
        return zona.getId_imgtype();
    }

}
