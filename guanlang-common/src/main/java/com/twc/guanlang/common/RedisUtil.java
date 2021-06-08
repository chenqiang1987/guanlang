package com.twc.guanlang.common;

import java.util.HashMap;
import java.util.Map;

public class RedisUtil {


    /*
    用户id与chatid对应关系
     */
    private Map user2ChatId = new HashMap();

    {

        user2ChatId.put("a","{\"type\":\"offer\",\"sdp\":\"v=0\\r\\no=- 4317439792905769574 2 IN IP4 127.0.0.1\\r\\ns=-\\r\\nt=0 0\\r\\na=group:BUNDLE 0\\r\\na=msid-semantic: WMS\\r\\nm=application 9 UDP/DTLS/SCTP webrtc-datachannel\\r\\nc=IN IP4 0.0.0.0\\r\\na=candidate:3687888086 1 udp 2113937151 e644b6a8-5992-4ef0-81e8-d6fe2e27e481.local 50079 typ host generation 0 network-cost 999\\r\\na=ice-ufrag:uzco\\r\\na=ice-pwd:8cipud0lCxVp7LWDE8ZaUR9F\\r\\na=ice-options:trickle\\r\\na=fingerprint:sha-256 5A:80:EE:11:78:6B:36:90:29:CC:F2:22:5C:14:CE:56:15:DE:27:46:5B:B9:3C:D7:B3:CA:B0:48:E4:2A:20:F7\\r\\na=setup:actpass\\r\\na=mid:0\\r\\na=sctp-port:5000\\r\\na=max-message-size:262144\\r\\n\"}");
        user2ChatId.put("b","{\"type\":\"offer\",\"sdp\":\"v=0\\r\\no=- 4317439792905769574 2 IN IP4 127.0.0.1\\r\\ns=-\\r\\nt=0 0\\r\\na=group:BUNDLE 0\\r\\na=msid-semantic: WMS\\r\\nm=application 9 UDP/DTLS/SCTP webrtc-datachannel\\r\\nc=IN IP4 0.0.0.0\\r\\na=candidate:3687888086 1 udp 2113937151 e644b6a8-5992-4ef0-81e8-d6fe2e27e481.local 50079 typ host generation 0 network-cost 999\\r\\na=ice-ufrag:uzco\\r\\na=ice-pwd:8cipud0lCxVp7LWDE8ZaUR9F\\r\\na=ice-options:trickle\\r\\na=fingerprint:sha-256 5A:80:EE:11:78:6B:36:90:29:CC:F2:22:5C:14:CE:56:15:DE:27:46:5B:B9:3C:D7:B3:CA:B0:48:E4:2A:20:F7\\r\\na=setup:actpass\\r\\na=mid:0\\r\\na=sctp-port:5000\\r\\na=max-message-size:262144\\r\\n\"}");

    }
}
