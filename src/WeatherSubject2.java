import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class WeatherSubject2 {
  public interface WeatherListener {
    void onWeatherTick(Map<Key, Double> latestByCellAttr,
                       Map<String, Double> averagesByAttr);
  }

  public static class Key {
    public final int x, y; public final String attr;
    public Key(int x, int y, String attr) { this.x=x; this.y=y; this.attr=attr; }
    @Override public int hashCode(){ return (x*31 + y)*31 + attr.hashCode(); }
    @Override public boolean equals(Object o){
      if(!(o instanceof Key)) return false; Key k=(Key)o; return x==k.x && y==k.y && attr.equals(k.attr);
    }
  }

  private final List<WeatherListener> listeners = new ArrayList<>();
  private final Map<Key, Double> latest = new HashMap<>();

  public void addListener(WeatherListener l){ listeners.add(l); }

  public void tick(List<WeatherDatum> batch){
    
    for(WeatherDatum d: batch){ latest.put(new Key(d.x, d.y, d.attribute), d.value); }
    
    Map<String, Double> avgs = latest.entrySet().stream()
      .collect(Collectors.groupingBy(e -> e.getKey().attr,
        Collectors.averagingDouble(Map.Entry::getValue)));
    
    for(WeatherListener l: listeners){ l.onWeatherTick(new HashMap<>(latest), avgs); }
  }
}


