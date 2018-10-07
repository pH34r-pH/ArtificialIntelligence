import java.awt.Point;
import java.lang.Math;
import java.util.ArrayList;
import java.util.List;
import pacsim.BFSPath;
import pacsim.PacAction;
import pacsim.PacCell;
import pacsim.PacFace;
import pacsim.PacSim;
import pacsim.PacUtils;
import pacsim.PacmanCell;

/**
 * University of Central Florida
 * CAP4630 - Fall 2018
 * @author : Tyler Harbin-Giuntoli
 */

 public class Valuer
 {
    /**
      * PacCell child types:
      * FoodCell, GhostCell, GoalCell, HouseCell, MorphCell
      * PacmanCell, PathCell, PowerCell, StartCell, WallCell
      */

    private Minimax GameState;
    // Blinky - targets pac man directly, home top right
    // Inky - targets spot on opposite side of pac man from Blinky, same distance as blinky is from pac man, 
    // home bottom right

    public Valuer(Minimax gameState)
    {
        GameState = gameState;
    }

    public int Evaluate(PacCell[][] grid)
    {
        List<Point> Ghosts = PacUtils.findGhosts(grid);
        PacmanCell PacmanLocation = PacUtils.findPacman(grid);
        if(PacmanLocation == null)
        {
            return Integer.MIN_VALUE;
        }
        return 0;
    }
 }