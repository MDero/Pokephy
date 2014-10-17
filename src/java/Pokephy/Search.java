/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pokephy;

import Pokephy.Pokephy.Pokemon;
import Pokephy.Pokephy.Skill;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author MrCake
 */
@ManagedBean(name="searchParams", eager=true)
@SessionScoped
public class Search implements Serializable{
    public String pskill, sskill;
    public String name;
    public String type;

    public String statFilter;
    public String getStatFilter(){ return statFilter ;}
    public void setStatFilter(String statFilter) {
        this.statFilter = statFilter;
    }
    
    public ArrayList<Pokemon> results = new ArrayList<>();
    private void cleanVariables()
    {
        this.pskill=null;
        this.sskill=null;
        this.name=null;
        this.type=null;
        this.statFilter=null;
    }
    public ArrayList<Pokemon> getResults(){ 
        generateResults(); 
//        cleanVariables();
        return results;
    }
    public void generateResults(){
        results.clear();
        System.out.println(statFilter);
        
        if (name!=null && !"".equals(name))
        {
            for (Pokemon p : Database.instance.getAllPokemon())
                if (p.getName().equals(name))
                    results.add(p);
        }
        else{
            Skill PS = null, SS = null;
            if (pskill!=null && !"".equals(pskill))
                for (Skill s : Database.instance.getAllPhysicalSkills())
                    if (s.getName().equals(pskill))
                        PS = s;
            if (sskill!=null && !"".equals(sskill))
                for (Skill s : Database.instance.getAllSpecialSkills())
                    if (s.getName().equals(sskill))
                        SS = s;
            
            List<Pokemon> source = 
                    "HP".equals(statFilter) ? Database.instance.getPokemonSortedByHP_DESC():
                    "ATK".equals(statFilter) ? Database.instance.getPokemonSortedByATK_DESC():
                    "DEF".equals(statFilter) ? Database.instance.getPokemonSortedByDEF_DESC():
                    "SPATK".equals(statFilter) ? Database.instance.getPokemonSortedBySPATK_DESC():
                    "SPDEF".equals(statFilter) ? Database.instance.getPokemonSortedBySPDEF_DESC():
                    "SPEED".equals(statFilter) ? Database.instance.getPokemonSortedBySPEED_DESC():
                    Database.instance.getAllPokemon()
                    ;
                    
            for (Pokemon p : source)
                if (
                        ((PS!=null && p.physicalSkill == PS) || (SS!=null && p.specialSkill == SS ))
                        ||
                        (PS==null && SS==null)
                        )
                    results.add(p);
        }
    }
    
    private final String[] statFilters = {"HP","ATK","DEF","SPDEF","SPATK","SPEED"};
    public String[] getStatFilters(){ return statFilters; }

    public String getPskill() {
        return pskill;
    }
    public void setPskill(String pskill) {
        this.pskill = pskill;
    }
    public String getSskill() {
        return sskill;
    }
    public void setSskill(String sskill) {
        this.sskill = sskill;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
