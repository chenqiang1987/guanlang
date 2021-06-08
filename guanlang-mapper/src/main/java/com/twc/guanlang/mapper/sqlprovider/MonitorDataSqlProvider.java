package com.twc.guanlang.mapper.sqlprovider;


public class MonitorDataSqlProvider {


    public String selectLatestDataTimeStamp() {

        return "SELECT\n" +
                "\tmax(mdata.`timeStamp`) maxTime,\n" +
                "\tmch.serial_number serialNumber\n" +
                "FROM\n" +
                "\tt_monitor_data mdata\n" +
                "RIGHT JOIN t_machine mch ON mdata.serialNumber = mch.serial_number\n" +
                "AND mch.`enable` = 1\n" +
                "WHERE\n" +
                "\tmch.`status` = 1\n" +
                "GROUP BY\n" +
                "\tserialNumber  ";
    }


}

