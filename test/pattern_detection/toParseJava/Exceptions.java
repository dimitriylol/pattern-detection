package com.patterns;

import java.io.*;

public class Exceptions {

    public void a() throws IOException, NumberFormatException {
        try(InputStream a = new FileInputStream(File.createTempFile("woah",""));
            InputStreamReader buf = new InputStreamReader(a);
            BufferedReader reader = new BufferedReader(buf)){
            String line = reader.readLine();
            int anInt = Integer.parseInt(line);
        }catch (IOException | NumberFormatException e){
            //log
            //...
           throw e;
        }
    }
}
