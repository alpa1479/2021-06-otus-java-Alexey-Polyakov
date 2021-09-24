package ru.otus.hw09.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {

    private static final String COMMA_DELIMITER = ",";
    private static final String QUESTION_MARK = "?";
    private static final String COMMA_AND_SPACE_DELIMITER = ", ";
    private static final String UPDATE_SYNTAX_DELIMITER = " = ?";

    private final EntityClassMetaData<?> entityClassMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData<?> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public String getSelectAllSql() {
        return String.format("select * from %s", entityClassMetaData.getName());
    }

    @Override
    public String getSelectByIdSql() {
        return String.format("select * from %s where %s = ?",
                entityClassMetaData.getName(),
                entityClassMetaData.getIdField().getName());
    }

    @Override
    public String getInsertSql() {
        final List<Field> fieldsWithoutId = entityClassMetaData.getFieldsWithoutId();
        return String.format("insert into %s(%s) values (%s)",
                entityClassMetaData.getName(),
                getPreparedForInsertFieldNames(fieldsWithoutId),
                getPreparedForInsertFieldValues(fieldsWithoutId)
        );
    }

    @Override
    public String getUpdateSql() {
        return String.format("update %s set %s where %s = ?",
                entityClassMetaData.getName(),
                getPreparedForUpdateFieldNames(entityClassMetaData.getFieldsWithoutId()),
                entityClassMetaData.getIdField().getName()
        );
    }

    private String getPreparedForInsertFieldNames(List<Field> fields) {
        return fields.stream()
                .map(Field::getName)
                .collect(Collectors.joining(COMMA_DELIMITER));
    }

    private String getPreparedForInsertFieldValues(List<Field> fields) {
        StringJoiner joiner = new StringJoiner(COMMA_AND_SPACE_DELIMITER);
        for (int index = 0, fieldsSize = fields.size(); index < fieldsSize; index++) {
            joiner.add(QUESTION_MARK);
        }
        return joiner.toString();
    }

    private String getPreparedForUpdateFieldNames(List<Field> fields) {
        return fields.stream()
                .map(field -> field.getName() + UPDATE_SYNTAX_DELIMITER)
                .collect(Collectors.joining(COMMA_AND_SPACE_DELIMITER));
    }
}
