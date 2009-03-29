package iax.protocol.util;

/**
 * Utility class that imepements a byte buffer and needed functionality.
 *
 */
public class ByteBuffer {

	//Number of bit in a byte.
    private final static int BYTE_BITS = 8;
    //Mask to get the little byte. 
    private final static int BYTE_MASK = 0xff;
    /**
     * Number of bytes in 8 bits.
     */
    public final static int SIZE_8BITS = 1;
    /**
     * Number of bytes in 16 bits.
     */
    public final static int SIZE_16BITS = 2;
    /**
     * Number of bytes in 32 bits.
     */
    public final static int SIZE_32BITS = 4;
    
    //Las position written.
    private int pos;
    //Buffer.
    private byte[] buffer;

    /**
     * Constructor. Initizalizes the buffer for a given size.
     * @param i Size of the new buffer.
     * @throws ByteBufferException
     */
    public ByteBuffer(int i) throws ByteBufferException {
        if (i>0) {
            this.buffer = new byte[i];
            this.pos = 0;
        } else throw new ByteBufferException("ByteBuffer's length isn't greater than zero");
    }

    /**
     * Csontructor. Creates a byte buffer from a given byte array.
     * @param buff Byte array to initialize the buffer.
     */
    public ByteBuffer(byte[] buff) {
        this.buffer = buff;
        this.pos = 0;
    }
    
    /**
     * Gets the number of free bytes in the buffer. 
     * @return The number of free bytes in the buffer.
     */
    public int getRemaining() {
        return buffer.length - pos;
    }

    /**
     * Indicates if the buffer has free space.
     * @return true if buffer has some free space, false otherwise.
     */
    public boolean hasRemaining() {
        return  pos <= buffer.length;
    }

    /**
     * Gets the buffer data.
     * @return The buffer in a byte array.
     */
    public byte[] getBuffer() {
        return buffer;
    }
    
    /**
     * Gets the next 8 bits in the buffer.
     * @return An 8 bits number stored in an integer.
     * @throws ByteBufferException
     */
    public int get8bits() throws ByteBufferException {
        if  (pos + SIZE_8BITS <= buffer.length) {
            int result = buffer[pos];
            pos+=SIZE_8BITS;
            return result;
        } else throw new ByteBufferException("ByteBuffer index of bound");
    }
    
    /**
     * Gets 8 bits from the buffer given a position. 
     * @param pos The position to read.
     * @return An 8 bits number stored in an integer.
     * @throws ByteBufferException
     */
    public int get8bits(int pos) throws ByteBufferException {
        if (pos + SIZE_8BITS <= buffer.length) {
            int result = buffer[pos];
            return result;
        } else throw new ByteBufferException("ByteBuffer index of bound");
    }
  
    /**
     * Gets the next 16 bits in the buffer.
     * @return An 16 bits number stored in an integer.
     * @throws ByteBufferException
     */
    public int get16bits() throws ByteBufferException {
        if (pos + SIZE_16BITS <= buffer.length) {
            int result = 0;
            for (int i=0, j=SIZE_16BITS-1; i < SIZE_16BITS; i++, j--) {
                result += (int) ((buffer[pos+i] & BYTE_MASK) << (j*BYTE_BITS));
            }
            pos+=SIZE_16BITS;
            return result;
        } else throw new ByteBufferException("ByteBuffer index of bound");
    }
    
    /**
     * Gets 16 bits from the buffer given a position. 
     * @param pos The position to read.
     * @return An 16 bits number stored in an integer.
     * @throws ByteBufferException
     */
    public int get16bits(int pos) throws ByteBufferException {
        if (pos + SIZE_16BITS <= buffer.length) {
            int result = 0;
            for (int i=0, j=SIZE_16BITS-1; i < SIZE_16BITS; i++, j--) {
                result += (int) ((buffer[pos+i] & BYTE_MASK) << (j*BYTE_BITS));
            }
            return result;
        } else throw new ByteBufferException("ByteBuffer index of bound");
    }
    
    /**
     * Gets the next 32 bits in the buffer.
     * @return An 32 bits number stored in an integer.
     * @throws ByteBufferException
     */
    public long get32bits() throws ByteBufferException {
        if (pos + SIZE_32BITS <= buffer.length) {
            long result = 0;
            for (int i=0, j=SIZE_32BITS-1; i < SIZE_32BITS; i++, j--) {
                result += (long) ((buffer[pos+i] & BYTE_MASK) << (j*BYTE_BITS));
            }
            pos+=SIZE_32BITS;
            return result;
        } else throw new ByteBufferException("ByteBuffer index of bound");
    }

    /**
     * Gets 32 bits from the buffer given a position. 
     * @param pos The position to read.
     * @return An 32 bits number stored in an integer.
     * @throws ByteBufferException
     */
    public long get32bits(int pos) throws ByteBufferException {
        if (pos + SIZE_32BITS <= buffer.length) {
            long result = 0;
            for (int i=0, j=SIZE_32BITS-1; i < SIZE_32BITS; i++, j--) {
                result += (long) ((buffer[pos+i] & BYTE_MASK) << (j*BYTE_BITS));
            }
            return result;
        } else throw new ByteBufferException("ByteBuffer index of bound");
    }
   
