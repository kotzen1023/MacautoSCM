package com.macauto.macautoscm.Service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.macauto.macautoscm.Data.Constants;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


public class LoginCheckService extends IntentService {
    public static final String TAG = "LoginCheckService";

    public static final String USER_NO = "user_no";

    private static final String NAMESPACE = "http://tempuri.org/"; // 命名空間

    private static final String METHOD_NAME = "check_emp_exist"; // 方法名稱

    private static final String SOAP_ACTION1 = "http://tempuri.org/check_emp_exist"; // SOAP_ACTION

    private static final String URL = "http://60.249.239.47/service.asmx"; // 網址

    public LoginCheckService() {
        super("LoginCheckService");
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "Handle");

        String user_no = intent.getStringExtra(USER_NO);

        /*if (intent.getAction().equals(Constants.ACTION.CHECK_EMPLOYEE_EXIST_ACTION)) {
            Log.i(TAG, "CHECK_EMPLOYEE_EXIST_ACTION");
        }*/

        try {
            // 建立一個 WebService 請求

            SoapObject request = new SoapObject(NAMESPACE,
                    METHOD_NAME);

            // 輸出值，帳號(account)、密碼(password)


            //request.addProperty("start_date", "");
            //request.addProperty("end_date", "");
            //request.addProperty("emp_no", "1050636");
            request.addProperty("user_no", user_no);
            //request.addProperty("emp_name", "方炳強");
            //request.addProperty("meeting_room_name", "");
            //request.addProperty("subject_or_content", "");
            //request.addProperty("meeting_type_id", "");
            //request.addProperty("passWord", "sunnyhitest");

            // 擴充 SOAP 序列化功能為第11版

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true; // 設定為 .net 預設編碼

            envelope.setOutputSoapObject(request); // 設定輸出的 SOAP 物件


            // 建立一個 HTTP 傳輸層

            HttpTransportSE httpTransport = new HttpTransportSE(URL);
            httpTransport.debug = true; // 測試模式使用

            httpTransport.call(SOAP_ACTION1, envelope); // 設定 SoapAction 所需的標題欄位


            // 將 WebService 資訊轉為 DataTable
            if (envelope.bodyIn instanceof SoapFault) {
                String str= ((SoapFault) envelope.bodyIn).faultstring;
                Log.e(TAG, str);
            } else {
                SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
                Log.d(TAG, String.valueOf(resultsRequestSOAP));

                if (String.valueOf(resultsRequestSOAP).indexOf("true") > 0) {
                    Log.e(TAG, "ret = true");
                    Intent decryptDoneIntent = new Intent(Constants.ACTION.CHECK_MANUFACTURER_EXIST_COMPLETE);
                    sendBroadcast(decryptDoneIntent);
                } else {
                    Log.e(TAG, "ret = false");
                    Intent decryptDoneIntent = new Intent(Constants.ACTION.CHECK_MANUFACTURER_EXIST_FAIL);
                    sendBroadcast(decryptDoneIntent);
                }
                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    InputStream stream = new ByteArrayInputStream(String.valueOf(resultsRequestSOAP).getBytes(StandardCharsets.UTF_8));
                    LoadAndParseXML(stream);
                } else {
                    InputStream stream = new ByteArrayInputStream(String.valueOf(resultsRequestSOAP).getBytes(Charset.forName("UTF-8")));
                    LoadAndParseXML(stream);
                }*/

            }


        } catch (Exception e) {
            // 抓到錯誤訊息

            e.printStackTrace();
            Intent decryptDoneIntent = new Intent(Constants.ACTION.SOAP_CONNECTION_FAIL);
            sendBroadcast(decryptDoneIntent);
        }



    }
}
