package planz.util;

import java.nio.charset.*;
import java.util.*;

/// <summary>
/// 바이트 단위의 데이터를 처리하기 위한 버퍼 클래스
/// </summary>
public class ByteBuffer extends Object
{
    /// <summary>
    /// 현재 버퍼내 데이터가 기록된 마지막 위치로 다음 기록할 위치가 된다.
    /// </summary>
    private int _offset = 0;
    public int  getOffset() { return _offset; }
    public void setOffset(int offset) { _offset = offset; }

    /// <summary>
    /// 버퍼내에서 기록할 수 있는 바이트 수를 반환한다.
    /// Remaining은 Append 함수 사용 시 앞서 기록한 데이터를 제외하고
    /// 버퍼에 기록할 수 있는 바이트 수에 대하여 계산된 값으로 "버퍼의 크기 - 마지막 기록위치"가 된다.
    /// </summary>
    public int getRemaining()
    {
        // 예외성 제거
        if (_buffer         == null ) return -1;
        if (_buffer.length  <= 0    ) return -1;

        return _buffer.length - _offset;
    }

    /// <summary>
    /// ByteBuffer 내부에서 사용하는 바이트 버퍼
    /// </summary>
    private byte[] _buffer;
    public byte[] getBuffer() { return _buffer; }

    /// <summary>
    /// 현재 버퍼에 데이터가 기록된 데이터의 길이를 가져오거나, 설정한다.
    /// </summary>
    public int getLength() { return _offset; }
    public void setLength(int length) 
    {
        // _buffer의 인스턴스가 생성되지 않았거나, 인수가 잘못된 경우
        // 처리하지 않는다.
        if (_buffer == null)
        {
            _buffer = new byte[length];
            return;
        }

        if (length < 0)
            return;
        if (length == this.getLength())
            return;

        if (length == 0)
        {
            _buffer = null;
            _offset = 0;
            return;
        }

        try
        {
            byte[] buff = new byte[length];

            // 기존 데이터는 지우지 않는다.
            // 만약 설정된 값이 기존 크기보다 작을시 설정된 크기만큼 복사한다.
            int write = Math.min(length, _offset);
            write(buff, 0, write, _buffer);

            _buffer = buff;
            _offset = write;
        }
        finally { }
    }

    /// <summary>
    /// 버퍼의 초기 크기로 버퍼를 초기화 할때 버퍼의 기본 크기로 사용된다.
    /// </summary>
    private int _capacity;
    public int getCapacity() { return _capacity; }

    /// <summary>버퍼의 인코딩을 가져오거나 설정한다.</summary>
    private static Charset _charset = Charset.defaultCharset();
    public Charset getCharset() { return _charset; }
    public void    setCharset(Charset charset) { _charset = charset; }
    public void    setCharset(String charsetName)
    {
        if (!Charset.isSupported(charsetName))
        _charset = Charset.forName(charsetName);
    }

    public ByteBuffer()
    {
        _buffer   = null;
        _offset   = 0;
        _capacity = 0;
        _charset  = Charset.defaultCharset();
    }

    public ByteBuffer(int capacity)
    {
        _buffer   = new byte[capacity];

        _offset   = 0;
        _capacity = capacity;
        _charset  = Charset.defaultCharset();
    }

    public ByteBuffer(byte[] buffer)
    {
        try
        {
            _buffer   = new byte[buffer.length];
            System.arraycopy(buffer, 0, _buffer, 0, buffer.length);

            _offset   = buffer.length;
            _capacity = 0;
            _charset  = Charset.defaultCharset();
        }
        finally { }
    }
    
    public ByteBuffer(byte[] buffer, int index, int count)
    {
        try
        {
            _buffer   = new byte[count];

            count     = Math.max(buffer.length, count);

            System.arraycopy(buffer, index, _buffer, 0, count);

            _offset   = count;
            _capacity = 0;
            _charset  = Charset.defaultCharset();
        }
        finally { }
    }

    /// <summary>
    /// 원본 바이트 배열을 지정한 배열로 복사한다.
    /// 원본 바이트 배열을 전체 복사를 시도하나, 대상 바이트 배열의 저장 공간이 부족할 경우
    /// 대상 바이트 배열에 지정된 크기 만큼 복사한다.
    /// </summary>
    /// <param name="dest">복사한 바이트 배열을 저장할 대상 바이트 배열</param>
    /// <param name="pos">복사한 바이트 배열을 저장할 대상 바이트 배열의 시작 위치</param>
    /// <param name="count">원본 바이트 배열로부터 복사할 바이트 수</param>
    /// <param name="src">복사할 원본 바이트 배열</param>
    public static void write(byte[] dest, int pos, int count, byte[] src)
    {
        try
        {
            int length = count;

            if (dest.length - pos < length)
                length = dest.length - pos;

            System.arraycopy(src, 0, dest, pos, length);
        }
        finally { }
    }

