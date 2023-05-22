package com.fashionstore.fashion.database;

import android.app.Application;
import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.fashionstore.fashion.prefs.DataStoreManager;
import com.fashionstore.fashion.utils.Constant;


public class ControllerApplication extends Application {

    private FirebaseDatabase firebaseDatabase;

    public static ControllerApplication get(Context context){
        return (ControllerApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance(Constant.FIREBASE_URL);
        DataStoreManager.init(getApplicationContext());
    }

//    public DatabaseReference getDatabaseReference() {
//        return firebaseDatabase.getReference("/products");
//    }

    public DatabaseReference getWomenDatabaseReference() {
        return firebaseDatabase.getReference("/product/women");
    }
    public DatabaseReference getMenDatabaseReference() {
        return firebaseDatabase.getReference("/product/men");
    }
    public DatabaseReference getKidsDatabaseReference() {
        return firebaseDatabase.getReference("/product/kids");
    }

    public DatabaseReference getFeedbackDatabaseReference() {
        return firebaseDatabase.getReference("/feedback");
    }

    public DatabaseReference getBookingDatabaseReference() {
        return firebaseDatabase.getReference("/booking");
    }
    public DatabaseReference getUserDatabaseReference() {
        return firebaseDatabase.getReference("/users");
    }

}
