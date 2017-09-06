package android.trikarya.growth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import Master.GetAllDataCallback;
import Master.ServerRequest;
/**
 *   PT Trikarya Teknologi
 */
public class ForgotPassword extends AppCompatActivity {
    Button ok;
    LinearLayout form;
    TextView status;
    EditText email,nik,no_telp;

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(android.trikarya.growth.R.layout.activity_forgot_password);
        form = (LinearLayout) findViewById(android.trikarya.growth.R.id.form_forgot);
        email = (EditText) findViewById(android.trikarya.growth.R.id.email);
        nik = (EditText) findViewById(android.trikarya.growth.R.id.nik);
        no_telp = (EditText) findViewById(android.trikarya.growth.R.id.phone);
        status = (TextView) findViewById(android.trikarya.growth.R.id.status);
        ok = (Button) findViewById(android.trikarya.growth.R.id.forgot_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(form.getVisibility() == View.GONE)
                    startActivity(new Intent(ForgotPassword.this,Login.class));
                else{
                    ServerRequest serverRequest = new ServerRequest(ForgotPassword.this);
                    serverRequest.resetPassword(ForgotPassword.this, email.getText().toString(), nik.getText().toString(), no_telp.getText().toString(), new GetAllDataCallback() {
                        @Override
                        public void Done(String message) {
                            if(message.equals("success")){
                                form.setVisibility(View.GONE);
                                status.setText("Password berhasil diubah, silahkan cek email anda.");
                                status.setVisibility(View.VISIBLE);
                            }
                            else {
                                if(message.equals("not found"))
                                    message = "Data yang anda masukkan salah, silahkan coba lagi";
                                Toast.makeText(ForgotPassword.this, message,
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }

}
