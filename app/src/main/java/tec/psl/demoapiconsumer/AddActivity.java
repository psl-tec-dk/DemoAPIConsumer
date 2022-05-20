package tec.psl.demoapiconsumer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tec.psl.demoapiconsumer.DataService.DataListener;
import tec.psl.demoapiconsumer.DataService.Dataservice;

public class AddActivity extends AppCompatActivity {

    private EditText etFirstName, etLastName;
    private CheckBox chkStudent;
    private Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        chkStudent = findViewById(R.id.chkStudent);
        btnSend = findViewById(R.id.btnSend);
        Dataservice dataservice = new Dataservice(this);

        btnSend.setOnClickListener(new View.OnClickListener() {
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
                            setResult(Constants.RESULT_ADD);
                            finish();
                        }

                        @Override
                        public void onFailure(String err) {
                            Log.d("PSL_LOG", err);
                        }
                    });
                }
                catch (JSONException e) {
                    Log.d("PSL_LOG", e.getMessage());
                }
            }
        });

    }
}