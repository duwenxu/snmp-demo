package simplesnmp;

import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;

/**
 * Created by yhy on 17-5-24.
 *
 * 简单的 SimpleSNMP 单实例类。默认监听 162 端口接收请求，且不可重用端口。
 *
 */
public class SimpleSNMP {

    private SimpleSNMP(){

    }

    private static volatile SimpleSNMP instance = null;

    private Snmp snmp = null;


    /**
     * Gets instance.
     *
     * @return the instance
     */
    static SimpleSNMP getInstance() {
        // 对象实例化时与否判断（不使用同步代码块，instance不等于null时，直接返回对象，提高运行效率）
        if (instance == null) {
            //同步代码块（对象未初始化时，使用同步代码块，保证多线程访问时对象在第一次创建后，不再重复被创建）
            synchronized (SimpleSNMP.class) {
                //未初始化，则初始instance变量
                if (instance == null) {
                    instance = new SimpleSNMP();
                }
            }
        }
        return instance;
    }

    /**
     * Create simplesnmp. listen poort 162
     *
     * @throws IOException the io exception
     */
    public void init() throws IOException {
        init(162,false);
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
    public void init(int listenPort, boolean reuseAddress) throws IOException {
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

    public Snmp getSnmp() {
        return snmp;
    }

}
