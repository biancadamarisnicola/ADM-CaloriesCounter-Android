package com.example.bianca.caloriecounter.mapping;

import android.util.JsonWriter;
import android.util.Log;

import com.example.bianca.caloriecounter.content.User;

import java.io.IOException;

import static com.example.bianca.caloriecounter.mapping.Api.Auth.PASSWORD;
import static com.example.bianca.caloriecounter.mapping.Api.Auth.USERNAME;

/**
 * Created by bianca on 30.11.2016.
 */
public class CredentialWriter implements ResourceWriter<User, JsonWriter> {
    private static final String TAG = CredentialWriter.class.getSimpleName();
    @Override
    public void write(User user, JsonWriter writer) throws IOException {
        writer.beginObject();
        {
            writer.name(USERNAME).value(user.getUsername());
            writer.name(PASSWORD).value(user.getPassword());
        }
        writer.endObject();
        Log.d(TAG, "User: "+user.toString());
    }
}
