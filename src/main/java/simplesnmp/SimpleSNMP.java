package simplesnmp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.Null;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yhy on 17-5-24.
 * <p>
 * 简单的 SimpleSNMP。
 */
public class SimpleSNMP {


    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleSNMP.class);
    /**
     * 简单的使用 udp 的随机端口发送 snmp get 请求
     *
     * @param ip
     * @param community
     * @param oid
     * @return
     * @throws IOException
     */
    public ResponseEvent get(String ip, String community, String oid) throws IOException {
        String address = String.format("udp:%s/161", ip);
        CommunityTarget communityTarget = SimpleSNMPUtil.createSimpleTarget(
                GenericAddress.parse(address),
                community);


        PDU simlePDU = SimpleSNMPUtil.createSimlePDU(oid);

        Snmp snmp = initSnmp();
        return snmp.get(simlePDU, communityTarget);
    }

    /**
     * snmp getnext 获取数据
     *
     * @param ip
     * @param community
     * @param oid
     * @return
     * @throws IOException
     */
    public ResponseEvent getNext(String ip, String community, String oid) throws IOException {
        String address = String.format("udp:%s/161", ip);
        CommunityTarget communityTarget = SimpleSNMPUtil.createSimpleTarget(
                GenericAddress.parse(address),
                community);


        PDU simlePDU = SimpleSNMPUtil.createSimlePDU(oid);

        Snmp snmp = initSnmp();
        return snmp.getNext(simlePDU, communityTarget);
    }

    /**
     * 通过 walk 的方式来获取数据
     *
     * @param ip
     * @param community
     * @param oid
     * @return
     * @throws IOException
     */
    public List<ResponseEvent> walk(String ip, String community, String oid) throws IOException {
        String address = String.format("udp:%s/161", ip);
        CommunityTarget communityTarget = SimpleSNMPUtil.createSimpleTarget(
                GenericAddress.parse(address),
                community);

        boolean finished = false;   //是否已经完成了walk
        List<ResponseEvent> responseEvents = new ArrayList<>(); //结果集

        Snmp snmp = initSnmp();

        //要 getnext 的目录。
        VariableBinding vb = new VariableBinding(new OID(oid));
        PDU pdu = new PDU();

        //========== 循环去 getNext ==============
        do {
            //添加要 getNext 的目录。
            pdu.clear();
            pdu.add(vb);

            ResponseEvent responseEvent = snmp.getNext(pdu, communityTarget);
            PDU responsePDU = responseEvent.getResponse();

            //
            if (null == responsePDU) {
                break;
            } else {
                vb = responsePDU.get(0);
            }
            //-----------查看是否采集完毕----------
            finished = checkWalkFinished(new OID(oid), pdu, vb);
            //最后一个非此根下的节点要抛弃
            if( !finished ){
                responseEvents.add(responseEvent);
            }
        } while (!finished);

        return responseEvents;
    }


    private Snmp initSnmp() throws IOException {
        //设定采取的协议为 udp
        TransportMapping transport = new DefaultUdpTransportMapping();
        if( null == transport ){
            throw new RuntimeException("DefaultUdpTransportMapping 初始化失败");
        }
        Snmp snmp = new Snmp(transport);
        //调用TransportMapping中的listen()方法，启动监听进程，接收消息
        transport.listen();
        return snmp;
    }

    /**
     * 1)responsePDU == null<br>
     * 2)responsePDU.getErrorStatus() != 0<br>
     * 3)responsePDU.get(0).getOid() == null<br>
     * 4)responsePDU.get(0).getOid().size() < targetOID.size()<br>
     * 5)targetOID.leftMostCompare(targetOID.size(),responsePDU.get(0).getOid())
     * !=0<br>
     * 6)Null.isExceptionSyntax(responsePDU.get(0).getVariable().getSyntax())<br>
     * 7)responsePDU.get(0).getOid().compareTo(targetOID) <= 0<br>
     *
     * @param targetOID
     * @param responsePDU
     * @param vb
     * @return 是否完成
     */
    private static boolean checkWalkFinished(OID targetOID, PDU responsePDU,
                                             VariableBinding vb) {
        String logPrefix = "walk fished!!!!, Because:";
        boolean finished = false;
        if (responsePDU.getErrorStatus() != 0) {
            finished = true;
            LOGGER.debug(logPrefix + "responsePDU.getErrorStatus() != 0");
        } else if (vb.getOid() == null) {
            finished = true;
            LOGGER.debug(logPrefix + "vb.getOid() == null");
        } else if (vb.getOid().size() < targetOID.size()) {
            finished = true;
            LOGGER.debug(logPrefix + "vb.getOid().size() < targetOID.size()");
        } else if (targetOID.leftMostCompare(targetOID.size(), vb.getOid()) != 0) {
            finished = true;
            LOGGER.debug(logPrefix + "targetOID.leftMostCompare() != 0" );
        } else if (Null.isExceptionSyntax(vb.getVariable().getSyntax())) {
            finished = true;
            LOGGER.debug(logPrefix + "Null.isExceptionSyntax(vb.getVariable().getSyntax())");
        } else if (vb.getOid().compareTo(targetOID) <= 0) {
            System.out.println("[true] Variable received is not "
                    + "lexicographic successor of requested " + "one:");
            System.out.println(vb.toString() + " <= " + targetOID);
            finished = true;
            LOGGER.debug("{} {}<={}",logPrefix,vb.getOid().toString(),targetOID.toString());
        }
        return finished;

    }
}
