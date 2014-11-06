package com.avaje.ebeaninternal.server.type;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.SQLException;

import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;

import com.avaje.ebean.config.ScalarTypeConverter;

/**
 * A ScalarType that uses a ScalarTypeConverter to convert to and from another
 * underlying ScalarType.
 * <p>
 * Enables the use of a simple interface to add additional scalarTypes.
 * </p>
 * 
 * @author rbygrave
 * 
 * @param <B>
 *            the logical type
 * @param <S>
 *            the underlying scalar type this is converted to
 */
public class ScalarTypeWrapper<B, S> implements ScalarType<B> {

    private final ScalarType<S> scalarType;
    private final ScalarTypeConverter<B, S> converter;
    private final Class<B> wrapperType;

    private final B nullValue;
    
    public ScalarTypeWrapper(Class<B> wrapperType, ScalarType<S> scalarType, ScalarTypeConverter<B, S> converter) {
        this.scalarType = scalarType;
        this.converter = converter;
        this.nullValue = converter.getNullValue();
        this.wrapperType = wrapperType;
    }
    
    public String toString() {
        return "ScalarTypeWrapper " + wrapperType + " to " + scalarType.getType();
    }
    
    @Override
    public boolean isMutable() {
      return scalarType.isMutable();
    }
    
    @Override
    public boolean isDirty(Object value) {
      return scalarType.isDirty(value);
    }

    @SuppressWarnings("unchecked")
    public Object readData(DataInput dataInput) throws IOException {
        Object v = scalarType.readData(dataInput);
        return converter.wrapValue((S)v);
    }

    @SuppressWarnings("unchecked")
    public void writeData(DataOutput dataOutput, Object v) throws IOException {
        S sv = converter.unwrapValue((B)v);
        scalarType.writeData(dataOutput, sv);
    }

    public void bind(DataBind b, B value) throws SQLException {
        if (value == null) {
            scalarType.bind(b, null);
        } else {
            S sv = converter.unwrapValue(value);
            scalarType.bind(b, sv);
        }
    }

    public int getJdbcType() {
        return scalarType.getJdbcType();
    }

    public int getLength() {
        return scalarType.getLength();
    }

    public Class<B> getType() {
        return wrapperType;
    }

    public boolean isDateTimeCapable() {
        return scalarType.isDateTimeCapable();
    }

    public boolean isJdbcNative() {
        return false;
    }

    @SuppressWarnings("unchecked")
    public String format(Object v) {
        return formatValue((B)v);
    }

    public String formatValue(B v) {
        S sv = converter.unwrapValue(v);
        return scalarType.formatValue(sv);
    }

    public B parse(String value) {
        S sv = scalarType.parse(value);
        if (sv == null) {
            return nullValue;
        }
        return converter.wrapValue(sv);
    }

    public B parseDateTime(long systemTimeMillis) {
        S sv = scalarType.parseDateTime(systemTimeMillis);
        if (sv == null) {
            return nullValue;
        }
        return converter.wrapValue(sv);
    }

    public void loadIgnore(DataReader dataReader) {
        dataReader.incrementPos(1);
    }

    public B read(DataReader dataReader) throws SQLException {

        S sv = scalarType.read(dataReader);
        if (sv == null) {
            return nullValue;
        }
        return converter.wrapValue(sv);
    }

    @SuppressWarnings("unchecked")
    public B toBeanType(Object value) {
        if (value == null) {
            return nullValue;
        }
        if (getType().isAssignableFrom(value.getClass())) {
            return (B) value;
        }
        if (value instanceof String) {
            return parse((String) value);
        }
        S sv = scalarType.toBeanType(value);
        return converter.wrapValue(sv);
    }

    @SuppressWarnings("unchecked")
    public Object toJdbcType(Object value) {

        Object sv = converter.unwrapValue((B) value);
        if (sv == null) {
            return nullValue;
        }
        return scalarType.toJdbcType(sv);
    }

    public void accumulateScalarTypes(String propName, CtCompoundTypeScalarList list) {
        list.addScalarType(propName, this);
    }

    public ScalarType<?> getScalarType() {
        return this;
    }

  @SuppressWarnings("unchecked")
  @Override
  public Object jsonRead(JsonParser ctx, Event event) {
    Object object = scalarType.jsonRead(ctx, event);
    return converter.wrapValue((S)object);
  }

  @Override
  public void jsonWrite(JsonGenerator ctx, String name, Object beanValue) {
    @SuppressWarnings("unchecked")
    S unwrapValue = converter.unwrapValue((B)beanValue);
    scalarType.jsonWrite(ctx, name, unwrapValue);
  }
  
  
}
