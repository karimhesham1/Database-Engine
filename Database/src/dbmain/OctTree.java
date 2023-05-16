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
				//						break;aa

				if(content[0].equals(tableName))
				{
					



					if (content[1].equals(insertedColName))
					{

						switch(i) {
						case 0:
							this.root.setxType(content[2]);
							if(this.root.getxType().equals("java.lang.Double"))
							{
								this.root.setxMin(Double.parseDouble(content[6]));
    							this.root.setxMax(Double.parseDouble(content[7]));
							}
							if(this.root.getxType().equals("java.lang.Integer"))
							{
								this.root.setxMin(Integer.parseInt(content[6]));
    							this.root.setxMax(Integer.parseInt(content[7]));
							}
							if(this.root.getxType().equals("java.util.Date"))
							{
								this.root.setxMin( DBApp.parseStringToDate(content[6]));
    							this.root.setxMax( DBApp.parseStringToDate(content[7]));
							}
							else if(this.root.getxType().equals("java.lang.String"))
							{
								this.root.setxMin(content[6]);
    							this.root.setxMax(content[7]);
							}
							
						     break;
						case 1:
							this.root.setyType(content[2]);
							if(this.root.getyType().equals("java.lang.Double"))
							{
								
								this.root.setyMin(Double.parseDouble(content[6]));
    							this.root.setyMax(Double.parseDouble(content[7]));
    							//System.out.println(this.root.getyMin().getClass());
							}
							if(this.root.getyType().equals("java.lang.Integer"))
							{
								this.root.setyMin(Integer.parseInt(content[6]));
    							this.root.setyMax(Integer.parseInt(content[7]));
							}
							if(this.root.getyType().equals("java.util.Date"))
							{
								this.root.setyMin( DBApp.parseStringToDate(content[6]));
    							this.root.setyMax( DBApp.parseStringToDate(content[7]));
							}
							else if(this.root.getyType().equals("java.lang.String"))
							{
								this.root.setyMin(content[6]);
    							this.root.setyMax(content[7]);
							}
						    break;
						case 2:
							this.root.setzType(content[2]);
							if(this.root.getzType().equals("java.lang.Double"))
							{
								this.root.setzMin(Double.parseDouble(content[6]));
    							this.root.setzMax(Double.parseDouble(content[7]));
							}
							if(this.root.getzType().equals("java.lang.Integer"))
							{
								this.root.setzMin(Integer.parseInt(content[6]));
    							this.root.setzMax(Integer.parseInt(content[7]));
							}
							if(this.root.getzType().equals("java.util.Date"))
							{
								this.root.setzMin( DBApp.parseStringToDate(content[6]));
    							this.root.setzMax( DBApp.parseStringToDate(content[7]));
							}
							else if(this.root.getzType().equals("java.lang.String"))
							{
								this.root.setzMin(content[6]);
    							this.root.setzMax(content[7]);
							}
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