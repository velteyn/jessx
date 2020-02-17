// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package com.keypoint;

import java.io.IOException;
import java.awt.image.PixelGrabber;
import java.io.OutputStream;
import java.util.zip.DeflaterOutputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.Deflater;
import java.awt.image.ImageObserver;
import java.util.zip.CRC32;
import java.awt.Image;

public class PngEncoder
{
    public static final boolean ENCODE_ALPHA = true;
    public static final boolean NO_ALPHA = false;
    public static final int FILTER_NONE = 0;
    public static final int FILTER_SUB = 1;
    public static final int FILTER_UP = 2;
    public static final int FILTER_LAST = 2;
    private static final byte[] IHDR;
    private static final byte[] IDAT;
    private static final byte[] IEND;
    private byte[] pngBytes;
    private byte[] priorRow;
    private byte[] leftBytes;
    private Image image;
    private int width;
    private int height;
    private int bytePos;
    private int maxPos;
    private CRC32 crc;
    private long crcValue;
    private boolean encodeAlpha;
    private int filter;
    private int bytesPerPixel;
    private int compressionLevel;
    
    public PngEncoder() {
        this(null, false, 0, 0);
    }
    
    public PngEncoder(final Image image) {
        this(image, false, 0, 0);
    }
    
    public PngEncoder(final Image image, final boolean encodeAlpha) {
        this(image, encodeAlpha, 0, 0);
    }
    
    public PngEncoder(final Image image, final boolean encodeAlpha, final int whichFilter) {
        this(image, encodeAlpha, whichFilter, 0);
    }
    
    public PngEncoder(final Image image, final boolean encodeAlpha, final int whichFilter, final int compLevel) {
        this.crc = new CRC32();
        this.image = image;
        this.encodeAlpha = encodeAlpha;
        this.setFilter(whichFilter);
        if (compLevel >= 0 && compLevel <= 9) {
            this.compressionLevel = compLevel;
        }
    }
    
    public void setImage(final Image image) {
        this.image = image;
        this.pngBytes = null;
    }
    
    public byte[] pngEncode(final boolean encodeAlpha) {
        final byte[] pngIdBytes = { -119, 80, 78, 71, 13, 10, 26, 10 };
        if (this.image == null) {
            return null;
        }
        this.width = this.image.getWidth(null);
        this.height = this.image.getHeight(null);
        this.pngBytes = new byte[(this.width + 1) * this.height * 3 + 200];
        this.maxPos = 0;
        this.bytePos = this.writeBytes(pngIdBytes, 0);
        this.writeHeader();
        if (this.writeImageData()) {
            this.writeEnd();
            this.pngBytes = this.resizeByteArray(this.pngBytes, this.maxPos);
        }
        else {
            this.pngBytes = null;
        }
        return this.pngBytes;
    }
    
    public byte[] pngEncode() {
        return this.pngEncode(this.encodeAlpha);
    }
    
    public void setEncodeAlpha(final boolean encodeAlpha) {
        this.encodeAlpha = encodeAlpha;
    }
    
    public boolean getEncodeAlpha() {
        return this.encodeAlpha;
    }
    
    public void setFilter(final int whichFilter) {
        this.filter = 0;
        if (whichFilter <= 2) {
            this.filter = whichFilter;
        }
    }
    
    public int getFilter() {
        return this.filter;
    }
    
    public void setCompressionLevel(final int level) {
        if (level >= 0 && level <= 9) {
            this.compressionLevel = level;
        }
    }
    
    public int getCompressionLevel() {
        return this.compressionLevel;
    }
    
    protected byte[] resizeByteArray(final byte[] array, final int newLength) {
        final byte[] newArray = new byte[newLength];
        final int oldLength = array.length;
        System.arraycopy(array, 0, newArray, 0, Math.min(oldLength, newLength));
        return newArray;
    }
    
    protected int writeBytes(final byte[] data, final int offset) {
        this.maxPos = Math.max(this.maxPos, offset + data.length);
        if (data.length + offset > this.pngBytes.length) {
            this.pngBytes = this.resizeByteArray(this.pngBytes, this.pngBytes.length + Math.max(1000, data.length));
        }
        System.arraycopy(data, 0, this.pngBytes, offset, data.length);
        return offset + data.length;
    }
    
    protected int writeBytes(final byte[] data, final int nBytes, final int offset) {
        this.maxPos = Math.max(this.maxPos, offset + nBytes);
        if (nBytes + offset > this.pngBytes.length) {
            this.pngBytes = this.resizeByteArray(this.pngBytes, this.pngBytes.length + Math.max(1000, nBytes));
        }
        System.arraycopy(data, 0, this.pngBytes, offset, nBytes);
        return offset + nBytes;
    }
    
    protected int writeInt2(final int n, final int offset) {
        final byte[] temp = { (byte)(n >> 8 & 0xFF), (byte)(n & 0xFF) };
        return this.writeBytes(temp, offset);
    }
    
    protected int writeInt4(final int n, final int offset) {
        final byte[] temp = { (byte)(n >> 24 & 0xFF), (byte)(n >> 16 & 0xFF), (byte)(n >> 8 & 0xFF), (byte)(n & 0xFF) };
        return this.writeBytes(temp, offset);
    }
    
    protected int writeByte(final int b, final int offset) {
        final byte[] temp = { (byte)b };
        return this.writeBytes(temp, offset);
    }
    
