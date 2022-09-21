package com.android.uraall.drugsdbwithroom;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import Data.DrugsAppDatabase;
import Model.Drug;

public class MainActivity extends AppCompatActivity {

    private DrugsAdapter drugsAdapter;
    private ArrayList<Drug> drugArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
//    private DatabaseHandler dbHandler;
    private DrugsAppDatabase drugsAppDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
//        dbHandler = new DatabaseHandler(this);
        drugsAppDatabase = Room.databaseBuilder(getApplicationContext(),
                DrugsAppDatabase.class, "DrugsDB")
                .build();

        new GetAllDrugsAsyncTask().execute();

        drugsAdapter = new DrugsAdapter(this, drugArrayList, MainActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(drugsAdapter);


        FloatingActionButton floatingActionButton =
                (FloatingActionButton) findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAndEditDrugs(false, null, -1);
            }


        });


    }

    public void addAndEditDrugs(final boolean isUpdate, final Drug drug, final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.layout_add_drug, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilderUserInput.setView(view);

        TextView newDrugTitle = view.findViewById(R.id.newDrugTitle);
        final EditText nameEditText = view.findViewById(R.id.nameEditText);
        final EditText priceEditText = view.findViewById(R.id.priceEditText);

        newDrugTitle.setText(!isUpdate ? "Add Drug" : "Edit Drug");

        if (isUpdate && drug != null) {
            nameEditText.setText(drug.getName());
            priceEditText.setText(drug.getPrice());
        }

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(isUpdate ? "Update" : "Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                })
                .setNegativeButton(isUpdate ? "Delete" : "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {

                                if (isUpdate) {

                                    deleteDrug(drug, position);
                                } else {

                                    dialogBox.cancel();

                                }

                            }
                        });


        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(nameEditText.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Enter drug name!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(priceEditText.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Enter drug price!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }


                if (isUpdate && drug != null) {

                    updateDrug(nameEditText.getText().toString(), priceEditText.getText().toString(), position);
                } else {

                    createDrug(nameEditText.getText().toString(), priceEditText.getText().toString());
                }
            }
        });
    }

    private void deleteDrug(Drug drug, int position) {

        drugArrayList.remove(position);

        new DeleteDrugAsyncTask().execute(drug);

    }

    private void updateDrug(String name, String price, int position) {

        Drug drug = drugArrayList.get(position);

        drug.setName(name);
        drug.setPrice(price);

        new UpdateDrugAsyncTask().execute(drug);

        drugArrayList.set(position, drug);


    }

    private void createDrug(String name, String price) {

        new CreateDrugAsyncTask().execute(new Drug(0, name, price));

    }

    private class GetAllDrugsAsyncTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {

            drugArrayList.addAll(drugsAppDatabase.getDrugDAO().getAllDrugs());

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            drugsAdapter.notifyDataSetChanged();
        }
    }


    private class CreateDrugAsyncTask extends AsyncTask<Drug, Void, Void> {


        @Override
        protected Void doInBackground(Drug... drugs) {

            long id = drugsAppDatabase.getDrugDAO().addDrug(
                    drugs[0]
            );


            Drug drug = drugsAppDatabase.getDrugDAO().getDrug(id);

            if (drug != null) {

                drugArrayList.add(0, drug);


            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            drugsAdapter.notifyDataSetChanged();
        }
    }

    private class UpdateDrugAsyncTask extends AsyncTask<Drug, Void, Void> {

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            drugsAdapter.notifyDataSetChanged();
        }

        @Override
        protected Void doInBackground(Drug... drugs) {

            drugsAppDatabase.getDrugDAO().updateDrug(drugs[0]);

            return null;
        }
    }

    private class DeleteDrugAsyncTask extends AsyncTask<Drug, Void, Void> {

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            drugsAdapter.notifyDataSetChanged();
        }

        @Override
        protected Void doInBackground(Drug... drugs) {

            drugsAppDatabase.getDrugDAO().deleteDrug(drugs[0]);


            return null;
        }
    }


}














