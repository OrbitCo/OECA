package gov.epa.oeca.common.infrastructure.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.hibernate.HibernateException;
import org.hibernate.annotations.common.util.ReflectHelper;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Objects;
import java.util.Properties;

/**
 * @author dfladung
 */
public class JsonBlobType implements UserType, ParameterizedType, Serializable {

    private static final long serialVersionUID = 1L;

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String CLASS_TYPE = "classType";
    private static final String TYPE = "type";
    private static final int[] SQL_TYPES = new int[]{Types.BLOB};
    private int sqlType = SQL_TYPES[0];
    private Class<?> classType;

    static {
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public void setParameterValues(Properties params) {
        String classTypeName = params.getProperty(CLASS_TYPE);
        try {
            this.classType = ReflectHelper.classForName(classTypeName, this.getClass());
        } catch (ClassNotFoundException cnfe) {
            throw new HibernateException("classType not found", cnfe);
        }
        String type = params.getProperty(TYPE);
        if (type != null) {
            this.sqlType = Integer.decode(type);
        }
    }

    @Override
    public int[] sqlTypes() {
        return SQL_TYPES;
    }

    @Override
    public Class returnedClass() {
        return this.classType;
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        return Objects.equals(x, y);
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return Objects.hashCode(x);
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner) throws HibernateException, SQLException {
        Object obj = null;
        if (this.sqlType == Types.BLOB) {
            InputStream is = rs.getBlob(names[0]).getBinaryStream();
            try {
                byte[] bytes = IOUtils.toByteArray(is);
                if (bytes != null) {
                    obj = MAPPER.readValue(bytes, this.classType);
                }
            } catch (IOException e) {
                throw new HibernateException("unable to read bytes from blob", e);
            }
        } else {
            throw new HibernateException("this type is not supported " + this.sqlType);
        }
        return obj;
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session) throws HibernateException, SQLException {
        if (value == null) {
            st.setNull(index, this.sqlType);
        } else {
            if (this.sqlType == Types.BLOB) {
                try {
                    st.setBytes(index, MAPPER.writeValueAsBytes(value));
                } catch (JsonProcessingException e) {
                    throw new HibernateException("unable to set object to result set", e);
                }
            } else {
                throw new HibernateException("this type is not supported " + this.sqlType);
            }
        }
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        if (value != null) {
            try {
                return MAPPER.readValue(MAPPER.writeValueAsString(value), this.classType);
            } catch (IOException e) {
                throw new HibernateException("unable to deep copy object", e);
            }
        } else {
            return null;
        }
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        try {
            return MAPPER.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new HibernateException("unable to disassemble object", e);
        }
    }

    @Override
    public Object assemble(Serializable cached, Object o) throws HibernateException {
        return this.deepCopy(cached);
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return this.deepCopy(original);
    }
}
