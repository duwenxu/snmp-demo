package simplesnmp;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.smi.VariableBinding;

import java.util.List;
import java.util.Vector;

/**
 * SimpleSNMP Tester.
 *
 * @author helloworldyu
 * @version 1.0
 * @since <pre>六月 3, 2017</pre>
 */
public class SimpleSNMPTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: get(String ip, String community, String oid)
     */
    @Test
    public void testGet() throws Exception {
        SimpleSNMP simpleSNMP = new SimpleSNMP();


        String ip = "127.0.0.1";
        String oid = ".1.3.6.1.4.1.2011.10.2.6.1.1.1.1.6.97";
        String responseSring = simpleSNMP.get(ip, "public", oid);

        printGetResult(oid, responseSring);
    }

    private void printGetResult(String oid, String responseSring) {
        System.out.println(String.format("get获取 oid=%s,value=%s", oid, responseSring));
    }

    /**
     * Method: getNext(String ip, String community, String oid)
     */
    @Test
    public void testGetNext() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: walk(String ip, String community, String oid)
     */
    @Test
    public void testWalkForIpCommunityOid() throws Exception {
        SimpleSNMP simpleSNMP = new SimpleSNMP();
        String ip = "61.175.164.174";
        String oid = "1.3.6.1.4.1.2011.10.2.75.4.2.2.1.2";
        List<String> responseList = simpleSNMP.walk(ip, "public", oid);


        printWalkResult(oid, responseList);

//        for (ResponseEvent responseEvent : responseEvents) {
//            printResponse(responseEvent);
//        }
    }

    private void printWalkResult(String oid, List<String> responseList) {
        System.out.println(String.format("walk获取oid=%s", oid));
        for (String responseSring : responseList) {
            System.out.println(String.format("value=%s", responseSring));
        }
    }

    /**
     * Method: walk(PDU pdu, CommunityTarget target)
     */
    @Test
    public void testWalkForPduTarget() throws Exception {
//TODO: Test goes here... 
    }

    private void printResponse(ResponseEvent responseEvent) {
        System.out.println("----------解析Response-------------");

        if (null != responseEvent && null != responseEvent.getResponse()) {
            List<? extends VariableBinding> bindings = responseEvent.getResponse().getVariableBindings();
            for (int i = 0; i < bindings.size(); i++) {
                VariableBinding recVB = bindings.get(i);
                System.out.println(recVB.getOid() + " : " + recVB.getVariable().toString());
            }
        }
    }

} 
