/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pokephy;

/**
 *
 * @author Maxime
 */

 
import Pokephy.Pokephy.Pokemon;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
 
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;
 
@ManagedBean
public class PickListView {
      
    /*********************************************/
    public DualListModel<Pokemon> createDual(ArrayList<Pokemon> items)
    {
        ArrayList<Pokemon> target = new ArrayList<>(items.size());
        DualListModel<Pokemon> dual = new DualListModel<>(items,target);
                
        return dual;
    }
    
    /*********************************************/
    
    
    private DualListModel<String> cities;
         
    @PostConstruct
    public void init() {
        //Cities
        List<String> citiesSource = new ArrayList<String>();
        List<String> citiesTarget = new ArrayList<String>();
         
        citiesSource.add("San Francisco");
        citiesSource.add("London");
        citiesSource.add("Paris");
        citiesSource.add("Istanbul");
        citiesSource.add("Berlin");
        citiesSource.add("Barcelona");
        citiesSource.add("Rome");
         
        cities = new DualListModel<String>(citiesSource, citiesTarget);    
    }
 
    public DualListModel<String> getCities() {
        return cities;
    }
 
    public void setCities(DualListModel<String> cities) {
        this.cities = cities;
    }

    public void onTransfer(TransferEvent event) {
        StringBuilder builder = new StringBuilder();
        for(Object item : event.getItems()) {
//            builder.append(((Theme) item).getName()).append("<br />");
        }
         
        FacesMessage msg = new FacesMessage();
        msg.setSeverity(FacesMessage.SEVERITY_INFO);
        msg.setSummary("Items Transferred");
        msg.setDetail(builder.toString());
         
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }  
}
