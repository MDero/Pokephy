/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pokephy;

import Pokephy.Pokephy.Named;
import Pokephy.Pokephy.Trainer;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author MrCake
 */
@ManagedBean(name = "users", eager = true)
@SessionScoped
public class User implements Serializable{

    /* Once logged in */
    public Trainer current;
    public Trainer getCurrent(){ return current; }
    public void setCurrent(Trainer t){ current = t; }
    
    
    /* Before logging in */
    public String login;
    public String getLogin(){ return login ; }
    public void setLogin(String login){ this.login = login; }
    
    public String password;
    public String getPassword(){ return password; }
    public void setPassword(String password){ this.password = password; }
    

    public boolean checkValidity()
    {
        if (login!=null)
        {
            FacesContext context = FacesContext.getCurrentInstance();


    //        context.addMessage(null, new FacesMessage("Second Message", "Additional Message Detail"));
            for (Named t : Database.instance.getAll("TRAINER"))
            {
                Trainer trainer = (Trainer) t;
                String cname, clogin;
                cname = trainer.getName().toUpperCase().replaceAll(" ", "");
                clogin = login.toUpperCase().replaceAll(" ", "");

                System.out.println(cname+","+clogin);

                if (cname.equals(clogin)
                        /* && TODO: CHECK PASSWORD */
                        )
                {
                    current = trainer;
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Successful",  login+" successfully logged in Pokephy"));
                    return true;
                }
            }
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error",  "login failed for "+login));
        }
        return false;
    }
    public String directionAfterLoginAttempt(){
        return checkValidity() ? "tabView" : "login";
    }
    
    
}
