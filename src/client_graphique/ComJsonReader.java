package client_graphique;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

// javax.json-1.0.4.jar

public class ComJsonReader {
	
	public static void main(String[] args) {
		
		// Structure globale
		JsonObjectBuilder comBuilder = Json.createObjectBuilder();
		// Exemple array
		JsonArrayBuilder messagesBuilder = Json.createArrayBuilder();
		messagesBuilder.add("bonjour");
		messagesBuilder.add("test");
		
		comBuilder.add("protocole", "get").add("messages", messagesBuilder);

		JsonObject jsonMessage = comBuilder.build();
		
		System.out.println("JSON : " + jsonMessage);
		
		// Parcours
		System.out.println("Protocole : " + jsonMessage.getString("protocole"));
	
		JsonArray listeMessages = jsonMessage.getJsonArray("messages");
		for (JsonValue message : listeMessages) {
			System.out.println("Un message : " + message.toString());
		}
		
	}

}