    protected void writeHeader() {
        final int writeInt4 = this.writeInt4(13, this.bytePos);
        this.bytePos = writeInt4;
        final int startPos = writeInt4;
        this.bytePos = this.writeBytes(PngEncoder.IHDR, this.bytePos);
        this.width = this.image.getWidth(null);
        this.height = this.image.getHeight(null);
        this.bytePos = this.writeInt4(this.width, this.bytePos);
        this.bytePos = this.writeInt4(this.height, this.bytePos);
        this.bytePos = this.writeByte(8, this.bytePos);
        this.bytePos = this.writeByte(this.encodeAlpha ? 6 : 2, this.bytePos);
        this.bytePos = this.writeByte(0, this.bytePos);
        this.bytePos = this.writeByte(0, this.bytePos);
        this.bytePos = this.writeByte(0, this.bytePos);
        this.crc.reset();
        this.crc.update(this.pngBytes, startPos, this.bytePos - startPos);
        this.crcValue = this.crc.getValue();
        this.bytePos = this.writeInt4((int)this.crcValue, this.bytePos);
    }
    
    protected void filterSub(final byte[] pixels, final int startPos, final int width) {
        final int offset = this.bytesPerPixel;
        final int actualStart = startPos + offset;
        final int nBytes = width * this.bytesPerPixel;
        int leftInsert = offset;
        int leftExtract = 0;
        for (int i = actualStart; i < startPos + nBytes; ++i) {
            this.leftBytes[leftInsert] = pixels[i];
            pixels[i] = (byte)((pixels[i] - this.leftBytes[leftExtract]) % 256);
            leftInsert = (leftInsert + 1) % 15;
            leftExtract = (leftExtract + 1) % 15;
        }
    }
    
    protected void filterUp(final byte[] pixels, final int startPos, final int width) {
        for (int nBytes = width * this.bytesPerPixel, i = 0; i < nBytes; ++i) {
            final byte currentByte = pixels[startPos + i];
            pixels[startPos + i] = (byte)((pixels[startPos + i] - this.priorRow[i]) % 256);
            this.priorRow[i] = currentByte;
        }
    }
    
    protected boolean writeImageData() {
        int rowsLeft = this.height;
        int startRow = 0;
        this.bytesPerPixel = (this.encodeAlpha ? 4 : 3);
        final Deflater scrunch = new Deflater(this.compressionLevel);
        final ByteArrayOutputStream outBytes = new ByteArrayOutputStream(1024);
        final DeflaterOutputStream compBytes = new DeflaterOutputStream(outBytes, scrunch);
        try {
            while (rowsLeft > 0) {
                final int nRows = Math.min(32767 / (this.width * (this.bytesPerPixel + 1)), rowsLeft);
                final int[] pixels = new int[this.width * nRows];
                final PixelGrabber pg = new PixelGrabber(this.image, 0, startRow, this.width, nRows, pixels, 0, this.width);
                try {
                    pg.grabPixels();
                }
                catch (Exception e2) {
                    System.err.println("interrupted waiting for pixels!");
                    return false;
                }
                if ((pg.getStatus() & 0x80) != 0x0) {
                    System.err.println("image fetch aborted or errored");
                    return false;
                }
                final byte[] scanLines = new byte[this.width * nRows * this.bytesPerPixel + nRows];
                if (this.filter == 1) {
                    this.leftBytes = new byte[16];
                }
                if (this.filter == 2) {
                    this.priorRow = new byte[this.width * this.bytesPerPixel];
                }
                int scanPos = 0;
                int startPos = 1;
                for (int i = 0; i < this.width * nRows; ++i) {
                    if (i % this.width == 0) {
                        scanLines[scanPos++] = (byte)this.filter;
                        startPos = scanPos;
                    }
                    scanLines[scanPos++] = (byte)(pixels[i] >> 16 & 0xFF);
                    scanLines[scanPos++] = (byte)(pixels[i] >> 8 & 0xFF);
                    scanLines[scanPos++] = (byte)(pixels[i] & 0xFF);
                    if (this.encodeAlpha) {
                        scanLines[scanPos++] = (byte)(pixels[i] >> 24 & 0xFF);
                    }
                    if (i % this.width == this.width - 1 && this.filter != 0) {
                        if (this.filter == 1) {
                            this.filterSub(scanLines, startPos, this.width);
                        }
                        if (this.filter == 2) {
                            this.filterUp(scanLines, startPos, this.width);
                        }
                    }
                }
                compBytes.write(scanLines, 0, scanPos);
                startRow += nRows;
                rowsLeft -= nRows;
            }
            compBytes.close();
            final byte[] compressedLines = outBytes.toByteArray();
            final int nCompressed = compressedLines.length;
            this.crc.reset();
            this.bytePos = this.writeInt4(nCompressed, this.bytePos);
            this.bytePos = this.writeBytes(PngEncoder.IDAT, this.bytePos);
            this.crc.update(PngEncoder.IDAT);
            this.bytePos = this.writeBytes(compressedLines, nCompressed, this.bytePos);
            this.crc.update(compressedLines, 0, nCompressed);
            this.crcValue = this.crc.getValue();
            this.bytePos = this.writeInt4((int)this.crcValue, this.bytePos);
            scrunch.finish();
            return true;
        }
        catch (IOException e) {
            System.err.println(e.toString());
            return false;
        }
    }
    
    protected void writeEnd() {
        this.bytePos = this.writeInt4(0, this.bytePos);
        this.bytePos = this.writeBytes(PngEncoder.IEND, this.bytePos);
        this.crc.reset();
        this.crc.update(PngEncoder.IEND);
        this.crcValue = this.crc.getValue();
        this.bytePos = this.writeInt4((int)this.crcValue, this.bytePos);
    }
    
    static {
        IHDR = new byte[] { 73, 72, 68, 82 };
        IDAT = new byte[] { 73, 68, 65, 84 };
        IEND = new byte[] { 73, 69, 78, 68 };
    }
}
