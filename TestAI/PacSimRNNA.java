
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
public class PacSimRNNA implements PacAction {

    private int simTime;
    private WeightedNodeFactory thinker;
    private boolean print = true;
      
    public PacSimRNNA( String fname ) {
        PacSim sim = new PacSim( fname );
        sim.init(this);
    }
   
    public static void main( String[] args ) {         
        System.out.println("\nRNN Agent by Tyler Harbin-Giuntoli;");
        System.out.println("\nMaze : " + args[ 0 ] + "\n" );
        new PacSimRNNA( args[ 0 ] );
    }

    @Override
    public void init() {
        simTime = 0;
        thinker = new WeightedNodeFactory();
    }
   
    @Override
    public PacFace action( Object state ) {

        PacCell[][] grid = (PacCell[][]) state;
        PacmanCell pc = PacUtils.findPacman( grid );
      
        // make sure Pac-Man is in this game
        if( pc == null ) return null;
            
        // if current path is empty, generate the path
      
        if( !thinker.isLoaded ) {
            long timer = System.currentTimeMillis();

            thinker.Init(pc, grid);
            thinker.Build();

            timer = System.currentTimeMillis() - timer;

            if(print)
            {
                System.out.println("Cost table:\n");
                thinker.PrintCostMap();

                System.out.println("\nFood Array:\n");
                thinker.PrintFoodList();

                thinker.PrintSteps();

                System.out.printf("\nTime to generate plan: %d msec\n", timer);
            }
            
            System.out.println("\nSolution moves:\n");
        }
      
        // take the next step on the current path
        Point next = thinker.TakeStep();
        PacFace face = PacUtils.direction( pc.getLoc(), next );
        System.out.printf( "%5d : From [ %2d, %2d ] go %s%n", thinker.step, pc.getLoc().x, pc.getLoc().y, face );
        
        return face;
    }
}
