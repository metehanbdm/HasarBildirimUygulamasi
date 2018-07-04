package com.example.rsyazilim.rs_ihbar;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.rsyazilim.rs_ihbar.model.DamageRecordInfo;
import com.example.rsyazilim.rs_ihbar.utils.CommonUtils;
import com.example.rsyazilim.rs_ihbar.utils.DatabaseUtils;
import com.example.rsyazilim.rs_ihbar.utils.GeneralUtils;
import com.example.rsyazilim.rs_ihbar.utils.MailHelper;
import com.example.rsyazilim.rs_ihbar.utils.MyGlideEngine;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PHOTO_ACTIVITY_REQUESTCODE = 100;
    private static final int MAP_ACTIVITY_REQUESTCODE = 101;
    private static final int INFORMATION_ACTIVITY_REQUESTCODE = 102;
    private DamageRecordInfo newRecord;
    private View baseLayout;
    AppDatabase database;
    public static final int VISIBLE = 0x00000000;
    public static final int INVISIBLE = 0x00000004;

    // TODO BILGILENDIRME hasar kaydina ait tum datalari kayit eden sinif.
    // Lokasyon ,
    // fotograf linkleri ,
    // aciklamlar ,
    // telefon numaralari burada tutuluyor
    //Ve database'e kayit edilen sinif bu

    //TODO GENEL ISLEYIS
    //Konum Sec ,Foto Sec ,Aciklama Gir dedikten sonra girilen veriler onActivityResultta yakalanip newRecord icerisine atildi.Dbye kaydetme islemi farkli activitylerde degil hepsinin toplandigi mainactivtyde yapilıyor.
    //newRecord icerisindeki booleanlar (locationSetted , imageSetted , informationSetted) konumSecildiyse FotoSecildiyse AciklamaGirildiyse anlaminda  true yapiliyo.bunlar true yapilirken sag taraflara eklenen tik isaretleri de yesıl yapılıyor
    //MainActivity onclick icinde cardview_send_mail icinde tum alanlar yesil tikliyse, mailgonder dbye kaydet islemleri yapilıyor
    //FotoActıvıtyde uygulama cokuyodu.Runtime persmissions icin gradle'a dexter dıye bir kütüphane eklendi.Bu kütüphane izinleri kontrol ediyor foto seçiminde

    //TODO YAPILACAKLAR
    //Todo: girilen textlerin sadece null kontrolu yapildi.bos string olma durumlari ,telefon numarasinin dogrulugu gibi kontroller koyulabilir
    //lokasyon için izin istemek için dextera geçirilebilir.kodu düzgün hale getirmek için ve eski cihazlarla uyumluu hale getirmek için.acil değil

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        baseLayout = findViewById(R.id.main_base_layout);

        newRecord = new DamageRecordInfo();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        super.onActivityResult(requestCode, resultCode, data);

        ImageView checkbox;
        if (resultCode == RESULT_OK) {
            switch (requestCode)
            {
                case MAP_ACTIVITY_REQUESTCODE:
                    Double lat = data.getDoubleExtra("LAT", 0);
                    Double lng = data.getDoubleExtra("LNG", 0);

                    checkbox = findViewById(R.id.cardview_map_checkbox);
                    if (lat==0 && lng ==0)
                    {
                        GeneralUtils.ShowSnackbar(baseLayout , "Lokasyon Bilgileri Alınamadı.Tekrar Deneyiniz.");
                        newRecord.setLocationChecked(false);
                        checkbox.setColorFilter(getResources().getColor(R.color.md_white_1000));
                    }
                    else{
                        newRecord.setLat(lat);
                        newRecord.setLng(lng);
                        newRecord.setLocationChecked(true);
                        checkbox.setVisibility(View.VISIBLE);
                        checkbox.setColorFilter(getResources().getColor(R.color.md_light_green_600));
                    }
                    break;
                case PHOTO_ACTIVITY_REQUESTCODE:
                    List<Uri> selectedPhoto = Matisse.obtainResult(data);

                    checkbox = findViewById(R.id.cardview_photo_checkbox);
                    if (selectedPhoto ==null && selectedPhoto.isEmpty())
                    {
                        GeneralUtils.ShowSnackbar(baseLayout , "Fotoğraflar Alınamadı.Tekrar Deneyiniz.");
                        newRecord.setImageChecked(false);
                        checkbox.setColorFilter(getResources().getColor(R.color.md_white_1000));
                    }
                    else
                        {
                            if (selectedPhoto.size() <4){
                                GeneralUtils.ShowSnackbar(baseLayout , "Lütfen 4 Adet Fotoğraf Seçiniz");}
                                else {
                                newRecord.setImage1(selectedPhoto.get(0) == null ? "": selectedPhoto.get(0).toString());
                                newRecord.setImage2(selectedPhoto.get(1) == null ? "": selectedPhoto.get(1).toString());
                                newRecord.setImage3(selectedPhoto.get(2) == null ? "": selectedPhoto.get(2).toString());
                                newRecord.setImage4(selectedPhoto.get(3) == null ? "": selectedPhoto.get(3).toString());
                                newRecord.setImageChecked(true);
                                checkbox.setVisibility(View.VISIBLE);
                                checkbox.setColorFilter(getResources().getColor(R.color.md_light_green_600)); }
                        }
                    break;
                case INFORMATION_ACTIVITY_REQUESTCODE:
                    String info  = data.getStringExtra("INFO");
                    String phone = data.getStringExtra("PHONE");

                    checkbox = findViewById(R.id.cardview_information_checkbox);
                    if (info.equals("") || phone.equals(""))
                    {
                        GeneralUtils.ShowSnackbar(baseLayout , "Bilgiler Alınamadı.Tekrar Deneyiniz.");
                        newRecord.setInformationChecked(false);
                        checkbox.setColorFilter(getResources().getColor(R.color.md_white_1000));
                    }
                    else
                        {
                            newRecord.setInformation(info == null ? "" : info);
                            newRecord.setPhone(phone == null ?"" : phone);
                            newRecord.setInformationChecked(true);
                            checkbox.setVisibility(View.VISIBLE);
                            checkbox.setColorFilter(getResources().getColor(R.color.md_light_green_600));
                        }

                break;
            }
        }
    }

        @Override
        public void onClick (View v){
            switch (v.getId()) {
                case R.id.cardview_photo:
                    requestCameraPermission();

                    break;
                case R.id.cardview_map:
                    startActivityForResult(new Intent(MainActivity.this, MapActivity.class), MAP_ACTIVITY_REQUESTCODE);

                    break;
                case R.id.cardview_information:
                    startActivityForResult(new Intent(MainActivity.this, InformationActivity.class), INFORMATION_ACTIVITY_REQUESTCODE);

                    break;
                case R.id.cardview_history:
                    startActivity(new Intent(MainActivity.this, HistoryActivity.class));


                    break;
                case R.id.cardview_send_mail:

                    //Tum hepsi check edildiyse gonderilsin.
                    if (newRecord.isImageChecked() && newRecord.isLocationChecked() && newRecord.isInformationChecked())
                    {
                        GeneralUtils.ShowSnackbar(baseLayout , "HASAR KAYIDINIZ İŞLENİYOR LÜTFEN BEKLEYİNİZ");
                        DatabaseUtils.Add(this , newRecord);

                        MailHelper mailHelper = new MailHelper(this, newRecord);
                        mailHelper.execute();
                    }else
                        GeneralUtils.ShowSnackbar(baseLayout , "Bilgiler Eksik Kontrol Edin");

                    break;
            }
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

                            Matisse.from(MainActivity.this)
                                    .choose(MimeType.allOf())
                                    .theme(R.style.Matisse_Dracula)
                                    .countable(true)
                                    .maxSelectable(4)
                                    .gridExpectedSize(500)
                                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                                    .thumbnailScale(0.85f)
                                    .imageEngine(new MyGlideEngine())
                                    .forResult(PHOTO_ACTIVITY_REQUESTCODE);
                            Toast.makeText(getApplicationContext(), "All permissions are granted!", Toast.LENGTH_SHORT).show();
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            CommonUtils.showPermissionSettingsDialog(MainActivity.this );
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

}