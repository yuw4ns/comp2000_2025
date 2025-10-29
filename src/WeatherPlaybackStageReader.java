public class WeatherPlaybackStageReader {
  public static Stage readStage(String path) {
    
    WeatherPlaybackStage stage = new WeatherPlaybackStage("data/weather.log");
    try {
      
      Stage base = StageReader.readStage(path);
      for(Actor a : base.listOfPlayers){ stage.addPlayer(a); }
    } catch(Exception e) {
      
    }
    return stage;
  }
}


