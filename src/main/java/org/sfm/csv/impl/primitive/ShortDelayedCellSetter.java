package org.sfm.csv.impl.primitive;

import org.sfm.csv.impl.DelayedCellSetter;
import org.sfm.csv.impl.ParsingContext;
import org.sfm.csv.impl.cellreader.ShortCellValueReader;
import org.sfm.reflect.primitive.ShortSetter;

public class ShortDelayedCellSetter<T> implements DelayedCellSetter<T, Short> {

	private final ShortSetter<T> setter;
	private final ShortCellValueReader reader;
	private short value;
    private boolean isNull;

	public ShortDelayedCellSetter(ShortSetter<T> setter, ShortCellValueReader reader) {
		this.setter = setter;
		this.reader = reader;
	}

	@Override
	public Short consumeValue() {
        return isNull ? null : getShort();
	}

    public Short peekValue() {
        return isNull ? null : value;
    }

	public short getShort() {
		short v = value;
		value = 0;
        isNull = true;
		return v;
	}
	
	@Override
	public void set(T t) throws Exception {
		setter.setShort(t, consumeValue());
	}

	@Override
	public boolean isSettable() {
		return setter != null;
	}

	@Override
	public void set(CharSequence value, ParsingContext parsingContext) throws Exception {
        isNull = value.length() ==0;
        this.value = reader.readShort(value, parsingContext);
	}

    @Override
    public String toString() {
        return "ShortDelayedCellSetter{" +
                "setter=" + setter +
                ", reader=" + reader +
                '}';
    }
}
