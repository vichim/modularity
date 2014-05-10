package ro.codecamp.modularity.infrastructure.control;

import java.util.ArrayList;
import java.util.List;

public class SearchResults<T> {
	
	private int totalItems;

	private boolean hasPrevious;
	private boolean hasNext;

	private List<T> items = new ArrayList<T>();

	public SearchResults(int totalItems) {
		this.totalItems = totalItems;
	}

	public int getTotalItems() {
		return totalItems;
	}

	public void addItem(T item) {
		items.add(item);
	}

	public void addItems(List<T> items) {
		this.items.addAll(items);
	}

	public List<T> getItems() {
		return items;
	}

	public boolean isHasPrevious() {
		return hasPrevious;
	}

	public void setHasPrevious(boolean hasPrevious) {
		this.hasPrevious = hasPrevious;
	}

	public boolean isHasNext() {
		return hasNext;
	}

	public void setHasNext(boolean hasNext) {
		this.hasNext = hasNext;
	}
}
