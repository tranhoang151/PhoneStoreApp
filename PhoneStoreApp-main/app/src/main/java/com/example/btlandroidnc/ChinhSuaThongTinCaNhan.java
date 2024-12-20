package com.example.btlandroidnc;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.btlandroidnc.Model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ChinhSuaThongTinCaNhan extends AppCompatActivity {
    DatabaseReference usersRef;
    Button btnLuu, btnHuy;
    EditText txtHovaTen, txtSDT, txtEmail, txtMK, txtNS;
    TextView profileName;
    SharedPreferences sharedPreferences;
    ImageButton bt_KhuyenMai, bt_ThongBao, bt_GioHang, bt_TTCaNhan, btTrangChu;
    DatePickerDialog datePickerDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chinh_sua_thong_tin_ca_nhan);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        ImageButton btDatePicker = findViewById(R.id.buttonDatePicker);

        btDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                // Khởi tạo DatePickerDialog và thiết lập sự kiện lắng nghe
                datePickerDialog = new DatePickerDialog(ChinhSuaThongTinCaNhan.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                // Xử lý khi người dùng chọn ngày
                                String selectedDate = "";
                                String m = "", d = "";
                                if (month < 9) {
                                    m = "0" + String.valueOf(month + 1);
                                } else {
                                    m = String.valueOf(month + 1);
                                }
                                if (dayOfMonth <= 9) {
                                    d = "0" + String.valueOf(dayOfMonth);
                                } else {
                                    d = String.valueOf(dayOfMonth);
                                }
                                selectedDate = d + "/" + m + "/" + year;
                                Toast.makeText(ChinhSuaThongTinCaNhan.this, "Ngày đã chọn: " + selectedDate, Toast.LENGTH_SHORT).show();
                                txtNS.setText(selectedDate);
                            }
                        }, year, month, dayOfMonth);

                // Hiển thị DatePickerDialog
                datePickerDialog.show();
//                Button positiveButton = datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE);
//                positiveButton.setTextColor(Color.RED);
            }
        });

        sharedPreferences = getSharedPreferences("com.example.sharedprerences", Context.MODE_PRIVATE);
        String myId = sharedPreferences.getString("id", "-1");
        usersRef = FirebaseDatabase.getInstance().getReference("Users").child(myId);
//        profileName = findViewById(R.id.profileName);
        txtHovaTen = findViewById(R.id.edHoVaTen);
        txtEmail = findViewById(R.id.edEmail);
        txtSDT = findViewById(R.id.edSDT);
        btnLuu = findViewById(R.id.buttonSave);
        btnHuy = findViewById(R.id.buttonCancel);
        bt_ThongBao = findViewById(R.id.button3);
        bt_TTCaNhan = findViewById(R.id.button5);
        bt_KhuyenMai = findViewById(R.id.button2);
        bt_GioHang = findViewById(R.id.button4);
        btTrangChu = findViewById(R.id.button1);
        txtMK = findViewById(R.id.edMatKhau);
        txtNS = findViewById(R.id.editNgaysinh);

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
//                    profileName.setText(user.getName());
                    txtHovaTen.setText(user.getName());
                    txtEmail.setText(user.getEmail());
                    txtSDT.setText(user.getPhone());
                    txtMK.setText(user.getPassword());

                    DataSnapshot dateOfBirthSnapshot = dataSnapshot.child("date_of_birth");
                    if (dateOfBirthSnapshot.exists()) {
                        int day = dateOfBirthSnapshot.child("day").getValue(Integer.class);
                        int month = dateOfBirthSnapshot.child("month").getValue(Integer.class);
                        int year = dateOfBirthSnapshot.child("year").getValue(Integer.class);

                        String formattedDate = String.format(Locale.getDefault(), "%02d/%02d/%d", day, month, year);
                        txtNS.setText(formattedDate);
                    }

                } else {
                    Toast.makeText(ChinhSuaThongTinCaNhan.this, "User data is null", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ChinhSuaThongTinCaNhan.this, "Failed to retrieve user data", Toast.LENGTH_SHORT).show();
            }
        });

        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String hoVaTen = txtHovaTen.getText().toString().trim();
                String sdt = txtSDT.getText().toString().trim();
                String email = txtEmail.getText().toString().trim();
                String mk = txtMK.getText().toString().trim();
                String ns = txtNS.getText().toString().trim();
                String id = email.replace(".","");

                Date convertedDate = null;
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

                try {
                    // Chuyển đổi chuỗi thành đối tượng Date
                    convertedDate = dateFormat.parse(ns);
                } catch (ParseException e) {
                    e.printStackTrace();
                    // Xử lý khi có lỗi trong quá trình chuyển đổi
                }
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(convertedDate);
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH) + 1;
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                calendar.set(year, month, day);
                Date date = calendar.getTime();
                Log.d("DateDK", String.valueOf(date));

                if (hoVaTen.isEmpty() || sdt.isEmpty() || email.isEmpty() || mk.isEmpty()) {
                    Toast.makeText(ChinhSuaThongTinCaNhan.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                usersRef.getParent().addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boolean emailExists = false;
                        boolean phoneExists = false;

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            User user = snapshot.getValue(User.class);
                            if (user != null) {
                                if (email.equals(user.getEmail()) && !snapshot.getKey().equals(myId)) {
                                    emailExists = true;
                                }
                                if (sdt.equals(user.getPhone()) && !snapshot.getKey().equals(myId)) {
                                    phoneExists = true;
                                }
                                if (emailExists || phoneExists) {
                                    break;
                                }
                            }
                        }

                        if (emailExists) {
                            Toast.makeText(ChinhSuaThongTinCaNhan.this, "Email đã tồn tại", Toast.LENGTH_SHORT).show();
                        } else if (phoneExists) {
                            Toast.makeText(ChinhSuaThongTinCaNhan.this, "Số điện thoại đã tồn tại", Toast.LENGTH_SHORT).show();
                        } else {
                            // Update the User object
                            usersRef.child("name").setValue(hoVaTen);
                            usersRef.child("phone").setValue(sdt);
                            usersRef.child("email").setValue(email);
                            usersRef.child("id").setValue(id);
                            usersRef.child("date_of_birth").child("day").setValue(day);
                            usersRef.child("date_of_birth").child("month").setValue(month);
                            usersRef.child("date_of_birth").child("year").setValue(year);
                            usersRef.child("password").setValue(mk)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(ChinhSuaThongTinCaNhan.this, "Thông tin đã được cập nhật", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(ChinhSuaThongTinCaNhan.this, TrangCaNhan.class);
                                            startActivity(intent);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(ChinhSuaThongTinCaNhan.this, "Không thể cập nhật thông tin. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(ChinhSuaThongTinCaNhan.this, "Không thể kiểm tra email và số điện thoại. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChinhSuaThongTinCaNhan.this, TrangCaNhan.class);
                startActivity(intent);
            }
        });




        bt_TTCaNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ChinhSuaThongTinCaNhan.this, TrangCaNhan.class);
                startActivity(i);
            }
        });

        bt_ThongBao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ChinhSuaThongTinCaNhan.this,TrangThongBao.class);
                startActivity(i);
            }
        });


        bt_KhuyenMai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ChinhSuaThongTinCaNhan.this, TrangKhuyenMai.class);
                startActivity(i);
            }
        });

        // Xử lý khi người dùng click vào nút Giỏ hàng
        bt_GioHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChinhSuaThongTinCaNhan.this, TrangGioHang.class);
                startActivity(intent);
            }
        });

        btTrangChu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChinhSuaThongTinCaNhan.this, TrangChu.class);
                startActivity(intent);
            }
        });

    }
}
