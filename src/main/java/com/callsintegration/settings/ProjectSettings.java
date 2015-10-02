package com.callsintegration.settings;

import java.util.HashMap;
import java.util.List;

/**
 * Created by berz on 22.11.14.
 */
public interface ProjectSettings {

    public String getPathToUploads();

    public HashMap<String, String> getDatabaseConnectionConfig();

    public List<String> getAllowedFileMimeTypes();

    public String getSiteUrl();

    public String fontsDirectoryLocation();

}