package index.utils.indexchangelistener;

public interface IIndexChangeSubject {
	public void addIndexChangeListener(IIndexChangeListener listener);
	public void updateIndexChangeListener();
}
