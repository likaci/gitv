package com.gala.android.dlna.sdk.mediarenderer.service.infor;

public class PrivateServiceConstStr {
    public static final String A_ARG_TYPE_NOTIFYMSG = "A_ARG_TYPE_NOTIFYMSG";
    public static final String CONTROL_URL = "_urn:schemas-upnp-org:service:PrivateServer_control";
    public static final String EVENTSUB_URL = "_urn:schemas-upnp-org:service:PrivateServer_event";
    public static final String INFOR = "Infor";
    public static final String INSTANCE_ID = "InstanceID";
    public static final String NOTIFY_MESSAGE = "NotifyMessage";
    public static final String NOTIFY_MSG = "NotifyMsg";
    public static final String RESULT = "Result";
    public static final String SCPD = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><scpd xmlns=\"urn:schemas-upnp-org:service-1-0\"><specVersion> <major>1</major><minor>0</minor></specVersion><serviceStateTable><stateVariable sendEvents=\"no\"><name>A_ARG_TYPE_InstanceID</name><dataType>ui4</dataType></stateVariable><stateVariable sendEvents=\"yes\"><name>A_ARG_TYPE_NOTIFYMSG</name><dataType>string</dataType></stateVariable><stateVariable sendEvents=\"no\"><name>A_ARG_TYPE_INFOR</name><dataType>string</dataType></stateVariable><stateVariable sendEvents=\"no\"><name>A_ARG_TYPE_SendMessage_Result</name><dataType>string</dataType></stateVariable></serviceStateTable><actionList><action><name>SendMessage</name><argumentList><argument><name>InstanceID</name><direction>in</direction><relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable></argument><argument><name>Infor</name><direction>in</direction><relatedStateVariable>A_ARG_TYPE_INFOR</relatedStateVariable></argument><argument><name>Result</name><direction>out</direction><relatedStateVariable>A_ARG_TYPE_SendMessage_Result</relatedStateVariable></argument></argumentList></action><action><name>NotifyMessage</name><argumentList><argument><name>NotifyMsg</name><direction>in</direction><relatedStateVariable>A_ARG_TYPE_NOTIFYMSG</relatedStateVariable></argument></argumentList></action></actionList></scpd>";
    public static final String SCPDURL = "_urn:schemas-upnp-org:service:PrivateServer_scpd.xml";
    public static final String SEND_MESSAGE = "SendMessage";
    public static final String SERVICE_ID = "urn:upnp-org:serviceId:PrivateServer";
    public static final String SERVICE_NAME = "PrivateServer";
    public static final String SERVICE_TYPE = "urn:schemas-upnp-org:service:PrivateServer:1";
}
