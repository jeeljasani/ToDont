package rocks.poopjournal.todont;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import rocks.poopjournal.todont.Fragments.LabelsAdapter;
import rocks.poopjournal.todont.showcaseview.RippleBackground;
import rocks.poopjournal.todont.showcaseview.ShowcaseViewBuilder;
import rocks.poopjournal.todont.utils.SharedPrefUtils;
import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType;
import smartdevelop.ir.eram.showcaseviewlib.config.Gravity;
import smartdevelop.ir.eram.showcaseviewlib.config.PointerType;
import smartdevelop.ir.eram.showcaseviewlib.listener.GuideListener;

public class Labels extends AppCompatActivity {
    RecyclerView rv_labels;
    TextView tv_label;
    Db_Controller db;
    LabelsAdapter adapter;
    ArrayList<String> gettinglabels = new ArrayList<>();
    FloatingActionButton labels_floatingbutton;
    public ShowcaseViewBuilder showcaseViewBuilder;
    private SharedPrefUtils prefUtils;

    SharedPreferences sharedPreferences;
    private RippleBackground fabHighlighter, tvHighlighter, btnHighlighter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labels);
        rv_labels = findViewById(R.id.rv_labels);
        tv_label = findViewById(R.id.tv_label);
        prefUtils = new SharedPrefUtils(this);

        sharedPreferences=getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        showcaseViewBuilder = ShowcaseViewBuilder.init(this);
        fabHighlighter = (RippleBackground) findViewById(R.id.fab_highlighter);

        db = new Db_Controller(getApplicationContext(), "", null, 2);
        db.show_labels();
        labels_floatingbutton = findViewById(R.id.label_floatingbtn);

        for (int i = 0; i < Helper.labels_array.size(); i++) {
            gettinglabels.add(Helper.labels_array.get(i));
        }

        rv_labels.setLayoutManager(new LinearLayoutManager(this));
        new ItemTouchHelper(itemtouchhelper).attachToRecyclerView(rv_labels);
        adapter= new LabelsAdapter(this,db,gettinglabels);
        rv_labels.setAdapter(adapter);
        rv_labels.setLayoutManager(new LinearLayoutManager(this));

        labels_floatingbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!prefUtils.getBool("plus1")) {
                    showcaseFab();
                } else {

                    final Dialog d = new Dialog(Labels.this);
                    d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    d.setContentView(R.layout.dialogbox_labels);
                    d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    Button btnsave = d.findViewById(R.id.saveLabelButton);
                    final EditText editText = d.findViewById(R.id.label);
                    WindowManager.LayoutParams lp = d.getWindow().getAttributes();
                    lp.dimAmount = 0.9f;
                    d.getWindow().setAttributes(lp);
                    btnsave.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String entered_text = editText.getText().toString().replace("'", "''");
                            db.insert_label(entered_text);
                            Helper.labels_array.add(entered_text);
                            db.show_labels();
                            Intent i = new Intent(getApplicationContext(), Labels.class);
                            startActivity(i);
                            overridePendingTransition(0, 0);

                        }
                    });
                    d.show();
                }


               /* final Dialog d = new Dialog(Labels.this);
                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                d.setContentView(R.layout.dialogbox_labels);
                d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                Button btnsave=d.findViewById(R.id.saveLabelButton);
                final EditText editText = d.findViewById(R.id.label);
                WindowManager.LayoutParams lp = d.getWindow().getAttributes();
                lp.dimAmount=0.9f;
                d.getWindow().setAttributes(lp);
                btnsave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String entered_text = editText.getText().toString().replace("'","''");
                        db.insert_label(entered_text);
                        Helper.labels_array.add(entered_text);
                        db.show_labels();
                        Intent i = new Intent(getApplicationContext(), Labels.class);
                        startActivity(i);
                        overridePendingTransition(0, 0);

                    }
                });d.show();*/
