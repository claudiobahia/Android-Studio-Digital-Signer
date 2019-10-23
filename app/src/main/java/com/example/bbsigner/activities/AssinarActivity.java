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
import android.widget.Toast;

import com.example.bbsigner.R;
import com.example.bbsigner.classes.AssinaturaDados;
import com.example.bbsigner.classes.Screenshot;

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
    //private Bitmap bitmap;

    private String DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/UserSignature/";
    private String dataAssinatura = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
    private String StoredPath = DIRECTORY + dataAssinatura + ".jpg";


    // Get datas from NovaAssinatura act

    private String nome = getIntent().getStringExtra("atendente");
    private String outro = getIntent().getStringExtra("outro");
    private String descricao = getIntent().getStringExtra("descricao");
    private ArrayList<AssinaturaDados> dados = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assinar);

        dados = load(dados);

        mContent = findViewById(R.id.canvasLayout);
        mSignature = new signature(getApplicationContext(), null);
        mSignature.setBackgroundColor(Color.WHITE);
        // Dynamically generating Layout through java code
        mContent.addView(mSignature, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mbtnLimpar = findViewById(R.id.clear);
        mbtnSalvar = findViewById(R.id.getsign);
        mbtnSalvar.setEnabled(false);
        view = mContent;
        mbtnSalvar.setOnClickListener(onButtonClick);
        mbtnLimpar.setOnClickListener(onButtonClick);

        // Method to create Directory, if the Directory doesn't exists
        file = new File(DIRECTORY);
        if (!file.exists()) {
            file.mkdir();
        }
    }

//    private void salvarBanco(String nome, String outro, String descricao, String dataAssinatura) {
//        AssinaturaDadosDAO dao = new AssinaturaDadosDAO();
//        ArrayList<AssinaturaDados> dados = new ArrayList<>();
//        dados = dao.readFileCliente(dados);
//        dados.add(new AssinaturaDados(nome, outro, descricao, dataAssinatura));
//    }

    Button.OnClickListener onButtonClick = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == mbtnLimpar) {
                Log.v("log_tag", "Panel Cleared");
                mSignature.clear();
                mbtnSalvar.setEnabled(false);
            } else if (v == mbtnSalvar) {
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

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
            finish();
        } else {
            Toast.makeText(this, "The app was not allowed to write to your storage. Hence," +
                    " it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
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

//        public void save(View v, String StoredPath) {
//            Log.v("log_tag", "Width: " + v.getWidth());
//            Log.v("log_tag", "Height: " + v.getHeight());
//            OutputStream outputStream;
//
//            try {
//                Bitmap bitmap = Screenshot.takescreenshotOfRootView(v);
//                // Output the file
//
//                File file = new File(DIRECTORY,System.currentTimeMillis() + ".jpg");
//
//                try {
//                    outputStream = new FileOutputStream(file);
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
//
//            } catch (Exception e) {
//                Log.v("log_tag", e.toString());
//            }
//        }

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
            fileInputStream = openFileInput("dao.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String linha;
            String[] strings;

            while ((linha = bufferedReader.readLine())  != null){
                strings = linha.split(";");
                AssinaturaDados dado = new AssinaturaDados(strings[0],strings[1], strings[2],strings[3]);
                dados.add(dado);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
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
            fileOutputStream = openFileOutput("dao.txt", MODE_PRIVATE);
            String string = "";
            for (AssinaturaDados dado : dados) {
                string += format("%s;%s;%s;%s\n", dado.getAtendente(), dado.getOutro(), dado.getDescricao(), dado.getAssinaturadir());
            }
            fileOutputStream.write(string.getBytes());
            Toast.makeText(getApplicationContext(), "Saved to " + getFilesDir() + "/dao.txt", Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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

    @Override
    protected void onDestroy() {
        save();
        super.onDestroy();
    }
}