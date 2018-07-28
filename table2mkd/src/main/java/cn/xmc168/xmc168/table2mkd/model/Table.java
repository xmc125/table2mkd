package cn.xmc168.xmc168.table2mkd.model;


public class Table {
    
    private String name;
    
    private String rmk;
    
    private String type;

    
    public String getName() {
        return name;
    }

    
    public void setName(String name) {
        this.name = name;
    }

    
    public String getRmk() {
        return rmk;
    }

    
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }

    
    public String getType() {
        return type;
    }

    
    public void setType(String type) {
        this.type = type;
    }


    @Override
    public String toString() {
        return "Table [name=" + name + ", rmk=" + rmk + ", type=" + type + "]";
    }
    

}
