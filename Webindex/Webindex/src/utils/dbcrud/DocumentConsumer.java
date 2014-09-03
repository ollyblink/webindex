package utils.dbcrud;

import java.util.concurrent.BlockingQueue;

public final class DocumentConsumer implements Runnable {

	boolean isUpdated;
	private BlockingQueue<String> documentQueue;
	private DBDataManager dbDataProvider;
	private boolean isRunning;

	public DocumentConsumer(DBDataManager dbDataProvider, BlockingQueue<String> documentQueue) {
		this.dbDataProvider = dbDataProvider;
		this.documentQueue = documentQueue;
		this.isUpdated = true;
		this.isRunning = true;

	}

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	@Override
	public void run() {
		while (isRunning) {
			while (!documentQueue.isEmpty()) {
				try {
					this.isUpdated = false;
					String documentToIndex = documentQueue.take();
					// if (documentQueue.size() % dbDataProvider.NUMBER_OF_INDEXED_DOCS == 0) {
					System.out.println("indexing document. " + documentQueue.size() + " documents left to index.");
					// }
					dbDataProvider.addDocument(documentToIndex);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if (!isUpdated) {
				updateIndex();
			}
		}
	}

	private void updateIndex() {
		dbDataProvider.updateTermNi();
		dbDataProvider.updateMetadataA();
		dbDataProvider.updateTermIdfs();
		dbDataProvider.updateTermOccursInDocumentTFIDFs();
		dbDataProvider.updateDocumentVectorNorms();
		dbDataProvider.updateMetadataB();
		isUpdated = true;
	}

	public boolean isUpdated() {
		return isUpdated;
	}

	public void setIsUpdated(boolean b) {
		this.isUpdated = b;
	}

}