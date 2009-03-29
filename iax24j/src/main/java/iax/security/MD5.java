package iax.security;

/*
 * This program convert from JSR 1321 Reference Implementation md5.h md5c.c
 */
public class MD5 {
    private static final int S11 = 7;
    private static final int S12 = 12;
    private static final int S13 = 17;
    private static final int S14 = 22;
    private static final int S21 = 5;
    private static final int S22 = 9;
    private static final int S23 = 14;
    private static final int S24 = 20;
    private static final int S31 = 4;
    private static final int S32 = 11;
    private static final int S33 = 16;
    private static final int S34 = 23;
    private static final int S41 = 6;
    private static final int S42 = 10;
    private static final int S43 = 15;
    private static final int S44 = 21;
	
	private MD5Context	context;

	private static final byte	padding[]	= { (byte) 0x80, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0 };

	private void decode( final byte buffer[], final int shift, final int[] out) {
		for(int i = 0;i < out.length;++i){
			out[i] = (buffer[shift + (i * 4)] & 0xff) | ((buffer[shift + (i * 4 + 1)] & 0xff) << 8)
			| ((buffer[shift + (i * 4 + 2)] & 0xff) << 16)
			| (buffer[shift + (i * 4 + 3)] << 24);
		}
	}

	private static byte[] encode( final int input[], final int len) {
		int i, j;
		byte out[];
		out = new byte[len];
		for (i = j = 0; j < len; i++, j += 4) {
			out[j] = (byte) (input[i] & 0xff);
			out[j + 1] = (byte) ((input[i] >>> 8) & 0xff);
			out[j + 2] = (byte) ((input[i] >>> 16) & 0xff);
			out[j + 3] = (byte) ((input[i] >>> 24) & 0xff);
		}
		return out;
	}

    private final int rotateLeft(int x,int n){
        return (x << n) | (x >>> (32 - n));
    }

    private int F(int x,int y,int z){
        return (x & y) | (~x & z);
    }

    private int G(int x, int y, int z){
        return (x & z) | (y & ~z);
    }

    private int H(int x, int y, int z){
        return x ^ y ^ z;
    }

    private int I(int x, int y, int z){
        return y ^ (x | ~z);
    }

