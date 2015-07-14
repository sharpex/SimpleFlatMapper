package org.sfm.map;

import org.sfm.jdbc.JdbcColumnKey;
import org.sfm.jdbc.JdbcMapper;
import org.sfm.jdbc.JdbcMappingContextFactoryBuilder;
import org.sfm.jdbc.impl.*;
import org.sfm.jdbc.impl.getter.ResultSetGetterFactory;
import org.sfm.map.column.ColumnProperty;
import org.sfm.map.impl.*;
import org.sfm.map.impl.context.MappingContextFactoryBuilder;
import org.sfm.reflect.ReflectionService;
import org.sfm.reflect.TypeReference;
import org.sfm.reflect.meta.ClassMeta;

import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * @param <T> the targeted type of the jdbcMapper
 */
public final class AbstractMapperBuilder<T> {

    public static final MapperSourceImpl<ResultSet, JdbcColumnKey> FIELD_MAPPER_SOURCE =
            new MapperSourceImpl<ResultSet, JdbcColumnKey>(ResultSet.class, new ResultSetGetterFactory());

    private final FieldMapperMapperBuilder<ResultSet, T, JdbcColumnKey> fieldMapperMapperBuilder;

    private final MapperConfig<JdbcColumnKey, FieldMapperColumnDefinition<JdbcColumnKey, ResultSet>> mapperConfig;
    private final MappingContextFactoryBuilder<ResultSet, JdbcColumnKey> mappingContextFactoryBuilder;

    private int calculatedIndex = 1;

    /**
     * Build a new JdbcMapperBuilder targeting the type specified by the TypeReference. The TypeReference
     * allow you to provide a generic type with check of T<br>
     * <code>new TypeReference&lt;List&lt;String&gt;&gt;() {}</code>
     *
     * @param target the TypeReference to the type T to map to
     */
    public AbstractMapperBuilder(final TypeReference<T> target) {
        this(target.getType());
    }

    /**
     * Build a new JdbcMapperBuilder targeting the specified type.
     *
     * @param target the type
     */
    public AbstractMapperBuilder(final Type target) {
        this(target, ReflectionService.newInstance());
    }

    /**
     * Build a new JdbcMapperBuilder targeting the specified type with the specified ReflectionService.
     *
     * @param target         the type
     * @param reflectService the ReflectionService
     */
    public AbstractMapperBuilder(final Type target, ReflectionService reflectService) {
        this(reflectService.<T>getClassMeta(target),
                MapperConfig.<ResultSet, JdbcColumnKey>fieldMapperConfig(),
                new ResultSetGetterFactory(),
                new JdbcMappingContextFactoryBuilder());
    }

    /**
     * @param classMeta                  the meta for the target class.
     * @param mapperConfig               the mapperConfig.
     * @param getterFactory              the Getter factory.
     * @param parentBuilder              the parent builder, null if none.
     */
    public AbstractMapperBuilder(
            final ClassMeta<T> classMeta,
            MapperConfig<JdbcColumnKey, FieldMapperColumnDefinition<JdbcColumnKey, ResultSet>> mapperConfig,
            GetterFactory<ResultSet, JdbcColumnKey> getterFactory,
            MappingContextFactoryBuilder<ResultSet, JdbcColumnKey> parentBuilder) {
        this.fieldMapperMapperBuilder =
                new FieldMapperMapperBuilder<ResultSet, T, JdbcColumnKey>(
                        FIELD_MAPPER_SOURCE.getterFactory(getterFactory),
                        classMeta,
                        mapperConfig,
                        parentBuilder
                );
        this.mapperConfig = mapperConfig;
        this.mappingContextFactoryBuilder = parentBuilder;
    }

    /**
     * @return a new instance of the jdbcMapper based on the current state of the builder.
     */
    public JdbcMapper<T> mapper() {
        Mapper<ResultSet, T> mapper = fieldMapperMapperBuilder.mapper();

        if (fieldMapperMapperBuilder.hasJoin()) {
            return new JoinJdbcMapper<T>(mapper, mapperConfig.rowHandlerErrorHandler(), mappingContextFactoryBuilder.newFactory());
        } else {
            return new StaticJdbcMapper<T>(mapper, mapperConfig.rowHandlerErrorHandler(), mappingContextFactoryBuilder.newFactory());
        }
    }

    /**
     * add a new mapping to the specified column with a key column definition and an undefined type.
     * The index is incremented for each non indexed column mapping.
     *
     * @param column the column name
     * @return the current builder
     */
    public AbstractMapperBuilder<T> addKey(String column) {
        return addMapping(column, calculatedIndex++, FieldMapperColumnDefinition.<JdbcColumnKey, ResultSet>key());
    }

    /**
     * add a new mapping to the specified column with an undefined type. The index is incremented for each non indexed column mapping.
     *
     * @param column the column name
     * @return the current builder
     */
    public AbstractMapperBuilder<T> addMapping(String column) {
        return addMapping(column, calculatedIndex++);
    }

