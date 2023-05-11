package dbmain;

public class OctTree {
    private Node root;
    

    public OctTree() {
        this.root = new Node();
        this.root.newParent();
        
    }
    
    public OctTree(Node parent)//nezabat el list bta3et el node ely da5lalna enhom ykono distributed 3la children + ely da5el gded
    {
    	this.root = parent;
    	this.root.newParent();
    }

    public void insert(Object x, Object y, Object z, Object ref) 
    {
    	Point p1 = new Point(x,y,z,ref);
    	root.insert(p1);
    	
    	//case 1 ehna f child w ma3andenash haga tahteena w el refrences a2al mn 16
    	//case 2  ehna f child w ma3andenash haga tahteena w el refrences equal 16
    	//case 3 ehna f root
    	
    	
    	
    /*	for(int i = 0; i<8;i++) {
    		 if(x <= midx){
    	            if(y <= midy){
    	                if(z <= midz)
    	                    pos = OctLocations.TopLeftFront.getNumber();
    	                else
    	                    pos = OctLocations.TopLeftBottom.getNumber();
    	            }else{
    	                if(z <= midz)
    	                    pos = OctLocations.BottomLeftFront.getNumber();
    	                else
    	                    pos = OctLocations.BottomLeftBack.getNumber();
    	            }
    	        }else{
    	            if(y <= midy){
    	                if(z <= midz)
    	                    pos = OctLocations.TopRightFront.getNumber();
    	                else
    	                    pos = OctLocations.TopRightBottom.getNumber();
    	            }else {
    	                if(z <= midz)
    	                    pos = OctLocations.BottomRightFront.getNumber();
    	                else
    	                    pos = OctLocations.BottomRightBack.getNumber();
    	            }
    	        }
    	}
    	*/
      //  root.insert(x, y, z, ref);
    }
}