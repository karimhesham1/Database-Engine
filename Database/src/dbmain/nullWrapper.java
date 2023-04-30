package dbmain;

import java.io.Serializable;

public class nullWrapper implements Serializable {
	
	/**
	 * 
	 */
	transient String value = "null";
	private static final long serialVersionUID = 1L;

	public nullWrapper() {}
	
	public String toString()
	{
		return this.value;
	}
}
