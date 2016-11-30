package com.example.bianca.caloriecounter.mapping;

import android.util.JsonReader;

import org.json.JSONException;

import java.io.IOException;

import static com.example.bianca.caloriecounter.mapping.Api.Auth.TOKEN;

/**
 * Created by bianca on 30.11.2016.
 */
public class TokenReader implements ResourceReader<String, JsonReader> {

    @Override
    public String read(JsonReader jsonReader) throws IOException, JSONException, Exception {
        jsonReader.beginObject();
        String token = null;
        while (jsonReader.hasNext()){
            String name = jsonReader.nextName();
            if (name.equals(TOKEN)){
                token = jsonReader.nextString();
            }
        }
        jsonReader.endObject();
        return token;
    }
}
