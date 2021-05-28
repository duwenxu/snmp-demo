package snmp_exchange;

/**
*
* @Description: SNMP 对象调用 OID 枚举
* @Date 11:27 2021/5/19
* @Author duwx
*/

public class ConstantEnum {

    /*
     * snmpwalk是对OID值的遍历（比如某个OID值下面有N个节点，则依次遍历出这N个节点的值。如果对某个叶子节点的OID值做walk，则取得到数据就不正确了，因为它会认为该节点是某些节点的父节点，而对其进行遍历，而实际上该节点已经没有子节点了，那么它会取出与该叶子节点平级的下一个叶子节点的值，而不是当前请求的节子节点的值。）
     * snmpget是取具体的OID的值。（适用于OID值是一个叶子节点的情况）
     */
    public final static String SNMP_REQUEST_METHOD_GET = "GET";
    public final static String SNMP_REQUEST_METHOD_WALK = "WALK";
    public final static String SNMP_REQUEST_METHOD_SET = "SET";

    public final static String SNMP_SET_VALUE_TYPE_NULL = null;
    public final static String SNMP_SET_VALUE_TYPE_INTEGER = "Integer";//整型
    public final static String SNMP_SET_VALUE_TYPE_STRING = "OctetString";//字符串
    public final static String SNMP_SET_VALUE_TYPE_TIME = "TimeTicks";//是一个时间单位，表示以0.01s(即10ms)为单位计算的时间
    public final static String SNMP_SET_VALUE_TYPE_GAUGE = "Gauge";//非负整数，它可以递增或递减，但达到最大值时保持在最大值，最大值为2^32-1

    public static final String COMMUNITY_READ = "public";
    public static final String COMMUNITY_WRITE = "private";


    public enum MIBObject{

        //获取系统基本信息
        SysDesc("1.3.6.1.2.1.1.1.0",SNMP_REQUEST_METHOD_GET),
        //系统运行时间
        SysUptime("1.3.6.1.2.1.1.3.0",SNMP_REQUEST_METHOD_GET),
        //系统联系人
        SysContact("1.3.6.1.2.1.1.4.0",SNMP_REQUEST_METHOD_GET),
        //系统名称
        SysName("1.3.6.1.2.1.1.5.0",SNMP_REQUEST_METHOD_GET),
        //系统运行的进程列表
        HrSWRunName("1.3.6.1.2.1.25.4.2.1.2",SNMP_REQUEST_METHOD_WALK),
        //系统安装的软件列表
        HrSWInstalledName("1.3.6.1.2.1.25.6.3.1.2",SNMP_REQUEST_METHOD_WALK),

        //***********网络接口***********
        //获取所有接口mib
        IfIndex(".1.3.6.1.2.1.2.2.1.1",SNMP_REQUEST_METHOD_GET),
        //获取接口数量
        IfNumber(".1.3.6.1.2.1.2.1.0",SNMP_REQUEST_METHOD_GET),
        //网络接口信息描述
        IfDescr(".1.3.6.1.2.1.2.2.1.2",SNMP_REQUEST_METHOD_GET),
        //网络接口类型 (POE交换机接口类型)
        IfType(".1.3.6.1.2.1.2.2.1.3",SNMP_REQUEST_METHOD_GET),
        //获取接口max transmision unit(short 1518-9600)
        IfMTU(".1.3.6.1.2.1.2.2.1.4",SNMP_REQUEST_METHOD_GET),
        //接口的物理地址
        IfPhysAddress(".1.3.6.1.2.1.2.2.1.6",SNMP_REQUEST_METHOD_GET),
        //接口当前操作状态 INTEGER {up(1), down(2),testing(3), unknown(4), dormant(5),notPresent(6), lowerLayerDown(7) }
        IfOperStatus(".1.3.6.1.2.1.2.2.1.8",SNMP_REQUEST_METHOD_GET),
        //接口收到的字节数
        IfInOctet(".1.3.6.1.2.1.2.2.1.10",SNMP_REQUEST_METHOD_GET),
        //接口发送的字节数
        IfOutOctet(".1.3.6.1.2.1.2.2.1.16",SNMP_REQUEST_METHOD_GET),
        //接口带宽
        IfSpeed(".1.3.6.1.2.1.2.2.1.5",SNMP_REQUEST_METHOD_GET),

        //***********CPU负载***********
/*		//系统CPU百分比
		SsCpuSystem,
		//空闲CPU百分比
		SsCpuIdle,
		//系统CPU使用时间
		SsCpuRawSystem,
		//系统CPU空闲时间
		SsCpuRawIdle,
		//CPU的当前负载，N个核就有N个负载(CPU使用率=N个VALUE相加/N)
		HrProcessorLoad,

		//***********内存及磁盘***********
		//获取内存大小
		HrMemorySize,
		//存储设备编号
		HrStorageIndex,
		//存储设备类型
		HrStorageType,
		//存储设备描述
		HrStorageDescr,
		//簇的大小
		HrStorageAllocationUnits,
		//簇的的数目
		HrStorageSize,
		//使用多少，跟总容量相除就是占用率
		HrStorageUsed;*/

