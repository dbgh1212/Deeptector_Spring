package com.example.demo;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;



@RestController
@CrossOrigin
public class ListController {
	
	public int sema = 0;
	public Vector<String> v = new Vector<String>();
	
	List<ListClass> vlist = new ArrayList<ListClass>();
	TcpServer tcpServer;
	CppServer cppserver;
	File file;
	boolean isFirst = true;
	public ListController(){
		if(isFirst == true) {
		tcpServer = new TcpServer();
		System.out.println("new TcpServer!!!");
		cppserver = new CppServer(tcpServer);
		System.out.println("new cppServer!!!");
		cppserver.pushThreadStart();
		
		isFirst = false;
		}
	}
	
	@Autowired
	DAO dao;
	
	
	/*@RequestMapping(value = "/")
	@CrossOrigin
	public ResponseEntity get(){
		
		ListClass list = new ListClass("20180524");
		
		return new ResponseEntity(list, HttpStatus.OK);
	}*/
	
	
	/*@RequestMapping(value="/postList", method = RequestMethod.POST)
	public ResponseEntity<ListClass> postList(@RequestBody ListClass list){
		System.out.println("/postList OK1");
		dao.createList(list);
		System.out.println("/postList OK2");
		return new ResponseEntity<ListClass>(list, HttpStatus.OK);
	}*/
	
	@RequestMapping(value = "/getList", method = RequestMethod.GET)
	public ResponseEntity<ArrayList<ListClass>> getList(){
		System.out.println("get react request OK");
		ArrayList<ListClass> a = new ArrayList<ListClass>();
		a = (ArrayList<ListClass>) dao.getList();
		Gson gson = new Gson();
		gson.toJson(a);
		return new ResponseEntity<ArrayList<ListClass>>(a, HttpStatus.OK);
	}
	
	
	/*@RequestMapping(value = "/list", method = RequestMethod.POST)
	@CrossOrigin
	public ResponseEntity<Object> update(@RequestBody ArrayList<ListClass> vlist) {
		Gson gson = new Gson();
		gson.toJson(vlist);
		String a = "OK";
		System.out.println("post ok");
		return new ResponseEntity<Object>(a, HttpStatus.OK);
	}*/
	
	@RequestMapping(value = "/and", method = RequestMethod.POST)
	@CrossOrigin
	public ResponseEntity<String> goAndroid(@RequestBody HashMap<String, String> input) {
		System.out.println("react post and ok");
		
		//wait
		System.out.println("before : " + v.size());
		
		v.add(input.get("videoName"));
		
		System.out.println("after : " + v.size());
		
		while(v.size()!=0 && sema == 0)
		{
			sema = 1;
			file = new File("C:\\Users\\hojin\\Desktop\\file\\" + v.get(0).trim());
			tcpServer.file = file;
		
			tcpServer.filelength = ((long)file.length());
			System.out.println(tcpServer.filelength+"");
		
			tcpServer.fileSend();
		
			sema = 0;
			v.remove(0);
		}
		
		
		return new ResponseEntity<String>("ok", HttpStatus.OK);
	}
	
}
