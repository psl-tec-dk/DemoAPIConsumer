package tec.psl.demoapiconsumer;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import tec.psl.demoapiconsumer.DataService.DataListener;
import tec.psl.demoapiconsumer.DataService.Dataservice;
import tec.psl.demoapiconsumer.Models.PersonModel;

public class MainActivity extends AppCompatActivity {

    private TextView txtData;

    private Button btnAddPerson;
    private Dataservice dataservice;
    private ListView lstPersons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtData = findViewById(R.id.txtData);

        btnAddPerson = findViewById(R.id.btnAddPerson);

        lstPersons = findViewById(R.id.lstPersons);

        Gson gson = new Gson();
        ArrayList<PersonModel> personModels = new ArrayList<>();
        ArrayList<String> personListStr = new ArrayList<>();

        ActivityResultLauncher launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Constants.RESULT_EDIT || result.getResultCode() == Constants.RESULT_ADD) {
                            recreate();
                        }
                    }
                });

        btnAddPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                launcher.launch(intent);
            }
        });

        lstPersons.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                intent.putExtra("personData", gson.toJson(personModels.get(position)));
                launcher.launch(intent);
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                personListStr
        );

        lstPersons.setAdapter(adapter);

        dataservice = new Dataservice(this);




        dataservice.getAllPersons(new DataListener() {
            @Override
            public void onDataReady(JSONArray jsonArray) {
                for(int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject json = jsonArray.getJSONObject(i);
                        PersonModel p = new PersonModel();
                        p = gson.fromJson(json.toString(), PersonModel.class);
                        personModels.add(p);
                        String stud = json.getBoolean("student") ? "ja" : "nej";
                        personListStr.add(json.getString("firstName") + " " + json.getString("lastName") + " | stud " + stud);
                        //txtData.append(json.getString("firstName") + " " + json.getString("lastName") + "\n");
                    }
                    catch (JSONException e) {
                        Log.d("PSL_LOG", e.getMessage());
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onDataReady(JSONObject jsonObject) { }

            @Override
            public void onFailure(String err) {
                txtData.setText(err);
            }
        });

    }
}