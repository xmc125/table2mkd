package cn.xmc168.xmc168.table2mkd;

import cn.xmc168.xmc168.table2mkd.utils.Table2MarkdownUtil;

/**
 * Hello world!
 *
 */
public class App 
{
    
    private static final String COMMAD_TABLE_TO_MKD="2mkd";
    private static final String COMMAD_MKD2TABLE_TO_TABLE="2tab";
    public static void main( String[] args )
    {
        String cmd = args[0];
        String dbName = args[1];
        String filePath = args[2];
        if(COMMAD_TABLE_TO_MKD.equals(cmd)) {
            //2mkd
            Table2MarkdownUtil tool = new Table2MarkdownUtil(dbName, filePath);
            tool.execute();
            
        }else if(COMMAD_MKD2TABLE_TO_TABLE.equals(cmd)) {
            //2tab
        }
    }
}
