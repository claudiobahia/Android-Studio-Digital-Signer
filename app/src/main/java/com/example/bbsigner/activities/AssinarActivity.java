package com.example.bbsigner.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bbsigner.R;
import com.example.bbsigner.classes.AssinaturaDados;
import com.example.bbsigner.classes.Screenshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static java.lang.String.format;

public class AssinarActivity extends AppCompatActivity {

    private Button mbtnLimpar, mbtnSalvar;
    private File file;
    private LinearLayout mContent;
    private View view;
    private signature mSignature;
    private String DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/UserSignature/";
    private Date date = new Date();
    private String dataAssinatura = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(date);
    private String StoredPath = DIRECTORY + dataAssinatura + ".jpg";
    private TextView mtxtData;
    private TextView mtxtAtendente;

    private ArrayList<AssinaturaDados> dados = new ArrayList();
    private AssinaturaDados dado;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assinar);

        mFirebaseDatabase = FirebaseDatabase.getInstance();

        String nome = getIntent().getStringExtra("atendente");
        String outro = getIntent().getStringExtra("outro");
        String descricao = getIntent().getStringExtra("descricao");
        mtxtData = findViewById(R.id.txtDataAss);
        mtxtAtendente = findViewById(R.id.txtAtendenteAss);
        mtxtData.setText("Data de assinatura: " + new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss").format(date));
        mtxtAtendente.setText("Nome do atendente: " + nome);

        //dados = load(dados);
        dado = new AssinaturaDados(nome, outro, descricao, dataAssinatura);
        /*
        dados.add(dado);
        Log.v("log_dado", dado.toString());
         */

        mContent = findViewById(R.id.canvasLayout);
        mSignature = new signature(getApplicationContext(), null);
        mSignature.setBackgroundColor(Color.WHITE);
        mContent.addView(mSignature, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mbtnLimpar = findViewById(R.id.clear);
        mbtnSalvar = findViewById(R.id.getsign);
        mbtnSalvar.setEnabled(false);
        view = mContent;
        mbtnSalvar.setOnClickListener(onButtonClick);
        mbtnLimpar.setOnClickListener(onButtonClick);

        file = new File(DIRECTORY);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    Button.OnClickListener onButtonClick = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == mbtnLimpar) {
                Log.v("log_tag", "Panel Cleared");
                mSignature.clear();
                mbtnSalvar.setEnabled(false);
            } else if (v == mbtnSalvar) {
                saveFirebase();
                if (!isStoragePermissionGranted()) {
                } else {
                    view.setDrawingCacheEnabled(true);
                    OutputStream outputStream;
                    try {
                        Bitmap bitmap = Screenshot.takescreenshotOfRootView(mContent);
                        File file = new File(StoredPath);
                        try {
                            outputStream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                            Toast.makeText(getApplicationContext(), "Successfully Saved", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //save();
                    finish();
                }
            }
        }
    };


    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getApplicationContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else {
            return true;
        }
    }

    public class signature extends View {
        private static final float STROKE_WIDTH = 5f;
        private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
        private Paint paint = new Paint();
        private Path path = new Path();
        private float lastTouchX;
        private float lastTouchY;
        private final RectF dirtyRect = new RectF();

        public signature(Context context, AttributeSet attrs) {
            super(context, attrs);
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeWidth(STROKE_WIDTH);
        }

        public void clear() {
            path.reset();
            invalidate();
            mbtnSalvar.setEnabled(false);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawPath(path, paint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float eventX = event.getX();
            float eventY = event.getY();
            mbtnSalvar.setEnabled(true);
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    path.moveTo(eventX, eventY);
                    lastTouchX = eventX;
                    lastTouchY = eventY;
                    return true;
                case MotionEvent.ACTION_MOVE:
                case MotionEvent.ACTION_UP:
                    resetDirtyRect(eventX, eventY);
                    int historySize = event.getHistorySize();
                    for (int i = 0; i < historySize; i++) {
                        float historicalX = event.getHistoricalX(i);
                        float historicalY = event.getHistoricalY(i);
                        expandDirtyRect(historicalX, historicalY);
                        path.lineTo(historicalX, historicalY);
                    }
                    path.lineTo(eventX, eventY);
                    break;
                default:
                    debug("Ignored touch event: " + event.toString());
                    return false;
            }
            invalidate((int) (dirtyRect.left - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.top - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.right + HALF_STROKE_WIDTH),
                    (int) (dirtyRect.bottom + HALF_STROKE_WIDTH));

            lastTouchX = eventX;
            lastTouchY = eventY;
            return true;
        }

        private void debug(String string) {
            Log.v("log_tag", string);
        }

        private void expandDirtyRect(float historicalX, float historicalY) {
            if (historicalX < dirtyRect.left) {
                dirtyRect.left = historicalX;
            } else if (historicalX > dirtyRect.right) {
                dirtyRect.right = historicalX;
            }
            if (historicalY < dirtyRect.top) {
                dirtyRect.top = historicalY;
            } else if (historicalY > dirtyRect.bottom) {
                dirtyRect.bottom = historicalY;
            }
        }

        private void resetDirtyRect(float eventX, float eventY) {
            dirtyRect.left = Math.min(lastTouchX, eventX);
            dirtyRect.right = Math.max(lastTouchX, eventX);
            dirtyRect.top = Math.min(lastTouchY, eventY);
            dirtyRect.bottom = Math.max(lastTouchY, eventY);
        }
    }

    public ArrayList<AssinaturaDados> load(ArrayList<AssinaturaDados> dados) {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(DIRECTORY + "dao.txt");
        } catch (FileNotFoundException e) {
            try {
                new FileOutputStream(new File(DIRECTORY + "dao.txt"));
                fileInputStream = new FileInputStream(DIRECTORY + "dao.txt");
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String linha;
            String[] strings;
            while ((linha = bufferedReader.readLine()) != null) {
                strings = linha.split(";");
                AssinaturaDados dado = new AssinaturaDados(strings[0], strings[1], strings[2], strings[3]);
                dados.add(dado);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return dados;
    }

    public void save() {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(new File(DIRECTORY, "dao.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            String string = "";
            for (AssinaturaDados dado : dados) {
                string += format("%s;%s;%s;%s\n", dado.getAtendente(), dado.getOutro(), dado.getDescricao(), dado.getAssinaturadata());
            }
            if (fileOutputStream != null) {
                fileOutputStream.write(string.getBytes());
                Toast.makeText(getApplicationContext(), "Saved to " + DIRECTORY + "dao.txt", Toast.LENGTH_SHORT).show();
            } else Toast.makeText(getApplicationContext(), "Deu ruim", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveFirebase() {
        mDatabaseReference = mFirebaseDatabase.getReference("assinaturas");
        DatabaseReference assinaturasRef = mDatabaseReference.child(dado.getAssinaturadata());
        assinaturasRef.setValue(dado);
    }
}