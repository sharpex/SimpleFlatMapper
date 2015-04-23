package org.sfm.csv.impl.cellreader;

import org.sfm.csv.CellValueReader;
import org.sfm.csv.impl.ParsingContext;

public interface DoubleCellValueReader extends CellValueReader<Double> {
    double readDouble(CharSequence value, ParsingContext parsingContext);
}
