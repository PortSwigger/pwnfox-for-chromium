package com.adeadfed;

import static com.adeadfed.common.Constants.*;

import burp.api.montoya.core.HighlightColor;
import burp.api.montoya.proxy.http.InterceptedRequest;
import burp.api.montoya.proxy.http.ProxyRequestHandler;
import burp.api.montoya.proxy.http.ProxyRequestReceivedAction;
import burp.api.montoya.proxy.http.ProxyRequestToBeSentAction;

public class PwnFoxForChromiumRequestHandler implements ProxyRequestHandler {
    @Override
    public ProxyRequestReceivedAction handleRequestReceived(InterceptedRequest interceptedRequest) {
        if (interceptedRequest.hasHeader(PWNFOX_HEADER)) {
            String tabColor = interceptedRequest.headerValue(PWNFOX_HEADER);
            interceptedRequest.annotations().setHighlightColor(
                    HighlightColor.highlightColor(tabColor)
            );
        }
        return null;
    }

    @Override
    public ProxyRequestToBeSentAction handleRequestToBeSent(InterceptedRequest interceptedRequest) {
        return null;
    }
}