/*                View bottomsheetview=null;
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Labels.this,
                        R.style.BottomSheetDialogTheme);
                if(Helper.isnightmodeon.equals("no")){
                    bottomsheetview = LayoutInflater.from(getApplicationContext()).
                            inflate(R.layout.labels_bottom_sheet,
                                    (RelativeLayout) view.findViewById(R.id.bottomsheetContainer));
                }
                else if(Helper.isnightmodeon.equals("yes")){
                    bottomsheetview = LayoutInflater.from(getApplicationContext()).
                            inflate(R.layout.labels_bottom_sheet_night,
                                    (RelativeLayout) view.findViewById(R.id.bottomsheetContainer));
                }
                final EditText editText = bottomsheetview.findViewById(R.id.label);
                Button saveLabelButton = bottomsheetview.findViewById(R.id.saveLabelButton);
                saveLabelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        db.insert_label(editText.getText().toString());
                        Helper.labels_array.add(editText.getText().toString());
                        db.show_labels();
                        Intent i = new Intent(getApplicationContext(), Labels.class);
                        startActivity(i);
                        overridePendingTransition(0, 0);
                        bottomSheetDialog.dismiss();
                    }
                });
                bottomSheetDialog.setContentView(bottomsheetview);
                bottomSheetDialog.show();*/
            }

        });
    }
    ItemTouchHelper.SimpleCallback itemtouchhelper = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
            if (direction == 8) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(Labels.this);
                builder1.setMessage("Do you really want to delete this?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                int i = viewHolder.getAdapterPosition();
                                db.delete_label(gettinglabels.get(i));
                                Intent intent = new Intent(getApplicationContext(), Labels.class);
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(getApplicationContext(), Labels.class);
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                                dialog.dismiss();
                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();

            }

        }
    };
    public void backbtnclicked(View view) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
    private void showcaseFab() {
//        showcaseViewBuilder.setTargetView(labels_floatingbutton)
//                .setBackgroundOverlayColor(0xcc000000)
//                .setBgOverlayShape(ShowcaseViewBuilder.ROUND_RECT)
//                .setRoundRectCornerDirection(ShowcaseViewBuilder.TOP_LEFT)
//                .setRoundRectOffset(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 170, getResources().getDisplayMetrics()))
//                .setRingColor(0xcc8e8e8e)
//                .setShowcaseShape(ShowcaseViewBuilder.SHAPE_CIRCLE)
//                .setRingWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()))
//                .setMarkerDrawable(getResources().getDrawable(R.drawable.arrow_up), Gravity.LEFT)
//                .addCustomView(R.layout.fab_label_description_view, Gravity.LEFT, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources().getDisplayMetrics()), TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, -228, getResources().getDisplayMetrics()), TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, -160, getResources().getDisplayMetrics()) ,0);
////                    .addCustomView(R.layout.fab_description_view, Gravity.CENTER);
//
//        showcaseViewBuilder.show();
//
//        showcaseViewBuilder.setClickListenerOnView(R.id.btn, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                prefUtils.setBool("plus1", true);
//
//                showcaseViewBuilder.hide();
//            }
//        });


        GuideView.Builder guideView = new GuideView.Builder(Labels.this)
                                .setContentText("Add Label.")
                .setTargetView(labels_floatingbutton)
                .setDismissType(DismissType.anywhere)
                .setPointerType(PointerType.arrow)
                .setGravity(Gravity.center)
                .setGuideListener(new GuideListener() {
                    @Override
                    public void onDismiss(View view) {
                        prefUtils.setBool("plus1", true);
                    }
                });
        guideView.build().show();


/*        if (!fabHighlighter.isRippleAnimationRunning()) {
            fabHighlighter.startRippleAnimation();
        } else {
            fabHighlighter.stopRippleAnimation();
            showcaseViewBuilder.setTargetView(labels_floatingbutton)
                    .setBackgroundOverlayColor(0xcc000000)
                    .setBgOverlayShape(ShowcaseViewBuilder.ROUND_RECT)
                    .setRoundRectCornerDirection(ShowcaseViewBuilder.TOP_LEFT)
                    .setRoundRectOffset(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 170, getResources().getDisplayMetrics()))
                    .setRingColor(0xcc8e8e8e)
                    .setShowcaseShape(ShowcaseViewBuilder.SHAPE_CIRCLE)
                    .setRingWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()))
                    .setMarkerDrawable(getResources().getDrawable(R.drawable.arrow_up), Gravity.LEFT)
                    .addCustomView(R.layout.fab_label_description_view, Gravity.LEFT, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources().getDisplayMetrics()), TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, -228, getResources().getDisplayMetrics()), TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, -160, getResources().getDisplayMetrics()) ,0);
//                    .addCustomView(R.layout.fab_description_view, Gravity.CENTER);

            showcaseViewBuilder.show();

            showcaseViewBuilder.setClickListenerOnView(R.id.btn, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showcaseViewBuilder.hide();
                }
            });
        }*/
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
