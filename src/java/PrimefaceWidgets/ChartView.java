package PrimefaceWidgets;

import Pokephy.Pokephy.Pokemon;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
 
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.HorizontalBarChartModel;
import org.primefaces.model.chart.ChartSeries;
 
@ManagedBean
public class ChartView implements Serializable {
    
    private HorizontalBarChartModel horizontalBarModel;
 
    public ChartView(){
        this.horizontalBarModel = new HorizontalBarChartModel();
        ChartSeries stats = new ChartSeries();
        stats.setLabel("Stats");
        stats.set("HP", 50);
        stats.set("ATK", 50);
        stats.set("DEF", 50);
        stats.set("SPATK", 0);
        stats.set("SPDEF", 0);
        stats.set("SPEED", 0);

        horizontalBarModel.addSeries(stats);

    }
    
    public HorizontalBarChartModel getHorizontalBarModel() {
        return horizontalBarModel;
    }

    public HorizontalBarChartModel createStatsBarModel(Pokemon p){
        if (p!=null)
        {
            horizontalBarModel = new HorizontalBarChartModel();

            /* Make series for each stat with a color corresponding to the value */
            ChartSeries hp = new ChartSeries(),atk = new ChartSeries(), def = new ChartSeries(),spatk = new ChartSeries(),spdef = new ChartSeries(),speed = new ChartSeries();
            
            hp.set("SPEED",0);
            hp.set("SPDEF",0);
            hp.set("SPATK",0);
            hp.set("DEF",0);
            hp.set("ATK", 0);
            hp.set("HP", p.getHP());
            
            atk.set("SPEED",0);
            atk.set("SPDEF",0);
            atk.set("SPATK",0);
            atk.set("DEF",0);
            atk.set("ATK", p.getATK());
            atk.set("HP",0);
            
            def.set("SPEED",0);
            def.set("SPDEF",0);
            def.set("SPATK",0);
            def.set("DEF", p.getDEF());
            def.set("ATK",0);
            def.set("HP",0);
            
            spatk.set("SPEED",0);
            spatk.set("SPDEF",0);
            spatk.set("SPATK", p.getSPATK());
            spatk.set("DEF",0);
            spatk.set("ATK",0);
            spatk.set("HP",0);
            
            spdef.set("SPEED",0);
            spdef.set("SPDEF", p.getSPDEF());
            spdef.set("SPATK", 0);
            spdef.set("DEF",0);
            spdef.set("ATK",0);
            spdef.set("HP",0);

            speed.set("SPEED", p.getSPEED());
            speed.set("SPDEF", 0);
            speed.set("SPATK", 0);
            speed.set("DEF",0);
            speed.set("ATK",0);
            speed.set("HP",0);
            
            //calculate colors
            String hpColor,atkColor,defColor,spatkColor, spdefColor, speedColor;
            double hpRatio, atkRatio, defRatio, spatkRatio, spdefRatio, speedRatio;
            hpRatio = p.getHP()/150.0;
            atkRatio = p.getATK()/150.0;
            defRatio = p.getDEF()/150.0;
            spatkRatio = p.getSPATK()/150.0;
            spdefRatio = p.getSPDEF()/150.0;
            speedRatio = p.getSPEED()/150.0;
           
            
            /* Calculate color */
//            String red = "cc6666";
//            String green = "00FF00";
//            String blue = "0000FF";
//            
//            hpColor = hpRatio > 0.3 ? hpRatio > 0.6 ? green : blue : red;
//            atkColor = atkRatio > 0.3 ? atkRatio > 0.6 ? green : blue : red;
//            defColor = defRatio > 0.3 ? defRatio > 0.6 ? green : blue : red;
//            spatkColor = spatkRatio > 0.3 ? spatkRatio > 0.6 ? green : blue : red;
//            spdefColor = spdefRatio > 0.3 ? spdefRatio > 0.6 ? green : blue : red;
//            speedColor = speedRatio > 0.3 ? speedRatio > 0.6 ? green : blue : red;
            
            String red, blue;
            red = "2F";
            blue = "36";
            int greenPlus = 0;
            double greenLevel = 130.0;
            double redLevel = 125.0-hpRatio*greenLevel;
            
            hpColor    = Integer.toHexString((int) ((1.0-hpRatio)    *redLevel)) +Integer.toHexString((int) (hpRatio    *greenLevel) +greenPlus) +blue;
            atkColor   = Integer.toHexString((int) ((1.0-atkRatio)   *redLevel)) +Integer.toHexString((int) (atkRatio   *greenLevel) +greenPlus) +blue;
            defColor   = Integer.toHexString((int) ((1.0-defRatio)   *redLevel)) +Integer.toHexString((int) (defRatio   *greenLevel) +greenPlus) +blue;
            spatkColor = Integer.toHexString((int) ((1.0-spatkRatio) *redLevel)) +Integer.toHexString((int) (spatkRatio *greenLevel) +greenPlus) +blue;
            spdefColor = Integer.toHexString((int) ((1.0-spdefRatio) *redLevel)) +Integer.toHexString((int) (spdefRatio *greenLevel) +greenPlus) +blue;
            speedColor = Integer.toHexString((int) ((1.0-speedRatio) *redLevel)) +Integer.toHexString((int) (speedRatio *greenLevel) +greenPlus) +blue;

            
            /* set series colors */
            String seriesColor = hpColor+","+atkColor+","+defColor+","+spatkColor+","+spdefColor+","+speedColor;
            
            horizontalBarModel.addSeries(hp);
            horizontalBarModel.addSeries(atk);
            horizontalBarModel.addSeries(def);
            horizontalBarModel.addSeries(spatk);
            horizontalBarModel.addSeries(spdef);
            horizontalBarModel.addSeries(speed);
            
            horizontalBarModel.setSeriesColors(seriesColor);
            horizontalBarModel.setAnimate(true);
            horizontalBarModel.setShadow(false);
            horizontalBarModel.setZoom(true);
            horizontalBarModel.setStacked(true);
//            //
//            ChartSeries stats = new ChartSeries();
//            stats.setLabel("Stats");
//            stats.set("HP", p.getHP());
//            stats.set("ATK", p.getATK());
//            stats.set("DEF", p.getDEF());
//            stats.set("SPATK", p.getSPATK());
//            stats.set("SPDEF", p.getSPDEF());
//            stats.set("SPEED", p.getSPEED());
//
//            horizontalBarModel.addSeries(stats);
//            System.out.println(horizontalBarModel.getSeriesColors());
////            horizontalBarModel.setSeriesColors("66cc66");
////            horizontalBarModel.setSeriesColors("E7E658");
////            horizontalBarModel.setSeriesColors("cc6666");
//            horizontalBarModel.setSeriesColors("00FFFF");

            Axis xAxis = horizontalBarModel.getAxis(AxisType.X);
            xAxis.setLabel("Value");
            xAxis.setMin(0);
            xAxis.setMax(150);

            Axis yAxis = horizontalBarModel.getAxis(AxisType.Y);
//            yAxis.setLabel("Stat"); 
        }
        
        return horizontalBarModel;
    }
 
}