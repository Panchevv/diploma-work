package com.diploma.panchev.nrfcloud.utils;

import lombok.NonNull;
import org.apache.tomcat.util.codec.binary.Base64;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;

public class Utils {
    public static byte[] convertPemToByte(@NonNull String value, @NonNull String type) {
        return Base64.decodeBase64(
                value
                        .replace("-----BEGIN "+ type + "-----", "")
                        .replace("-----END "+ type + "-----", "")
                        .replaceAll("\n", "")
        );
    }

    public static OffsetDateTime mapSecond(long seconds) {
        return OffsetDateTime.ofInstant(
                Instant.ofEpochMilli(seconds),
                ZoneId.systemDefault()
        );
    }
}
