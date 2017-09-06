package android.trikarya.growth;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import Master.DatabaseHandler;
import Master.GetAllDataCallback;
import Master.GetVisitPlanCallback;
import Master.ServerRequest;
import Master.User;
/**
 *   PT Trikarya Teknologi
 */
public class Dashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView username;
    DatabaseHandler databaseHandler;
    ServerRequest serverRequest;
    private String PROJECT_NUMBER = "1095564458173";
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(android.trikarya.growth.R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(android.trikarya.growth.R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(android.trikarya.growth.R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, android.trikarya.growth.R.string.navigation_drawer_open, android.trikarya.growth.R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(android.trikarya.growth.R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        databaseHandler = new DatabaseHandler(this);
        if(databaseHandler.getUserCount() == 0)
            startActivity(new Intent(this,Login.class));
        else {
            username = (TextView) navigationView.getHeaderView(0).findViewById(android.trikarya.growth.R.id.tvusername);
            username.setText(databaseHandler.getUser().getNama());
            user = databaseHandler.getUser();
            if (user.getStatus() == 0) {
                GCMClientManager pushClientManager = new GCMClientManager(this, PROJECT_NUMBER);
                pushClientManager.registerIfNeeded(new GCMClientManager.RegistrationCompletedHandler() {
                    @Override
                    public void onSuccess(String registrationId, boolean isNewRegistration) {
                        if(isNewRegistration)
                            databaseHandler.createKonfigurasi(1);
                        getData(0);
                        user.setStatus(1);
                        databaseHandler.updateUser(user);
                    }

                    @Override
                    public void onFailure(String ex) {
                        super.onFailure(ex);
                    }
                });
            }
        }
    }

    private void getData(int status)
    {
        Toast.makeText(this, "Getting data from server, please wait",
                Toast.LENGTH_LONG).show();
        serverRequest = new ServerRequest(this);
        if(status == 0) {
            serverRequest.getAllData(this, user, new GetAllDataCallback() {
                @Override
                public void Done(String message) {
                    if(!message.equals("success"))
                        showError(message);
                }
            });
        }
        else
            serverRequest.fetchVisitPlanDataInBackground(user, new GetVisitPlanCallback() {
                @Override
                public void Done(String message) {
                    if(!message.equals("success"))
                        Toast.makeText(Dashboard.this, "Getting data from server failed because of "+message,
                                Toast.LENGTH_LONG).show();
                }
            });
    }

    private void showError(String messsage) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage(messsage);
        alert.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getData(0);
            }
        });
        alert.show();
    }

    public void goToMenu(View view)
    {
        Intent intent = null;
        switch (view.getId()){
            case android.trikarya.growth.R.id.dash_sv:
                intent = new Intent(this,SubmitVisit.class);
                break;
            case android.trikarya.growth.R.id.dash_vp:
                intent = new Intent(this,VisitPlan.class);
                break;
            case android.trikarya.growth.R.id.dash_ol:
                intent = new Intent(this,OutletList.class);
                break;
            case android.trikarya.growth.R.id.dash_tp:
                intent = new Intent(this,TakePhoto.class);
                break;
            case android.trikarya.growth.R.id.dash_draft:
                intent = new Intent(this,Draft.class);
                break;
            default:
                intent = null;
                databaseHandler.deleteAll();
                getData(0);
                break;
        }
        if(intent != null) {
            startActivity(intent);
            this.finish();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(android.trikarya.growth.R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Really Exit?")
                    .setMessage("Are you sure you want to exit?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            Dashboard.super.onBackPressed();
                        }
                    }).create().show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(android.trikarya.growth.R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.trikarya.growth.R.id.nav_about) {
            startActivity(new Intent(this,About.class));
            this.finish();
        }
        else if (id == android.trikarya.growth.R.id.nav_profile) {
            startActivity(new Intent(this, ProfilUser.class));
            this.finish();
        }
        else if (id == android.trikarya.growth.R.id.nav_history) {
            startActivity(new Intent(this, History.class));
            this.finish();
        }
        else if (id == android.trikarya.growth.R.id.nav_logout) {
            databaseHandler.deleteAll();
            databaseHandler.deleteUser();
            this.finish();
            startActivity(new Intent(Dashboard.this, SplashScreen.class));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(android.trikarya.growth.R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
