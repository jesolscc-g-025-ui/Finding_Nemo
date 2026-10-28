package project1.MacManufacturerLookup;

import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.sql.ClientInfoStatus;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestManufacturerApi {

    private  HttpServer server ;
    private  MacManufacturerApi api;
    private  int statusCode = 200;
    private  String JSONResponse = "[{\"company\":\"Test Manufacturer Ltd\"}]";

    @BeforeEach

    void SetUp()throws IOException {
        //MOCK SERVER//
       //1st Create a HttpServer For the Api to connect to on an available

        server = HttpServer.create( new InetSocketAddress("localhost",0),0);

        //2nd Configure the API endpoint

        server.createContext("/api/v2/",Exchange -> {

            /*** Simulate Json Response
             ***/



            //convert JSON String into bytes
            byte[] responseBytes =
                    JSONResponse.getBytes(StandardCharsets.UTF_8);

            //set the response content type Tells the client what type of data it is receiving
            Exchange.getResponseHeaders().set("Content-Type","application/json" );

            //send the HTTP status code Success and specify it to the length of the responseBytes
            Exchange.sendResponseHeaders(statusCode,responseBytes.length);

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
        String result = api.lookupManufacturer("00:11:22:33:AA:BB");

        assertEquals("Test Manufacturer Ltd",result);
    }

    @Test

    void TestLookUpResultReturnsUnknownWhenResultIsEmpty()throws Exception{
        statusCode = 204;
        String result = api.lookupManufacturer("00:11:22:33:AA:BB");
        assertEquals("Unknown manufacturer",result);

    }

    @Test
    void TestServerErrorReturnsExpectedExceptionMessage()throws IOException{
        statusCode = 500;
        assertThrows(IOException.class,
                ()-> api.lookupManufacturer("00:11:22:33:AA:BB"));
    }

    @Test
    void TestInvalidJSONThrowsExceptionCatch()throws IOException,InterruptedException {

        JSONResponse = "[{\"company\"}]";

//
        assertThrows(IOException.class,
                () -> api.lookupManufacturer("00:11:22:33:AA:BB"));
    }

}
