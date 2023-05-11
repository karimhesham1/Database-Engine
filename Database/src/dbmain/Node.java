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
        	this.children = new Node[8];
        	this.distributeRef();
        }

        public void distributeRef() {
        	int size = this.rowPoint.size();
        	for(int i =0;i<size;i++) {
        		Object x = this.rowPoint.get(i).getX();
        		Object y = this.rowPoint.get(i).getY();
        		Object z = this.rowPoint.get(i).getZ();
        		Point p1 = this.rowPoint.get(i);
        		
        		
        	}
        	
        	
        }
        public void insert(Point ref) {
        	
        	if (this.getChildren().length==0)
        	{
        		if(this.getRows().size()<16) 
        		{
        			this.rowPoint.add(ref);
        		}
        		else 
        		{
        			this.newParent();
        			//this.root.insert
        		}
        	
        	
        		
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

































