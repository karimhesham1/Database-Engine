package dbmain;

import java.util.Vector;

public class Node {
        private static final int MAX_ENTRIES = 16;
        private Vector<Point> rowPoint;
        private Node[] children;
        private Object xMin,xMax,yMin,yMax,zMin,zMax;

        public Node() {
           rowPoint= new Vector<Point>(MAX_ENTRIES);
           children = null;
        }
        
        public void newParent()
        {
        	//IF INT 
        	int xRange= ((int)this.xMax- (int)this.xMin)/8 ;
        	 int yRange= ((int)this.yMax- (int)this.yMin)/8 ;
        	 int zRange= ((int)this.zMax- (int)this.zMin)/8 ;
        	this.children = new Node[8];
        	for(int i=0; i< this.children.length; i++)
        	{
        		
        		 if(i==0)
        		 {
        		
        			 this.children[i].xMin=this.xMin;
        			 this.children[i].yMin=this.yMin;
        			 this.children[i].zMin=this.zMin;
        		 }
        		 else 
        		 {
        			 this.children[i].xMin=  (int)this.children[i-1].xMax +1;
        			 this.children[i].yMin=  (int)this.children[i-1].yMax +1;
        			 this.children[i].zMin=  (int)this.children[i-1].zMax +1;
        		 }
        		 this.children[i].xMax=(int)this.children[i].xMin+ xRange;
        		 this.children[i].yMax=(int)this.children[i].yMin+ yRange;
        		 this.children[i].zMax=(int)this.children[i].zMin+ zRange;
        		 
        		 
        		 
        	}
        	//IF DOUBLE
        	 double xRangeD= ((double) this.xMax- (double)this.xMin)/8 ;
        	 double yRangeD= ((double)this.yMax- (double)this.yMin)/8 ;
        	 double zRangeD= ((double)this.zMax- (double)this.zMin)/8 ;
        	this.children = new Node[8];
        	for(int i=0; i< this.children.length; i++)
        	{
        		
        		 if(i==0)
        		 {
        		
        			 this.children[i].xMin=this.xMin;
        			 this.children[i].yMin=this.yMin;
        			 this.children[i].zMin=this.zMin;
        		 }
        		 else 
        		 {
        			 this.children[i].xMin=  (int)this.children[i-1].xMax +1;
        			 this.children[i].yMin=  (int)this.children[i-1].yMax +1;
        			 this.children[i].zMin=  (int)this.children[i-1].zMax +1;
        		 }
        		 this.children[i].xMax=(int)this.children[i].xMin+ xRangeD;
        		 this.children[i].yMax=(int)this.children[i].yMin+ yRangeD;
        		 this.children[i].zMax=(int)this.children[i].zMin+ zRangeD;
        		 
        		 
        		 
        	}
        	
        	
        	
        	
        	
        	
        	this.distributeRef();
        }

        public void distributeRef() {
//        	int size = this.rowPoint.size();
//        	for(int i =0;i<size;i++) {
//        		Object x = this.rowPoint.get(i).getX();
//        		Object y = this.rowPoint.get(i).getY();
//        		Object z = this.rowPoint.get(i).getZ();
//        		Point p1 = this.rowPoint.get(i);
//        		
//        		
//        		
//        		
//        	}
        	Vector<Point> tmp= new Vector<Point>(this.rowPoint.size());
        	int i=0;
        	while(!tmp.isEmpty())
        	{
        		
        		int x = (int) this.rowPoint.get(i).getX();
        		int y = (int) this.rowPoint.get(i).getY();
        		int z = (int) this.rowPoint.get(i).getZ();
        		Point p1 = this.rowPoint.get(i);
        		
        		
        		for(int j=0; j< this.children.length; j++)
        		{	
        			if( (int)this.children[j].xMin>x && (int)this.children[j].xMax<x
        				&&	(int)this.children[j].yMin>y && (int)this.children[j].yMax<y	
        				&&	(int)this.children[j].zMin>z && (int)this.children[j].zMax<z	
        					)
        			{
        				insert(p1);
        				tmp.remove(i);
        				break;
        			}
        		}
        		
        		
        		i++;
        		
        	}
        	
        	
        }
        public void insert(Point ref) {
        	
        	int x=(int) ref.getX();
        	
        	
        	if (this.getChildren().length==0)
        	{
        		if(this.getRows().size()<16) 
        		{
        			this.rowPoint.add(ref);
        		}
        		else 
        		{
        			this.newParent();
        			insert(ref);
        		//this.root.insert
        		}
        		
        	}
        	else 
        	{
        		
        	}
//            if (children != null) {
//                int octant = getOctantIndex(x, y, z);
//                children[octant].insert(x, y, z);
//            } else {
//                if (xs.length < MAX_ENTRIES) {
//                    // There's still room in this node, add the point
//                    int i = xs.length;
//                    xs[i] = x;
//                    ys[i] = y;
//                    zs[i] = z;
//                } else {
//                    // Node is full, split into octants
//                    children = new Node[8];
//                    double cx = (xs[0] + xs[MAX_ENTRIES-1]) / 2;
//                    double cy = (ys[0] + ys[MAX_ENTRIES-1]) / 2;
//                    double cz = (zs[0] + zs[MAX_ENTRIES-1]) / 2;
//                    for (int i = 0; i < MAX_ENTRIES; i++) {
//                        int octant = getOctantIndex(xs[i], ys[i], zs[i]);
//                        if (children[octant] == null) {
//                            children[octant] = new Node();
//                        }
//                        children[octant].insert(xs[i], ys[i], zs[i]);
//                    }
//                    xs = null;
//                    ys = null;
//                    zs = null;
//                    insert(x, y, z); // Insert the new point into the new structure
//                }
            }
        

//        private int getOctantIndex(double x, double y, double z) {
//            int octant = 0;
//            if (x >= xs[0] && x <= xs[MAX_ENTRIES-1]) octant |= 1;
//            if (y >= ys[0] && y <= ys[MAX_ENTRIES-1]) octant |= 2;
//            if (z >= zs[0] && z <= zs[MAX_ENTRIES-1]) octant |= 4;
//            return octant;
//        }
        public Node[] getChildren() {
        	return children;
        }
        public Vector<Point> getRows(){
        	return rowPoint;
        }
    }

































