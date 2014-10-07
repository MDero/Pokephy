/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PrimefaceWidgets;
 
import Pokephy.Database;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import Pokephy.Pokephy.*;
 
@ManagedBean
@ViewScoped
public class DataGridView implements Serializable {
     
    private List<Pokemon> pokemons;
     
    private Pokemon selectedPokemon;
     
    @PostConstruct
    public void init() {
        pokemons = Database.instance.getAllPokemon();
    }
  
    public List<Pokemon> getPokemons(){ return pokemons; }
    
    public Pokemon getSelectedPokemon(){ return selectedPokemon; }
    public void setSelectedPokemon(Pokemon selectedPokemon){ this.selectedPokemon = selectedPokemon; } 
}