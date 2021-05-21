package com.gameengine.game.mapobjects;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapAdapter extends TypeAdapter<Map> {
    @Override
    public void write(JsonWriter jsonWriter, Map map) throws IOException {

    }

    @Override
    public Map read(JsonReader jsonReader) throws IOException {
        Map map = new Map();
        jsonReader.beginObject();
        String fieldname = null;

        while(jsonReader.hasNext()){
            JsonToken token = jsonReader.peek();

            if(token.equals(JsonToken.NAME)){
                fieldname = jsonReader.nextName();
            }

            if ("data".equals(fieldname)) {
                token = jsonReader.peek();
                jsonReader.beginArray();
                List<Integer> tiles = new ArrayList<>();
                if(token.equals(JsonToken.BEGIN_ARRAY)){
                    token = jsonReader.peek();
                    while (token.equals(JsonToken.NUMBER)) {
                        int i = jsonReader.nextInt();
                        tiles.add(i);
                        token = jsonReader.peek();
                    }
                }
                jsonReader.endArray();
                map.setData(tiles);
            }


            if ("height".equals(fieldname)){
                token = jsonReader.peek();
                map.setHeight(jsonReader.nextInt());
            }

            if ("id".equals(fieldname)){
                token = jsonReader.peek();
                map.setId(jsonReader.nextInt());
            }

            if ("name".equals(fieldname)){
                token = jsonReader.peek();
                map.setName(jsonReader.nextString());
            }

            if ("opacity".equals(fieldname)){
                token = jsonReader.peek();
                map.setOpacity(jsonReader.nextInt());
            }

            if ("type".equals(fieldname)){
                token = jsonReader.peek();
                map.setType(jsonReader.nextString());
            }

            if ("visible".equals(fieldname)){
                token = jsonReader.peek();
                map.setVisible(jsonReader.nextBoolean());
            }

            if ("width".equals(fieldname)){
                token = jsonReader.peek();
                map.setWidth(jsonReader.nextInt());
            }

            if ("x".equals(fieldname)){
                token = jsonReader.peek();
                map.setX(jsonReader.nextInt());
            }

            if ("y".equals(fieldname)){
                token = jsonReader.peek();
                map.setY(jsonReader.nextInt());
            }

        }
        jsonReader.endObject();
        return map;
    }

    private List<Integer> formatStringToInt(String data){
        String[] items = data.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s", "").split(",");

        List<Integer> results = new ArrayList<>();

        for (int i = 0; i < items.length; i++) {
            try {
                results.add(Integer.parseInt(items[i]));
            } catch (NumberFormatException nfe) {
                System.out.println("Failed parsing data.");
            };
        }
        return results;
    }
}
