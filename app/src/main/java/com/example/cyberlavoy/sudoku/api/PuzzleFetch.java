package com.example.cyberlavoy.sudoku.api;

import android.net.Uri;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by CyberLaVoy on 4/11/2018.
 */

public class PuzzleFetch {
    private static final String TAG = "PuzzleFetch";

    public byte[] getUrlBytes(String urlSpec, File imageFile) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();

        try {

            int bytesRead;
            byte[] buffer = new byte[1024];

           // request body code

            /*
            connection.setDoOutput(true);
            connection.setFixedLengthStreamingMode(imageFile.length());
            connection.setRequestProperty("Content-Type", "application/octet-stream");

            OutputStream requestOut = new BufferedOutputStream(connection.getOutputStream());
            InputStream requestIn = new FileInputStream(imageFile);
            while ((bytesRead = requestIn.read(buffer)) > 0) {
                requestOut.write(buffer, 0, bytesRead);
            }
            requestIn.close();
            */

           // After response
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() +
                        ": with " +
                        urlSpec);
            }
           // response body code
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    public String getUrlString(String urlSpec, File imageFile) throws IOException {
        return new String(getUrlBytes(urlSpec, imageFile));
    }

    public String fetchBoardLayout(File puzzleImage) {
        String puzzleLayout = "";
        try {
            String url = Uri.parse("http://hidden-lake-94556.herokuapp.com/puzzles")
                    .buildUpon()
                    .build().toString();
            String jsonString = getUrlString(url, puzzleImage);
            JSONObject jsonBody = new JSONObject(jsonString);
            puzzleLayout = jsonBody.getString("puzzle_layout");
            Log.i(TAG, puzzleLayout);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        } catch (JSONException je){
            Log.e(TAG, "Failed to parse JSON", je);
        }
        return puzzleLayout;
    }

}

