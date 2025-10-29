public class WeatherDatum {
  public final long timestamp;
  public final String attribute; 
  public final int x; 
  public final int y;
  public final double value; 

  public WeatherDatum(long timestamp, String attribute, int x, int y, double value) {
    this.timestamp = timestamp;
    this.attribute = attribute;
    this.x = x;
    this.y = y;
    this.value = value;
  }
}