    /**
     * add a new mapping to the specified column with the specified columnDefinition and an undefined type. The index is incremented for each non indexed column mapping.
     *
     * @param column           the column name
     * @param columnDefinition the definition
     * @return the current builder
     */
    public AbstractMapperBuilder<T> addMapping(final String column, final FieldMapperColumnDefinition<JdbcColumnKey, ResultSet> columnDefinition) {
        return addMapping(column, calculatedIndex++, columnDefinition);
    }

    /**
     * add a new mapping to the specified column with the specified columnDefinition and an undefined type. The index is incremented for each non indexed column mapping.
     *
     * @param column           the column name
     * @param properties the definition
     * @return the current builder
     */
    public AbstractMapperBuilder<T> addMapping(final String column, final ColumnProperty... properties) {
        return addMapping(column, calculatedIndex++, properties);
    }

    /**
     * add a new mapping to the specified column with the specified index and an undefined type.
     *
     * @param column the column name
     * @param index  the column index
     * @return the current builder
     */
    public AbstractMapperBuilder<T> addMapping(String column, int index) {
        return addMapping(column, index, JdbcColumnKey.UNDEFINED_TYPE);
    }

    /**
     * add a new mapping to the specified column with the specified index, specified column definition and an undefined type.
     *
     * @param column           the column name
     * @param index            the column index
     * @param columnDefinition the column definition
     * @return the current builder
     */
    public AbstractMapperBuilder<T> addMapping(String column, int index, final FieldMapperColumnDefinition<JdbcColumnKey, ResultSet> columnDefinition) {
        return addMapping(column, index, JdbcColumnKey.UNDEFINED_TYPE, columnDefinition);
    }

    /**
     * add a new mapping to the specified column with the specified index, specified column definition and an undefined type.
     *
     * @param column           the column name
     * @param index            the column index
     * @param properties the column properties
     * @return the current builder
     */
    public AbstractMapperBuilder<T> addMapping(String column, int index, final ColumnProperty... properties) {
        return addMapping(column, index, JdbcColumnKey.UNDEFINED_TYPE, properties);
    }

    /**
     * append a FieldMapper to the mapping list.
     *
     * @param mapper the field jdbcMapper
     * @return the current builder
     */
    public AbstractMapperBuilder<T> addMapper(FieldMapper<ResultSet, T> mapper) {
        fieldMapperMapperBuilder.addMapper(mapper);
        return this;
    }

    /**
     * add a new mapping to the specified column with the specified index and the specified type.
     *
     * @param column  the column name
     * @param index   the column index
     * @param sqlType the column type, @see java.sql.Types
     * @return the current builder
     */
    public AbstractMapperBuilder<T> addMapping(final String column, final int index, final int sqlType) {
        addMapping(column, index, sqlType, FieldMapperColumnDefinition.<JdbcColumnKey, ResultSet>identity());
        return this;
    }

    /**
     * add a new mapping to the specified column with the specified index,  the specified type.
     *
     * @param column           the column name
     * @param index            the column index
     * @param sqlType          the column type, @see java.sql.Types
     * @param columnDefinition the column definition
     * @return the current builder
     */
    public AbstractMapperBuilder<T> addMapping(final String column, final int index, final int sqlType, FieldMapperColumnDefinition<JdbcColumnKey, ResultSet> columnDefinition) {
        return addMapping(new JdbcColumnKey(column, index, sqlType), columnDefinition);
    }

    /**
     * add a new mapping to the specified column with the specified index,  the specified type.
     *
     * @param column           the column name
     * @param index            the column index
     * @param sqlType          the column type, @see java.sql.Types
     * @param properties the column properties
     * @return the current builder
     */
    public AbstractMapperBuilder<T> addMapping(final String column, final int index, final int sqlType, ColumnProperty... properties) {
        return addMapping(new JdbcColumnKey(column, index, sqlType), properties);
    }

    /**
     * add the all the column present in the metaData
     *
     * @param metaData the metaDAta
     * @return the current builder
     * @throws SQLException when an error occurs getting the metaData
     */
    public AbstractMapperBuilder<T> addMapping(final ResultSetMetaData metaData) throws SQLException {
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            addMapping(metaData.getColumnLabel(i), i, metaData.getColumnType(i));
        }

        return this;
    }

    public AbstractMapperBuilder<T> addMapping(JdbcColumnKey key, FieldMapperColumnDefinition<JdbcColumnKey, ResultSet> columnDefinition) {
        fieldMapperMapperBuilder.addMapping(key, columnDefinition);
        return this;
    }

    public AbstractMapperBuilder<T> addMapping(JdbcColumnKey key, ColumnProperty... properties) {
        fieldMapperMapperBuilder.addMapping(key, FieldMapperColumnDefinition.<JdbcColumnKey, ResultSet>of(properties));
        return this;
    }
}