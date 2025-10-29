import java.awt.Graphics;
import java.util.List;
import java.util.Random;

public class BotMoving implements GameState {
  @Override
  public void mouseClick(int x, int y, Stage s) {
    
  }

  @Override
  public void paint(Graphics g, Stage s) {
    for(Actor player: s.listOfPlayers) {
      if(player.isBot()) {
        List<Cell> possibleLocs = s.getClearRadius(player.loc, player.moves);
        if(possibleLocs.size() > 0) {
          
          int gx = (player.loc.x - 10) / Cell.size - 10;
          int gy = (player.loc.y - 10) / Cell.size - 10;
          double wx = 0.0, wy = 0.0;
          if(s instanceof WeatherPlaybackStage) {
            WeatherPlaybackStage w = (WeatherPlaybackStage) s;
            java.lang.Double vx = w.latestWeather().getOrDefault(new WeatherSubject2.Key(gx, gy, "windx"), 0.0);
            java.lang.Double vy = w.latestWeather().getOrDefault(new WeatherSubject2.Key(gx, gy, "windy"), 0.0);
            wx = vx; wy = vy;
          }
          Cell best = possibleLocs.get((new Random()).nextInt(possibleLocs.size()));
          double bestScore = -1e9;
          for(Cell c: possibleLocs){
            int cx = (c.x - 10) / Cell.size - 10; int cy = (c.y - 10) / Cell.size - 10;
            
            double score = (cx-gx)*wx + (cy-gy)*wy + Math.random()*0.1;
            if(score > bestScore){ bestScore = score; best = c; }
          }
          player.setLocation(best);
        }
      }
    }
    s.currentState = new ChoosingActor();
    for(Actor player: s.listOfPlayers) {
      player.turns = 1;
    }
  }  

  public String toString() {
    return getClass().getSimpleName();
  }
}
