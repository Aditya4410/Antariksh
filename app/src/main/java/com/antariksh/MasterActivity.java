package com.antariksh;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.antariksh.helper.SharedPrefManager;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class MasterActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener {

    FirebaseUser user;
    FirebaseAuth mAuth;
    ImageView profilePic;
    TextView textViewProfileName,textViewProfileEmail;
    CardView cardViewWildlife,cardViewSpace,cardViewHistory,cardViewMechanics,cardViewNasaProj,cardViewNature;
    SharedPrefManager prefManager;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_master);
        prefManager = new SharedPrefManager(this);
        progressDialog= new ProgressDialog(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        toolbar.setTitle("");
        toolbar.setLogo(R.drawable.titlesmall);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        LayoutInflater layoutInflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.nav_header_master, navigationView);

        mAuth= FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        profilePic= (ImageView) view.findViewById(R.id.profilePic);
        textViewProfileName= (TextView) view.findViewById(R.id.textViewProfileName);
        textViewProfileEmail= (TextView) view.findViewById(R.id.textViewProfileEmail);
        Picasso.with(MasterActivity.this).load(user.getPhotoUrl()).into(profilePic);
        textViewProfileName.setText(user.getDisplayName());
        textViewProfileEmail.setText(user.getEmail());

        cardViewWildlife= (CardView) findViewById(R.id.cardWildlife);
        cardViewSpace= (CardView) findViewById(R.id.cardSpace);
        cardViewHistory= (CardView) findViewById(R.id.cardHistory);
        cardViewMechanics= (CardView) findViewById(R.id.cardMechanics);
        cardViewNasaProj= (CardView) findViewById(R.id.cardNasaProjects);
        cardViewNature= (CardView) findViewById(R.id.cardNature);

        cardViewWildlife.setOnClickListener(this);
        cardViewSpace.setOnClickListener(this);
        cardViewHistory.setOnClickListener(this);
        cardViewMechanics.setOnClickListener(this);
        cardViewNasaProj.setOnClickListener(this);
        cardViewNature.setOnClickListener(this);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.master, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_leaderboard) {
            // Handle the camera action
        } else if (id == R.id.nav_logout) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Logged in as: "+user.getDisplayName());
            builder.setMessage("Sure To logout?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    progressDialog.setTitle("Logging out...");
                    progressDialog.show();
                    prefManager.setLoggedIn(false);
                    if (mAuth!=null)
                        mAuth.signOut();
                    disconnectFromFacebook();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                 dialogInterface.dismiss();
                }
            });
            builder.create().show();
        } else if (id == R.id.nav_about) {

        } else if (id == R.id.nav_explore) {

        } else if (id == R.id.nav_feedback) {

        } else if (id == R.id.nav_quiz) {
            finish();
            startActivity(new Intent(MasterActivity.this,QuizActivity.class));

        } else if (id==R.id.nav_videos){
            startActivity(new Intent(MasterActivity.this,VideoDisplay.class));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(MasterActivity.this,FactDisplayActivity.class);
        if (view== cardViewWildlife) intent.putExtra("Theme","Wildlife");
        else if (view== cardViewSpace) intent.putExtra("Theme","Space");
        else if (view== cardViewMechanics) intent.putExtra("Theme","Technology");
        else if (view== cardViewHistory) intent.putExtra("Theme","History");
        else if (view== cardViewNasaProj) intent.putExtra("Theme","NASA");
        else if (view== cardViewNature) intent.putExtra("Theme","Nature");
        startActivity(intent);

    }

    public void disconnectFromFacebook()
    {
        if (AccessToken.getCurrentAccessToken() == null) {
            return;
        }

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {
                LoginManager.getInstance().logOut();
                if (progressDialog!=null)
                    progressDialog.dismiss();

                startActivity(new Intent(MasterActivity.this,LoginActivity.class));
                finish();
            }
        }).executeAsync();
    }
}
