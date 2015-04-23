package org.sfm.csv.impl.primitive;

import org.sfm.csv.impl.DelayedCellSetter;
import org.sfm.csv.impl.ParsingContext;
import org.sfm.csv.impl.cellreader.DoubleCellValueReader;
import org.sfm.reflect.primitive.DoubleSetter;

public class DoubleDelayedCellSetter<T> implements DelayedCellSetter<T, Double> {

	private final DoubleSetter<T> setter;
	private final DoubleCellValueReader reader;
	private double value;
    private boolean isNull;

	public DoubleDelayedCellSetter(DoubleSetter<T> setter, DoubleCellValueReader reader) {
		this.setter = setter;
		this.reader = reader;
	}

	@Override
	public Double consumeValue() {
		return isNull ? null : consumeDouble();
	}

    @Override
    public Double peekValue() {
        return isNull ? null : value;
    }

    public double consumeDouble() {
		double v = value;
		value = 0;
        isNull = true;
		return v;
	}
	
	@Override
	public void set(T t) throws Exception {
		setter.setDouble(t, consumeValue());
	}

	@Override
	public boolean isSettable() {
		return setter != null;
	}

	@Override
	public void set(CharSequence value, ParsingContext parsingContext) throws Exception {
        isNull = value.length() == 0;
		this.value = reader.readDouble(value, parsingContext);
	}

    @Override
    public String toString() {
        return "DoubleDelayedCellSetter{" +
                "setter=" + setter +
                ", reader=" + reader +
                '}';
    }
}
