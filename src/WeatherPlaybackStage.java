import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class WeatherPlaybackStage extends Stage implements WeatherSubject2.WeatherListener {
  private final WeatherPlaybackClient client;
  private final WeatherSubject2 subject;
  private final List<CellWeatherDecorator> decorated;
  private Map<WeatherSubject2.Key, java.lang.Double> latest = new HashMap<>();
  private Map<String, java.lang.Double> avgs = new HashMap<>();

  public WeatherPlaybackStage(String file){
    super();
    client = new WeatherPlaybackClient(file);
    subject = new WeatherSubject2();
    subject.addListener(this);
    decorated = new ArrayList<>();
    
    for(int i=0;i<grid.cells.length;i++){
      for(int j=0;j<grid.cells[i].length;j++){
        decorated.add(new CellWeatherDecorator(grid.cells[i][j]));
      }
    }
  }

  @Override
  public void paint(Graphics g, Point mouseLoc){
    
    currentState.paint(g, this);

    
    subject.tick(client.readBatch(12));
    
    for(CellWeatherDecorator c: decorated){ c.setLatest(latest); }

    
    for(CellWeatherDecorator c: decorated){ c.paint(g, mouseLoc); }

    
    grid.paintOverlay(g, cellOverlay, new Color(0f, 0f, 1f, 0.5f));

    
    for(Actor a: listOfPlayers){ a.paint(g); }

    draw_panel(g, mouseLoc);
  }

  private void draw_panel(Graphics g, Point mouseLoc){
    final int hTab = 10; final int blockVT = 35; final int margin = 21*blockVT;
    int y = 20;
    g.setColor(Color.DARK_GRAY);
    g.drawString("Weather Playback", margin, y); y += blockVT;

    
    g.drawString("avg rain="+fmt(avgs.getOrDefault("rain",0.0))+" wind="+fmt(windMag())+" temp="+fmt(avgs.getOrDefault("temp",0.0)), margin, y); y += blockVT;

    
    Map<String, Long> counts = latest.keySet().stream()
      .collect(Collectors.groupingBy(k->k.attr, Collectors.counting()));
    g.drawString("cells: rain="+counts.getOrDefault("rain",0L)+" storm="+stormCells()+" heat/cold="+heatColdCells(), margin, y);
  }

  private double windMag(){
    double wx = avgs.getOrDefault("windx",0.0); double wy = avgs.getOrDefault("windy",0.0);
    return Math.sqrt(wx*wx+wy*wy);
  }
  private String fmt(double d){ return String.format("%.2f", d); }
  private long stormCells(){
    return latest.entrySet().stream()
      .collect(Collectors.groupingBy(e->e.getKey().x+","+e.getKey().y, Collectors.toMap(e->e.getKey().attr, Map.Entry::getValue, (a,b)->b)))
      .values().stream()
      .filter(m -> {
        double wx = m.getOrDefault("windx",0.0); double wy = m.getOrDefault("windy",0.0);
        return Math.sqrt(wx*wx+wy*wy) > 0.45;
      }).count();
  }
  private long heatColdCells(){
    return latest.entrySet().stream()
      .collect(Collectors.groupingBy(e->e.getKey().x+","+e.getKey().y, Collectors.toMap(e->e.getKey().attr, Map.Entry::getValue, (a,b)->b)))
      .values().stream()
      .filter(m -> m.getOrDefault("temp",0.5) > 0.65 || m.getOrDefault("temp",0.5) < 0.35)
      .count();
  }

  @Override
  public void onWeatherTick(Map<WeatherSubject2.Key, java.lang.Double> latestByCellAttr, Map<String, java.lang.Double> averagesByAttr) {
    this.latest = latestByCellAttr; this.avgs = averagesByAttr;
    
    for(Actor a: listOfPlayers){
      if(!a.isBot()){
        a.resetMovesToBase();
        
        int gx = (a.loc.x - 10) / Cell.size - 10;
        int gy = (a.loc.y - 10) / Cell.size - 10;
        double rain = latest.getOrDefault(new WeatherSubject2.Key(gx, gy, "rain"), 0.0);
        double windx = latest.getOrDefault(new WeatherSubject2.Key(gx, gy, "windx"), 0.0);
        double windy = latest.getOrDefault(new WeatherSubject2.Key(gx, gy, "windy"), 0.0);
        double temp = latest.getOrDefault(new WeatherSubject2.Key(gx, gy, "temp"), 0.5);
        double wind = Math.sqrt(windx*windx + windy*windy);
        
        if(rain > 0.35) a.moves = Math.max(1, a.moves - 1);
        if(temp > 0.75) a.moves = Math.max(1, (int)Math.ceil(a.moves * 0.5));
        if(temp < 0.25 && a.turns > 0) { 
          if(Math.random() < 0.5) a.turns = 0;
        }
      } else {
        
        a.resetMovesToBase();
        int gx = (a.loc.x - 10) / Cell.size - 10;
        int gy = (a.loc.y - 10) / Cell.size - 10;
        double rain = latest.getOrDefault(new WeatherSubject2.Key(gx, gy, "rain"), 0.0);
        double windx = latest.getOrDefault(new WeatherSubject2.Key(gx, gy, "windx"), 0.0);
        double windy = latest.getOrDefault(new WeatherSubject2.Key(gx, gy, "windy"), 0.0);
        double temp = latest.getOrDefault(new WeatherSubject2.Key(gx, gy, "temp"), 0.5);
        double wind = Math.sqrt(windx*windx + windy*windy);
        
        if(rain > 0.35 && Math.random() < 0.5) { a.turns = 0; } 
        if(wind > 0.6) { a.moves = Math.max(1, a.moves - 1); }
        if(temp > 0.80) { a.moves = 1; }
        if(temp < 0.20) { a.moves = Math.max(1, a.moves - 1); }
      }
    }
  }

  
  public Map<WeatherSubject2.Key, java.lang.Double> latestWeather() { return latest; }
}


