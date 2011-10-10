package org.xent.mctalk.audio;

import java.io.ByteArrayOutputStream;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 *
 * @author Chance Snow
 */
public class Compressor {

    public static byte[] compressBytes(byte[] input) {
        Deflater compress = new Deflater();
        compress.setLevel(Deflater.BEST_SPEED);
        compress.setInput(input);
        compress.finish();
        ByteArrayOutputStream bos = new ByteArrayOutputStream(input.length);
        byte[] buf = new byte[1024];
        while (!compress.finished()) {
            int count = compress.deflate(buf);
            bos.write(buf, 0, count);
        }
        try {
            bos.close();
        } catch (Exception ex) {
            return null;
        }
        return bos.toByteArray();
    }

    public static byte[] decompressBytes(byte[] input) {
        Inflater decompress = new Inflater();
        decompress.setInput(input);
        ByteArrayOutputStream bos = new ByteArrayOutputStream(input.length);
        byte[] buf = new byte[1024];
        while (!decompress.finished()) {
            try {
                int count = decompress.inflate(buf);
                bos.write(buf, 0, count);
            } catch (Exception e) {
                return null;
            }
        }
        try {
            bos.close();
        } catch (Exception e) {
            return null;
        }
        return bos.toByteArray();
    }
}