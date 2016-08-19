package com.kosign.bizaddress.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by jung on 2016-02-22.
 */
public class HttpUtil {


    public String send(String strUrl , String strParam , String strMethod , String strOutPutStreamCharSet , String strInPutStreamCharSet , String readTime, Map<String, String> propertyMap)
    {
        PrintWriter postReq = null;
        BufferedReader postRes = null;
        String resultJson = null;
        StringBuilder resultBuffer = new StringBuilder();

        URL connectUrl = null;
        HttpURLConnection con = null;

        try {
            connectUrl = new URL(strUrl);

            //https 통신
            if(strUrl.indexOf("https")==0) {
                con = (HttpsURLConnection) connectUrl.openConnection();
                //System.out.println("https 통신입니다.");
            }
            else {
                con = (HttpURLConnection) connectUrl.openConnection();
                //System.out.println("http 통신입니다.");
            }

            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.setConnectTimeout(5000); //TIMEOUT 5초 설정
            //BizplayGate Input항목 정의
            if(readTime != null){
                con.setReadTimeout(Integer.parseInt(readTime));
            }
            if(propertyMap != null){
                for(String key : propertyMap.keySet()){
                    con.setRequestProperty(key, propertyMap.get(key));
                }
            }
            // JSONArray 전송
            if(strInPutStreamCharSet.length()>0) {
                postReq = new PrintWriter(new OutputStreamWriter(con.getOutputStream(), strInPutStreamCharSet));
            }
            else{
                postReq = new PrintWriter(new OutputStreamWriter(con.getOutputStream()));
            }
            postReq.write(strParam);
            postReq.flush();


            // JSONObject 수신
            if(strOutPutStreamCharSet.length()>0) {
                postRes = new BufferedReader(new InputStreamReader(con.getInputStream(), strOutPutStreamCharSet));
            }else{
                postRes = new BufferedReader(new InputStreamReader(con.getInputStream()));
            }
            while ((resultJson = postRes.readLine()) != null){
                resultBuffer.append(resultJson);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally{
            if(postReq!=null) try{ postReq.close();}catch(Exception ee){}
            if(postRes!=null) try{ postRes.close();}catch(Exception ee){}
            if(con!=null) try{ con.disconnect();}catch(Exception ee){}
        }

        return resultBuffer.toString();
    }

}
