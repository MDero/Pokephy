/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pokephy;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

/**
 *
 * @author Maxime
 */

@ManagedBean(name = "addPokemon")
public class addPokemon {
        private String name;
        private String type;
        private String physicalSkill;
        private String specialSkill;
        public String HP, DEF, SP_ATK, SP_DEF, SPEED;
        public String GIF;
        public int ATK;

    public String getGIF() {
        return GIF;
    }

    public void setGIF(String GIF) {
        this.GIF = GIF;
    }
        
        
        
    public String getPhysicalSkill() {
        return physicalSkill;
    }

    public void setPhysicalSkill(String physicalSkill) {
        this.physicalSkill = physicalSkill;
    }

    public String getSpecialSkill() {
        return specialSkill;
    }

    public void setSpecialSkill(String specialSkill) {
        this.specialSkill = specialSkill;
    }

    public String getHP() {
        return HP;
    }

    public void setHP(String HP) {
        this.HP = HP;
    }

    public int getATK() {
        return ATK;
    }

    public void setATK(int ATK) {
        this.ATK = ATK;
    }

    public String getDEF() {
        return DEF;
    }

    public void setDEF(String DEF) {
        this.DEF = DEF;
    }

    public String getSP_ATK() {
        return SP_ATK;
    }

    public void setSP_ATK(String SP_ATK) {
        this.SP_ATK = SP_ATK;
    }

    public String getSP_DEF() {
        return SP_DEF;
    }

    public void setSP_DEF(String SP_DEF) {
        this.SP_DEF = SP_DEF;
    }

    public String getSPEED() {
        return SPEED;
    }

    public void setSPEED(String SPEED) {
        this.SPEED = SPEED;
    }
        
        
       public String getName() {
                return name;
        }
        public void setName(String name) {
                this.name = name;
        }
        public String getType() {
            return type;
        }
        public void setType(String type) {
            this.type = type;
        }
        public void saveName(ActionEvent actionEvent) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Pokemon " + name + " added !"));
        }
}