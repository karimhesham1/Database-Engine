package dbmain;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.Vector;

public class OctTree implements Serializable {
    /**
	 * 
	 */	private static final long serialVersionUID = 1L;
	private Node root;
	private String xName,yName,zName;
	private String tableName;
    
	

	public Node getRoot() {
		return root;
	}
    public String getxName() {
		return xName;
	}
    public String getName() {
    	return this.tableName;
    }

	public String getyName() {
		return yName;
	}

	public String getzName() {
		return zName;
	}

	public OctTree(String tableName,String[] columns) throws DBAppException, IOException {
        this.root = new Node();
        try {
			this.setTypes(tableName,columns);
		} catch (IOException e) {
			e.printStackTrace();
			throw new DBAppException();
		}
        //this.root.newParent();
        this.tableName = tableName;
       
        this.xName=columns[0];
        this.yName=columns[1];
        this.zName=columns[2];
       
     
    }
	
	public void printOctTree(Node node, String indent) {
	    if (node == null) {
	        return;
	    }

	    // Print node information
	    System.out.println(indent + "+- Node: " + " xMin:" + node.getxMin() + " xMax:" + node.getxMax() +"   "+ " yMin:" + node.getyMin() + " yMax:"  +node.getyMax()+"   "+ " zMin:"+ node.getzMin() + " zMax:" + node.getzMax());
	    if (node.getRowPoint() != null) {
	        for (Vector<Point> vector : node.getRowPoint()) {
	            for (Point point : vector) {
	                System.out.println(indent + "   |- Point: (" + point.getX() + ", " + point.getY() + ", " + point.getZ() + ")");
	            }
	        }
	    }

	    // Print child nodes recursively
	    if (node.getChildren() != null) {
	        for (int i = 0; i < node.getChildren().length; i++) {
	            Node child = node.getChildren()[i];
	            System.out.println(indent + "   |- Child " + i + ":");
	            printOctTree(child, indent + "      ");
	        }
	    }
	}


    public void setTypes(String tableName,String[] columns) throws IOException {
    	for(int i=0;i<columns.length;i++) {
			boolean flag = false;
			String insertedColName = columns[i];
			
			BufferedReader br = new BufferedReader(new FileReader("metadata.csv"));
			String line = br.readLine();
			boolean firstloop =true;
			while (line != null) 
			{

				if (firstloop)
					line = br.readLine();

				firstloop = false;
				
				String[] content = line.split(",");

				//					if(!(columnNames.hasMoreElements()) )
				//						break;

				if(content[0].equals(tableName))
				{
					



					if (content[1].equals(insertedColName))
					{

						switch(i) {
						case 0:
							root.setxType(content[2]);
							root.setxMin(content[6]);
						    root.setxMax(content[7]);
						     break;
						case 1:
							root.setyType(content[2]);
							root.setyMin(content[6]);
						    root.setyMax(content[7]);
						    break;
						case 2:
							root.setzType(content[2]);
							root.setzMin(content[6]);
						    root.setzMax(content[7]);
						    break;
						default:break;
						
						}
					}
				}
				line = br.readLine();
			}
			br.close(); //msh aref law dy hate3mel moshkela
    	}
    	
    }

    public void insert(Object x, Object y, Object z, Object ref, Object pk) throws IOException 
    {
    	Point p1 = new Point(x,y,z,ref, pk);
    	root.insert(p1);
    	
    	
    }
}