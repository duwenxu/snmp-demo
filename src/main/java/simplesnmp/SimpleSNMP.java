package simplesnmp;

import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;

/**
 * Created by yhy on 17-5-24.
 *
 * 简单的 SimpleSNMP 主要是为了全局初始化一个 snmp 对象监听 162 端口接收请求，且不可重用端口。
 *
 */
public class SimpleSNMP {
    /**
     * 真正做 snmp 客户端的类
     */
    static private Snmp snmp = null;



    /**
     * Create simplesnmp. listen port 1162 允许端口重入
     *
     * @throws IOException the io exception
     */
    public static void init() throws IOException {
        init(1162,true);
    }


    /**
     * init snmp
     *
     * @param listenPort   the listen port
     * @param reuseAddress
     *      if <code>true</code> addresses are reused which provides faster socket
     *      binding if an application is restarted for instance.
     * @throws IOException the io exception
     */
    public static void init(int listenPort, boolean reuseAddress) throws IOException {
        if( null != snmp ){
            throw new DoubleInitException("重复初始化 snmp");
        }
        //设定采取的协议为 udp
        UdpAddress udpAddress = new UdpAddress(listenPort);
        TransportMapping transport = new DefaultUdpTransportMapping(udpAddress,reuseAddress);
        if( null == transport ){
            throw new RuntimeException("初始化 DefaultUdpTransportMapping 失败");
        }
        //调用TransportMapping中的listen()方法，启动监听进程，接收消息
        transport.listen();
        snmp = new Snmp(transport);
    }

    public static Snmp getSnmp() {
        return snmp;
    }

}
