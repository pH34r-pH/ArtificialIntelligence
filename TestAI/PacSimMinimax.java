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

 public class PacSimMinimax implements PacSimAction
 {
    public PacSimMinimax(int depth, String fname, int te, int gran, int max)
    {
        PacSim sim = new PacSim(fname, te, gran, max);
        sim.init(this);
    }

    public static void main(String[] args)
    {
        String fname = args[0];
        int depth = Integer.parseInt(args[1]);
        int te = 0;
        int gr = 0;
        int ml = 0;

        if(args.length == 5)
        {
            te = integer.parseInt(args[2]);
            gr = integer.parseInt(args[3]);
            ml = integer.parseInt(args[4]);
        }

        new PacSimMinimax(depth, fname, te, gr, ml);

        System.out.println("\nAdversarial Search using Minimax by Tyler Harbin-Giuntoli");
        System.out.println("\n    Game Board: " + fname);
        System.out.println("    Search Depth: " + depth + "\n");
        if(te > 0)
        {
            System.out.printf("    Preliminary Runs: %d\nGranularity: %d\nMax Move Limit: %d\n\nPreliminary Run Results:\n", te, gr, ml);
        }
    }

    @Override
    public void init()
    {

    }

    @Override
    public PacFace action(Object state)
    {
        PacCell[][] grid = (PacCel[][]) state;
        PacFace newFace = null;


        return newFace;
    }
 }