import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Map;

/** Draws overlays on top of a base Cell based on latest weather. */
public class CellWeatherDecorator extends Cell {
  private final Cell base;
  private Map<WeatherSubject2.Key, java.lang.Double> latest; // injected per tick

  public CellWeatherDecorator(Cell base){
    super(base.col, base.row, base.x, base.y);
    this.base = base;
  }

  public void setLatest(Map<WeatherSubject2.Key, java.lang.Double> latest){ this.latest = latest; }

  @Override
  public void paint(Graphics g, Point mousePos){
    base.paint(g, mousePos);
    if(latest == null) return;
    // convert this cell to world coords (−10..9)
    int gx = (x - 10) / size - 10;
    int gy = (y - 10) / size - 10;

    double rain = latest.getOrDefault(new WeatherSubject2.Key(gx, gy, "rain"), java.lang.Double.valueOf(0.0));
    double windx = latest.getOrDefault(new WeatherSubject2.Key(gx, gy, "windx"), java.lang.Double.valueOf(0.0));
    double windy = latest.getOrDefault(new WeatherSubject2.Key(gx, gy, "windy"), java.lang.Double.valueOf(0.0));
    double temp = latest.getOrDefault(new WeatherSubject2.Key(gx, gy, "temp"), java.lang.Double.valueOf(0.5));
    double wind = Math.sqrt(windx*windx + windy*windy);

    // thresholds
    if(rain > 0.35){ // flood tint
      g.setColor(new Color(0, 120, 255, (int)Math.min(160, rain*255)));
      g.fillRect(x+2, y+2, width-4, height-4);
    }
    if(wind > 0.45){ // storm haze
      g.setColor(new Color(160, 160, 160, (int)Math.min(120, wind*200)));
      g.fillRect(x+3, y+3, width-6, height-6);
    }
    if(temp > 0.65){ // heat
      g.setColor(new Color(255, 120, 0, (int)Math.min(110, (temp-0.65)*400)));
      g.fillRect(x+4, y+4, width-8, height-8);
    }
    if(temp < 0.35){ // cold
      g.setColor(new Color(100, 200, 255, (int)Math.min(110, (0.35-temp)*400)));
      g.fillRect(x+4, y+4, width-8, height-8);
    }
  }
}


