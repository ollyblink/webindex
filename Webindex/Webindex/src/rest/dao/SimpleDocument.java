package rest.dao;
 
public   class SimpleDocument implements Comparable<SimpleDocument>{

	@Override
	public String toString() {
		return "[docid=" + id + ", fij=" + fij + "]";
	}
	private final long id;
	private final Integer fij;

	public SimpleDocument(long id, int fij) {
		this.id = id;
		this.fij = new Integer(fij);
	}
	public long getId() {
		return id;
	} 
	public int getFij() {
		return fij;
	} 
 
	  
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SimpleDocument other = (SimpleDocument) obj;
		if (id != other.id)
			return false;
		return true;
	}
	@Override
	public int compareTo(SimpleDocument o) {
		return -fij.compareTo(o.fij);
	} 

}