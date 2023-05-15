package dbmain;

import java.io.Serializable;

public class Point implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Object x,y,z,ref,pk;
	
	public  Point(Object x,Object y, Object z, Object ref, Object pk)
	{
		this.ref=ref;
		this.x=x;
		this.y=y;
		this.z=z;
		this.pk=pk;
	}

	public Object getPk() {
		return pk;
	}

	public Object getX() {
		return x;
	}

	public void setX(Object x) {
		this.x = x;
	}

	public Object getY() {
		return y;
	}

	public void setY(Object y) {
		this.y = y;
	}

	public Object getZ() {
		return z;
	}

	public void setZ(Object z) {
		this.z = z;
	}

	public Object getRef() {
		return ref;
	}

	public void setRef(Object ref) {
		this.ref = ref;
	}
	
	
}
