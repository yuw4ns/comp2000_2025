public class WeatherDatum {
  public final long timestamp;
  public final String attribute; // rain, windx, windy, temp
  public final int x; // world coord (0,0) at grid centre
  public final int y;
  public final double value; // 0.0..1.0

  public WeatherDatum(long timestamp, String attribute, int x, int y, double value) {
    this.timestamp = timestamp;
    this.attribute = attribute;
    this.x = x;
    this.y = y;
    this.value = value;
  }
}


