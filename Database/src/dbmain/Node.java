package dbmain;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.Vector;
import java.text.SimpleDateFormat;

public class Node implements Serializable{
       
		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		private int MAX_ENTRIES = 0;
        private Vector< Vector<Point> > rowPoint;
        private Node[] children;
        private Object xMin,xMax,yMin,yMax,zMin,zMax;
        private String xType,yType,zType;

        public Node() throws IOException {
        	Properties props = new Properties();
        	InputStream inputStream = getClass().getClassLoader().getResourceAsStream("resources/DBApp.config");
        	props.load(inputStream);

        	this.MAX_ENTRIES = Integer.parseInt(props.getProperty("MaximumEntriesinOctreeNode"));

           rowPoint= new Vector< Vector<Point> >(MAX_ENTRIES);
           children = null;
        }
        public Vector< Vector<Point> > getRows(){
        	return rowPoint;
        }
        public void del(Vector<Point> RemovePoints) {
        	Object x = RemovePoints.get(0).getX();
        	Object y = RemovePoints.get(0).getY();
        	Object z = RemovePoints.get(0).getZ();
        	Object[] Rem = new Object[3];
        	Rem[0]=x;
        	Rem[1]=y;
        	Rem[2]=z;
        	int index=0;
        	Node Target = get(Rem,false);
        	for(int j=0;j<Target.getRows().size();j++) {
				if(Target.getRows().get(j).get(0).getX().equals(Rem[0])
						&&Target.getRows().get(j).get(0).getY().equals(Rem[1])
						&&Target.getRows().get(j).get(0).getZ().equals(Rem[2])) {
					index = j;
					

				}
        }
        	this.get(Rem, false).getRowPoint().remove(index);
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
    							this.setxType(content[2]);
    							if(this.getxType().equals("java.lang.Double"))
    							{
    								this.setxMin(Double.parseDouble(content[6]));
        							this.setxMax(Double.parseDouble(content[7]));
    							}
    							if(this.getxType().equals("java.lang.Integer"))
    							{
    								this.setxMin(Integer.parseInt(content[6]));
        							this.setxMax(Integer.parseInt(content[7]));
    							}
    							if(this.getxType().equals("java.util.Date"))
    							{
    								this.setxMin( DBApp.parseStringToDate(content[6]));
        							this.setxMax( DBApp.parseStringToDate(content[7]));
    							}
    							else if(this.getxType().equals("java.lang.String"))
    							{
    								this.setxMin(content[6]);
        							this.setxMax(content[7]);
    							}
    							
    						     break;
    						case 1:
    							this.setyType(content[2]);
    							if(this.getyType().equals("java.lang.Double"))
    							{
    								this.setyMin(Double.parseDouble(content[6]));
        							this.setyMax(Double.parseDouble(content[7]));
    							}
    							if(this.getyType().equals("java.lang.Integer"))
    							{
    								this.setyMin(Integer.parseInt(content[6]));
        							this.setyMax(Integer.parseInt(content[7]));
    							}
    							if(this.getyType().equals("java.util.Date"))
    							{
    								this.setyMin( DBApp.parseStringToDate(content[6]));
        							this.setyMax( DBApp.parseStringToDate(content[7]));
    							}
    							else if(this.getyType().equals("java.lang.String"))
    							{
    								this.setyMin(content[6]);
        							this.setyMax(content[7]);
    							}
    						    break;
    						case 2:
    							this.setzType(content[2]);
    							if(this.getzType().equals("java.lang.Double"))
    							{
    								this.setzMin(Double.parseDouble(content[6]));
        							this.setzMax(Double.parseDouble(content[7]));
    							}
    							if(this.getzType().equals("java.lang.Integer"))
    							{
    								this.setzMin(Integer.parseInt(content[6]));
        							this.setzMax(Integer.parseInt(content[7]));
    							}
    							if(this.getzType().equals("java.util.Date"))
    							{
    								this.setzMin( DBApp.parseStringToDate(content[6]));
        							this.setzMax( DBApp.parseStringToDate(content[7]));
    							}
    							else if(this.getzType().equals("java.lang.String"))
    							{
    								this.setzMin(content[6]);
        							this.setzMax(content[7]);
    							}
    						    break;
    						default:break;
    						
    						}
    					}
    				}
    			}
    			br.close(); //msh aref law dy hate3mel moshkela
        	}
        	
        }
 

		public void newParent() throws IOException
        {
			int y=0;
        	this.children = new Node[8];
        	for(int i=0;i<this.children.length;i++) {
        		children[i]=new Node();
        		children[i].xType = this.xType;
        		children[i].yType = this.yType;
        		children[i].zType = this.zType;
        	
        		
        	}
        	//IF INT 
        	//If X int
        	Object xMid = null;
        	Object NextX = null;
        	Object yMid = null;
        	Object NextY = null;
        	Object zMid = null;
        	Object NextZ = null;
        	if(xType.equals("java.lang.Integer")||xType.equals("java.lang.Double")) { 
        		if(xType.equals("java.lang.Integer")) {
        	 xMid= ((int)this.xMax- (int)this.xMin)/2 ; 
        	 NextX= (int)xMid+1;}
        		if(xType.equals("java.lang.Double")) {
        			 xMid= ((Double) this.xMax - (Double) this.xMin )/2 ;
                	 NextX= (double) xMid +0.001;
        		}
        	for(int i=0; i< 4; i++)
        	{
        		
        	this.children[i].xMin = xMin;
        	this.children[i].xMax = xMid;
        	this.children[i+4].xMin = NextX;
        	this.children[i+4].xMax = xMax;
        	}
        	}
        	else if(xType.equals("java.lang.String")) {
        		xMid = printMiddleString((String)xMin,(String)xMax);
        		NextX = getNextString((String)xMid);
        		for(int i=0; i< 4; i++)
            	{
            		
            	this.children[i].xMin = xMin;
            	this.children[i].xMax = xMid;
            	this.children[i+4].xMin = NextX;
            	this.children[i+4].xMax = xMax;
            	}
        		
        	}
        	else if(xType.equals("java.util.Date")){
        		try {
					xMid = getMiddleDate((Date)xMin,(Date)xMax);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		try {
					NextX = getMiddleDatePlusOne((Date)xMin,(Date)xMax);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		for(int i=0; i< 4; i++)
            	{
            		
            	this.children[i].xMin = xMin;
            	this.children[i].xMax = xMid;
            	this.children[i+4].xMin = NextX;
            	this.children[i+4].xMax = xMax;
            	}
        	}
        	//If y int
        	if(yType.equals("java.lang.Integer")||yType.equals("java.lang.Double")) {
        		if(yType.equals("java.lang.Integer")) {
           	 yMid= ((int)this.yMax- (int)this.yMin)/2 ;
           	 NextY= (int)yMid+1;}
           		if(yType.equals("java.lang.Double")) {
           			 yMid=  ((Double)this.yMax -(Double) this.yMin) /2 ;
                   	 NextY= (Double) yMid+0.001;
           		}
           		}
        	else if(yType.equals("java.lang.String")) {
        		yMid = printMiddleString((String)yMin,(String)yMax);
        		NextY = getNextString((String)yMid);
        	}
        	else if(yType.equals("java.util.Date")){
        		try {
					yMid = getMiddleDate((Date)yMin,(Date)yMax);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		try {
					NextY = getMiddleDatePlusOne((Date)yMin,(Date)yMax);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		}
        	 for(int i=0; i< 4; i++)
         	{
         	switch(i) {
         	case 0:
         	case 1:
         	this.children[i].yMin=yMin;
         	this.children[i].yMax=yMid;
         	this.children[i+4].yMin=yMin;
         	this.children[i+4].yMax=yMid;
         	
         	break;
         	
         	case 2:
         	case 3:
         	this.children[i].yMin=NextY;
         	this.children[i].yMax=yMax;
         	this.children[i+4].yMin=NextY;
         	this.children[i+4].yMax=yMax;
         	
         	break;
         	
         	default:break;
        	}
         	}
        	
        	 //If z int
        	if(zType.equals("java.lang.Integer")||zType.equals("java.lang.Double")) {
        		if(zType.equals("java.lang.Integer")){
           	 zMid= ((int)this.zMax- (int)this.zMin)/2 ;
           	 NextZ= (int)zMid+1;
           	 }
           		if(zType.equals("java.lang.Double")) {
           			 zMid= ((Double) this.zMax - (Double) this.zMin )/2 ;
                   	 NextZ= (double) zMid +0.001;
           		}
        	}
        	else if(zType.equals("java.lang.String")) {
        		zMid = printMiddleString((String)zMin,(String)zMax);
        		NextZ = getNextString((String)zMid);
        	}
        	else if(zType.equals("java.util.Date")){
        		try {
					zMid = getMiddleDate((Date)zMin,(Date)zMax);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		try {
					NextZ = getMiddleDatePlusOne((Date)zMin,(Date)zMax);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		}
        	 for(int i=0; i< 4; i++)
         	{
         	switch(i) {
         	case 0:
         	case 2:
         	
         	this.children[i].zMin=zMin;
         	this.children[i].zMax=zMid;
         	this.children[i+4].zMin=zMin;
         	this.children[i+4].zMax=zMid;
         	break;
         	case 1:
         	case 3:
         	
         	this.children[i].zMin=NextZ;
         	this.children[i].zMax=zMax;
         	this.children[i+4].zMin=NextZ;
         	this.children[i+4].zMax=zMax;
         	break;
         	default:break;
         
         	
         	
         	
         	}
         		 
         	}
        	
        	
        	//System.out.println(this.yMin.getClass());
        	this.distributeRef();
        }
	
		public void distributeRef() {


			int i=rowPoint.size()-1;
			
			while (!rowPoint.isEmpty()) //infinite
			{	
				Object x= null;
				Object y=  null;
				Object z=  null;

				Object xMinn = null,xMaxx = null,yMinn = null,yMaxx = null,zMinn=null,zMaxx=null;


				//Point p1 = this.rowPoint.get(i).get(0);

				
				for(int j=0; j< this.children.length; j++)
				{

					//=========================================================================

					if(xType.equals("java.lang.Integer"))
					{
						xMinn= (int)this.children[j].xMin;
						xMaxx= (int)this.children[j].xMax;
						x= (int)this.rowPoint.get(i).get(0).getX();
					}

					if(yType.equals("java.lang.Integer"))
					{
						yMinn= (int)this.children[j].yMin;
						yMaxx= (int)this.children[j].yMax;
						y= (int)this.rowPoint.get(i).get(0).getY();
					}
					if(zType.equals("java.lang.Integer")) 
					{
						zMinn= (int)this.children[j].zMin;
						zMaxx= (int)this.children[j].zMax;
						z= (int)this.rowPoint.get(i).get(0).getZ();
					}


					//double
					if(xType.equals("java.lang.Double"))
					{
						
						xMinn = (double) this.children[j].xMin  ;
						xMaxx = (double) this.children[j].xMax  ;
						x= (double) this.rowPoint.get(i).get(0).getX();
					}

					if(yType.equals("java.lang.Double"))
					{
						//System.out.println(this.children[j].yMin.getClass()); 
						yMinn = (double)this.children[j].yMin ;
						yMaxx = (double) this.children[j].yMax;
						y= (double) this.rowPoint.get(i).get(0).getY();
					}


					if(zType.equals("java.lang.Double"))
					{
						zMinn = (double) this.children[j].zMin  ;
						zMaxx =  (double)this.children[j].zMax  ;
						z= (double) this.rowPoint.get(i).get(0).getZ();
					}

					//string 
					if(xType.equals("java.lang.String"))
					{
						xMinn=(String) this.children[j].xMin;
						xMaxx=(String) this.children[j].xMax;
						x= (String) this.rowPoint.get(i).get(0).getX();
					}
					if(yType.equals("java.lang.String"))
					{
						yMinn=(String) this.children[j].yMin;
						yMaxx=(String) this.children[j].yMax;
						y= (String) this.rowPoint.get(i).get(0).getY();
					}

					if(zType.equals("java.lang.String"))
					{
						zMinn=(String) this.children[j].zMin;
						zMaxx=(String) this.children[j].zMax;
						z= (String) this.rowPoint.get(i).get(0).getZ();
					}


					//date 
					if(xType.equals("java.util.Date"))
					{
						xMinn= (Date)this.children[j].xMin;
						xMaxx= (Date) this.children[j].xMax;
						x = (Date) this.rowPoint.get(i).get(0).getX();
					}

					if(yType.equals("java.util.Date"))
					{
						yMinn=(Date)this.children[j].yMin;
						yMaxx=(Date)this.children[j].yMax;
						y = (Date) this.rowPoint.get(i).get(0).getY();
					}

					if(zType.equals("java.util.Date"))
					{
						zMinn= (Date)this.children[j].zMin;
						zMaxx= (Date) this.children[j].zMax;
						z = (Date) this.rowPoint.get(i).get(0).getZ();
					}



					//===================================================


					int compareXMIN= compare(xMinn, x);
					int compareXMax= compare(xMaxx, x);

					int compareYMIN= compare(yMinn, y);
					int compareYMax= compare(yMaxx, y);


					int compareZMIN= compare(zMinn, z);
					int compareZMax= compare(zMaxx, z);
					if( compareXMIN <=0 && compareXMax >=0 &&
							compareYMIN<=0 && compareYMax>=0 &&
							compareZMIN<=0 && compareZMax>=0 
							)
					{
						children[j].insert( this.rowPoint.get(i));
						rowPoint.remove(i);
						i--;
						break;
					}


				}
			}
			this.rowPoint =new  Vector<Vector<Point>>();

		}




        public void insert(Point ref) throws IOException 
        {
       	
        	if (this.getChildren()==null) //new kanet .length ==0
        	{
        		if(this.getRowPoint().size()<this.MAX_ENTRIES) 
        		{
        			Vector <Point> x= new Vector <Point>();
        			x.add(ref);
        			this.rowPoint.add( x );
        			
        			
        		}
        		else 
        		{
        			System.out.println(this.yMin.getClass());
        			this.newParent();
        			System.out.println(this.yMin.getClass());
        			insert(ref);
        		//this.root.insert
        		}
        		
        	}
        	else 
        	{
        		
        		Object x = null;
        		Object y = null;;
        		Object z = null;;
        		
        		Object midx = null;
        		Object midy = null;;
        		Object midz = null;
        		//int
            	if(xType.equals("java.lang.Integer"))
            	{
            		midx = (int) this.xMax+ (int) this.xMin/2;
            		x=(int) ref.getX();
            	}
                
            	if(yType.equals("java.lang.Integer"))
            	{
            	midy = (int) this.yMax+ (int) this.yMin/2;
                y=(int) ref.getY();
            	}
            	if(zType.equals("java.lang.Integer")) 
                {
                midz =(int) this.zMax+ (int) this.zMin/2;
                z=(int) ref.getZ();
                }
                
            	
            	//double
            	if(xType.equals("java.lang.Double"))
                {
                	midx = ((Double) this.xMax +(Double) this.xMin ) /2;
                	x=(double)ref.getX();
                }
                
                if(yType.equals("java.lang.Double"))
                {
                	midy = ((Double) this.yMax +(Double) this.yMin ) /2;
                	y=(double) ref.getY(); 
                }
                
                
                if(zType.equals("java.lang.Double"))
                {
                	midz =((Double) this.zMax +(Double) this.zMin ) /2;
                	z=(double) ref.getZ();
                }
                
                //string 
                if(xType.equals("java.lang.String"))
                {
            		midx = printMiddleString((String)xMin,(String)xMax);
            		x=(String) ref.getX(); 
                }
                if(yType.equals("java.lang.String"))
                {
            		midy = printMiddleString((String)yMin,(String)yMax);
            		y=(String) ref.getY(); 
                }
                if(zType.equals("java.lang.String"))
                {
            		midz = printMiddleString((String)zMin,(String)zMax);
            		z=(String) ref.getZ(); 
                }
             
                //date 
                if(xType.equals("java.util.Date"))
                {
            		try {
    					midx = getMiddleDate((Date)xMin,(Date)xMax);
    					x=(Date) ref.getX();
    					
    				} catch (ParseException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
                }
                
                if(yType.equals("java.util.Date"))
                {
            		try {
    					midy = getMiddleDate((Date)yMin,(Date)yMax);
    					y=(Date) ref.getY();
    					
    				} catch (ParseException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
                }
                
                if(zType.equals("java.util.Date"))
                {
            		try {
    					midz = getMiddleDate((Date)zMin,(Date)zMax);
    					z=(Date) ref.getZ();
    					
    				} catch (ParseException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
                }
              
                
  // ----------------------------------------------------------------------------------------
                
                if(compare(x,midx)<1)
                	{
                	if(compare(y,midy)<1)
                	{
                		{
                		if(compare(z,midz)<1) //low low low
                			this.children[0].insert(ref);
                		else //low low high
                			this.children[1].insert(ref);
                		}
                
                
                	}
                	else 
                	{
                		{
                    		if(compare(z,midz)<1) //low high low 
                    			this.children[2].insert(ref);
                    		else //low high high 
                    			this.children[3].insert(ref);
                    		}
                	}
                	}
                else // x aslun high 
                {
                	if(compare(y,midy)<1)
                	{
                		{
                		if(compare(z,midz)<1) //high low low
                			this.children[4].insert(ref);
                		else //high low high
                			this.children[5].insert(ref);
                		}
                
                
                	}
                	else 
                	{
                		{
                    		if(compare(z,midz)<1) //high high low 
                    			this.children[6].insert(ref);
                    		else //high high high 
                    			this.children[7].insert(ref);
                    		}
                	}
                }
                		
                
        	}
        }
        
        public void  insert(Vector<Point> a)
        {
        	this.rowPoint.add(a);
        }
        
         
        
        public boolean search(Object[] findMe )
        {
        	Object x= findMe[0];
        	Object y= findMe[1];
        	Object z= findMe[2];
        	
        	//lw el point bara el range aslun 
        	//check lw equal mksla afkr now now hfkr fel bus 
        	
        	if(compare(x,this.xMin)<0||compare(y,this.yMin)<0||compare(z,this.zMin)<0
        			||  compare(x,this.xMax)>0|| compare(y,this.yMax)>0|| compare(z,this.zMax)>0)
        		return false;
        	
        	
        	
      		Object midx = null;
    		Object midy = null;;
    		Object midz = null;
    		//int
        	if(xType.equals("java.lang.Integer"))
        	{
        		midx = (int) this.xMax+ (int) this.xMin/2;
        		x=(int) findMe[0];
        	}
            
        	if(yType.equals("java.lang.Integer"))
        	{
        	midy = (int) this.yMax+ (int) this.yMin/2;
            y=(int) findMe[1];
        	}
        	if(zType.equals("java.lang.Integer")) 
            {
            midz =(int) this.zMax+ (int) this.zMin/2;
            z=(int) findMe[2];
            }
            
        	
        	//double
        	if(xType.equals("java.lang.Double"))
            {
            	midx = ((Double) this.xMax +(Double) this.xMin ) /2;
            	x=(double)findMe[0];
            }
            
            if(yType.equals("java.lang.Double"))
            {
            	midy = ((Double) this.yMax +(Double) this.yMin ) /2;
            	y=(Double) findMe[1];
            }
            
            
            if(zType.equals("java.lang.Double"))
            {
            	midz =((Double) this.zMax +(Double) this.zMin ) /2;
            	z=(double) findMe[2];
            }
            
            //string 
            if(xType.equals("java.lang.String"))
            {
        		midx = printMiddleString((String)xMin,(String)xMax);
        		x=(String) findMe[0]; 
            }
            if(yType.equals("java.lang.String"))
            {
        		midy = printMiddleString((String)yMin,(String)yMax);
        		y=(String) findMe[1]; 
            }
            if(zType.equals("java.lang.String"))
            {
        		midz = printMiddleString((String)zMin,(String)zMax);
        		z=(String) findMe[2];
            }
         
            //date 
            if(xType.equals("java.util.Date"))
            {
        		try {
					midx = getMiddleDate((Date)xMin,(Date)xMax);
					x=(Date) findMe[0];
					
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
            
            if(yType.equals("java.util.Date"))
            {
        		try {
					midy = getMiddleDate((Date)yMin,(Date)yMax);
					y=(Date) findMe[1];
					
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
            
            if(zType.equals("java.util.Date"))
            {
        		try {
					midz = getMiddleDate((Date)zMin,(Date)zMax);
					z=(Date) findMe[2];
					
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
            
            
            //------------------------------------------------------------------------
            
            if(this.children==null) // m3ndosh 3yal hdwr fel vector points 3la el point d 
            {
            	for(int i=0; i<this.rowPoint.size(); i++)
            	{
            		if( compare(this.rowPoint.get(i).get(0).getX(), x)==0
            				&& compare(this.rowPoint.get(i).get(0).getY(), y)==0
            				&& compare(this.rowPoint.get(i).get(0).getZ(), z)==0 )
            				return true; 
            	}
            }
            else 
            {
            	   // 3ndo 3yal h2olo yro7 ye search fe anhe wa7ed fehom
                if(compare(x,midx)<=0)
                	{
                	if(compare(y,midy)<=0)
                	{
                		{
                		if(compare(z,midz)<=0) //low low low
                			return this.children[0].search(findMe);
                		else //low low high
                			return this.children[1].search(findMe);
                		}
                
                
                	}
                	else 
                	{
                		{
                    		if(compare(z,midz)<=0) //low high low 
                    			return this.children[2].search(findMe);
                    		else //low high high 
                    			return this.children[3].search(findMe);
                    		}
                	}
                	}
                else // x aslun high 
                {
                	if(compare(y,midy)<=0)
                	{
                		{
                		if(compare(z,midz)<=0) //high low low
                			return this.children[4].search(findMe);
                		else //high low high
                			return this.children[5].search(findMe);
                		}
                
                
                	}
                	else 
                	{
                		{
                    		if(compare(z,midz)<=0) //high high low 
                    			return this.children[6].search(findMe);
                    		else //high high high 
                    			return this.children[7].search(findMe);
                    		}
                	}
                }
                		
                
        	}
            
            
        	// m3nosh 3yal w m3ndosh kalba lesa 
        	return false;
        }
      
        
        public Node get(Object[] findMyNode,  boolean found)
        {
        	
        	//if point == null ??
        	
        	
        	
        	if(search(findMyNode )&&  found ==false ) // lw mwgoda aslun 
        	{
        		
        		Object x= null;
        		Object y= null;
            	Object z = null;
            	
          		Object midx = null;
        		Object midy = null;;
        		Object midz = null;
        		
        		// ana bgeb el values bs 
        		if(xType.equals("java.lang.Integer"))
            	{
            		midx = (int) this.xMax+ (int) this.xMin/2;
            		x=(int) findMyNode[0];
            	}
                
            	if(yType.equals("java.lang.Integer"))
            	{
            	midy = (int) this.yMax+ (int) this.yMin/2;
                y=(int) findMyNode[1];
            	}
            	if(zType.equals("java.lang.Integer")) 
                {
                midz =(int) this.zMax+ (int) this.zMin/2;
                z=(int) findMyNode[2];
                }
                
            	
            	//double
            	if(xType.equals("java.lang.Double"))
                {
                	midx = ((Double) this.xMax +(Double) this.xMin ) /2;
                	x=(double)findMyNode[0];
                }
                
                if(yType.equals("java.lang.Double"))
                {
                	midy = ((Double) this.yMax +(Double) this.yMin ) /2;
                	y=(Double) findMyNode[1];
                }
                
                
                if(zType.equals("java.lang.Double"))
                {
                	midz =((Double) this.zMax +(Double) this.zMin ) /2;
                	z=(double) findMyNode[2];
                }
                
                //string 
                if(xType.equals("java.lang.String"))
                {
            		midx = printMiddleString((String)xMin,(String)xMax);
            		x=(String) findMyNode[0];
                }
                if(yType.equals("java.lang.String"))
                {
            		midy = printMiddleString((String)yMin,(String)yMax);
            		y=(String) findMyNode[1];
                }
                if(zType.equals("java.lang.String"))
                {
            		midz = printMiddleString((String)zMin,(String)zMax);
            		z=(String) findMyNode[2];
                }
             
                //date 
                if(xType.equals("java.util.Date"))
                {
            		try {
    					midx = getMiddleDate((Date)xMin,(Date)xMax);
    					x=(Date) findMyNode[0];
    					
    				} catch (ParseException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
                }
                
                if(yType.equals("java.util.Date"))
                {
            		try {
    					midy = getMiddleDate((Date)yMin,(Date)yMax);
    					y=(Date) findMyNode[1];
    					
    				} catch (ParseException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
                }
                
                if(zType.equals("java.util.Date"))
                {
            		try {
    					midz = getMiddleDate((Date)zMin,(Date)zMax);
    					z=(Date) findMyNode[2];
    					
    				} catch (ParseException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
                }
        		
                
                
                //------------------------------------------------------------------------
                
                if(this.children==null) // m3ndosh 3yal hdwr fel vector points 3la el point d 
                {
                	for(int i=0; i<this.rowPoint.size(); i++)
                	{
                		if( compare(this.rowPoint.get(i).get(0).getX(), x)==0
                				&& compare(this.rowPoint.get(i).get(0).getY(), y)==0
                				&& compare(this.rowPoint.get(i).get(0).getZ(), z)==0 )
                		{
                				found=true;
                				return this; 
                		}
                	}
                }
                else 
                {
                	   // 3ndo 3yal h2olo yro7 ye search fe anhe wa7ed fehom
                    if(compare(x,midx)<1)
                    	{
                    	if(compare(y,midy)<1)
                    	{
                    		{
                    		if(compare(z,midz)<1) //low low low
                    			return this.children[0].get(findMyNode, found);
                    		else //low low high
                    			return this.children[1].get(findMyNode, found);
                    		}
                    
                    
                    	}
                    	else 
                    	{
                    		{
                        		if(compare(z,midz)<1) //low high low 
                        			return this.children[2].get(findMyNode, found);
                        		else //low high high 
                        			return this.children[3].get(findMyNode, found);
                        		}
                    	}
                    	}
                    else // x aslun high 
                    {
                    	if(compare(y,midy)<1)
                    	{
                    		{
                    		if(compare(z,midz)<1) //high low low
                    			return this.children[4].get(findMyNode, found);
                    		else //high low high
                    			return this.children[5].get(findMyNode, found);
                    		}
                    
                    
                    	}
                    	else 
                    	{
                    		{
                        		if(compare(z,midz)<1) //high high low 
                        			return this.children[6].get(findMyNode, found);
                        		else //high high high 
                        			return this.children[7].get(findMyNode, found);
                        		}
                    	}
                    }
                    		
                    
            	}
  
                
        			}
        	//lw hya msh mwgoda
        	return null; 
        			
        }

   
        
        public static int compare(Object o1, Object o2) {
            if (o1 instanceof Integer && o2 instanceof Integer) {
                return Integer.compare((int) o1, (int) o2);
            } else if (o1 instanceof Double && o2 instanceof Double) {
                return Double.compare((double) o1, (double) o2);
            } else if (o1 instanceof String && o2 instanceof String) {
            	String m = (String) o1;
            	String n = (String) o2;
            	return m.compareToIgnoreCase(n);
                //return ((String) o1).compareTo((String) o2);
            } else if (o1 instanceof Date && o2 instanceof Date) {
                return ((Date) o1).compareTo((Date) o2);
            } else {
                throw new IllegalArgumentException("Objects must be of the same type");
            }
        }
        
public Vector<Vector<Point>> getX(Object x, String op)
{
	
	Vector<Vector<Point>> out= new Vector<Vector<Point>>();

	Object midx = null;
	if(xType.equals("java.lang.Integer"))
	{
		midx = (int) this.xMax+ (int) this.xMin/2;
		x=(int) x;
	}
	
	
	if(xType.equals("java.lang.Double"))
    {
    	midx = ((Double) this.xMax +(Double) this.xMin ) /2;
    	x=(double)x;
    }
	
	
	  if(xType.equals("java.lang.String"))
      {
  		midx = printMiddleString((String)xMin,(String)xMax);
  		x=(String) x;
      }
	
      if(xType.equals("java.util.Date"))
      {
  		try {
				midx = getMiddleDate((Date)xMin,(Date)xMax);
				x=(Date) x;
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
      }
      
      
      
      if(this.children==null) // m3ndosh 3yal hdwr fel vector points 3la el point d 
      {
      	for(int i=0; i<this.rowPoint.size(); i++)
      	{
      		if(op.equals("="))
      		{
      		if( compare(this.rowPoint.get(i).get(0).getX(), x)==0)
      		{
      			 out.add(rowPoint.get(i));
      			 return out;
      		}
      		}
      		else if(op.equals(">="))
      		{
      		if( compare(this.rowPoint.get(i).get(0).getX(), x)>=0)
      		{
      			 out.add(rowPoint.get(i));
      			 return out;
      		}
      		}
      		else if(op.equals("<="))
      		{
      		if( compare(this.rowPoint.get(i).get(0).getX(), x)<=0)
      		{
      			 out.add(rowPoint.get(i));
      			 return out;
      		}
      		}
      		else if(op.equals(">"))
      		{
      		if( compare(this.rowPoint.get(i).get(0).getX(), x)>0)
      		{
      			 out.add(rowPoint.get(i));
      			 return out;
      		}
      		}
      		else if(op.equals("<"))
      		{
      		if( compare(this.rowPoint.get(i).get(0).getX(), x)<0)
      		{
      			 out.add(rowPoint.get(i));
      			 return out;
      		}
      		}
      	
      	}

        }
      else 
      {
    	   if(compare(x,midx)>0)
    	   {
    		   out.add((Vector)this.children[4].getX(x,op));
    		   out.add((Vector)this.children[5].getX(x,op));
    		   out.add((Vector)this.children[6].getX(x,op));
    		   out.add((Vector)this.children[7].getX(x,op));
    	   }
    	   else
    	   {
    		   out.add((Vector)this.children[0].getX(x,op));
    		   out.add((Vector)this.children[1].getX(x,op));
    		   out.add((Vector)this.children[2].getX(x,op));
    		   out.add((Vector)this.children[3].getX(x,op));
    	   }
      }

      return out;
}

public Vector<Vector<Point>> getY(Object y, String op)
{
	
	Vector<Vector<Point>> out= new Vector<Vector<Point>>();

	Object midy = null;
	if(yType.equals("java.lang.Integer"))
	{
		midy = (int) this.yMax+ (int) this.yMin/2;
		y=(int) y;
	}
	
	
	if(yType.equals("java.lang.Double"))
    {
    	midy = ((Double) this.yMax +(Double) this.yMin ) /2;
    	y=(double)y;
    }
	
	
	  if(yType.equals("java.lang.String"))
      {
  		midy = printMiddleString((String)yMin,(String)yMax);
  		y=(String) y;
      }
	
      if(yType.equals("java.util.Date"))
      {
  		try {
				midy = getMiddleDate((Date)yMin,(Date)yMax);
				y=(Date) y;
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
      }
      
      
      
      if(this.children==null) // m3ndosh 3yal hdwr fel vector points 3la el point d 
      {
      	for(int i=0; i<this.rowPoint.size(); i++)
      	{
      		if(op.equals("="))
      		{
      		if( compare(this.rowPoint.get(i).get(0).getY(), y)==0)
      		{
      			 out.add(rowPoint.get(i));
      			 return out;
      		}
      		}
      		else if(op.equals(">="))
      		{
      		if( compare(this.rowPoint.get(i).get(0).getY(), y)>=0)
      		{
      			 out.add(rowPoint.get(i));
      			 return out;
      		}
      		}
      		else if(op.equals("<="))
      		{
      		if( compare(this.rowPoint.get(i).get(0).getY(), y)<=0)
      		{
      			 out.add(rowPoint.get(i));
      			 return out;
      		}
      		}
      		else if(op.equals(">"))
      		{
      		if( compare(this.rowPoint.get(i).get(0).getY(), y)>0)
      		{
      			 out.add(rowPoint.get(i));
      			 return out;
      		}
      		}
      		else if(op.equals("<"))
      		{
      		if( compare(this.rowPoint.get(i).get(0).getY(), y)<0)
      		{
      			 out.add(rowPoint.get(i));
      			 return out;
      		}
      		}
      	
      	}

        }
      else 
      {
    	   if(compare(y,midy)>0)
    	   {
    		   out.add((Vector)this.children[4].getY(y,op));
    		   out.add((Vector)this.children[5].getY(y,op));
    		   out.add((Vector)this.children[6].getY(y,op));
    		   out.add((Vector)this.children[7].getY(y,op));
    	   }
    	   else
    	   {
    		   out.add((Vector)this.children[0].getY(y,op));
    		   out.add((Vector)this.children[1].getY(y,op));
    		   out.add((Vector)this.children[2].getY(y,op));
    		   out.add((Vector)this.children[3].getY(y,op));
    	   }
      }

      return out;
}


public Vector<Vector<Point>> getZ(Object z, String op)
{
	
	Vector<Vector<Point>> out= new Vector<Vector<Point>>();

	Object midz = null;
	if(zType.equals("java.lang.Integer"))
	{
		midz = (int) this.zMax+ (int) this.yMin/2;
		z=(int) z;
	}
	
	
	if(zType.equals("java.lang.Double"))
    {
    	midz = ((Double) this.zMax+(Double) this.zMin ) /2;
    	z=(double)z;
    }
	
	
	  if(zType.equals("java.lang.String"))
      {
  		midz = printMiddleString((String)zMin,(String)zMax);
  		z=(String) z;
      }
	
      if(zType.equals("java.util.Date"))
      {
  		try {
				midz = getMiddleDate((Date)zMin,(Date)zMax);
				z=(Date) z;
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
      }
      
      
      
      if(this.children==null) // m3ndosh 3yal hdwr fel vector points 3la el point d 
      {
      	for(int i=0; i<this.rowPoint.size(); i++)
      	{
      		if(op.equals("="))
      		{
      		if( compare(this.rowPoint.get(i).get(0).getZ(), z)==0)
      		{
      			 out.add(rowPoint.get(i));
      			 return out;
      		}
      		}
      		else if(op.equals(">="))
      		{
      		if( compare(this.rowPoint.get(i).get(0).getZ(), z)>=0)
      		{
      			 out.add(rowPoint.get(i));
      			 return out;
      		}
      		}
      		else if(op.equals("<="))
      		{
      		if( compare(this.rowPoint.get(i).get(0).getZ(), z)<=0)
      		{
      			 out.add(rowPoint.get(i));
      			 return out;
      		}
      		}
      		else if(op.equals(">"))
      		{
      		if( compare(this.rowPoint.get(i).get(0).getZ(), z)>0)
      		{
      			 out.add(rowPoint.get(i));
      			 return out;
      		}
      		}
      		else if(op.equals("<"))
      		{
      		if( compare(this.rowPoint.get(i).get(0).getZ(), z)<0)
      		{
      			 out.add(rowPoint.get(i));
      			 return out;
      		}
      		}
      	
      	}

        }
      else 
      {
    	   if(compare(z,midz)>0)
    	   {
    		   out.addAll(this.children[4].getZ(z,op));
    		   out.addAll(this.children[5].getZ(z,op));
    		   out.addAll(this.children[6].getZ(z,op));
    		   out.addAll(this.children[7].getZ(z,op));
    	   }
    	   else
    	   {
    		   out.addAll(this.children[0].getZ(z,op));
    		   out.addAll(this.children[1].getZ(z,op));
    		   out.addAll(this.children[2].getZ(z,op));
    		   out.addAll(this.children[3].getZ(z,op));
    	   }
      }

      return out;
}



// ========================HELPERS =====================


public static Date getMiddleDate(Date startDate, Date endDate) throws ParseException {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//    Date start = dateFormat.parse(startDate);
//    Date end = dateFormat.parse(endDate);
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(startDate);
    long startTimeInMillis = calendar.getTimeInMillis();
    calendar.setTime(endDate);
    long endTimeInMillis = calendar.getTimeInMillis();
    long middleTimeInMillis = (startTimeInMillis + endTimeInMillis) / 2;
    Date middleDate = new Date(middleTimeInMillis);
    return middleDate;
}

public static Date getMiddleDatePlusOne(Date startDate, Date endDate) throws ParseException {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//    Date start = dateFormat.parse(startDate);
//    Date end = dateFormat.parse(endDate);
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(startDate);
    long startTimeInMillis = calendar.getTimeInMillis();
    calendar.setTime(endDate);
    long endTimeInMillis = calendar.getTimeInMillis();
    long middleTimeInMillis = (startTimeInMillis + endTimeInMillis) / 2;
    Date middleDate = new Date(middleTimeInMillis);

    // Add one day to the middle date
    calendar.setTime(middleDate);
    calendar.add(Calendar.DAY_OF_MONTH, 1);
    Date nextDay = calendar.getTime();

    return nextDay;
}
public static String getNextString(String s) {
    // Convert the string into a number in base 26
    int n = 0;
    for (int i = 0; i < s.length(); i++) {
        n = n * 26 + (int)(s.charAt(i) - 'a' + 1);
    }

    // Add 1 to the number
    n++;

    // Convert the resulting number back into a string in base 26
    StringBuilder sb = new StringBuilder();
    while (n > 0) {
        n--;
        sb.append((char)('a' + n % 26));
        n /= 26;
    }

    return sb.reverse().toString();
}
static String printMiddleString(String S, String T)
{
    S=S.toLowerCase();
    T=T.toLowerCase();
    char a = 'a';
    int max,N;

    if(S.length()>T.length()){
       max=S.length();
       while(T.length()!=max)
       {
        T=T+a;
       }
    }

    else {
    max=T.length();
    while(S.length()!=max)
       {
        S=S+a;
       }
    }
    N=max;

    int[] a1 = new int[N + 1];

    for (int i = 0; i < N; i++) {
        a1[i + 1] = (int)S.charAt(i) - 97
                    + (int)T.charAt(i) - 97;
    }
    for (int i = N; i >= 1; i--) {
        a1[i - 1] += (int)a1[i] / 26;
        a1[i] %= 26;
    }

    for (int i = 0; i <= N; i++) {
        if ((a1[i] & 1) != 0) {

            if (i + 1 <= N) {
                a1[i + 1] += 26;
            }
        }

        a1[i] = (int)a1[i] / 2;
    }
    String res="";

    for (int i = 1; i <= N; i++) {
        res+=(char)(a1[i] + 97);
    }
    return res;
}



// ============================SETTERS AND GETTERS ===================================
public Object getxMin() {
	return xMin;
}
public Object getxMax() {
	return xMax;
}
public Object getyMin() {
	return yMin;
}
public Object getyMax() {
	return yMax;
}
public Object getzMin() {
	return zMin;
}
public Object getzMax() {
	return zMax;
}
public Node[] getChildren() {
	return children;
}

public void setChildren(Node[] children) {
	this.children = children;
}

public Vector<Vector<Point>> getRowPoint() {
	return rowPoint;
}

public void setRowPoint(Vector<Vector<Point>> rowPoint) {
	this.rowPoint = rowPoint;
}

public void setxMin(Object xMin) {
	this.xMin = xMin;
}

public void setxMax(Object xMax) {
	this.xMax = xMax;
}

public String getxType() {
	return xType;
}

public void setyMin(Object yMin) {
	this.yMin = yMin;
}

public void setyMax(Object yMax) {
	this.yMax = yMax;
}

public void setzMin(Object zMin) {
	this.zMin = zMin;
}

public void setzMax(Object zMax) {
	this.zMax = zMax;
}

public void setxType(String xType) {
	this.xType = xType;
}

public String getyType() {
	return yType;
}

public void setyType(String yType) {
	this.yType = yType;
}

public String getzType() {
	return zType;
}

public void setzType(String zType) {
	this.zType = zType;
}







}


