        //***********光网视交换机***********
        //交换机重启 (INTEGER) {noReboot(0), coldReboot(1), warmReboot(2) }
        VtssSysutilControlRebootType(".1.3.6.1.4.1.6603.1.24.1.4.1.1.2.1",SNMP_REQUEST_METHOD_SET),
        //获取交换机PORT (INTEGER) {force10ModeFdx(0),force10ModeHdx(1),force100ModeFdx(2),force100ModeHdx(3),force1GModeFdx(4),autoNegMode(5),force2G5ModeFdx(6),force5GModeFdx(7),force10GModeFdx(8),force12GModeFdx(9) }
        VtssPortConfigSpeed(".1.3.6.1.4.1.6603.1.11.1.2.1.1.3",SNMP_REQUEST_METHOD_GET),
        //获取交换机PORT TYPE (INTEGER) {rj45(0), sfp(1), dual(2) }
        VtssPortConfigMediaType(".1.3.6.1.4.1.6603.1.11.1.2.1.1.5",SNMP_REQUEST_METHOD_GET),
        //获取交换机max transmision unit(short 1518-9600)
        VtssPortConfigMTU(".1.3.6.1.4.1.6603.1.11.1.2.1.1.7",SNMP_REQUEST_METHOD_GET),
        //获取交换机 received (good and bad) bytes
        VtssPortStatisticsRmonStatisticsRxOctets(".1.3.6.1.4.1.6603.1.11.1.5.1.1.3",SNMP_REQUEST_METHOD_GET),
        //获取交换机 transmitted (good and bad) bytes
        VtssPortStatisticsRmonStatisticsTxOctets(".1.3.6.1.4.1.6603.1.11.1.5.1.1.3",SNMP_REQUEST_METHOD_GET),
        //获取交换机port link status (INTEGER) {true(1), false(2) }
        VtssPortStatusInformationLink(".1.3.6.1.4.1.6603.1.11.1.3.1.1.2",SNMP_REQUEST_METHOD_GET),
        //获取交换机current interface speed(INTEGER) {undefined(0), speed10M(1), speed100M(2),speed1G(3), speed2G5(4), speed5G(5),speed10G(6), speed12G(7) }
        VtssPortStatusInformationSpeed(".1.3.6.1.4.1.6603.1.11.1.3.1.1.5",SNMP_REQUEST_METHOD_GET),
        //获取交换机接口当前温度
        VtssThermalProtectionStatusInterfaceTemperature(".1.3.6.1.4.1.6603.1.78.1.3.1.1.2",SNMP_REQUEST_METHOD_GET),
        //获取交换机Average CPU load (%) in 100msec/10sec/1sec.
        VtssSysutilStatusCpuLoadAverage100msec("1.3.6.1.4.1.6603.1.24.1.3.1.1.0",SNMP_REQUEST_METHOD_GET),
        VtssSysutilStatusCpuLoadAverage10sec("1.3.6.1.4.1.6603.1.24.1.3.1.3.0",SNMP_REQUEST_METHOD_GET),
        VtssSysutilStatusCpuLoadAverage1sec("1.3.6.1.4.1.6603.1.24.1.3.1.2.0",SNMP_REQUEST_METHOD_GET),

        //获取接口当前POE功率(单位：十瓦特)
        VtssPoeStatusInterfacePowerConsumption(".1.3.6.1.4.1.6603.1.43.1.3.1.1.4",SNMP_REQUEST_METHOD_GET),
        //获取接口当前POE电流(单位：毫安)
        VtssPoeStatusInterfaceCurrentConsumption(".1.3.6.1.4.1.6603.1.43.1.3.1.1.5",SNMP_REQUEST_METHOD_GET),
        //获取接口当前POE供电状态(INTEGER) {notSupported(0), budgetExceeded(1), noPoweredDeviceDetected(2), poweredDeviceOn(3), poweredDeviceOff(4), poweredDeviceOverloaded(5), unknownState(6),disabled(7) }
        VtssPoeStatusInterfaceCurrentState(".1.3.6.1.4.1.6603.1.43.1.3.1.1.3",SNMP_REQUEST_METHOD_GET);
        private String oid;
        private String method;

        private MIBObject(String oid, String method) {
            this.oid = oid;
            this.method = method;
        }
        public String getOid() {
            return oid;
        }

        public void setOid(String oid) {
            this.oid = oid;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }
    }

}
