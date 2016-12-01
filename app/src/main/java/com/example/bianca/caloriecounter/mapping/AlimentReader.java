package com.example.bianca.caloriecounter.mapping;

import android.util.JsonReader;
import android.util.Log;

import com.example.bianca.caloriecounter.content.Aliment;

import java.io.IOException;

import static com.example.bianca.caloriecounter.mapping.Api.Note.CALORIES;
import static com.example.bianca.caloriecounter.mapping.Api.Note.CARBS;
import static com.example.bianca.caloriecounter.mapping.Api.Note.FATS;
import static com.example.bianca.caloriecounter.mapping.Api.Note.NAME;
import static com.example.bianca.caloriecounter.mapping.Api.Note.PROTEINS;

/**
 * Created by bianca on 01.12.2016.
 */
public class AlimentReader implements ResourceReader<Aliment, JsonReader>{
    private static final String TAG = AlimentReader.class.getSimpleName();

    @Override
    public Aliment read(JsonReader reader) throws IOException {
        Aliment aliment = new Aliment();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals(NAME)) {
                aliment.setName(reader.nextString());
            } else if (name.equals(CALORIES)) {
                aliment.setCalories(reader.nextDouble());
            } else if (name.equals(CARBS)) {
                aliment.setCarbs(reader.nextDouble());
            } else if (name.equals(PROTEINS)) {
                aliment.setProteins(reader.nextDouble());
            } else if (name.equals(FATS)) {
                aliment.setFats(reader.nextDouble());
            } else {
                reader.skipValue();
                Log.w(TAG, String.format("Aliment property '%s' ignored", name));
            }
        }
        reader.endObject();
        return aliment;
    }
}
