package org.sfm.map.impl;

import org.sfm.jdbc.JdbcMapper;
import org.sfm.jdbc.impl.ResultSetEnumarable;
import org.sfm.map.*;
import org.sfm.utils.Enumarable;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class JoinMapperImpl<T> extends AbstractEnumarableDelegateMapper<ResultSet, ResultSet, T, SQLException> implements JdbcMapper<T> {

    private final Mapper<ResultSet, T> mapper;
    private final MappingContextFactory<ResultSet> mappingContextFactory;

    public JoinMapperImpl(Mapper<ResultSet, T> mapper, RowHandlerErrorHandler errorHandler, MappingContextFactory<ResultSet> mappingContextFactory) {
        super(errorHandler);
        this.mapper = mapper;
        this.mappingContextFactory = mappingContextFactory;
    }

    @Override
    protected Mapper<ResultSet, T> getMapper(ResultSet source) {
        return mapper;
    }

    @Override
    protected Enumarable<T> newEnumarableOfT(ResultSet rs) throws SQLException {
        return new JoinEnumarable<ResultSet, T>(mapper, mappingContextFactory.newContext(), new ResultSetEnumarable(rs));
    }

    @Override
    public MappingContext<ResultSet> newMappingContext(ResultSet rs) {
        return mappingContextFactory.newContext();
    }

}
