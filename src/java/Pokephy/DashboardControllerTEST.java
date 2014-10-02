/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pokephy;

import java.util.List;
import org.primefaces.component.dashboard.Dashboard;

/**
 *
 * @author Maxime
 */
public class DashboardControllerTEST {
    private List<Dashboard> lstDashboard;
    public DashboardControllerTEST() {
        try {
            
        } catch (Exception e) {
            //log the exception or something else...
        }
    }
    //getter and setter...
    public List<Dashboard> getLstDashboard() {
        return this.lstDashboard;
    }
    public void setLstDashboard(List<Dashboard> lstDashboard) {
        this.lstDashboard = lstDashboard;
    }
    //your other methods here...

 
}
