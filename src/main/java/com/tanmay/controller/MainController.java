package com.tanmay.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import au.com.bytecode.opencsv.CSVReader;

import com.google.gson.Gson;
import com.tanmay.constants.Constants;
import com.tanmay.entity.Bundle;

/**
 * 
 * @author tanmay
 *
 */
@Path("/api")
public class MainController {

	@GET
	@Path("/test")
	public Response test() {
		String output = "Request received";
		return Response.status(200).entity(output).build();
	}

	@SuppressWarnings("resource")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/convert")
	public Response convert(String requestJson) {
		Bundle bundle = new Gson().fromJson(requestJson, Bundle.class);
		String csvFilePath = fetchCSVPath(bundle.getId());
		CSVReader reader = null;
		List<Map<String, String>> list = new LinkedList<Map<String, String>>();
		try {
			reader = new CSVReader(new FileReader(csvFilePath));
			String[] header = reader.readNext();
			List<String> headers = new LinkedList<String>();
			for (int i = 0; i < header.length; i++) {
				headers.add(header[i]);
			}
			String[] line;

			while ((line = reader.readNext()) != null) {
				LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
				for (int i = 0; i < headers.size(); i++) {
					map.put(header[i], line[i]);
				}
				if (applyFilters(bundle.getFilter(), map, bundle.getCombination())) {
					if (null != bundle.getColumns()) {
						for (int j = 0; j < header.length; j++) {
							if(bundle.getColumns().contains(j)){
								continue;
							}
							else{
								map.remove(header[j]);
							}
						}
					}
					list.add(map);
				} else
					continue;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Response.ok(createJson(list), MediaType.APPLICATION_JSON).build();
	}

	private Boolean applyFilters(Map<String, ArrayList<String>> filter, LinkedHashMap<String, String> map, Boolean combination) {
		if (null == combination) {
			combination = true;
		}
		Boolean passed = Boolean.FALSE;
		if (!combination) {
			for (Map.Entry<String, ArrayList<String>> e : filter.entrySet()) {
				if (e.getValue().indexOf(map.get(e.getKey())) >= 0)
					passed = Boolean.TRUE;
				else {
					passed = Boolean.FALSE;
					break;
				}
			}
		} else {
			for (Map.Entry<String, ArrayList<String>> e : filter.entrySet()) {
				if (e.getValue().indexOf(map.get(e.getKey())) >= 0)
					passed = Boolean.TRUE;
			}
		}
		return passed;

	}

	private String fetchCSVPath(String id) {
		ClassLoader classLoader = new MainController().getClass().getClassLoader();
		File file = new File(classLoader.getResource(Constants.JSON_FILE_NAME).getFile());
		Object obj;
		JSONParser parser = new JSONParser();
		String path = null;
		try {
			obj = parser.parse(new FileReader(file));
			JSONObject jsonObject = (JSONObject) obj;
			path = jsonObject.get(id).toString();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return path;

	}

	@SuppressWarnings("unchecked")
	private String createJson(List<Map<String, String>> list) {
		JSONArray jsonArray = new JSONArray();
		for (Map<String, String> map : list) {
			JSONObject obj = new JSONObject();
			for (Map.Entry<String, String> entry : map.entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				obj.put(key, value);
			}
			jsonArray.add(obj);
		}
		return jsonArray.toString();

	}
}