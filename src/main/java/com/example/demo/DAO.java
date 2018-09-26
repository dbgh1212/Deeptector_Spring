package com.example.demo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.mysql.jdbc.Statement;


@Repository
public class DAO implements ListDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public int createList(ListClass list) {
		System.out.println("DAO1");
		KeyHolder keyHolder = new GeneratedKeyHolder();
		System.out.println("DAO2");
		jdbcTemplate.update((Connection connection) -> {
			System.out.println("DAO3");
			PreparedStatement preparedStatement;
			System.out.println("DAO4");
			preparedStatement = connection.prepareStatement("INSERT INTO list(date) VALUES(?)",Statement.RETURN_GENERATED_KEYS);
			System.out.println("DAO5");
			preparedStatement.setString(1, list.getDate());
			System.out.println("DAO6");
			return preparedStatement;
		}, keyHolder);
		
		return 1;
	}

	@Override
	public List<ListClass> getList() {
		
		List<ListClass> lists = new ArrayList<ListClass>();
		
		Collection<Map<String, Object>> rows = null;
		rows = jdbcTemplate.queryForList("SELECT * FROM list order by date desc");
		
		rows.stream().map((row) -> {
			ListClass list = new ListClass();
		list.setDate((String)row.get("date"));
		return list;
		}).forEach((ss) -> {
			lists.add(ss);
		});
		
		return lists;
	}

}
