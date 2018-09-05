
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
 * Simple Repetitive Nearest Neighbor Agent
 * @author Tyler Harbin-Giuntoli
 */
public class PacSimRNNA implements PacAction {

    private int simTime;
    private WeightedNodeFactory thinker;
      
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
            thinker.Init(pc, grid);

            System.out.println("Cost table:");
            thinker.PrintCostMap();

            System.out.println("\nFood Array:");
            thinker.PrintFoodList();
            
            thinker.Build();

            for(int i = 0; i < food.size(); ++i){
                System.out.printf("%d : cost=%d : ", i, thinker.root.Next.get(i).PathToHereCost());
                thinker.root.Next.get(i).Print();
                System.out.println();
            }

            //-----------------------------------------------------------------------------------------
            /*
            Point tgt = PacUtils.nearestFood( pc.getLoc(), grid);
            path = BFSPath.getPath(grid, pc.getLoc(), tgt);
         
            System.out.println("Pac-Man currently at: [ " + pc.getLoc().x + ", " + pc.getLoc().y + " ]");
            System.out.println("Setting new target  : [ " + tgt.x + ", " + tgt.y + " ]");
            */
        }
      
        // take the next step on the current path
        /*
        Point next = path.remove( 0 );
        PacFace face = PacUtils.direction( pc.getLoc(), next );
        System.out.printf( "%5d : From [ %2d, %2d ] go %s%n", ++simTime, pc.getLoc().x, pc.getLoc().y, face );
        */
        return null;
    }
}
