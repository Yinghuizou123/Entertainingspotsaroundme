package com.yinghuizou.placesnearme;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by yinghuizou on 8/17/17.
 */

//Class used to  retrieve data from URL using HttpURLConnection and File handling methods.
//Take the data from the website and make into json format, and we get using httpUrlConnection
public class DownloadUrl {




    public String readUrl(String strUrl) throws IOException {

        //Data download from the web will be in json format
        String data = "";
        //Used to read URl
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;

        try {
            URL url = new URL(strUrl);
            // Creating http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();
            //connect the url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();


            //Check if the data is not equal to null in BufferedReader,
            // we put the data in StringBuffer
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            br.close();



        }catch (Exception e) {
            e.printStackTrace();

        }
        finally {
            iStream.close();
            urlConnection.disconnect();
        }



        return data;
    }


}
