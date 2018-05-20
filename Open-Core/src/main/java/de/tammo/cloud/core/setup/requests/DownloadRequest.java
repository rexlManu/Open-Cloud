/*
 * Copyright (c) 2018. File created by Tammo
 */

package de.tammo.cloud.core.setup.requests;

import de.tammo.cloud.core.exceptions.FileDownloadException;
import de.tammo.cloud.core.logging.Logger;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class DownloadRequest {

    public void request(final Logger logger, final String url, final String path, final Runnable complete) throws IOException{
        final HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            Files.copy(connection.getInputStream(), new File(path).toPath(), StandardCopyOption.REPLACE_EXISTING);
            complete.run();
        } else {
            logger.error("Cant download file from requested url!", new FileDownloadException("Cant download file from requested url!"));
        }
    }

}
