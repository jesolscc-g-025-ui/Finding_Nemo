package project1.MacManufacturerLookup;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class MacManufacturerApi implements ApiManufacturerLookUp {

    private static final String API_URL =  "https://api.macaddresslookup.app/api/v2/"; // BaseAddress of the API

    //Create A HttpClient to connect to the remote server of the API

    private final HttpClient CLIENT;
   //Attributes to allow local Server Initialisation to Simulate Server-Client Connection/Responses&Requests Of API
    private final String apiUrl;


    public MacManufacturerApi(String apiUrl){
        this.apiUrl = apiUrl.endsWith("/") ?apiUrl : apiUrl + "/";
        this.CLIENT = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build();
    }

    public MacManufacturerApi(){
        this(API_URL);
    }



    public String lookupManufacturer(String macAddress)
            throws IOException, InterruptedException {

        //Error handling for Null macAddress or doesnt Match the Hexa-decimal structure of A MacAddress
        if (macAddress == null ||
                !macAddress.matches(
                        "(?i)[0-9a-f]{2}([:-][0-9a-f]{2}){5}")) {
            throw new IllegalArgumentException(
                    "Invalid MAC address: " + macAddress);
        }
        //Adjust the MACAddress format to match the HTTP request required format.
        String urlMac = macAddress.replace(':', '-');

        //Build the Http Get Request to send to the API using the urlMAC
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl + urlMac)).timeout(Duration.ofSeconds(10))
                .GET().build();

        //Send the Request and Expect a response Back that's Stored as a String to Use later
        HttpResponse<String> response = CLIENT.send(
                request, HttpResponse.BodyHandlers.ofString());

        //HTTP Status Codes Check
        //- 204 means that the server returned no content,which in our case would mean an Unknown Manufacturer
        if (response.statusCode() == 204) {
            return "Unknown manufacturer";
        }
        //Error handling for any responses that are not Specified.
        if (response.statusCode() != 200) {
            throw new IOException(
                    "MAC API returned HTTP "
                            + response.statusCode());
        }

        try {
            //Parse the HTTP JSON Response text into an Array Object that Gson can understand.
            JsonArray results =
                    JsonParser.parseString(response.body())
                            .getAsJsonArray();

            if (results.isEmpty()) {
                return "Unknown manufacturer";
            }
            //Take the first Object in the Array(The Company) ,if the array is not an Object results in unknown manufacturer
            JsonObject result = results.get(0).getAsJsonObject();

            if (!result.has("company") ||
                    result.get("company").isJsonNull()) {
                return "Unknown manufacturer";
            }

            return result.get("company").getAsString();

        } catch (RuntimeException e) {
            throw new IOException(
                    "Could not parse MAC API response", e);
        }
    }
}
