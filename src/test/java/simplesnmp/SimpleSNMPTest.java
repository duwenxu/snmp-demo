package simplesnmp;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.VariableBinding;

import java.io.IOException;
import java.util.Vector;


/**
 * SimpleSNMP Tester.
 *
 * @author helloworldyu
 * @version 1.0
 * @since <pre>五月 25, 2017</pre>
 */
public class SimpleSNMPTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: createSimpleTarget(final int version, final Address address, final String community, final int retries, final int timeout)
     */
    @Test
    public void testCreateSimpleTarget() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: init(int listenPort)
     */
    @Test
    public void testCreateSimpleSnmp() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: createSimlePDU(int type, String oid)
     */
    @Test
    public void testCreateSimlePDU() throws Exception {
//TODO: Test goes here...
    }

    @Test
    public void testSimleSNMPGet() throws IOException {

        //初始化 snmp 监听端口
        SimpleSNMP.getInstance().init(1090, false);

        CommunityTarget communityTarget = SimpleSNMPUtil.createSimpleTarget(
                GenericAddress.parse("udp:61.175.164.174/161"),
                "public");


        PDU simlePDU = SimpleSNMPUtil.createSimlePDU("1.3.6.1.4.1.2011.10.2.75.2.1.1");
        ResponseEvent responseEvent = SimpleSNMP.getInstance().getSnmp().getNext(simlePDU, communityTarget);

        printResponse(responseEvent);
    }

    private void printResponse( ResponseEvent responseEvent) {
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
