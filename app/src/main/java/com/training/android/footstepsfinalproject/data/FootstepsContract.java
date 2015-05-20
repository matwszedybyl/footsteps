package com.training.android.footstepsfinalproject.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by mwszedybyl on 5/3/15.
 */
public class FootstepsContract
{


    /* Inner class that defines the table contents of the location table */
    public static final class WalkEntry implements BaseColumns
    {

        // The "Content authority" is a name for the entire content provider, similar to the
        // relationship between a domain name and its website.  A convenient string to use for the
        // content authority is the package name for the app, which is guaranteed to be unique on the
        // device.
        public static final String CONTENT_AUTHORITY = "com.training.android.footstepsfinalproject";

        // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
        // the content provider.
        public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

        // Possible paths (appended to base content URI for possible URI's)
        // For instance, content://com.example.android.sunshine.app/weather/ is a valid path for
        // looking at weather data. content://com.example.android.sunshine.app/givemeroot/ will fail,
        // as the ContentProvider hasn't been given any information on what to do with "givemeroot".
        // At least, let's hope not.  Don't be that dev, reader.  Don't be that dev.
        public static final String PATH_WALKS = "walk";


        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_WALKS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_WALKS;

        // Table name
        public static final String TABLE_NAME = "walk";
        public static final String COLUMN_STARTING_LAT = "starting_lat";
        public static final String COLUMN_STARTING_LONG = "starting_long";
        public static final String COLUMN_ENDING_LAT = "ending_lat";
        public static final String COLUMN_ENDING_LONG = "ending_long";
        public static final String COLUMN_DISTANCE = "distance";
        public static final String COLUMN_TEMP = "temperature";

        public static Uri buildWalkUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }
}
