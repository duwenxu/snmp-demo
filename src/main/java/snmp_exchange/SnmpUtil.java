package snmp_exchange;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.event.ResponseListener;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static snmpdemo.SnmpData.*;

/**
 * Snmp核心工具类
 *
 * @author duwenxu
 * @create 2021-05-19 11:24
 */
public class SnmpUtil {

    private static final Logger log = LoggerFactory.getLogger(SnmpUtil.class);

    /*
     * SNMPv3
     * 需要创建USM对象并添加User
     * 需要创建UserTarget对象，而不是创建CommunityTarget对象
     * 需要创建ScopedPDU对象，而不是PDU对象
     */

    /**
     * @description:创建对象communityTarget
     * @param ip
     * @param community
     * @return
     */
    public static CommunityTarget createDefault(String ip, String community) {

//        //snmp 版本
//        int version = Integer.valueOf(PropertyUtil.getProperty("snmp_version2c"));
//        //snmp 端口
//        int port = Integer.valueOf(PropertyUtil.getProperty("snmp_switch_port"));
//        //snmp 协议
//        String protocol = PropertyUtil.getProperty("snmp_default_protocol");
//        //snmp 超时时间
//        long timeout = Long.valueOf(PropertyUtil.getProperty("snmp_default_timeout"));
//        //snmp 重试
//        int retry = Integer.valueOf(PropertyUtil.getProperty("snmp_default_retry"));
//        //创建地址
//        Address address = GenericAddress.parse(protocol + ":" + ip + "/" + port);
//        //创建CommunityTarget对象
//        CommunityTarget target = new CommunityTarget();
//        //设置社区名称
//        target.setCommunity(new OctetString(community));
//        //设置ip端口
//        target.setAddress(address);
//        //设置版本
//        target.setVersion(version);
//        //设置超时时间
//        target.setTimeout(timeout);
//        //设置retry
//        target.setRetries(retry);
//        return target;

        Address address = GenericAddress.parse(DEFAULT_PROTOCOL + ":" + ip
                + "/" + DEFAULT_PORT);
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString(community));
        target.setAddress(address);
        target.setVersion(DEFAULT_VERSION);
        target.setTimeout(DEFAULT_TIMEOUT); // milliseconds
        target.setRetries(DEFAULT_RETRY);
        return target;
    }

    /**
     * @description:GET方式获取单个OID的单个值
     * @param ip
     * @param community
     * @param oid
     */
    public static Map<String, String> snmpGetByOID(String ip, String community, String oid) {
        Map<String, String> map = new HashMap<String, String>();
        CommunityTarget target = createDefault(ip, community);
        Snmp snmp = null;
        try {
            PDU pdu = new PDU();
            pdu.add(new VariableBinding(new OID(oid)));
            DefaultUdpTransportMapping transport = new DefaultUdpTransportMapping();
            snmp = new Snmp(transport);
            snmp.listen();
            pdu.setType(PDU.GET);
            ResponseEvent respEvent = snmp.send(pdu, target);
            PDU response = respEvent.getResponse();
            if (response == null) {
                log.warn("response is null, request time out");
            } else {
                for (int i = 0; i < response.size(); i++) {
                    VariableBinding vb = response.get(i);
                    map.put(vb.getOid().toString(), vb.getVariable().toString());
                }
            }
        } catch (Exception e) {
            log.error("SNMP snmpGetByOID Exception:" + e);
        } finally {
            if (snmp != null) {
                try {
                    snmp.close();
                } catch (IOException ex1) {
                    snmp = null;
                }
            }
        }
        return map;
    }

    /**
     * @description:GET方式获取多个OID(返回list map)
     * @param ip
     * @param community
     * @param oidList
     * @return
     */
    public static List<Map<String, String>> snmpGetListMapByOIDs(String ip, String community, List<String> oidList) {
        List<Map<String, String>> list = new ArrayList<Map<String,String>>();
        CommunityTarget target = createDefault(ip, community);
        Snmp snmp = null;
        try {
            PDU pdu = new PDU();
            for (String oid : oidList) {
                pdu.add(new VariableBinding(new OID(oid)));
            }
            DefaultUdpTransportMapping transport = new DefaultUdpTransportMapping();
            snmp = new Snmp(transport);
            snmp.listen();
            pdu.setType(PDU.GET);
            ResponseEvent respEvent = snmp.send(pdu, target);
            PDU response = respEvent.getResponse();
            if (response == null) {
                log.warn("response is null, request time out");
            } else {
                for (int i = 0; i < response.size(); i++) {

                    Map<String, String> map = new HashMap<String, String>();
                    VariableBinding vb = response.get(i);
                    map.put(vb.getOid().toString(), vb.getVariable().toString());
                    list.add(map);
                }
            }
        } catch (Exception e) {
            log.error("SNMP snmpGetListMapByOIDs Exception:" + e);
        } finally {
            if (snmp != null) {
                try {
                    snmp.close();
                } catch (IOException ex1) {
                    snmp = null;
                }
            }
        }
        return list;
    }

    /**
     * @description:GET方式获取多个OID(返回list string)
     * @param ip
     * @param community
     * @param oidList
     * @return
     */
    public static List<String> snmpGetListByOIDs(String ip, String community,List<String> oidList) {
        List<String> list = new ArrayList<String>();
        CommunityTarget target = createDefault(ip, community);
        Snmp snmp = null;
        try {
            PDU pdu = new PDU();
            for (String oid : oidList) {
                pdu.add(new VariableBinding(new OID(oid)));
            }
            DefaultUdpTransportMapping transport = new DefaultUdpTransportMapping();
            snmp = new Snmp(transport);
            snmp.listen();
            pdu.setType(PDU.GET);
            ResponseEvent respEvent = snmp.send(pdu, target);
            PDU response = respEvent.getResponse();
            if (response == null) {
                log.warn("response is null, request time out");
            } else {
                for (int i = 0; i < response.size(); i++) {
                    VariableBinding vb = response.get(i);
                    list.add(vb.getVariable().toString());
                }
            }
        } catch (Exception e) {
            log.error("SNMP snmpGetListByOIDs Exception:" + e);
        } finally {
            if (snmp != null) {
                try {
                    snmp.close();
                } catch (IOException ex1) {
                    snmp = null;
                }
            }
        }
        return list;
    }

    /**
     * @description:异步GET多个OID
     * @param ip
     * @param community
     * @param oidList
     */
    public static List<Map<String, String>> snmpAsynGetListByOIDs(String ip, String community,List<String> oidList) {
        final List<Map<String, String>> list = new ArrayList<Map<String,String>>();
        CommunityTarget target = createDefault(ip, community);
        Snmp snmp = null;
        try {
            PDU pdu = new PDU();
            for (String oid : oidList) {
                pdu.add(new VariableBinding(new OID(oid)));
            }
            DefaultUdpTransportMapping transport = new DefaultUdpTransportMapping();
            snmp = new Snmp(transport);
            snmp.listen();
            pdu.setType(PDU.GET);
            /* 异步获取 */
            final CountDownLatch latch = new CountDownLatch(1);
            ResponseListener listener = new ResponseListener() {
                public void onResponse(ResponseEvent event) {
                    ((Snmp) event.getSource()).cancel(event.getRequest(), this);
                    PDU response = event.getResponse();
                    if (response == null) {
                        log.error("[ERROR]: response is null");
                    } else if (response.getErrorStatus() != 0) {
                        log.error("[ERROR]: response status"
                                + response.getErrorStatus() + " Text:"
                                + response.getErrorStatusText());
                    } else {
                        for (int i = 0; i < response.size(); i++) {
                            Map<String, String> map = new HashMap<String, String>();
                            VariableBinding vb = response.get(i);
                            map.put(vb.getOid().toString(), vb.getVariable().toString());
                            list.add(map);
                        }
                        latch.countDown();
                    }
                }
            };
            snmp.send(pdu, target, null, listener);
            latch.await(30, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("SNMP snmpAsynGetListByOIDs Exception:" + e);
        } finally {
            if (snmp != null) {
                try {
                    snmp.close();
                } catch (IOException ex1) {
                    snmp = null;
                }
            }
        }
        return list;
    }

    /* 根据targetOID，获取树形数据 */

    /**
     * @description: walk遍历子节点获取所有
     * @param ip
     * @param community
     * @param targetOid
     */
    public static List<Map<String, String>> snmpWalk(String ip, String community, String targetOid) {
        List<Map<String, String>> list = new ArrayList<Map<String,String>>();
        CommunityTarget target = createDefault(ip, community);
        TransportMapping transport = null;
        Snmp snmp = null;
        try {
            transport = new DefaultUdpTransportMapping();
            snmp = new Snmp(transport);
            transport.listen();
            PDU pdu = new PDU();
            OID targetOID = new OID(targetOid);
            pdu.add(new VariableBinding(targetOID));
            boolean finished = false;
            while (!finished) {
                VariableBinding vb = null;
                ResponseEvent respEvent = snmp.getNext(pdu, target);
                PDU response = respEvent.getResponse();
                if (response == null) {
                    log.warn("response is null, request time out");
                    finished = true;
                    break;
                } else {
                    vb = response.get(0);
                }
                //检查是否遍历完成，否则一直遍历
                finished = checkWalkFinished(targetOID, pdu, vb);
                if (!finished) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put(vb.getOid().toString(), vb.getVariable().toString());
                    list.add(map);
                    //为下一次遍历绑定参数
                    pdu.setRequestID(new Integer32(0));
                    pdu.set(0, vb);
                } else {
                    log.info("SNMP walk OID has finished.");
                }
            }
        } catch (Exception e) {
            log.error("SNMP snmpWalk Exception:" + e);
        } finally {
            if (snmp != null) {
                try {
                    snmp.close();
                } catch (IOException ex1) {
                    snmp = null;
                }
            }
        }
        return list;
    }

    /**
     * @description:检查walk遍历是否结束
     * @param targetOID
     * @param pdu
     * @param vb
     * @return
     */
    private static boolean checkWalkFinished(OID targetOID, PDU pdu,VariableBinding vb) {
        boolean finished = false;
        if (pdu.getErrorStatus() != 0) {
            log.info("[true] responsePDU.getErrorStatus() != 0 ");
            finished = true;
        } else if (vb.getOid() == null) {
            log.info("[true] vb.getOid() == null");
            finished = true;
        } else if (vb.getOid().size() < targetOID.size()) {
            log.info("[true] vb.getOid().size() < targetOID.size()");
            finished = true;
        } else if (targetOID.leftMostCompare(targetOID.size(), vb.getOid()) != 0) {
            log.info("[true] targetOID.leftMostCompare() != 0");
            finished = true;
        } else if (Null.isExceptionSyntax(vb.getVariable().getSyntax())) {
            log.info("[true] Null.isExceptionSyntax(vb.getVariable().getSyntax())");
            finished = true;
        } else if (vb.getOid().compareTo(targetOID) <= 0) {
            log.info("[true] Variable received is not lexicographic successor of requested one:");
            log.info(vb.toString() + " <= " + targetOID);
            finished = true;
        }
        return finished;

    }

    /* 根据targetOID，异步获取树形数据 */
    public static List<Map<String, String>> snmpAsynWalk(String ip, String community, String oid) {
        final List<Map<String, String>> list = new ArrayList<Map<String,String>>();
        final CommunityTarget target = createDefault(ip, community);
        Snmp snmp = null;
        try {
            DefaultUdpTransportMapping transport = new DefaultUdpTransportMapping();
            snmp = new Snmp(transport);
            snmp.listen();

            final PDU pdu = new PDU();
            final OID targetOID = new OID(oid);
            final CountDownLatch latch = new CountDownLatch(1);
            pdu.add(new VariableBinding(targetOID));

            ResponseListener listener = new ResponseListener() {
                public void onResponse(ResponseEvent event) {
                    ((Snmp) event.getSource()).cancel(event.getRequest(), this);
                    try {
                        PDU response = event.getResponse();
                        if (response == null) {
                            log.error("[ERROR]: response is null");
                        } else if (response.getErrorStatus() != 0) {
                            log.error("[ERROR]: response status"
                                    + response.getErrorStatus() + " Text:"
                                    + response.getErrorStatusText());
                        } else {
                            VariableBinding vb = response.get(0);
                            boolean finished = checkWalkFinished(targetOID,pdu, vb);
                            if (!finished) {
                                Map<String, String> map = new HashMap<String, String>();
                                map.put(vb.getOid().toString(), vb.getVariable().toString());
                                list.add(map);
                                //为下一次遍历绑定参数
                                pdu.setRequestID(new Integer32(0));
                                pdu.set(0, vb);
                                ((Snmp) event.getSource()).getNext(pdu, target, null, this);
                            } else {
                                log.error("SNMP Asyn walk OID value success !");
                                latch.countDown();
                            }
                        }
                    } catch (Exception e) {
                        log.error("SNMP snmpAsynWalk Exception:" + e);
                        latch.countDown();
                    }
                }
            };
            snmp.getNext(pdu, target, null, listener);
            boolean wait = latch.await(30, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("SNMP snmpAsynWalk Exception:" + e);
        } finally {
            if (snmp != null) {
                try {
                    snmp.close();
                } catch (IOException ex1) {
                    snmp = null;
                }
            }
        }
        return list;
    }

    /**
     * @description:     设置OID值
     * @param ip         主机IP
     * @param community  团体名称
     * @param oid	     OID
     * @param val        值
     * @param valueType  值类型
     * @throws IOException
     */
    public static boolean setPDUValue(String ip, String community, String oid,String val,String valueType) throws IOException {

        boolean flag = false;
        CommunityTarget target = createDefault(ip, community);
        Snmp snmp = null;
        PDU pdu = new PDU();

        if (ConstantEnum.SNMP_SET_VALUE_TYPE_INTEGER.equals(valueType)) {
            pdu.add(new VariableBinding(new OID(oid), new Integer32(Integer.parseInt(val))));
        } else if (ConstantEnum.SNMP_SET_VALUE_TYPE_STRING.equals(valueType)) {
            pdu.add(new VariableBinding(new OID(oid), new OctetString(val)));
        } else if (ConstantEnum.SNMP_SET_VALUE_TYPE_TIME.equals(valueType)) {
            pdu.add(new VariableBinding(new OID(oid), new TimeTicks(Long.parseLong(val))));
        } else if (ConstantEnum.SNMP_SET_VALUE_TYPE_GAUGE.equals(valueType)) {
            pdu.add(new VariableBinding(new OID(oid), new Gauge32(Long.parseLong(val))));
        }
        pdu.setType(PDU.SET);
        DefaultUdpTransportMapping transport = new DefaultUdpTransportMapping();
        snmp = new Snmp(transport);
        snmp.listen();

        ResponseEvent respEvent = snmp.send(pdu, target);
        PDU response = respEvent.getResponse();
        if (response == null) {
            log.warn("response is null, request time out");
        } else {
            flag = true;
        }
        snmp.close();
        return flag;
    }

}
