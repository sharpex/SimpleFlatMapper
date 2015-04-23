package org.sfm.csv.impl.primitive;

import org.sfm.csv.impl.DelayedCellSetter;
import org.sfm.csv.impl.ParsingContext;
import org.sfm.csv.impl.cellreader.ByteCellValueReader;
import org.sfm.reflect.primitive.ByteSetter;

public class ByteDelayedCellSetter<T> implements DelayedCellSetter<T, Byte> {

	private final ByteSetter<T> setter;
	private final ByteCellValueReader reader;
    private byte value;
    private boolean isNull;

	public ByteDelayedCellSetter(ByteSetter<T> setter, ByteCellValueReader reader) {
		this.setter = setter;
		this.reader = reader;
	}

	@Override
	public Byte consumeValue() {
		return isNull ? null : consumeByte();
	}

    @Override
    public Byte peekValue() {
        return isNull ? null : value;
    }

    public byte consumeByte() {
		byte v = value;
		value = 0;
        isNull = true;
		return v;
	}
	
	@Override
	public void set(T t) throws Exception {
		setter.setByte(t, consumeByte());
	}

	@Override
	public boolean isSettable() {
		return setter != null;
	}

	@Override
	public void set(CharSequence value, ParsingContext parsingContext) throws Exception {
        isNull = value.length() == 0;
		this.value = reader.readByte(value, parsingContext);
	}

    @Override
    public String toString() {
        return "ByteDelayedCellSetter{" +
                "setter=" + setter +
                ", reader=" + reader +
                '}';
    }
}
