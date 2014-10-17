/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pokephy;

import Pokephy.Pokephy.Named;
import Pokephy.Pokephy.Pokemon;
import Pokephy.Pokephy.Skill;
import Pokephy.Pokephy.Type;
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
        public String HP, ATK, DEF, SP_ATK, SP_DEF, SPEED;
        public String GIF;

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

    public String getATK() {
        return ATK;
    }

    public void setATK(String ATK) {
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
                
                //correct strings
                HP = HP.replaceAll("[^0-9]", "");
                ATK = ATK.replaceAll("[^0-9]", "");
                DEF = DEF.replaceAll("[^0-9]", "");
                SP_ATK = SP_ATK.replaceAll("[^0-9]", "");
                SP_DEF = SP_DEF.replaceAll("[^0-9]", "");
                SPEED = SPEED.replaceAll("[^0-9]", "");
                
                Pokemon p = new Pokemon(
                        name,
                        Type.valueOf(type),
                        Double.valueOf(HP),
                        Double.valueOf(ATK),
                        Double.valueOf(DEF),
                        Double.valueOf(SP_ATK),
                        Double.valueOf(SP_DEF),
                        Double.valueOf(SPEED)
                );
                Skill pskill=null, sskill=null;
                for (Named n : Database.instance.getAllPhysicalSkills())
                    if (n.getName().equals(physicalSkill))
                        pskill = (Skill)n;
                for (Named n : Database.instance.getAllSpecialSkills())
                    if (n.getName().equals(specialSkill))
                        sskill = (Skill)n;
                if (pskill!=null)
                    p.physicalSkill=pskill;
                if (sskill!=null)
                    p.specialSkill=sskill;
                
                Database.instance.insertPokemon(p);
        }
}