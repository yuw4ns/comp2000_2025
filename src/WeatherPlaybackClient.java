import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Reads weather lines from a local file, treating it like a stream.
 * Each call to readBatch() advances by a few lines to simulate realtime.
 */
public class WeatherPlaybackClient {
  private final List<String> allLines;
  private int cursor = 0;

  public WeatherPlaybackClient(String file) {
    List<String> lines;
    try {
      lines = Files.readAllLines(Path.of(file));
    } catch(IOException e) {
      lines = new ArrayList<>();
    }
    this.allLines = lines;
  }

  // Return next N parsed entries (default 8 if not enough left)
  public List<WeatherDatum> readBatch(int n) {
    int end = Math.min(cursor + n, allLines.size());
    List<WeatherDatum> batch = allLines.subList(cursor, end).stream()
      .map(String::trim)
      .filter(s -> !s.isEmpty())
      .map(this::parse)
      .filter(Objects::nonNull)
      .collect(Collectors.toList());
    cursor = end == cursor ? 0 : end; // loop playback when reaching the end
    return batch;
  }

  private WeatherDatum parse(String line) {
    // timestamp attribute x y value
    String[] p = line.split("\\s+");
    if(p.length != 5) return null;
    try {
      long ts = Long.parseLong(p[0]);
      String attr = p[1].toLowerCase();
      int x = Integer.parseInt(p[2]);
      int y = Integer.parseInt(p[3]);
      double v = Double.parseDouble(p[4]);
      return new WeatherDatum(ts, attr, x, y, v);
    } catch(Exception e) {
      return null;
    }
  }
}


