package tec.psl.demoapiconsumer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tec.psl.demoapiconsumer.DataService.DataListener;
import tec.psl.demoapiconsumer.DataService.Dataservice;
import tec.psl.demoapiconsumer.Models.PersonModel;

public class EditActivity extends AppCompatActivity {

    private TextView txtPersonData, txtPersId;
    private EditText etFirstName, etLastName;
    private CheckBox chkStudent;
    private Button btnUpdatePerson, btnDeletePerson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        txtPersId = findViewById(R.id.txtPersId);
        chkStudent = findViewById(R.id.chkStudent);
        btnUpdatePerson = findViewById(R.id.btnUpdatePerson);
        btnDeletePerson = findViewById(R.id.btnDeletePerson);

        txtPersonData = findViewById(R.id.txtPersonData);
        Gson gson = new Gson();
        Dataservice dataservice = new Dataservice(this);

        Intent intent = getIntent();
        PersonModel personModel = gson.fromJson(intent.getStringExtra("personData"), PersonModel.class);

        txtPersId.setText("" + personModel.getPersId());
        etFirstName.setText(personModel.getFirstName());
        etLastName.setText(personModel.getLastName());
        chkStudent.setChecked(personModel.isStudent());


        txtPersonData.setText(personModel.toString());


        btnDeletePerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int persId = Integer.parseInt(txtPersId.getText().toString());
                dataservice.deletePerson(persId, new DataListener() {
                    @Override
                    public void onDataReady(JSONArray jsonArray) { }

                    @Override
                    public void onDataReady(JSONObject jsonObject) {
                        setResult(Constants.RESULT_EDIT);
                        finish();
//                        Log.d("PSL_LOG", jsonObject.toString());
//                        recreate();
                    }

                    @Override
                    public void onFailure(String err) {
                        txtPersonData.setText(err);
                    }
                });
            }
        });

        btnUpdatePerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject json = new JSONObject();
                try {
                    json.put("persId", Integer.parseInt(txtPersId.getText().toString()));
                    json.put("firstName", etFirstName.getText().toString());
                    json.put("lastName", etLastName.getText().toString());
                    json.put("student", chkStudent.isChecked());
                    dataservice.updatePerson(json, new DataListener() {
                        @Override
                        public void onDataReady(JSONArray jsonArray) { }

                        @Override
                        public void onDataReady(JSONObject jsonObject) {
                            setResult(Constants.RESULT_EDIT);
                            finish();
//                            recreate();
                        }

                        @Override
                        public void onFailure(String err) {
                            txtPersonData.setText(err);
                        }
                    });
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });





    }
}