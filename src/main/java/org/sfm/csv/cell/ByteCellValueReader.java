package org.sfm.csv.cell;

import org.sfm.csv.CellValueReader;

public class ByteCellValueReader implements CellValueReader<Byte> {

	@Override
	public Byte read(char[] chars, int offset, int length) {
		return new Byte((byte)IntegerCellValueReader.parseInt(chars, offset, length));
	}
}