    /**
     * Gets the unread bytes from the buffer.
     * @return The bytes that haven't been read in a byte array. 
     * @throws ByteBufferException
     */
    public byte[] getByteArray() throws ByteBufferException {
        if (pos <= buffer.length) {
            byte[] result = new byte[buffer.length-pos];
            System.arraycopy(buffer, pos, result, 0, result.length);
            pos+=result.length;
            return result;
        } else throw new ByteBufferException("ByteBuffer index of bound");
    }
    
    /**
     * Gets the bytes from pos to buffer end.
     * @param pos A given position in the buffer. Must be smaller than buffer size.
     * @return The bytes from pos to buffer end stored in a byte array.
     * @throws ByteBufferException
     */
    public byte[] getByteArray(int pos) throws ByteBufferException {
        if (pos <= buffer.length) {
            byte[] result = new byte[buffer.length-pos];
            System.arraycopy(buffer, pos, result, 0, result.length);
            return result;
        } else throw new ByteBufferException("ByteBuffer index of bound");
    }

    /**
     * Stores 8 bits in the buffer.
     * @param value The 8 bits to be stored given as integer.
     * @throws ByteBufferException
     */
    public void put8bits(int value) throws ByteBufferException {
        if (pos + SIZE_8BITS <= buffer.length) {
            buffer[pos] = (byte)value;
            pos+=SIZE_8BITS;
        } else throw new ByteBufferException("ByteBuffer index of bound");
    }
    
    /**
     * Stores 8 bits in the buffer in a given position.
     * Postion must be smaller than buffer size minus 1.
     * @param value The 8 bits to be stored given as integer.
     * @param pos The position to store the 8 bits. 
     * @throws ByteBufferException
     */
    public void put8bits(int value, int pos) throws ByteBufferException {
        if (pos + SIZE_8BITS <= buffer.length) {
            buffer[pos] = (byte)value;
        } else throw new ByteBufferException("ByteBuffer index of bound");
    }

    /**
     * Stores 16 bits in the buffer.
     * @param value The 16 bits to be stored given as integer.
     * @throws ByteBufferException
     */
    public void put16bits(int value) throws ByteBufferException {
        if (pos + SIZE_16BITS <= buffer.length) {
            for (int i=0, j=SIZE_16BITS-1; i < SIZE_16BITS; i++, j--) {
                buffer [pos+i] = (byte) (value >> (j*BYTE_BITS) & BYTE_MASK);
            }
            pos+=SIZE_16BITS;
        } else throw new ByteBufferException("ByteBuffer index of bound");
    }

    /**
     * Stores 16 bits in the buffer in a given position.
     * Postion must be smaller than buffer size minus 2.
     * @param value The 16 bits to be stored given as integer.
     * @param pos The position to store the 32 bits. 
     * @throws ByteBufferException
     */
    public void put16bits(int value, int pos) throws ByteBufferException {
        if (pos + SIZE_16BITS <= buffer.length) {
            for (int i=0, j=SIZE_16BITS-1; i < SIZE_16BITS; i++, j--) {
                buffer [pos+i] = (byte) (value >> (j*BYTE_BITS) & BYTE_MASK);
            }
        } else throw new ByteBufferException("ByteBuffer index of bound");
    }
    
    /**
     * Stores 32 bits in the buffer.
     * @param value The 32 bits to be stored given as integer.
     * @throws ByteBufferException
     */
    public void put32bits(long value) throws ByteBufferException {
        if (pos + SIZE_32BITS <= buffer.length) {
            for (int i=0, j=SIZE_32BITS-1; i < SIZE_32BITS; i++, j--) {
                buffer [pos+i] = (byte) (value >> (j*BYTE_BITS) & BYTE_MASK);
            }
            pos+=SIZE_32BITS;
        } else throw new ByteBufferException("ByteBuffer index of bound");
    }

    /**
     * Stores 32 bits in the buffer in a given position.
     * Postion must be smaller than buffer size minus 4.
     * @param value The 32 bits to be stored given as integer.
     * @param pos The position to store the 32 bits. 
     * @throws ByteBufferException
     */
    public void put32bits(long value, int pos) throws ByteBufferException {
        if (pos + SIZE_32BITS <= buffer.length) {
            for (int i=0, j=SIZE_32BITS-1; i < SIZE_32BITS; i++, j--) {
                buffer [pos+i] = (byte) (value >> (j*BYTE_BITS) & BYTE_MASK);
            }
        } else throw new ByteBufferException("ByteBuffer index of bound");
    }

    /**
     * Stores a byte array in the buffer.
     * @param byteArray The byte array to be stored.
     * @throws ByteBufferException
     */
    public void putByteArray(byte byteArray[]) throws ByteBufferException {
        if (pos + byteArray.length <= buffer.length) {
            System.arraycopy(byteArray, 0, buffer, pos, byteArray.length);
            pos+=byteArray.length;
        } else throw new ByteBufferException("ByteBuffer index of bound");
    }
    
    /**
     * Stores a byte array in a given position of the buffer.
     * The position plus the byte array length must be smaller than the buffer size.
     * @param byteArray The byte array to store.
     * @param pos The position to store the byte array.
     * @throws ByteBufferException
     */
    public void putByteArray(byte byteArray[], int pos) throws ByteBufferException {
        if (pos + byteArray.length <= buffer.length) {
            System.arraycopy(byteArray, 0, buffer, pos, byteArray.length);
        } else throw new ByteBufferException("ByteBuffer index of bound");
    }
}
