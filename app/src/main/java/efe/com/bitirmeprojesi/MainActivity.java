package efe.com.bitirmeprojesi;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //Arayüzdeki degiskenler
    private EditText mEmailDegisken;
    private EditText mParolaDegisken;

    //FirebaseAuth sınıfının nesne örneği
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Edit Textler
        mEmailDegisken = findViewById(R.id.edt_email);
        mParolaDegisken = findViewById(R.id.edt_parola);

        //Butonlar
        findViewById(R.id.btn_giris).setOnClickListener(this);
        findViewById(R.id.btn_kayit_ol).setOnClickListener(this);

        //FirebaseAuth nesne değişkeni örneklendiriliyor.
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        arayuzuGuncelle(currentUser);
    }

    //firebase yardımı ile kullanıcıları kayıt eder.
    private void hesapOlustur(String email, String password){
        if (!formuDogrula()){
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //Kayıt başarılı ise, arayüzü kullanıcı bilgileri ile güncelle.
                            FirebaseUser user = mAuth.getCurrentUser();
                            arayuzuGuncelle(user);
                        }else{
                            //Kayıt başarısız ise arayüzü null ile güncelle
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            arayuzuGuncelle(null);
                        }
                    }
                });
    }

    //email ve parolanın belirtilen kriterlere göre girilip girilmediğini kontrol eder.
    private boolean formuDogrula() {
        boolean valid = true;

        String email = mEmailDegisken.getText().toString();
        if(TextUtils.isEmpty(email)){
            mEmailDegisken.setError("Gerekli");
            valid = false;
        }else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            mEmailDegisken.setError("Geçersiz Email");
            valid=false;
        }else{
            mEmailDegisken.setError(null);
        }

        String parola = mParolaDegisken.getText().toString();
        if(TextUtils.isEmpty(parola)){
            mParolaDegisken.setError("Gerekli");
            valid = false;
        }else if(parola.length() < 6){
            mParolaDegisken.setError("En Az 6 Karakter Olmalı");
            valid=false;
        }else{
            mParolaDegisken.setError(null);
        }
        return valid;
    }

    //Giriş yapmış bir kullanıcının olup olmadığını uygulama başlığında belirtiyor.
    private void arayuzuGuncelle(FirebaseUser currentUser) {
        if(currentUser == null){
            this.setTitle("kullanıcı boş");
        }else{
            this.setTitle("kullanıcı giriş yaptı");
        }

    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if(i == R.id.btn_giris){
            //signIn(mEmailDegisken.getText().toString(), mParolaDegisken.getText().toString());
        }else if(i == R.id.btn_kayit_ol){
            hesapOlustur(mEmailDegisken.getText().toString(), mParolaDegisken.getText().toString());
        }
    }
}