    /// <summary>
    /// 원본 바이트 배열을 지정한 배열로 복사한다.
    /// 원본 바이트 배열을 전체 복사를 시도하나, 대상 바이트 배열의 저장 공간이 부족할 경우
    /// 대상 바이트 배열에 지정된 크기 만큼 복사한다.
    /// </summary>
    /// <param name="dest">복사한 바이트 배열을 저장할 대상 바이트 배열</param>
    /// <param name="pos">복사한 바이트 배열을 저장할 대상 바이트 배열의 시작 위치</param>
    /// <param name="src">복사할 원본 바이트 배열</param>
    public static void write(byte[] dest, int pos, byte[] src)
    {
        write(dest, pos, src.length, src);
    }

    public static void write(byte[] dest, int pos, String src)
    {
        byte[] conv = src.getBytes(_charset);

        write(dest, pos, conv.length, conv);
    }
/*
    public static void write(byte[] dest, int pos, int src)
    {
        byte[] conv = ByteConverter.ToByte(src);

        write(dest, pos, conv.length, conv);
    }
*/
    /// <summary>
    /// 지정한 바이트 배열의 <para>offset</para>위치에서 <para>count</para>만큼 데이터를
    /// 바이트 배열로 반환한다.
    /// </summary>
    /// <param name="buff">바이트 배열</param>
    /// <param name="offset">버퍼에서 읽기 시작할 위치</param>
    /// <param name="count">버퍼에서 읽을 데이터의 크기</param>
    /// <returns>메시지 버퍼에서 읽은 바이트 배열</returns>
    public static byte[] Read(byte[] buff, int offset, int count)
    {
        // 예외성 제거
        if (buff        == null) return null;
        if (buff.length <=    0) return null;
        if (count       <=    0) return null;
        if (offset      <     0) return null;

        int length = count;
        length = Math.min(length, buff.length - offset);

        byte[] retArray = new byte[length];
        
        write(retArray, 0, count, buff);

        return retArray;
    }

    /// <summary>
    /// 지정된 byte 배열 데이터를 읽어 내부 버퍼에 기록한다.
    /// </summary>
    /// <param name="pos">내부 버퍼의 기록할 시작 위치</param>
    /// <param name="src">기록할 데이터 소스</param>
    /// <param name="index">기록할 데이터의 위치</param>
    /// <param name="count">기록할 데이터의 수</param>
    /// <returns>내부 버퍼에 기록한 데이터의 수</returns>
    public synchronized int write(int pos, byte[] src, int index, int count)
    {
        // 버퍼가 생성되어 있지 않다면, 버퍼를 생성하고,
        if (_buffer == null)
            _buffer = new byte[count];

        if (pos < 0)
            return -1;

        int ret = -1;

        try
        {
            int    buffSize= _buffer.length;

            // 지정한 크기보다 복사할 내용이 적을 수 있으므로, 복사 가능한 크기를 계산하고,
            count = Math.min(src.length, count);

            // 복사하고자 하는 원본이 바이트 버퍼보다 클 경우 버퍼의 크기를 늘려서 복사하고, 
            // 그렇지 않다면, 버퍼에 단순 복사한다.
            if (buffSize < pos + count)
            {
                // 새로운 버퍼를 생성하고,
                byte[] newBuff = new byte[pos + count];

                // 기존 버퍼에 존재하는 데이터를 복사하고,
                System.arraycopy(_buffer, 0, newBuff, 0, _buffer.length);

                // 복사할 바이트 배열을 새로운 버퍼에 복사하고,
                System.arraycopy(src, 0, newBuff, pos, count);

                // 새로운 버퍼로 교체한다.
                _buffer = newBuff;
            }
            else
            {
                // 버퍼에 단순 복사한다.
                System.arraycopy(src, 0, _buffer, pos, count);
            }

            // 데이터를 기록한 최대 위치를 계산한다.
            _offset = Math.max(_offset, pos + count);

            ret = count; 
        }
        catch (Exception ex)
        {
            ret = -1;
        }

        this.notify();

        return ret;
    }

