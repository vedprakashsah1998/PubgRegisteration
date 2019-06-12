package com.client.vpman.pubgregisteration;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.UUID;


public class Home extends Fragment
{

    String NameHolder, DescHold,DateHold,TimeHold,MoneyHold,Img,images;

    Firebase firebase;
    TournamentDetail tournamentDetail=new TournamentDetail();

    Button SubmitButton;

    DatabaseReference databaseReference;
    public static final String Database_Path = "Tournament_detail";
    public static final String Firebase_Server_URL = "https://insertdata-android-examples.firebaseio.com/";
   static TextInputEditText textInputEditText,textInputEditText1,textInputEditText2,textInputEditText3,money;
    final Calendar myCalendar = Calendar.getInstance();
    View view;
    String selectedDate;

    public static final int REQUEST_CODE = 11;
    StorageReference storageReference;
    ImageView imageView;
    FirebaseStorage storage;
    private Uri filepath;

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home, container, false);


        textInputEditText = view.findViewById(R.id.name);
        textInputEditText1 = view.findViewById(R.id.desc);
        textInputEditText2 = view.findViewById(R.id.date1);
        textInputEditText3 = view.findViewById(R.id.time1);
        SubmitButton=view.findViewById(R.id.submit);
        money=view.findViewById(R.id.money1);
        imageView=view.findViewById(R.id.ImgView);
        final FragmentManager fm = ((AppCompatActivity)getActivity()).getSupportFragmentManager();
        textInputEditText2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                  /*  if (event.getRawX() >= (textInputEditText2.getRight() - textInputEditText2.getCompoundDrawables()
                            [DRAWABLE_RIGHT].getBounds().width())) {*/


                        DialogFragment datePicker = new DatePickerFragment();
                        datePicker.setTargetFragment(Home.this,REQUEST_CODE);
                        datePicker.show( fm, "date picker");

                     /*   return true;
                    }*/
                }
                return false;
            }
        });



        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                chooseImage();
            }
        });

        textInputEditText3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                   /* if (event.getRawX() >= (textInputEditText3.getRight() - textInputEditText3.getCompoundDrawables()
                            [DRAWABLE_RIGHT].getBounds().width())) {*/
                        DialogFragment timePicker = new TimePickerFragment();
                        timePicker.show(getFragmentManager(), "time picker");
/*

                        return true;
                    }*/
                }
                return false;
            }
        });

        Firebase.setAndroidContext(getActivity());

        firebase = new Firebase(Firebase_Server_URL);


        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();

        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);

        SubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadImage();



            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK && data!=null &&data.getData()!=null)
        {
            // get date from string
          /*  selectedDate = data.getStringExtra("selectedDate");
            // set the value of the editText
            textInputEditText2.setText(selectedDate);*/

            filepath=data.getData();
            try {
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),filepath);
                imageView.setImageBitmap(bitmap);

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }


        }
    }

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }*/




    private void chooseImage()
    {
        Intent intent=new Intent();
        intent.setType("image/*");
       // intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"select picture"),REQUEST_CODE);
    }

    private void uploadImage() {
        if (filepath!=null)
        {
            final ProgressDialog progressDialog=new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();


            final StorageReference ref=storageReference.child("images/"+ UUID.randomUUID().toString());
            UploadTask uploadTask=ref.putFile(filepath);
            Task<Uri> urlTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful())
                    {
                        throw task.getException();
                    }
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful())
                    {
                        NameHolder=textInputEditText.getText().toString().trim();
                        DescHold=textInputEditText1.getText().toString().trim();
                        DateHold=textInputEditText2.getText().toString().trim();
                        TimeHold=textInputEditText3.getText().toString().trim();
                        MoneyHold=money.getText().toString().trim();

                        tournamentDetail.setNameHolder1(NameHolder);
                        tournamentDetail.setDateHold1(DateHold);
                        tournamentDetail.setDescHold1(DescHold);
                        tournamentDetail.setTimeHold1(TimeHold);
                        tournamentDetail.setMoneyHold1(MoneyHold);

                        Uri url=task.getResult();

                        progressDialog.dismiss();
                        Img=databaseReference.push().getKey();
                        //databaseReference.child(Img).setValue(url.toString());
                        tournamentDetail.setImage(url.toString());
                       // String StudentRecordIDFromServer = databaseReference.push().getKey();
                        databaseReference.child(Img).setValue(tournamentDetail);
                        Toast.makeText(getActivity(),"Data Inserted Successfully into Firebase Database", Toast.LENGTH_LONG).show();
/*
                        databaseReference.child(key).child("likes").setValue(0);
*/
                        Toast.makeText(getActivity(),"Uploaded",Toast.LENGTH_LONG).show();

                    }
                    else
                    {

                    }
                }
            });




        }
        else {
            Toast.makeText(getActivity(),"No File is Selected",Toast.LENGTH_LONG).show();
        }
    }

    public static Home newInstance(String text) {
        Home f = new Home();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);
        return f;
    }
}
