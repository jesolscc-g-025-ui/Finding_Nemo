package project1.MacManufacturerLookup;

import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;
import java.sql.ClientInfoStatus;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestManufacturerApi {

    private  HttpServer server ;
    private  MacManufacturerApi api;

    @BeforeEach

    void SetUp()throws IOException {
       //1st Create a HttpServer For the Api to connect to on an available

        server = HttpServer.create( new InetSocketAddress("localhost",0),0);

        //2nd Configure the API endpoint

        server.createContext("/api/v2/",Exchange -> {

            /*** Simulate Json Response
             ***/

            //create a mock Json Response with a successful response
            String JSONResponse = "[{\"company\":\" Test Manufacturer Ltd\"}]";

            //convert JSON String into bytes
            byte[] responseBytes =
                    JSONResponse.getBytes(StandardCharsets.UTF_8);

            //set the response content type Tells the client what type of data it is recieving
            Exchange.getRequestHeaders().set("Content-Type","application/json" );

            //send the HTTP status code and specify it to the length of the resonseBytes
            Exchange.sendResponseHeaders(200,responseBytes.length);

            //Write the Json Response into a ResponseBody
            Exchange.getResponseBody().write(responseBytes);

            Exchange.close();
        });
        server.start();

        int port = server.getAddress().getPort();

        api = new MacManufacturerApi("http://localhost:" + port +"/api/v2");
    }

    @AfterEach

    void TearDown(){
        if (server != null){
            server.stop(0);
        }
    }

    @Test

    void TestSuccessfulManufacturerFoundWhenRequestSucceeds()throws Exception{
        String result = api.lookupManufacturer("00:11:22:33:AA:BB:CC");

        assertEquals("Test Manufacturer Ltd",result);
    }
}
