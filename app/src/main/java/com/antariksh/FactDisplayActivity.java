package com.antariksh;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.support.constraint.solver.widgets.Snapshot;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ScrollingView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.antariksh.helper.FactStore;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import fr.castorflex.android.verticalviewpager.VerticalViewPager;

public class FactDisplayActivity extends AppCompatActivity {
    private static final float MIN_SCALE = 0.75f;
    private static final float MIN_ALPHA = 0.75f;
    private DatabaseReference myRef;
    private ArrayList<FactStore> allFacts;
    private ProgressDialog pd;
    private static String KEY_POSITION="Argument";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_fact_display);
        final VerticalViewPager verticalViewPager = (VerticalViewPager) findViewById(R.id.verticalviewpager);
        pd=new ProgressDialog(this);
        pd.setTitle("Loading facts");
        pd.show();

        allFacts = new ArrayList<>();
        myRef = FirebaseDatabase.getInstance().getReference().child("Facts").child(getIntent().getStringExtra("Theme"));
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Toast.makeText(getApplicationContext(),snapshot.getKey().toString(),Toast.LENGTH_LONG).show();
                    FactStore factStore = new FactStore(snapshot.getKey(), snapshot.child("picture").getValue().toString(), snapshot.child("description").getValue().toString());
                    allFacts.add(factStore);
                    Log.d("Message--", "Fact" + allFacts.size() + " Added");
                }
                verticalViewPager.setAdapter(new DummyAdapter(FactDisplayActivity.this,allFacts));
                pd.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Sorry.! An error occurred", Toast.LENGTH_LONG).show();
            }
        });

        verticalViewPager.setPageMargin(getResources().getDimensionPixelSize(R.dimen.pagemargin));

        verticalViewPager.setPageMarginDrawable(new ColorDrawable(getResources().getColor(android.R.color.background_dark)));

        verticalViewPager.setPageTransformer(true, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View view, float position) {
                int pageWidth = view.getWidth();
                int pageHeight = view.getHeight();

                if (position < -1) { // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    view.setAlpha(0);

                } else if (position <= 1) { // [-1,1]
                    // Modify the default slide transition to shrink the page as well
                    float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                    float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                    float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                    if (position < 0) {
                        view.setTranslationY(vertMargin - horzMargin / 2);
                    } else {
                        view.setTranslationY(-vertMargin + horzMargin / 2);
                    }

                    // Scale the page down (between MIN_SCALE and 1)
                    view.setScaleY(scaleFactor);

                    // Fade the page relative to its size.
                    view.setAlpha(MIN_ALPHA +
                            (scaleFactor - MIN_SCALE) /
                                    (1 - MIN_SCALE) * (1 - MIN_ALPHA));
                } else { // (1,+Infinity]
                    // This page is way off-screen to the right.
                    view.setAlpha(0);
                }
            }
        });

    }

    public class DummyAdapter extends PagerAdapter {
        Context context;
        ArrayList<FactStore> al;

        public DummyAdapter(Context context, ArrayList<FactStore> al) {
            this.context = context;
            this.al=al;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(context.LAYOUT_INFLATER_SERVICE);
            View rootView = inflater.inflate(R.layout.fact, container, false);
            ImageView imgv= (ImageView) rootView.findViewById(R.id.factImage);
            TextView textViewTopic= (TextView) rootView.findViewById(R.id.factTitle);
            TextView textViewDiscript= (TextView) rootView.findViewById(R.id.factdiscript);
            textViewTopic.setText(allFacts.get(position).getTopic());
            textViewDiscript.setText(allFacts.get(position).getDescription());
            container.addView(rootView);
            Picasso.with(context).load(allFacts.get(position).getPicture()).into(imgv);
            return  rootView;

        }

        @Override
        public int getCount() {
            return allFacts.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((RelativeLayout) object);

        }
    }

}

