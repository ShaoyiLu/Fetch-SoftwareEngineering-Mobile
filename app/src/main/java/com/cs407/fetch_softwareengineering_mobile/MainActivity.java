package com.cs407.fetch_softwareengineering_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetchData();
    }

    private void fetchData() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("https://fetch-hiring.s3.amazonaws.com/hiring.json").build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                // Handle the error
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String jsonData = response.body().string();
                    Gson gson = new Gson();
                    Type listType = new TypeToken<ArrayList<Item>>() {}.getType();
                    List<Item> items = gson.fromJson(jsonData, listType);
                    List<Item> processedItems = processItems(items);
                    runOnUiThread(() ->
                        recyclerView.setAdapter(new ItemAdapter(processedItems, MainActivity.this)));
                }
            }
        });
    }

    private List<Item> processItems(List<Item> items) {
        List<Item> filteredItems = new ArrayList<>();
        for (Item item : items) {
            if (item.getName() != null && !item.getName().isEmpty()) {
                filteredItems.add(item);
            }
        }

        filteredItems.sort(Comparator.comparingInt(Item::getListId)
                .thenComparing(item -> {
                    String name = item.getName().replaceAll("\\D", "");
                    return Integer.parseInt(name);
                }));

        return filteredItems;
    }

}