	private void transform(MD5Context context, byte buffer[], int shift, int[] decode_buf) {
		int a = context.state[0], b = context.state[1], c = context.state[2], d = context.state[3], x[] = decode_buf;

		decode(buffer, shift, decode_buf);

		/* Round 1 */
        a = rotateLeft(a + F(b, c, d) + x[ 0] + 0xd76aa478, S11) + b;
        d = rotateLeft(d + F(a, b, c) + x[ 1] + 0xe8c7b756, S12) + a;
        c = rotateLeft(c + F(d, a, b) + x[ 2] + 0x242070db, S13) + d;
        b = rotateLeft(b + F(c, d, a) + x[ 3] + 0xc1bdceee, S14) + c;
        a = rotateLeft(a + F(b, c, d) + x[ 4] + 0xf57c0faf, S11) + b;
        d = rotateLeft(d + F(a, b, c) + x[ 5] + 0x4787c62a, S12) + a;
        c = rotateLeft(c + F(d, a, b) + x[ 6] + 0xa8304613, S13) + d;
        b = rotateLeft(b + F(c, d, a) + x[ 7] + 0xfd469501, S14) + c;
        a = rotateLeft(a + F(b, c, d) + x[ 8] + 0x698098d8, S11) + b;
        d = rotateLeft(d + F(a, b, c) + x[ 9] + 0x8b44f7af, S12) + a;
        c = rotateLeft(c + F(d, a, b) + x[10] + 0xffff5bb1, S13) + d;
        b = rotateLeft(b + F(c, d, a) + x[11] + 0x895cd7be, S14) + c;
        a = rotateLeft(a + F(b, c, d) + x[12] + 0x6b901122, S11) + b;
        d = rotateLeft(d + F(a, b, c) + x[13] + 0xfd987193, S12) + a;
        c = rotateLeft(c + F(d, a, b) + x[14] + 0xa679438e, S13) + d;
        b = rotateLeft(b + F(c, d, a) + x[15] + 0x49b40821, S14) + c;

		/* Round 2 */
        a = rotateLeft(a + G(b, c, d) + x[ 1] + 0xf61e2562, S21) + b;
        d = rotateLeft(d + G(a, b, c) + x[ 6] + 0xc040b340, S22) + a;
        c = rotateLeft(c + G(d, a, b) + x[11] + 0x265e5a51, S23) + d;
        b = rotateLeft(b + G(c, d, a) + x[ 0] + 0xe9b6c7aa, S24) + c;
        a = rotateLeft(a + G(b, c, d) + x[ 5] + 0xd62f105d, S21) + b;
        d = rotateLeft(d + G(a, b, c) + x[10] + 0x02441453, S22) + a;
        c = rotateLeft(c + G(d, a, b) + x[15] + 0xd8a1e681, S23) + d;
        b = rotateLeft(b + G(c, d, a) + x[ 4] + 0xe7d3fbc8, S24) + c;
        a = rotateLeft(a + G(b, c, d) + x[ 9] + 0x21e1cde6, S21) + b;
        d = rotateLeft(d + G(a, b, c) + x[14] + 0xc33707d6, S22) + a;
        c = rotateLeft(c + G(d, a, b) + x[ 3] + 0xf4d50d87, S23) + d;
        b = rotateLeft(b + G(c, d, a) + x[ 8] + 0x455a14ed, S24) + c;
        a = rotateLeft(a + G(b, c, d) + x[13] + 0xa9e3e905, S21) + b;
        d = rotateLeft(d + G(a, b, c) + x[ 2] + 0xfcefa3f8, S22) + a;
        c = rotateLeft(c + G(d, a, b) + x[ 7] + 0x676f02d9, S23) + d;
        b = rotateLeft(b + G(c, d, a) + x[12] + 0x8d2a4c8a, S24) + c;

		/* Round 3 */
        a = rotateLeft(a + H(b, c, d) + x[ 5] + 0xfffa3942, S31) + b;
        d = rotateLeft(d + H(a, b, c) + x[ 8] + 0x8771f681, S32) + a;
        c = rotateLeft(c + H(d, a, b) + x[11] + 0x6d9d6122, S33) + d;
        b = rotateLeft(b + H(c, d, a) + x[14] + 0xfde5380c, S34) + c;
        a = rotateLeft(a + H(b, c, d) + x[ 1] + 0xa4beea44, S31) + b;
        d = rotateLeft(d + H(a, b, c) + x[ 4] + 0x4bdecfa9, S32) + a;
        c = rotateLeft(c + H(d, a, b) + x[ 7] + 0xf6bb4b60, S33) + d;
        b = rotateLeft(b + H(c, d, a) + x[10] + 0xbebfbc70, S34) + c;
        a = rotateLeft(a + H(b, c, d) + x[13] + 0x289b7ec6, S31) + b;
        d = rotateLeft(d + H(a, b, c) + x[ 0] + 0xeaa127fa, S32) + a;
        c = rotateLeft(c + H(d, a, b) + x[ 3] + 0xd4ef3085, S33) + d;
        b = rotateLeft(b + H(c, d, a) + x[ 6] + 0x04881d05, S34) + c;
        a = rotateLeft(a + H(b, c, d) + x[ 9] + 0xd9d4d039, S31) + b;
        d = rotateLeft(d + H(a, b, c) + x[12] + 0xe6db99e5, S32) + a;
        c = rotateLeft(c + H(d, a, b) + x[15] + 0x1fa27cf8, S33) + d;
        b = rotateLeft(b + H(c, d, a) + x[ 2] + 0xc4ac5665, S34) + c;

		/* Round 4 */
        a = rotateLeft(a + I(b, c, d) + x[ 0] + 0xf4292244, S41) + b;
        d = rotateLeft(d + I(a, b, c) + x[ 7] + 0x432aff97, S42) + a;
        c = rotateLeft(c + I(d, a, b) + x[14] + 0xab9423a7, S43) + d;
        b = rotateLeft(b + I(c, d, a) + x[ 5] + 0xfc93a039, S44) + c;
        a = rotateLeft(a + I(b, c, d) + x[12] + 0x655b59c3, S41) + b;
        d = rotateLeft(d + I(a, b, c) + x[ 3] + 0x8f0ccc92, S42) + a;
        c = rotateLeft(c + I(d, a, b) + x[10] + 0xffeff47d, S43) + d;
        b = rotateLeft(b + I(c, d, a) + x[ 1] + 0x85845dd1, S44) + c;
        a = rotateLeft(a + I(b, c, d) + x[ 8] + 0x6fa87e4f, S41) + b;
        d = rotateLeft(d + I(a, b, c) + x[15] + 0xfe2ce6e0, S42) + a;
        c = rotateLeft(c + I(d, a, b) + x[ 6] + 0xa3014314, S43) + d;
        b = rotateLeft(b + I(c, d, a) + x[13] + 0x4e0811a1, S44) + c;
        a = rotateLeft(a + I(b, c, d) + x[ 4] + 0xf7537e82, S41) + b;
        d = rotateLeft(d + I(a, b, c) + x[11] + 0xbd3af235, S42) + a;
        c = rotateLeft(c + I(d, a, b) + x[ 2] + 0x2ad7d2bb, S43) + d;
        b = rotateLeft(b + I(c, d, a) + x[ 9] + 0xeb86d391, S44) + c;

		context.state[0] += a;
		context.state[1] += b;
		context.state[2] += c;
		context.state[3] += d;
	}

	private void update(MD5Context context, byte buffer[], int offset, int length) {
		int index, partlen, i, start;

		if ((length - offset) > buffer.length){
			length = buffer.length - offset;
		}

		index = (int) (context.count & 0x3f);
		context.count += length;

		partlen = 64 - index;

		if (length >= partlen) {
			int[] decode_buf = new int[16];
			if (partlen == 64) {
				partlen = 0;
			} else {
				for (i = 0; i < partlen; ++i){
					context.buffer[i + index] = buffer[i + offset];
				}
				transform(context, context.buffer, 0, decode_buf);
			}
			for (i = partlen; (i + 63) < length; i += 64) {
				transform(context, buffer, i + offset, decode_buf);
			}
			index = 0;
		} else {
			i = 0;
		}

		if (i < length) {
			start = i;
			for (; i < length; ++i) {
				context.buffer[index + i - start] = buffer[i + offset];
			}
		}
	}
	
	public MD5() {
		context = new MD5Context();
	}
	
	public MD5( byte[] data ) {
		context = new MD5Context();
		update( data );
	}
	
	public void update( byte buffer[]) {
		if( buffer == null )
			return;
		this.context = new MD5Context();
		update( context, buffer,0, buffer.length );
	}

	public synchronized byte[] finish() {
		byte bits[];
		int index, padlen;

		MD5Context fin = new MD5Context(context);
		int[] count_ints = { (int) (fin.count << 3),(int) (fin.count >> 29) };
		bits = encode(count_ints, 8);
		index = (int) (fin.count & 0x3f);
		padlen = (index < 56) ? (56 - index) : (120 - index);
		update(fin, padding, 0, padlen);
		update(fin, bits, 0, 8);

		return encode(fin.state, 16);
	}
	
	public byte[] digest(byte[] input){
		update(input);
		return finish();
	}

}
