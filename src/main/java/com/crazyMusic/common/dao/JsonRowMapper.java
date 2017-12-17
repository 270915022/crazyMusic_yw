package com.crazyMusic.common.dao;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.crazyMusic.common.constant.Const;

/**
 * <B>系统名称�?</B>通用系统功能<BR>
 * <B>模块名称�?</B>数据访问通用功能<BR>
 * <B>中文类名�?</B>JSON数据行映射器<BR>
 * <B>概要说明�?</B><BR>
 */
public class JsonRowMapper implements RowMapper<JSONObject> {

    /**
     * <B>方法名称�?</B>映射行数�?<BR>
     * <B>概要说明�?</B><BR>
     * 
     * @param rs 结果�?
     * @param row 行号
     * @return JSONObject 数据
     * @throws SQLException SQL异常错误
     * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet,
     *      int)
     */
    public JSONObject mapRow(ResultSet rs, int row) throws SQLException {
        String key = null;
        Object obj = null;
        JSONObject json = new JSONObject();
        ResultSetMetaData rsmd = rs.getMetaData();
        int count = rsmd.getColumnCount();
        for (int i = 1; i <= count; i++) {
            key = JdbcUtils.lookupColumnName(rsmd, i);
            obj = JdbcUtils.getResultSetValue(rs, i);
            try {
            	if(obj != null && obj instanceof Timestamp) {
            		obj = DateFormatUtils.format(((Timestamp)obj).getTime(), Const.FORMAT_DATETIME);
            	}
                json.put(key, obj);
            }
            catch (JSONException e) {
            }
        }
        return json;
    }
}
