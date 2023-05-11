package dbmain;

public enum ChildIndex {
	XloYloZlo(0),
	XloYloZhi(1),
	XloYhiZlo(2),
	XloYhiZhi(3),
	XhiYloZlo(4),
	XhiYloZhi(5),
	XhiYhiZlo(6),
	XhiYhiZhi(7);
	
	
	int index;
	
	 ChildIndex(int index){
	        this.index = index;
	    }

	    int getNumber(){
	        return index;
	    }

}
