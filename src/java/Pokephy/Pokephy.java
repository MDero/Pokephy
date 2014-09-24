package Pokephy;


import Pokephy.Pokephy.Skill.SkillType;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author MrCake
 */

public class Pokephy {
    
    /* UTIL CLASS FOR NAMING AND ID */
    public class Named 
    {
        //NAME
        private final String name;
        public String getName(){ return name; }
        
        public Named(String name)
        {
            this.name = name;
        }
        
        //ID 
        private int id;
        void setId(int id) {
            this.id = id;
        }
    }
    
    /* USER/TRAINER DEFINITION */
    public static class Trainer extends Named
    {
        public ArrayList<Pokemon> pool = new ArrayList<>();
        public Pokemon[] team = new Pokemon[6];
        
        public Trainer(String name)
        {
            super(name);
        }
    }
    
    /* POKEMON DEFINITION */
    public static enum Type 
    {
        Fire("FIRE"), Water("WATER"), Grass("GRASS");
        
        Type(String nameInDb){ nameInDatabase = nameInDb; }
        
        private final String nameInDatabase;
        public String getName(){ return nameInDatabase ;}
        
        Type strongAgainst, weakAgainst;
        
        public static void init()
        {
            Fire.strongAgainst = Grass;
            Fire.weakAgainst = Water;
            
            Water.strongAgainst = Fire;
            Water.weakAgainst = Grass;
            
            Grass.strongAgainst = Water;
            Grass.weakAgainst = Fire;
        }

        private int id;
        void setId(int id) {
            this.id = id;
        }
    }
    
    public static class Skill extends Named
    {
        public static double defaultPower = 50.0;
        
        public enum SkillType
        {
            Physical, Special;
        }
        
        public double power;
        public SkillType skilltype;
        public Type type;
        
        Skill(String name,Type type, SkillType skilltype, double power)
        {
            super(name);
            
            this.type = type;
            this.skilltype = skilltype;
            this.power = power;
        }
        
        public boolean isPhysical(){ return skilltype==SkillType.Physical; }
        public boolean isSpecial(){ return skilltype==SkillType.Special; }
    }
    public static class PhysicalSkill extends Skill
    {
        public PhysicalSkill(String name,Type type, double power) 
        {
            super(name,type, SkillType.Physical, power);
        }
    }
    public static class SpecialSkill extends Skill
    {
        public SpecialSkill(String name,Type type, double power) 
        {
            super(name,type, SkillType.Special, power);
        }
    }
    
    public static class Pokemon extends Named
    {
        private double HP;
        public double getHP(){ return HP; }
        public void setHP(double hp){ this.HP = hp < 0 ? 0 : hp; }
        
        public boolean KO; 
        public boolean isKO(){ return this.KO; }
        
        public double ATK, DEF, SP_ATK, SP_DEF, SPEED;
        public double getATK(){ return ATK; }
        public double getDEF(){ return DEF; }
        public double getSPATK(){ return SP_ATK; }
        public double getSPDEF(){ return SP_DEF; }
        public double getSPEED(){ return SPEED; }
             
        private final Type type;
        public Type getType(){ return type; }
        
        private Trainer trainer;
        public Trainer getTrainer(){ return trainer; }
        
        public PhysicalSkill physicalSkill;
        public SpecialSkill specialSkill;
        
        Pokemon(String name, Type type, double HP, double ATK, double DEF, double SP_ATK, double SP_DEF, double SPEED)
        {
            super(name);
            
            this.type = type;
                this.physicalSkill = new PhysicalSkill("testSkillP",type,Skill.defaultPower);
                this.specialSkill = new SpecialSkill("testSkillS",type,Skill.defaultPower);
            
            this.HP = HP;
            this.ATK = ATK;
            this.DEF = DEF;
            this.SP_ATK = SP_ATK;
            this.SP_DEF = SP_DEF;
            this.SPEED = SPEED;
        }
                
        /* FIGHT MANAGEMENT */
        public void attack(Pokemon opponent, Skill skill)
        {
            //calculate modifiers
            double type_bonus = 1.0;
            double stab_bonus = 1.0;
            
            //calculate damage
            double damage = (skill.isPhysical() ? ATK : SP_ATK) + skill.power; 
            
            //apply bonuses
            damage *= skill.type == this.type ? 1.5 : 1.0; // STAB
            damage *= skill.type == opponent.getType().weakAgainst ? 2.0 : 1.0;//Types balance
            
            //apply damage
            opponent.setHP(opponent.getHP() - damage);
        }
        
    }
    
    public static void main(String args[])
    {
        Database db = Database.instance;
        
        /* TEST CONNEXION */
        //db.executeTestQuery();//OK
        
        /* TEST TYPE INSERTION */
        Type.init();//initialize type balance
        db.insertType(Type.Fire);//OK
        db.insertType(Type.Water);//OK
        db.insertType(Type.Grass);//OK
        
        /* TEST SKILL INSERTION */
        Skill s = new Skill("Flammèche",Type.Fire,SkillType.Special,30);
        db.insertSkill(s);//OK
    }
    
}
