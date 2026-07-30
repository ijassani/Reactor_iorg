package com.alchemain.rx.http;

import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpRequestDecoder;

import java.util.concurrent.atomic.AtomicLong;

/**
 * HTTP request decoder that records requests containing both chunked transfer
 * encoding and a content length.
 *
 * <p>Netty detects this ambiguous framing while parsing the request. Delegating
 * to the parent implementation applies Netty's handling for the condition,
 * which removes the conflicting content-length header in Netty 4.1.94.Final.</p>
 */
public class TransferEncodingAwareHttpRequestDecoder extends HttpRequestDecoder {
	private final AtomicLong conflictingFramingHeaderCount = new AtomicLong();

	@Override
	protected void handleTransferEncodingChunkedWithContentLength(HttpMessage message) {
		conflictingFramingHeaderCount.incrementAndGet();
		super.handleTransferEncodingChunkedWithContentLength(message);
	}

	public long getConflictingFramingHeaderCount() {
		return conflictingFramingHeaderCount.get();
	}
}
