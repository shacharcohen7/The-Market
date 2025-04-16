package DomainLayer;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class HttpRequestController {
    private URL url;
    private HttpURLConnection connection;
    private static final int MINIMUM_WAIT_TIME_SECONDS = 60; // Minimum wait time in seconds
    private static final int MAXIMUM_WAIT_TIME_SECONDS = 120;
    private static final int CONNECTION_TIMEOUT = 10000;//

    public HttpRequestController(String urlAddress) throws Exception
    {
        url = new URL(urlAddress);
        configureConnection();
    }

    private void configureConnection() throws Exception
    {
        connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("POST");
        connection.setConnectTimeout(CONNECTION_TIMEOUT);
        connection.setReadTimeout(CONNECTION_TIMEOUT);


//        connection.setConnectTimeout(TIMEOUT_SECONDS * 1000); // Convert seconds to milliseconds
//        connection.setReadTimeout(TIMEOUT_SECONDS * 1000); // Convert seconds to milliseconds
    }

    private byte[] configureRequest(Map<String, String> params) throws Exception
    {
        // Instantiate a requestData object to store our data
        StringBuilder requestData = new StringBuilder();

        for (Map.Entry<String, String> param : params.entrySet()) {
            if (requestData.length() != 0) {
                requestData.append('&');
            }
            // Encode the parameter based on the parameter map we've defined
            // and append the values from the map to form a single parameter
            requestData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            requestData.append('=');
            requestData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }

        // Convert the requestData into bytes
        byte[] requestDataBytes = requestData.toString().getBytes("UTF-8");
        return requestDataBytes;
    }

    public String sendRequest(Map<String, String> requestParams)
    {
        try
        {
           // Thread.sleep(MINIMUM_WAIT_TIME);
            byte[] requestDataBytes = configureRequest(requestParams);
            connection.setDoOutput(true);
            try(DataOutputStream writer = new DataOutputStream(connection.getOutputStream()))
            {
                //Send Request
                writer.write(requestDataBytes);
               // Read Response
                StringBuilder content;
                try(BufferedReader in =  new BufferedReader(new InputStreamReader(connection.getInputStream())))
                {
                    String line;
                    content = new StringBuilder();
                    while((line = in.readLine()) != null)
                    {
                        content.append(line);
                        content.append(System.lineSeparator());
                    }
                }
                String beforeRemove = content.toString();
                if(beforeRemove.contains("error"))
                    return null;
//                //Remove \n and \r from string
                String afterRemove = beforeRemove.replaceAll("\n", "");
                afterRemove = afterRemove.replaceAll("\r", "");
                return afterRemove;
            }
            finally
            {
                connection.disconnect();
            }
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted status
            System.out.println("Minimum wait time interrupted: " + e.getMessage());
            return null; // Handle interruption scenario
        }
        catch (Exception e)
        {
            return null;
        }

    }

//    public String sendRequest1(Map<String, String> requestParams) {
//        try {
//            byte[] requestDataBytes = configureRequest(requestParams);
//            connection.setDoOutput(true);
//
//            // Introduce a CountDownLatch to control minimum wait time
//            CountDownLatch latch = new CountDownLatch(1);
//
//            // Start a thread to countdown the latch after minimum wait time
//            Thread waitForMinimumTimeThread = new Thread(() -> {
//                try {
//                    Thread.sleep(MINIMUM_WAIT_TIME_SECONDS * 1000); // Convert seconds to milliseconds
//                    latch.countDown();
//                } catch (InterruptedException e) {
//                    Thread.currentThread().interrupt(); // Restore interrupted status
//                    System.out.println("Thread interrupted while waiting: " + e.getMessage());
//                }
//            });
//            waitForMinimumTimeThread.start();
//
//            try (DataOutputStream writer = new DataOutputStream(connection.getOutputStream())) {
//                // Send Request
//                writer.write(requestDataBytes);
//
//                // Wait for minimum wait time or until response is received
//
//                // Read Response
//                StringBuilder content;
//                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
//                    String line;
//                    content = new StringBuilder();
//                    while ((line = in.readLine()) != null) {
//                        // latch.await(MAXIMUM_WAIT_TIME_SECONDS - MINIMUM_WAIT_TIME_SECONDS, TimeUnit.SECONDS);
//                        content.append(line);
//                        content.append(System.lineSeparator());
//                    }
//                }
//
//                String response = content.toString();
//                if (response.contains("error")) {
//                    return null;
//                }
//
//                // Remove \n and \r from string
//                response = response.replaceAll("[\\n\\r]", "");
//                return response;
//            } finally {
//                connection.disconnect();
//                waitForMinimumTimeThread.interrupt(); // Interrupt the wait thread if it's still running
//            }
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt(); // Restore interrupted status
//            System.out.println("Thread interrupted while waiting: " + e.getMessage());
//            return null; // Handle interruption scenario
//        } catch (Exception e) {
//            e.printStackTrace(); // Log or handle the exception appropriately
//            return null;
//        }
//    }

    public boolean checkHandShake()
    {
        Map<String,String> params = new HashMap<>();
        params.put("action_type", "handshake");
        //connection.setConnectTimeout(10000);
        String response = sendRequest(params);
        if(response == null)
            return false;
        return response.equals("OK");
    }
}
