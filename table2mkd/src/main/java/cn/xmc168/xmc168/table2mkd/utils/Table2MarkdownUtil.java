package cn.xmc168.xmc168.table2mkd.utils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cn.xmc168.xmc168.table2mkd.model.Table;

/**
 * 
 * 数据库转markDown
 * 
 * @version 
 * <pre>
 * Author	Version		Date		Changes
 * xumch 	1.0  		2018年7月27日 	Created
 *
 * </pre>
 * @since 1.
 */
public class Table2MarkdownUtil {
    private String dbName;
    
    private String filePath;
    
    private DatabaseMetaData dbMetaData = null;
    
    private Connection con = null;
    
    public Table2MarkdownUtil(String dbName, String filePath){
        this.setDbName(dbName);
        this.setFilePath(filePath);
    }
    
    public void execute() {
        getDatabaseMetaData();
        getDataBaseInformations();
        //getTableHead();
        List<Table> tables = getAllTableList("by_park");
        StringBuffer sb = new StringBuffer();
        tables.forEach(t->{
                sb.append("==============================================================================================\r");
                sb.append(getTableHead(t.getName(), t.getRmk()));
                getTableColumns(null, t.getName(), sb);
        });
        System.out.println(sb.toString());
    }
    /**
     * 获取mete对象
     */
    private void getDatabaseMetaData() {
        try {
            if (dbMetaData == null) {
                Class.forName(DBPropertiesUtil.getDbDriver());
                con = DriverManager.getConnection(DBPropertiesUtil.getUrl(), DBPropertiesUtil.getUser(), DBPropertiesUtil.getPassword());
                dbMetaData = con.getMetaData();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 打印数据库基本信息
     */
    public void getDataBaseInformations() {
        try {
            System.out.println("数据库已知的用户: " + dbMetaData.getUserName());
            System.out.println("数据库的系统函数的逗号分隔列表: " + dbMetaData.getSystemFunctions());
            System.out.println("数据库的时间和日期函数的逗号分隔列表: " + dbMetaData.getTimeDateFunctions());
            System.out.println("数据库的字符串函数的逗号分隔列表: " + dbMetaData.getStringFunctions());
            System.out.println("数据库供应商用于 'schema' 的首选术语: " + dbMetaData.getSchemaTerm());
            System.out.println("数据库URL: " + dbMetaData.getURL());
            System.out.println("是否允许只读:" + dbMetaData.isReadOnly());
            System.out.println("数据库的产品名称:" + dbMetaData.getDatabaseProductName());
            System.out.println("数据库的版本:" + dbMetaData.getDatabaseProductVersion());
            System.out.println("驱动程序的名称:" + dbMetaData.getDriverName());
            System.out.println("驱动程序的版本:" + dbMetaData.getDriverVersion());
 
            System.out.println();
            System.out.println("数据库中使用的表类型");
            ResultSet rs = dbMetaData.getTableTypes();
            while (rs.next()) {
                System.out.println(rs.getString(1));
            }
            rs.close();
            System.out.println();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private  List<Table> getAllTableList(String schemaName) {
        List<Table> tables = new ArrayList<>();
        try {
            // table type. Typical types are "TABLE", "VIEW", "SYSTEM TABLE",
            // "GLOBAL TEMPORARY", "LOCAL TEMPORARY", "ALIAS", "SYNONYM".
            String[] types = { "TABLE" };
            ResultSet rs = dbMetaData.getTables(null, schemaName, "%", types);
            
            while (rs.next()) {   
                
                String tableCat = rs.getString("TABLE_CAT");
                if(!dbName.equals(tableCat)) {
                    continue;
                }
                String tableName = rs.getString("TABLE_NAME"); // 表名
                String tableType = rs.getString("TABLE_TYPE"); // 表类型
                String remarks = rs.getString("REMARKS"); // 表备注
                
                System.out.println(tableName + "-" + tableType + "-" + remarks +"  tableCat="+tableCat); 
                Table table = new Table();
                table.setName(tableName);
                table.setType(tableType);
                table.setRmk(remarks);
                tables.add(table);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tables;
    }
    
    
    private String getTableHead(String tableName, String rmk) {
        String head = "**" + rmk + "(" + tableName + ")** \r\r";
        head +=  "|字段|类型|空|默认|注释| \r "
                + "|:----|:-------|:--- |---|------| \r";
        return head;
    }
    
    
    public static void main(String[] args) {
        Table2MarkdownUtil t2m = new Table2MarkdownUtil("by_park", "by_park");
        t2m.execute();
    }
    
    
    public void getTableColumns(String schemaName, String tableName, StringBuffer sb) {
        
        try{  
                
            ResultSet rs = dbMetaData.getColumns(null, schemaName, tableName, "%");           
            while (rs.next()){
                    String tableCat = rs.getString("TABLE_CAT");//表目录（可能为空）               
                    String tableSchemaName = rs.getString("TABLE_SCHEM");//表的架构（可能为空）  
                    String tableName_ = rs.getString("TABLE_NAME");//表名
                    String columnName = rs.getString("COLUMN_NAME");//列名
                    int dataType = rs.getInt("DATA_TYPE"); //对应的java.sql.Types类型  
                    String dataTypeName = rs.getString("TYPE_NAME");//java.sql.Types类型   名称
                    int columnSize = rs.getInt("COLUMN_SIZE");//列大小
                    int decimalDigits = rs.getInt("DECIMAL_DIGITS");//小数位数
                    int numPrecRadix = rs.getInt("NUM_PREC_RADIX");//基数（通常是10或2）
                    int nullAble = rs.getInt("NULLABLE");//是否允许为null
                    String remarks = rs.getString("REMARKS");//列描述
                    String columnDef = rs.getString("COLUMN_DEF");//默认值
                    int sqlDataType = rs.getInt("SQL_DATA_TYPE");//sql数据类型
                    int sqlDatetimeSub = rs.getInt("SQL_DATETIME_SUB");   //SQL日期时间分?
                    int charOctetLength = rs.getInt("CHAR_OCTET_LENGTH");   //char类型的列中的最大字节数
                    int ordinalPosition = rs.getInt("ORDINAL_POSITION");  //表中列的索引（从1开始）
                     
                    /**
                     * ISO规则用来确定某一列的为空性。
                     * 是---如果该参数可以包括空值;
                     * 无---如果参数不能包含空值
                     * 空字符串---如果参数为空性是未知的
                     */
                    String isNullAble = rs.getString("IS_NULLABLE");
                     
                    /**
                     * 指示此列是否是自动递增
                     * 是---如果该列是自动递增
                     * 无---如果不是自动递增列
                     * 空字串---如果不能确定它是否
                     * 列是自动递增的参数是未知
                     */
                    String isAutoincrement = rs.getString("IS_AUTOINCREMENT");  
                     // "|字段|类型|空|默认|注释| \r "
                    sb.append("| ").append(columnName).append("  |  ").append(dataTypeName+"("+columnSize+")");
                    sb.append("  |  ").append(nullAble==0?"否":"是").append("   |  ").append(columnDef).append("  |  ").append(remarks==null||"".equals(remarks)?"无":remarks).append("  | \r");
                    
                    
                    System.out.println(tableCat + "-" + tableSchemaName + "-" + tableName_ + "-" + columnName + "-" 
                            + dataType + "-" + dataTypeName + "-" + columnSize + "-" + decimalDigits + "-" + numPrecRadix  
                            + "-" + nullAble + "-" + remarks + "-" + columnDef + "-" + sqlDataType + "-" + sqlDatetimeSub  
                            + charOctetLength + "-" + ordinalPosition + "-" + isNullAble + "-" + isAutoincrement + "-");  
                }  
            } catch (SQLException e){
                e.printStackTrace();  
            }
    }
    
    

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    
    
    
    
}
