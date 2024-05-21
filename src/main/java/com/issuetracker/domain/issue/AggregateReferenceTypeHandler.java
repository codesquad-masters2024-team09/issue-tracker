package com.issuetracker.domain.issue;

import com.issuetracker.domain.milestone.Milestone;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.springframework.data.jdbc.core.mapping.AggregateReference;

public class AggregateReferenceTypeHandler extends BaseTypeHandler<AggregateReference<Milestone, String>> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, AggregateReference<Milestone, String> parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.getId());
    }

    @Override
    public AggregateReference<Milestone, String> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String id = rs.getString(columnName);
        return id != null ? AggregateReference.to(id) : null;
    }

    @Override
    public AggregateReference<Milestone, String> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String id = rs.getString(columnIndex);
        return id != null ? AggregateReference.to(id) : null;
    }

    @Override
    public AggregateReference<Milestone, String> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String id = cs.getString(columnIndex);
        return id != null ? AggregateReference.to(id) : null;
    }
}