    /// <summary>
    /// 지정된 ByteBuffer의 데이터를 읽어 내부 버퍼에 기록한다.
    /// </summary>
    /// <param name="pos">내부 버퍼의 기록할 시작 위치</param>
    /// <param name="src">기록할 데이터 소스</param>
    /// <param name="index">기록할 데이터의 위치</param>
    /// <param name="count">기록할 데이터의 수</param>
    /// <returns>내부 버퍼에 기록한 데이터의 수</returns>
    public int write(int pos, ByteBuffer src, int index, int count)
    {
        return write(pos, src.getBuffer(), index, count);
    }

    public int write(int pos, String src)
    {
        byte[] buff = src.getBytes(_charset);
        
        return write(pos, buff, 0, buff.length);
    }

    public int write(int pos, byte[] src)
    {
        return write(pos, src, 0, src.length);
    }

    public int write(int pos, byte src)
    {
        return write(pos, new byte [] {src});
    }

    public int write(byte[] src)
    {
        return write(0, src, 0, src.length);
    }

    public int write(ByteBuffer src)
    {
        return write(0, src, 0, src.getLength());
    }

    /// <summary>
    /// 메시지 버퍼의 <para>offset</para>에서부터 <para>count</para> 만큼의 데이터를
    /// 바이트 배열로 반환한다.
    /// </summary>
    /// <param name="offset">버퍼로부터 가져올 데이터의 시작 위치</param>
    /// <param name="count">지정된 <para>offset</para>에서부터 가져올 데이터 수</param>
    /// <returns></returns>
    public synchronized byte[] read(int offset, int count)
    {
        // 예외성 제거
        if (offset  <  0    ) return null;
        if (count   <= 0    ) return null;
        if (_buffer == null ) return null;

        count = Math.min(_buffer.length - offset, count);

        byte[] buff = new byte[count];

        try
        {
            System.arraycopy(_buffer, offset, buff, 0, count);
        }
        catch (Exception ex)
        {
            buff = null;
        }

        return buff;
    }

    public byte[] read()
    {
        return read(0, this.getLength());
    }

    public synchronized byte[] readEndZero(int offset)
    {
        // 예외성 제거
        if (offset  <  0    ) return null;
        if (_buffer == null ) return null;

        byte[] buff = null;
        int count = 0;

        try
        {
            for (int idx = offset; idx < _buffer.length; idx++)
            {
                if (_buffer[idx] == 0x00)
                    break;

                count++;
            }

            if (count > 0)
            {
                buff = new byte[count];
                System.arraycopy(_buffer, offset, buff, 0, count);
            }
        }
        catch (Exception ex)
        {
            buff = null;
        }

        return buff;
    }

    public byte[] readEndZero()
    {
        return readEndZero(0);
    }

    public synchronized int append(byte[] src, int length)
    {
        if (src == null) return 0;
        if (src.length <= 0) return src.length;
        if (length < 0)  return 0;

        if (src.length < length)
            length = src.length;

        length = (_buffer == null) ? length : _offset + length;

        try
        {
            // 추가하고자 하는 데이터의 크기가 기본 버퍼에 기록함에 부족하지 않다면 단순 복사하고,
            // 만약 기존 버퍼의 크기가 모자랄 경우 새로운 버퍼에 기록 후 기존 버퍼와 교환한다.
            if (this.getLength() < length)
            {
                // 새로운 버퍼를 생성하고,
                byte[] newBuff = new byte[length];

                // 기존 버퍼가 존재하고, 데이터가 존재한다면,
                // 기존 버퍼에 있는 데이터를 새로운 버퍼로 복사하고,
                if (_buffer != null && _offset > 0)
                    System.arraycopy(_buffer, 0, newBuff, 0, _offset);

                // 기존 버퍼를 새로운 버퍼로 교환하고,
                _buffer = newBuff;
            }

            // 추가할 데이터를 새로운 버퍼에 복사한다.
            System.arraycopy(src, 0, _buffer, _offset, src.length);

            _offset = length;
        }
        catch (Exception ex)
        {
            length = 0;
        }

        this.notify();

        return length;
    }

    public int append(byte[] src)
    {
        return append(src, src.length);
    }

    public int append(String src, int length)
    {
        byte[] buff = src.getBytes(_charset);
        return append(buff, length);
    }

    public int append(String src)
    {
        byte[] buff = src.getBytes(_charset);
        return append(buff, buff.length);
    }

    public int append(String format, Object...args)
    {
        String src = String.format(format, args);
        byte buff[] = src.getBytes();
        return append(buff, buff.length);
    }
/*
    public int append(int src)
    {
        return append(ByteConvert.ToByte(src));
    }

    public int append(short src)
    {
        return append(ByteConvert.ToByte(src));
    }
*/
    public int append(ByteBuffer src, int length)
    {
        return append(src.read(0, length), length);
    }

