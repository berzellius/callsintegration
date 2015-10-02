package com.callsintegration.settings;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by berz on 22.11.14.
 */
public class LocalProjectSettings extends CommonProjectSettings {
    @Override
    public String getPathToUploads() {
        return "C:\\projects\\uploads";
    }

    @Override
    public HashMap<String, String> getDatabaseConnectionConfig() {
        HashMap<String, String> dbConnect = new LinkedHashMap<String, String>();
        dbConnect.put("path","jdbc:postgresql://localhost:5432/calls");
        dbConnect.put("database", "postgres");
        dbConnect.put("password", "postgres");

        return dbConnect;
    }

    @Override
    public String getSiteUrl() {
        return "http://localhost:8080/";
    }

    @Override
    public String fontsDirectoryLocation() {
        return "c:/windows/fonts/";
    }
}
