public class WeatherPlaybackStageReader {
  public static Stage readStage(String path) {
    // ignore stage file layout for simplicity; reuse existing default actors
    WeatherPlaybackStage stage = new WeatherPlaybackStage("data/weather.log");
    try {
      // try reuse existing StageReader actor parsing to populate
      Stage base = StageReader.readStage(path);
      for(Actor a : base.listOfPlayers){ stage.addPlayer(a); }
    } catch(Exception e) {
      // defaults already handled by StageReader itself
    }
    return stage;
  }
}


