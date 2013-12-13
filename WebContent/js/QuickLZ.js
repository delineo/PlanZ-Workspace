String.prototype.getB64Bytes = function()
{
    var b = [], i, unicode;

    for(i = 0; i < this.length; i++)
    {
        unicode = this.charCodeAt(i);

        if (unicode <= 0x7f)
        {   // 0x00000000 - 0x0000007f -> 0xxxxxxx
            b.push(unicode);
        }
        else if (unicode <= 0x7ff)
        {   // 0x00000080 - 0x000007ff -> 110xxxxx 10xxxxxx
            b.push(((unicode >>  6)       ) | 0xc0);
            b.push(((unicode      ) & 0x3f) | 0x80);
        }
        else if (unicode <= 0xffff)
        {   // 0x00000800 - 0x0000ffff -> 1110xxxx 10xxxxxx 10xxxxxx
            b.push(((unicode >> 12)       ) | 0xe0);
            b.push(((unicode >>  6) & 0x3f) | 0x80);
            b.push(((unicode      ) & 0x3f) | 0x80);
        }
        else
        {   // 0x00010000 - 0x001fffff -> 11110xxx 10xxxxxx 10xxxxxx 10xxxxxx
            b.push(((unicode >> 18)       ) | 0xf0);
            b.push(((unicode >> 12) & 0x3f) | 0x80);
            b.push(((unicode >>  6) & 0x3f) | 0x80);
            b.push(((unicode      ) & 0x3f) | 0x80);
        }
    }
    return b;
};

Array.prototype.toB64String = function()
{
    var out = [], pos = 0, c = 0;
    while (pos < this.length)
    {
        var c1 = this[pos++];
        if ((c1 & 0x80) == 0x00)        // c1 <= 0x7f
        {   // 0xxxxxxx
            out[c++] = String.fromCharCode(c1);
        }
        else if ((c1 & 0xe0) == 0xc0)   // c1 >= 0xc0 && c1 < 0xe0
        {   // 110xxxxx 10xxxxxx
            var c2 = this[pos++];
            out[c++] = String.fromCharCode
            (
                (c1 & 0x1f) <<  6
              | (c2 & 0x3f)
            );
        }
        else if ((c1 & 0xf0) == 0xe0)
        {   // 1110xxxx 10xxxxxx 10xxxxxx
            var c2 = this[pos++];
            var c3 = this[pos++];
            out[c++] = String.fromCharCode
            (
                (c1 & 0x0f) << 12
              | (c2 & 0x3f) <<  6
              | (c3 & 0x3f)
            );
        }
        else
        {   // 11110xxx 10xxxxxx 10xxxxxx 10xxxxxx
            var c2 = this[pos++];
            var c3 = this[pos++];
            var c4 = this[pos++];
            out[c++] = String.fromCharCode
            (
                (c1 & 0x07) << 18
              | (c2 & 0x3f) << 12
              | (c3 & 0x3f) <<  6
              | (c4 & 0x3f)
            );
        }
    }
    return out.join('');
};

