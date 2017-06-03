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
* @since <pre>六月 3, 2017</pre> 
* @version 1.0 
*/ 
public class SimpleSNMPTest { 

@Before
public void before() throws Exception { 
} 

@After
public void after() throws Exception { 
} 

/** 
* 
* Method: get(String ip, String community, String oid) 
* 
*/ 
@Test
public void testGet() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: getNext(String ip, String community, String oid) 
* 
*/ 
@Test
public void testGetNext() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: walk(String ip, String community, String oid) 
* 
*/ 
@Test
public void testWalkForIpCommunityOid() throws Exception { 
    SimpleSNMP simpleSNMP = new SimpleSNMP();
    List<ResponseEvent> responseEvents = simpleSNMP.walk("61.175.164.174", "public", "1.3.6.1.4.1.2011.10.2.75.4.2.2.1.2");

    for( ResponseEvent responseEvent : responseEvents ){
        printResponse(responseEvent);
    }
}

/** 
* 
* Method: walk(PDU pdu, CommunityTarget target) 
* 
*/ 
@Test
public void testWalkForPduTarget() throws Exception {
//TODO: Test goes here... 
}

    private void printResponse(ResponseEvent responseEvent) {
        System.out.println("----------解析Response-------------");

        if (null != responseEvent && null != responseEvent.getResponse()) {
            Vector<? extends VariableBinding> variableBindings = responseEvent.getResponse().getVariableBindings();
            for (int i = 0; i < variableBindings.size(); i++) {
                VariableBinding recVB = variableBindings.elementAt(i);
                System.out
                        .println(recVB.getOid() + " : " + recVB.getVariable().toString());
            }
        }
    }

} 
