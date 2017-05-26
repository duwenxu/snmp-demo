package simplesnmp;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.VariableBinding;
import simplesnmp.DoubleInitException;
import simplesnmp.SimpleSNMP;
import simplesnmp.SimpleSNMPUtil;

import java.util.Vector;

/**
 * SimpleSNMP Tester.
 *
 * @author helloworldyu
 * @version 1.0
 * @since <pre>五月 26, 2017</pre>
 */
public class SimpleSNMPTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: init()
     */
    @Test
    public void testInit() throws Exception {
        SimpleSNMP.init();
    }

    @Test(expected = DoubleInitException.class)
    public void testdDoubleInit() throws Exception {
        SimpleSNMP.init(1162,true);
        SimpleSNMP.init(1162,true);
    }

    /**
     * Method: init(int listenPort, boolean reuseAddress)
     */
    @Test
    public void testInitForListenPortReuseAddress() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: getSnmp()
     */
    @Test
    public void testGetSnmp() throws Exception {
        //初始化 snmp 监听端口
        SimpleSNMP.init(1090, false);

        CommunityTarget communityTarget = SimpleSNMPUtil.createSimpleTarget(
                GenericAddress.parse("udp:61.175.164.174/161"),
                "public");


        PDU simlePDU = SimpleSNMPUtil.createSimlePDU("1.3.6.1.4.1.2011.10.2.75.2.1.1");

        //发起请求
        ResponseEvent responseEvent = SimpleSNMP.getSnmp().getNext(simlePDU, communityTarget);

        printResponse(responseEvent);
    }

    private void printResponse(ResponseEvent responseEvent) {
            System.out.println("----------解析Response-------------");

            if (null != responseEvent && null != responseEvent.getResponse()) {
                Vector<? extends VariableBinding> variableBindings = responseEvent.getResponse().getVariableBindings();
                for (int i = 0; i < variableBindings.size(); i++) {
                    VariableBinding recVB = variableBindings.elementAt(i);
                    System.out
                            .println(recVB.getOid() + " : " + recVB.getVariable());
                }
            }
        }


} 