    public int append(ByteBuffer src)
    {
        return append(src.read());
    }

    public synchronized byte[] getRawBuffer()
    {
        // 예외성 제거
        if (_buffer == null ) return null;
        if (_offset <= 0    ) return null;

        byte[] raw = null;

        try
        {
            raw = new byte[_offset];
            System.arraycopy(_buffer, 0, raw, 0, _offset);
        }
        catch (Exception ex)
        {
            raw = null;
        }

        return raw;
    }

    /// <summary>
    /// 지정된 offset 위치까지의 데이터를 제거하고 나머지 데이터를
    /// 0 위치로 옮긴다.
    /// </summary>
    /// <param name="offset">데이터를 제거할 위치</param>
    public synchronized void resize(int offset)
    {
        // 예외성 제거
        if (_buffer == null ) return;
        if (_offset <= 0    ) return;
        if (offset  <= 0    ) return;

        byte[] tmp = _buffer;

        try
        {
            int newSize = 0;

            newSize = _buffer.length - offset;
            //newSize = Math.Max(newSize, _capacity);       
            byte[] newBuff = new byte[newSize];

            int count = _offset - offset;

            System.arraycopy(_buffer, offset, newBuff, 0, count);

            _buffer = newBuff;
            _offset = count;
        }
        catch (Exception ex)
        {
            _buffer = tmp;
        }
    }

    /// <summary>
    /// 지정된 ByteBuffer.Offset 위치까지의 데이터를 제거하고 나머지 데이터를
    /// 0 위치로 옮긴다.
    /// </summary>
    public synchronized void resize()
    {
        // 예외성 제거
        if (_buffer == null ) return;
        if (_offset <= 0    ) return;

        byte[] newBuff = null;
        byte[] tmp = _buffer;

        try
        {
            if (this.getRemaining() > 0)
            {
                newBuff = new byte[_capacity];
                //newBuff = new byte[Math.Max(_capacity, Remaining)];

                System.arraycopy(_buffer, _offset, newBuff, 0, this.getRemaining());
            }

            _buffer = newBuff;
            _offset = 0;
        }
        catch (Exception ex)
        {
            _buffer = tmp;
        }
    }

    /// <summary>
    /// 지정된 위치부터 끝까지 데이터를 제거한다.
    /// </summary>
    /// <param name="offset">데이터 제거를 시작할 위치</param>
    public synchronized void remove(int offset)
    {
        // 예외성 제거
        if (_buffer == null ) return;
        if (_offset <= 0    ) return;
        if (offset  <= 0    ) return;

        byte[] tmp = _buffer;

        try
        {
            byte[] newBuff = new byte[offset];

            int count = Math.min(offset, _buffer.length);
            System.arraycopy(_buffer, 0, newBuff, 0, count);

            _buffer = newBuff;
            _offset = count;
        }
        catch (Exception ex)
        {
            _buffer = tmp;
        }

        this.notify();
    }

    public synchronized void clear()
    {
        Arrays.fill(_buffer, (byte)0);
        _buffer = null;

        if (_capacity > 0)
            _buffer = new byte[_capacity];

        _offset = 0;
    }

    public synchronized ByteBuffer clone()
    {
        ByteBuffer clone = new ByteBuffer(_capacity);
        clone.append(_buffer);
        clone.setOffset(_offset);

        return clone;
    }
/*
    public String toHexa()
    {
        // 예외성 제거
        if (_buffer         == null ) return "";
        if (_buffer.length  <= 0    ) return "";

        String hexa = "";

        this.wait();

        hexa = ByteConvert.ToHexa(_buffer);

        this.notify();

        return hexa;
    }
*/
    public static ByteBuffer join(ByteBuffer src1, ByteBuffer src2)
    {
        ByteBuffer buff = new ByteBuffer(src1.getLength() + src2.getLength());

        buff.write(0, src1, 0, src1.getLength());
        buff.write(src1.getLength(), src2, 0, src2.getLength());

        return buff;
    }

    public static ByteBuffer join(ByteBuffer src1, String src2)
    {
        ByteBuffer buffer = src1.clone();
        byte[] buff = src2.getBytes();
        buffer.append(buff);

        return buffer;
    }

    public static ByteBuffer join(ByteBuffer src1, char[] src2)
    {
        return ByteBuffer.join(src1, new String(src2));
    }

    public static ByteBuffer join(ByteBuffer src1, byte[] src2)
    {
        return ByteBuffer.join(src1, new ByteBuffer(src2));
    }
}