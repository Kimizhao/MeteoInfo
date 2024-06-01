package org.meteoinfo.data.meteodata.radar;

import org.meteoinfo.common.DataConvert;
import org.meteoinfo.ndarray.DataType;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class GenericHeader {
    public static int length = 32;
    public int magicNumber;
    public int majorVersion;
    public int minorVersion;
    public int genericType;
    public int productType;
    public byte[] reserved;

    /**
     * Constructor
     * @param raf RandomAccessFile object
     */
    public GenericHeader(RandomAccessFile raf) throws IOException {
        byte[] bytes = new byte[4];
        raf.read(bytes);
        magicNumber = DataConvert.bytes2Int(bytes, ByteOrder.LITTLE_ENDIAN);
        bytes = new byte[2];
        raf.read(bytes);
        majorVersion = DataConvert.bytes2UShort(bytes, ByteOrder.LITTLE_ENDIAN);
        raf.read(bytes);
        minorVersion = DataConvert.bytes2UShort(bytes, ByteOrder.LITTLE_ENDIAN);
        bytes = new byte[4];
        raf.read(bytes);
        genericType = DataConvert.bytes2Int(bytes, ByteOrder.LITTLE_ENDIAN);
        raf.read(bytes);
        productType = DataConvert.bytes2Int(bytes, ByteOrder.LITTLE_ENDIAN);
        reserved = new byte[16];
        raf.read(reserved);
    }

    /**
     * Constructor
     * @param in The InputStream
     */
    public GenericHeader(InputStream in) throws IOException {
        byte[] bytes = new byte[4];
        in.read(bytes);
        magicNumber = DataConvert.bytes2Int(bytes, ByteOrder.LITTLE_ENDIAN);
        bytes = new byte[2];
        in.read(bytes);
        majorVersion = DataConvert.bytes2UShort(bytes, ByteOrder.LITTLE_ENDIAN);
        in.read(bytes);
        minorVersion = DataConvert.bytes2UShort(bytes, ByteOrder.LITTLE_ENDIAN);
        bytes = new byte[4];
        in.read(bytes);
        genericType = DataConvert.bytes2Int(bytes, ByteOrder.LITTLE_ENDIAN);
        in.read(bytes);
        productType = DataConvert.bytes2Int(bytes, ByteOrder.LITTLE_ENDIAN);
        reserved = new byte[16];
        in.read(reserved);
    }

    /**
     * Constructor
     * @param inBytes The input bytes
     */
    public GenericHeader(byte[] inBytes) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.wrap(inBytes);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        magicNumber = byteBuffer.getInt();
        majorVersion = DataType.unsignedShortToInt(byteBuffer.getShort());
        minorVersion = DataType.unsignedShortToInt(byteBuffer.getShort());
        genericType = byteBuffer.getInt();
        productType = byteBuffer.getInt();
    }
}
