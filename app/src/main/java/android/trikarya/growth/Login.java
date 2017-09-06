package android.trikarya.growth;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import Master.DatabaseHandler;
import Master.GetUserCallback;
import Master.ServerRequest;
import Master.User;
import fcm.StringHelper;
import fcm.TempIDFCM;
import network.ConnectionHandler;
import network.JsonCallback;
import network.UserNetwork;

/**
 *   PT Trikarya Teknologi
 */
public class Login extends AppCompatActivity {
    private EditText username,password;
    private Button signin;
    TextView forgot;
    String token;
    TempIDFCM tempIDFCM;
    UserNetwork userNetwork;
    User user;

    //OLD
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(android.trikarya.growth.R.layout.activity_login);
        username = (EditText) findViewById(android.trikarya.growth.R.id.username);
        password = (EditText) findViewById(android.trikarya.growth.R.id.password);
        signin = (Button) findViewById(android.trikarya.growth.R.id.signin);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User(username.getText().toString(),password.getText().toString());
                authenticateUser(user);
            }
        });
        forgot = (TextView) findViewById(android.trikarya.growth.R.id.forgot);
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,ForgotPassword.class));
            }
        });
    }
    private void authenticateUser(final User user) {
        ServerRequest serverRequest = new ServerRequest(this);
        serverRequest.fetchUserDataInBackground(user, new GetUserCallback() {
            @Override
            public void Done(User returnedUser) {
                if (returnedUser == null) {
                    showError();
                } else {
                    if (returnedUser.getKodeRole() != 3)
                        showError();
                    else
                        logUserIn(returnedUser);
                }
            }
        });
    }

    private void logUserIn(User returnedUser) {
        DatabaseHandler databaseHandler = new DatabaseHandler(this);
        databaseHandler.createUser(returnedUser);
        this.finish();
        startActivity(new Intent(this, Dashboard.class));
    }

    private void showError() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("Maaf, Username/Password Salah !");
        alert.setPositiveButton("Tutup", null);
        alert.show();
    }

    @Override
    public void onBackPressed() {
        Login.super.onBackPressed();
    }

    //NEW
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//        tempIDFCM = new TempIDFCM(getApplicationContext());
//        token = tempIDFCM.getToken();
//        username = (EditText) findViewById(R.id.username);
//        password = (EditText) findViewById(R.id.password);
//        signin = (Button) findViewById(R.id.signin);
//        signin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                authenticateUser();
//            }
//        });
//        forgot = (TextView) findViewById(R.id.forgot);
//        forgot.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(Login.this, ForgotPassword.class));
//            }
//        });
//        userNetwork = new UserNetwork(this);
//    }
//
//    private void authenticateUser() {
//        if (StringHelper.isNotEmpty(username.getText().toString()) && StringHelper.isNotEmpty(password.getText().toString())) {
//            if(!StringHelper.isNotEmpty(token)){
//                token = tempIDFCM.getToken();
//            }
////            userNetwork.login(username.getText().toString(), password.getText().toString(), token, new JsonCallback() {
//            userNetwork.login(username.getText().toString(), password.getText().toString(), new JsonCallback() {
//                @Override
//                public void Done(JSONObject jsonObject, String message) {
//                    if (message.equals(ConnectionHandler.response_message_success) && jsonObject != null) {
//                        try {
//                            JSONObject data = new JSONObject(jsonObject.getString(ConnectionHandler.response_data));
//                            user = new User(data.getInt("id"), data.getInt("kd_role"), data.getInt("kd_kota"), data.getInt("kd_area")
//                                    , data.getString("nik"), data.getString("nama"), data.getString("alamat")
//                                    , data.getString("telepon"), data.getString("foto"), data.getString("username"), data.getString("password"), data.getString("email_user"), 0, data.getString("id_gcm"));
//                            logUserIn(user);
//                        } catch (JSONException e) {
//                            Toast.makeText(Login.this, "Terjadi kesalahan, mohon coba kembali.", Toast.LENGTH_LONG).show();
//                            e.printStackTrace();
//                        }
//                    } else {
//                        Toast.makeText(Login.this, message, Toast.LENGTH_LONG).show();
//                    }
//                }
//            });
//        } else
//            Toast.makeText(this, "Pastikan username dan password telah terisi", Toast.LENGTH_LONG).show();
//    }
//
//    private void logUserIn(User returnedUser) {
//        DatabaseHandler databaseHandler = new DatabaseHandler(this);
//        databaseHandler.createUser(returnedUser);
//        this.finish();
//        startActivity(new Intent(this, SplashScreen.class));
//    }
//
//    private void showError() {
//        AlertDialog.Builder alert = new AlertDialog.Builder(this);
//        alert.setMessage("Wrong Credential !");
//        alert.setPositiveButton("Got It", null);
//        alert.show();
//    }
//
//    @Override
//    public void onBackPressed() {
//        Login.super.onBackPressed();
//    }

}
