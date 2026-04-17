package com.example.passwordvault.activities;

import android.app.Activity;
import android.graphics.Typeface;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Ui {

 static TextView title(Activity a, String s) {
  TextView v = new TextView(a);
  v.setText(s);
  v.setTextSize(26);
  v.setTypeface(null, Typeface.BOLD);
  return v;
 }

 static EditText input(Activity a, String hint) {
  EditText e = new EditText(a);
  e.setHint(hint);
  e.setSingleLine(!hint.toLowerCase().contains("note"));
  return e;
 }

 static Button button(Activity a, String s) {
  Button b = new Button(a);
  b.setText(s);
  b.setAllCaps(false);
  b.setLayoutParams(new LinearLayout.LayoutParams(
          LinearLayout.LayoutParams.MATCH_PARENT,
          LinearLayout.LayoutParams.WRAP_CONTENT
  ));
  return b;
 }

 static TextView text(Activity a, String s, int size) {
  TextView v = new TextView(a);
  v.setText(s);
  v.setTextSize(size);
  v.setPadding(0, 8, 0, 8);
  return v;
 }
}