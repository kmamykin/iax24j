package iax.security;

class MD5Context {
	int[] state;
	long count;
	byte[] buffer;

	public MD5Context() {
		buffer = new byte[64];
		count = 0;
		state = new int[4];
		state[0] = 0x67452301;
		state[1] = 0xefcdab89;
		state[2] = 0x98badcfe;
		state[3] = 0x10325476;
	}

	/** Copy constructor */
	public MD5Context(MD5Context from) {
		this();

		for (int i = 0; i < buffer.length; ++i) {
			this.buffer[i] = from.buffer[i];
		}
		for (int i = 0; i < state.length; ++i) {
			this.state[i] = from.state[i];
		}
		this.count = from.count;
	}
};
