package com.example.rsyazilim.rs_ihbar;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.rsyazilim.rs_ihbar.utils.CommonUtils;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;


public class PhotoActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int IMAGE_PICK = 1;
    private static final int REQUEST_PIC = 100;



    private ImageView imageView,imageView2,imageView3,imageView4;
    private Button send_button;
    private ImageView X;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        this.imageView  = this.findViewById(R.id.photo_imageView1);
        this.imageView2 = this.findViewById(R.id.photo_imageView2);
        this.imageView3 = this.findViewById(R.id.photo_imageView3);
        this.imageView4 = this.findViewById(R.id.photo_imageView4);
        this.send_button = this.findViewById(R.id.photo_btn);

        this.imageView.setOnClickListener(this);
        this.imageView2.setOnClickListener(this);
        this.imageView3.setOnClickListener(this);
        this.imageView4.setOnClickListener(this);
        this.send_button.setOnClickListener(this);

        // Program açılışında ,ilk 1. imageview e tıklansın
        //      sonra 2, sonra 3, sonra 4 olsun diye yapıldı aşağıdaki kısım
        imageView2.setEnabled(false);
        imageView3.setEnabled(false);
        imageView4.setEnabled(false);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.photo_imageView1:
                X = imageView;
                imageView2.setEnabled(true);
                requestCameraPermission();
                //X = imageView.findViewById(R.id.home_imageView1);
                break;

            case R.id.photo_imageView2:
                X = imageView2;
                imageView3.setEnabled(true);
                requestCameraPermission();
                break;

            case R.id.photo_imageView3:
                X = imageView3;
                imageView4.setEnabled(true);
                requestCameraPermission();
                break;

            case R.id.photo_imageView4:
                X = imageView4;
                requestCameraPermission();
                break;

            case R.id.photo_btn:
                SendToInformation();
                break;

            default:
                break;
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode ==REQUEST_PIC)
            {
                if (data.getData() ==null)
                  this.imageFromCamera(resultCode ,data);
                else
                    this.imageFromGallery(resultCode , data);
            }

        }
    }

    private void imageFromCamera(int resultCode, Intent data) {

            Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
            X.setImageBitmap(imageBitmap);
            Uri tempUri = getImageUri(getApplicationContext(),imageBitmap);
            File finalFile = new File(getRealPathFromURI(tempUri));
            this.X.setTag( finalFile.getAbsolutePath());


    }

    private void imageFromGallery(int resultCode, Intent data) {

        Uri selectedImage = data.getData();
        String [] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String filePath = cursor.getString(columnIndex);
        this.X.setImageBitmap(BitmapFactory.decodeFile(filePath));
        this.X.setTag( filePath);
        // 'X' yukarıdaki switch metodundan hangi imageView olduğunu ayırmak için.
        cursor.close();
    }

    public void SendToInformation() // Gönder butonu için..
    {
        Intent intent = getIntent();
        intent.putExtra("IMAGE1", (String)imageView.getTag());
        intent.putExtra("IMAGE2", (String)imageView2.getTag());
        intent.putExtra("IMAGE3", (String)imageView3.getTag());
        intent.putExtra("IMAGE4", (String)imageView4.getTag());

        setResult(RESULT_OK, intent);
        finish();

    }


    private void requestCameraPermission() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            //openCamera();
                            openTakeCamera();
                            Toast.makeText(getApplicationContext(), "All permissions are granted!", Toast.LENGTH_SHORT).show();
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            CommonUtils.showPermissionSettingsDialog(PhotoActivity.this );
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }


    private void openTakeCamera(){

        Intent galleryintent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryintent.setType("image/*");

        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        Intent chooser = new Intent(Intent.ACTION_CHOOSER);
        chooser.putExtra(Intent.EXTRA_INTENT, galleryintent);
        chooser.putExtra(Intent.EXTRA_TITLE, "Select from:");

        Intent[] intentArray = { cameraIntent };
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
        startActivityForResult(chooser, REQUEST_PIC);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {

        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }
}
