/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pokephy;

import Pokephy.Pokephy.Trainer;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author MrCake
 */
@ManagedBean(name = "users", eager = true)
@SessionScoped
public class User extends Trainer{

    public static User current;
    public static User getCurrent(){ return current; }
    
    public User() {
        super("default");
    }
    
    
}
