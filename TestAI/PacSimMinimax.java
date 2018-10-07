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

 public class PacSimMinimax implements PacAction
 {
    Minimax thinker;
    int steps;

    public PacSimMinimax(int depth, String fname, int te, int gran, int max)
    {
        PacSim sim = new PacSim(fname, te, gran, max);
        steps = depth;
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
            te = Integer.parseInt(args[2]);
            gr = Integer.parseInt(args[3]);
            ml = Integer.parseInt(args[4]);
        }

        new PacSimMinimax(depth, fname, te, gr, ml);

        System.out.println("\nAdversarial Search using Minimax by Tyler Harbin-Giuntoli\n");
        System.out.println("    Game Board: " + fname);
        System.out.println("    Search Depth: " + depth + "\n");
        if(te > 0)
        {
            System.out.printf(
                        "    Preliminary Runs: %d\n"+
                        "    Granularity: %d\n"+
                        "    Max Move Limit: %d\n\n"+
                        "    Preliminary Run Results:\n", te, gr, ml);
        }
    }

    @Override
    public void init()
    {
        thinker = new Minimax(steps);
    }

    @Override
    public PacFace action(Object state)
    {
        return thinker.Turn((PacCell[][])state);
    }
 }