package com.deloitte.cc.job;

import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args)  {
		while (true) {
			
			try {
			HttpResponse<String> response = Unirest
					.post("https://deloitte02-alliance-prtnr-na05-dw.demandware.net/dw/oauth2/access_token?client_id=2b51dbad-13cb-461e-8e6e-05c058edd3ba&grant_type=urn:demandware:params:oauth:grant-type:client-id:dwsid:dwsecuretoken")
					.header("authorization",
							"Basic Z2F1cmF2YXJvcmE4QGRlbG9pdHRlLmNvbTpEZWwwMkAxNjM6Q2hhbmdlTWVEZWxvaXR0ZQ==")
					.header("content-type", "application/x-www-form-urlencoded").asString();

			JSONObject jsonObj = new JSONObject(response.getBody());
			
			if(jsonObj == null || jsonObj.isNull("access_token")) {
				continue;
			}

			String authHeadr = "Bearer " + jsonObj.getString("access_token");

			response = Unirest
					.post("https://deloitte02-alliance-prtnr-na05-dw.demandware.net/s/-/dw/data/v17_6/jobs/TW-Import-Inventory/executions")
					.header("authorization", authHeadr).asString();
			
			System.out.println("Inventory Sync Job: Status " + response.getStatusText());
			
			response = Unirest
					.post("https://deloitte02-alliance-prtnr-na05-dw.demandware.net/s/-/dw/data/v17_6/jobs/Store-Generate-Notifications/executions")
					.header("authorization", authHeadr).asString();
			
			System.out.println("Notifications Job: Status " + response.getStatusText());
			
			Thread.sleep(3000);
			}
			catch(Exception e) {
				System.out.println("Caught");
			}
		} 

	}
}
