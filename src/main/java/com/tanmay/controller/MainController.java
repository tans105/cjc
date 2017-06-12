package com.tanmay.controller;

import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import au.com.bytecode.opencsv.CSVReader;

import com.tanmay.constants.Constants;

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
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/convert")
	public Response convert() {
		String csvFile = Constants.CSV_LOCATION;
		CSVReader reader = null;
		List<Map<String, String>> list = new LinkedList<Map<String, String>>();
		try {
			reader = new CSVReader(new FileReader(csvFile));
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
				list.add(map);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Response.ok(createJson(list), MediaType.APPLICATION_JSON).build();
	}

	private String createJson(List<Map<String, String>> list) {
		JSONArray jsonArray = new JSONArray();
		for (Map<String, String> map : list) {
			JSONObject obj = new JSONObject();
			for (Map.Entry<String, String> entry : map.entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				try {
					obj.put(key, value);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			jsonArray.put(obj);
		}
		return jsonArray.toString();

	}
}