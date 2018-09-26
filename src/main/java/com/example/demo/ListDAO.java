package com.example.demo;

import java.util.List;

public interface ListDAO {
	public abstract int createList(ListClass list);
	
	public abstract List<ListClass> getList();
}
