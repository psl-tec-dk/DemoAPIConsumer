package tec.psl.demoapiconsumer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tec.psl.demoapiconsumer.DataService.DataListener;
import tec.psl.demoapiconsumer.DataService.Dataservice;

public class MainActivity extends AppCompatActivity {

    private TextView txtData;
    private EditText etFirstName, etLastName, etPersId;
    private CheckBox chkStudent;
    private Button btnAddPerson, btnUpdatePerson, btnDeletePerson;
    private Dataservice dataservice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtData = findViewById(R.id.txtData);
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etPersId = findViewById(R.id.etPersId);
        chkStudent = findViewById(R.id.chkStudent);
        btnAddPerson = findViewById(R.id.btnAddPerson);
        btnUpdatePerson = findViewById(R.id.btnUpdatePerson);
        btnDeletePerson = findViewById(R.id.btnDeletePerson);

        dataservice = new Dataservice(this);

        btnDeletePerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int persId = Integer.parseInt(etPersId.getText().toString());
                dataservice.deletePerson(persId, new DataListener() {
                    @Override
                    public void onDataReady(JSONArray jsonArray) { }

                    @Override
                    public void onDataReady(JSONObject jsonObject) {
                        recreate();
                    }

                    @Override
                    public void onFailure(String err) {
                        txtData.setText(err);
                    }
                });
            }
        });

        btnUpdatePerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject json = new JSONObject();
                try {
                    json.put("persId", Integer.parseInt(etPersId.getText().toString()));
                    json.put("firstName", etFirstName.getText().toString());
                    json.put("lastName", etLastName.getText().toString());
                    json.put("student", chkStudent.isChecked());
                    dataservice.updatePerson(json, new DataListener() {
                        @Override
                        public void onDataReady(JSONArray jsonArray) { }

                        @Override
                        public void onDataReady(JSONObject jsonObject) {
                            recreate();
                        }

                        @Override
                        public void onFailure(String err) {
                            txtData.setText(err);
                        }
                    });
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        btnAddPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject json = new JSONObject();
                try {
                    json.put("firstName", etFirstName.getText().toString());
                    json.put("lastName", etLastName.getText().toString());
                    json.put("student", chkStudent.isChecked());
                    dataservice.registerPerson(json, new DataListener() {
                        @Override
                        public void onDataReady(JSONArray jsonArray) { }

                        @Override
                        public void onDataReady(JSONObject jsonObject) {
                            etFirstName.getText().clear();
                            etLastName.getText().clear();
                            chkStudent.setChecked(false);
                            recreate();
                        }

                        @Override
                        public void onFailure(String err) {
                            txtData.setText(err);
                        }
                    });
                }
                catch (JSONException e) {
                    Log.d("PSL_LOG", e.getMessage());
                }
            }
        });

        dataservice.getAllPersons(new DataListener() {
            @Override
            public void onDataReady(JSONArray jsonArray) {
                for(int i = 0; i < jsonArray.length(); i++){
                    try {
                        JSONObject json = jsonArray.getJSONObject(i);
                        txtData.append(json.getString("firstName") + " " +json.getString("lastName") + "\n");
                    }
                    catch (JSONException e) {
                        Log.d("PSL_LOG", e.getMessage());
                    }
                }
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