package Pokephy;


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
    
    /* USER/TRAINER DEFINITION */
    public static class Trainer
    {
        private String name; 
        public String getName(){ return name; }
        
        public ArrayList<Pokemon> pool = new ArrayList<>();
        public Pokemon[] team = new Pokemon[6];
        
        private int id;
        void setId(int id) {
            this.id = id;
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
    
    public static class Skill
    {
        public static double defaultPower = 50.0;
        
        public enum SkillType
        {
            Physical, Special;
        }
        
        public double power;
        public SkillType skilltype;
        public Type type;
        
        Skill(Type type, SkillType skilltype, double power)
        {
            this.type = type;
            this.skilltype = skilltype;
            this.power = power;
        }
        
        public boolean isPhysical(){ return skilltype==SkillType.Physical; }
        public boolean isSpecial(){ return skilltype==SkillType.Special; }
    
        private int id;
        void setId(int id) {
            this.id = id;
        }
    }
    public static class PhysicalSkill extends Skill
    {
        public PhysicalSkill(Type type, double power) 
        {
            super(type, SkillType.Physical, power);
        }
    }
    public static class SpecialSkill extends Skill
    {
        public SpecialSkill(Type type, double power) 
        {
            super(type, SkillType.Special, power);
        }
    }
    
    public static class Pokemon 
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
        
        private final String name;
        public String getName(){ return name; }
        
        private final Type type;
        public Type getType(){ return type; }
        
        private Trainer trainer;
        public Trainer getTrainer(){ return trainer; }
        
        public PhysicalSkill physicalSkill;
        public SpecialSkill specialSkill;
        
        Pokemon(String name, Type type, double HP, double ATK, double DEF, double SP_ATK, double SP_DEF, double SPEED)
        {
            this.name = name;
            this.type = type;
                this.physicalSkill = new PhysicalSkill(type,Skill.defaultPower);
                this.specialSkill = new SpecialSkill(type,Skill.defaultPower);
            
            this.HP = HP;
            this.ATK = ATK;
            this.DEF = DEF;
            this.SP_ATK = SP_ATK;
            this.SP_DEF = SP_DEF;
            this.SPEED = SPEED;
        }
        
        private int id;
        void setId(int id) {
            this.id = id;
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
        //db.insertType(Type.Fire);//OK
        //db.insertType(Type.Water);//OK
        //db.insertType(Type.Grass);//OK
        
        
    }
    
}
