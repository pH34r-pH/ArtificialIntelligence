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
    /**************************************
    * Evaluation Function Logic:
    * I start with a base score of 100, and then add or subtract based on the following features;
    * - Win or Loss conditions are Integer max value and min value respectively
    * - Score is lost for letting ghosts get closer, but it has a reduced impact when they're in scatter mode
    * - Score is gained for letting ghosts get closer while a power pellet is active, with a small buffer time at the end
    * - Score is lost for every food pellet on the board
    *
    * I think this is a pretty good metric because increasing search depth does increase win rate pretty linearly
    *************************************/
      

    private Minimax GameState;
    private int GhostRangeWeight = 30;
    private int HuntingBufferTime = 5;
    private int FoodWeight = 3;

    public Valuer(Minimax gameState)
    {
        GameState = gameState;
    }

    public int Evaluate(PacCell[][] grid)
    {
        // Initial base score
        // It doesn't matter what this is set to, but 100 sounds nice
        int score = 100;

        // We reduce the "fear factor" of the ghosts when they aren't actively seeking pacman
        // This is so that we don't corner ourselves when the ghosts aren't after us
        int GhostWeight = GameState.GhostsAreChasing ? GhostRangeWeight : (GhostRangeWeight / 2);
        

        List<Point> Ghosts = PacUtils.findGhosts(grid);
        PacmanCell PacmanLocation = PacUtils.findPacman(grid);

        // The worst possible thing is pacman dying
        if(PacmanLocation == null)
        {
            return Integer.MIN_VALUE;
        }

        // The best possible thing is eating all the food
        if(!PacUtils.foodRemains(grid))
        {
            return Integer.MAX_VALUE;
        }

        Point Pacman = PacmanLocation.getLoc();

        // We want to stay as far away from ghosts as possible, but chase them when power pellets are active
        int closestGhost = 1000;
        for(Point g : Ghosts)
        {
            int thisGhost = PacUtils.manhattanDistance(Pacman, g);

            if(thisGhost < closestGhost) closestGhost = thisGhost;
        }
        // This adds a small buffer so when we're about to run out of hunting time, we don't end up right next to a ghost
        if(GameState.PowerPelletActive && GameState.Timer > HuntingBufferTime)
        {
            score += GhostWeight / closestGhost;
        }
        else if(GameState.PowerPelletActive && GameState.Timer < HuntingBufferTime)
        {
            // Do nothing- this way we ignore the ghosts and don't get stuck in a corner or something
        }
        else
        {
            score -= GhostWeight / closestGhost;
        }

        // We also want to reduce the number of food on the board
        score -= (PacUtils.findFood(grid).size() * FoodWeight);

        return score;
    }
 }