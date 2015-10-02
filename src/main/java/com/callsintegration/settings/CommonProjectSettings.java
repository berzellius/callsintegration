package com.callsintegration.settings;

import java.util.*;

/**
 * Created by berz on 23.11.14.
 */
public abstract class CommonProjectSettings implements ProjectSettings {

    @Override
    public final List<String> getAllowedFileMimeTypes(){
        List<String> types = new LinkedList<String>();
        types.add("image/png");
        types.add("image/jpeg");
        types.add("image/gif");
        types.add("image/tiff");

        return types;
    }


}
