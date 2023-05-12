package dbmain;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import java.text.SimpleDateFormat;

public class Node {
        private static final int MAX_ENTRIES = 16;
        private Vector<Point> rowPoint;
        private Node[] children;
        private Object xMin,xMax,yMin,yMax,zMin,zMax;
        private String xType,yType,zType;

        public Node() {
           rowPoint= new Vector<Point>(MAX_ENTRIES);
           children = null;
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

		public void setRowPoint(Vector<Point> rowPoint) {
			this.rowPoint = rowPoint;
		}

		public void setChildren(Node[] children) {
			this.children = children;
		}

		public void setxMin(Object xMin) {
			this.xMin = xMin;
		}

		public void setxMax(Object xMax) {
			this.xMax = xMax;
		}

		public void newParent()
        {
        	this.children = new Node[8];
        	for(int i=0;i<this.children.length;i++) {
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
        		
        	 xMid= ((int)this.xMax- (int)this.xMin)/2 ;  //el double hena hayegy error ya nouuuuuuuuuuuuuuuuuuuuurrrrrrrrrrrrr
        	 NextX= (int)xMid+1;
        		if(xType.equals("java.lang.Double")) {
        			 xMid= ((Double)this.xMax- (Double)this.xMin)/2 ;
                	 NextX= (Double)xMid+1;
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
					xMid = getMiddleDate((String)xMin,(String)xMax);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		//NextX Kamel henaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa w ba3daha e3mel nafs el kalam L y and z
        	}
        	//If y int
        	if(yType.equals("java.lang.Integer")||yType.equals("java.lang.Double")) {
        		
           	 yMid= ((int)this.yMax- (int)this.yMin)/2 ;
           	 NextY= (int)yMid+1;
           		if(yType.equals("java.lang.Double")) {
           			 yMid= ((Double)this.yMax- (Double)this.yMin)/2 ;
                   	 NextY= (Double)yMid+1;
           		}
           		}
        	else if(xType.equals("java.lang.String")) {
        		yMid = printMiddleString((String)yMin,(String)yMax);
        		NextY = getNextString((String)yMid);
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
        		
           	 zMid= ((int)this.zMax- (int)this.zMin)/2 ;
           	 NextZ= (int)zMid+1;
           		if(zType.equals("java.lang.Double")) {
           			 zMid= ((Double)this.zMax- (Double)this.zMin)/2 ;
                   	 NextZ= (Double)zMid+1;
           		}
        	}
        	else if(zType.equals("java.lang.String")) {
        		zMid = printMiddleString((String)zMin,(String)zMax);
        		NextZ = getNextString((String)zMid);
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
        	
        	
        		 
        	
        	
        	
        	
        

        	
        	
        	
        	this.distributeRef();
        }
		public static String getMiddleDate(String startDate, String endDate) throws ParseException {
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	        Date start = dateFormat.parse(startDate);
	        Date end = dateFormat.parse(endDate);
	        Calendar calendar = Calendar.getInstance();
	        calendar.setTime(start);
	        long startTimeInMillis = calendar.getTimeInMillis();
	        calendar.setTime(end);
	        long endTimeInMillis = calendar.getTimeInMillis();
	        long middleTimeInMillis = (startTimeInMillis + endTimeInMillis) / 2;
	        Date middleDate = new Date(middleTimeInMillis);
	        return dateFormat.format(middleDate);
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

        public void distributeRef() {
//        	int size = this.rowPoint.size();
//        	for(int i =0;i<size;i++) {
//        		Object x = this.rowPoint.get(i).getX();
//        		Object y = this.rowPoint.get(i).getY();
//        		Object z = this.rowPoint.get(i).getZ();
//        		Point p1 = this.rowPoint.get(i);
//  
//        	}
        	Vector<Point> tmp= new Vector<Point>(this.rowPoint.size());
        	tmp = rowPoint; //new
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
        				i--; //new
        				break;
        			}
        		}
        		
        		
        		i++;
        		
        	}
        	
        	
        }
        public void insert(Point ref) 
        {
        	
        	
        
        	
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
                	midx = (double)this.xMax+(double) this.xMin/2;
                	x=(double)ref.getX();
                }
                
                if(yType.equals("java.lang.Double"))
                {
                	midy = (double)this.yMax+ (double) this.yMin/2;
                	y=(int) ref.getY(); 
                }
                
                
                if(zType.equals("java.lang.Double"))
                {
                	midz =(double) this.zMax+ (double) this.zMin/2;
                	z=(double) ref.getZ();
                }
                
                //string 
                if(xType.equals("java.lang.String"))
                {
            		midy = printMiddleString((String)yMin,(String)yMax);
            		y=(String) ref.getY(); 
                }
             //mksla akmel
                //date 
                
                
                
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


        public Node[] getChildren() {
        	return children;
        }
        public Vector<Point> getRows(){
        	return rowPoint;
        }
    

        public static int compare(Object o1, Object o2) {
            if (o1 instanceof Integer && o2 instanceof Integer) {
                return Integer.compare((int) o1, (int) o2);
            } else if (o1 instanceof Double && o2 instanceof Double) {
                return Double.compare((double) o1, (double) o2);
            } else if (o1 instanceof String && o2 instanceof String) {
                return ((String) o1).compareTo((String) o2);
            } else if (o1 instanceof Date && o2 instanceof Date) {
                return ((Date) o1).compareTo((Date) o2);
            } else {
                throw new IllegalArgumentException("Objects must be of the same type");
            }
        }


        }




























