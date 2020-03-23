package it.cnr.ibimet.restutil;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;




public class HttpURLManager {
    private final String USER_AGENT="Mozilla/5.0";

    private String url;


    public HttpURLManager() {
        super();

    }

    public HttpURLManager(String url){
        this();
        this.url=url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String sendGet() throws Exception{
        URL obj = new URL(url);
        HttpURLConnection con =(HttpURLConnection)obj.openConnection();

       // System.setProperty("http.agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
        con.setRequestMethod("GET");
        con.setDoOutput(true);
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept-Charset", "UTF-8");
        //con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");

        int responseCode=con.getResponseCode();
        System.out.println("Call service: "+url);
        System.out.println("RetCode: "+responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

        String inputLine;
        StringBuffer response = new StringBuffer();

        while((inputLine=in.readLine())!=null){
            response.append(inputLine);
        }


        in.close();

        System.out.println(response.toString());

        return(response.toString());
    }

}