var QuickLZ =
({
    // Streaming mode not supported
    QLZ_STREAMING_BUFFER    : 0

    // Bounds checking not supported. Use try...catch instead
  , QLZ_MEMORY_SAFE         : 0

  , QLZ_VERSION_MAJOR       : 1
  , QLZ_VERSION_MINOR       : 5
  , QLZ_VERSION_REVISION    : 0

    // Decrease QLZ_POINTERS_3 to increase compression speed of level 3. Do not
    // edit any other constants!
  , init : function()
    {
        this.HASH_VALUES             = 4096;
        this.MINOFFSET               = 2;
        this.UNCONDITIONAL_MATCHLEN  = 6;
        this.UNCOMPRESSED_END        = 4;
        this.CWORD_LEN               = 4;
        this.DEFAULT_HEADERLEN       = 9;
        this.QLZ_POINTERS_1          = 1;
        this.QLZ_POINTERS_3          = 16;

        // void write_header(byte[] dst, int level, boolean compressible, int size_compressed, int size_decompressed)
        this.write_header = function(dst, level, compressible, size_compressed, size_decompressed)
        {
            dst[0]  = (0xff & (2 | (compressible ? 1 : 0)));
            dst[0] |= (0xff & (level << 2));
            dst[0] |= (1 << 6);
            dst[0] |= (0 << 4);
            this.fast_write(dst, 1, size_decompressed, 4);
            this.fast_write(dst, 5, size_compressed  , 4);
        };

        // long fast_read(byte[] a, int i, int numbytes)
        this.fast_read = function(a, i, numbytes)
        {
            var l = 0; //    long l = 0;
            for (var j = 0; j < numbytes; j++)
                l |= ((a[i+j] & 0xff) << j * 8);

            return l;
        };

        // void fast_write(byte[] a, int i, long value, int numbytes)
        this.fast_write = function(a, i, value, numbytes)
        {
            for (var j = 0; j < numbytes; j++)
                a[i + j] = (0xff & (value >>> (j * 8)));
        };

        return this;
    }
    // int headerLen(byte[] source)
  , headerLen : function(source)
    {
        return ((source[0] & 2) == 2) ? 9 : 3;
    }
    // long sizeDecompressed(byte[] source)
  , sizeDecompressed : function(source)
    {
        if (this.headerLen(source) == 9)
            return this.fast_read(source, 5, 4);
        else
            return this.fast_read(source, 2, 1);
    }
    // long sizeCompressed(byte[] source)
  , sizeCompressed : function(source)
    {
        if (this.headerLen(source) == 9)
            return this.fast_read(source, 1, 4);
        else
            return this.fast_read(source, 1, 1);
    }
    // byte[] compress(byte[] source, int level)
  , compress : function(source, level)
    {
        // 문자열을 배열로 전환
        if (typeof(source) == 'string')
            source = source.getB64Bytes();

        if (source.length == 0)
            return new Array(0);

        var src = 0;
        var dst = this.DEFAULT_HEADERLEN + this.CWORD_LEN;

        var cword_val    = 0x80000000;
        var cword_ptr    = this.DEFAULT_HEADERLEN;
        var destination  = new Array(source.length + 400);
        var hashtable;
        var cachetable   = new Array(this.HASH_VALUES);
        var hash_counter = new Array(this.HASH_VALUES);
        var d2;
        var fetch = 0;
        var last_matchstart = (source.length - this.UNCONDITIONAL_MATCHLEN - this.UNCOMPRESSED_END - 1);
        var lits  = 0;

        if (level != 1 && level != 3)
        {
            throw 'Javascript version only supports level 1 and 3';
        }

        hashtable = new Array(this.HASH_VALUES);
        for(var idx=0; idx<this.HASH_VALUES; idx++)
            hashtable[idx] = new Array(level==1?this.QLZ_POINTERS_1:this.QLZ_POINTERS_3);

        if (src <= last_matchstart)
            fetch = this.fast_read(source, src, 3);

        while (src <= last_matchstart)
        {
            if ((cword_val & 1) == 1)
            {
                if (src > 3 * (source.length >> 2) && dst > src - (src >> 5))
                {
                    d2 = new Array(this.DEFAULT_HEADERLEN);
                    this.write_header(d2, level, false, source.length, source.length + this.DEFAULT_HEADERLEN);
                    return d2.concat(source);
                }

                this.fast_write(destination, cword_ptr, (cword_val >>> 1) | 0x80000000, 4);
                cword_ptr = dst;
                dst += this.CWORD_LEN;
                cword_val = 0x80000000;
            }

            if (level == 1)
            {
                var hash  = ((fetch >>> 12) ^ fetch) & (this.HASH_VALUES - 1);
                var o     = hashtable[hash][0];
                var cache = cachetable[hash] ^ fetch;

                cachetable[hash] = fetch;
                hashtable[hash][0] = src;

                if (
                       cache == 0 
                    && hash_counter[hash] != 0
                    && (
                           src - o > this.MINOFFSET
                        || (
                               src  == o + 1
                            && lits >= 3
                            && src  >  3
                            && source[src] == source[src - 3]
                            && source[src] == source[src - 2]
                            && source[src] == source[src - 1]
                            && source[src] == source[src + 1]
                            && source[src] == source[src + 2]
                           )
                       )
                   )
                {
                    cword_val = ((cword_val >>> 1) | 0x80000000);
                    if (source[o + 3] != source[src + 3])
                    {
                        var f = 3 - 2 | (hash << 4);
                        destination[dst + 0] = (0xff & (f >>> 0 * 8));
                        destination[dst + 1] = (0xff & (f >>> 1 * 8));
                        src += 3;
                        dst += 2;
                    }
                    else
                    {
                        var old_src = src;
                        var remaining = (source.length - this.UNCOMPRESSED_END - src + 1 - 1) > 255
                                      ? 255
                                      : (source.length - this.UNCOMPRESSED_END - src + 1 - 1)
                        ;

                        src += 4;
                        if (source[o + src - old_src] == source[src])
                        {
                            src++;
                            if (source[o + src - old_src] == source[src])
                            {
                                src++;
                                while (source[o + (src - old_src)] == source[src] && (src - old_src) < remaining)
                                    src++;
                            }
                        }

                        var matchlen = src - old_src;

                        hash <<= 4;
                        if (matchlen < 18)
                        {
                            var f = hash | (matchlen - 2);
                            // Neither Java nor C# wants to inline fast_write
                            destination[dst + 0] = (0xff & (f >>> 0 * 8));
                            destination[dst + 1] = (0xff & (f >>> 1 * 8));
                            dst += 2;
                        }
                        else
                        {
                            var f = hash | (matchlen << 16);
                            this.fast_write(destination, dst, f, 3);
                            dst += 3;
                        }
                    }
                    lits = 0;
                    fetch = this.fast_read(source, src, 3);
                }
                else
                {
                    lits++;
                    hash_counter[hash] = 1;
                    destination[dst] = source[src];
                    cword_val = (cword_val >>> 1);
                    src++;
                    dst++;
                    fetch = ((fetch >>> 8) & 0xffff) | ((source[src + 2] & 0xff) << 16);
                }
            }
            else
            { 
                fetch = this.fast_read(source, src, 3);

                var o, offset2;
                var matchlen, k, m, best_k = 0;
                var c;
                var remaining = (source.length - this.UNCOMPRESSED_END - src + 1 - 1) > 255
                              ? 255
                              : (source.length - this.UNCOMPRESSED_END - src + 1 - 1)
                ;
                var hash = ((fetch >>> 12) ^ fetch) & (this.HASH_VALUES - 1);

                c = hash_counter[hash];
                matchlen = 0;
                offset2  = 0;
                for (k = 0; k < this.QLZ_POINTERS_3 && (c > k || c < 0); k++)
                {
                    o = hashtable[hash][k];

                    if (
                           (0xff & (fetch       )) == source[o  ]
                        && (0xff & (fetch >>>  8)) == source[o+1]
                        && (0xff & (fetch >>> 16)) == source[o+2]
                        && o < src - MINOFFSET
                       )
                    {
                        m = 3;

                        while (source[o + m] == source[src + m] && m < remaining)
                            m++;

                        if ((m > matchlen) || (m == matchlen && o > offset2))
                        {
                            offset2  = o;
                            matchlen = m;
                            best_k   = k;
                        }
                    }
                }
                o = offset2;
                hashtable[hash][c & (this.QLZ_POINTERS_3 - 1)] = src;
                c++;
                hash_counter[hash] = c;

                if (matchlen >= 3 && src - o < 131071)
                {
                    var offset = src - o;
                    for (var u = 1; u < matchlen; u++)
                    {
                        fetch = this.fast_read(source, src + u, 3);
                        hash  = ((fetch >>> 12) ^ fetch) & (this.HASH_VALUES - 1);
                        c = hash_counter[hash]++;
                        hashtable[hash][c & (this.QLZ_POINTERS_3 - 1)] = src + u;
                    }

                    src += matchlen;
                    cword_val = ((cword_val >>> 1) | 0x80000000);

                    if (matchlen == 3 && offset <= 63)
                    {
                        this.fast_write(destination, dst, offset << 2, 1);
                        dst++;
                    }
                    else if (matchlen == 3 && offset <= 16383)
                    {
                        this.fast_write(destination, dst, (offset << 2) | 1, 2);
                        dst += 2;
                    }
                    else if (matchlen <= 18 && offset <= 1023)
                    {
                        this.fast_write(destination, dst, ((matchlen - 3) << 2) | (offset << 6) | 2, 2);
                        dst += 2;
                    }
                    else if (matchlen <= 33)
                    {
                        this.fast_write(destination, dst, ((matchlen - 2) << 2) | (offset << 7) | 3, 3);
                        dst += 3;
                    }
                    else
                    {
                        this.fast_write(destination, dst, ((matchlen - 3) << 7) | (offset << 15) | 3, 4);
                        dst += 4;
                    }
                }
                else
                {
                    destination[dst] = source[src];
                    cword_val = (cword_val >>> 1);
                    src++;
                    dst++;
                }
            }
        }

        while (src <= source.length - 1)
        {
            if ((cword_val & 1) == 1)
            {
                this.fast_write(destination, cword_ptr, ((cword_val >>> 1) | 0x80000000), 4);
                cword_ptr = dst;
                dst += this.CWORD_LEN;
                cword_val = 0x80000000;
            }

            destination[dst] = source[src];
            src++;
            dst++;
            cword_val = (cword_val >>> 1);
        }
        while ((cword_val & 1) != 1)
        {
            cword_val = (cword_val >>> 1);
        }
        this.fast_write(destination, cword_ptr, ((cword_val >>> 1) | 0x80000000), this.CWORD_LEN);
        this.write_header(destination, level, true, source.length, dst);

        return destination.slice(0, dst);
    }

  , decompress : function(source)
	{
		var size = this.sizeDecompressed(source);
		var src  = this.headerLen(source);
		var dst  = 0;
		var cword_val = 1;
		var destination  = new Array(size);
		var hashtable    = new Array(4096);
		var hash_counter = new Array(4096);
		var last_matchstart = size - this.UNCONDITIONAL_MATCHLEN - this.UNCOMPRESSED_END - 1;
		var last_hashed = -1;
		var hash;
		var fetch = 0;

		var level = (source[0] >>> 2) & 0x3;

		if (level != 1 && level != 3)
			throw 'Javascript version only supports level 1 and 3';

		if ((source[0] & 1) != 1)
			return source.slice(this.headerLen(source), size);

		for (;;)
		{
			if (cword_val == 1)
			{
				cword_val = this.fast_read(source, src, 4);
				src += 4;
				if (dst <= last_matchstart)
				{
					if(level == 1)
						fetch = this.fast_read(source, src, 3);
					else
						fetch = this.fast_read(source, src, 4);
				}
			}

			if ((cword_val & 1) == 1)
			{
				var matchlen
				  , offset2;

				cword_val = cword_val >>> 1;

				if (level == 1)
				{
					hash = (fetch >>> 4) & 0xfff;
					offset2 = hashtable[hash];

					if ((fetch & 0xf) != 0)
					{
						matchlen = (fetch & 0xf) + 2;
						src += 2;
					}
					else
					{
						matchlen = source[src + 2] & 0xff;
						src += 3;
					}
				}
				else
				{
					var offset;

					if ((fetch & 3) == 0)
					{
						offset = (fetch & 0xff) >>> 2;
						matchlen = 3;
						src++;
					}
					else if ((fetch & 2) == 0)
					{
						offset = (fetch & 0xffff) >>> 2;
						matchlen = 3;
						src += 2;
					}
					else if ((fetch & 1) == 0)
					{
						offset = (fetch & 0xffff) >>> 6;
						matchlen = ((fetch >>> 2) & 15) + 3;
						src += 2;
					}
					else if ((fetch & 127) != 3)
					{
						offset = (fetch >>> 7) & 0x1ffff;
						matchlen = ((fetch >>> 2) & 0x1f) + 2;
						src += 3;
					}
					else
					{
						offset = (fetch >>> 15);
						matchlen = ((fetch >>> 7) & 255) + 3;
						src += 4;
					}
					offset2 = dst - offset;
				}

				destination[dst + 0] = destination[offset2 + 0];
				destination[dst + 1] = destination[offset2 + 1];
				destination[dst + 2] = destination[offset2 + 2];

				for (var i = 3; i < matchlen; i += 1)
				{
					destination[dst + i] = destination[offset2 + i];
				}
				dst += matchlen;

				if (level == 1)
				{
					fetch = this.fast_read(destination, last_hashed + 1, 3); // destination[last_hashed + 1] | (destination[last_hashed + 2] << 8) | (destination[last_hashed + 3] << 16);
					while (last_hashed < dst - matchlen)
					{
						last_hashed++;
						hash = ((fetch >>> 12) ^ fetch) & (this.HASH_VALUES - 1);
						hashtable[hash] = last_hashed;
						hash_counter[hash] = 1;
						fetch = fetch >>> 8 & 0xffff
						      | (destination[last_hashed + 3] & 0xff) << 16;
					}
					fetch = this.fast_read(source, src, 3);
				}
				else
				{
					fetch = this.fast_read(source, src, 4);
				}
				last_hashed = dst - 1;
			}
			else
			{
				if (dst <= last_matchstart)
				{
					destination[dst] = source[src];
					dst += 1;
					src += 1;
					cword_val = cword_val >>> 1;

					if (level == 1)
					{
						while (last_hashed < dst - 3)
						{
							last_hashed++;
							var fetch2 = this.fast_read(destination, last_hashed, 3);
							hash = ((fetch2 >>> 12) ^ fetch2) & (this.HASH_VALUES - 1);
							hashtable[hash] = last_hashed;
							hash_counter[hash] = 1;
						}
						fetch = fetch >> 8 & 0xffff 
						      | (source[src + 2] & 0xff) << 16
						;
					}
					else
					{
						fetch = fetch >> 8 & 0xffff 
						      | (source[src + 2] & 0xff) << 16
						      | (source[src + 3] & 0xff) << 24
						;
					}
				}
				else
				{
					while (dst <= size - 1)
					{
						if (cword_val == 1)
						{
							src += this.CWORD_LEN;
							cword_val = 0x80000000;
						}

						destination[dst] = source[src];
						dst++;
						src++;
						cword_val = cword_val >>> 1;
					}
					return destination;
				}
			}
		}
	}
}).init();