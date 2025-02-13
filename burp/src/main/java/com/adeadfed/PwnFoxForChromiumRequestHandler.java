package com.adeadfed;

import burp.api.montoya.core.HighlightColor;
import burp.api.montoya.proxy.http.InterceptedRequest;
import burp.api.montoya.proxy.http.ProxyRequestHandler;
import burp.api.montoya.proxy.http.ProxyRequestReceivedAction;
import burp.api.montoya.proxy.http.ProxyRequestToBeSentAction;

public class PwnFoxForChromiumRequestHandler implements ProxyRequestHandler {
    private String pwnFoxHeader = "X-Pwnfox-Color";

    @Override
    public ProxyRequestReceivedAction handleRequestReceived(InterceptedRequest interceptedRequest) {
        if (interceptedRequest.hasHeader(pwnFoxHeader)) {
            String tabColor = interceptedRequest.headerValue(pwnFoxHeader);
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
