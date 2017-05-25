package simplesnmp;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;

/**
 * Created by yhy on 17-5-26.
 */
public class SimpleSNMPUtil {

    /**
     * @param address 设置远程目标地址
     * @param community 设置团体名
     * @return 一个初始化好的 CommunityTarget
     */
    public static CommunityTarget createSimpleTarget(
            final Address address,
            final String community
    ) {
        return createSimpleTarget(SnmpConstants.version2c, address, community, 2,200 );
    }

    /**
     * @param version 设置版本号  SnmpConstants.version1,SnmpConstants.version2c,SnmpConstants.version3
     * @param address 设置远程目标地址
     * @param community 设置团体名
     * @param retries 设置重试次数
     * @param timeout 设置超时时间
     * @return 一个初始化好的 CommunityTarget
     */
    public static CommunityTarget createSimpleTarget(
            final int version,
            final Address address,
            final String community,
            final int retries,
            final int timeout
    ){
        CommunityTarget communityTarget =  new CommunityTarget();
        //设置版本号
        communityTarget.setVersion(version);
        //设置远程目标地址
        communityTarget.setAddress(address);
        //设置团体名
        communityTarget.setCommunity(new OctetString(community));
        //设置重试次数
        communityTarget.setRetries(retries);
        //设置超时时间
        communityTarget.setTimeout(timeout);

        return communityTarget;
    }


    /**
     *
     * @param oid 访问的 oid
     */
    public static PDU createSimlePDU(String oid ){
        PDU pdu = new PDU();
        //调用的add方法绑定要查询/设置的OID
        pdu.add(new VariableBinding(new OID(oid)));

        return pdu;
    }
}
